package progressed.type;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.type.*;
import mindustry.ui.*;

public class VisualItem extends Item{
    public UnitType unit;
    public String spriteName;

    public TextureRegion sprite;

    public VisualItem(String name, Color color, UnitType unit){
        super(name, color);
        this.unit = unit;
    }

    public VisualItem(String name, Color color, String spriteName){
        super(name, color);
        this.spriteName = spriteName;
    }

    @Override
    public void init(){
        super.init();

        if(unit != null){
            sprite = unit.fullIcon;
        }else if(spriteName != null){
            sprite = Core.atlas.find(spriteName);
        }
    }
}
