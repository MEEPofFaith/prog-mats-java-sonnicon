package progressed.content;

import arc.scene.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import progressed.util.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class SettingAdder{
    public void init(){
        BaseDialog dialog = new BaseDialog("Progressed Materials");

        dialog.addCloseButton();
        dialog.cont.center().pane(p -> {
            sliderPref(p, "pm-swordopacity", 100, 20, 100, 5, s -> s + "%");
            sliderPref(p, "pm-strobespeed", 3, 1, 20, 1, s -> PMUtls.stringsFixed(s / 2f));
        }).growY().width(mobile ? graphics.getWidth() : graphics.getWidth() / 3f);

        ui.settings.shown(() -> {
            Table settingUi = (Table)((Group)((Group)(ui.settings.getChildren().get(1))).getChildren().get(0)).getChildren().get(0); //This looks so stupid lol
            settingUi.row();
            settingUi.button(bundle.get("setting.pm-title"), Styles.cleart, dialog::show);
        });

    }

    public void sliderPref(Table table, String name, String title, int def, int min, int max, int step, StringProcessor sp){
        Slider slider = new Slider(min, max, step, false);

        slider.setValue(settings.getInt(name, def));

        Label label = new Label(title);
        slider.changed(() -> {
            settings.put(name, (int)slider.getValue());
            label.setText(title + ": " + sp.get((int)slider.getValue()));
        });

        slider.change();

        table.table(t -> {
            t.left().defaults().left();
            t.add(label).minWidth(label.getPrefWidth() / Scl.scl(1f) + 50);
            t.add(slider).width(180);
        }).left().padTop(3);

        table.row();
    }

    public void sliderPref(Table table, String name, int def, int min, int max, int step, StringProcessor sp){
        sliderPref(table, name, bundle.get("setting." + name + ".name"), def, min, max, step, sp);
    }
}