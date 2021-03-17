package progressed.content;

import arc.graphics.*;
import mindustry.ctype.*;
import mindustry.type.*;

public class PMItems implements ContentList{
    public static Item
    techtanite;

    @Override
    public void load() {
        techtanite = new Item("techtanite", Color.valueOf("8C8C8C")){{
            cost = 2.5f;
        }};
    }
}
