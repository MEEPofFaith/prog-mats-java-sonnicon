package progressed.content;

import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.world.*;
import progressed.world.blocks.payloads.*;

import static mindustry.type.ItemStack.*;

public class PMPayloads implements ContentList{
    public static Block

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
            powerCost = 3f;
        }};

        basicMissile = new Missile("basic-missile"){{
            requirements = with(Items.thorium, 3, Items.blastCompound, 6);

            prev = emptyMissile;
            size = 2;
            powerCost = 3f;
        }};

        empMissile = new Missile("emp-missile"){{
            requirements = with(Items.lead, 12, Items.titanium, 10, Items.silicon, 10);

            prev = emptyMissile;
            size = 2;
            powerCost = 4f;
            constructTime = 75f;
            requiresUnlock = true;
        }};

        recursiveMissile = new Missile("recursive-missile"){{
            requirements = with(Items.copper, 30, Items.lead, 15, Items.titanium, 15, Items.plastanium, 10, Items.silicon, 10);

            prev = emptyMissile;
            size = 2;
            powerCost = 5f;
            constructTime = 80f;
            requiresUnlock = true;
        }};

        emptyNuke = new Missile("empty-nuke"){{
            requirements = with(Items.titanium, 10, Items.surgeAlloy, 10, PMItems.techtanite, 10);

            size = 3;
            powerCost = 5f;
            constructTime = 90f;
            requiresUnlock = true;
        }};

        basicNuke = new Missile("basic-nuke"){{
            requirements = with(Items.titanium, 25, Items.thorium, 35, Items.blastCompound, 25);

            prev = emptyNuke;
            size = 3;
            powerCost = 6f;
            constructTime = 90f;
            requiresUnlock = true;
        }};

        clusterNuke = new Missile("cluster-nuke"){{
            requirements = with(Items.titanium, 25, Items.thorium, 35, Items.blastCompound, 25);

            prev = emptyNuke;
            size = 3;
            powerCost = 6.25f;
            constructTime = 120f;
            requiresUnlock = true;
        }};

        basicSentry = new Sentry("basic-sentry"){{
            requirements = with(Items.copper, 30, Items.lead, 35, Items.titanium, 15, Items.silicon, 25);

            size = 2;
            powerCost = 4f;
            constructTime = 90f;
           type = PMUnitTypes.barrage;
        }};

        strikeSentry = new Sentry("strike-sentry"){{
            requirements = with(Items.copper, 40, Items.lead, 40, Items.titanium, 20, Items.silicon, 30, Items.blastCompound, 10);

            size = 2;
            powerCost = 4.5f;
            constructTime = 120f;
           type = PMUnitTypes.downpour;
        }};

        dashSentry = new Sentry("dash-sentry"){{
            requirements = with(Items.copper, 30, Items.lead, 30, Items.titanium, 30, Items.graphite, 15, Items.silicon, 35);

            size = 2;
            powerCost = 5.25f;
            constructTime = 105f;
           type = PMUnitTypes.rapier;
        }};
    }
}
