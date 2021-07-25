package progressed.world.blocks.crafting;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import multilib.*;
import progressed.type.*;

public class VisualMultiCrafter extends MultiCrafter{
    public TextureRegion colorRegion, topRegion;

    public VisualMultiCrafter(String name, Recipe[] recs){
        super(name, recs);
        isSmelter = false;
    }
    
    public VisualMultiCrafter(String name, int recLen){
        this(name, new Recipe[recLen]);
    }

    @Override
    public void load(){
        super.load();
        colorRegion = Core.atlas.find(name + "-color");
        topRegion = Core.atlas.find(name + "-top");
    }

    @Override
    public TextureRegion[] icons(){
        if(topRegion.found()){
            return new TextureRegion[]{region, topRegion};
        }else{
            return super.icons();
        }
    }

    public class VisualMultiCrafterBuild extends MultiCrafterBuild{
        @Override
        public void draw(){
            super.draw();
            if(toggle >= 0 && recs[toggle].output.items[0].item instanceof VisualItem s){
                if(colorRegion.found()){
                    Draw.color(s.color);
                    Draw.rect(colorRegion, x, y);
                    Draw.color();
                }
                Draw.draw(Draw.z(), () -> {
                    Drawf.construct(x, y, s.sprite, team.color, 0f, progress, warmup, totalProgress);
                });
            }
            if(topRegion.found()){
                Draw.rect(topRegion, x, y);
            }
        }
    }
}