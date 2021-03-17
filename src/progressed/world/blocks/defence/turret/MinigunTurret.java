package progressed.world.blocks.defence.turret;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;

public class MinigunTurret extends ItemTurret{
    public float windupSpeed, windDownSpeed, minFiringSpeed;
    public float barX, barY, barStroke, barLength;
    public float[] shootLocs;
    public TextureRegion[] turretRegions = new TextureRegion[3], outlineRegions = new TextureRegion[3], heatRegions = new TextureRegion[12];

    public MinigunTurret(String name){
        super(name);
    }

    @Override
    public void load(){
        super.load();

        for(int i = 0; i < 3; i ++){
            turretRegions[i] = Core.atlas.find(name + "-frame-" + i);
            outlineRegions[i] = Core.atlas.find(name + "-outline-" + i);
        }
        for(int i = 0; i < 12; i++){
            heatRegions[i] = Core.atlas.find(name + "-heat-" + i);
        }
        region = turretRegions[0];
    }

    public class MinigunTurretBuild extends ItemTurretBuild{
        public float[] heats = {0f, 0f, 0f, 0f};
        public int[] heatFrames = {0, 0, 0, 0};
        int frame, barrel;
        float frameSpeed, trueFrame;
        boolean shouldShoot, shouldBarrel;

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);

            Draw.z(Layer.turret);

            tr2.trns(rotation, -recoil);

            Drawf.shadow(outlineRegions[frame], x + tr2.x - elevation, y + tr2.y - elevation, rotation - 90f);
            Draw.rect(outlineRegions[frame], x + tr2.x, y + tr2.y, rotation - 90f);
            Draw.rect(turretRegions[frame], x + tr2.x, y + tr2.y, rotation - 90f);

            for(int i = 0; i < 4; i++){
                if(heats[i] > 0.001f){
                    Draw.blend(Blending.additive);
                    Draw.color(heatColor, heats[i]);
                    Draw.rect(heatRegions[heatFrames[i]], x + tr2.x, y + tr2.y, rotation - 90f);
                    Draw.blend();
                    Draw.color();
                }
            }

            if(frameSpeed > 0f){
                Draw.color(team.color);
                Lines.stroke(barStroke);
                for(int i = 0; i < 2; i++){
                    tr2.trns(rotation - 90f, barX * Mathf.signs[i], barY - recoil);
                    Lines.lineAngle(x + tr2.x, y + tr2.y, rotation, barLength * frameSpeed);
                }
            }
        }

        @Override
        public void updateTile(){
            super.updateTile();

            if(!hasAmmo() || !isShooting() || !isActive()){
                frameSpeed = Mathf.lerpDelta(frameSpeed, 0, windDownSpeed);
            }

            trueFrame = trueFrame + frameSpeed * delta();
            frame = Mathf.floor(trueFrame % 3f);
            for(int i = 0; i < 4; i++){
                heatFrames[i] = Mathf.mod(Mathf.floor(trueFrame % 12) - (i * 3), 12);
                heats[i] = Mathf.lerpDelta(heats[i], 0f, cooldown);
            }

            if(frame != 0){
                shouldShoot = true;
                shouldBarrel = true;
            }else if(shouldBarrel){
                barrel = barrel + 1;
                shouldBarrel = false;
            }
        }

        @Override
        protected void updateShooting(){
            if(hasAmmo()){
                float maxUsed = consumes.<ConsumeLiquidBase>get(ConsumeType.liquid).amount;

                Liquid liquid = liquids.current();

                float used = Math.min(Math.min(liquids.get(liquid), maxUsed * Time.delta), Math.max(0, ((reloadTime - reload) / coolantMultiplier) / liquid.heatCapacity)) * baseReloadSpeed();
                frameSpeed = Mathf.lerpDelta(frameSpeed, 1f, windupSpeed * (1 + used) * liquid.heatCapacity * coolantMultiplier * peekAmmo().reloadMultiplier);
                liquids.remove(liquid, used);

                if(frame == 0 && shouldShoot && frameSpeed > minFiringSpeed){
                    BulletType type = peekAmmo();

                    shoot(type);

                    shouldShoot = false;
                    heats[barrel % 4] = 1f;
                }
            }
        }
        
        @Override
        protected void shoot(BulletType type){
            for(int i = 0; i < shootLocs.length; i++){
                if(hasAmmo()){
                    tr.trns(rotation - 90, shootLocs[i], shootLength);
                    bullet(type, rotation + Mathf.range(inaccuracy + type.inaccuracy));
                    effects();
                    useAmmo();
                }
            }
        }

        @Override
        protected void updateCooling(){
            //Do nothing, cooling is already in `updateShooting()`
        }
    }
}