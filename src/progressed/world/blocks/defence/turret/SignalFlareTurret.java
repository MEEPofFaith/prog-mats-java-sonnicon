package progressed.world.blocks.defence.turret;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.ui.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;
import progressed.entities.bullet.*;
import progressed.entities.units.*;
import progressed.ui.*;

public class SignalFlareTurret extends ItemTurret{
    public int flareLimit = 1;

    public SignalFlareTurret(String name){
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.remove(Stat.ammo);
        stats.add(Stat.ammo, new PMAmmoListValue<>(ammoTypes));
    }

    @Override
    public void setBars(){
        super.setBars();

        bars.add("pm-flare-limit", (SignalFlareTurretBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pm-flare-limit", entity.flares.size, flareLimit),
            () -> entity.team.color,
            () -> (float)entity.flares.size / (float)flareLimit
        ));
    }

    public class SignalFlareTurretBuild extends ItemTurretBuild{
        public Bullet bullet;
        public Seq<FlareUnitEntity> flares = new Seq<>();

        @Override
        public boolean canControl(){
            return false;
        }

        @Override
        public void control(LAccess type, Object p1, double p2, double p3, double p4) {
            // cannot control
        }

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4) {
            // cannot control
        }

        @Override
        public void updateTile(){
            wasShooting = false;

            recoil = Mathf.lerpDelta(recoil, 0f, restitution);
            heat = Mathf.lerpDelta(heat, 0f, cooldown);

            unit.health(health);
            unit.rotation(rotation);
            unit.team(team);
            unit.set(x, y);

            if(logicControlTime > 0){
                logicControlTime -= Time.delta;
            }

            flares.each(b -> {
                if(!b.isAdded() || b.dead || b.health < 0f){
                    flares.remove(b);
                }
            });

            if(bullet != null && !bullet.isAdded()){
                bullet = null;
            }

            if(hasAmmo()){
                if(timer(timerTarget, targetInterval)){
                    target = Groups.bullet.intersect(x - range, y - range, range * 2f, range * 2f).min(
                        b -> b.team != team && within(b, range) && !(b.type instanceof SignalFlareBulletType) && b.type().speed > 0.01f,
                        b -> b.dst2(this)
                    );
                }

                //pooled flares
                if(target != null && !target.isAdded()){
                    target = null;
                }

                if(target != null){
                    targetPosition(target);

                    float targetRot = angleTo(targetPos);

                    if(shouldTurn()){
                        turnToTarget(targetRot);
                    }

                    if(Angles.angleDist(rotation, targetRot) < shootCone){
                        wasShooting = true;
                        updateShooting();
                    }
                }
            }

            if(acceptCoolant){
                updateCooling();
            }
        }

        @Override
        protected void updateShooting(){
            if(reload >= reloadTime && !charging && flares.size < flareLimit && bullet == null){
                BulletType type = peekAmmo();

                shoot(type);

                reload = 0f;
            }else{
                reload += delta() * peekAmmo().reloadMultiplier * baseReloadSpeed();
            }
        }

        @Override
        protected void bullet(BulletType type, float angle){
            float lifeScl = type.scaleVelocity ? Mathf.clamp(Mathf.dst(x + tr.x, y + tr.y, targetPos.x, targetPos.y) / type.range(), minRange / type.range(), range / type.range()) : 1f;

            bullet = type.create(this, team, x + tr.x, y + tr.y, angle, 1f + Mathf.range(velocityInaccuracy), lifeScl);
        }

        @Override
        public BlockStatus status(){
            return (flares.size >= flareLimit || bullet != null) ? BlockStatus.noOutput : super.status();
        }
    }
}