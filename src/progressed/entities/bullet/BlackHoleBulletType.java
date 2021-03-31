package progressed.entities.bullet;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import progressed.content.*;
import progressed.graphics.*;
import progressed.util.*;

public class BlackHoleBulletType extends BulletType{
    public float maxHealthReduction = 1f, bulletAbsorbPercent = 0.05f, hitSizeScl = 0.02f, damageIncreasePercent = 0.005f, strengthIncreasePercent = 0.005f;
    public Effect absorbEffect = PMFx.blackHoleAbsorb, swirlEffect = PMFx.blackHoleSwirl;
    public float cataclysmRadius = 20f * 8f;
    public float cataclysmForceMul = 5f, cataclysmBulletForceMul = 5f, cataclysmForceRange = 40f * 8f;
    public float suctionRadius = 96f, size = 5f, damageRadius = 32f;
    public float force = 15f, scaledForce = 160f, bulletForce = 0.1f, bulletScaledForce = 2f;
    public float swirlSize = 4f;
    public boolean repel;

    public BlackHoleBulletType(float speed, float damage){
        super(speed, damage);
        hittable = absorbable = false;
        collides = collidesAir = collidesGround = collidesTiles = false;
        pierce = true;
        shootEffect = smokeEffect = Fx.none;
        despawnEffect = PMFx.blackHoleDespawn;
    }

    @Override
    public void init(Bullet b){
        b.data = new BlackHoleData(){{
            sR = suctionRadius;
            s = size;
            dR = damageRadius;
            sS = swirlSize;
            f = force;
            sF = scaledForce;
            bF = bulletForce;
            bSF = bulletScaledForce;
            cFR = cataclysmForceRange;
        }};
        super.init(b);
    }

    @Override
    public float continuousDamage(){
        return damage / 2f * 60f; //Damage every 2 ticks
    }

    @Override
    public void update(Bullet b){
        if(b.timer(1, 2f)){
            BlackHoleData data = (BlackHoleData)b.data;

            Damage.damage(b.team, b.x, b.y, data.dR, b.damage);
            
            if(swirlEffect != Fx.none && b.time <= b.lifetime - swirlEffect.lifetime){
                swirlEffect.at(b.x, b.y, Mathf.random(360f), b);
            }

            Units.nearbyEnemies(b.team, b.x - data.sR, b.y - data.sR, data.sR * 2f, data.sR * 2f, unit -> {
                if(unit.within(b.x, b.y, data.sR)){
                    float deadForce = unit.dead ? 5f : 1f;
                    Vec2 impulse = Tmp.v1.set(b).sub(unit).limit(((data.f * deadForce) + (1f - unit.dst(b) / data.sR) * (data.sF * deadForce)) * Time.delta);
                    if(repel) impulse.rotate(180f);
                    unit.impulseNet(impulse);

                    if(unit.within(b.x, b.y, data.s)){
                        float scl = (unit.dst(b) / data.s) * (unit.type.hitSize * hitSizeScl);
                        b.damage += b.damage * scl * damageIncreasePercent;
                        unit.maxHealth -= scl * maxHealthReduction;
                        
                        unit.clampHealth();
                        if(unit.maxHealth < 0f) unit.kill();

                        data.powerIncrease(scl * strengthIncreasePercent);
                    }
                }
            });

            Groups.bullet.intersect(b.x - data.sR, b.y - data.sR, data.sR * 2f, data.sR * 2f, other -> {
                if(other != null && Mathf.within(b.x, b.y, other.x, other.y, data.sR) && b != other && b.team != other.team && other.type.speed > 0.01f && !checkType(other.type)){
                    Vec2 impulse = Tmp.v1.set(b).sub(other).limit((data.bF + (1f - other.dst(b) / data.sR) * data.bSF) * Time.delta);
                    if(repel) impulse.rotate(180f);
                    other.vel().add(impulse);

                    //manually move units to simulate velocity for remote players
                    if(b.isRemote()){
                        other.move(impulse.x, impulse.y);
                    }

                    if(Mathf.within(b.x, b.y, other.x, other.y, data.s * 2f)){
                        if(other.type instanceof BlackHoleBulletType type){
                            float radius = (cataclysmRadius + type.cataclysmRadius) / 2f;
                            if(radius > 0){ //Do not create negative radius cataclysms. I have no idea what this would cause anyways.
                                float thisUMul = cataclysmForceMul * Mathf.sign(!repel), thisBMul = cataclysmBulletForceMul * Mathf.sign(!repel);
                                float otherUMul = type.cataclysmForceMul * Mathf.sign(!type.repel), otherBMul = type.cataclysmBulletForceMul * Mathf.sign(!type.repel);

                                BlackHoleData oData = (BlackHoleData)other.data;

                                CataclysmData cData = new CataclysmData(){{
                                    r = radius;
                                    f = (data.f * thisUMul + oData.f * otherUMul) / 2f;
                                    sF = (data.sF * thisUMul + oData.sF * otherUMul) / 2f;
                                    bF = (data.bF * thisBMul + oData.bF * otherBMul) / 2f;
                                    bSF = (data.bSF * thisBMul + oData.bSF * otherBMul) / 2f;
                                    rg = (data.cFR + oData.cFR) / 2f;
                                    c1 = b.team.color;
                                    c2 = other.team.color;
                                }};

                                float midX = (b.x + other.x) / 2f;
                                float midY = (b.y + other.y) / 2f;

                                Effect.shake(radius / 1.5f, radius * 1.5f, midX, midY);
                                PMBullets.cataclysm.create(b.owner, b.team, midX, midY, 0f, 0f, 1f, 1f, cData);
                                absorbBullet(b, other, true);
                            }
                        }else{
                            absorbBullet(b, other, false);
                        }
                    }
                }
            });
        }

        super.update(b);
    }

