package progressed.entities.bullet;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.Units.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import progressed.graphics.*;

import static mindustry.Vars.*;

public class StrikeBulletType extends BasicBulletType{
    public float autoDropRadius, stopRadius;
    public boolean resumeSeek = true, snapRot;
    public float weaveWidth, weaveSpeed;
    public Effect rocketEffect = Fx.rocketSmoke;
    public float trailChance = 0.5f, smokeTrailChance = 0.75f;
    public float targetRadius = 1f;
    public float riseEngineTime, riseEngineSize = 8f, fallEngineTime = 8f, fallEngineSize = 6f;
    public float trailRnd = 3f, trailSize = 0.5f;
    public float riseTime = 60f, fallTime = 20f, elevation = 200f;
    public float riseEngineLightRadius = 50f, fallEngineLightRadius = 42f, engineLightOpacity = 0.5f;
    public Color engineLightColor = Pal.engine;
    public float riseSpin = 0f, fallSpin = 0f;

    public Sortf unitSort = Unit::dst2;

    public StrikeBulletType(float speed, float damage, String sprite){
        super(speed, damage, sprite);
        ammoMultiplier = 1;
        backMove = collides = hittable = absorbable = reflectable = keepVelocity = false;
        hitEffect = Fx.blockExplosionSmoke;
        shootEffect = smokeEffect = Fx.none;
        lightRadius = 32f;
        lightOpacity = 0.6f;
        lightColor = Pal.engine;
    }

    public StrikeBulletType(float speed, float damage){
        this(speed, damage, "error");
    }

    @Override
    public void init(){
        super.init();

        drawSize = elevation + 64f;
    }

    @Override
    public void init(Bullet b){
        super.init(b);

        if(b.data == null){
            if(b.owner instanceof Unit unit){
                b.data = new StrikeBulletData(unit.x, unit.y);
            }
            if(b.owner instanceof Building build){
                b.data = new StrikeBulletData(build.x, build.y);
            }
        }
    }

    @Override
    public void update(Bullet b){
        if(b.data instanceof StrikeBulletData data){
            float x = data.x;
            float y = data.y;

            float rise = Interp.pow5In.apply(Mathf.curve(b.time, 0f, riseTime));
            float rRocket = Interp.pow5In.apply(Mathf.curve(b.time, 0f, riseEngineTime)) - Interp.pow5In.apply(Mathf.curve(b.time, riseEngineTime, riseTime));
            float weave = weaveWidth > 0f ? Mathf.sin(b.time * weaveSpeed) * weaveWidth * Mathf.signs[Mathf.round(Mathf.randomSeed(b.id, 1f))] * rise : 0f;
            if(rise < 0.9999f && Mathf.chanceDelta(smokeTrailChance)){
                rocketEffect.at(x + weave + Mathf.range(trailRnd * rRocket), y + rise * elevation + Mathf.range(trailRnd * rRocket), trailSize * rRocket);
            }

            Teamc target = Units.bestTarget(b.team, b.x, b.y, homingRange, e -> !e.dead() && e.checkTarget(collidesAir, collidesGround), build -> !build.dead() && collidesGround, unitSort);

            //Instant drop
            float dropTime = (1f - Mathf.curve(b.time, 0, riseTime)) + Mathf.curve(b.time, b.lifetime - fallTime, b.lifetime);
            if(autoDropRadius > 0f && dropTime == 0 && target != null && Mathf.within(b.x, b.y, target.x(), target.y(), autoDropRadius)){
                b.time = b.lifetime - fallTime;
            }

            //Start and stop
            if(target != null && stopRadius > 0f){
                boolean inRange = Mathf.within(b.x, b.y, target.x(), target.y(), stopRadius);
                if(inRange && !data.stopped){
                    data.setVel(b.vel);
                    data.stopped = true;
                    b.vel.trns(b.vel.angle(), 0.001f);
                }else if(resumeSeek && (!inRange || ((Healthc)target).dead() || ((Healthc)target).health() < 0f) && data.stopped){
                    b.vel.set(data.vel);
                    data.stopped = false;
                }
            }else if((resumeSeek || target == null) && data.stopped){
                b.vel.set(data.vel);
                data.stopped = false;
            }

            if(!data.stopped){
                if(homingPower > 0.0001f && b.time >= homingDelay){
                    if(target != null){
                        b.vel.setAngle(Angles.moveToward(b.rotation(), b.angleTo(target), homingPower * Time.delta * 50f));
                    }
                }

                if(weaveMag > 0){
                    float scl = Mathf.randomSeed(b.id, 0.9f, 1.1f);
                    b.vel.rotate(Mathf.sin(b.time + Mathf.PI * weaveScale / 2f * scl, weaveScale * scl, weaveMag * (Mathf.randomSeed(b.id, 0, 1) == 1 ? -1 : 1)) * Time.delta);
                }

                if(trailChance > 0){
                    if(Mathf.chanceDelta(trailChance)){
                        trailEffect.at(b.x, b.y, trailParam, trailColor);
                    }
                }
            }
        }
    }

