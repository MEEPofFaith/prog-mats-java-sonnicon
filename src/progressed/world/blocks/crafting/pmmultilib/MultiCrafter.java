package progressed.world.blocks.crafting.pmmultilib;

import arc.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.fragments.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import progressed.ui.*;
import progressed.world.blocks.crafting.pmmultilib.Recipe.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class MultiCrafter extends GenericCrafter{
    private final int invTimer = timers++;
    private final MultiCrafterBlockInventoryFragment invFrag = new MultiCrafterBlockInventoryFragment();

    public Seq<Recipe> recipes = new Seq<>();

    protected Seq<Item> possibleInputs = new Seq<>();

    public MultiCrafter(String name){
        super(name);

        configurable = true;

        config(Integer.class, (MultiCrafterBuild tile, Integer i) -> {
           if(tile.recipe == i){
               tile.recipe = -1;
           }else{
               tile.recipe = i;
           }
           tile.progress = 0;
        });
        config(Item.class, (MultiCrafterBuild tile, Item i) -> {
            Recipe rec = recipes.find(r -> r.outputItems()[0].item == i);
            if(rec != null) tile.recipe = recipes.indexOf(rec);
        });

        configClear((MultiCrafterBuild tile) -> tile.recipe = 0);
    }

    public void addRecipe(Inputs inputs, Outputs outputs, boolean requiresResearch){
        recipes.add(new Recipe(inputs, outputs, requiresResearch));
    }

    public void addRecipe(Inputs inputs, Outputs outputs){
        recipes.add(new Recipe(inputs, outputs));
    }

    @Override
    public void init(){
        consumes.add(new MultiCrafterConsumeItems());
        consumes.add(new MultiCrafterConsumePower());

        super.init();

        if(recipes.size <= 0){
            throw new RuntimeException("Bruh, MultiCrafter " + name + " doesn't have any recipies!");
        }

        if(recipes.contains(Recipe::canRequirePower)){
            hasPower = true;
        }

        recipes.each(r -> {
           for(ItemStack stack : r.inputItems()){
               possibleInputs.add(stack.item);
           }
        });
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.remove(Stat.productionTime);
        stats.remove(Stat.powerUse);
        stats.remove(Stat.input);

        stats.add(Stat.input, t -> {
           t.row();

           recipes.each(r -> {
                t.table(tt -> {
                    tt.add(Core.bundle.format("stat.pm-multi-recipe", recipes.indexOf(r) + 1)).left().row();

                    //Inputs
                    tt.add("[accent]" + Stat.input.localized()).left().padLeft(8f).row();
                    tt.table(s -> {
                        int cols = 9;
                        int row = 0;

                        for(ItemStack stack : r.inputItems()){
                            s.add(PMElements.itemImage(stack.item.uiIcon, () -> stack.amount == 0 ? "" : stack.amount + ""));
                            if(row++ % cols == cols - 1) s.row();
                        }
                    }).left().top().padLeft(16f).row();
                    tt.table(p -> {
                        StatValues.number(r.inputPower() * 60f, StatUnit.powerSecond).display(p);
                    }).left().top().padLeft(16f).row();
                    tt.table(c -> {
                        StatValues.number(r.craftTime() / 60f, StatUnit.seconds).display(c);
                    }).left().padLeft(16f).row();

                    //Outputs
                    tt.add("[accent]" + Stat.output.localized()).left().padLeft(8f).row();
                    tt.table(s -> {
                        int cols = 9;
                        int row = 0;

                        for(ItemStack stack : r.outputItems()){
                            s.add(PMElements.itemImage(stack.item.uiIcon, () -> stack.amount == 0 ? "" : stack.amount + ""));
                            if(row++ % cols == cols - 1) s.row();
                        }
                    }).left().top().padLeft(16f);
                }).padTop(4f).padLeft(8f).color(Pal.accent).left().get().background(Tex.button);
                t.row();
           });
        });
    }

    public ItemStack[] getCost(int rec){
        return recipes.get(rec).inputItems();
    }

    public class MultiCrafterBuild extends GenericCrafterBuild{
        public int recipe = -1;
        protected float itemHas;
        protected boolean openInv;

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4){
            if(type == LAccess.config){
                recipe = (int)p1;
            }

            super.control(type, p1, p2, p3, p4);
        }

        @Override
        public void updateTile(){
            if(timer.get(invTimer, 6)){
                itemHas = 0;
                items.each((item, amount) -> itemHas++);
            }
            if(!headless && control.input.frag.config.getSelectedTile() != this && openInv){
                invFrag.hide();
                openInv = false;
            }

            //Why bother with liquids if nothings gonna use liquids?
            if(recipe >= 0){
                if(consValid()){
                    progress += getProgressIncrease(getRecipe().craftTime());
                    totalProgress += delta();
                    warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

                    if(Mathf.chanceDelta(updateEffectChance)){
                        updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4));
                    }
                }else{
                    warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                }

                ItemStack[] currentOutput = getRecipe().outputItems();

                if(progress >= 1f){
                    consume();

                    if(currentOutput != null){
                        for(ItemStack output : currentOutput){
                            for(int i = 0; i < output.amount; i++){
                                offload(output.item);
                            }
                        }
                    }

                    craftEffect.at(x, y);
                    progress %= 1f;
                }

                if(currentOutput != null && timer(timerDump, dumpTime / timeScale)){
                    for(ItemStack output : currentOutput){
                        dump(output.item);
                    }
                }
            }
        }

        @Override
        public void updateTableAlign(Table table){
            float pos = input.mouseScreen(x, y - size * 4 - 1).y;
            Vec2 relative = input.mouseScreen(x, y + size * 4);

            table.setPosition(relative.x, Math.min(pos, (float)(relative.y - Math.ceil(itemHas / 3f) * 48f - 4f)), Align.top);
            if(!invFrag.isShown() && control.input.frag.config.getSelectedTile() == this && items.any()) invFrag.showFor(this);
        }

        @Override
        public void buildConfiguration(Table table){
            if(invFrag.isShown()){
                invFrag.hide();
                control.input.frag.config.hideConfig();
            }
            if(!invFrag.isBuilt()) invFrag.build(table.parent);
            openInv = true;

            ButtonGroup<ImageButton> group = new ButtonGroup<>();
            group.setMinCheckCount(0);
            group.setMaxCheckCount(1);

            Table cont = new Table();
            cont.defaults().size(40f);

            recipes.each(r -> {
                boolean researched = true;
                if(r.requireResearch){
                    for(ItemStack output : r.outputs.outputItems){
                        if(!output.item.unlockedNow()){
                            researched = false;
                            break;
                        }
                    }
                }
                if(researched){
                    int index = recipes.indexOf(r);
                    ImageButton button = cont.button(Tex.whiteui, Styles.clearToggleTransi, 40f, () -> {}).group(group).get();
                    button.changed(() -> configure(index));
                    button.getStyle().imageUp = new TextureRegionDrawable(r.outputs.outputItems[0].item.fullIcon, 8f * 3f);
                    button.update(() -> button.setChecked(index == recipe));
                }
            });

            if(group.getButtons().size >= 1){
                table.add(cont);
            }else{
                table.add("@block.pm-recipe-notice");
            }
        }

        @Override
        public boolean onConfigureTileTapped(Building other){
            if(self() == other){
                deselect();
                recipe = -1;
                return false;
            }

            return true;
        }

        @Override
        public Object config(){
            if(recipe >= 0){
                return getRecipe().outputItems()[0].item;
            }
            return null;
        }

        @Override
        public void deselect(){
            if(!headless && control.input.frag.config.getSelectedTile() == self()){
                control.input.frag.config.hideConfig();
                invFrag.hide();
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            if(items.get(item) >= getMaximumAccepted(item)) return false;
            return possibleInputs.contains(item);
        }

        @Override
        public boolean consValid(){
            if(recipe >= 0){
                return super.consValid();
            }else{
                return false;
            }
        }

        public Recipe getRecipe(){
            if(recipe >= 0){
                return recipes.get(recipe);
            }else{
                return null;
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.i(recipe);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            if(revision >= 1){
                recipe = read.i();
            }
        }

        @Override
        public byte version(){
            return 1;
        }
    }

    protected class MultiCrafterConsumePower extends ConsumePower{
        public MultiCrafterConsumePower(){
            super();
        }

        @Override
        public float requestedPower(Building entity){
            if(entity instanceof MultiCrafterBuild b && b.recipe >= 0){
                return b.getRecipe().inputPower();
            }else{
                return 0f;
            }
        }
    }

    protected class MultiCrafterConsumeItems extends ConsumeItems{
        public MultiCrafterConsumeItems(){
            super();
        }

        @Override
        public boolean valid(Building entity){
            if(entity instanceof MultiCrafterBuild b && b.recipe >= 0){
                return entity.items != null && entity.items.has(b.getRecipe().inputItems());
            }else{
                return false;
            }
        }

        @Override
        public void trigger(Building entity){
            if(entity instanceof MultiCrafterBuild b && b.recipe >= 0){
                for(ItemStack stack : b.getRecipe().inputItems()){
                    entity.items.remove(stack);
                }
            }
        }
    }

    protected static class MultiCrafterBlockInventoryFragment extends BlockInventoryFragment{
        private boolean built = false;
        private boolean visible = false;

        public boolean isBuilt(){
            return built;
        }

        public boolean isShown(){
            return visible;
        }

        @Override
        public void showFor(Building t){
            visible = true;
            super.showFor(t);
        }

        @Override
        public void hide(){
            visible = false;
            super.hide();
        }

        @Override
        public void build(Group parent){
            built = true;
            super.build(parent);
        }
    }
}
