package progressed.world.blocks.defence.turret;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.turrets.*;

import static arc.Core.*;

public class SniperTurret extends ItemTurret{
    public int partCount = 3;
    public float split, chargeMoveFract = 0.9f;

    protected TextureRegion[] outlines, parts;

    public SniperTurret(String name){
        super(name);
    }

    @Override
    public void load(){
        super.load();

        outlines = new TextureRegion[partCount];
        parts = new TextureRegion[partCount];
        
        for(int i = 0; i < partCount; i++){
            outlines[i] = atlas.find(name + "-outline-" + i);
            parts[i] = atlas.find(name + "-part-" + i);
        }
    }

    public class SniperTurretBuild extends ItemTurretBuild{
        public float charge;

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);

            Draw.z(Layer.turret);

            tr2.trns(rotation, -recoil);

            for(int i = 0; i < partCount; i++){
                float tx = Angles.trnsx(this.rotation, split * this.charge * i);
                float ty = Angles.trnsy(this.rotation, split * this.charge * i);
                Drawf.shadow(outlines[i], this.x + tr2.x + tx - elevation, this.y + tr2.y + ty - elevation, this.rotation - 90);
            }

            for(int i = 0; i < partCount; i++){
                float tx = Angles.trnsx(this.rotation, split * this.charge * i);
                float ty = Angles.trnsy(this.rotation, split * this.charge * i);
                Draw.rect(outlines[i], this.x + tr2.x + tx, this.y + tr2.y + ty, this.rotation - 90);
            }

            for(int i = 0; i < partCount; i++){
                float tx = Angles.trnsx(this.rotation, split * this.charge * i);
                float ty = Angles.trnsy(this.rotation, split * this.charge * i);
                Draw.rect(parts[i], this.x + tr2.x + tx, this.y + tr2.y + ty, this.rotation - 90);
            }
        }

        @Override
        public void updateTile(){
            if(charging && hasAmmo() && consValid()){
                charge = Mathf.clamp(charge + Time.delta / chargeTime);
            }else{
                charge = 0;
            }
    
            super.updateTile();
        }

        @Override
        protected void updateShooting(){
            if(consValid()){
                if(reload >= reloadTime && !charging){
                    BulletType type = peekAmmo();
        
                    shoot(type);
        
                    reload = 0;
                }else if(hasAmmo()){
                    reload += delta() * peekAmmo().reloadMultiplier * baseReloadSpeed();
                }
            }
        }

        @Override
        protected void updateCooling(){
            if(hasAmmo() && consValid()){
                super.updateCooling();
            }
        }
        
        @Override
        protected void turnToTarget(float targetRot){
            rotation = Angles.moveToward(rotation, targetRot, efficiency() * rotateSpeed * delta() * (charging ? (1 - chargeMoveFract * charge) : 1));
        }
        
        @Override
        public boolean shouldTurn(){
            return true;
        }
    }
}