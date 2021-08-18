package progressed.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Missile extends NuclearWarhead{
    public BulletType explosion;
    public int explosions = 1;
    public float explosionArea = 0f;
    public float maxDelay;

    public Block prev;
    public float powerUse, constructTime = -1;
    public boolean requiresUnlock;

    public float shadowRad = -1f;

    public Missile(String name){
        super(name);

        buildVisibility = BuildVisibility.sandboxOnly;
        category = Category.units;
        researchCostMultiplier = 5f;
        hasShadow = false;
        rebuildable = false;
        drawDisabled = false;
    }

    @Override
    public void init(){
        if(constructTime < 0) constructTime = buildCost;
        if(shadowRad < 0) shadowRad = size * tilesize * 1.5f;
        if(explosionArea < 0) explosionArea = size * tilesize;

        super.init();
    }

    public void drawBase(Tile tile){
        Draw.z(Layer.blockUnder - 1f);
        Drawf.shadow(tile.drawx(), tile.drawy(), shadowRad);
        Draw.z(Layer.block);
        Draw.rect(region, tile.drawx(), tile.drawy());
    }

    public class MissileBuild extends NuclearWarheadBuild{
        @Override
        public void onDestroyed(){
            super.onDestroyed();

            //Kaboom
            explode();
        }

        public void explode(){
            if(explosion != null){
                for(int i = 0; i < explosions; i++){
                    Time.run(Mathf.random(maxDelay), () -> {
                        Tmp.v1.setToRandomDirection().setLength(explosionArea);
                        Bullet b = explosion.create(this, Team.derelict, x + Tmp.v1.x, y + Tmp.v1.y, 0f);
                        b.time = b.lifetime;
                    });
                }
            }
        }
    }
}