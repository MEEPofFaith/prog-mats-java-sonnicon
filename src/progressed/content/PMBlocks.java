package progressed.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import multilib.*;
import multilib.Recipe.*;
import progressed.graphics.*;
import progressed.ui.*;
import progressed.util.*;
import progressed.world.blocks.crafting.*;
import progressed.world.blocks.defence.*;
import progressed.world.blocks.defence.turret.*;
import progressed.world.blocks.defence.turret.EruptorTurret.*;
import progressed.world.blocks.distribution.*;
import progressed.world.blocks.sandbox.*;

import static mindustry.type.ItemStack.*;
import static mindustry.Vars.*;

public class PMBlocks implements ContentList{
    public static Block

    // Region Turrets

    //Miniguns
    minigun, miinigun, mivnigun,

    //Teslas
    shock, spark, storm,

    //Eruptors
    flame, blaze, inferno,
    
    //Swords
    masquerade, violet,

    //Pixel Turrets
    bit,

    //Magnets
    magnet,

    //Crit Sniper(s)
    caliber,

    //Misc
    signal, tinker,

    //Why do I hear anxiety piano
    sentinel,

    //Missiles
    firestorm, strikedown, arbiter,

    //Misc
    blackhole, excalibur,

    // endregion
    // Region Distribution
    
    floatingConveyor, burstDriver,

    // endregion
    // Region Crafting

    //Crafters
    mindronCollider, shellPress, missileFactory, sentryBuilder,

    // endregion
    // Region Effect

    fence, web, shieldProjector,

    // endregion
    // Region Sandbox

    //Turret
    harbinger, everythingGun,

    //Distribution
    sandDriver,
    
    //Power
    strobeNode, strobeInf, strobeBoost, 

    //Defense
    sandboxWall, sandboxWallLarge,

    //Unit
    godFactory, capBlock,

    //Effect
    multiSource, multiVoid, multiSourceVoid;

    // endregion

