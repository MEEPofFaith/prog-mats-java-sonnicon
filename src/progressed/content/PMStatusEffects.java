package progressed.content;

import arc.graphics.*;
import arc.struct.*;
import mindustry.ctype.*;
import mindustry.graphics.*;
import mindustry.type.*;
import progressed.type.*;

public class PMStatusEffects implements ContentList{
    public static StatusEffect
    emp,

    vcFrenzy, vcDisassembly, vcWeaken;

    @Override
    public void load(){
        emp = new ParalyzeStatusEffect("emp"){{
            color = Color.valueOf("7fabff");
            speedMultiplier = 0.4f;
            reloadMultiplier = 0.7f;
            damage = 0.04f;
            rotationRand = 8f;
            cooldown = 60f * 5f;
        }};

        //Anti-vaxxers are quivering in fear
        vcFrenzy = new ExclusiveStatusEffect("frenzy"){{
            color = Pal.lightOrange;
            damageMultiplier = 1.5f;
            speedMultiplier = 1.5f;
            reloadMultiplier = 1.5f;
            healthMultiplier = 0.5f;
            damage = 5f;
        }};

        vcDisassembly = new ExclusiveStatusEffect("disassembly"){{
            color = Color.darkGray;
            reloadMultiplier = 0.6f;
            speedMultiplier = 0.7f;
            healthMultiplier = 0.6f;
            damage = 1f;
        }};

        vcWeaken = new ExclusiveStatusEffect("weaken"){{
            color = Pal.sapBulletBack;
            healthMultiplier = 0.7f;
            damageMultiplier = 0.5f;
        }};

        afterLoad();
    }

    public void afterLoad(){
        ((ExclusiveStatusEffect)vcFrenzy).exclusives = Seq.with(vcDisassembly, vcWeaken);
        ((ExclusiveStatusEffect)vcDisassembly).exclusives = Seq.with(vcFrenzy, vcWeaken);
        ((ExclusiveStatusEffect)vcWeaken).exclusives = Seq.with(vcFrenzy, vcDisassembly);
    }
}