package progressed.type;

import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;

public class ParalizeStatusEffect extends StatusEffect implements Timerc{
    public float cooldown, rotationRand;
    public Color effectColor = Pal.lancerLaser;

    public ParalizeStatusEffect(String name){
        super(name);
        color = Pal.lancerLaser;
    }

    @Override
    public void update(Unit unit, float time){
        float strength = Mathf.clamp(time / cooldown);
        if(strength > 0f && timer(1, 1f)){
            for(WeaponMount mount : unit.mounts){
                Weapon weapon = mount.weapon;
                if(weapon.rotate){
                    mount.rotation += Mathf.range(weapon.rotateSpeed * rotationRand * strength);
                }
            }
        }
        
        if(damage > 0f){
            unit.damageContinuousPierce(damage);
        }else if(damage < 0f){ //heal unit
            unit.heal(-1f * damage * Time.delta);
        }

        if(effect != Fx.none && Mathf.chanceDelta(effectChance)){
            Tmp.v1.rnd(unit.type.hitSize /2f);
            effect.at(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, effectColor);
        }
    }

    @Override
    public boolean isAdded() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void remove() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void add() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isLocal() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRemote() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isNull() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T extends Entityc> T self() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T as() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T with(Cons<T> cons) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int classId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean serialize() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void read(Reads read) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void write(Writes write) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterRead() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int id() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void id(int id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean timer(int index, float time) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Interval timer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void timer(Interval timer) {
        // TODO Auto-generated method stub
        
    }
}
