package progressed.content;

import arc.graphics.*;
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
    blackhole,

    //Random distribution
    floatingConveyor,
    
    //Sandbox Power
    strobeNode, strobeInf, strobeBoost;

    @Override
    public void load(){
        flame = new Block("flame");

        blaze = new Block("blaze");

        inferno = new Block("inferno");

        minigun = new MinigunTurret("minigun"){{
            requirements(Category.turret, with(Items.copper, 200, Items.graphite, 175, Items.titanium, 100, Items.thorium, 80));
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
            requirements(Category.turret, with(Items.copper, 350, Items.graphite, 300, Items.plastanium, 175, Items.thorium, 80, PMItems.techtanite, 80));
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
            requirements(Category.turret, with(Items.copper, 650, Items.graphite, 600, Items.titanium, 120, Items.thorium, 160, Items.plastanium, 325, PMItems.techtanite, 240));
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

        shock = new Block("shock");

        spark = new Block("spark");

        storm = new Block("storm");

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
            requirements(Category.turret, with(Items.copper, 50, Items.lead, 60,  Items.silicon, 40, Items.titanium, 30));
            size = 2;
            reloadTime = 70f;
            recoilAmount = 4f;
            inaccuracy = 15f;
            range = 140f;
            powerUse = 1.35f;
            shootType = PMBullets.pixel;
        }};

        tinker = new LaunchTurret("tinker"){{
            requirements(Category.turret, with(Items.copper, 125, Items.lead, 75, Items.silicon, 30, Items.titanium, 50));
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
            requirements(Category.turret, with(Items.copper, 180, Items.graphite, 140, Items.silicon, 65, Items.titanium, 70));
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
            requirements(Category.turret, with(Items.copper, 70, Items.lead, 350, Items.graphite, 300, Items.silicon, 300, Items.titanium, 250, PMItems.techtanite, 120));
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
            requirements(Category.turret, with(Items.copper, 4000, Items.graphite, 2200, Items.silicon, 2000, Items.titanium, 1300, Items.thorium, 650, Items.surgeAlloy, 200, PMItems.techtanite, 800));
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
            requirements(Category.turret, with(Items.titanium, 100, Items.thorium, 150, Items.plastanium, 250, Items.surgeAlloy, 250, Items.silicon, 800, Items.phaseFabric, 500, PMItems.techtanite, 500));
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

        floatingConveyor = new FloatingConveyor("floating-conveyor"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.metaglass, 4, Items.titanium, 1));
            health = 15;
            speed = 0.08f;
            displayedSpeed = 11f;
            coverColor = Color.sky.cpy().a(0.5f);
        }};

        strobeNode = new StrobeNode("rainbow-power-node");

        strobeInf = new StrobeSource("rainbow-power-source");

        strobeBoost = new StrobeSource("rainbow-power-boost"){{
            size = 2;
            boost = true;
            speedBoost = 100f;
        }};
    }
}