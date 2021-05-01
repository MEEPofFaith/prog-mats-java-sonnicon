package progressed.world.blocks.sandbox;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;
import progressed.ui.*;

import static mindustry.Vars.*;

public class MultiSource extends Block{
    protected TextureRegion cross;
    protected TextureRegion[] center = new TextureRegion[2];

    public MultiSource(String name){
        super(name);
        requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.empty);
        alwaysUnlocked = true;

        update = true;
        solid = true;
        hasItems = true;
        hasLiquids = true;
        liquidCapacity = 100f;
        configurable = true;
        outputsLiquid = true;
        saveConfig = true;
        noUpdateDisabled = true;
        displayFlow = false;

        config(SourceData.class, (MultiSourceBuild tile, SourceData m) -> tile.data.set(m));
        configClear((MultiSourceBuild tile) -> tile.data.clear());
    }

    @Override
    public void setBars(){
        super.setBars();
        bars.remove("items");
        bars.remove("liquid");
    }

    @Override
    public void load(){
        super.load();

        cross = Core.atlas.find(name + "-cross");
        center[0] = Core.atlas.find(name + "-center-0");
        center[1] = Core.atlas.find(name + "-center-1");
    }

    @Override
    public void drawRequestConfig(BuildPlan req, Eachable<BuildPlan> list){
        Draw.rect(cross, req.drawx(), req.drawy());
        if(req.config instanceof SourceData data){
            drawRequestConfigCenter(req, data.item, name + "-center-0");
            drawRequestConfigCenter(req, data.liquid, name + "-center-1");
        }
    }

    public class MultiSourceBuild extends Building{
        protected SourceData data = new SourceData();

        @Override
        public void draw(){
            super.draw();

            Draw.rect(cross, x, y);

            if(data.item != null){
                Draw.color(data.item.color);
                Draw.rect(center[0], x, y);
                Draw.color();
            }
            
            if(data.liquid != null){
                Draw.color(data.liquid.color);
                Draw.rect(center[1], x, y);
                Draw.color();
            }
        }

        @Override
        public void updateTile(){
            if(data.item != null){
                items.set(data.item, 1);
                dump(data.item);
                items.set(data.item, 0);
            }

            if(data.liquid == null){
                liquids.clear();
            }else{
                liquids.add(data.liquid, liquidCapacity);
                dumpLiquid(data.liquid);
            }
        }

        @Override
        public void buildConfiguration(Table table){
            ImageButtonStyle style = new ImageButtonStyle(Styles.clearTransi);
            style.imageDisabledColor = Color.gray;
            Cell<ImageButton> b = table.button(Icon.cancel, style, () -> data.clear()).top().size(40f);
            b.get().setDisabled(data::invalid);

            table.table(t -> {
                PMItemSelection.buildTable(t, content.items(), () -> data.item, this::configure, false, true).top();
                t.row();
                t.image(Tex.whiteui).size(40f * 4f, 8f).color(Color.gray).left().top();
                t.row();
                PMItemSelection.buildTable(t, content.liquids(), () -> data.liquid, this::configure, false, true).top();
            });
        }

        @Override
        public boolean onConfigureTileTapped(Building other){
            if(this == other){
                deselect();
                return false;
            }

            return true;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return false;
        }

        @Override
        public SourceData config(){
            return data;
        }

        @Override
        public void configure(Object value){
            if(value instanceof Item i){
                if(data.item == i){
                    data.item = null;
                }else{
                    data.set(i);
                }
            }else if(value instanceof Liquid l){
                if(data.liquid == l){
                    data.liquid = null;
                }else{
                    data.set(l);
                }
            }
            //save last used config
            block.lastConfig = data;
            Call.tileConfig(player, self(), value);
        }

        @Override
        public void configureAny(Object value){
            if(value instanceof Item i){
                if(data.item == i){
                    data.item = null;
                }else{
                    data.set(i);
                }
            }else if(value instanceof Liquid l){
                if(data.liquid == l){
                    data.liquid = null;
                }else{
                    data.set(l);
                }
            }
            Call.tileConfig(player, self(), value);
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.s(data.item == null ? -1 : data.item.id);
            write.s(data.liquid == null ? -1 : data.liquid.id);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            data.set(content.item(read.s()), content.liquid(read.s()));
        }
    }

    public static class SourceData{
        protected Item item;
        protected Liquid liquid;

        public SourceData(Item item, Liquid liquid){
            this.item = item;
            this.liquid = liquid;
        }
        
        public SourceData(){}

        public void set(Item item, Liquid liquid){
            this.item = item;
            this.liquid = liquid;
        }

        public void set(Item item){
            set(item, this.liquid);
        }

        public void set(Liquid liquid){
            set(this.item, liquid);
        }

        public void set(SourceData data){
            set(data.item, data.liquid);
        }

        public void set(Object[] arr){
            set((Item)arr[0], (Liquid)arr[1]);
        }

        public Object[] toArray(){
            return new Object[]{item, liquid};
        }

        public boolean invalid(){
            return item == null && liquid == null;
        }

        public void clear(){
            item = null;
            liquid = null;
        }
    }
}