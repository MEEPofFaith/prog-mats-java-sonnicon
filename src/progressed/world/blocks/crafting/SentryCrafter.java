package progressed.world.blocks.crafting;

import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import progressed.type.*;

public class SentryCrafter extends MissileCrafter{
    public SentryCrafter(String name, int recLen){
        super(name, recLen);
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