    @Override
    public void draw(Bullet b){
        Draw.z(Layer.max - 0.01f);
        Fill.light(b.x, b.y, 60, ((BlackHoleData)b.data).s,
            b.team.color.cpy().lerp(Color.black, 0.5f + Mathf.absin(Time.time + Mathf.randomSeed(b.id), 10f, 0.4f)), Color.black);
    }

    @Override
    public void drawLight(Bullet b){
        Drawf.light(b.team, b, lightRadius, b.team.color, lightOpacity);
    }

    @Override
    public void despawned(Bullet b){
        despawnEffect.at(b.x, b.y, b.rotation(), b.team.color);

        hitSound.at(b);

        Effect.shake(despawnShake, despawnShake, b);

        if(!b.hit && (fragBullet != null || splashDamageRadius > 0f || lightning > 0)){
            hit(b);
        }
    }

    public boolean checkType(BulletType type){ //Returns true for bullets immune to suction.
        return (type instanceof StrikeBulletType) ||
            (type instanceof UnitSpawnStrikeBulletType) ||
            (type instanceof ParticleBulletType) ||
            (type instanceof BlackHoleCataclysmType);
    }

    public void absorbBullet(Bullet b, Bullet other, boolean cataclysm){
        if(!cataclysm){
            if(absorbEffect != Fx.none) absorbEffect.at(other.x, other.y);
            b.damage += PMUtls.bulletDamage(other.type, other.type.lifetime) * other.damageMultiplier() * bulletAbsorbPercent;
        }
        other.type = PMBullets.absorbed;
        other.absorb();
        if(cataclysm){
            b.type = PMBullets.absorbed;
            b.absorb();
        }
    }

    public class BlackHoleData{
        //suctionRadius, size, damageRadius, swirlSize, force, scaledForce, bulletForce, bulletScaledForce
        public float sR, s, dR, sS, f, sF, bF, bSF, cFR;

        public BlackHoleData(){}

        public void powerIncrease(float amount){
            sR += sR * amount;
            s += s * amount;
            dR += dR * amount;
            sS += sS * amount;
            f += f * amount;
            sF += sF * amount;
            bF += bF * amount;
            bSF += bSF * amount;
            cFR += cFR * amount;
        }
    }

    public class CataclysmData{
        //radius, uForce, uScaledForce, bForce, bScaledForce, range
        protected float r, f, sF, bF, bSF, rg;
        //color 1, color 2
        protected Color c1, c2;
        //has converted floor to space
        protected boolean space;

        public CataclysmData(){}
    }
}