package progressed.world.blocks.defence.turret;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.Units.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import mindustry.world.meta.values.*;
import progressed.ProgressedMaterials;
import progressed.content.*;
import progressed.entities.*;
import progressed.graphics.*;
import progressed.util.*;

import static mindustry.Vars.*;

public class SwordTurret extends BaseTurret{
    //after being logic-controlled and this amount of time passes, the turret will resume normal AI
    public static final float logicControlCooldown = 60 * 2;

    public final int timerTarget = timers++;
    public int targetInterval = 20;

    public boolean targetAir = true;
    public boolean targetGround = true;

    public int swords = 3;
    public float minRadius = tilesize, radius = 4f * tilesize, expandedRadius = -1;
    public float expandTime = 9f, pauseTime = 15f, stabTime = 18f, totalTime = 30f;
    public float attackRadius = 2f * tilesize, speed = 2f;
    public Color heatColor = Pal.lancerLaser;
    public float cooldown = 0.05f;
    public float minSpeed = 0.05f;

    public float bladeCenter, trailWidth = 8f;
    public Color trailColor = Color.white;
    public int trailLength = 7;

    public float damage = 300f, damageRadius = tilesize;
    public StatusEffect status = StatusEffects.none;
    public float statusDuration = 10 * 60;
    public Sound hitSound = PMSounds.swordStab;
    public float minVolume = 1f, maxVolume = 1f;
    public float minPitch = 0.9f, maxPitch = 1.1f;
    public Effect hitEffect = Fx.none;
    public Color hitColor = Color.white;
    public float hitShake;

    public float powerUse = 1f;

    public Sortf unitSort = Unit::dst2;

    public float elevation = -1f, swordElevation = -1f;

    protected TextureRegion baseRegion, outlineRegion, swordRegion, heatRegion;

    public SwordTurret(String name){
        super(name);
        hasPower = true;
        rotateSpeed = 4f;
        expanded = true;
    }

    @Override
    public void init(){
        consumes.powerCond(powerUse, SwordTurretBuild::isActive);
        if(elevation < 0) elevation = size / 2f;
        if(swordElevation < 0) swordElevation = elevation * 2f;
        if(expandedRadius < 0) expandedRadius = radius * 1.5f;

        super.init();
    }

    @Override
    public void load(){
        super.load();

        baseRegion = Core.atlas.find(name + "-base", "block-" + size);
        swordRegion = Core.atlas.find(name + "-sword");
        outlineRegion = Core.atlas.find(name + "-sword-outline");
        heatRegion = Core.atlas.find(name + "-sword-heat");
    }

    @Override
    protected TextureRegion[] icons(){
        return new TextureRegion[]{
            baseRegion,
            region
        };
    }

    @Override
    public void setStats(){
        super.setStats();

        if(acceptCoolant){
            stats.add(Stat.booster, new BoosterListValue(speed, consumes.<ConsumeLiquidBase>get(ConsumeType.liquid).amount, coolantMultiplier, true, l -> consumes.liquidfilters.get(l.id)));
        }

        stats.add(Stat.reload, PMUtls.stringsFixed(totalTime / 60f));
        stats.add(Stat.targetsAir, targetAir);
        stats.add(Stat.targetsGround, targetGround);

        stats.add(Stat.ammo, stat -> {
            stat.row();
            stat.table(t -> {
                t.left().defaults().padRight(3).left();

                t.add(Core.bundle.format("bullet.pm-sword-damage", damage, Strings.fixed(damageRadius / tilesize, 1)));
                t.row();

                if(status != StatusEffects.none){
                    t.add((status.minfo.mod == null ? status.emoji() : "") + "[stat]" + status.localizedName);
                    t.row();
                }

                t.add(Core.bundle.format("bullet.pm-sword-speed", speed));
            }).padTop(-9).left().get().background(Tex.underline);;
        });
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list){
        super.drawRequestRegion(req, list);

        for(int i = 0; i < swords; i++){
            float rot = (i + (swords % 4f) / 4f) * (360f / swords);
            Tmp.v1.trns(rot, -radius);
            Draw.rect(outlineRegion, req.drawx() + Tmp.v1.x, req.drawy() + Tmp.v1.y, rot);
        }
    }

