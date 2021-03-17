package progressed.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.Effect;

public class PMFx{
    public static final Effect

    bitTrail = new Effect(75f, e -> {
        float offset = Mathf.randomSeed(e.id);
        Color c = PMPal.pixelFront.cpy().lerp(PMPal.pixelBack, Mathf.absin(Time.time * 0.05f + offset, 1f, 1f));
        Draw.color(c);
        Fill.square(e.x, e.y, e.rotation * e.fout());
    }),

    bitBurst = new Effect(30f, e -> {
        float[] set = {Mathf.curve(e.time, 0, e.lifetime * 2/3), Mathf.curve(e.time, e.lifetime * 1/3, e.lifetime)};
        float offset = Mathf.randomSeed(e.id);
        Color c = PMPal.pixelFront.cpy().lerp(PMPal.pixelBack, Mathf.absin(Time.time * 0.05f + offset, 1f, 1f));
        Draw.color(c);
        Lines.stroke(2.5f);

        for(int i = 0; i < 2; i++){
            if(set[i] > 0 && set[i] < 1){
                for(int j = 0; j < 8; j++){
                    float s = 41 * set[i];
                    float front = Mathf.clamp(s, 0f, 21f - 2f * 3f);
                    float back = Mathf.clamp(s - 3f, 0f, 21f - 2f * 3f);
                    
                    Tmp.v1.trns(j * 45f, 0f, front);
                    Tmp.v1.add(e.x, e.y);
                    Tmp.v2.trns(j * 45f, 0f, back);
                    Tmp.v2.add(e.x, e.y);
                    
                    Lines.line(Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y);
                }
            }
        }
    });
}
