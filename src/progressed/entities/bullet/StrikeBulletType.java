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
        backMove = collides = hittable = absorbable = keepVelocity = false;
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
            if(b.owner instanceof Unit){
                Unit owner = (Unit)b.owner;
                Object[] data = {owner.x, owner.y, null, false};
                b.data = data;
            }
            if(b.owner instanceof Building){
                Building owner = (Building)b.owner;
                Object[] data = {owner.x, owner.y, null, false};
                b.data = data;
            }
        }
    }

    @Override
    public void update(Bullet b){
        Object[] rawData = (Object[])b.data;
        float x = (float)rawData[0];
        float y = (float)rawData[1];

        float rise = Interp.pow5In.apply(Mathf.curve(b.time, 0f, riseTime));
        float rRocket = Interp.pow5In.apply(Mathf.curve(b.time, 0f, riseEngineTime)) - Interp.pow5In.apply(Mathf.curve(b.time, riseEngineTime, riseTime));
        float weave = weaveWidth > 0f ? Mathf.sin(b.time * weaveSpeed) * weaveWidth * Mathf.signs[Mathf.round(Mathf.randomSeed(b.id, 1f))] * rise : 0f;
        if(rise < 0.9999f && Mathf.chanceDelta(smokeTrailChance)){
            rocketEffect.at(x + weave + Mathf.range(trailRnd * rRocket), y + rise * elevation + Mathf.range(trailRnd * rRocket), trailSize * rRocket);
        }

        Teamc target = Units.bestTarget(b.team, b.x, b.y, homingRange, e -> !e.dead() && (e.isGrounded() && collidesGround) || (e.isFlying() && collidesAir), build -> !build.dead() && collidesGround, unitSort);

        //Instant drop
        float dropTime = (1f - Mathf.curve(b.time, 0, riseTime)) + Mathf.curve(b.time, b.lifetime - fallTime, b.lifetime);
        if(autoDropRadius > 0f && dropTime == 0 && target != null && Mathf.within(b.x, b.y, target.x(), target.y(), autoDropRadius)){
            b.time = b.lifetime - fallTime;
        }

        //Start and stop
        if(target != null && stopRadius > 0f){
            boolean inRange = Mathf.within(b.x, b.y, target.x(), target.y(), stopRadius);
            if(inRange && !(boolean)rawData[3]){
                rawData[2] = b.vel.len();
                rawData[3] = true;
                b.vel.trns(b.vel.angle(), 0.001f);
            }else if(resumeSeek && (!inRange || ((Healthc)target).dead() || ((Healthc)target).health() < 0f) && (boolean)rawData[3]){
                b.vel.trns(b.vel.angle(), (float)rawData[2]);
                rawData[3] = false;
            }
        }else if(resumeSeek && (boolean)rawData[3]){
            b.vel.set((Vec2)rawData[2]);
            rawData[3] = false;
        }

        if(!(boolean)rawData[3]){
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

        b.data = rawData;
    }

    @Override
    public void draw(Bullet b){
        //wall of variables text oh god
        Object[] rawData = (Object[])b.data;
        float x = (float)rawData[0];
        float y = (float)rawData[1];

        float rise = Interp.pow5In.apply(Mathf.curve(b.time, 0f, riseTime));
        float fadeOut = 1f - rise;
        float fadeIn = Mathf.curve(b.time, b.lifetime - fallTime, b.lifetime);
        float fall = 1f - fadeIn;
        float a = fadeOut + Interp.pow5Out.apply(fadeIn);
        float rRocket = Interp.pow5In.apply(Mathf.curve(b.time, 0f, riseEngineTime)) - Interp.pow5In.apply(Mathf.curve(b.time, riseEngineTime, riseTime));
        float fRocket = Interp.pow5In.apply(Mathf.curve(b.time, b.lifetime - fallTime, b.lifetime - fallTime + fallEngineTime));
        float target = Mathf.curve(b.time, 0f, 8f) - Mathf.curve(b.time, b.lifetime - 8f, b.lifetime);
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
                for(int i = 0; i < 4; i++){
                    Drawf.tri(rX, rY, riseEngineSize * 0.375f, riseEngineSize * 2.5f * rRocket, i * 90f + (Time.time * 1.5f + Mathf.randomSeed(b.id, 360f)));
                }
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
                for(int i = 0; i < 4; i++){
                    Drawf.tri(fX, fY, fallEngineSize * 0.375f, fallEngineSize * 2.5f * fRocket, i * 90f + (Time.time * 1.5f + Mathf.randomSeed(b.id + 2, 360f)));
                }
                Drawf.light(b.team, fX, fY, fallEngineLightRadius * fRocket, engineLightColor, engineLightOpacity * fRocket);
            }
            //Missile shadow
            Draw.z(Layer.flyingUnit + 1f);
            Draw.color(0f, 0f, 0f, 0.22f * a);
            Draw.rect(backRegion, fX + Tmp.v2.x, fY + Tmp.v2.y, backRegion.width * Draw.scl, backRegion.height * Draw.scl, rot + 180f + Mathf.randomSeed(b.id + 3, 360f));
        }

        Draw.reset();
    }

    @Override
    public void drawLight(Bullet b){
        //Do nothing
    }
}