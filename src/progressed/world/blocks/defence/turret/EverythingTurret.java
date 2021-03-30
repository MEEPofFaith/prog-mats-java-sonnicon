package progressed.world.blocks.defence.turret;

import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;
import progressed.ProgressedMaterials;
import progressed.graphics.*;
import progressed.util.*;

import static mindustry.Vars.*;

public class EverythingTurret extends PowerTurret{
    public float startingBias = 0.1f, maxBias = 6000f, growSpeed = 1.004f, shrinkSpeed = 0.05f;

    protected Seq<Object[]> bullets = new Seq<>();

    public EverythingTurret(String name){
        super(name);
        
        requirements(Category.turret, BuildVisibility.sandboxOnly, ItemStack.empty);
        shootLength = 0f;
        targetInterval = 1;
        minRange = 0f;
        shootType = Bullets.standardCopper;
        powerUse = 246810f/60f;
    }

    @Override
    public void init(){
        super.init();

        content.units().each(u -> {
            u.weapons.each(w -> {
                if(w.bullet != null && !w.bullet.killShooter){
                    BulletType bul = w.bullet;
                    Object[] data = new Object[]{bul, w.shootSound, bul.shootEffect, bul.smokeEffect, w.shake, bul.lifetime, false};
                    if(!bullets.contains(data)){
                        bullets.add(data);
                    }
                }
            });
        });
        content.blocks().each(b -> {
            if(b != this && b instanceof Turret){
                if(b instanceof LaserTurret block && block.shootType != null){
                    BulletType bul = block.shootType;
                    Effect fshootEffect = block.shootEffect == Fx.none ? bul.shootEffect : block.shootEffect;
                    Effect fsmokeEffect = block.smokeEffect == Fx.none ? bul.smokeEffect : block.smokeEffect;
                    Object[] data = new Object[]{bul, block.shootSound, fshootEffect, fsmokeEffect, block.shootShake, bul.lifetime + block.shootDuration, true};
                    if(!bullets.contains(data)){
                        bullets.add(data);
                    }
                }else if(b instanceof PowerTurret block && block.shootType != null){
                    BulletType bul = block.shootType;
                    Effect fshootEffect = block.shootEffect == Fx.none ? bul.shootEffect : shootEffect;
                    Effect fsmokeEffect = block.smokeEffect == Fx.none ? bul.smokeEffect : smokeEffect;
                    Object[] data = new Object[]{bul, block.shootSound, fshootEffect, fsmokeEffect, block.shootShake, bul.lifetime, false};
                    if(!bullets.contains(data)){
                        bullets.add(data);
                    }
                }else if(b instanceof ItemTurret block){
                    content.items().each(i -> {
                        if(block.ammoTypes.get(i) != null){
                            BulletType bul = block.ammoTypes.get(i);
                            Effect fshootEffect = block.shootEffect == Fx.none ? bul.shootEffect : shootEffect;
                            Effect fsmokeEffect = block.smokeEffect == Fx.none ? bul.smokeEffect : smokeEffect;
                            Object[] data = new Object[]{bul, block.shootSound, fshootEffect, fsmokeEffect, block.shootShake, bul.lifetime, false};
                            if(!bullets.contains(data)){
                                bullets.add(data);
                            }
                        }
                    });
                }
            }
        });

        bullets.sort(b -> PMUtls.bulletDamage((BulletType)(b[0]), (float)(b[5])));

        requirements = PMUtls.randomizedItems(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3}, 0, 100000);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.remove(Stat.ammo);
        stats.add(Stat.ammo, "[red]Everything");
    }

    public class EverythingTurretBuild extends PowerTurretBuild{
        public float drawBias, bias = startingBias, drawRot = Mathf.random(360f);
        public int selectedBullet;

        @Override
        public void updateTile(){
            super.updateTile();
            
            float lerp = PMUtls.customPowOut(20).apply(bias / maxBias);

            for(int i = 0; i < 1f + lerp * 100f; i++){
                if(Mathf.chanceDelta(1f)){ //I'm just copying over code I have no idea what the hell I'm looking at.
                    PMFx.everythingGunSwirl.at(x, y,
                        Mathf.random(lerp * 45f, lerp * 720f), team.color,
                        new float[]{
                            (4f + (lerp * 16f)) + Mathf.sin((Time.time + Mathf.randomSeed(id)) / 30f) * (lerp * 6f),
                            lerp * 420f + Mathf.sin((Time.time + Mathf.randomSeed(id + 1)) / 30f) * (lerp * 80f)
                        }
                    );
                }
            }
            
            drawRot = Mathf.mod(drawRot - Time.delta * lerp * 110f, 360f);
      
            if(isShooting() && consValid()){
                bias = Mathf.clamp(bias * Mathf.pow(growSpeed, edelta()), 0f, maxBias);
            }else{
                bias = Mathf.lerpDelta(bias, startingBias, shrinkSpeed);
            }

            ProgressedMaterials.print(bias);
        }

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);
            Draw.z(Layer.turret);
            Drawf.shadow(region, x - elevation, y - elevation, drawRot);
            Draw.rect(region, x, y, drawRot);
        }

        @Override
        protected void updateShooting(){
            if(reload >= reloadTime && !charging){
                selectedBullet = Mathf.clamp(Mathf.floor(1f / (((1f / Mathf.random()) - 1f) / bias + 1f) * bullets.size), 0, bullets.size - 1);

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
            float laserLifeScl = ((boolean)bullets.get(selectedBullet)[6]) ? ((float)bullets.get(selectedBullet)[5]) / type.lifetime : 1f;

            type.create(this, team, x + tr.x, y + tr.y, angle, 1f + Mathf.range(velocityInaccuracy), lifeScl * laserLifeScl);
        }

        @Override
        protected void effects(){
            ((Effect)bullets.get(selectedBullet)[2]).at(x, y, rotation, team.color);
            ((Effect)bullets.get(selectedBullet)[3]).at(x, y, rotation, team.color);
            ((Sound)bullets.get(selectedBullet)[1]).at(x, y, Mathf.random(0.9f, 1.1f));

            float shake = ((float)bullets.get(selectedBullet)[4]);
            if(shake > 0){
                Effect.shake(shake, shake, this);
            }
        }

        @Override
        protected void turnToTarget(float targetRot){
            rotation = targetRot;
        }

        @Override
        public BulletType useAmmo(){
            return ((BulletType)bullets.get(selectedBullet)[0]);
        }

        @Override
        public BulletType peekAmmo(){
            return ((BulletType)bullets.get(selectedBullet)[0]);
        }
    }
}
