package progressed.world.blocks.payloads;

import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;

public class Missile extends NuclearWarhead{
    public float powerCost;

    public Missile(String name){
        super(name);

        buildVisibility = BuildVisibility.sandboxOnly;
        hasShadow = false;
    }
}
