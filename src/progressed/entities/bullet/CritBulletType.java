package progressed.entities.bullet;

import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import progressed.graphics.*;

import static mindustry.Vars.*;

public class CritBulletType extends BasicBulletType{
    public float critChance = 0.15f, critMultiplier = 5f;
    public Effect critEffect = PMFx.sniperCrit;
    public int trailLength = 6; 
    public float trailWidth = 4f;

    public CritBulletType(float speed, float damage, String sprite){
        super(speed, damage, sprite);
        pierce = true;
        pierceBuilding = true;
        ammoMultiplier = 1;
        shootEffect = Fx.shootBig;
        smokeEffect = Fx.shootBigSmoke;
        hitEffect = PMFx.critPierce;
        drawSize = 300f;
    }

    public CritBulletType(float speed, float damage){
        this(speed, damage, "bullet");
    }

    public CritBulletType(){
        this(1f, 1f);
    }

    @Override
    public void init(Bullet b){
        if(b.data == null){
            if(Mathf.chance(critChance)){
                b.data = new CritBulletData(true, new Trail(trailLength));
            }else{
                b.data = new CritBulletData(false, new Trail(trailLength));
            }
        }
        if(((CritBulletData)b.data).crit) b.damage *= critMultiplier;

        super.init(b);
    }

    @Override
    public void draw(Bullet b){
        if(((CritBulletData)b.data).trail instanceof Trail tr) tr.draw(backColor, trailWidth);
        super.draw(b);
    }

    @Override
    public void update(Bullet b){
        if(Mathf.chanceDelta(1) && ((CritBulletData)b.data).crit){
            critEffect.at(b.x, b.y, b.rotation(), b.team.color);
        }

        if(((CritBulletData)b.data).trail instanceof Trail tr) tr.update(b.x, b.y);

        super.update(b);
    }

    @Override
    public void despawned(Bullet b){
        if(((CritBulletData)b.data).trail instanceof Trail tr) tr.clear();
        super.despawned(b);
    }

    @Override
    public void hit(Bullet b, float x, float y){
        boolean crit = ((CritBulletData)b.data).crit;
        float critBonus = crit ? this.critMultiplier : 1f;
        b.hit = true;
        hitEffect.at(x, y, b.rotation(), hitColor);
        hitSound.at(x, y, hitSoundPitch, hitSoundVolume);

        Effect.shake(hitShake, hitShake, b);

        if(fragBullet != null){
            for(int i = 0; i < fragBullets; i++){
                float len = Mathf.random(1f, 7f);
                float a = b.rotation() + Mathf.range(fragCone/2) + fragAngle;
                int fragTrailLength = fragBullet instanceof CritBulletType critB ? critB.trailLength : 0;
                fragBullet.create(b.owner, b.team, x + Angles.trnsx(a, len), y + Angles.trnsy(a, len), a, -1f, Mathf.random(fragVelocityMin, fragVelocityMax), Mathf.random(fragLifeMin, fragLifeMax), new CritBulletData(crit, new Trail(fragTrailLength)));
            }
        }

        if(puddleLiquid != null && puddles > 0){
            for(int i = 0; i < puddles; i++){
                Tile tile = world.tileWorld(x + Mathf.range(puddleRange), y + Mathf.range(puddleRange));
                Puddles.deposit(tile, puddleLiquid, puddleAmount);
            }
        }

        if(Mathf.chance(incendChance)){
            Damage.createIncend(x, y, incendSpread, incendAmount);
        }

        if(splashDamageRadius > 0 && !b.absorbed){
            Damage.damage(b.team, x, y, splashDamageRadius, splashDamage * b.damageMultiplier() * critBonus, collidesAir, collidesGround);

            if(status != StatusEffects.none){
                Damage.status(b.team, x, y, splashDamageRadius, status, statusDuration, collidesAir, collidesGround);
            }

            if(healPercent > 0f){
                indexer.eachBlock(b.team, x, y, splashDamageRadius, Building::damaged, other -> {
                    Fx.healBlockFull.at(other.x, other.y, other.block.size, Pal.heal);
                    other.heal(healPercent * critBonus / 100f * other.maxHealth());
                });
            }

            if(makeFire){
                indexer.eachBlock(null, x, y, splashDamageRadius, other -> other.team != b.team, other -> {
                    Fires.create(other.tile);
                });
            }
        }

        for(int i = 0; i < lightning; i++){
            Lightning.create(b, lightningColor, (lightningDamage < 0 ? damage : lightningDamage) * critBonus, b.x, b.y, b.rotation() + Mathf.range(lightningCone/2) + lightningAngle, lightningLength + Mathf.random(lightningLengthRand));
        }
    }

    public class CritBulletData{
        protected boolean crit;
        protected Trail trail;

        public CritBulletData(boolean crit, Trail trail){
            this.crit = crit;
            this.trail = trail;
        }
    }
}