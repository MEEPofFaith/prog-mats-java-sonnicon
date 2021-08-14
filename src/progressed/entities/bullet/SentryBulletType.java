package progressed.entities.bullet;

import arc.graphics.g2d.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import progressed.world.blocks.payloads.*;

public class SentryBulletType extends BulletType{
    public UnitType unit;

    public SentryBulletType(UnitType unit){
        super(10f, 0f);
        this.unit = unit;

        lifetime = 35f;
        collidesGround = collidesAir = collidesTiles = collides = false;
        scaleVelocity = true;
        splashDamage = 60f;
        splashDamageRadius = 8f;
        hitEffect = despawnEffect = Fx.none;
        layer = Layer.flyingUnitLow - 1f;
    }

    @Override
    public void draw(Bullet b){
        if(b.data instanceof Sentry s){
            Draw.rect(s.unit.fullIcon, b.x, b.y, b.rotation() - 90f);
        }
    }

    @Override
    public void despawned(Bullet b){
        if(b.data instanceof Sentry s){
            Unit spawned = s.unit.spawn(b.team, b);
            spawned.rotation = b.rotation();
            spawned.vel.add(b.vel);
        }

        super.despawned(b);
    }
}
