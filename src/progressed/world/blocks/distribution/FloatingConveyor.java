package progressed.world.blocks.distribution;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;

import static mindustry.Vars.*;

public class FloatingConveyor extends Conveyor{
    public Color coverColor;

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
    public void init(){
        super.init();

        if(coverColor == null) coverColor = Pal.shadow;
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list){
        super.drawRequestRegion(req, list);
        Tile t = world.tileWorld(req.drawx(), req.drawy());
        if(t != null && t.floor().isDeep()){
            int[] bits = getTiling(req, list);

            if(bits == null) return;

            Draw.color(coverColor);
            TextureRegion region = coverRegions[bits[0]];
            Draw.rect(region, req.drawx(), req.drawy(), region.width * bits[1] * Draw.scl, region.height * bits[2] * Draw.scl, req.rotation * 90);
            Draw.color();
            region = floatRegions[bits[0]];
            Draw.rect(region, req.drawx(), req.drawy(), region.width * bits[1] * Draw.scl, region.height * bits[2] * Draw.scl, req.rotation * 90);
        }
    }

    public class FloatingConveyorBuild extends ConveyorBuild{
        @Override
        public void draw(){
            super.draw();
            if(tile().floor().isDeep()){
                Draw.z(Layer.block - 0.09f);
                Draw.color(coverColor);
                Draw.rect(coverRegions[blendbits], x, y, coverRegions[blendbits].width / 4f * blendsclx, coverRegions[blendbits].height / 4f * blendscly, rotation * 90);
                Draw.color();

                Draw.z(Layer.block - 0.08f);
                Draw.rect(floatRegions[blendbits], x, y, floatRegions[blendbits].width / 4f * blendsclx, floatRegions[blendbits].height / 4f * blendscly, rotation * 90);
            }
        }

        @Override
        public void unitOn(Unit unit){
            if(!tile().floor().isDeep()){
                super.unitOn(unit);
            }
        }
    }
}