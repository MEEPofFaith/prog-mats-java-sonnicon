package progressed.content;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.Effect;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import progressed.entities.bullet.*;
import progressed.graphics.*;

public class PMBullets implements ContentList{
    public static BulletType
    flameMagma, blazeMagma, infernoMagma,

    standardCopperMini, standardDenseMini, standardHomingMini, standardIncendiaryMini, standardThoriumMini,

    shockZap, sparkZap, stormZap,

    sniperBoltSilicon, sniperBoltTitanium, sniperBoltThorium, sniperBoltSurge, sniperBoltTechtaniteFrag, sniperBoltTechtanite,

    pixel,
    
    basicSentryLaunch, strikeSentryLaunch, dashSentryLaunch,

    smallFlare, mediumFlare, largeFlare,
    
    blackHole, cataclysm, absorbed,
    
    empParticle, quantumParticle, empParticleStrong,
    
    firestormMissile,

    recursionTwo, recursionOne,
    
    strikedownBasic, strikedownEmp, strikedownQuantum, strikedownRecursive,
    
    basicSentryDrop, strikeSentryDrop, dashSentryDrop,
    
    arbiterBasic, arbiterEmp, arbiterClusterFrag, arbiterCluster, arbiterSentry,
    
    harbingerLaser, excaliburLaser;

