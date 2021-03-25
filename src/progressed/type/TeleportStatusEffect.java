package progressed.type;

import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import progressed.graphics.*;

public class TeleportStatusEffect extends StatusEffect{
    public float cooldown, teleportRange, rangeScl, teleportDamageScl;
    public Color trailColor = Color.valueOf("FFD59E"); //Phase color default

    public TeleportStatusEffect(String name){
        super(name);
    }

    @Override
    public void update(Unit unit, float time){
        float strength = Mathf.clamp(time / cooldown);
        if(strength > 0f && Mathf.chanceDelta(1f)){
            Tmp.v1.rnd(Mathf.random(0f, (teleportRange + unit.type.hitSize * rangeScl) * strength));
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
}