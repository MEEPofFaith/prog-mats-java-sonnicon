package progressed.type;

import arc.*;
import arc.struct.*;
import mindustry.game.EventType.*;
import mindustry.type.*;

public class ExclusiveStatusEffect extends StatusEffect{
    public Seq<StatusEffect> exclusives;

    public ExclusiveStatusEffect(String name){
        super(name);
    }

    @Override
    public void init(){
        super.init();

        //This effect gets replaced by the new effect.
        exclusives.each(s -> {
            transitions.put(s, ((unit, result, time) -> {
                result.set(result.effect, result.time);
            }));
            opposites.add(s);
        });
    }
}
