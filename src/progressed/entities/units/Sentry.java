package progressed.entities.units;

import arc.math.*;
import mindustry.gen.*;
import progressed.content.*;
import progressed.world.units.*;

public class Sentry extends UnitEntity{
    public float duration;

    public float durationf(){
        return duration / ((SentryUnit)type).duration;
    }

    public void clampDuration(){
        duration = Mathf.clamp(duration, 0f, ((SentryUnit)type).duration);
    }

    @Override
    public boolean damaged(){
        return false; //Never view as damaged, healing will not target this.
    }

    @Override
    public void heal(){
        //Do nothing
    }

    @Override
    public void heal(float amount){
        //Do nothing
    }

    @Override
    public void healFract(float amount){
        //Do nothing
    }

    @Override
    public int cap(){
        return count() + 5;
    }

    @Override
    public float prefRotation(){
        return rotation();
    }

    @Override
    public int classId(){
        return PMUnitTypes.getClassId(0);
    }
}