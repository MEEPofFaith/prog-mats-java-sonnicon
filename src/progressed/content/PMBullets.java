package progressed.content;

import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import progressed.entities.bullet.*;

public class PMBullets implements ContentList{
    public static BulletType
    standardCopperMini, standardDenseMini, standardHomingMini, standardIncendiaryMini, standardThoriumMini, standardExplosiveMini,

    sniperBoltThorium,

    pixel,
    
    basicSentryLaunch, strikeSentryLaunch, dashSentryLaunch,
    
    blackHole, cataclysm, absorbed;

    @Override
    public void load(){
        standardCopperMini = new BasicBulletType(2.5f, 21f){{
            width = height = 3f;
            lifetime = 90f;
        }};

        standardDenseMini = new BasicBulletType(3.5f, 28f){{
            width = height = 3.5f;
            reloadMultiplier = 0.6f;
            ammoMultiplier = 4;
            pierce = true;
            pierceCap = 2;
            lifetime = 90f;
        }};

        standardHomingMini = new BasicBulletType(3f, 23f){{
            width = height = 2.5f;
            homingPower = 0.07f;
            reloadMultiplier = 1.4f;
            ammoMultiplier = 5f;
            lifetime = 90f;
        }};

        standardIncendiaryMini = new BasicBulletType(3.2f, 25f){{
            width = height = 3f;
            frontColor = Pal.lightishOrange;
            backColor = Pal.lightOrange;
            inaccuracy = 5f;
            makeFire = true;
            lifetime = 90f;
        }};

        standardThoriumMini = new BasicBulletType(4f, 46f){{
            width = height = 3f;
            shootEffect = Fx.shootBig;
            smokeEffect = Fx.shootBigSmoke;
            ammoMultiplier = 4f;
            pierce = true;
            pierceCap = 3;
            lifetime = 90f;
        }};

        standardExplosiveMini = new BasicBulletType(3.5f, 24f){{
            width = height = 3f;
            shootEffect = Fx.shootBig;
            smokeEffect = Fx.shootBigSmoke;
            hitEffect = despawnEffect = Fx.blastExplosion;
            splashDamage = 34f;
            splashDamageRadius = 12f;
            inaccuracy = 6f;
            ammoMultiplier = 3;
            lifetime = 90f;
        }};

        sniperBoltThorium = new CritBulletType(20f, 800f){{
            lifetime = 30f;
            knockback = 50f;
            height = 12f;
        }};

        pixel = new BitBulletType(2f, 5f){{
            lifetime = 90f;
            splashDamage = 27f;
            splashDamageRadius = 40f;
            hitSize = size = 8f;
            homingPower = 0.01f;
            homingRange = 160f;
            knockback = 3f;
            weaveScale = 10f;
            weaveMag = 2f;
            trailDelay = 7.5f;
        }};

        basicSentryLaunch = new UnitSpawnBulletType(2f, PMUnitTypes.basicSentry);

        strikeSentryLaunch = new UnitSpawnBulletType(2f, PMUnitTypes.strikeSentry);

        dashSentryLaunch = new UnitSpawnBulletType(2f, PMUnitTypes.dashSentry);

        blackHole = new BlackHoleBulletType(0.5f, 575f / 30f){{
            lifetime = 420f;
            backMove = false;
            lightRadius = 8f;
            lightOpacity = 0.7f;
        }};

        cataclysm = new BlackHoleCataclysmType();

        absorbed = new BulletType(0f, 0f){
            @Override
            public void despawned(Bullet b){
                //Do nothing
            }

            @Override
            public void hit(Bullet b, float x, float y){
                //Do nothing
            }

            @Override
            public void hitTile(Bullet b, Building build, float initialHealth, boolean direct){
                //do nothing
            }

            @Override
            public void update(Bullet b){
                //Do nothing
            }
        };
    }
}