    @Override
    public void load(){

        // Region Turrets

        minigun = new MinigunTurret("minigun"){{
            requirements(Category.turret, with(
                Items.copper, 200,
                Items.graphite, 175,
                Items.titanium, 100,
                Items.thorium, 80
            ));
            ammo(
                Items.copper, PMBullets.standardCopperMini,
                Items.graphite, PMBullets.standardDenseMini,
                Items.silicon, PMBullets.standardHomingMini,
                Items.pyratite, PMBullets.standardIncendiaryMini,
                Items.thorium, PMBullets.standardThoriumMini
            );
            size = 4;
            range = 255f;
            maxSpeed = 0.75f;
            health = 140 * size * size;
            shootCone = 35f;
            shootSound = Sounds.shootBig;
            targetAir = targetGround = true;
            recoilAmount = 3f;
            restitution = 0.02f;
            cooldown = 0.11f;
            inaccuracy = 3f;
            shootEffect = smokeEffect = ammoUseEffect = Fx.none;
            heatColor = Pal.turretHeat;

            barX = 4f;
            barY = -10f;
            barStroke = 1f;
            barLength = 9f;

            shootLocs = new float[]{0f};
            windupSpeed = 0.0001875f;
            windDownSpeed = 0.003125f;
            minFiringSpeed = 1f/12f;
        }};

        miinigun = new MinigunTurret("miinigun"){{
            requirements(Category.turret, with(
                Items.copper, 350,
                Items.graphite, 300,
                Items.titanium, 150,
                Items.plastanium, 175,
                Items.thorium, 170,
                PMItems.techtanite, 120
            ));
            ammo(
                Items.copper, PMBullets.standardCopperMini,
                Items.graphite, PMBullets.standardDenseMini,
                Items.silicon, PMBullets.standardHomingMini,
                Items.pyratite, PMBullets.standardIncendiaryMini,
                Items.thorium, PMBullets.standardThoriumMini
            );
            size = 4;
            range = 255f;
            maxSpeed = 0.73f;
            health = 150 * size * size;
            shootCone = 35f;
            shootSound = Sounds.shootBig;
            targetAir = targetGround = true;
            recoilAmount = 3f;
            restitution = 0.02f;
            cooldown = 0.11f;
            inaccuracy = 3f;
            shootEffect = smokeEffect = ammoUseEffect = Fx.none;
            heatColor = Pal.turretHeat;

            barX = 4f;
            barY = -10f;
            barStroke = 1f;
            barLength = 9f;

            shootLocs = new float[]{-4f, 4f};
            windupSpeed = 0.0001875f;
            windDownSpeed = 0.003125f;
            minFiringSpeed = 1f/12f;
        }};

        mivnigun = new MinigunTurret("mivnigun"){{
            requirements(Category.turret, with(
                Items.copper, 650,
                Items.graphite, 600,
                Items.titanium, 370,
                Items.thorium, 340,
                Items.plastanium, 325,
                Items.surgeAlloy, 220,
                PMItems.techtanite, 270
            ));
            ammo(
                Items.copper, PMBullets.standardCopperMini,
                Items.graphite, PMBullets.standardDenseMini,
                Items.silicon, PMBullets.standardHomingMini,
                Items.pyratite, PMBullets.standardIncendiaryMini,
                Items.thorium, PMBullets.standardThoriumMini
            );
            size = 4;
            range = 255f;
            maxSpeed = 0.71f;
            health = 160 * size * size;
            shootCone = 35f;
            shootSound = Sounds.shootBig;
            targetAir = targetGround = true;
            recoilAmount = 3f;
            restitution = 0.02f;
            cooldown = 0.11f;
            inaccuracy = 3f;
            shootEffect = smokeEffect = ammoUseEffect = Fx.none;
            heatColor = Pal.turretHeat;

            barX = 5f;
            barY = -10f;
            barStroke = 1f;
            barLength = 9f;

            shootLocs = new float[]{-9f, -3f, 3f, 9f};
            windupSpeed = 0.0001875f;
            windDownSpeed = 0.003125f;
            minFiringSpeed = 1f/12f;
        }};

        shock = new TeslaTurret("shock"){{
            requirements(Category.turret, with(
                Items.copper, 45,
                Items.lead, 60,
                Items.silicon, 25,
                Items.titanium, 25
            ));
            rings.add(
                new TeslaRing(0.75f),
                new TeslaRing(2.5f)
            );
            size = 1;
            health = 310;
            powerUse = 3.6f;
            reloadTime = 30f;
            range = 72f;
            rangeExtention = 8f;
            shots = 2;
            zaps = 4;
            zapAngleRand = 27f;
            inaccuracy = 32f;
            shootType = PMBullets.shockZap;
        }};

        spark = new TeslaTurret("spark"){{
            requirements(Category.turret, with(
                Items.copper, 60,
                Items.lead, 85,
                Items.graphite, 40,
                Items.silicon, 55,
                Items.titanium, 80
            ));
            rings.add(
                new TeslaRing(2f),
                new TeslaRing(6f)
            );
            size = 2;
            health = 200 * size * size;
            powerUse = 4.8f;
            reloadTime = 20f;
            range = 130f;
            rangeExtention = 16f;
            shots = 2;
            zaps = 5;
            zapAngleRand = 19f;
            inaccuracy = 28f;
            shootType = PMBullets.sparkZap;
        }};

        storm = new TeslaTurret("storm"){{
            requirements(Category.turret, ItemStack.with(
                Items.copper, 120,
                Items.lead, 150,
                Items.graphite, 55,
                Items.silicon, 105,
                Items.titanium, 90,
                Items.surgeAlloy, 40,
                PMItems.techtanite, 50
            ));
            rings.addAll(
                new TeslaRing(1f),
                new TeslaRing(3.25f),
                new TeslaRing(6.5f),
                //Spinner 1
                new TeslaRing(4.25f){{ //TL
                    hasSprite = true;
                    drawUnder = true;
                    xOffset = -8.625f;
                    yOffset = 8.625f;
                    rotationMul = 12f;
                }},
                new TeslaRing(4.25f){{ //TR
                    drawUnder = true;
                    xOffset = yOffset = 8.625f;
                    rotationMul = 12f;
                }},
                new TeslaRing(4.25f){{ //BL
                    drawUnder = true;
                    xOffset = yOffset = -8.625f;
                    rotationMul = 12f;
                }},
                new TeslaRing(4.25f){{ //BR
                    drawUnder = true;
                    xOffset = 8.625f;
                    yOffset = -8.625f;
                    rotationMul = 12f;
                }},
                //Spinner 2
                new TeslaRing(1f){{ //Tl
                    hasSprite = true;
                    drawUnder = true;
                    xOffset = -7.625f;
                    yOffset = 7.625f;
                    rotationMul = -12f;
                }},
                new TeslaRing(1f){{ //TT
                    drawUnder = true;
                    xOffset = yOffset = 7.625f;
                    rotationMul = -12f;
                }},
                new TeslaRing(1f){{ //Bl
                    drawUnder = true;
                    xOffset = yOffset = -7.625f;
                    rotationMul = -12f;
                }},
                new TeslaRing(1f){{ //BR
                    drawUnder = true;
                    xOffset = 7.625f;
                    yOffset = -7.625f;
                    rotationMul = -12f;
                }}
            );
            size = 3;
            health = 180 * size * size;
            powerUse = 8.9f;
            reloadTime = 10f;
            range = 210f;
            rangeExtention = 24f;
            shots = 3;
            zaps = 7;
            zapAngleRand = 13f;
            inaccuracy = 28f;
            coolantMultiplier = 1f;
            spinUp = 0.005f;
            spinDown = 0.0125f;
            hasSpinners = true;
            shootType = PMBullets.stormZap;
        }};

        flame = new EruptorTurret("flame"){{
            requirements(Category.turret, ItemStack.with(
                Items.copper, 200,
                Items.lead, 300,
                Items.graphite, 300,
                Items.silicon, 325,
                Items.titanium, 200,
                Items.thorium, 200
            ));
            float h = 0.75f;
            cells.add(
                new EruptorCell(-h, -h),
                new EruptorCell(h, -h),
                new EruptorCell(-h, h),
                new EruptorCell(h, h)
            );
            size = 3;
            health = 210 * size * size;
            powerUse = 14f;
            shootCone = 10f;
            range = 240f;
            rangeExtention = 40f;
            reloadTime = 90f;
            shootLength = 5f / 4f;
            recoilAmount = 3f;
            shootDuration = 180f;
            shootType = PMBullets.flameMagma;

            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 1f/3f)).update(false);
        }};

        blaze = new EruptorTurret("blaze"){{
            requirements(Category.turret, with(
                Items.copper, 350,
                Items.lead, 550,
                Items.graphite, 550,
                Items.silicon, 600,
                Items.titanium, 350,
                Items.surgeAlloy, 200,
                PMItems.techtanite, 200
            ));
            float h = 0.9f;
            cells.addAll(
                new EruptorCell(-h, -h),
                new EruptorCell(h, -h),
                new EruptorCell(-h, h),
                new EruptorCell(h, h),
                new EruptorCell(-h, 0f, 2),
                new EruptorCell(h, 0f, 2),
                new EruptorCell(-h, h, 2),
                new EruptorCell(h, h, 2)
            );
            size = 4;
            health = 190 * size * size;
            powerUse = 17f;
            shootDuration = 240f;
            range = 280f;
            rangeExtention = 60f;
            reloadTime = 150f;
            shootLength = 11f / 4f;
            rotateSpeed = 3.5f;
            recoilAmount = 4f;
            lightningStroke = 6f;
            layers = 2;
            shootType = PMBullets.blazeMagma;

            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.75f)).update(false);
        }};

        inferno = new InfernoTurret("inferno"){{
            requirements(Category.turret, with(
                Items.copper, 700,
                Items.lead, 950,
                Items.graphite, 750,
                Items.silicon, 800,
                Items.titanium, 600,
                Items.thorium, 800,
                Items.surgeAlloy, 650,
                PMItems.techtanite, 600
            ));
            float h = 0.5f;
            cells.add(
                new EruptorCell(-h, h),
                new EruptorCell(h, h),
                new EruptorCell(-h, h, 2),
                new EruptorCell(h, h, 2)
            );
            size = 4;
            health = 200 * size * size;
            powerUse = 23f;
            recoilAmount = 8f;
            range = 200f;
            rangeExtention = 60f;
            reloadTime = 240f;
            shootLength = 42f / 4f;
            shootDuration = 60f;
            rotateSpeed = 8f;
            layers = 2;
            shootType = PMBullets.infernoMagma;

            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 1f)).update(false);
        }};

        masquerade = new SwordTurret("masquerade"){{
            requirements(Category.turret, with(
                Items.copper, 500,
                Items.graphite, 250,
                Items.silicon, 350,
                Items.titanium, 200,
                Items.phaseFabric, 50,
                PMItems.techtanite, 150
            ));
            size = 3;
            health = 340 * size * size;
            range = 180f;
            powerUse = 6.5f;
            minRadius = 16.5f;
            bladeCenter = 9f;
            trailWidth = 30f / 4f;
        }};

        violet = new SwordTurret("violet"){
            {
                requirements(Category.turret, with(
                    Items.copper, 1400,
                    Items.graphite, 350,
                    Items.silicon, 400,
                    Items.surgeAlloy, 400,
                    Items.phaseFabric, 200,
                    PMItems.techtanite, 450
                ));
                size = 5;
                health = 230 * size * size;
                range = 260f;
                powerUse = 13.5f;
                damage = 1000f;
                bladeCenter = 122f / 8f;
                trailWidth = 18f;
                trailLength = 6;
                float attackScl = 1.25f;
                damageRadius *= attackScl;
                attackRadius *= attackScl;
                swords = 5;
                minRadius = 33.25f;
                radius = 6.25f * tilesize;
                float timeScl = 0.9f;
                expandTime *= timeScl;
                pauseTime *= timeScl;
                stabTime *= timeScl;
                totalTime *= timeScl;
                cooldown *= timeScl;
                speed = 3f;
                rotateSpeed = 4.5f;
            }

            @Override
            public void load(){
                super.load();

                baseRegion = Core.atlas.find("prog-mats-block-" + size);
            }
        };

        bit = new BitTurret("bit"){{
            requirements(Category.turret, with(
                Items.copper, 50,
                Items.lead, 60,
                Items.silicon, 40,
                Items.titanium, 30
            ));
            size = 2;
            health = 300 * size * size;
            reloadTime = 70f;
            rotateSpeed = 10f;
            recoilAmount = 4f;
            inaccuracy = 15f;
            range = 140f;
            powerUse = 1.35f;
            shootType = PMBullets.pixel;
        }};

        magnet = new ItemTurret("attraction"){
            {
                requirements(Category.turret, empty);
                ammo(
                    Items.copper, PMBullets.magnetCopper,
                    Items.titanium, PMBullets.magnetTitanium,
                    PMItems.techtanite, PMBullets.magnetTechtanite
                );
                size = 3;
                health = 90 * size * size;
                range = 23f * 8f;
                reloadTime = 200f;
                inaccuracy = 30f;
                velocityInaccuracy = 0.2f;
                burstSpacing = 5f;
                shots = 4;
            }

            @Override
            public void setStats(){
                super.setStats();

                stats.remove(Stat.ammo);
                stats.add(Stat.ammo, new PMAmmoListValue<>(ammoTypes));
            }
        
            @Override
            public void setBars(){
                super.setBars();
                bars.add("pm-reload", (ItemTurretBuild entity) -> new Bar(
                    () -> Core.bundle.format("bar.pm-reload", PMUtls.stringsFixed(Mathf.clamp(entity.reload / reloadTime) * 100f)),
                    () -> entity.team.color,
                    () -> Mathf.clamp(entity.reload / reloadTime)
                ));
            }
        };

        caliber = new SniperTurret("caliber"){{
            requirements(Category.turret, with(
                Items.copper, 220,
                Items.titanium, 200,
                Items.thorium, 150,
                Items.plastanium, 110,
                PMItems.techtanite, 60
            ));
            ammo(
                Items.titanium, PMBullets.sniperBoltTitanium,
                Items.thorium, PMBullets.sniperBoltThorium,
                Items.silicon, PMBullets.sniperBoltSilicon,
                PMItems.techtanite, PMBullets.sniperBoltTechtanite,
                Items.surgeAlloy, PMBullets.sniperBoltSurge
            );
            size = 3;
            health = 120 * size * size;
            reloadTime = 450f;
            inaccuracy = 0f;
            range = 544f;
            rotateSpeed = 2.5f;
            recoilAmount = 5f;
            split = 3f;
            chargeTime = 150f;
            shootSound = Sounds.railgun;
        }};

        signal = new SignalFlareTurret("signal"){{
            requirements(Category.turret, with(
                Items.lead, 80,
                Items.silicon, 130,
                Items.plastanium, 110,
                PMItems.techtanite, 90
            ));
            ammo(
                Items.silicon, PMBullets.smallFlare,
                PMItems.techtanite, PMBullets.mediumFlare,
                Items.surgeAlloy, PMBullets.largeFlare
            );
            size = 2;
            health = 250 * size * size;
            minRange = 5f * tilesize;
            range = 60f * tilesize;
            shootLength = 23f / 4f;
            reloadTime = 900f;
            inaccuracy = 10f;
            velocityInaccuracy = 0.2f;
            shootSound = Sounds.shootSnap;
            maxAmmo = 30;
            ammoPerShot = 10;
        }};

        tinker = new LaunchTurret("tinker"){{
            requirements(Category.turret, with(
                Items.copper, 125,
                Items.lead, 75,
                Items.silicon, 30,
                Items.titanium, 50
            ));
            ammo(
                PMItems.basicSentryBox, PMBullets.basicSentryLaunch,
                PMItems.strikeSentryBox, PMBullets.strikeSentryLaunch,
                PMItems.dashSentryBox, PMBullets.dashSentryLaunch
            );
            size = 3;
            health = 140 * size * size;
            reloadTime = 60f * 10f;
            minRange = 5f * tilesize;
            range = 40 * tilesize;
            velocityInaccuracy = 0.2f;
            cooldown = 0.03f;
            recoilAmount = 6f;
            restitution = 0.02f;
            shootShake = 2f;
            shootLength = 16f;
        }};

        sentinel = new AimLaserTurret("sentinel"){{
            requirements(Category.turret, with(
                Items.copper, 900,
                Items.lead, 375,
                Items.graphite, 350,
                Items.surgeAlloy, 450,
                Items.silicon, 450,
                PMItems.techtanite, 250
            ));

            size = 4;
            health = 120 * size * size;
            
            shootLength = 11f;
            range = 328f;
            reloadTime = 600f;

            powerUse = 29f;

            float mul = 3.5f;
            coolantUsage *= mul;
            coolantMultiplier /= mul;

            chargeTime = PMFx.aimChargeBegin.lifetime;
            chargeBeginEffect = PMFx.aimChargeBegin;
            chargeEffect = PMFx.aimCharge;
            chargeEffects = 30;
            chargeMaxDelay = PMFx.aimChargeBegin.lifetime - PMFx.aimCharge.lifetime;

            chargeSound = PMSounds.sentenelCharge; //Note so I don't forget: Sound is made with AnaloguePiano2 in LMMS
            shootSound = Sounds.laserblast;
            chargeSoundVolume = 2f;
            minPitch = 0.7f;
            shootSoundVolume = 0.75f;

            recoilAmount = 3f;
            restitution = 0.02f;
            cooldown = 0.005f;

            aimRnd = 16f;

            shootType = PMBullets.sentinelLaser;
            unitSort = (u, x, y) -> -u.maxHealth + u.dst2(x, y) / 6400f;

            heatDrawer = tile -> {
                if(tile.heat <= 0.00001f) return;
        
                Draw.color(tile.team.color, tile.heat);
                Draw.blend(Blending.additive);
                Draw.rect(heatRegion, tile.x + tr2.x, tile.y + tr2.y, tile.rotation - 90);
                Draw.blend();
                Draw.color();
            };
        }};

        firestorm = new MissileTurret("firestorm"){{
            requirements(Category.turret, with(
                Items.copper, 180,
                Items.graphite, 140,
                Items.silicon, 65,
                Items.titanium, 70
            ));
            ammo(
                Items.blastCompound, PMBullets.firestormMissile
            );
            size = 3;
            health = 120 * size * size;
            range = 160f;
            reloadTime = 75f;
            shootSound = Sounds.missile;
            cooldown = 0.01f;
            shootShake = 1f;
            targetAir = false;
            burstSpacing = 7f;
            inaccuracy = 15f;
            maxAmmo = 36;
            shootLocs = new float[][]{
                {-31f/4f, 31f/4f}, //TL
                {31f/4f, 31f/4f}, //TR
                {-31f/4f, -31f/4f}, //BL
                {31f/4f, -31f/4f}, //BR
                {0f, 29f/4f}, //T
                {-29f/4f, 0f}, //L
                {0f, -29f/4f}, //B
                {29f/4f, 0f}, //R
                {0f, 0f} //C
            };
        }};

        strikedown = new MissileTurret("strikedown"){{
            requirements(Category.turret, with(
                Items.copper, 70,
                Items.lead, 350,
                Items.graphite, 300,
                Items.silicon, 300,
                Items.titanium, 250,
                PMItems.techtanite, 120
            ));
            ammo(
                PMItems.basicMissile, PMBullets.strikedownBasic,
                PMItems.empMissile, PMBullets.strikedownEmp,
                PMItems.quantiumMissile, PMBullets.strikedownQuantum,
                PMItems.recursiveMissile, PMBullets.strikedownRecursive
            );
            reloadBar = true;
            size = 4;
            health = 160 * size * size;
            range = 330f;
            reloadTime = 180f;
            shootSound = Sounds.artillery;
            cooldown = 0.001f;
            shootShake = 5f;
            inaccuracy = 5f;
            maxAmmo = 8;
            unitSort = (u, x, y) -> -u.maxHealth + u.dst2(x, y) / 6400f;
        }};

        arbiter = new MissileTurret("arbiter"){{
            requirements(Category.turret, with(
                Items.copper, 4000,
                Items.graphite, 2200,
                Items.silicon, 2000,
                Items.titanium, 1300,
                Items.thorium, 650,
                Items.surgeAlloy, 200,
                PMItems.techtanite, 800
            ));
            ammo(
                PMItems.basicNuke, PMBullets.arbiterBasic,
                PMItems.empNuke, PMBullets.arbiterEmp,
                PMItems.clusterNuke, PMBullets.arbiterCluster,
                PMItems.sentryNuke, PMBullets.arbiterSentry
            );
            reloadBar = true;
            size = 7;
            health = 170 * size * size;
            range = 4400f;
            shootSound = Sounds.explosionbig;
            cooldown = 0.001f;
            shootShake = 10f;
            reloadTime = 1500f;
            maxAmmo = 2;
            unitSort = (u, x, y) -> -u.maxHealth + u.dst2(x, y) / 6400f;
        }};

        blackhole = new BlackHoleTurret("blackhole"){{
            requirements(Category.turret, with(
                Items.titanium, 100,
                Items.thorium, 150,
                Items.plastanium, 250,
                Items.surgeAlloy, 250,
                Items.silicon, 800,
                Items.phaseFabric, 500,
                PMItems.techtanite, 500
            ));
            size = 4;
            health = 230 * size * size;
            canOverdrive = false;
            reloadTime = 520f;
            range = 256f;
            shootEffect = smokeEffect = Fx.none;
            chargeBeginEffect = PMFx.kugelblitzChargeBegin;
            chargeEffect = PMFx.kugelblitzCharge;
            chargeMaxDelay = 30f;
            chargeEffects = 16;
            chargeTime = PMFx.kugelblitzChargeBegin.lifetime;
            rotateSpeed = 2f;
            recoilAmount = 2f;
            restitution = 0.015f;
            cooldown = 0.005f;
            shootLength = 0f;
            shootSound = Sounds.release;
            shootType = PMBullets.blackHole;
        }};

        excalibur = new PopeshadowTurret("excalibur"){
            {
                requirements(Category.turret, with(
                    Items.copper, 1200,
                    Items.lead, 1100,
                    Items.graphite, 800,
                    Items.silicon, 1500,
                    Items.titanium, 800,
                    Items.thorium, 700,
                    Items.plastanium, 350,
                    Items.surgeAlloy, 450,
                    PMItems.techtanite, 800
                ));
                size = 6;
                health = 140 * size * size;
                reloadTime = 450f;
                range = 740f;
                shootEffect = smokeEffect = Fx.none;
                shootLength = 0f;
                cooldown = 0.0075f;
                heatColor = Pal.surge;
                chargeTime = 180f;
                chargeSound = PMSounds.popeshadowCharge;
                shootSound = PMSounds.popeshadowBlast;
                rotateSpeed = 2f;
                recoilAmount = 8f;
                restitution = 0.05f;
                shootType = PMBullets.excaliburLaser;
            }

            @Override
            public void load(){
                super.load();

                baseRegion = Core.atlas.find("prog-mats-block-" + size);
            }
        };
        
        // endregion
        // Region Distribution

        floatingConveyor = new FloatingConveyor("floating-conveyor"){{
            requirements(Category.distribution, with(
                Items.lead, 3,
                Items.metaglass, 3,
                Items.plastanium, 3,
                PMItems.techtanite, 3
            ));
            health = 15;
            speed = 0.08f;
            deepSpeed = 0.06f;
            displayedSpeed = 11f;
            deepDisplayedSpeed = 8.4f;
            buildCostMultiplier = 0.25f;
            researchCostMultiplier = 300f;
        }};

        burstDriver = new BurstDriver("burst-driver"){{
            requirements(Category.distribution, with(
                Items.titanium, 275,
                Items.silicon, 200,
                Items.lead, 350,
                Items.thorium, 125,
                PMItems.techtanite, 75
            ));
            size = 3;
            itemCapacity = 180;
            reloadTime = 120f;
            shots = 90;
            delay = 0.75f;
            range = 560f;
            consumes.power(2.75f);
        }};

        // endregion
        // Region Crafting

        mindronCollider = new ColliderCrafter("mindron-collider"){{
            requirements(Category.crafting, with(
                Items.silicon, 150,
                Items.metaglass, 50,
                Items.plastanium, 80,
                Items.thorium, 100,
                Items.surgeAlloy, 110
            ));
            size = 5;
            craftTime = 300f;
            itemCapacity = 15;
            consumes.power(8f);
            consumes.items(with(
                Items.titanium, 3,
                Items.thorium, 3
            ));
            outputItem = new ItemStack(PMItems.techtanite, 2);
        }};

        shellPress = new MultiCrafter("shell-press", 2){{
            requirements(Category.crafting, with(
                Items.copper, 75,
                Items.lead, 100,
                Items.titanium, 100,
                Items.silicon, 80
            ));
            addRecipe(
                new InputContents(with(Items.copper, 5, Items.lead, 5, Items.titanium, 5), 3f),
                new OutputContents(with(PMItems.missileShell, 2)),
                60f
            );
            addRecipe(
                new InputContents(with(Items.titanium, 10, Items.surgeAlloy, 10, PMItems.techtanite, 10), 5f),
                new OutputContents(with(PMItems.nukeShell, 1)),
                90f, true
            );
            size = 3;
            itemCapacity = 30;
            craftEffect = Fx.pulverizeMedium;
            updateEffect = Fx.none;
            dumpToggle = true;
        }};

        missileFactory = new MissileCrafter("missile-factory", 8){{
            requirements(Category.crafting, with(
                Items.copper, 300,
                Items.lead, 200,
                Items.silicon, 200,
                Items.plastanium, 150,
                Items.thorium, 100,
                Items.surgeAlloy, 110
            ));
            
            // Region Missiles

            addRecipe(//Baisc Missile
                new InputContents(with(PMItems.missileShell, 1, Items.thorium, 3, Items.blastCompound, 6), 3f),
                new OutputContents(with(PMItems.basicMissile, 1)),
                60f
            );
            addRecipe(//EMP Missile
                new InputContents(with(PMItems.missileShell, 1, Items.lead, 12, Items.titanium, 10, Items.silicon, 10), 4f),
                new OutputContents(with(PMItems.empMissile, 1)),
                75f, true
            );
            addRecipe(//Quantium Missile
                new InputContents(with(PMItems.missileShell, 1, Items.thorium, 8, Items.phaseFabric, 13, Items.silicon, 10), 7f),
                new OutputContents(with(PMItems.quantiumMissile, 1)),
                90f, true
            );
            addRecipe(//Recursive Missile
                new InputContents(with(PMItems.missileShell, 1, PMItems.basicMissile, 2, Items.copper, 15, Items.plastanium, 10, Items.silicon, 10), 5f),
                new OutputContents(with(PMItems.recursiveMissile, 1)),
                80f, true
            );

            // endregion
            // Region Nukes

            addRecipe(//Basic Nuke
                new InputContents(with(PMItems.nukeShell, 1, Items.titanium, 25, Items.thorium, 35, Items.blastCompound, 25), 6f),
                new OutputContents(with(PMItems.basicNuke, 1)),
                90f, true
            );
            addRecipe(//EMP Nuke
                new InputContents(with(PMItems.nukeShell, 1, Items.silicon, 30, Items.surgeAlloy, 20, PMItems.techtanite, 40), 8f),
                new OutputContents(with(PMItems.empNuke, 1)),
                105f, true
            );
            addRecipe(//Cluster Nuke
                new InputContents(with(PMItems.nukeShell, 1, PMItems.basicMissile, 5, Items.copper, 30, Items.plastanium, 15, PMItems.techtanite, 25), 6.25f),
                new OutputContents(with(PMItems.clusterNuke, 1)),
                120f, true
            );
            addRecipe(//Sentry Nuke
                new InputContents(with(PMItems.nukeShell, 1, PMItems.basicSentryBox, 3, PMItems.strikeSentryBox, 3, PMItems.dashSentryBox, 3, Items.pyratite, 10, Items.blastCompound, 5), 5.5f),
                new OutputContents(with(PMItems.sentryNuke, 1)),
                150f, true
            );

            // endregion

            size = 4;
            itemCapacity = 50;
            craftEffect = Fx.pulverizeMedium;
            updateEffect = Fx.none;
            dumpToggle = true;
        }};

        sentryBuilder = new SentryCrafter("sentry-builder", 3){{
            requirements(Category.crafting, with(
                Items.copper, 90,
                Items.lead, 80,
                Items.titanium, 60,
                Items.silicon, 150
            ));
            addRecipe(//Basic Sentry
                new InputContents(with(Items.copper, 30, Items.lead, 35, Items.titanium, 15, Items.silicon, 25), 4f),
                new OutputContents(with(PMItems.basicSentryBox, 3)),
                90f, true
            );
            addRecipe(//Strike Sentry
                new InputContents(with(Items.copper, 40, Items.lead, 40, Items.titanium, 20, Items.silicon, 30, Items.blastCompound, 10), 4.5f),
                new OutputContents(with(PMItems.strikeSentryBox, 3)),
                120f
            );
            addRecipe(//Dash Sentry
                new InputContents(with(Items.copper, 30, Items.lead, 30, Items.titanium, 30, Items.graphite, 15, Items.silicon, 35), 5.25f),
                new OutputContents(with(PMItems.dashSentryBox, 3)),
                105f
            );
            size = 4;
            itemCapacity = 100;
            craftEffect = updateEffect = Fx.none;
            dumpToggle = true;
        }};

        // endregion
        // Region Effect

        fence = new StaticNode("fence"){{
            requirements(Category.effect, with(
                Items.copper, 60,
                Items.lead, 50,
                Items.silicon, 20
            ));
            size = 1;
            health = 90;
            laserRange = 35;
            damage = 7f;
            powerPerLink = 1.2f;
        }};

        web = new StaticNode("web"){{
            requirements(Category.effect, with(
                Items.copper, 70,
                Items.lead, 35,
                Items.silicon, 25
            ));
            size = 1;
            health = 110;
            laserRange = 17;
            maxNodes = 6;
            damage = 4f;
            powerPerLink = 0.5f;
        }};

        shieldProjector = new ShieldProjector("shield-projector"){{
            requirements(Category.effect, with(
                Items.lead, 325,
                Items.titanium, 225,
                Items.surgeAlloy, 75,
                PMItems.techtanite, 125
            ));
            size = 4;
            radius = 88f;
            shieldHealth = 2600f;
            phaseShieldBoost = 1800f;
            cooldownBrokenBase *= 2f;

            consumes.items(with(Items.phaseFabric, 1, PMItems.techtanite, 1)).boost();
            consumes.power(7f);
        }};

        // endregion
        // Region Sandbox

        //Turret
        harbinger = new ChaosTurret("harbinger"){
            {
                size = 8;
                health = 999999999;
                shots = 100;
                inaccuracy = 45f;
                shootShake = 150f;
                powerUse = 300f;
                range = 560f;
                recoilAmount = 8f;
                rotateSpeed = 0.3f;
                shootCone = 20f;
                cooldown = 0.0015f;
                restitution = 0.008f;
                reloadTime = 450f;
                chargeTime = PMFx.harbingerCharge.lifetime;
                chargeBeginEffect = PMFx.harbingerCharge;
                chargeSound = PMSounds.harbingerCharge;
                shootSound = PMSounds.harbingerBlast;
                shootType = PMBullets.harbingerLaser;
            }

            @Override
            public void init(){
                super.init();
                shootLength -= 16f;
            }

            @Override
            public void load(){
                super.load();
                baseRegion = Core.atlas.find("prog-mats-block-" + size);
            }
        };

        everythingGun = new EverythingTurret("everything-gun"){
            {
                size = 6;
                health = 999999999;
                reloadTime = 1f;
                range = 4400f;
                shootCone = 360f;
            }

            @Override
            public void load(){
                super.load();
                baseRegion = Core.atlas.find("prog-mats-block-" + size);
            }
        };

        // Distribution
        sandDriver = new SandDriver("sand-driver"){{
            size = 3;
            itemCapacity = 180;
            reloadTime = 120f;
            shots = 90;
            delay = 0.75f;
            range = 560f;
            consumes.power(0.1f);
        }};

        //Power
        strobeNode = new StrobeNode("rainbow-power-node");

        strobeInf = new StrobeSource("rainbow-power-source");

        strobeBoost = new StrobeSource("rainbow-power-boost"){{
            size = 2;
            boost = true;
            speedBoost = 100f;
        }};

        //Wall
        sandboxWall = new SandboxWall("sandbox-wall"){{
            iconSize = 3f;
            rotateRadius = 2.5f;
        }};

        sandboxWallLarge = new SandboxWall("sandbox-wall-large"){{
            size = 2;
            iconSize = 6f;
            rotateRadius = 5f;
        }};

        //Unit
        godFactory = new UnitFactory("god-factory"){{
            requirements(Category.units, BuildVisibility.sandboxOnly, empty);
            alwaysUnlocked = true;

            size = 3;
            health = 999999999;
            plans = Seq.with(
                new UnitPlan(PMUnitTypes.everythingUnit, 60f * 10f, empty)
            );

            consumes.power(1f);
        }};

        capBlock = new CapBlock("cap-block"){{
            health = 10000;
            unitCapModifier = 25;
        }};

        //Effect
        multiSource = new MultiSource("multi-source");
        multiVoid = new MultiVoid("multi-void");
        multiSourceVoid = new MultiSourceVoid("multi-source-void");

        // endregion
    }
}