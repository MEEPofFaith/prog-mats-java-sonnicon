package progressed.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.BuildPlan;
import mindustry.graphics.*;
import mindustry.world.blocks.distribution.*;

import static mindustry.Vars.*;

public class FloatingConveyor extends Conveyor{
    public float elevation = -1f;

    protected TextureRegion[] floatRegions = new TextureRegion[5];

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
        }
    }

    @Override
    public void init(){
        super.init();

        if(elevation < 0) elevation = size * 2f;
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list){
        super.drawRequestRegion(req, list);
        if(world.tileWorld(req.drawx(), req.drawy()).floor().isDeep()){
            int[] bits = getTiling(req, list);

            if(bits == null) return;

            TextureRegion region = floatRegions[bits[0]];
            Draw.rect(region, req.drawx(), req.drawy(), region.width * bits[1] * Draw.scl, region.height * bits[2] * Draw.scl, req.rotation * 90);
        }
    }

    public class FloatingConveyorBuild extends ConveyorBuild{
        @Override
        public void draw(){
            super.draw();
            if(tile().floor().isDeep()){
                int frame = enabled && clogHeat <= 0.5f ? (int)((Time.time * speed * 8f * timeScale) % 4) : 0;

                //draw extra conveyors facing this one for non-square tiling purposes
                for(int i = 0; i < 4; i++){
                    if((blending & (1 << i)) != 0){
                        int dir = rotation - i;
                        float rot = i == 0 ? rotation * 90 : (dir)*90;

                        Draw.z(Layer.blockUnder - 0.01f);
                        Drawf.shadow(sliced(regions[blendbits][frame], i != 0 ? SliceMode.bottom : SliceMode.top), x + Geometry.d4x(dir) * tilesize * 0.75f - elevation, y + Geometry.d4y(dir) * tilesize * 0.75f - elevation, rot);
                        Draw.z(Layer.block - 0.199f);
                        Draw.rect(sliced(floatRegions[blendbits], i != 0 ? SliceMode.bottom : SliceMode.top), x + Geometry.d4x(dir) * floatRegions[blendbits].width / 4f * 0.75f, y + Geometry.d4y(dir) * floatRegions[blendbits].height / 4f * 0.75f, rot);
                    }
                }

                Draw.z(Layer.blockUnder - 0.01f);
                Drawf.shadow(regions[blendbits][frame], x - elevation, y - elevation, tilesize * blendsclx, tilesize * blendscly, rotation * 90);
                Draw.z(Layer.block - 0.199f);
                Draw.rect(floatRegions[blendbits], x, y, floatRegions[blendbits].width / 4f * blendsclx, floatRegions[blendbits].height / 4f * blendscly, rotation * 90);
            }
        }
    }
}
