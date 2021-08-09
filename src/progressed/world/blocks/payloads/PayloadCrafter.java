package progressed.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import progressed.ui.*;

import static mindustry.Vars.*;

public class PayloadCrafter extends BlockProducer{
    public Seq<Block> products;
    public boolean hasTop = true;

    public int[] capacities = {};

    public PayloadCrafter(String name){
        super(name);

        buildSpeed = 0.1f;
        configurable = true;

        config(Block.class, (ShellBuilderBuild tile, Block block) -> {
            if(tile.recipe != block) tile.progress = 0f;
            if(canProduce(block)){
                tile.recipe = block;
            }
        });
    }

    @Override
    public void init(){
        consumes.add(new DynamicConsumePower());

        capacities = new int[content.items().size];
        products.each(r -> {
            for(ItemStack stack : r.requirements){
                capacities[stack.item.id] = Math.max(capacities[stack.item.id], stack.amount * 2);
                itemCapacity = Math.max(itemCapacity, stack.amount * 2);
            }
        });

        super.init();
    }

    @Override
    public void load(){
        super.load();

        inRegion = Core.atlas.find(name + "-in", Core.atlas.find("factory-in-" + size, "prog-mats-factory-in-" + size));
        outRegion = Core.atlas.find(name + "-out", Core.atlas.find("factory-out-" + size, "prog-mats-factory-out-" + size));
        if(!hasTop) topRegion = Core.atlas.find("clear");
    }

    @Override
    public TextureRegion[] icons(){
        if(products.contains(b -> b instanceof Missile m && m.prev != null)){
            return new TextureRegion[]{region, inRegion, outRegion, topRegion};
        }
        return new TextureRegion[]{region, outRegion, topRegion};
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.remove(Stat.powerUse);

        stats.add(Stat.input, t -> {
           t.row();

           products.each(p -> {
               t.image(p.fullIcon).padRight(4).right().top();
               t.add(p.localizedName).padRight(10).left().top();

               t.table(ct -> {
                   ct.left().defaults().padRight(3).left();

                   ct.table(it -> {
                       it.add(Stat.input.localized() + ": ");
                       for(ItemStack stack : p.requirements){
                           it.add(PMElements.itemImage(stack.item.uiIcon, () -> stack.amount == 0 ? "" : stack.amount + ""));
                       }
                   });

                   if(p instanceof Missile m){
                       if(m.prev != null){
                           ct.row();
                           ct.table(pt -> {
                               pt.image(m.prev.fullIcon).padLeft(60f).padRight(4).right().top();
                               pt.add(m.prev.localizedName).padRight(10).left().top();
                           });
                       }
                       if(m.powerCost > 0){
                           ct.row();
                           ct.add(Stat.powerUse.localized() + ": " + (m.powerCost * 60f) + " " + StatUnit.powerSecond.localized());
                       }
                       if(m.requiresUnlock){
                           ct.row();
                           ct.add("@block.pm-requires-unlock");
                       }
                   }
               }).padTop(-9).left().get().background(Tex.underline);

               t.row();
           });
        });
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list){
        Draw.rect(region, req.drawx(), req.drawy());
        if(products.contains(b -> b instanceof Missile m && m.prev != null)) Draw.rect(inRegion, req.drawx(), req.drawy(), req.rotation * 90);
        Draw.rect(outRegion, req.drawx(), req.drawy(), req.rotation * 90);
        Draw.rect(topRegion, req.drawx(), req.drawy());
    }

    public boolean canProduce(Block b){
        boolean unlocked = true;
        if(b instanceof Missile m && m.requiresUnlock){
            unlocked = b.unlockedNow();
        }
        return unlocked && products.contains(b);
    }

    public ItemStack[] getCost(int rec){
        return products.get(rec).requirements;
    }

    public class ShellBuilderBuild extends BlockProducerBuild{
        public @Nullable Block recipe;

        @Override
        public @Nullable Block recipe(){
            return recipe;
        }

        @Override
        public void control(LAccess type, Object p1, double p2, double p3, double p4){
            if(p1 instanceof Block b && products.contains(b)){
                recipe = b;
            }
            super.control(type, p1, p2, p3, p4);
        }

        @Override
        public Object senseObject(LAccess sensor){
            if(sensor == LAccess.config) return recipe;
            if(sensor == LAccess.progress) return progress;
            return super.senseObject(sensor);
        }

        @Override
        public void updateTile(){
            var recipe = recipe();
            boolean produce = recipe instanceof Missile m && consValid() && (m.prev != null ? (payload != null && hasArrived() && payload.block() == m.prev) : payload == null);

            if(recipe instanceof Missile m){
                if(payload != null && payload.block() != m.prev){
                    moveOutPayload();
                }
            }else{
                moveOutPayload();
            }

            if(recipe instanceof Missile m && m.prev != null && payload != null && payload.block() != m){
                moveInPayload(false);
            }

            if(produce){
                progress += buildSpeed * edelta();

                if(progress >= recipe.buildCost){
                    consume();
                    payload = new BuildPayload(recipe, team);
                    payVector.setZero();
                    progress %= 1f;
                }
            }

            heat = Mathf.lerpDelta(heat, Mathf.num(produce), 0.15f);
            time += heat * delta();
        }

        @Override
        public void draw(){
            Draw.rect(region, x, y);

            //draw input
            if(curInput()){
                for(int i = 0; i < 4; i++){
                    if(blends(i) && i != rotation){
                        Draw.rect(inRegion, x, y, (i * 90f) - 180f);
                    }
                }
            }

            Draw.rect(outRegion, x, y, rotdeg());

            drawPayload();

            if(recipe != null){
                Draw.draw(Layer.blockOver, () -> Drawf.construct(this, recipe.fullIcon, 0, progress / recipe.buildCost, heat, time));
            }

            Draw.z(Layer.blockOver);

            Draw.z(Layer.blockOver + 0.1f);
            Draw.rect(topRegion, x, y);
        }

        @Override
        public void drawSelect(){
            // Do not
        }

        public boolean curInput(){
            return recipe instanceof Missile m && m.prev != null;
        }

        @Override
        public int getMaximumAccepted(Item item){
            return capacities[item.id];
        }

        @Override
        public void buildConfiguration(Table table){
            ItemSelection.buildTable(table, content.blocks().select(PayloadCrafter.this::canProduce), () -> recipe, this::configure);
            if(!products.contains(PayloadCrafter.this::canProduce)){
                table.add("@block.pm-no-unlock"); //If you can't build anything, go unlock stuff.
            }
        }

        @Override
        public Object config(){
            return recipe;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            return this.payload == null && recipe instanceof Missile m && payload instanceof BuildPayload p && p.block() == m.prev;
        }
    }

    protected class DynamicConsumePower extends ConsumePower{
        public DynamicConsumePower(){
            super();
        }

        @Override
        public float requestedPower(Building entity){
            if(entity instanceof ShellBuilderBuild s && s.recipe() instanceof Missile m){
                return m.powerCost;
            }

            return super.requestedPower(entity);
        }
    }
}
