package progressed.content;

import arc.graphics.*;
import mindustry.ctype.*;
import mindustry.type.*;
import progressed.type.*;

public class PMItems implements ContentList{
    public static Item
    techtanite;

    @Override
    public void load(){
        techtanite = new Item("techtanite", Color.valueOf("B0BAC0")){{
            cost = 1.6f;
        }};
    }
}