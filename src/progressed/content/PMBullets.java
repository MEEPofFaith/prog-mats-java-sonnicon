package progressed.content;

import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import progressed.entities.bullet.*;

public class PMBullets implements ContentList{
    public static BulletType
    pixel;

    @Override
    public void load(){
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
    }
}
