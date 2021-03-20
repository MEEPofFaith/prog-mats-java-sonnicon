package progressed.world.units;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.*;

public class SentryUnit extends UnitType{
    public int engines = 4;
    public float engineRotOffset = 45f, duration = 600f, riseSpeed = 0.016f;

    public SentryUnit(String name){
        super(name);
        speed = accel = 0f;
        drag = 0.025f;
        flying = lowAltitude = true;
        engineOffset = 6f;
        isCounted = false;
        itemCapacity = 10;
        health = 200;
        commandLimit = 2;
    }

    @Override
    public void drawEngine(Unit unit) {
        if(!unit.isFlying()) return;

        float scl = unit.elevation;
        float offset = engineOffset * scl;

        Draw.color(unit.team.color);
        for(int i = 0; i < engines; i++){
            Fill.circle(
                unit.x + Angles.trnsx(unit.rotation + engineRotOffset + (i * 360f / engines), offset),
                unit.y + Angles.trnsy(unit.rotation + engineRotOffset + (i * 360f / engines), offset),
                (engineSize + Mathf.absin(2, engineSize / 4f)) * scl
            );
        }

        Draw.color(Color.white);
        for(int i = 0; i < engines; i++){
            Fill.circle(
                unit.x + Angles.trnsx(unit.rotation + engineRotOffset + (i * 360f / engines), offset - 1f),
                unit.y + Angles.trnsy(unit.rotation + engineRotOffset + (i * 360f / engines), offset - 1f),
                (engineSize + Mathf.absin(2, engineSize / 4f)) / 2f * scl
            );
        }

        Draw.reset();
    }

    @Override
    public void update(Unit unit){
        if(!unit.dead && unit.health > 0) unit.elevation = Mathf.clamp(unit.elevation + riseSpeed * Time.delta);

        float sub = (unit.maxHealth / duration) * Time.delta;
        unit.health -= sub;

        super.update(unit);
    }

    @Override
    public Unit create(Team team){
        Unit unit = constructor.get();
        unit.team = team;
        unit.setType(this);
        unit.ammo = ammoCapacity; //fill up on ammo upon creation
        unit.elevation = 0;
        unit.health = unit.maxHealth;
        return unit;
    }

    @Override
    public void setStats(){
        super.setStats();
        
        stats.add(Stat.health, table -> {
            table.table(t -> {
                float durSec = duration / 60f;
                String val = durSec + " " + StatUnit.seconds.localized();
                t.add("(" + Core.bundle.get("stat.prog-mats.sentry-lifetime") + ": " + val + ")");
            });
        });

        stats.remove(Stat.speed);
        stats.remove(Stat.itemCapacity);
    }
}