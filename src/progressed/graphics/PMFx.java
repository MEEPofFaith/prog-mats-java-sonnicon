package progressed.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.graphics.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

public class PMFx{
    public static final Effect

    bitTrail = new Effect(75f, e -> {
        float offset = Mathf.randomSeed(e.id);
        Color c = PMPal.pixelFront.cpy().lerp(PMPal.pixelBack, Mathf.absin(Time.time * 0.05f + offset, 1f, 1f));
        color(c);
        Fill.square(e.x, e.y, e.rotation * e.fout());
    }),

    bitBurst = new Effect(30f, e -> {
        float[] set = {Mathf.curve(e.time, 0, e.lifetime * 2/3), Mathf.curve(e.time, e.lifetime * 1/3, e.lifetime)};
        float offset = Mathf.randomSeed(e.id);
        Color c = PMPal.pixelFront.cpy().lerp(PMPal.pixelBack, Mathf.absin(Time.time * 0.05f + offset, 1f, 1f));
        color(c);
        stroke(2.5f);

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
                    
                    line(Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y);
                }
            }
        }
    }),

    missileTrailSmall = new Effect(150f, 100f, e -> {
        color(e.color);
        Fill.circle(e.x, e.y, 3f * e.fout());
    }),

    missileTrailMedium = new Effect(120f, 300f, e -> {
        color(e.color);
        Fill.circle(e.x, e.y, 5f * e.fout());
    }),

    missileTrailLarge = new Effect(240f, 500f, e -> {
        color(e.color);
        Fill.circle(e.x, e.y, 8f * e.fout());
    }),

    smallBoom = new Effect(30f, e -> {
        color(Pal.missileYellow);

        e.scaled(7, s -> {
            stroke(1.5f * s.fout());
            Lines.circle(e.x, e.y, 2f + s.fin() * 15f);
        });

        color(Color.gray);

        randLenVectors(e.id, 8, 1f + 15f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.25f + e.fout() * 2f);
        });

        color(Pal.missileYellowBack);
        stroke(e.fout() * 0.5f);

        randLenVectors(e.id + 1, 6, 0.5f + 14.5f * e.finpow(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 0.5f + e.fout() * 2f);
        });
    }),

    nuclearExplosion = new Effect(140f, e -> {
        e.scaled(16f, s -> {
            color(Pal.missileYellow);
            stroke(4.5f * s.fout());
            Lines.circle(e.x, e.y, 6f + s.fin() * 45f);
        });
        e.scaled(60f, s -> {
            color(Color.gray);
    
            randLenVectors(e.id, 8, 3f + 45f * s.finpow(), (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.75f + s.fout() * 6f);
            });
    
            color(Pal.missileYellowBack);
            stroke(s.fout() * 1.5f);
    
            randLenVectors(e.id + 1, 6, 1.5f + 43.5f * s.finpow(), (x, y) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1.5f + s.fout() * 6f);
            });
        });
        randLenVectors(e.id + 3, 50, e.finpow() * 40f, (x, y) -> {
            float size = e.fout() * 4f;
            color(Pal.lighterOrange, Color.lightGray, e.fin());
            Fill.circle(e.x + x, e.y + y, size / 2f);
        });
    }),

    mushroomCloudExplosion = new Effect(450f, 800f, e -> {
        float colorFin = e.fin(Interp.pow2Out);
        float slopeFin = e.fin(Interp.pow3Out);
        float slopeFout = 1 - e.fin(Interp.pow3In);

        randLenVectors(e.id, 300, 140f * slopeFin, (x, y) -> {
            color(Color.white, Color.gray, colorFin);
            Fill.circle(e.x + x, e.y + y, 8f * slopeFout);
        });

        color(Color.yellow, Color.lightGray, colorFin);
        stroke(6f * e.fout());
        Lines.circle(e.x, e.y, 180f * slopeFin);

        randLenVectors(e.id * 2, 400, 60f * slopeFin, (x, y) -> {
            color(Color.orange.cpy().lerp(new Color[]{Color.orange, Color.red, Color.crimson, Color.darkGray}, colorFin));
            Fill.circle(e.x + x, e.y + y, 8f * slopeFout);
        });
    }),
    
    sniperCrit = new Effect(120f, e -> {
        Tmp.v1.trns(e.rotation + 90f, 0f, 48f * e.fin(Interp.pow2Out));
        
        randLenVectors(e.id, 12, 24f, (x, y) -> {
            float rot = Mathf.randomSeed((long)(e.id + x + y), 360);
            float tx = x * e.fin(Interp.pow2Out);
            float ty = y * e.fin(Interp.pow2Out);
            PMDrawf.plus(e.x + tx + Tmp.v1.x, e.y + ty + Tmp.v1.y, 4f, rot, e.color, e.fout());
        });
    });
}