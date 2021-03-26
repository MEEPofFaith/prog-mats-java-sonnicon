package progressed.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import progressed.graphics.*;
import progressed.world.blocks.defence.turret.*;
import progressed.world.blocks.distribution.*;
import progressed.world.blocks.power.*;

import static mindustry.type.ItemStack.*;
import static mindustry.Vars.*;

public class PMBlocks implements ContentList{
    public static Block
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

    //Crafters
    shellPress, missileFactory, sentryBuilder,

    //Random distribution
    floatingConveyor,
    
    //Sandbox Power
    strobeNode, strobeInf, strobeBoost;

    @Override
    public void load(){

        // Region Turrets

        flame = new Block("flame");

        blaze = new Block("blaze");

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

            //Dummy stats to mess with the shots/sec stat
            reloadTime = 3f;
            shots = 1;
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

            //Dummy stats to mess with the shots/sec stat
            reloadTime = 3f;
            shots = 2;
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

            //Dummy stats to mess with the shots/sec stat
            reloadTime = 3f;
            shots = 1;
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
            reloadTime = 60f;
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
            reloadTime = 40f;
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
            reloadTime = 20f;
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
            requirements(Category.turret, BuildVisibility.sandboxOnly, empty);
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

        excalibur = new Block("excalibur");

        harbinger = new Block("harbinger");

        everythingGun = new Block("everything-gun");
        
        //End Region
        //Region Crafters

        shellPress = new Block("shell-press");

        missileFactory = new Block("missile-facotry");

        sentryBuilder = new Block("sentry-builder");

        //End Region
        //Region Distribution

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

        //End Region
        //Region Power

        strobeNode = new StrobeNode("rainbow-power-node");

        strobeInf = new StrobeSource("rainbow-power-source");

        strobeBoost = new StrobeSource("rainbow-power-boost"){{
            size = 2;
            boost = true;
            speedBoost = 100f;
        }};


        //End Region
    }
}