    public class SwordTurretBuild extends BaseTurretBuild implements ExtensionHolder, ControlBlock{
        public Extension ext;
        public @Nullable Posc target;
        public Vec2 targetPos = new Vec2(), currentPos = new Vec2();
        protected float animationTime, coolantScl = 1f, logicControlTime = -1, lookAngle, heat;
        public BlockUnitc unit = Nulls.blockUnit;
        protected boolean logicShooting, wasAttacking, ready, traveling, hit;
        protected FixedTrail[] trails = new FixedTrail[swords];

        @Override
        public void created(){
            unit = (BlockUnitc)UnitTypes.block.create(team);
            unit.tile(this);
            currentPos.set(x, y);
            ext = Extension.create();
            ext.holder = this;
            ext.set(x, y);
            ext.add();
            for(int i = 0; i < swords; i++){
                trails[i] = new FixedTrail(trailLength);
            }
            ProgressedMaterials.print(rotation);
        }

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4){
            if(type == LAccess.shoot && !unit.isPlayer()){
                targetPos.trns(angleTo(World.unconv((float)p1), World.unconv((float)p2)), dst(World.unconv((float)p1), World.unconv((float)p2))).limit(range).add(this);
                logicControlTime = logicControlCooldown;
                logicShooting = !Mathf.zero(p3);
            }

            super.control(type, p1, p2, p3, p4);
        }

        @Override
        public void control(LAccess type, Object p1, double p2, double p3, double p4){
            if(type == LAccess.shootp && !unit.isPlayer()){
                logicControlTime = logicControlCooldown;
                logicShooting = !Mathf.zero(p2);

                if(p1 instanceof Posc p){
                    targetPosition(p);
                }
            }

            super.control(type, p1, p2, p3, p4);
        }

        @Override
        public double sense(LAccess sensor){
            //Ignore the "Java 14" error, it somehow works
            return switch(sensor){
                case ammo -> power.status;
                case shootX -> World.conv(targetPos.x);
                case shootY -> World.conv(targetPos.y);
                case shooting -> isAttacking() ? 1 : 0;
                default -> super.sense(sensor);
            };
        }

        /** @return whether this block is being controlled by a player. */
        public boolean isControlled(){
            return unit().isPlayer();
        }

        /** @return whether this block can be controlled at all. */
        public boolean canControl(){
            return true;
        }

        /** @return whether targets should automatically be selected (on mobile) */
        public boolean shouldAutoTarget(){
            return true;
        }

        public boolean isAttacking(){
            return (isControlled() ? unit.isShooting() : logicControlled() ? logicShooting : target != null);
        }

        @Override
        public Unit unit(){
            return (Unit)unit;
        }

        public boolean logicControlled(){
            return logicControlTime > 0f;
        }

        public boolean isActive(){
            return target != null || wasAttacking;
        }

        public void targetPosition(Posc pos){
            if(!consValid() || pos == null) return;
            Tmp.v1.trns(angleTo(pos), dst(pos)).limit(range).add(this);
            targetPos.set(Predict.intercept(currentPos, Tmp.v1, speed));
        }

        @Override
        public void draw(){
            Draw.rect(baseRegion, x, y);

            Draw.z(Layer.turret);

            Drawf.shadow(region, x - elevation, y - elevation, lookAngle);
            Draw.rect(region, x, y, lookAngle);
        }

