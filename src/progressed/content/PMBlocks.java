package progressed.content;

import arc.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import multilib.*;
import multilib.Recipe.*;
import progressed.graphics.*;
import progressed.world.blocks.crafting.*;
import progressed.world.blocks.defence.turret.*;
import progressed.world.blocks.defence.wall.*;
import progressed.world.blocks.distribution.*;
import progressed.world.blocks.power.*;

import static mindustry.type.ItemStack.*;
import static mindustry.Vars.*;

public class PMBlocks implements ContentList{
    public static Block

    // Region Turrets

    //Eruptors
    flame, blaze, inferno,

    //Miniguns
    minigun, miinigun, mivnigun,

    //Teslas
    shock, spark, storm,

    //Crit Snipers
    caliber,

    //Pixel Turrets
    bit,

    //Misc
    tinker,

    //Missiles
    firestorm, strikedown, arbiter,

    //Misc
    blackhole, excalibur,

    //Sandbox
    harbinger, everythingGun,

    // endregion
    // Region Distribution
    
    floatingConveyor,

    // endregion
    // Region Power
    
    //Sandbox Power
    strobeNode, strobeInf, strobeBoost,

    // endregion
    // Region Walls

    sandboxWall, sandboxWallLarge,

    // endregion
    // Region Crafting

    //Crafters
    mindronCollider, shellPress, missileFactory, sentryBuilder;

    // endregion

