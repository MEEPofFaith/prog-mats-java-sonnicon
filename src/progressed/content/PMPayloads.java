package progressed.content;

import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.type.*;
import progressed.world.blocks.payloads.*;

import static mindustry.type.ItemStack.*;

public class PMPayloads implements ContentList{
    public static Missile

    //Region Missiles

    emptyMissile,

    //Region Nukes

    emptyNuke;

    @Override
    public void load(){
        emptyMissile = new Missile("empty-missile"){{
            requirements(Category.units, with(Items.copper, 5, Items.lead, 5, Items.titanium, 5));

            size = 2;
        }};

        emptyNuke = new Missile("empty-nuke"){{
            requirements(Category.units, with(Items.titanium, 10, Items.surgeAlloy, 10, PMItems.techtanite, 10));

            size = 3;
        }};
    }
}
