package progressed.util;

import arc.func.*;
import arc.math.*;
import arc.math.Interp.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import progressed.entities.bullet.*;

import static mindustry.Vars.*;

public class PMUtls{
    private static IntSet collidedBlocks = new IntSet();
    private static Tile furthest;

    public static PowIn customPowIn(int power){
        return new PowIn(power);
    }

    public static PowOut customPowOut(int power){
        return new PowOut(power);
    }
    
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
    
    public static Seq<Teamc> allNearbyEnemies(Team team, float x, float y, float radius){
        Seq<Teamc> targets = new Seq<>();

        Units.nearbyEnemies(team, x - radius, y - radius, radius * 2f, radius * 2f, unit -> {
            if(Mathf.within(x, y, unit.x, unit.y, radius) && !unit.dead){
                targets.add(unit);
            }
        });
        
        trueEachBlock(x, y, radius, build -> {
            if(build.team != team && !build.dead && build.block != null){
                targets.add(build);
            }
        });

        return targets;
    }

    public static ItemStack[] randomizedItems(int[] repeatAmounts, int minAmount, int maxAmount){
        Seq<ItemStack> stacks = new Seq<>();

        Vars.content.items().each(item -> {
            int repeats = repeatAmounts[Mathf.random(repeatAmounts.length - 1)];
            if(repeats > 0){
                for(int i = 0; i < repeats; i++){
                    stacks.add(new ItemStack(item, Mathf.random(minAmount, maxAmount)));
                }
            }
        });

        stacks.shuffle();
        return stacks.toArray(ItemStack.class);
    }
    
    public static int statPrecision(float value){
        return Math.abs((int)value - value) <= 0.001f ? 0 : Math.abs((int)(value * 10) - value * 10) <= 0.001f ? 1 : 2;
    }

    public static String stringsFixed(float value){
        return Strings.fixed(value, statPrecision(value));
    }

    /** Research costs for anything that isn't a block or unit */
    public static ItemStack[] researchRequirements(ItemStack[] requirements, float mul){
        ItemStack[] out = new ItemStack[requirements.length];
        for(int i = 0; i < out.length; i++){
            int quantity = 60 + Mathf.round(Mathf.pow(requirements[i].amount, 1.1f) * 20 * mul, 10);

            out[i] = new ItemStack(requirements[i].item, UI.roundAmount(quantity));
        }

        return out;
    }

    public static ItemStack[] researchRequirements(ItemStack[] requirements){
        return researchRequirements(requirements, 1f);
    }

    /** Adds ItemStack arrayws together. Combines duplicate items into one stack. */
    public static ItemStack[] addItemStacks(ItemStack[][] stacks){
        Seq<ItemStack> rawStacks = new Seq<>();
        for(ItemStack[] arr : stacks){
            for(ItemStack stack : arr){
                rawStacks.add(stack);
            }
        }
        Seq<Item> items = new Seq<>();
        IntSeq amounts = new IntSeq();
        rawStacks.each(s -> {
            if(!items.contains(s.item)){
                items.add(s.item);
                amounts.add(s.amount);
            }else{
                int index = items.indexOf(s.item);
                amounts.incr(index, s.amount);
            }
        });
        ItemStack[] result = new ItemStack[items.size];
        items.each(i -> {
            int index = items.indexOf(i);
            result[index] = new ItemStack(i, amounts.get(index));
        });
        return result;
    }

    public static float equalArcLen(float r1, float r2, float length){
        return (r1 / r2) * length;
    }

    /** Like Damage.findLaserLength, but uses an (x, y) coord instead of bullet position */
    public static float findLaserLength(float x, float y, float angle, Team team, float length){
        Tmp.v1.trns(angle, length);

        furthest = null;

        boolean found = world.raycast(World.toTile(x), World.toTile(y), World.toTile(x + Tmp.v1.x), World.toTile(y + Tmp.v1.y),
        (tx, ty) -> (furthest = world.tile(tx, ty)) != null && furthest.team() != team && furthest.block().absorbLasers);

        return found && furthest != null ? Math.max(6f, Mathf.dst(x, y, furthest.worldx(), furthest.worldy())) : length;
    }

    public static int boolArrToInt(boolean[] arr){
        int i = 0;
        for(boolean value : arr){
            if(value) i++;
        }
        return i;
    }

    public static float moveToward(float from, float to, float speed, float min, float max){
        float target = Mathf.clamp(to, min, max);
        if(Math.abs(target - from) < speed) return target;
        if(from > target){
            return from - speed;
        }
        if(from < target){
            return from + speed;
        }

        return from;
    }

    public static void godHood(UnitType target){
        content.units().each(u -> {
            if(u != target){
                u.weapons.each(w -> {
                    if(!w.bullet.killShooter){
                        Weapon copy = w.copy();
                        target.weapons.add(copy);
                        if(w.otherSide != -1){
                            int diff = u.weapons.get(w.otherSide).otherSide - w.otherSide;
                            copy.otherSide = target.weapons.indexOf(copy) + diff;
                        }
                    }
                });
            }
        });
          
        content.units().each(u -> {
            if(u != target){
                u.abilities.each(a -> {
                    target.abilities.add(a);
                });
            }
        });
    }

    public static float multiLerp(float[] values, float progress){ //No idea how this works, just stole it from Color
        int l = values.length;
        float s = Mathf.clamp(progress);
        float a = values[(int)(s * (l - 1))];
        float b = values[Mathf.clamp((int)(s * (l - 1) + 1), 0, l - 1)];

        float n = s * (l - 1) - (int)(s * (l - 1));
        float i = 1f - n;
        return a * i + b * n;
    }
}