    @Override
    public void load(){

        // Region Turrets

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
            health = 1800;
            powerUse = 14f;
            shootCone = 10f;
            range = 240f;
            rangeExtention = 40f;
            reloadTime = 90f;
            recoilAmount = 3f;
            rotateSpeed = 3f;
            shootDuration = 180f;
            shootType = PMBullets.flameMagma;

            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.5f)).update(false);
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
            health = 2140;
            powerUse = 17f;
            shootDuration = 240f;
            range = 280f;
            rangeExtention = 60f;
            reloadTime = 150f;
            rotateSpeed = 2.25f;
            recoilAmount = 4f;
            layers = 2;
            shootType = PMBullets.blazeMagma;
        }};

        inferno = new Block("inferno");

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
                Items.thorium, PMBullets.standardThoriumMini,
                Items.blastCompound, PMBullets.standardExplosiveMini
            );
            size = 4;
            range = 255f;
            health = 1800;
            shootCone = 20f;
            shootSound = Sounds.shootBig;
            targetAir = targetGround = true;
            rotateSpeed = 4f;
            recoilAmount = 3f;
            restitution = 0.02f;
            cooldown = 0.11f;
            inaccuracy = 8f;
            shootEffect = smokeEffect = ammoUseEffect = Fx.none;
            heatColor = Pal.turretHeat;

            barX = 4f;
            barY = -10f;
            barStroke = 1f;
            barLength = 9f;

            shootLocs = new float[]{0f};
            windupSpeed = 0.000125f;
            windDownSpeed = 0.0125f;
            minFiringSpeed = 1f/6f;
        }};

        miinigun = new MinigunTurret("miinigun"){{
            requirements(Category.turret, with(
                Items.copper, 350,
                Items.graphite, 300,
                Items.plastanium, 175,
                Items.thorium, 80,
                PMItems.techtanite, 80
            ));
            ammo(
                Items.copper, PMBullets.standardCopperMini,
                Items.graphite, PMBullets.standardDenseMini,
                Items.silicon, PMBullets.standardHomingMini,
                Items.pyratite, PMBullets.standardIncendiaryMini,
                Items.thorium, PMBullets.standardThoriumMini,
                Items.blastCompound, PMBullets.standardExplosiveMini
            );
            size = 4;
            range = 255f;
            health = 1800;
            shootCone = 20f;
            shootSound = Sounds.shootBig;
            targetAir = targetGround = true;
            rotateSpeed = 4f;
            recoilAmount = 3f;
            restitution = 0.02f;
            cooldown = 0.11f;
            inaccuracy = 8f;
            shootEffect = smokeEffect = ammoUseEffect = Fx.none;
            heatColor = Pal.turretHeat;

            barX = 4f;
            barY = -10f;
            barStroke = 1f;
            barLength = 9f;

            shootLocs = new float[]{-4f, 4f};
            windupSpeed = 0.000125f/1.2f;
            windDownSpeed = 0.0125f;
            minFiringSpeed = 1f/6f;
        }};

        mivnigun = new MinigunTurret("mivnigun"){{
            requirements(Category.turret, with(
                Items.copper, 650,
                Items.graphite, 600,
                Items.titanium, 120,
                Items.thorium, 160,
                Items.plastanium, 325,
                PMItems.techtanite, 240
            ));
            ammo(
                Items.copper, PMBullets.standardCopperMini,
                Items.graphite, PMBullets.standardDenseMini,
                Items.silicon, PMBullets.standardHomingMini,
                Items.pyratite, PMBullets.standardIncendiaryMini,
                Items.thorium, PMBullets.standardThoriumMini,
                Items.blastCompound, PMBullets.standardExplosiveMini
            );
            size = 4;
            range = 255f;
            health = 1800;
            shootCone = 20f;
            shootSound = Sounds.shootBig;
            targetAir = targetGround = true;
            rotateSpeed = 4f;
            recoilAmount = 3f;
            restitution = 0.02f;
            cooldown = 0.11f;
            inaccuracy = 8f;
            shootEffect = smokeEffect = ammoUseEffect = Fx.none;
            heatColor = Pal.turretHeat;

            barX = 5f;
            barY = -10f;
            barStroke = 1f;
            barLength = 9f;

            shootLocs = new float[]{-9f, -3f, 3f, 9f};
            windupSpeed = 0.000125f/1.4f;
            windDownSpeed = 0.0125f;
            minFiringSpeed = 1f/6f;
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
            health = 260;
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
            health = 870;
            powerUse = 4.8f;
            reloadTime = 20f;
            range = 130f;
            rangeExtention = 16f;
            shots = 2;
            zaps = 6;
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
            health = 1540;
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

        caliber = new SniperTurret("caliber"){{
            requirements(Category.turret, with(
                Items.copper, 220,
                Items.titanium, 200,
                Items.thorium, 150,
                Items.plastanium, 110
            ));
            ammo(
                Items.thorium, PMBullets.sniperBoltThorium
            );
            size = 3;
            reloadTime = 450f;
            inaccuracy = 0f;
            range = 544f;
            split = 3f;
            chargeTime = 300f;
            shootLength = 18f;
            shootSound = Sounds.railgun;
        }};

        bit = new BitTurret("bit"){{
            requirements(Category.turret, with(
                Items.copper, 50,
                Items.lead, 60,
                Items.silicon, 40,
                Items.titanium, 30
            ));
            size = 2;
            reloadTime = 70f;
            recoilAmount = 4f;
            inaccuracy = 15f;
            range = 140f;
            powerUse = 1.35f;
            shootType = PMBullets.pixel;
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
            health = 1430;
            range = 160f;
            reloadTime = 75f;
            shootSound = Sounds.missile;
            cooldown = 0.001f;
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
                PMItems.quantiumMissile, PMBullets.strikedownQuantum
            );
            reloadBar = true;
            size = 4;
            health = 2870;
            range = 330f;
            reloadTime = 180f;
            shootSound = Sounds.artillery;
            cooldown = 0.001f;
            shootShake = 5f;
            inaccuracy = 5f;
            maxAmmo = 8;
            unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;
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
            health = 5950;
            range = 4400f;
            shootSound = Sounds.explosionbig;
            cooldown = 0.001f;
            shootShake = 10f;
            reloadTime = 1500f;
            maxAmmo = 2;
            unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;
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
            health = 2140;
            reloadTime = 520f;
            range = 128f;
            shootEffect = smokeEffect = Fx.none;
            chargeBeginEffect = PMFx.kugelblitzChargeBegin;
            chargeEffect = PMFx.kugelblitzCharge;
            chargeMaxDelay = 30f;
            chargeEffects = 16;
            chargeTime = PMFx.kugelblitzChargeBegin.lifetime;
            recoilAmount = 2f;
            restitution = 0.015f;
            cooldown = 0.005f;
            shootLength = 0f;
            rotateSpeed = 1.25f;
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
                health = 4610;
                range = 740f;
                shootEffect = smokeEffect = Fx.none;
                shootLength = 0f;
                cooldown = 0.0075f;
                heatColor = Pal.surge;
                chargeTime = 180f;
                chargeSound = PMSounds.popeshadowCharge;
                shootSound = PMSounds.popeshadowBlast;
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

        harbinger = new ChaosTurret("harbinger"){
            {
                size = 8;
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
                health = 5000000;
                size = 6;
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
        
        // endregion
        // Region Distribution

        floatingConveyor = new FloatingConveyor("floating-conveyor"){{
            requirements(Category.distribution, with(
                Items.lead, 1,
                Items.metaglass, 2,
                Items.titanium, 2
            ));
            health = 15;
            speed = 0.08f;
            displayedSpeed = 11f;
            drawShallow = true;
        }};

        // endregion
        // Region Power

        strobeNode = new StrobeNode("rainbow-power-node");

        strobeInf = new StrobeSource("rainbow-power-source");

        strobeBoost = new StrobeSource("rainbow-power-boost"){{
            size = 2;
            boost = true;
            speedBoost = 100f;
        }};


        // endregion
        // Region Walls

        sandboxWall = new SandboxWall("sandbox-wall"){{
            health = 150000000;
            iconSize = 3f;
            rotateRadius = 2.5f;
        }};

        sandboxWallLarge = new SandboxWall("sandbox-wall-large"){{
            size = 2;
            health = 600000000;
            iconSize = 6f;
            rotateRadius = 5f;
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
            consumes.power(14f);
            consumes.items(with(
                Items.titanium, 7,
                Items.thorium, 7
            ));
            outputItem = new ItemStack(PMItems.techtanite, 4);
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
            health = 135;
            itemCapacity = 30;
            craftEffect = Fx.pulverizeMedium;
            updateEffect = Fx.none;
            dumpToggle = true;
        }};

        missileFactory = new MissileCrafter("missile-factory", 7){{
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
            health = 215;
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
            health = 265;
            itemCapacity = 100;
            craftEffect = updateEffect = Fx.none;
            dumpToggle = true;
        }};

        // endregion
    }
}