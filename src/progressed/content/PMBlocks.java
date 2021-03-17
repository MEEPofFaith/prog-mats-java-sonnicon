package progressed.content;

import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import progressed.world.blocks.defence.turret.*;

import static mindustry.type.ItemStack.with;

public class PMBlocks implements ContentList{
    public static Block bit;

    @Override
    public void load(){
        bit = new BitTurret("pixel-i"){{
            requirements(Category.turret, BuildVisibility.sandboxOnly, with(Items.copper, 50, Items.lead, 60,  Items.silicon, 40, Items.titanium, 30));
            size = 2;
            reloadTime = 70f;
            recoilAmount = 4f;
            inaccuracy = 15f;
            range = 140f;
            powerUse = 1.35f;
            shootType = PMBullets.pixel;
        }};
    }
}