package progressed.content;

import progressed.util.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class SettingAdder{
    public void init(){
        boolean tmp = settings.getBool("uiscalechanged", false);
        settings.put("uiscalechanged", false);

        ui.settings.graphics.sliderPref("pm-swordopacity", 100, 20, 100, 5, s -> s + "%");
        ui.settings.graphics.sliderPref("pm-strobespeed", 3, 1, 20, 1, s -> PMUtls.stringsFixed(s / 2f));

        settings.put("uiscalechanged", tmp);
    }
}