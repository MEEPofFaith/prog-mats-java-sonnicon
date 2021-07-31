package progressed.ui;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;

public class PMElements{
    public static Element itemImage(TextureRegion region, Prov<CharSequence> text){
        Stack stack = new Stack();

        Table t = new Table().left().bottom();
        t.label(text);

        stack.add(new Image(region));
        stack.add(t);
        return stack;
    }
}
