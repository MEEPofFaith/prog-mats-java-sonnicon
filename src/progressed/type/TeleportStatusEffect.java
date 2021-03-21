package progressed.type;

import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import progressed.graphics.*;

public class TeleportStatusEffect extends StatusEffect implements Timerc{
    public float cooldown, teleportRange, teleportDamageScl;
    public Color trailColor;

    public TeleportStatusEffect(String name){
        super(name);
    }

    @Override
    public void update(Unit unit, float time){
        float strength = Mathf.clamp(time / cooldown);
        if(strength > 0f && timer(1, 1f)){
            Tmp.v1.rnd(Mathf.random(0f, teleportRange * strength));
            Tmp.v1.add(unit.x, unit.y);

            PMFx.teleportEffect.at(unit.x, unit.y, 0f, trailColor, new float[]{unit.type.hitSize / 4f, Tmp.v1.x, Tmp.v1.y});
            unit.set(Tmp.v1.x, Tmp.v1.y);
            unit.rotation += Mathf.range(180f * strength);

            float hit = Mathf.clamp(unit.type.hitSize * teleportDamageScl * strength, 0.1f, unit.maxHealth);
            unit.damagePierce(hit);
            unit.maxHealth -= hit;
            if(unit.maxHealth < 0f || unit.health < 0f){
                unit.killed();
            }
            
            for(WeaponMount mount : unit.mounts){
                Weapon weapon = mount.weapon;
                if(weapon.rotate){
                    mount.rotation += Mathf.range(180f * strength);
                    mount.reload = Mathf.random(0f, weapon.reload);
                }
            }
        }

        super.update(unit, time);
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
