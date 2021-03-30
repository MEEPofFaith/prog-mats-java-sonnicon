package progressed.world.blocks.defence.turret;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import mindustry.world.meta.values.*;
import progressed.util.*;

import static mindustry.Vars.*;

public class ChaosTurret extends PowerTurret{
    public float shootDuration;

    public ChaosTurret(String name){
        super(name);

        requirements(Category.turret, BuildVisibility.sandboxOnly, ItemStack.empty);

        consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.01f)).update(false);
        coolantMultiplier = 1f;

        heatDrawer = tile -> {
            if(tile.heat <= 0.00001f) return;
            float r = Interp.pow2Out.apply(tile.heat);
            float g = (Interp.pow3In.apply(tile.heat) + ((1f - Interp.pow3In.apply(tile.heat)) * 0.12f)) / 2f;
            float b = PMUtls.customPowIn(6).apply(tile.heat);
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
    public void setStats(){
        super.setStats();

        stats.remove(Stat.booster);
        stats.add(Stat.input, new BoosterListValue(reloadTime, consumes.<ConsumeLiquidBase>get(ConsumeType.liquid).amount, coolantMultiplier, false, l -> consumes.liquidfilters.get(l.id)));
    }

    public class ChaosTurretBuild extends PowerTurretBuild{
        public Bullet bullet;

        @Override
        public void updateTile(){
            super.updateTile();

            if(active()){
                heat = 1f;
                recoil = recoilAmount;
                wasShooting = true;
            }

            if(reload < reloadTime){
                Liquid liquid = liquids.current();
                float maxUsed = consumes.<ConsumeLiquidBase>get(ConsumeType.liquid).amount;

                float used = (cheating() ? maxUsed * delta() : Math.min(liquids.get(liquid), maxUsed * delta())) * liquid.heatCapacity * coolantMultiplier;
                reload += used;
                liquids.remove(liquid, used);

                if(Mathf.chance(0.06 * used)){
                    coolEffect.at(x + Mathf.range(size * tilesize / 2f), y + Mathf.range(size * tilesize / 2f));
                }
            }
        }

        @Override
        protected void updateCooling(){
            //Do nothing, coolant is irrelevant here
        }

        @Override
        protected void updateShooting(){
            if(reload >= reloadTime && !charging && consValid() && !active()){
                BulletType type = peekAmmo();

                shoot(type);

                reload = 0f;
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
