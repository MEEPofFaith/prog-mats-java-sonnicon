package progressed.entities.bullet;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import progressed.content.*;
import progressed.graphics.*;
import progressed.util.*;

public class BlackHoleCataclysmType extends BulletType{
    public float growTime = 20f, secondaryGrowTime = 180f, fadeTime = 90f;

    public BlackHoleCataclysmType(){
        super(0f, 0f);
        hittable = absorbable = false;
        collides = collidesAir = collidesGround = collidesTiles = false;
        pierce = true;
        shootEffect = smokeEffect = Fx.none;
        lifetime = 60f * 60f; //Minute of death
        drawSize = 2048f;
    }

    @Override
    public void update(Bullet b) {
        //[radius, uForce, uScaledForce, bForce, bScaledForce, range, c1, c2]
        Object[] rawData = (Object[])b.data;

        float[] data = new float[6];
        for(int i = 0; i < 6; i++){
            data[i] = (float)rawData[i];
        }

        float shrink = 1 - Mathf.curve(b.time, b.lifetime - fadeTime, b.lifetime);
        float scl = Mathf.curve(b.time, 0f, growTime) * shrink;
        float suctionRadius = (data[0] + data[5]) * scl;

        if(b.timer(1, 2f)){
            Units.nearbyEnemies(null, b.x - suctionRadius, b.y - suctionRadius, suctionRadius * 2f, suctionRadius * 2f, unit -> {
                if(unit.within(b.x, b.y, suctionRadius)){
                    unit.impulseNet(Tmp.v1.set(b).sub(unit).limit((data[1] * scl + (1f - unit.dst(b) / suctionRadius) * data[2] * scl) * Time.delta));

                    if(unit.within(b.x, b.y, data[0] * scl)){
                        unit.kill();
                    }
                }
            });

            Groups.bullet.intersect(b.x - suctionRadius, b.y - suctionRadius, suctionRadius * 2f, suctionRadius * 2f, other -> {
                if(other != null && Mathf.within(b.x, b.y, other.x, other.y, suctionRadius) && b != other && other.type.speed > 0.01f){
                    Vec2 impulse = Tmp.v1.set(b).sub(other).limit((data[3] * scl + (1f - other.dst(b) / suctionRadius) * data[4] * scl) * Time.delta);
                    other.vel().add(impulse);

                    if(Mathf.within(b.x, b.y, other.x, other.y, data[0] * scl)){
                        absorbBullet(other);
                    }
                }
            });

            PMUtls.trueEachBlock(b.x, b.y, data[0] * scl, other -> {
                other.kill();
            });
        }

        if(b.time < growTime * 2f){ //*inhales* SPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACE
            PMUtls.trueEachTile(b.x, b.y, data[0] * scl, tile -> {
                tile.setAir();
                tile.setFloorNet(Blocks.space);
                Vars.world.notifyChanged(tile);
            });
            Events.fire(new WorldLoadEvent());
        }
    }

    @Override
    public void draw(Bullet b){
        //[radius, uForce, uScaledForce, bForce, bScaledForce, range, c1, c2]
        Object[] rawData = (Object[])b.data;

        float[] data = new float[6];
        for(int i = 0; i < 6; i++){
            data[i] = (float)rawData[i];
        }

        Color[] colors = new Color[]{(Color)rawData[6], (Color)rawData[7]};
        Color[] darkenedColors = new Color[]{colors[0].cpy().lerp(Color.black, 0.5f), colors[1].cpy().lerp(Color.black, 0.5f)};

        float shrink = 1 - Mathf.curve(b.time, b.lifetime - fadeTime, b.lifetime);
        float scl = Mathf.curve(b.time, 0f, growTime) * shrink;
        float grow2 = Interp.pow2Out.apply(Mathf.curve(b.time, 0f, secondaryGrowTime));
        float radius = data[0] * scl;

        Draw.z(Layer.max);
        Fill.light(b.x, b.y, 60, radius, darkenedColors[0].cpy().lerp(darkenedColors[1], Mathf.absin(Time.time + Mathf.randomSeed(b.id), 10f, 1f)), Color.black);

        Angles.randLenVectors(b.id * 2, Mathf.round(data[0] / 3f), (data[0] + data[5]), (x, y) -> {
            float offset = Mathf.randomSeed((long)(b.id * Mathf.randomSeed((long)x) * Mathf.randomSeed((long)y)));
            float tx = x * grow2;
            float ty = y * grow2;
            Fill.light(b.x + tx, b.y + ty, 60, data[0] / 10f * grow2 * shrink, darkenedColors[0].cpy().lerp(darkenedColors[1], Mathf.absin(Time.time + offset, 10f, 1f)), Color.black);
        });
    }

    public void absorbBullet(Bullet other){
        PMFx.blackHoleAbsorb.at(other.x, other.y);
        other.type = PMBullets.absorbed;
        other.absorb();
    }
}
