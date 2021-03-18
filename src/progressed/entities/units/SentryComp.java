package progressed.entities.units;

import mindustry.gen.*;

public interface SentryComp extends Unitc{
    @Override
    default boolean damaged(){
        return false; //Never view as damaged, healing will not target this.
    }

    @Override
    default void heal(){
        //Do nothing
    }

    @Override
    default void heal(float amount){
        //Do nothing
    }

    @Override
    default void healFract(float amount){
        //Do nothing
    }

    @Override
    default int cap(){
        return count() + 1;
    }

    @Override
    default float prefRotation(){
        return rotation();
    }
}