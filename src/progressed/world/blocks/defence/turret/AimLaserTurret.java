package progressed.world.blocks.defence.turret;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.defense.turrets.*;
import progressed.entities.*;
import progressed.graphics.*;
import progressed.util.*;

public class AimLaserTurret extends PowerTurret{
    public float aimStroke = 2f, aimRnd;
    public float chargeSoundVolume = 1f, shootSoundVolume = 1f;

    public AimLaserTurret(String name){
        super(name);
    }

    @Override
    public void setBars(){
        super.setBars();
        bars.add("pm-reload", (AimLaserTurretBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pm-reload", PMUtls.stringsFixed(Mathf.clamp(entity.reload / reloadTime) * 100f)),
            () -> entity.team.color,
            () -> Mathf.clamp(entity.reload / reloadTime)
        ));

        bars.add("pm-charge", (AimLaserTurretBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pm-charge", PMUtls.stringsFixed(Mathf.clamp(entity.charge) * 100f)),
            () -> Color.sky,
            () -> entity.charge
        ));
    }

    public class AimLaserTurretBuild extends PowerTurretBuild implements ExtensionHolder{
        public Extension ext;
        protected float charge;

        @Override
        public void created(){
            super.created();
            ext = Extension.create();
            ext.holder = this;
            ext.set(x, y);
            ext.add();
        }
        
        public boolean isAI(){
            return !(isControlled() || (logicControlled() && logicShooting));
        }

        @Override
        public void drawExt(){
            if(charging){
                Draw.mixcol();

                Draw.z(Layer.effect);
                Lines.stroke(aimStroke * charge, team.color);

                float c = Interp.pow2Out.apply(charge);
                float dst = shootType.range() + shootLength;
                Healthc box = PMDamage.linecast(targetGround, targetAir, team, x, y, rotation, dst);

                Tmp.v1.trns(rotation, shootLength);
                if(isAI() && target instanceof Unit){
                    Tmp.v2.trns(rotation, dst(targetPos)).limit(dst);
                }else if(box != null){
                    Tmp.v2.trns(rotation, dst(box)).limit(dst);
                }else{
                    Tmp.v2.trns(rotation, dst);
                }
                Tmp.v3.rnd(Mathf.random(aimRnd * (1f - c)));
                Tmp.v4.set(Tmp.v2).add(Tmp.v3);

                PMDrawf.line(x, y, Tmp.v1, Tmp.v4, false);

                if(isAI() && target instanceof Unit u){
                    Draw.alpha(0.75f);

                    Lines.line(u.x, u.y, targetPos.x, targetPos.y);
                    Fill.circle(u.x, u.y, aimStroke / 2f * c);
                    Fill.circle(targetPos.x, targetPos.y, aimStroke / 2f * c);

                    Draw.mixcol(team.color, 1f);
                    Draw.rect(u.type.icon(Cicon.full), targetPos.x, targetPos.y, u.rotation - 90f);
                    Draw.mixcol();
                    Draw.alpha(1f);
                }
                
                Fill.circle(x + Tmp.v1.x, y + Tmp.v1.y, aimStroke / 2f * c);
                Fill.circle(x + Tmp.v4.x, y + Tmp.v4.y, aimStroke / 2f * c);
                Lines.circle(x + Tmp.v4.x, y + Tmp.v4.y, aimStroke * 2f * (0.5f + c / 2f));

                Draw.color();
            }
        }

        @Override
        public void updateTile(){
            if(charging){
                charge = Mathf.clamp(charge + Time.delta / chargeTime);
            }else{
                charge = 0;
            }

            super.updateTile();
        }

        @Override
        protected void updateCooling(){
            if(!charging){
                super.updateCooling();
            }
        }

        @Override
        protected void updateShooting(){
            if(!charging){
                super.updateShooting();
            }
        }

        @Override
        protected void shoot(BulletType type){
            useAmmo();
            tr.trns(rotation, shootLength);
            chargeBeginEffect.at(x + tr.x, y + tr.y, rotation, team.color, self());
            chargeSound.at(x + tr.x, y + tr.y, 1, chargeSoundVolume);

            for(int i = 0; i < chargeEffects; i++){
                Time.run(Mathf.random(chargeMaxDelay), () -> {
                    if(!isValid()) return;
                    tr.trns(rotation, shootLength);
                    chargeEffect.at(x + tr.x, y + tr.y, rotation, team.color, self());
                });
            }

            charging = true;

            Time.run(chargeTime, () -> {
                if(!isValid()) return;
                tr.trns(rotation, shootLength);
                recoil = recoilAmount;
                heat = 1f;
                bullet(type, rotation + Mathf.range(inaccuracy));
                effects();
                charging = false;
            });
        }
        
        @Override
        protected void effects(){
            Effect fshootEffect = shootEffect == Fx.none ? peekAmmo().shootEffect : shootEffect;
            Effect fsmokeEffect = smokeEffect == Fx.none ? peekAmmo().smokeEffect : smokeEffect;

            fshootEffect.at(x + tr.x, y + tr.y, rotation);
            fsmokeEffect.at(x + tr.x, y + tr.y, rotation);
            shootSound.at(x + tr.x, y + tr.y, Mathf.random(0.9f, 1.1f), shootSoundVolume);

            if(shootShake > 0){
                Effect.shake(shootShake, shootShake, this);
            }

            recoil = recoilAmount;
        }

        @Override
        public boolean shouldTurn(){
            return true;
        }

        @Override
        public float clipSizeExt(){
            return (shootType.range() + shootLength + aimRnd + aimStroke * 2f) * Vars.renderer.getDisplayScale();
        }

        @Override
        public void onRemoved(){
            super.onRemoved();
            ext.remove();
        }
    }
}