        @Override
        public void drawExt(){
            Draw.mixcol();

            Tmp.c1.set(trailColor).lerp(heatColor, heat);
            Draw.z(Layer.flyingUnit + 0.002f);

            for(FixedTrail t : trails){
                t.draw(Tmp.c1, trailWidth);
            }

            for(int i = 0; i < swords; i++){
                float rot = rotation + i * (360f / swords);

                Tmp.v1.trns(rot, -getRadius());

                float sX = currentPos.x + Tmp.v1.x, sY = currentPos.y + Tmp.v1.y;

                Draw.z(Layer.flyingUnit + 0.001f);
                Drawf.shadow(outlineRegion, sX - swordElevation, sY - swordElevation, rot + getRotation(i));

                Draw.z(Layer.flyingUnit + 0.003f);
                Draw.rect(outlineRegion, sX, sY, rot + getRotation(i));
            }

            for(int i = 0; i < swords; i++){
                float rot = rotation + i * (360f / swords);

                Tmp.v1.trns(rot, -getRadius());

                float sX = currentPos.x + Tmp.v1.x, sY = currentPos.y + Tmp.v1.y;

                Draw.z(Layer.flyingUnit + 0.004f);
                Draw.rect(swordRegion, sX, sY, rot + getRotation(i));

                if(ready && heat > 0f){
                    Draw.color(heatColor, heat);
                    Draw.blend(Blending.additive);
                    Draw.rect(heatRegion, sX, sY, rot + getRotation(i));
                    Draw.color();
                    Draw.blend();
                }
            }
        }

        protected float getRotation(int sword){
            float expand = Mathf.curve(animationTime, 0f, expandTime);
            float endRot = Mathf.curve(animationTime, stabTime + (totalTime - stabTime) * 0.2f, totalTime);
            float drawRot = -90 * expand + -270f * endRot;
            return drawRot;
        }

        protected float getRadius(){
            float expand = Mathf.curve(animationTime, 0f, expandTime);
            float pause = Mathf.curve(animationTime, expandTime, pauseTime);
            float attack = Mathf.curve(animationTime, pauseTime, stabTime);
            float reset = Mathf.curve(animationTime, stabTime, totalTime);
            float progress = (expand + pause + attack + reset) / 4f;
            return PMUtls.multiLerp(new float[]{radius, expandedRadius, expandedRadius, minRadius, radius}, progress);
        }

        protected void turnTo(float target){
            lookAngle = Angles.moveToward(lookAngle, target - 90f, speed * 3f * cdelta() * efficiency());
        }

        @Override
        public void updateTile(){
            if(!validateTarget()) target = null;

            wasAttacking = false;

            unit.health(health);
            unit.rotation(rotation);
            unit.team(team);
            unit.set(x, y);

            if(logicControlTime > 0){
                logicControlTime -= Time.delta;
            }

            if(consValid()){

                if(!ready && !validateTarget() && timer(timerTarget, targetInterval)){
                    findTarget();
                }

                if(validateTarget()){
                    boolean canAttack = true;

                    if(isControlled()){ //player behavior
                        targetPos.trns(unit.angleTo(unit.aimX(), unit.aimY()), unit.dst(unit.aimX(), unit.aimY())).limit(range).add(this);
                        canAttack = unit.isShooting();
                    }else if(logicControlled()){ //logic behavior
                        canAttack = logicShooting;
                    }else{ //default AI behavior
                        targetPosition(target);
                    }

                    if(canAttack){
                        wasAttacking = true;
                        moveTo(targetPos, true);
                        updateCooling();
                    }else if(!ready){
                        reset();
                    }
                }else if(!ready){
                    reset();
                }
            }else if(!ready){
                reset();
            }

            if(ready){
                animationTime += cdelta() * efficiency();
                if(animationTime >= pauseTime && animationTime <= stabTime){
                    heat = Mathf.curve(animationTime, pauseTime, stabTime);
                }else{
                    heat = Mathf.lerpDelta(heat, 0f, cooldown);
                }
                if(animationTime >= stabTime && !hit){
                    hit = true;
                    hitEffect.at(currentPos.x, currentPos.y, hitColor);
                    for(int i = 0; i < swords; i++){
                        hitSound.at(currentPos.x, currentPos.y, Mathf.random(minPitch, maxPitch), Mathf.random(minVolume, maxVolume));
                    }
                    if(hitShake > 0f){
                        Effect.shake(hitShake, hitShake, this);
                    }
                    //Slow speed, weak hit -> * efficiency()
                    Damage.damage(team, currentPos.x, currentPos.y, damageRadius, damage * efficiency(), true, targetAir, targetGround);
                    if(status != StatusEffects.none){
                        Damage.status(team, currentPos.x, currentPos.y, damageRadius, status, statusDuration * efficiency(), targetAir, targetGround);
                    }
                }
                if(animationTime > totalTime){
                    if(!validateTarget() || !isAttacking() || currentPos.dst(targetPos) > attackRadius || !consValid()){
                        ready = false; //do not stop until dead or unable to attack
                        target = null;
                    }
                    hit = false;
                    animationTime = 0f;
                }
            }else{
                heat = Mathf.lerpDelta(heat, 0f, cooldown);
                animationTime = 0f;
            }

            if(dst(currentPos) > size / 2f || isAttacking()) turnTo(angleTo(currentPos));
            rotation = (rotation - rotateSpeed * cdelta() * efficiency()) % 360f;

            for(int i = 0; i < swords; i++){
                float rot = rotation + i * (360f / swords);

                Tmp.v1.trns(rot, -getRadius());

                float sX = currentPos.x + Tmp.v1.x, sY = currentPos.y + Tmp.v1.y;

                Tmp.v2.trns(rot + getRotation(i) + 90f, bladeCenter);

                trails[i].update(sX + Tmp.v2.x, sY + Tmp.v2.y, rot + getRotation(i));
            }
        }

