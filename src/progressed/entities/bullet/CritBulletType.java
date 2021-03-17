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
    public float critChance = 0.25f, critMultiplier = 5f;
    public Effect critEffect = PMFx.sniperCrit;

    public CritBulletType(float speed, float damage, String sprite){
        super(speed, damage, sprite);
        pierce = true;
        pierceBuilding = true;
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
                b.data = true;
            }else{
                b.data = false;
            }
        }
        if((boolean)b.data) b.damage *= critMultiplier;

        super.init(b);
    }

    @Override
    public void update(Bullet b) {
        if(Mathf.chanceDelta(1) && (boolean)b.data){
            critEffect.at(b.x, b.y, b.rotation(), b.team.color);
        }
    }

    @Override
    public void hit(Bullet b, float x, float y){
        boolean crit = (boolean)b.data;
        float critBonus = crit ? this.critMultiplier : 1f;
        b.hit = true;
        hitEffect.at(x, y, b.rotation(), hitColor);
        hitSound.at(x, y, hitSoundPitch, hitSoundVolume);

        Effect.shake(hitShake, hitShake, b);

        if(fragBullet != null){
            for(int i = 0; i < fragBullets; i++){
                float len = Mathf.random(1f, 7f);
                float a = b.rotation() + Mathf.range(fragCone/2) + fragAngle;
                fragBullet.create(b.owner, b.team, x + Angles.trnsx(a, len), y + Angles.trnsy(a, len), a, -1f, Mathf.random(fragVelocityMin, fragVelocityMax), Mathf.random(fragLifeMin, fragLifeMax), crit);
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
}