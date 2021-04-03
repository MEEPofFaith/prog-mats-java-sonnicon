package progressed.world.blocks.defence.turret;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.defense.turrets.*;
import progressed.util.*;

public class LaunchTurret extends ItemTurret{
    public int arrows = 5;
    public float warmupSpeed = 0.05f, chargeupSpeed = 0.07f, animSpeed = 0.05f, sep = 0.5f, back = 0f, end = -6f, pauseTime = 1.25f;
    public Effect launchEffect = Fx.none, launchSmokeEffect = Fx.none;

    protected TextureRegion[] arrowRegions;
    protected TextureRegion topRegion;

    public LaunchTurret(String name){
        super(name);
        chargeTime = 10f;
        shootEffect = smokeEffect = Fx.none;
        shootSound = Sounds.artillery;
        maxAmmo = 3;
    }

    @Override
    public void load(){
        super.load();

        arrowRegions = new TextureRegion[arrows];
        for(int i = 0; i < arrows; i++){
            arrowRegions[i] = Core.atlas.find(name + "-arrow-" + i);
        }
        topRegion = Core.atlas.find(name + "-top");
    }

    @Override
    public void setBars(){
        super.setBars();
        bars.add("pm-reload", (LaunchTurretBuild entity) -> new Bar(
            () -> Core.bundle.format("bar.pm-reload", PMUtls.stringsFixed(Mathf.clamp(entity.reload / reloadTime) * 100f)),
            () -> entity.team.color,
            () -> entity.reload / reloadTime
        ));
    }

    public class LaunchTurretBuild extends ItemTurretBuild{
        protected float warmup, speed, current, offset, charge;
        protected boolean collantReloading;

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);
            Draw.color();

            Draw.z(Layer.turret);

            tr2.trns(rotation, -recoil);

            Drawf.shadow(region, x + tr2.x - elevation, y + tr2.y - elevation, rotation - 90f);
            drawer.get(this);

            if(warmup > 0.01f){
                Draw.color(team.color);
                for(int i = 0; i < arrows; i++){
                    Draw.alpha(Mathf.clamp(Mathf.sin(current - i * sep, 1, warmup)));
                    Draw.rect(arrowRegions[i], x + tr2.x, y + tr2.y, rotation - 90f);
                }
            }
            Draw.color();

            if(hasAmmo() && consValid()){
                TextureRegion sentryRegion = ((BasicBulletType)peekAmmo()).frontRegion;
                float dst = (shootLength + sentryRegion.height / 4f) - back;
                float hdst = dst / 2f;
                float s = size * Vars.tilesize;
      
                Rect rect1 = Tmp.r1.setCentered(x + tr2.x, y + tr2.y, s, dst).move(0f, recoil - hdst);
                Rect rect2 = Tmp.r2.setCentered(x + tr2.x, y + tr2.y, sentryRegion.width / 4f, sentryRegion.height / 4f).move(0f, recoil - offset);
      
                TextureRegion clipped = clipRegion(rect1, rect2, sentryRegion);
      
                Tmp.v1.trns(rotation, offset - clipped.height / 2f * Draw.scl);
                Draw.rect(clipped, x + tr2.x + Tmp.v1.x, y + tr2.y + Tmp.v1.y, rotation - 90f);
            }
              
            Draw.rect(topRegion, x + tr2.x, y + tr2.y, rotation - 90f);

