package progressed.world.blocks.power;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.meta.*;

public class StrobeSource extends StrobeNode{
    public float powerProduction = 2000000000f / 60f;
    public boolean boost;
    public float speedBoost;

    public StrobeSource(String name){
        super(name);
        outputsPower = true;
        consumesPower = false;
    }

    @Override
    public void setStats(){
        super.setStats();
        if(boost){
            stats.add(Stat.speedIncrease, (100 * speedBoost), StatUnit.percent);
        }
        stats.add(Stat.basePowerGeneration, powerProduction * 60.0f, StatUnit.powerSecond);
    }

    @Override
    public void setBars(){
        super.setBars();
        if(boost){
            bars.add("boost", (StrobeSourceBuild entity) -> new Bar(() -> Core.bundle.get("stat.prog-mats.gay") + " " + (speedBoost * 100f) + "%", () -> laserColor1.cpy().lerp(laserColor3, Mathf.absin(Time.time * lerpSpeed, 1f, 1f)).shiftHue(Time.time * speed), () -> 100f));
        }
    }

    public class StrobeSourceBuild extends StrobeNodeBuild{
        @Override
        public float getPowerProduction(){
            return enabled ? powerProduction : 0f;
        }

        @Override
        public void updateTile(){
            super.updateTile();
            if(boost && timer(1, 60f)){
                for(int i = 0; i < power.links.size; i++){
                    Building b = Vars.world.tile(power.links.items[i]).build;
                    if(b != null){
                        b.applyBoost(speedBoost, 65f);
                    }
                }
            }
        }
    }
}