    @Override
    public void hit(Bullet b, float x, float y){
        b.hit = true;
        hitEffect.at(x, y, b.rotation(), hitColor);
        hitSound.at(x, y, hitSoundPitch, hitSoundVolume);

        Effect.shake(hitShake, hitShake, b);

        if(fragBullet != null){
            for(int i = 0; i < fragBullets; i++){
                float len = Mathf.random(1f, 7f);
                float a = b.rotation() + Mathf.range(fragCone/2) + fragAngle;
                if(fragBullet instanceof StrikeBulletType strike){
                    strike.create(b.owner, b.team, x, y, a, -1f, Mathf.random(fragVelocityMin, fragVelocityMax), Mathf.random(fragLifeMin, fragLifeMax), new StrikeBulletData(x, y));
                }else{
                    fragBullet.create(b, x + Angles.trnsx(a, len), y + Angles.trnsy(a, len), a, Mathf.random(fragVelocityMin, fragVelocityMax), Mathf.random(fragLifeMin, fragLifeMax));
                }
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
            Damage.damage(b.team, x, y, splashDamageRadius, splashDamage * b.damageMultiplier(), collidesAir, collidesGround);

            if(status != StatusEffects.none){
                Damage.status(b.team, x, y, splashDamageRadius, status, statusDuration, collidesAir, collidesGround);
            }

            if(healPercent > 0f){
                indexer.eachBlock(b.team, x, y, splashDamageRadius, Building::damaged, other -> {
                    Fx.healBlockFull.at(other.x, other.y, other.block.size, Pal.heal);
                    other.heal(healPercent / 100f * other.maxHealth());
                });
            }

            if(makeFire){
                indexer.eachBlock(null, x, y, splashDamageRadius, other -> other.team != b.team, other -> {
                    Fires.create(other.tile);
                });
            }
        }

        for(int i = 0; i < lightning; i++){
            Lightning.create(b, lightningColor, lightningDamage < 0 ? damage : lightningDamage, b.x, b.y, b.rotation() + Mathf.range(lightningCone/2) + lightningAngle, lightningLength + Mathf.random(lightningLengthRand));
        }
    }

    @Override
    public void draw(Bullet b){
        //wall of variables text oh god
        if(b.data instanceof StrikeBulletData data){
            float x = data.x;
            float y = data.y;

            float rise = Interp.pow5In.apply(Mathf.curve(b.time, 0f, riseTime));
            float fadeOut = 1f - rise;
            float fadeIn = Mathf.curve(b.time, b.lifetime - fallTime, b.lifetime);
            float fall = 1f - fadeIn;
            float a = fadeOut + Interp.pow5Out.apply(fadeIn);
            float rRocket = Interp.pow5In.apply(Mathf.curve(b.time, 0f, riseEngineTime)) - Interp.pow5In.apply(Mathf.curve(b.time, riseEngineTime, riseTime));
            float fRocket = Interp.pow5In.apply(Mathf.curve(b.time, b.lifetime - fallTime, b.lifetime - fallTime + fallEngineTime));
            float target = Mathf.curve(b.time, 0f, riseTime / 2f) - Mathf.curve(b.time, b.lifetime - fallTime / 2f, b.lifetime);
            float rot = snapRot ? b.rotation() + 90f : rise * riseSpin + fadeIn * fallSpin;
            Tmp.v1.trns(225f, rise * elevation * 2f);
            Tmp.v2.trns(225f, fall * elevation * 2f);
            float rY = y + rise * elevation;
            float fY = b.y + fall * elevation;
            float side = Mathf.signs[Mathf.round(Mathf.randomSeed(b.id, 1f))];
            float weave = Mathf.sin(b.time * weaveSpeed) * weaveWidth * side;
            float rWeave = weaveWidth > 0f ? weave * rise : 0f;
            float fWeave = weaveWidth > 0f ? weave * fall : 0f;
            float rX = x + rWeave;
            float fX = b.x + fWeave;

            //Target
            float radius = targetRadius * target;
            if(autoDropRadius > 0f){
                float dropAlpha = Mathf.curve(b.time, riseTime * 2f/3f, riseTime) - Mathf.curve(b.time, b.lifetime - 8f, b.lifetime);
                Draw.z(Layer.bullet + 0.001f);
                Draw.color(Color.red, (0.25f + 0.5f * Mathf.absin(16f, 1f)) * dropAlpha);
                Fill.circle(b.x, b.y, autoDropRadius);
            }
            if(targetRadius > 0){
                Draw.z(Layer.bullet + 0.002f);
                Draw.color(Pal.gray, target);
                Lines.stroke(3);
                Lines.poly(b.x, b.y, 4, 7f * radius, Time.time * 1.5f + Mathf.randomSeed(b.id, 360f));
                Lines.spikes(b.x, b.y, 3f * radius, 6f * radius, 4, Time.time * 1.5f + Mathf.randomSeed(b.id, 360f));
                Draw.color(b.team.color, target);
                Lines.stroke(1);
                Lines.poly(b.x, b.y, 4, 7f * radius, Time.time * 1.5f + Mathf.randomSeed(b.id, 360f));
                Lines.spikes(b.x, b.y, 3f * radius, 6f * radius, 4, Time.time * 1.5f + Mathf.randomSeed(b.id, 360f));
                Draw.reset();
            }

            //Missile
            if(fadeOut > 0 && fadeIn == 0){
                //Engine stolen from launchpad
                if(riseEngineSize > 0f){
                    Draw.z(Layer.effect + 0.001f);
                    Draw.color(engineLightColor);
                    Fill.light(rX, rY, 10, riseEngineSize * 1.5625f * rRocket, Tmp.c1.set(Pal.engine).mul(1f, 1f, 1f, rRocket), Tmp.c2.set(Pal.engine).mul(1, 1f, 1f, 0f));
                    PMDrawf.cross(rX, rY, riseEngineSize * 0.375f, riseEngineSize * 2.5f * rRocket, Time.time * 1.5f + Mathf.randomSeed(b.id, 360f));
                    Drawf.light(b.team, rX, rY, riseEngineLightRadius * rRocket, engineLightColor, engineLightOpacity * rRocket);
                }
                //Missile itself
                Draw.z(Layer.weather - 1);
                Draw.color();
                Draw.alpha(a);
                Draw.rect(frontRegion, rX, rY, frontRegion.width * Draw.scl, frontRegion.height * Draw.scl, rot);
                Drawf.light(b.team, rX, rY, lightRadius, lightColor, lightOpacity);
                //Missile shadow
                Draw.z(Layer.flyingUnit + 1f);
                Draw.color(0f, 0f, 0f, 0.22f * a);
                Draw.rect(frontRegion, rX + Tmp.v1.x, rY + Tmp.v1.y, frontRegion.width * Draw.scl, frontRegion.height * Draw.scl, rot);
            }else if(fadeOut == 0f && fadeIn > 0f){
                //Missile itself
                Draw.z(Layer.weather - 2f);
                Draw.color();
                Draw.alpha(a);
                Draw.rect(backRegion, fX, fY, backRegion.width * Draw.scl, backRegion.height * Draw.scl, rot + 180f);
                Drawf.light(b.team, fX, fY, lightRadius, lightColor, lightOpacity);
                //Engine stolen from launchpad
                if(fallEngineSize > 0f){
                    Draw.z(Layer.weather - 1f);
                    Draw.color(engineLightColor);
                    Fill.light(fX, fY, 10, fallEngineSize * 1.5625f * fRocket, Tmp.c1.set(Pal.engine).mul(1f, 1f, 1f, fRocket), Tmp.c2.set(Pal.engine).mul(1f, 1f, 1f, 0f));
                    PMDrawf.cross(fX, fY, riseEngineSize * 0.375f, riseEngineSize * 2.5f * fRocket, Time.time * 1.5f + Mathf.randomSeed(b.id + 2, 360f));
                    Drawf.light(b.team, fX, fY, fallEngineLightRadius * fRocket, engineLightColor, engineLightOpacity * fRocket);
                }
                //Missile shadow
                Draw.z(Layer.flyingUnit + 1f);
                Draw.color(0f, 0f, 0f, 0.22f * a);
                Draw.rect(backRegion, fX + Tmp.v2.x, fY + Tmp.v2.y, backRegion.width * Draw.scl, backRegion.height * Draw.scl, rot + 180f + Mathf.randomSeed(b.id + 3, 360f));
            }

            Draw.reset();
        }
    }

    @Override
    public void drawLight(Bullet b){
        //Do nothing
    }

    public static class StrikeBulletData{
        public float x, y;
        public Vec2 vel;
        protected boolean stopped;

        public StrikeBulletData(float x, float y){
            this.x = x;
            this.y = y;
        }

        public void setVel(Vec2 vel){
            this.vel = vel.cpy();
        }

        public String toString(){
            return "x : " + x +
            "\ny: " + y +
            "\nstopped: " + stopped;
        }
    }
}