package progressed.entities.units;

import mindustry.gen.*;
import progressed.content.*;

public class SentryBase extends UnitEntity implements SentryComp{
    @Override
    public int classId(){
        return PMUnitTypes.getClassId(0);
    }
}