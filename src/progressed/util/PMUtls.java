package progressed.util;

import arc.func.*;
import arc.math.*;
import arc.math.Interp.*;
import arc.struct.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.world.*;
import progressed.entities.bullet.*;

public class PMUtls{
    private static IntSet collidedBlocks = new IntSet();

    public static final PowIn pow6In = new PowIn(6);
    
    public static float bulletDamage(BulletType b, float lifetime){
        float damage = b.damage + b.splashDamage; //Base Damage

        damage += b.lightningDamage * b.lightning * b.lightningLength; //Lightning Damage

        if(b.fragBullet != null){
            damage += bulletDamage(b.fragBullet, b.fragBullet.lifetime) * b.fragBullets; //Frag Bullet Damage
        }

        if(b instanceof ContinuousLaserBulletType){ //Continuous Damage
            return damage * lifetime / 5f;
        }else if(b instanceof BlackHoleBulletType){
            return damage * lifetime / 2f;
        }else{
            return damage;
        }
    }

    public static void trueEachBlock(float wx, float wy, float range, Cons<Building> cons){
        collidedBlocks.clear();
        int tx = World.toTile(wx);
        int ty = World.toTile(wy);

        int tileRange = Mathf.floorPositive(range / Vars.tilesize);

        for(int x = -tileRange + tx; x <= tileRange + tx; x++){
            for(int y = -tileRange + ty; y <= tileRange + ty; y++){
                if(Mathf.within(x * Vars.tilesize, y * Vars.tilesize, wx, wy, range)){
                    Building other = Vars.world.build(x, y);
                    if(other != null && !collidedBlocks.contains(other.pos())){
                        cons.get(other);
                        collidedBlocks.add(other.pos());
                    }
                }
            }
        }
    }

    public static void trueEachTile(float wx, float wy, float range, Cons<Tile> cons){
        collidedBlocks.clear();
        int tx = World.toTile(wx);
        int ty = World.toTile(wy);

        int tileRange = Mathf.floorPositive(range / Vars.tilesize);

        for(int x = -tileRange + tx; x <= tileRange + tx; x++){
            for(int y = -tileRange + ty; y <= tileRange + ty; y++){
                if(Mathf.within(x * Vars.tilesize, y * Vars.tilesize, wx, wy, range)){
                    Tile other = Vars.world.tile(x, y);
                    if(other != null && !collidedBlocks.contains(other.pos())){
                        cons.get(other);
                        collidedBlocks.add(other.pos());
                    }
                }
            }
        }
    }
}
