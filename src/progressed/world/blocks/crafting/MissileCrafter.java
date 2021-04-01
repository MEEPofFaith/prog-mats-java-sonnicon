package progressed.world.blocks.crafting;

import arc.*;
import arc.graphics.g2d.*;
import multilib.*;

public class MissileCrafter extends MultiCrafter{
    protected TextureRegion colorRegion;
    
    public MissileCrafter(String name, int recLen){
        super(name, recLen);
        isSmelter = false;
    }

    @Override
    public void load(){
        super.load();
        colorRegion = Core.atlas.find(name + "-color");
    }

    public class MissileCrafterBuild extends MultiCrafterBuild{
        @Override
        public void draw(){
            super.draw();
            if(toggle >= 0){
                Draw.color(recs[toggle].output.items[0].item.color);
                Draw.rect(colorRegion, x, y);
            }
        }
    }
}
