package progressed.content;

import arc.func.*;
import arc.graphics.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import progressed.ai.*;
import progressed.entities.bullet.*;
import progressed.entities.units.*;
import progressed.graphics.PMFx;
import progressed.world.units.*;

public class PMUnitTypes implements ContentList{
    //Steal from Project Unity
    private static final Prov<?>[] constructors = new Prov[]{
        Sentry::new
    };

    //Steal from Project Unity
    private static final int[] classIDs = new int[constructors.length];

    //sentry
    public static UnitType basicSentry, strikeSentry, dashSentry;

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
        setEntity("basic-sentry", Sentry::new);
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

        setEntity("strike-sentry", Sentry::new);
        strikeSentry = new SentryUnit("strike-sentry"){{
            health = 150f;

            weapons.add(new Weapon(name + "-hole"){{
                rotate = mirror = alternate = top = false;
                x = y = recoil = shootY = 0f;
                reload = 40f;
                shootCone = 360f;
                inaccuracy = 15f;
                shootSound = Sounds.missile;
                bullet = new StrikeBulletType(2.4f, 40f, "prog-mats-storm-missile"){{
                    lifetime = 90f;
                    
                    splashDamage = 250f;
                    splashDamageRadius = 42f;
                    homingPower = 0.035f;
                    homingRange = 200f;

                    hitSound = Sounds.explosion;
                    hitShake = 1.5f;

                    trailParam = 3f;
                    trailEffect = PMFx.missileTrailSmall;

                    despawnEffect = PMFx.missileBoom;

                    autoDropRadius = 12f;
                    stopRadius = 8f;

                    riseEngineSize = fallEngineSize = 5f;

                    trailSize = 0.2f;
                    targetRadius = 0.5f;
                }};
            }});
        }};

        setEntity("dash-sentry", Sentry::new);
        dashSentry = new SentryUnit("dash-sentry"){
            float len = 56f;
            {
                health = 450f;
                rotateSpeed = 30f;
                range = len * 6f;
                duration = 12f * 60f;
                itemCapacity = 15;

                weapons.add(new Weapon(name + "-laser"){{
                    top = true;
                    rotate = true;
                    mirror = false;
                    rotateSpeed = 60f;
                    reload = 20f;
                    x = 0f;
                    y = -2f;
                    shootY = 4.25f;
                    shootCone = 2;
                    shootSound = Sounds.laser;
                    bullet = new LaserBulletType(90f){
                        {
                            length = len;
                            recoil = -10f;
                            colors = new Color[]{Pal.surge.cpy().a(0.4f), Pal.surge, Color.white};
                        }

                        @Override
                        public float range(){
                            return length * 6f;
                        };
                    };
                }});
            }
        };
    }
}