        protected void updateCooling(){
            float maxUsed = consumes.<ConsumeLiquidBase>get(ConsumeType.liquid).amount;

            Liquid liquid = liquids.current();

            float used = Math.min(Math.min(liquids.get(liquid), maxUsed * Time.delta), Math.max(0, (speed / coolantMultiplier) / liquid.heatCapacity));
            coolantScl = 1f + used;
            liquids.remove(liquid, used);

            if(Mathf.chance(0.06 * used)){
                coolEffect.at(x + Mathf.range(size * tilesize / 2f), y + Mathf.range(size * tilesize / 2f));
            }
        }

        protected void reset(){
            ready = false;
            coolantScl = 1f;
            moveTo(x, y, false);
        }

        protected void moveTo(Vec2 pos, boolean readyUp){
            float angle = currentPos.angleTo(pos);
            float dist = currentPos.dst(pos);
            boolean checkDst = dist < attackRadius;
            if(checkDst && readyUp){
                ready = true;
            }
            Tmp.v1.trns(angle, speed * cdelta() * efficiency()).limit(dist);
            currentPos.add(Tmp.v1);
        }

        protected void moveTo(float x, float y, boolean readyUp){
            moveTo(Tmp.v1.set(x, y), readyUp);
        }

        protected boolean validateTarget(){
            return (!Units.invalidateTarget(target, team, x, y) || isControlled() || logicControlled());
        }

        protected void findTarget(){
            if(targetAir && !targetGround){
                target = Units.bestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded(), unitSort);
            }else{
                target = Units.bestTarget(team, x, y, range, e -> !e.dead() && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround), b -> true, unitSort);
            }
        }

        protected float cdelta(){
            return delta() * coolantScl;
        }

        @Override
        public float efficiency(){
            //disabled -> 0.1f efficiency
            if(!enabled) return 0.1f;
            return power != null && (block.consumes.has(ConsumeType.power) && !block.consumes.getPower().buffered) ? (0.1f + power.status * 0.9f) : 1f;
        }

        @Override
        public float clipSizeExt(){
            return range + getRadius() + swordRegion.height / 2f;
        }

        @Override
        public void onRemoved(){
            super.onRemoved();
            ext.remove();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.bool(ready);
            write.bool(hit);
            write.f(animationTime);
            write.f(currentPos.x);
            write.f(currentPos.y);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            if(revision >= 1){
                ready = read.bool();
                hit = read.bool();
                animationTime = read.f();
                currentPos.set(read.f(), read.f());
            }
        }

        @Override
        public byte version(){
            return 1;
        }
    }
}