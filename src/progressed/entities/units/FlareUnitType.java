package progressed.entities.units;

import arc.*;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import progressed.ai.*;
import progressed.graphics.*;
import progressed.util.*;

public class FlareUnitType extends UnitType{
    public Effect flareEffect = PMFx.flare;
    public float flareX, flareY;
    public float flareEffectChance = 0.5f, flareEffectSize = 1f;
    public float duration;
    public float shadowSize = -1f;
    public float attraction;

    public FlareUnitType(String name, float duration){
        super(name);
        this.duration = duration;
        defaultController = EmptyAI::new;

        drag = 1f;
        speed = accel = 0f;
        isCounted = false;
        canDrown = false;
        flying = false;
        commandLimit = 0;
        fallSpeed = 1f / 30f;
        hitSize = 1f;
    }

    public FlareUnitType(String name){
        this(name, 300f);
    }

    @Override
    public void init(){
        super.init();

        if(shadowSize < 0f) shadowSize = hitSize * 2f;
    }

    @Override
    public void update(Unit unit){
        FlareUnitEntity flare = (FlareUnitEntity)unit;
        flare.duration -= Time.delta;
        flare.clampDuration();

        if(Mathf.chanceDelta(flareEffectChance) && flareEffect != Fx.none && !(unit.dead || unit.health < 0f)){
            flareEffect.at(
                unit.x + flareX,
                unit.y + flareY,
                flareEffectSize,
                unit.team.color
            );
        }
    }

    @Override
    public void drawShadow(Unit unit){
        // don't draw
    }

    @Override
    public void drawSoftShadow(Unit unit){
        Draw.color(0, 0, 0, 0.4f * ((FlareUnitEntity)unit).animation);
        float rad = 1.6f;
        float size = shadowSize * Draw.scl * 16f;
        Draw.rect(softShadowRegion, unit, size * rad, size * rad);
        Draw.color();
    }

    @Override
    public void drawOutline(Unit unit){
        Draw.reset();

        if(Core.atlas.isFound(outlineRegion)){
            Draw.alpha(((FlareUnitEntity)unit).animation);
            Draw.rect(outlineRegion, unit.x, unit.y, unit.rotation - 90f);
            Draw.reset();
        }
    }

    @Override
    public void drawBody(Unit unit){
        applyColor(unit);

        Draw.alpha(((FlareUnitEntity)unit).animation);
        Draw.rect(region, unit.x, unit.y, unit.rotation - 90f);
        Draw.reset();
    }

    @Override
    public Color cellColor(Unit unit){
        return super.cellColor(unit).mul(1f, 1f, 1f, ((FlareUnitEntity)unit).animation);
    }

    @Override
    public Unit create(Team team){
        Unit unit = constructor.get();
        unit.team = team;
        unit.setType(this);
        unit.health = health;
        unit.maxHealth = attraction;
        unit.rotation = 90f;
        ((FlareUnitEntity)unit).duration = duration;
        return unit;
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

            FlareUnitEntity flare = ((FlareUnitEntity)unit);
            bars.add(new Bar(
                () -> Core.bundle.format("bar.pm-lifetime", PMUtls.stringsFixed(flare.durationf() * 100f)),
                () -> Pal.accent,
                flare::durationf
            ));
            bars.row();
        }).growX();
    }
    
    @Override
    public boolean isHidden(){
        return true;
    }
}