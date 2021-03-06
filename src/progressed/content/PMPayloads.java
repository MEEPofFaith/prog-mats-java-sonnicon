package progressed.content;

import mindustry.content.*;
import mindustry.ctype.*;
import progressed.world.blocks.payloads.*;

import static mindustry.Vars.*;
import static mindustry.type.ItemStack.*;

public class PMPayloads implements ContentList{
    public static Missile

    //Region Missiles

    emptyMissile,

    basicMissile, empMissile, recursiveMissile,

    //Region Nukes

    emptyNuke,

    basicNuke, clusterNuke,

    //Region Sentries

    basicSentry, strikeSentry, dashSentry;

    @Override
    public void load(){
        emptyMissile = new Missile("empty-missile"){{
            requirements = with(Items.copper, 5, Items.lead, 5, Items.titanium, 5);

            size = 2;
            powerUse = 3f;
            constructTime = 60f * 5f;
        }};

        basicMissile = new Missile("basic-missile"){{
            requirements = with(Items.thorium, 3, Items.blastCompound, 6);

            prev = emptyMissile;
            size = 2;
            powerUse = 3f;
            constructTime = 60f * 19f;

            explosion = PMBullets.strikedownBasic;
        }};

        empMissile = new Missile("emp-missile"){{
            requirements = with(Items.lead, 12, Items.titanium, 10, Items.silicon, 10);

            prev = emptyMissile;
            size = 2;
            powerUse = 4f;
            constructTime = 60f * 21f;
            requiresUnlock = true;

            explosion = PMBullets.strikedownEmp;
        }};

        recursiveMissile = new Missile("recursive-missile"){{
            requirements = with(Items.copper, 30, Items.lead, 15, Items.titanium, 15, Items.plastanium, 10, Items.silicon, 10);

            prev = emptyMissile;
            size = 2;
            powerUse = 5f;
            constructTime = 60f * 26f;
            requiresUnlock = true;

            explosionArea = -1f;
            explosion = PMBullets.recursionTwo;
            explosions = 13;
            maxDelay = 20f;
        }};

        emptyNuke = new Missile("empty-nuke"){{
            requirements = with(Items.titanium, 10, Items.surgeAlloy, 10, PMItems.fusium, 10);

            size = 3;
            powerUse = 5f;
            constructTime = 60f * 8f;
            requiresUnlock = true;
            shadowRad = size * tilesize * 2f;
        }};

        basicNuke = new Missile("basic-nuke"){{
            requirements = with(Items.titanium, 25, Items.thorium, 35, Items.blastCompound, 25);

            prev = emptyNuke;
            size = 3;
            powerUse = 6f;
            constructTime = 60f * 55f;
            requiresUnlock = true;
            shadowRad = size * tilesize * 2f;

            explosion = PMBullets.arbiterBasic;
        }};

        clusterNuke = new Missile("cluster-nuke"){{
            requirements = with(Items.titanium, 30, Items.plastanium, 15, PMItems.fusium, 10, Items.silicon, 20, Items.thorium, 15);

            prev = emptyNuke;
            size = 3;
            powerUse = 6.25f;
            constructTime = 60f * 60f;
            requiresUnlock = true;
            shadowRad = size * tilesize * 2f;

            explosionArea = -1f;
            explosion = PMBullets.arbiterClusterFrag;
            explosions = PMBullets.arbiterCluster.fragBullets;
            maxDelay = 20f;
        }};

        basicSentry = new Sentry("basic-sentry"){{
            requirements = with(Items.copper, 30, Items.lead, 35, Items.titanium, 15, Items.silicon, 25);

            size = 2;
            powerUse = 4f;
            constructTime = 60f * 20f;
            unit = PMUnitTypes.barrage;
        }};

        strikeSentry = new Sentry("strike-sentry"){{
            requirements = with(Items.copper, 40, Items.lead, 40, Items.titanium, 20, Items.silicon, 30, Items.blastCompound, 10);

            size = 2;
            powerUse = 4.5f;
            constructTime = 60f * 25f;
            unit = PMUnitTypes.downpour;
        }};

        dashSentry = new Sentry("dash-sentry"){{
            requirements = with(Items.copper, 30, Items.lead, 30, Items.titanium, 30, Items.graphite, 15, Items.silicon, 35);

            size = 2;
            powerUse = 5.25f;
            constructTime = 60f * 23f;
            unit = PMUnitTypes.rapier;
        }};
    }
}
