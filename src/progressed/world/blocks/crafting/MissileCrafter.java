package progressed.world.blocks.crafting;

import arc.*;
import arc.graphics.g2d.*;
import multilib.*;

public class MissileCrafter extends MultiCrafter{
    protected TextureRegion colorRegion;

    public MissileCrafter(String name, Recipe[] recs){
        super(name, recs);
        isSmelter = false;
    }
    
    public MissileCrafter(String name, int recLen){
        this(name, new Recipe[recLen]);
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