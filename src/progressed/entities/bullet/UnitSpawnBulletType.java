package progressed.entities.bullet;

import arc.graphics.g2d.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import progressed.graphics.*;

public class UnitSpawnBulletType extends BasicBulletType{
    public float scaleAmount = 0.2f, trailSize = -1, trailTimeMul = 1f, shadowOffset = 10f;
    public float velMult = 1f;
    public UnitType spawn;

    public UnitSpawnBulletType(float speed, UnitType spawn){
        super(speed, 0f);
        this.spawn = spawn;
        drawSize = 800f;
        backMove = false;
        scaleVelocity = true;
        shootEffect = smokeEffect = Fx.none;
        lifetime = 120f;
        trailEffect = PMFx.sentryTrail;
        ammoMultiplier = 1;
        collidesGround = collidesAir = collidesTiles = collides = false;
    }

    @Override
    public void load(){
        frontRegion = spawn.fullIcon;
    }

    @Override
    public void init(){
        super.init();

        if(trailSize < 0) trailSize = spawn.hitSize * 0.75f;
    }

    @Override
    public void despawned(Bullet b){
        Unit spawned = spawn.spawn(b.team, b.x, b.y);
        spawned.rotation = b.rotation();
        if(velMult != 0f) spawned.vel.add(b.vel, velMult);

        super.despawned(b);
    }

    @Override
    public void update(Bullet b){
        float scl = b.lifetime / b.type.lifetime;
        float slope = b.fin() * (1f - b.fin());
        float scale = scaleAmount * (slope * 4f) * scl + 1f;
        float trail = trailSize * scale;
        if(b.timer(0, (3f + slope * 2f) * trailTimeMul)){
            trailEffect.at(b.x, b.y, trail, trailColor);
        }
        
        super.update(b);
    }

    @Override
    public void draw(Bullet b){
        float scl = b.lifetime / b.type.lifetime;
        float slope = b.fin() * (1f - b.fin());
        float shadowScl = (slope * 4f) * scl;
        float shadowOff = shadowOffset * shadowScl;
        float scale = scaleAmount * shadowScl + 1;

        Draw.z(Layer.flyingUnit + 1);
        Drawf.shadow(frontRegion, b.x - shadowOff, b.y - shadowOff, b.rotation() - 90f);
        
        Draw.z(Layer.flyingUnit + 2);
        Draw.rect(frontRegion, b.x, b.y, frontRegion.width / 4f * scale, frontRegion.height / 4f * scale, b.rotation() - 90f);
    }
}