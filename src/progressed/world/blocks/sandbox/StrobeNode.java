package progressed.world.blocks.sandbox;

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
        alwaysUnlocked = true;
        
        health = 999999999;
        laserRange = 200;
        maxNodes = 65535;
        laserColor1 = Color.valueOf("ffcccc");
        laserColor2 = Color.valueOf("fb6767");
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
        Color c1 = Tmp.c1.set(laserColor1).lerp(laserColor3, Mathf.absin(Time.time * lerpSpeed, 1, 1));
        Draw.color(c1.shiftHue(Time.time * speed), Tmp.c1.set(laserColor2).shiftHue(Time.time * speed), 1 - satisfaction);
        Draw.alpha(Renderer.laserOpacity);
    }

    public class StrobeNodeBuild extends PowerNodeBuild{
        @Override
        public void draw(){
            super.draw();
            Draw.z(Layer.block);
            Color c1 = Tmp.c1.set(laserColor1).lerp(laserColor3, Mathf.absin(Time.time * lerpSpeed, 1, 1));
            Draw.color(c1.shiftHue(Time.time * speed), Tmp.c1.set(laserColor2).shiftHue(Time.time * speed), 1 - this.power.graph.getSatisfaction());
            Draw.alpha(1);
            Draw.rect(colorRegion, this.x, this.y);
            Draw.reset();
        }
    }
}