package progressed.content;

import arc.graphics.*;
import mindustry.ctype.*;
import mindustry.type.*;
import progressed.type.*;

public class PMItems implements ContentList{
    public static Item
    techtanite,
    
    basicSentryBox, strikeSentryBox, dashSentryBox;

    @Override
    public void load(){
        techtanite = new Item("techtanite", Color.valueOf("B0BAC0")){{
            cost = 1.6f;
        }};

        basicSentryBox = new VisualItem("basic-sentry-box", Color.valueOf("C9A58F"), PMUnitTypes.basicSentry);
        
        strikeSentryBox = new VisualItem("strike-sentry-box", Color.valueOf("FFA665"), PMUnitTypes.strikeSentry);

        dashSentryBox = new VisualItem("dash-sentry-box", Color.valueOf("84F491"), PMUnitTypes.dashSentry);
    }
}