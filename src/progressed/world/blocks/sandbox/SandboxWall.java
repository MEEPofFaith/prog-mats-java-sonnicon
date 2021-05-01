package progressed.world.blocks.sandbox;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.meta.*;
import progressed.util.*;

import static mindustry.Vars.*;

public class SandboxWall extends Wall{
    public float speed = 1.5f;
    public float rotateSpeed = 6f, rotateRadius, iconSize;

    protected Item[] iconItems = {Items.surgeAlloy,  Items.phaseFabric, Items.plastanium};
    protected TextureRegion colorRegion;
    protected TextureRegion[] colorVariantRegions;

    public SandboxWall(String name){
        super(name);
        requirements(Category.defense, BuildVisibility.sandboxOnly, ItemStack.empty);
        alwaysUnlocked = true;
        
        lightningChance = 1f;
        lightningDamage = 5000f;
        lightningLength = 10;
        chanceDeflect = 100000000f;
        flashHit = insulated = absorbLasers = true;
        schematicPriority = 10;
        configurable = saveConfig = update = noUpdateDisabled = true;

        config(boolean[].class, (SandboxWallBuild tile, boolean[] arr) -> tile.modes = arr.clone());
        configClear((SandboxWallBuild tile) -> tile.modes = new boolean[3]);
    }

    @Override
    public void load(){
        super.load();

        colorRegion = Core.atlas.find(name + "-color");
        
        if(variants != 0){
            colorVariantRegions = new TextureRegion[variants];

            for(int i = 0; i < variants; i++){
                colorVariantRegions[i] = Core.atlas.find(name + "-color-" + i);
            }
            colorRegion = colorVariantRegions[0];
        }
    }

    public class SandboxWallBuild extends WallBuild{
        boolean[] modes = new boolean[3];
        protected float rotation = 90f;

        @Override
        public void draw(){
            if(variants == 0){
                Draw.rect(region, x, y);
                Draw.color(Tmp.c1.set(Color.red).shiftHue(Time.time * speed), 1f);
                Draw.rect(colorRegion, x, y);
                Draw.reset();
            }else{
                int variant = Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1));
                Draw.rect(variantRegions[variant], x, y);
                Draw.color(Tmp.c1.set(Color.red).shiftHue(Time.time * speed), 1f);
                Draw.rect(colorVariantRegions[variant], x, y);
                Draw.reset();
            }

            //draw flashing white overlay if enabled
            if(flashHit && modes[1] && hit >= 0.0001f){

                Draw.color(flashColor);
                Draw.alpha(hit * 0.5f);
                Draw.blend(Blending.additive);
                Fill.rect(x, y, tilesize * size, tilesize * size);
                Draw.blend();
                Draw.reset();
            }

            //draw floating items to represent active mode
            int num = 0;
            int amount = PMUtls.boolArrToInt(modes);
            for(int i = 0; i < 3; i++){
                if(modes[i]){
                    float rot = rotation + 360f / amount * num;
                    Draw.rect(iconItems[i].icon(Cicon.full), x + Angles.trnsx(rot, rotateRadius), y + Angles.trnsy(rot, rotateRadius), iconSize, iconSize, 0f);
                    num++;
                }
            }
        }

        @Override
        public void updateTile(){
            super.updateTile();
            hit = Mathf.clamp(hit - Time.delta / 10f);
            rotation = (rotation - Time.delta * rotateSpeed) % 360f;
        }

        @Override
        public boolean collision(Bullet bullet){
            damage(bullet.damage() * bullet.type().buildingDamageMultiplier);

            hit = 1f;

            //create lightning if necessary
            if(lightningChance > 0f && modes[0]){
                if(Mathf.chance(lightningChance)){
                    Lightning.create(team, lightningColor, lightningDamage, x, y, bullet.rotation() + 180f, lightningLength);
                    lightningSound.at(tile, Mathf.random(0.9f, 1.1f));
                }
            }

            //deflect bullets if necessary
            if(chanceDeflect > 0f && modes[1]){
                //slow bullets are not deflected
                if(bullet.vel().len() <= 0.1f || !bullet.type.reflectable) return true;

                //bullet reflection chance depends on bullet damage
                if(!Mathf.chance(chanceDeflect / bullet.damage())) return true;

                //make sound
                deflectSound.at(tile, Mathf.random(0.9f, 1.1f));

                //translate bullet back to where it was upon collision
                bullet.trns(-bullet.vel.x, -bullet.vel.y);

                float penX = Math.abs(x - bullet.x), penY = Math.abs(y - bullet.y);

                if(penX > penY){
                    bullet.vel.x *= -1;
                }else{
                    bullet.vel.y *= -1;
                }

                bullet.owner(this);
                bullet.team(team);
                bullet.time(bullet.time() + 1f);

                //disable bullet collision by returning false
                return false;
            }

            return true;
        }

        @Override
        public void buildConfiguration(Table table){
            ButtonGroup<ImageButton> group = new ButtonGroup<>();
            group.setMinCheckCount(0);
            group.setMaxCheckCount(3);
            Table cont = new Table();
            cont.defaults().size(40);

            for(int i = 0; i < 3; i++){
                int ii = i;
                ImageButton button = cont.button(Tex.whiteui, Styles.clearToggleTransi, 40, () -> {}).group(group).get();
                button.changed(() -> configure(ii));
                button.getStyle().imageUp = new TextureRegionDrawable(iconItems[i].icon(Cicon.small));
                button.update(() -> button.setChecked(modes[ii]));
            }

            table.add(cont);
        }

        @Override
        public boolean onConfigureTileTapped(Building other){
            if(this == other){
                deselect();
                for(int i = 0; i < 3; i++){
                    if(modes[i]) configure(i);
                }
                return false;
            }

            return true;
        }

        @Override
        public void configure(Object value){
            if (value instanceof Integer v) {
                for (int i = 0; i < modes.length; i++) {
                    modes[i] = getBit(v, i) == 1 ? true : false;
                }
            }

            block.lastConfig = value;
            Call.tileConfig(player, self(), value);
        }

        @Override
        public void configureAny(Object value){
            if (value instanceof Integer v) {
                for (int i = 0; i < modes.length; i++) {
                    modes[i] = getBit(v, i) == 1 ? true : false;
                }
            }

            block.lastConfig = value;
            Call.tileConfig(player, self(), value);
        }

        @Override
        public Integer config() {
            int rtn = 0;

            for (int i = 0;i < modes.length;i++) {
                rtn = setBit(rtn, i, modes[i]);
            }
            return rtn;
        }

        protected int setBit(int number, int pos, boolean set) {
            if (set) {
                return number | (1 << pos);
            }
            return number & ~(1 << pos);
        }

        protected byte getBit(int number, int pos) {
            return (byte) ((number >> pos) & 1);
        }

        @Override
        public void write(Writes write){
            super.write(write);
            for(int i = 0; i < 3; i++){
                write.bool(modes[i]);
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            if(revision >= 1){
                modes = new boolean[]{read.bool(), read.bool(), read.bool()};
            }
        }

        @Override
        public byte version(){
            return 1;
        }
    }
}
