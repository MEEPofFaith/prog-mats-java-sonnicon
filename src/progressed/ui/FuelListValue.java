package progressed.ui;

import arc.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import progressed.world.blocks.crafting.*;

public class FuelListValue implements StatValue{
    private final FuelCrafter smelter;

    public FuelListValue(FuelCrafter smelter){
        this.smelter = smelter;
    }

    @Override
    public void display(Table table){
        table.row();

        table.image(smelter.fuelItem.fullIcon).size(3 * 8).padRight(4).right().top();
        table.add(smelter.fuelItem.localizedName).padRight(10).left().top();

        table.table(t -> {
            t.left().defaults().padRight(3).left();

            t.add(Core.bundle.format("fuel.fc-input", smelter.fuelPerItem));

            sep(t, Core.bundle.format("fuel.fc-use", smelter.fuelPerCraft));

            sep(t, Core.bundle.format("fuel.fc-capacity", smelter.fuelCapacity));

            if(smelter.attribute != null){
                sep(t, Core.bundle.get("fuel.fc-affinity"));
                t.row();
                t.table(at -> {
                    Attribute attr = smelter.attribute;

                    at.left().defaults().padRight(3).left();
                    for(var block : Vars.content.blocks()
                        .select(block -> block instanceof Floor f && f.attributes.get(attr) != 0 && !(f.isLiquid && !smelter.floating))
                        .<Floor>as().with(s -> s.sort(f -> f.attributes.get(attr)))){
                            floorStat(at, attr, block);
                    }
                }).padTop(0).left().get().background(null);
            }
        }).padTop(-9).left().get().background(Tex.underline);
    }

    void sep(Table table, String text){
        table.row();
        table.add(text);
    }

    void floorStat(Table t, Attribute attr, Floor floor){
        float multiplier = floor.attributes.get(attr) * smelter.fuelUseReduction / -100f;
        t.stack(new Image(floor.fullIcon).setScaling(Scaling.fit), new Table (ft -> {
            ft.top().right().add((multiplier < 0 ? "[accent]" : "[scarlet]+") + (multiplier * 100f) + "%").style(Styles.outlineLabel);
        }));
    }
}