            if(Core.atlas.isFound(heatRegion)){
                heatDrawer.get(this);
            }
        }

        @Override
        protected void updateShooting(){
            if(consValid()){
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
        protected void updateCooling(){
            if(hasAmmo() && consValid()){
                if(reload < reloadTime){
                    collantReloading = true;
                }else{
                    collantReloading = false;
                }
                super.updateCooling();
            }else{
                collantReloading = false;
                reload = 0f;
            }
        }

        @Override
        public void updateTile(){
            float scl = isShooting() ? 1f : 0.5f; //Make it seem "half active" when just reloading with coolant.
            if((isShooting() || collantReloading) && hasAmmo() && consValid()){
                warmup = Mathf.lerpDelta(warmup, 1f, warmupSpeed * scl);
                speed = Mathf.lerpDelta(warmup, 1f, chargeupSpeed * scl);
            }else{
                warmup = Mathf.lerpDelta(warmup, 0f, warmupSpeed);
                speed = Mathf.lerpDelta(warmup, 0f, chargeupSpeed);
            }
            current += animSpeed * speed * scl * Time.delta;

            if(speed < 0.01f || warmup < 0.01f){
                current = 0f;
            }
            
            if(charging && hasAmmo()){
                charge = Mathf.clamp(charge + Time.delta / chargeTime);
                TextureRegion sentryRegion = ((BasicBulletType)peekAmmo()).frontRegion;
                offset = Mathf.lerp(back, shootLength + sentryRegion.height / 8f, charge);
            }else{
                charge = 0f;
                offset = Mathf.lerp(end, back, Mathf.clamp(reload / reloadTime * pauseTime));
            }

            super.updateTile();
        }

        @Override
        protected void shoot(BulletType type){
            preEffects();

            tr.trns(rotation, shootLength - recoil);

            chargeBeginEffect.at(x + tr.x, y + tr.y, rotation);
            chargeSound.at(x + tr.x, y + tr.y, 1);

            for(int i = 0; i < chargeEffects; i++){
                Time.run(Mathf.random(chargeMaxDelay), () -> {
                    if(!isValid()) return;
                    tr.trns(rotation, shootLength - recoil);
                    chargeEffect.at(x + tr.x, y + tr.y, rotation);
                });
            }

            charging = true;

            Time.run(chargeTime, () -> {
                if(!isValid()) return;
                tr.trns(rotation, shootLength - recoil);
                bullet(type, rotation + Mathf.range(inaccuracy));
                effects();
                useAmmo();
                charging = false;
            });
        }

        public void preEffects(){
            recoil = recoilAmount;
            heat = 1f;

            if(shootShake > 0f){
                Effect.shake(shootShake, shootShake, this);
            }

            if(hasAmmo()){
                TextureRegion sentryRegion = ((BasicBulletType)peekAmmo()).frontRegion;
                Tmp.v1.trns(rotation, back - sentryRegion.height / 8f - recoil);

                launchEffect.at(x + Tmp.v1.x, y + Tmp.v1.y, rotation);	
                launchSmokeEffect.at(x + Tmp.v1.x, y + Tmp.v1.y, rotation);	
                
                shootSound.at(x + Tmp.v1.x, y + Tmp.v1.y, Mathf.random(0.9f, 1.1f));
            }
        }

        @Override
        protected void effects() {
            Effect fshootEffect = shootEffect == Fx.none ? peekAmmo().shootEffect : shootEffect;
            Effect fsmokeEffect = smokeEffect == Fx.none ? peekAmmo().smokeEffect : smokeEffect;

            fshootEffect.at(x + tr.x, y + tr.y, rotation);
            fsmokeEffect.at(x + tr.x, y + tr.y, rotation);
        }

        @Override
        public void handleItem(Building source, Item item){
            reload = 0f; //Sorry, but you can't just replace a half-loaded bullet. Gotta restart.
            super.handleItem(source, item);
        }

        //Steal from PayloadConveyor
        protected TextureRegion clipRegion(Rect bounds, Rect sprite, TextureRegion region){
            Rect over = Tmp.r3;

            boolean overlaps = Intersector.intersectRectangles(bounds, sprite, over);

            TextureRegion out = Tmp.tr1;
            out.set(region.texture);

            if(overlaps){
                float w = region.u2 - region.u;
                float h = region.v2 - region.v;
                float x = region.u, y = region.v;
                float newX = (over.x - sprite.x) / sprite.width * w + x;
                float newY = (over.y - sprite.y) / sprite.height * h + y;
                float newW = (over.width / sprite.width) * w, newH = (over.height / sprite.height) * h;

                out.set(newX, newY, newX + newW, newY + newH);
            }else{
                out.set(0f, 0f, 0f, 0f);
            }

            return out;
        }
    }
}