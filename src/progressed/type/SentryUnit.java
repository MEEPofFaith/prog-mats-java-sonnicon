package progressed.type;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;
import progressed.entities.units.*;

import static mindustry.Vars.*;

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
    public void drawEngine(Unit unit){
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

        Sentry sentry = ((Sentry)unit);
        sentry.duration -= Time.delta;
        sentry.clampDuration();
        if(sentry.duration <= 0f){
            sentry.kill();
        }

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
        ((Sentry)unit).duration = duration;
        return unit;
    }

    @Override
    public void setStats(){
        super.setStats();
        
        stats.add(Stat.health, Core.bundle.format("stat.pm-sentry-lifetime", (int)(duration / 60f)));

        stats.remove(Stat.speed);
        stats.remove(Stat.itemCapacity);
    }

    @Override
    public void display(Unit unit, Table table){
        table.table(t -> {
            t.left();
            t.add(new Image(icon(Cicon.medium))).size(8 * 4).scaling(Scaling.fit);
            t.labelWrap(localizedName).left().width(190f).padLeft(5);
        }).growX().left();
        table.row();

        table.table(bars -> {
            bars.defaults().growX().height(20f).pad(4);

            bars.add(new Bar("stat.health", Pal.health, unit::healthf).blink(Color.white));
            bars.row();
            
            Sentry sentry = ((Sentry)unit);
            bars.add(new Bar(
                () -> Core.bundle.format("bar.pm-sentry-life", Strings.fixed(sentry.duration / 60f, 1)),
                () -> Pal.accent,
                () -> sentry.durationf()
            ));
            bars.row();

            for(Ability ability : unit.abilities){
                ability.displayBars(unit, bars);
            }
        }).growX();

        if(unit.controller() instanceof LogicAI){
            table.row();
            table.add(Blocks.microProcessor.emoji() + " " + Core.bundle.get("units.processorcontrol")).growX().wrap().left();
            table.row();
            table.label(() -> Iconc.settings + " " + (long)unit.flag + "").color(Color.lightGray).growX().wrap().left();
        }
        
        table.row();
    }
}