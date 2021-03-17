package progressed;

import arc.util.*;
import mindustry.ctype.*;
import mindustry.mod.*;
import progressed.content.*;

public class ProgressedMaterials extends Mod{

    private final ContentList[] pmContent = {
        new PMItems(),
        new PMBullets(),
        new PMBlocks()
    };

    public ProgressedMaterials(){
        super();
    }

    @Override
    public void init(){
    }

    @Override
    public void loadContent(){
        for(ContentList list : pmContent){
            list.load();

            Log.info("@: Loaded content list: @", getClass().getSimpleName(), list.getClass().getSimpleName());
        }
    }
}