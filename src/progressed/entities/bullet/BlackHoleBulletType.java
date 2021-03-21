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
    public float cataclysmRadius = 25f * 8f;
    public float cataclysmForceMul = 5f, cataclysmBulletForceMul = 5f, cataclymForceRange = 18f * 8f;
    public float suctionRadius = 96f, size = 5f, damageRadius = 32f;
    public float force = 90f, scaledForce = 80f, bulletForce = 0.8f, bulletScaledForce = 1.2f;
    public float swirlSize = 4f;

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
        b.data = new float[]{suctionRadius, size, damageRadius, swirlSize, force, scaledForce, bulletForce, bulletScaledForce};
        super.init(b);
    }

    @Override
    public void update(Bullet b){
        if(b.timer(1, 2f)){
            Damage.damage(b.team, b.x, b.y, ((float[])b.data)[2], b.damage);
            
            if(swirlEffect != Fx.none && b.time <= b.lifetime - swirlEffect.lifetime){
                swirlEffect.at(b.x, b.y, Mathf.random(360f), b);
            }

            Units.nearbyEnemies(b.team, b.x - ((float[])b.data)[0], b.y - ((float[])b.data)[0], ((float[])b.data)[0] * 2f, ((float[])b.data)[0] * 2f, unit -> {
                if(unit.within(b.x, b.y, ((float[])b.data)[0])){
                    unit.impulseNet(Tmp.v1.set(b).sub(unit).limit((((float[])b.data)[4] + (1f - unit.dst(b) / ((float[])b.data)[0]) * ((float[])b.data)[5]) * Time.delta));

                    if(unit.within(b.x, b.y, ((float[])b.data)[1])){
                        float scl = (unit.dst(b) / ((float[])b.data)[1]) * (unit.type.hitSize * hitSizeScl);
                        b.damage += b.damage * scl * damageIncreasePercent;
                        unit.maxHealth -= scl * maxHealthReduction;
                        
                        unit.clampHealth();
                        if(unit.maxHealth < 0f) unit.kill();

                        for(int i = 0; i < ((float[])b.data).length; i++){
                            ((float[])b.data)[i] += ((float[])b.data)[i] * scl * strengthIncreasePercent;
                        }
                    }
                }
            });

            Groups.bullet.intersect(b.x - ((float[])b.data)[0], b.y - ((float[])b.data)[0], ((float[])b.data)[0] * 2f, ((float[])b.data)[0] * 2f, other -> {
                if(other != null && Mathf.within(b.x, b.y, other.x, other.y, ((float[])b.data)[0]) && b != other && b.team != other.team && other.type.speed > 0.01f && !checkType(other.type)){
                    Vec2 impulse = Tmp.v1.set(b).sub(other).limit((((float[])b.data)[6] + (1f - other.dst(b) / ((float[])b.data)[0]) * ((float[])b.data)[7]) * Time.delta);
                    other.vel().add(impulse);

                    if(Mathf.within(b.x, b.y, other.x, other.y, ((float[])b.data)[1] * 2f)){
                        if(other.type instanceof BlackHoleBulletType){
                            BlackHoleBulletType type = (BlackHoleBulletType)other.type; //Pattern matching in instanceof when hhhhh
                            float radius = (cataclysmRadius + type.cataclysmRadius) / 2f;
                            if(radius > 0){ //Do not create negative radius cataclysms. I have no idea what this would cause anyways.
                                float uForce = (((float[])b.data)[4] * cataclysmForceMul + ((float[])other.data)[4] * type.cataclysmForceMul) / 2f;
                                float uScaledForce = (((float[])b.data)[5] * cataclysmForceMul + ((float[])other.data)[5] * type.cataclysmForceMul) / 2f;
                                float bForce = (((float[])b.data)[6] * cataclysmBulletForceMul + ((float[])other.data)[6] * type.cataclysmBulletForceMul) / 2f;
                                float bScaledForce = (((float[])b.data)[7] * cataclysmBulletForceMul + ((float[])other.data)[7] * type.cataclysmBulletForceMul) / 2f;
                                float range = (cataclymForceRange + type.cataclymForceRange) / 2f;
                                Object[] cataclysmParams = {radius, uForce, uScaledForce, bForce, bScaledForce, range, b.team.color, other.team.color, true};

                                float midX = (b.x + other.x) / 2f;
                                float midY = (b.y + other.y) / 2f;

                                Effect.shake(radius / 1.5f, radius * 1.5f, midX, midY);
                                PMBullets.cataclysm.create(b.owner, b.team, midX, midY, 0f, 0f, 1f, 1f, cataclysmParams);
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
        Fill.light(b.x, b.y, 60, ((float[])b.data)[1], b.team.color.cpy().lerp(Color.black, 0.5f + Mathf.absin(Time.time + Mathf.randomSeed(b.id), 10f, 0.4f)), Color.black);
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
        return (type instanceof StrikeBulletType) || (type instanceof UnitSpawnStrikeBulletType) || (type instanceof ParticleBulletType) || (type instanceof BlackHoleCataclysmType);
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
}