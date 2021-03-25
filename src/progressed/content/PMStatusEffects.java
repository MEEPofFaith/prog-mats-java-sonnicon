package progressed.content;

import mindustry.ctype.*;
import mindustry.graphics.*;
import mindustry.type.*;
import progressed.type.*;

public class PMStatusEffects implements ContentList{
    public static StatusEffect emp, teleportation, empStrong;

    @Override
    public void load(){
        emp = new ParalizeStatusEffect("strikedown-emp"){{
            damageMultiplier = 0.9f;
            speedMultiplier = 0.04f;
            reloadMultiplier = 0.55f;
            damage = 3f;
            rotationRand = 8f;
            cooldown = 60f * 5f;
        }};

        teleportation = new TeleportStatusEffect("strikedown-quantum"){{
            teleportRange = 40f;
            rangeScl = 1.5f;
            teleportDamageScl = 0.5f;
            cooldown = 60f * 5f;
        }};

        empStrong = new ParalizeStatusEffect("arbiter-emp"){{
            damageMultiplier = 0.8f;
            healthMultiplier = 0.9f;
            speedMultiplier = 0.02f;
            reloadMultiplier = 0.5f;
            damage = 5f;
            rotationRand = 15f;
            cooldown = 60f * 12f;
        }};
    }
}
