package progressed.type;

import arc.graphics.*;
import mindustry.type.*;

public class SentryItem extends Item{
    public UnitType sentry;

    public SentryItem(String name, Color color, UnitType sentry){
        super(name, color);
        this.sentry = sentry;
    }
}