    @Override
    public void load(){
        flameMagma = new MagmaBulletType(50f, 18f){{
            shake = 1f;
        }};

        blazeMagma = new MagmaBulletType(75f, 36f){{
            shake = 2f;
        }};

        infernoMagma = new MagmaBulletType(62.5f, 13f){{
            shake = 0.1f;
        }};

        standardCopperMini = new BasicBulletType(2.5f, 5f){{
            width = 5f;
            height = 7f;
            lifetime = 110f;
        }};

        standardDenseMini = new BasicBulletType(3.5f, 10f){{
            width = 5.5f;
            height = 9f;
            reloadMultiplier = 0.6f;
            ammoMultiplier = 4;
            lifetime = 76f;
        }};

        standardHomingMini = new BasicBulletType(3f, 6f){{
            width = 4f;
            height = 6f;
            homingPower = 0.07f;
            reloadMultiplier = 1.3f;
            ammoMultiplier = 5f;
            lifetime = 92f;
        }};

        standardIncendiaryMini = new BasicBulletType(3.2f, 7f){{
            width = 5f;
            height = 8f;
            frontColor = Pal.lightishOrange;
            backColor = Pal.lightOrange;
            inaccuracy = 5f;
            makeFire = true;
            lifetime = 86f;
        }};

        standardThoriumMini = new BasicBulletType(4f, 13f){{
            width = 6f;
            height = 11f;
            shootEffect = Fx.shootBig;
            smokeEffect = Fx.shootBigSmoke;
            ammoMultiplier = 4f;
            lifetime = 66f;
        }};

        shockZap = new LightningBulletType(){{
            damage = 8f;
            lightningLength = 5;
            lightningLengthRand = 3;
            lightningAngle = 0f;
            lightningColor = Pal.surge;
            lightRadius = 24f;
            lightOpacity = 0.7f;
            backMove = false;
        }};

        sparkZap = new LightningBulletType(){{
            damage = 12f;
            lightningLength = 6;
            lightningLengthRand = 4;
            lightningAngle = 0f;
            lightningColor = Pal.surge;
            lightRadius = 24f;
            lightOpacity = 0.7f;
            backMove = false;
        }};

        stormZap = new LightningBulletType(){{
            damage = 15f;
            lightningLength = 7;
            lightningLengthRand = 5;
            lightningAngle = 0f;
            lightningColor = Pal.surge;
            lightRadius = 24f;
            lightOpacity = 0.7f;
            backMove = false;
        }};

        sniperBoltSilicon = new CritBulletType(12f, 300f){{
            lifetime = 48f;
            knockback = 4f;
            width = 5f;
            height = 8f;
            pierceCap = 20;
            reloadMultiplier = 1.2f;
            critChance = 0.2f;
            critMultiplier = 2f;
            bouncing = true;
        }};

        sniperBoltTitanium = new CritBulletType(13f, 500f){{
            lifetime = 45f;
            knockback = 5f;
            width = 7f;
            height = 12f;
            pierceCap = 26;
            reloadMultiplier = 1.7f;
            critChance = 0.15f;
            critMultiplier = 2.5f;
        }};

        sniperBoltThorium = new CritBulletType(12f, 800f){{
            lifetime = 51f;
            knockback = 5f;
            width = 8f;
            height = 14f;
            pierceCap = 30;
            critChance = 0.1f;
            critMultiplier = 4.5f;
        }};

        sniperBoltSurge = new CritBulletType(14f, 1000f){{
            frontColor = Color.valueOf("F3E979");
            backColor = hitColor = Color.valueOf("D99F6B");
            lifetime = 42f;
            knockback = 5f;
            width = 10f;
            height = 19f;
            trailLength = 11;
            pierceCap = 40;
            knockback = 7f;
            reloadMultiplier = 0.7f;
            lightning = 5;
            lightningLength = 3;
            lightningLengthRand = 2;
            lightningDamage = 40f;
            critChance = 0.20f;
            critMultiplier = 5f;
        }};

        sniperBoltTechtaniteFrag = new CritBulletType(13f, 160f){{
            lifetime = 23f;
            knockback = 3f;
            width = 6f;
            height = 14f;
            pierceCap = 12;
            critMultiplier = 3f;
            critEffect = PMFx.sniperCritMini;
        }};

        sniperBoltTechtanite = new CritBulletType(13f, 800f){
            float fragInacc = 5f;

            {
                lifetime = 23f;
                knockback = 4f;
                width = 9f;
                height = 16f;
                pierceCap = 13;
                fragBullets = 5;
                fragBullet = sniperBoltTechtaniteFrag;
                fragVelocityMin = 0.8f;
                fragVelocityMax = 1.2f;
                fragCone = 30f;
                critChance = 0.1f;
                critMultiplier = 3f;
            }

            @Override
            public void hit(Bullet b, float x, float y){
                b.hit = true;
                if(!((CritBulletData)b.data).despawned){
                    hitEffect.at(x, y, b.rotation(), hitColor);
                    hitSound.at(x, y, hitSoundPitch, hitSoundVolume);
                }

                Effect.shake(hitShake, hitShake, b);
            }

            @Override
            public void despawned(Bullet b){ //Only frag on despawn
                CritBulletData data = (CritBulletData)b.data;
                for(int i = 0; i < fragBullets; i++){
                    float a = b.rotation() + ((fragCone / fragBullets) * (i - (fragBullets - 1f) / 2f)) + Mathf.range(fragInacc);
                    if(fragBullet instanceof CritBulletType critB){
                        Bullet bullet = critB.create(b.owner, b.team, b.x, b.y, a, -1f, Mathf.random(fragVelocityMin, fragVelocityMax), 1f, new CritBulletData(data.crit, data.trail.copy()));
                        if(b.collided.size > 0) bullet.collided.add(b.collided.peek());
                    }
                }

                super.despawned(b);
            }
        };

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

        smallFlare = new SignalFlareBulletType(8f, 60f, PMUnitTypes.flareSmall){{
            size = 4f;
            spinSpeed = 3f;
        }};

        mediumFlare = new SignalFlareBulletType(7f, 70f, PMUnitTypes.flareMedium){{
            size = 8f;
            spinSpeed = 5f;
        }};

        largeFlare = new SignalFlareBulletType(6f, 80f, PMUnitTypes.flareLarge){{
            size = 12f;
            spinSpeed = 8f;
        }};

        blackHole = new BlackHoleBulletType(0.5f, 1400f / 30f){{
            lifetime = 630f;
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

        empParticle = new ParticleBulletType(){{
            particleSound = Sounds.spark;
            hitColor = trailColor = Pal.lancerLaser;
            status = PMStatusEffects.emp;
            statusDuration = 60f * 10f;
        }};

        quantumParticle = new ParticleBulletType(3f, 0f){{
            lifetime = 40f;
            particleSound = Sounds.sap;
            particleSoundChance = 0.25f;
            hitColor = trailColor = Color.valueOf("EFE4CA");
            status = PMStatusEffects.teleportation;
            statusDuration = 60f * 10f;
        }};

        empParticleStrong = new ParticleBulletType(12f, 0f){{
            lifetime = 48f;
            particleSound = Sounds.spark;
            particleSoundChance = 0.4f;
            particleSoundMinPitch = 0.4f;
            particleSoundMaxPitch = 0.7f;
            hitColor = trailColor = Pal.lancerLaser;
            status = PMStatusEffects.empStrong;
            statusDuration = 60f * 12f;
        }};
        
        firestormMissile = new StrikeBulletType(2.4f, 28f, "prog-mats-storm-missile"){{
            splashDamage = 72f;
            splashDamageRadius = 30f;
            lifetime = 90f;
            homingPower = 0.035f;
            homingRange = 200f;
            ammoMultiplier = 4f;
            hitSound = Sounds.explosion;
            collidesAir = false;
            hitShake = 2f;
            despawnEffect = PMFx.missileBoom;
            
            trailParam = 3f;
            trailEffect = Fx.missileTrail;

            elevation = 150f;
            riseTime = 30f;
            fallTime = 20f;
            weaveWidth = 12f;
            weaveSpeed = 0.3f;

            autoDropRadius = 0f;
            stopRadius = 8f;
            resumeSeek = false;
            riseEngineSize = fallEngineSize = 5f;
            trailSize = 0.2f;
            targetRadius = 0.5f;
        }};

        strikedownBasic = new StrikeBulletType(2f, 80f, "prog-mats-strikedown-basic"){{
            splashDamage = 750f;
            splashDamageRadius = 64f;
            homingPower = 0.05f;
            homingRange = 330f;
            lifetime = 180f;
            hitSound = Sounds.bang;
            hitShake = 5f;
            despawnEffect = PMFx.nuclearExplosion;

            trailParam = 5f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailMedium;

            autoDropRadius = 15f;
            stopRadius = 10f;
            riseEngineSize = 16f;
            fallEngineSize = 8f;
            elevation = 300f;
            riseTime = 45f;
            fallTime = 25f;
            trailSize = 0.7f;
            riseSpin = 300f;
            fallSpin = 135f;

            unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;
        }};

        strikedownEmp = new StrikeBulletType(3f, 80f, "prog-mats-strikedown-emp"){{
            splashDamage = 235f;
            splashDamageRadius = 48f;
            reloadMultiplier = 0.75f;
            homingPower = 0.075f;
            homingRange = 330f;
            lifetime = 120f;
            hitSound = Sounds.bang;
            hitShake = 5f;
            despawnEffect = PMFx.nuclearExplosion;

            fragBullets = 360;
            fragVelocityMin = 0.5f;
            fragBullet = empParticle;

            trailParam = 5f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailMedium;

            autoDropRadius = 35f;
            stopRadius = 10f;
            riseEngineSize = 16f;
            fallEngineSize = 8f;
            elevation = 300f;
            riseTime = 35f;
            fallTime = 15f;
            trailSize = 0.7f;
            riseSpin = 270f;
            fallSpin = 90f;
        }};

        strikedownQuantum = new StrikeBulletType(1.8f, 80f, "prog-mats-strikedown-quantum"){{
            splashDamage = 160f;
            splashDamageRadius = 48f;
            reloadMultiplier = 0.5f;
            homingPower = 0.075f;
            homingRange = 330f;
            lifetime = 200f;
            hitSound = Sounds.bang;
            hitShake = 5f;
            despawnEffect = PMFx.nuclearExplosion;

            fragBullets = 360;
            fragVelocityMin = 0.5f;
            fragBullet = quantumParticle;

            trailParam = 5f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailMedium;

            autoDropRadius = 25f;
            stopRadius = 10f;
            riseEngineSize = 16f;
            fallEngineSize = 8f;
            elevation = 300f;
            riseTime = 30f;
            fallTime = 25f;
            trailSize = 0.7f;
            riseSpin = 270f;
            fallSpin = 90f;
        }};

        recursionTwo = new StrikeBulletType(4f, 80f, "prog-mats-arbiter-cluster-frag"){{
            splashDamage = 50f;
            splashDamageRadius = 32f;
            homingPower = 0.1f;
            homingRange = 330f;
            lifetime = 70f;
            hitSound = Sounds.bang;
            hitShake = 5f;
            despawnEffect = PMFx.nuclearExplosion;

            trailParam = 5f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailMedium;

            autoDropRadius = 15f;
            stopRadius = 10f;
            riseEngineSize = 16f;
            fallEngineSize = 8f;
            elevation = 230f;
            riseTime = 25f;
            fallTime = 15f;
            trailSize = 0.7f;
            riseSpin = 190f;
            fallSpin = 110f;

            unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;
        }};

        recursionOne = new StrikeBulletType(3f, 80f, "prog-mats-arbiter-cluster-frag"){{
            splashDamage = 100f;
            splashDamageRadius = 40f;
            homingPower = 0.07f;
            homingRange = 330f;
            lifetime = 90f;
            hitSound = Sounds.bang;
            hitShake = 5f;
            despawnEffect = PMFx.nuclearExplosion;

            trailParam = 5f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailMedium;

            autoDropRadius = 15f;
            stopRadius = 10f;
            riseEngineSize = 16f;
            fallEngineSize = 8f;
            elevation = 260f;
            riseTime = 35f;
            fallTime = 20f;
            trailSize = 0.7f;
            riseSpin = 230f;
            fallSpin = 120f;

            unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;

            fragBullets = 3;
            fragVelocityMin = 0.8f;
            fragVelocityMax = 1.2f;
            fragBullet = recursionTwo;
        }};

        strikedownRecursive = new StrikeBulletType(2f, 80f, "prog-mats-arbiter-cluster-frag"){{
            splashDamage = 200f;
            splashDamageRadius = 48f;
            homingPower = 0.05f;
            homingRange = 330f;
            lifetime = 135f;
            hitSound = Sounds.bang;
            hitShake = 5f;
            despawnEffect = PMFx.nuclearExplosion;

            trailParam = 5f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailMedium;

            autoDropRadius = 15f;
            stopRadius = 10f;
            riseEngineSize = 16f;
            fallEngineSize = 8f;
            elevation = 300f;
            riseTime = 45f;
            fallTime = 25f;
            trailSize = 0.7f;
            riseSpin = 300f;
            fallSpin = 135f;

            unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;

            fragBullets = 3;
            fragVelocityMin = 0.8f;
            fragVelocityMax = 1.2f;
            fragBullet = recursionOne;
        }};

        arbiterBasic = new StrikeBulletType(1f, 300f, "prog-mats-arbiter-basic"){{
            splashDamage = 27000f;
            splashDamageRadius = 240f;
            homingPower = 0.05f;
            homingRange = 2200f;
            lifetime = 5500f;
            hitSound = Sounds.bang;
            hitShake = 30f;
            despawnEffect = PMFx.mushroomCloudExplosion;

            trailParam = 8f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailLarge;

            autoDropRadius = 30f;
            stopRadius = 20f;
            riseEngineSize = 24f;
            fallEngineSize = 14f;
            trailSize = 0.7f;
            elevation = 900f;
            riseTime = 240f;
            fallTime = 90f;
            trailSize = 2f;
            riseSpin = 720f;
            fallSpin = 180f;
            targetRadius = 2f;

            unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;
        }};

        arbiterEmp = new StrikeBulletType(2f, 300f, "prog-mats-arbiter-emp"){{
            splashDamage = 12000f;
            splashDamageRadius = 170f;
            reloadMultiplier = 0.75f;
            homingPower = 0.075f;
            homingRange = 2200f;
            lifetime = 2250f;
            hitSound = Sounds.bang;
            hitShake = 30f;
            despawnEffect = PMFx.mushroomCloudExplosion;

            fragBullets = 360;
            fragBullet = empParticleStrong;
            fragVelocityMin = 0.5f;

            trailParam = 8f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailLarge;

            autoDropRadius = 60f;
            stopRadius = 30f;
            riseEngineSize = 24f;
            fallEngineSize = 14f;
            trailSize = 0.7f;
            elevation = 900f;
            riseTime = 180f;
            fallTime = 70f;
            trailSize = 2f;
            riseSpin = 720f;
            fallSpin = 180f;
            targetRadius = 2f;
        }};

        arbiterClusterFrag = new StrikeBulletType(1f, 80f, "prog-mats-arbiter-cluster-frag"){{
            splashDamage = 3000f;
            splashDamageRadius = 40f;
            lifetime = 150f;
            hitSound = Sounds.bang;
            hitShake = 5f;
            despawnEffect = PMFx.nuclearExplosion;

            trailParam = 5f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailMedium;

            autoDropRadius = stopRadius = -1f;
            fallEngineSize = 8f;
            elevation = 900f;
            riseTime = -1f;
            fallTime = 75f;
            trailSize = 0.7f;
            fallSpin = 135f;
        }};

        arbiterCluster = new StrikeBulletType(1f, 0f, "prog-mats-arbiter-cluster"){{
            homingPower = 0.05f;
            homingRange = 2200f;
            lifetime = 5500f;
            hitSound = Sounds.none;
            hitShake = 0f;
            despawnEffect = hitEffect = Fx.none;

            fragBullets = 20;
            fragBullet = arbiterClusterFrag;
            fragVelocityMin = 0.1f;
            fragVelocityMax = 1f;
            fragLifeMin = 0.5f;

            trailParam = 8f;
            trailChance = 0.2f;
            trailEffect = PMFx.missileTrailLarge;

            autoDropRadius = 30f;
            stopRadius = 20f;
            riseEngineSize = 24f;
            trailSize = 0.7f;
            elevation = 900f;
            riseTime = 240f;
            fallTime = -1f;
            trailSize = 2f;
            riseSpin = 720f;
            targetRadius = 2f;

            unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;
        }};

        basicSentryDrop = new UnitSpawnStrikeBulletType(PMUnitTypes.basicSentry);
        strikeSentryDrop = new UnitSpawnStrikeBulletType(PMUnitTypes.strikeSentry);
        dashSentryDrop = new UnitSpawnStrikeBulletType(PMUnitTypes.dashSentry);

        arbiterSentry = new StrikeBulletType(2.25f, 0f, "prog-mats-arbiter-unit"){
            public BulletType[] unitDrops = {basicSentryDrop, strikeSentryDrop, dashSentryDrop};

            {
                reloadMultiplier = 1.25F;
                homingPower = 0.05f;
                homingRange = 2200f;
                lifetime = 2000f;
                hitSound = Sounds.none;
                hitShake = 0f;
                despawnEffect = hitEffect = Fx.none;

                fragBullets = 30;
                fragVelocityMin = 0.1f;
                fragVelocityMax = 1.5f;
                fragLifeMin = 0.5f;

                trailParam = 8f;
                trailChance = 0.2f;
                trailEffect = PMFx.missileTrailLarge;

                autoDropRadius = 30f;
                stopRadius = 20f;
                riseEngineSize = 24f;
                trailSize = 0.7f;
                elevation = 900f;
                riseTime = 240f;
                fallTime = -1f;
                trailSize = 2f;
                riseSpin = 720f;
                targetRadius = 2f;

                unitSort = (u, x, y) -> -u.maxHealth + Mathf.dst2(x, y, u.x, u.y) / 1000f;
            }

            @Override
            public void despawned(Bullet b){
                super.despawned(b);
                for(int i = 0; i < fragBullets; i++){
                    float len = Mathf.random(1f, 7f);
                    float a = b.rotation() + Mathf.range(fragCone/2) + fragAngle;
                    BulletType randUnitDrop = unitDrops[Mathf.random(unitDrops.length - 1)];
                    randUnitDrop.create(b, b.x + Angles.trnsx(a, len), b.y + Angles.trnsy(a, len), a, Mathf.random(fragVelocityMin, fragVelocityMax), Mathf.random(fragLifeMin, fragLifeMax));
                }
            }
        };

        harbingerLaser = new LaserBulletType((float)Double.MAX_VALUE){
            {
                colors = new Color[]{Color.valueOf("F3E97966"), Color.valueOf("F3E979"), Color.white};
                length = 900f;
                width = 75f;
                lifetime = 130;
                lightColor = colors[1];
                ammoMultiplier = 1;

                lightningSpacing = 20f;
                lightningLength = 15;
                lightningLengthRand = 10;
                lightningDelay = 0.5f;
                lightningDamage = (float)Double.MAX_VALUE;
                lightningAngleRand = 45f;
                lightningColor = colors[1];

                sideAngle = 25f;
                sideWidth = width / 8f;
                sideLength = length / 1.5f;
            }

            @Override
            public void hitTile(Bullet b, Building build, float initialHealth, boolean direct){
                super.hitTile(b, build, initialHealth, direct);
                if(build.team != b.team) build.kill();
            }

            @Override
            public void hitEntity(Bullet b, Hitboxc other, float initialHealth){
                super.hitEntity(b, other, initialHealth);
                if(((Teamc)other).team() != b.team) ((Healthc)other).kill();
            }
        };

        excaliburLaser = new CrossLaserBulletType(5000f){{
            length = 800f;
            width = 26f;
            growTime = 10f;
            fadeTime = 40f;
            lifetime = 70f;
            crossLength = 300f;
            crossWidth = 18f;
            colors = new Color[]{
                Color.valueOf("E8D174").a(0.4f),
                Pal.surge,
                Color.white
            };
        }};
    }
}