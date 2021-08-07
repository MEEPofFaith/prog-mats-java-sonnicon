package progressed.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.consumers.*;

public class ShellBuilder extends BlockForge{
    public Seq<Block> products;

    public ShellBuilder(String name){
        super(name);
    }

    @Override
    public void init(){
        consumes.add(new DynamicConsumePower());

        super.init();
    }

    @Override
    public void load(){
        super.load();

        outRegion = Core.atlas.find(name + "-out", Core.atlas.find("factory-out-" + size, "prog-mats-factory-out-" + size));
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{region, outRegion, topRegion};
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list){
        super.drawRequestRegion(req, list);
        Draw.rect(topRegion, req.drawx(), req.drawy());
    }

    @Override
    public boolean canProduce(Block b){
        return b.unlockedNow() && products.contains(b);
    }

    public ItemStack[] getCost(int rec){
        return products.get(rec).requirements;
    }

    public class ShellBuilderBuild extends BlockForgeBuild{
        @Override
        public void draw(){
            Draw.rect(region, x, y);
            Draw.rect(outRegion, x, y, rotdeg());

            if(recipe != null){
                Draw.draw(Layer.blockOver, () -> Drawf.construct(this, recipe.fullIcon, 0, progress / recipe.buildCost, heat, time));
            }

            Draw.z(Layer.blockOver);
            drawPayload();

            Draw.z(Layer.blockOver + 0.1f);
            Draw.rect(topRegion, x, y);
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
