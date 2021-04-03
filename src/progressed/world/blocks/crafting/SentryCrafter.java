package progressed.world.blocks.crafting;

import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import multilib.*;
import progressed.type.*;

public class SentryCrafter extends MissileCrafter{
    public SentryCrafter(String name, Recipe[] recs){
        super(name, recs);
    }
    
    public SentryCrafter(String name, int recLen){
        this(name, new Recipe[recLen]);
    }
    
    public class SentryCrafterBuild extends MissileCrafterBuild{
        @Override
        public void draw(){
            super.draw();
            if(toggle >= 0 && recs[toggle].output.items[0].item instanceof SentryItem s){
                Draw.color(s.color);
                Draw.rect(colorRegion, x, y);
                Draw.color();
                Draw.draw(Draw.z(), () -> {
                    Drawf.construct(x, y, s.sentry.icon(Cicon.full), team.color, 0f, progress, warmup, totalProgress);
                });
            }
        }
    }
}