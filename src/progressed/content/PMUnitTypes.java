package progressed.content;

import arc.func.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import progressed.ai.*;
import progressed.entities.units.*;
import progressed.world.units.*;

public class PMUnitTypes implements ContentList{
    //Steal from Project Unity
    private static final Prov<?>[] constructors = new Prov[]{
        SentryBase::new
    };

    //Steal from Project Unity
    private static final int[] classIDs = new int[constructors.length];

    //sentry
    public static UnitType basicSentry;

    //Steal from Project Unity
    public static int getClassId(int index){
        return classIDs[index];
    }

    //Steal from Project Unity
    private static void setEntity(String name, Prov<?> c){
        EntityMapping.nameMap.put(name, c);
    }

    @Override
    public void load(){
        //Steal from Project Unity
        for(int i = 0, j = 0, len = EntityMapping.idMap.length; i < len; i++){
            if(EntityMapping.idMap[i] == null){
                classIDs[j] = i;
                EntityMapping.idMap[i] = constructors[j++];

                if(j >= constructors.length) break;
            }
        }

        //Region Sentry Units
        setEntity("basic-sentry", SentryBase::new);
        basicSentry = new SentryUnit("basic-sentry"){{
            defaultController = SentryAI::new;

            duration = 16f * 60f;

            weapons.add(new Weapon("large-weapon"){{
                top = false;
                rotate = false;
                alternate = true;

                x = 4f;
                y = 2.25f;
                shootX = -0.625f;

                reload = 6f;
                recoil = 1.75f;
                ejectEffect = Fx.casing1;

                bullet = new BasicBulletType(3f, 20f){{
                    width = 7f;
                    height = 9f;
                    homingPower = 0.03f;
                    homingRange = 120f;
                    lifetime = 80f;
                }};
            }});
        }};
    }
}
