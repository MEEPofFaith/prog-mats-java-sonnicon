package progressed.world.blocks.defence.turret;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.Interp.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import progressed.util.*;

public class ChaosTurret extends PowerTurret{
    public float shootDuration;

    protected PowIn pow = PMUtls.customPowIn(6);

    public ChaosTurret(String name){
        super(name);

        requirements(Category.turret, BuildVisibility.sandboxOnly, ItemStack.empty);

        alwaysUnlocked = true;

        heatDrawer = tile -> {
            if(tile.heat <= 0.00001f) return;
            float r = Interp.pow2Out.apply(tile.heat);
            float g = Interp.pow3In.apply(tile.heat) + ((1f - Interp.pow3In.apply(tile.heat)) * 0.12f);
            float b = pow.apply(tile.heat);
            float a = Interp.pow2Out.apply(tile.heat);
            Tmp.c1.set(r, g, b, a);
            Draw.color(Tmp.c1);
    
            Draw.blend(Blending.additive);
            Draw.rect(heatRegion, tile.x + tr2.x, tile.y + tr2.y, tile.rotation - 90);
            Draw.blend();
            Draw.color();
        };
    }

    @Override
    public void init(){
        super.init();

        requirements = PMUtls.randomizedItems(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3}, 0, 100000);
    }

    @Override
    public void setBars(){
        super.setBars();
        bars.add("pm-reload", (ChaosTurretBuild entity) -> new Bar(
            () -> {
                float ovd = entity.timeScale; //Overdrive
                float mul = entity.hasAmmo() ? entity.peekAmmo().reloadMultiplier : 1f; //Reload Multiplier
                Liquid liquid = entity.liquids.current();
                float reloadRate = 1f + consumes.<ConsumeLiquidBase>get(ConsumeType.liquid).amount * coolantMultiplier * liquid.heatCapacity;
                float result = reloadTime / (reloadTime / reloadRate);
                float liq = entity.liquids.currentAmount() > 0f ? result : 1f; //Coolant (stolen from BoosterStatListValue)
                float reloadSpeed = ovd * mul / liq;
                return Core.bundle.format("bar.pm-reload", Strings.fixed(Mathf.clamp((reloadTime - entity.reload) / reloadSpeed, 0f, reloadTime) / 60f, 1));
            },
            () -> entity.team.color,
            () -> entity.reload / reloadTime
        ));
    }

    public class ChaosTurretBuild extends PowerTurretBuild{
        protected Bullet bullet;

        @Override
        public void updateTile(){
            super.updateTile();

            if(active()){
                heat = 1f;
                recoil = recoilAmount;
                wasShooting = true;
            }
        }

        @Override
        protected void updateCooling(){
            if(consValid() && !active()){
                super.updateCooling();
            }
        }

        @Override
        protected void updateShooting(){
            if(consValid() && !active()){
                if(reload >= reloadTime && !charging){
                    BulletType type = peekAmmo();

                    shoot(type);

                    reload = 0f;
                }else{
                    reload += delta() * peekAmmo().reloadMultiplier * baseReloadSpeed();
                }
            }
        }

        @Override
        protected void shoot(BulletType type){
            requirements = PMUtls.randomizedItems(new int[]{0, 0, 0, 1, 1, 1, 2, 2, 4}, 0, 100000);
            
            useAmmo();

            tr.trns(rotation, shootLength);
            chargeBeginEffect.at(x + tr.x, y + tr.y, rotation, (Object)team);
            chargeSound.at(x + tr.x, y + tr.y, 1f);

            for(int i = 0; i < chargeEffects; i++){
                Time.run(Mathf.random(chargeMaxDelay), () -> {
                    if(!isValid()) return;
                    tr.trns(rotation, shootLength);
                    chargeEffect.at(x + tr.x, y + tr.y, rotation);
                });
            }

            charging = true;

            Time.run(chargeTime, () -> {
                if(!isValid()) return;
                tr.trns(rotation, shootLength);
                recoil = recoilAmount;
                heat = 1f;
                for(int i = 0; i < shots; i++){
                    bullet(type, rotation + Mathf.range(inaccuracy));
                }
                effects();
                charging = false;
            });
        }

        @Override
        protected void bullet(BulletType type, float angle){
            bullet = type.create(tile.build, team, x + tr.x, y + tr.y, angle);
        }

        public boolean active(){
            return bullet != null && bullet.time < bullet.lifetime ? true : false;
        }
    }
}
