package progressed.content;

import static arc.Core.*;
import static mindustry.Vars.*;

public class SettingAdder{
    public void init(){
        boolean tmp = settings.getBool("uiscalechanged", false);
        settings.put("uiscalechanged", false);

        ui.settings.graphics.sliderPref("pm-swordopacity", 100, 20, 100, 5, s -> s + "%");

        settings.put("uiscalechanged", tmp);
    }
}