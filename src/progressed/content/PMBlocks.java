package progressed.content;

import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.Sounds;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import progressed.world.blocks.defence.turret.*;

import static mindustry.type.ItemStack.*;

public class PMBlocks implements ContentList{
    public static Block 
    //Crit Snipers
    caliber,

    //Pixel Turrets
    bit;

    @Override
    public void load(){
        caliber = new SniperTurret("caliber"){{
            requirements(Category.turret, BuildVisibility.sandboxOnly, empty);
            size = 3;
            reloadTime = 450f;
            inaccuracy = 0f;
            range = 544f;
            split = 3f;
            chargeTime = 300f;
            shootLength = 18f;
            shootSound = Sounds.railgun;
            ammo(Items.thorium, PMBullets.sniperBoltThorium);
        }};

        bit = new BitTurret("bit"){{
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