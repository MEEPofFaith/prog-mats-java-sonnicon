package progressed.world.blocks.defence.turret;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;
import progressed.graphics.*;
import progressed.util.*;

public class TeslaTurret extends PowerTurret{
    public Seq<TeslaRing> rings = new Seq<>();
    public boolean hasSpinners;
    public Color lightningColor;
    public float zapAngleRand, spinUp, spinDown, rangeExtention;
    public int zaps;

    protected TextureRegion[] ringRegions, heatRegions, outlineRegions;
    protected TextureRegion bottomRegion, topRegion;

    public TeslaTurret(String name){
        super(name);
        shootCone = 306f;
        lightningColor = Pal.surge;
        shootSound = Sounds.spark;
        shootEffect = Fx.sparkShoot;
        cooldown = 0.04f;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.remove(Stat.inaccuracy);
        if(inaccuracy > 0f) stats.add(Stat.inaccuracy, inaccuracy / Vars.tilesize, StatUnit.blocks);
    }

    @Override
    public void load(){
        super.load();

        ringRegions = new TextureRegion[rings.size];
        heatRegions = new TextureRegion[rings.size];
        outlineRegions = new TextureRegion[rings.size];
        
        for(int i = 0; i < ringRegions.length; i++){
            if(rings.get(i).hasSprite){
                ringRegions[i] = Core.atlas.find(name + "-ring-" + i);
                outlineRegions[i] = Core.atlas.find(name + "-outline-" + i);
            }
            heatRegions[i] = Core.atlas.find(name + "-heat-" + i);
        }

        if(hasSpinners) bottomRegion = Core.atlas.find(name + "-bottom");
        topRegion = Core.atlas.find(name + "-top");
    }

    @Override
    public void init(){
        super.init();

        if(rings.size == 0){
            throw new RuntimeException(name + " does not have any rings!");
        }
    }

    public class TeslaRing implements Cloneable{ //Create different rings out of this
        public boolean drawUnder, hasSprite;
        public float rotationMul, radius, xOffset, yOffset;

        public TeslaRing(float radius){
            this.radius = radius;
        };

        public TeslaRing copy(){
            try{
                return (TeslaRing)clone();
            }catch(CloneNotSupportedException suck){
                throw new RuntimeException("very good language design", suck);
            }
        }
    }
    
    public class TeslaTurretBuild extends PowerTurretBuild{
        public Seq<Teamc> targets = new Seq<>();
        public float[] heats = new float[rings.size];
        public float speedScl;

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);

            Draw.z(Layer.turret);

            for(int i = 0; i < rings.size; i++){
                TeslaRing ring = rings.get(i);
                if(ring.hasSprite){
                    Drawf.shadow(ringRegions[i], x - elevation, y - elevation, rotation * ring.rotationMul - 90f);
                }
            }

            TextureRegion r = hasSpinners ? bottomRegion : region;

            Drawf.shadow(r, x - elevation, y - elevation);

            Draw.rect(r, x, y);

            for(int i = 0; i < rings.size; i++){
                TeslaRing ring = rings.get(i);
                if(ring.hasSprite){
                    Draw.rect(outlineRegions[i], x, y, rotation * ring.rotationMul - 90f);
                }
            }

            for(int i = 0; i < rings.size; i++){
                TeslaRing ring = rings.get(i);
                if(ring.drawUnder){
                    if(ring.hasSprite) Draw.rect(ringRegions[i], x, y, rotation * ring.rotationMul - 90f);

                    if(heats[i] > 0.00001f){
                        Draw.color(heatColor, heats[i]);
                        Draw.blend(Blending.additive);
                        Draw.rect(heatRegions[i], x, y, rotation * ring.rotationMul - 90f);
                        Draw.blend();
                        Draw.color();
                    }
                }
            }

            Draw.rect(topRegion, x, y);

            for(int i = 0; i < rings.size; i++){
                TeslaRing ring = rings.get(i);
                if(!ring.drawUnder){
                    if(ring.hasSprite) Draw.rect(ringRegions[i], x, y, rotation * ring.rotationMul - 90f);

                    if(heats[i] > 0.00001f){
                        Draw.color(heatColor, heats[i]);
                        Draw.blend(Blending.additive);
                        Draw.rect(heatRegions[i], x, y, rotation * ring.rotationMul - 90f);
                        Draw.blend();
                        Draw.color();
                    }
                }
            }
        }

        @Override
        public void updateTile(){
            for(int i = 0; i < heats.length; i++){
                heats[i] = Mathf.lerpDelta(heats[i], 0f, cooldown);
            }

            if(!hasAmmo() || !isShooting() || !isActive() || !cons.valid()){
                    speedScl = Mathf.lerpDelta(speedScl, 0, spinDown);
                }
                if(hasAmmo() && isShooting() && isActive() && cons.valid()){
                    Liquid liquid = liquids.current();
                    speedScl = Mathf.lerpDelta(speedScl, 1, spinUp * peekAmmo().reloadMultiplier * liquid.heatCapacity * coolantMultiplier * edelta());
                }
                
                rotation -= speedScl * Time.delta;
            
            super.updateTile();
        }

        @Override
        protected void shoot(BulletType type){
            targets.clear();

            targets = PMUtls.allNearbyEnemies(team, x, y, range + rangeExtention);

            if(targets.size > 0){
                for(int i = 0; i < shots; i++){
                    TeslaRing ring = rings.random();
                    Teamc target = targets.random();

                    heats[rings.indexOf(ring)] = 1f;

                    tr.trns(rotation * ring.rotationMul, ring.xOffset, ring.yOffset);
                    tr2.setLength(ring.radius).setToRandomDirection();

                    Tmp.v1.setLength(Mathf.random(inaccuracy)).setToRandomDirection(); //inaccuracy

                    float shootX = x + tr.x + tr2.x + Tmp.v1.x, shootY = y + tr.y + tr2.y + Tmp.v1.y;

                    float shootAngle = Angles.angle(shootX, shootY, target.x(), target.y());
                    float dist = Mathf.dst(shootX, shootY, target.x(), target.y());

                    PMFx.fakeLightning.at(shootX, shootY,shootAngle, lightningColor, new Object[]{dist, 5f, team});
                    shootSound.at(shootX, shootY,shootAngle, Mathf.random(0.9f, 1.1f));
                    shootEffect.at(shootX, shootY, shootAngle, lightningColor);
                    
                    final float spawnX = target.x(), spawnY = target.y();
                    Time.run(3f, () -> {
                        for(int j = 0; j < zaps; j++){
                            shootType.create(this, team, spawnX, spawnY, ((360f / zaps) * j) + Mathf.range(zapAngleRand));
                        }
                    });
                }
            }
        }

        @Override
        protected void turnToTarget(float targetRot){
            //DO nothing, turning is irrelevant
        }

        @Override
        public boolean canControl(){
            return false; //Can't aim, technically does not shoot
        }
    }
}