package progressed.world.blocks.sandbox;

import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.meta.*;

public class CapBlock extends Wall{
    public CapBlock(String name){
        super(name);
        requirements(Category.units, BuildVisibility.sandboxOnly, ItemStack.empty);
        alwaysUnlocked = true;

        flags = EnumSet.of(BlockFlag.unitModifier);
    }

    public class CapBlockBuild extends WallBuild{
        @Override
        public void draw(){
            Draw.rect(block.region, x, y);
    
            drawTeamTop();
        }
    }
}
