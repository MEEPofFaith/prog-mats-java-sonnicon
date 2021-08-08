package progressed.world.blocks.payloads;

import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;

public class Missile extends NuclearWarhead{
    public Block prev;
    public float powerCost;
    public boolean requiresUnlock;

    public Missile(String name){
        super(name);

        buildVisibility = BuildVisibility.sandboxOnly;
        category = Category.units;
        hasShadow = false;
    }
}
