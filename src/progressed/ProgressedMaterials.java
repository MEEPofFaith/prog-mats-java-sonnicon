package progressed;

import arc.*;
import arc.func.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.mod.*;
import mindustry.mod.Mods.*;
import progressed.content.*;

import static mindustry.Vars.*;

public class ProgressedMaterials extends Mod{

    private final ContentList[] pmContent = {
        new PMItems(),
        new PMUnitTypes(),
        new PMBullets(),
        new PMWeathers(),
        new PMBlocks()
    };

    public ProgressedMaterials(){
        super();
    }

    @Override
    public void init(){
        enableConsole = true;
        if(!headless){
            Func<String, String> stringf = value -> Core.bundle.get("mod." + value);
            LoadedMod progM = mods.locateMod("prog-mats");

            progM.meta.displayName = stringf.get(progM.meta.name + ".name");
            progM.meta.author = stringf.get(progM.meta.name + ".author");
            progM.meta.description = stringf.get(progM.meta.name + ".description");
        }
    }

    @Override
    public void loadContent(){
        for(ContentList list : pmContent){
            list.load();

            Log.info("@: Loaded content list: @", getClass().getSimpleName(), list.getClass().getSimpleName());
        }
    }

    public static void print(Object... args){
        StringBuilder builder = new StringBuilder();
        if(args == null){
            builder.append("null");
        }else{
            for(int i = 0; i < args.length; i++){
                builder.append(args[i]);
                if(i < args.length - 1) builder.append(", ");
            }
        }

        Log.info("&lc&fb[PM]&fr @", builder.toString());
    }
}