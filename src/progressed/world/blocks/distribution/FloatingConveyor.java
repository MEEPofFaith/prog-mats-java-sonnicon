package progressed.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;

import static mindustry.Vars.*;

public class FloatingConveyor extends Conveyor{
    public boolean drawShallow;
    public float coverOpacity = 0.5f;

    protected TextureRegion[] floatRegions = new TextureRegion[5], coverRegions = new TextureRegion[5];

    public FloatingConveyor(String name){
        super(name);
        floating = true;
        placeableLiquid = true;
    }

    @Override
    public void load(){
        super.load();
        for(int i = 0; i < 5; i++){
            floatRegions[i] = Core.atlas.find(name + "-float-" + i);
            coverRegions[i] = Core.atlas.find(name + "-cover-" + i);
        }
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list){
        super.drawRequestRegion(req, list);
        if(shouldDrawOverlay(req.drawx(), req.drawy())){
            int[] bits = getTiling(req, list);

            if(bits == null) return;

            Draw.color(world.tileWorld(req.drawx(), req.drawy()).floor().mapColor, coverOpacity);
            TextureRegion region = coverRegions[bits[0]];
            Draw.rect(region, req.drawx(), req.drawy(), region.width * bits[1] * Draw.scl, region.height * bits[2] * Draw.scl, req.rotation * 90);
            Draw.color();
            region = floatRegions[bits[0]];
            Draw.rect(region, req.drawx(), req.drawy(), region.width * bits[1] * Draw.scl, region.height * bits[2] * Draw.scl, req.rotation * 90);
        }
    }

    public boolean shouldDrawOverlay(float x, float y){
        Tile t = world.tileWorld(x, y);
        if(t != null && t.floor().isLiquid){
            if(drawShallow || t.floor().isDeep()){
                return true;
            }
        }
        return false;
    }

    public class FloatingConveyorBuild extends ConveyorBuild{
        @Override
        public void draw(){
            super.draw();
            if(shouldDrawOverlay(x, y)){
                Draw.z(Layer.block - 0.09f);
                Draw.color(world.tileWorld(x, y).floor().mapColor, coverOpacity);
                Draw.rect(coverRegions[blendbits], x, y, coverRegions[blendbits].width / 4f * blendsclx, coverRegions[blendbits].height / 4f * blendscly, rotation * 90);
                Draw.color();

                Draw.z(Layer.block - 0.08f);
                Draw.rect(floatRegions[blendbits], x, y, floatRegions[blendbits].width / 4f * blendsclx, floatRegions[blendbits].height / 4f * blendscly, rotation * 90);
            }
        }

        @Override
        public void unitOn(Unit unit){
            if(!shouldDrawOverlay(x, y)){
                super.unitOn(unit);
            }
        }
    }
}