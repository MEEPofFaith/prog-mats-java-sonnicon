package progressed.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import progressed.entities.bullet.*;
import progressed.entities.units.*;
import progressed.graphics.*;

public class PMUnitTypes implements ContentList{
    public static UnitType
    
    //sentry
    barrage, downpour, rapier,

    //signal flare
    flareSmall, flareMedium, flareLarge,
    
    //sandy
    everythingUnit;

    @Override
    public void load(){
        //Region Sentry Units
        barrage = new SentryUnitType("barrage"){{
            health = 500f;
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

        downpour = new SentryUnitType("downpour"){{
            health = 300f;

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

                    targetColor = PMPal.missileBasic;

                    despawnEffect = PMFx.missileBoom;
                    blockEffect = PMFx.missileBlockedSmall;

                    autoDropRadius = 12f;
                    stopRadius = 8f;

                    riseEngineSize = fallEngineSize = 5f;

                    trailSize = 0.2f;
                    targetRadius = 0.5f;
                }};
            }});
        }};

        rapier = new SentryUnitType("rapier"){
            final float len = 56f, rangeMul = 16f;
            {
                health = 800f;
                duration = 12f * 60f;

                rotateSpeed = 30f;
                range = len * rangeMul;
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
                            return length * rangeMul;
                        }
                    };
                }});
            }
        };

        flareSmall = new FlareUnitType("small-flare"){{
            health = 300f;
            attraction = 800f;
            flareY = 29f / 4f;
        }};

        flareMedium = new FlareUnitType("medium-flare", 360f){{
            health = 900f;
            attraction = 11000f;
            flareY = 45f / 4f;
            flareEffectSize = 1.5f;
        }};

        flareLarge = new FlareUnitType("large-flare", 420f){{
            health = 2700f;
            attraction = 26000f;
            flareY = 61f / 4f;
            flareEffectSize = 2f;
        }};

        everythingUnit = new UnitType("god"){
            {
                constructor = UnitEntity::create;
                alwaysUnlocked = true;
                flying = true;
                lowAltitude = true;
                mineSpeed = 100f;
                mineTier = 10000;
                buildSpeed = 10000f;
                drag = 0.05f;
                speed = 3.55f;
                rotateSpeed = 19f;
                accel = 0.11f;
                itemCapacity = 20000;
                health = 200000f;
                engineOffset = 4f;
                engineSize = 1f;
                hitSize = 11f;
                commandLimit = 100;
            }

            @Override
            public void setStats(){
                super.setStats();

                stats.add(Stat.abilities, "Everything");
                stats.add(Stat.weapons, "Everything");
            }
        };
    }
}