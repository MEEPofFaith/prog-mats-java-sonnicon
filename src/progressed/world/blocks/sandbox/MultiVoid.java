package progressed.world.blocks.sandbox;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class MultiVoid extends Block{
    public MultiVoid(String name){
        super(name);
        requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.empty);
        alwaysUnlocked = true;
        
        update = solid = acceptsItems = hasLiquids = true;
    }

    @Override
    public void setBars(){
        super.setBars();
        bars.remove("liquid");
    }

    public class MultiVoidBUild extends Building{
        @Override
        public void handleItem(Building source, Item item){}

        @Override
        public boolean acceptItem(Building source, Item item){
            return enabled;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return enabled;
        }

        @Override
        public void handleLiquid(Building source, Liquid liquid, float amount){
        }
    }
}
