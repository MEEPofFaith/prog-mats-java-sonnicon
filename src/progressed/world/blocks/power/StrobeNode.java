package progressed.world.blocks.power;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.core.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;

public class StrobeNode extends PowerNode{
    public float speed = 1.5f, lerpSpeed = 0.005f;
    public Color laserColor3 = Color.red;

    protected TextureRegion colorRegion;

    public StrobeNode(String name){
        super(name);
        requirements(Category.power, BuildVisibility.sandboxOnly, ItemStack.empty);
        
        health = 10000;
        laserRange = 200;
        maxNodes = 65535;
        laserColor1 = Color.valueOf("FFCCCC");
        laserColor2 = Color.valueOf("fb6767");
        alwaysUnlocked = true;
    }

    @Override
    public void load(){
        super.load();

        colorRegion = Core.atlas.find(name + "-strobe");
        laser = Core.atlas.find("prog-mats-rainbow-laser");
        laserEnd = Core.atlas.find("prog-mats-rainbow-laser-end");
    }

    @Override
    protected void setupColor(float satisfaction){
        Color c1 = laserColor1.cpy().lerp(laserColor3, Mathf.absin(Time.time * lerpSpeed, 1, 1));
        Draw.color(c1.shiftHue(Time.time * speed), laserColor2.cpy().shiftHue(Time.time * speed), 1 - satisfaction);
        Draw.alpha(Renderer.laserOpacity);
    }

    public class StrobeNodeBuild extends PowerNodeBuild{
        @Override
        public void draw(){
            super.draw();
            Draw.z(Layer.block);
            Color c1 = laserColor1.cpy().lerp(laserColor3, Mathf.absin(Time.time * lerpSpeed, 1, 1));
            Draw.color(c1.shiftHue(Time.time * speed), laserColor2.cpy().shiftHue(Time.time * speed), 1 - this.power.graph.getSatisfaction());
            Draw.alpha(1);
            Draw.rect(colorRegion, this.x, this.y);
            Draw.reset();
        }
    }
}