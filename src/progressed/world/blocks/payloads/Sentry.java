package progressed.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;

public class Sentry extends Missile{
    public UnitType unit;

    public Sentry(String name){
        super(name);
        rotate = true;
        rebuildable = false;
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list){
        Draw.rect(region, req.drawx(), req.drawy(), req.rotation * 90 - 90f);
    }

    public class SentryBuild extends Building{
        @Override
        public void placed(){
            spawn();
        }

        @Override
        public void dropped(){
            spawn();
        }

        public void spawn(){
            Unit spawned = unit.spawn(team, self());
            spawned.rotation(rotdeg());
            kill();
        }

        @Override
        public void onDestroyed(){
            //no
        }
    }
}
