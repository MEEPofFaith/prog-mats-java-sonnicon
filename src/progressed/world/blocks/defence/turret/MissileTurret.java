package progressed.world.blocks.defence.turret;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;

import static mindustry.Vars.*;

public class MissileTurret extends ItemTurret{
    public float[][] shootLocs = {{0f, 0f}};
    public TextureRegion[] heatRegions;
    public boolean reloadBar;

    public MissileTurret(String name){
        super(name);
        shootEffect = smokeEffect = Fx.none;
    }

    @Override
    public void load(){
        super.load();
        heatRegions = new TextureRegion[shootLocs.length];
        for(int i = 0; i < heatRegions.length; i++){
            heatRegions[i] = Core.atlas.find(name + "-heat-" + i);
        }
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{region};
    }

    @Override
    public void init(){
        super.init();
        shots = shootLocs.length;
    }

    
            
    @Override
    public void setBars(){
        super.setBars();
        if(reloadBar){
            bars.add("pm-reload", (MissileTurretBuild entity) -> new Bar(
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
    }

    public class MissileTurretBuild extends ItemTurretBuild{
        boolean firing;
        boolean[] shot = new boolean[shots];
        float speedScl;
        float[] heats = new float[shots];

        @Override
        public void draw(){
            Draw.rect(region, x, y);

            if(hasAmmo() && peekAmmo() != null){
                Draw.draw(Draw.z(), () -> {
                    for(int i = 0; i < shots; i++){
                        if(!shot[i]){
                            float tx = shootLocs[i][0] + x;
                            float ty = shootLocs[i][1] + y;
                            Drawf.construct(tx, ty, ((BasicBulletType)peekAmmo()).frontRegion, team.color, 0f, reload / reloadTime, speedScl, reload);
                        }
                    }
                });
            }

            for(int i = 0; i < shots; i++){
                if(Core.atlas.isFound(heatRegions[i]) && heats[i] > 0.001f){
                    Draw.color(heatColor, heats[i]);
                    Draw.blend(Blending.additive);
                    Draw.rect(heatRegions[i], x, y);
                    Draw.blend();
                    Draw.color();
                }
            }
        }

        @Override
        public void updateTile(){
            super.updateTile();
            for(int i = 0; i < shots; i++){
                heats[i] = Mathf.lerpDelta(heats[i], 0f, cooldown);
            }
              
            if(reload < reloadTime && hasAmmo() && consValid() && !firing){
                speedScl = Mathf.lerpDelta(speedScl, 1f, 0.05f);
            }else{
                speedScl = Mathf.lerpDelta(speedScl, 0f, 0.05f);
            }
        }

        @Override
        protected void updateCooling(){
            if(!firing && hasAmmo() && consValid()){
                float maxUsed = consumes.<ConsumeLiquidBase>get(ConsumeType.liquid).amount;

                Liquid liquid = liquids.current();
        
                float used = Math.min(Math.min(liquids.get(liquid), maxUsed * Time.delta), Math.max(0, ((reloadTime - reload) / coolantMultiplier) / liquid.heatCapacity)) * baseReloadSpeed();
                reload += used * liquid.heatCapacity * coolantMultiplier;
                liquids.remove(liquid, used);

                if(Mathf.chance(0.06 * used)){
                    coolEffect.at(x + Mathf.range(size * tilesize / 2f), y + Mathf.range(size * tilesize / 2f));
                }
            }
        }

        @Override
        protected void updateShooting(){
            if(hasAmmo() && consValid()){
                if(reload > reloadTime && !firing){
                    BulletType type = peekAmmo();
                  
                    shoot(type);
                }else if(!firing){
                    reload += Time.delta * peekAmmo().reloadMultiplier * baseReloadSpeed();
                }
            }
        }

        @Override
        protected void shoot(BulletType type){
            firing = true;
      
            for(int i = 0; i < shots; i++){
                final int sel = i;
                Time.run(burstSpacing * i, () -> {
                    if(!isValid() || !hasAmmo()) return;
                    float tx = shootLocs[sel][0] + x;
                    float ty = shootLocs[sel][1] + y;
                    
                    type.create(this, team, tx, ty, rotation + Mathf.range(inaccuracy), -1f, 1f + Mathf.range(velocityInaccuracy), 1f, new Object[]{tx, ty, 0f, false});
                    effects();
                    useAmmo();
                    heats[sel] = 1;
                    shot[sel] = true;
                });
            }
            
            Time.run(burstSpacing * shots, () -> {
                reload = 0f;
                firing = false;
                for(int i = 0; i < shots; i++){
                    shot[i] = false;
                }
            });
        }        

        @Override
        protected void turnToTarget(float targetRot){
            rotation = targetRot;
        }
    }
}