package progressed.entities.bullet;

import arc.math.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;

public class UnitSpawnStrikeBulletType extends StrikeBulletType{
    public float minVelMult = 1f, maxVelMult = 3f, killChance = 0.25f;
    public Item[] items = {Items.pyratite, Items.pyratite, Items.blastCompound};
    public UnitType spawn;

    public UnitSpawnStrikeBulletType(float speed, float damage, UnitType spawn){
        super(speed, damage);
        this.spawn = spawn;
        snapRot = true;

        //All of the unit drops have the same stats, so I might as well.
        riseEngineSize = fallEngineSize = -1f;
        homingPower = -1;
        lifetime = 150f;
        elevation = 900f;
        riseTime = -1f;
        fallTime = 75f;
        hitSound = Sounds.none;
        hitShake = 0.5f;
        targetRadius = 0.5f;
        trailEffect = despawnEffect = Fx.none;
        autoDropRadius = stopRadius = riseSpin = fallSpin = 0f;
    }

    public UnitSpawnStrikeBulletType(UnitType spawn){
        this(0.5f, 20f, spawn);
    }

    @Override
    public void init(Bullet b){
        super.init(b);

        Object[] data = {b.x, b.y, null, false};
        b.data = data;

        drawSize = elevation + 64f;
    }
    
    @Override
    public void load(){
        frontRegion = backRegion = spawn.icon(Cicon.full);
    }

    @Override
    public void despawned(Bullet b){
        super.despawned(b);

        Unit spawned = spawn.spawn(b.team, b.x, b.y);
        spawned.rotation = b.rotation();
        float vel = Mathf.random(minVelMult, maxVelMult);

        if(items.length > 0){
            Item randomItem = items[Mathf.random(items.length - 1)];
            int amount = Mathf.random(0, spawned.maxAccepted(randomItem));
            if(amount > 0){
                spawned.addItem(randomItem, amount);
            }
        }

        if(vel != 0f) spawned.vel.add(b.vel, vel);
        if(Mathf.chance(killChance)) spawned.kill();
    }
}
