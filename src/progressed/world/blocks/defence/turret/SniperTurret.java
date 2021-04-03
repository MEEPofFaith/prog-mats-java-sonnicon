package progressed.world.blocks.defence.turret;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;
import progressed.ui.*;
import progressed.util.*;

import static arc.Core.*;

public class SniperTurret extends ItemTurret{
    public int partCount = 3;
    public float split, chargeMoveFract = 0.9f;

    protected TextureRegion[] outlines, parts;

    public SniperTurret(String name){
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.remove(Stat.ammo);
        stats.add(Stat.ammo, new PMAmmoListValue<>(ammoTypes));
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

    @Override
    public void setBars(){
        super.setBars();
        bars.add("pm-reload", (SniperTurretBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pm-reload", PMUtls.stringsFixed(Mathf.clamp(entity.reload / reloadTime) * 100f)),
            () -> entity.team.color,
            () -> Mathf.clamp(entity.reload / reloadTime, 0f, reloadTime)
        ));

        bars.add("pm-charge", (SniperTurretBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pm-charge", PMUtls.stringsFixed(Mathf.clamp(entity.charge) * 100f)),
            () -> Pal.surge,
            () -> entity.charge
        ));
    }

    public class SniperTurretBuild extends ItemTurretBuild{
        protected float charge;

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
                }else if(hasAmmo() && reload < reloadTime){
                    reload += delta() * peekAmmo().reloadMultiplier * baseReloadSpeed();
                }
            }
        }

        @Override
        protected void shoot(BulletType type){
            tr.trns(rotation, shootLength);
            chargeBeginEffect.at(x + tr.x, y + tr.y, rotation);
            chargeSound.at(x + tr.x, y + tr.y, 1);

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
                bullet(type, rotation + Mathf.range(inaccuracy));
                useAmmo();
                effects();
                reload = 0;
                charging = false;
            });
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