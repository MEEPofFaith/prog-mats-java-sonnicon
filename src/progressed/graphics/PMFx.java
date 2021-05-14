package progressed.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import progressed.entities.bullet.*;
import progressed.entities.bullet.BlackHoleBulletType.*;
import progressed.world.blocks.defence.turret.AimLaserTurret;
import progressed.world.blocks.defence.turret.AimLaserTurret.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;

public class PMFx{
    public static final Effect

    bitTrail = new Effect(75f, e -> {
        float offset = Mathf.randomSeed(e.id);
        Color c = Tmp.c1.set(PMPal.pixelFront).lerp(PMPal.pixelBack, Mathf.absin(Time.time * 0.05f + offset, 1f, 1f));
        color(c);
        Fill.square(e.x, e.y, e.rotation * e.fout());
    }),

    bitBurst = new Effect(30f, e -> {
        float[] set = {Mathf.curve(e.time, 0, e.lifetime * 2/3), Mathf.curve(e.time, e.lifetime * 1/3, e.lifetime)};
        float offset = Mathf.randomSeed(e.id);
        Color c = Tmp.c1.set(PMPal.pixelFront).lerp(PMPal.pixelBack, Mathf.absin(Time.time * 0.05f + offset, 1f, 1f));
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

    critPierce = new Effect(20f, e -> {
        float rot = e.rotation - 90f;
        float fin = e.fin(Interp.pow5Out);
        float end = e.lifetime - 6f;
        float fout = 1f - Interp.pow2Out.apply(Mathf.curve(e.time, end, e.lifetime));
        float width = fin * fout;

        e.scaled(7f, s -> {
            stroke(0.5f + s.fout());
            color(Color.white, e.color, s.fin());
            Lines.circle(e.x + trnsx(rot, 0f, 5f * fin), e.y + trnsy(rot, 0f, 5f * fin), s.fin() * 6f);
        });

        color(Color.white, e.color, Mathf.curve(e.time, 0f, end));

        Fill.quad(
            e.x + trnsx(rot, 0f, 2f * fin), e.y + trnsy(rot, 0f, 2f * fin),
            e.x + trnsx(rot, 4f * width, -4f * fin), e.y + trnsy(rot, 4f * width, -4f * fin),
            e.x + trnsx(rot, 0f, 8f * fin), e.y + trnsy(rot, 0f, 8f * fin),
            e.x + trnsx(rot, -4f * width, -4f * fin), e.y + trnsy(rot, -4f * width, -4f * fin)
        );
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

    missileBoom = new Effect(30f, e -> {
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

    mushroomCloudExplosion = new Effect(250f, 800f, e -> {
        float colorFin = e.fin(Interp.pow2Out);
        float slopeFin = e.fin(Interp.pow5Out);
        float slopeFout = 1 - e.fin(Interp.pow3In);

        randLenVectors(e.id, 150, 140f * slopeFin, (x, y) -> {
            color(Color.white, Color.gray, colorFin);
            Fill.circle(e.x + x, e.y + y, 8f * slopeFout);
        });

        e.scaled(125f, s -> {
            float sColorFin = s.fin(Interp.pow2Out);
            float sSlopeFin = s.fin(Interp.pow5Out);
            color(Color.lightGray, Color.white, sColorFin);
            stroke(6f * s.fout());
            Lines.circle(e.x, e.y, 180f * sSlopeFin);
        });

        randLenVectors(e.id * 2, 200, 60f * slopeFin, (x, y) -> {
            color(Tmp.c1.set(Color.orange).lerp(new Color[]{Color.orange, Color.red, Color.crimson, Color.darkGray}, colorFin));
            Fill.circle(e.x + x, e.y + y, 8f * slopeFout);
        });
    }),
    
    sniperCritMini = new Effect(90f, e -> {
        Tmp.v1.trns(e.rotation + 90f, 0f, 32f * e.fin(Interp.pow2Out));
        
        randLenVectors(e.id, 2, 18f, (x, y) -> {
            float rot = Mathf.randomSeed((long)(e.id + x + y), 360);
            float tx = x * e.fin(Interp.pow2Out);
            float ty = y * e.fin(Interp.pow2Out);
            PMDrawf.plus(e.x + tx + Tmp.v1.x, e.y + ty + Tmp.v1.y, 3f, rot, e.color, e.fout());
        });
    }),
    
    sniperCrit = new Effect(120f, e -> {
        Tmp.v1.trns(e.rotation + 90f, 0f, 48f * e.fin(Interp.pow2Out));
        
        randLenVectors(e.id, 6, 24f, (x, y) -> {
            float rot = Mathf.randomSeed((long)(e.id + x + y), 360);
            float tx = x * e.fin(Interp.pow2Out);
            float ty = y * e.fin(Interp.pow2Out);
            PMDrawf.plus(e.x + tx + Tmp.v1.x, e.y + ty + Tmp.v1.y, 4f, rot, e.color, e.fout());
        });
    }),
    
    sentryTrail = new Effect(150f, e -> {
        color(e.color);
        Fill.circle(e.x, e.y, e.rotation * e.fout());
    }),
    
    kugelblitzChargeBegin = new Effect(80f, e -> {
        Draw.z(Layer.max - 0.01f);
        Fill.light(e.x, e.y, 60, 6f * e.fin(), Tmp.c1.set(e.color).lerp(Color.black, 0.5f + Mathf.absin(10f, 0.4f)), Color.black);
    }),
    
    kugelblitzCharge = new Effect(38f, e -> {
        color(Tmp.c1.set(e.color).lerp(Color.black, 0.5f), Color.black, e.fin());
        randLenVectors(e.id, 2, 45f * e.fout(), e.rotation, 180f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            Lines.lineAngle(e.x + x, e.y + y, ang, e.fslope() * 5f);
        });
    }),
    
    blackHoleSwirl = new Effect(90f, e -> {
        Bullet bullet = (Bullet)e.data;

        if(bullet != null && bullet.type instanceof BlackHoleBulletType b){
            float a = Mathf.clamp(e.fin() * 8f);
            Tmp.c1.set(bullet.team.color).lerp(Color.black, 0.5f + Mathf.absin(Time.time + Mathf.randomSeed(e.id), 10f, 0.4f)).a(a);
            Tmp.c2.set(Color.black).a(a);
            float startAngle = Mathf.randomSeed(e.id, 360f, 720f);

            Fill.light(bullet.x + trnsx(e.rotation + startAngle * e.fout(),
                b.suctionRadius * e.fout()),
                bullet.y + trnsy(e.rotation + startAngle * e.fout(), b.suctionRadius * e.fout()),
                60,
                b.swirlSize * e.fout(),
                Tmp.c1,
                Tmp.c2
            );

            Drawf.light(bullet.x + trnsx(e.rotation + startAngle * e.fout(),
                b.suctionRadius * e.fout()),
                bullet.y + trnsy(e.rotation + startAngle * e.fout(),
                b.suctionRadius * e.fout()),
                b.swirlSize * e.fout(),
                Tmp.c1,
                0.7f * a
            );
        }
    }).layer(Layer.max - 0.02f),
    
    blackHoleDespawn = new Effect(24f, e -> {
        color(Color.darkGray, Color.black, e.fin());

        e.scaled(12f, s -> {
            stroke(2f * e.fout());
            Lines.circle(e.x, e.y, s.fin() * 10f);
        });

        stroke(2f * e.fout());
        randLenVectors(e.id, 4, e.fin() * 15f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
        });

        color(e.color);
        randLenVectors(e.id * 2, 4, e.fin() * 15f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
        });
    }).layer(Layer.max - 0.03f),
    
    blackHoleAbsorb = new Effect(20f, e -> {
        color(Color.black);
        stroke(2f * e.fout(Interp.pow3In));
        Lines.circle(e.x, e.y, 8f * e.fout(Interp.pow3In));
    }).layer(Layer.max - 0.04f),
    
    particle = new Effect(38f, e -> {
        color(e.color);

        randLenVectors(e.id, 2, 1f + 20f * e.fin(Interp.pow2Out), e.rotation, 120f, (x, y) -> {
            Drawf.tri(e.x + x, e.y + y, e.fslope() * 3f + 1, e.fslope() * 3f + 1, Mathf.angle(x, y));
        });
    }),
    
    teleportEffect = new Effect(60f, e -> {
        color(e.color, e.fout());

        float[] data = (float[])e.data;
        
        stroke(data[0] * e.fout());
        line(e.x, e.y, data[1], data[2]);
        Fill.circle(e.x, e.y, data[0] * 1.5f);
        Fill.circle(data[1], data[2], data[0] * 1.5f);
        
        reset();
    }),
    
    //[length, width, team]
    fakeLightning = new Effect(10f, 500f, e -> {
        Object[] data = (Object[])e.data;

        float length = (float)data[0];
        int tileLength = Mathf.round(length / tilesize);
        
        Lines.stroke((float)data[1] * e.fout());
        Draw.color(e.color, Color.white, e.fin());
        
        for(int i = 0; i < tileLength; i++){
            float offsetXA = i == 0 ? 0f : Mathf.randomSeed(e.id + (i * 6413), -4.5f, 4.5f);
            float offsetYA = (length / tileLength) * i;
            
            int f = i + 1;
            
            float offsetXB = f == tileLength ? 0f : Mathf.randomSeed(e.id + (f * 6413), -4.5f, 4.5f);
            float offsetYB = (length / tileLength) * f;
            
            Tmp.v1.trns(e.rotation, offsetYA, offsetXA);
            Tmp.v1.add(e.x, e.y);
            
            Tmp.v2.trns(e.rotation, offsetYB, offsetXB);
            Tmp.v2.add(e.x, e.y);
            
            Lines.line(Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y, false);
            Fill.circle(Tmp.v1.x, Tmp.v1.y, Lines.getStroke() / 2f);
            Drawf.light((Team)data[2], Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y, (float)data[1] * 3f, e.color, 0.4f);
        }

        Fill.circle(Tmp.v2.x, Tmp.v2.y, Lines.getStroke() / 2);
    }).layer(Layer.bullet + 0.01f),
    
    //[length, width, team]
    fakeLightningFast = new Effect(5f, 500f, e -> {
        Object[] data = (Object[])e.data;

        float length = (float)data[0];
        int tileLength = Mathf.round(length / tilesize);
        
        Lines.stroke((float)data[1] * e.fout());
        Draw.color(e.color, Color.white, e.fin());
        
        for(int i = 0; i < tileLength; i++){
            float offsetXA = i == 0 ? 0f : Mathf.randomSeed(e.id + (i * 6413), -4.5f, 4.5f);
            float offsetYA = (length / tileLength) * i;
            
            int f = i + 1;
            
            float offsetXB = f == tileLength ? 0f : Mathf.randomSeed(e.id + (f * 6413), -4.5f, 4.5f);
            float offsetYB = (length / tileLength) * f;
            
            Tmp.v1.trns(e.rotation, offsetYA, offsetXA);
            Tmp.v1.add(e.x, e.y);
            
            Tmp.v2.trns(e.rotation, offsetYB, offsetXB);
            Tmp.v2.add(e.x, e.y);
            
            Lines.line(Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y, false);
            Fill.circle(Tmp.v1.x, Tmp.v1.y, Lines.getStroke() / 2f);
            Drawf.light((Team)data[2], Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y, (float)data[1] * 3f, e.color, 0.4f);
        }

        Fill.circle(Tmp.v2.x, Tmp.v2.y, Lines.getStroke() / 2);
    }).layer(Layer.bullet + 0.01f),
    
    harbingerCharge = new Effect(150f, 1600f, e -> {
        Color[] colors = {Color.valueOf("D99F6B55"), Color.valueOf("E8D174aa"), Color.valueOf("F3E979"), Color.valueOf("ffffff")};
        float[] tscales = {1f, 0.7f, 0.5f, 0.2f};
        float[] strokes = {2f, 1.5f, 1, 0.3f};
        float[] lenscales = {1, 1.12f, 1.15f, 1.17f};

        float lightOpacity = 0.4f + (e.finpow() * 0.4f);
        
        Draw.color(colors[0], colors[2], 0.5f + e.finpow() * 0.5f);
        Lines.stroke(Mathf.lerp(0f, 28f, e.finpow()));
        Lines.circle(e.x, e.y, 384f * (1f - e.finpow()));
        
        for(int i = 0; i < 36; i++){
            Tmp.v1.trns(i * 10f, 384f * (1 - e.finpow()));
            Tmp.v2.trns(i * 10f + 10f, 384f * (1f - e.finpow()));
            Drawf.light((Team)e.data, e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.x + Tmp.v2.x, e.y + Tmp.v2.y, 14f / 2f + 60f * e.finpow(), Draw.getColor(), lightOpacity + (0.2f * e.finpow()));
        }
        
        float fade = 1f - Mathf.curve(e.time, e.lifetime - 30f, e.lifetime);
        float grow = Mathf.curve(e.time, 0f, e.lifetime - 30f);
        
        for(int i = 0; i < 4; i++){
            float baseLen = (900f + (Mathf.absin(Time.time / ((i + 1f) * 2f) + Mathf.randomSeed(e.id), 0.8f, 1.5f) * (900f / 1.5f))) * 0.75f * fade;
            Draw.color(Tmp.c1.set(colors[i]).mul(1f + Mathf.absin(Time.time / 3f + Mathf.randomSeed(e.id), 1.0f, 0.3f) / 3f));
            for(int j = 0; j < 2; j++){
                int dir = Mathf.signs[j];
                for(int k = 0; k < 10; k++){
                    float side = k * (360f / 10f);
                    for(int l = 0; l < 4; l++){
                        Lines.stroke((16f * 0.75f + Mathf.absin(Time.time, 0.5f, 1f)) * grow * strokes[i] * tscales[l]);
                        Lines.lineAngle(e.x, e.y, (e.rotation + 360f * e.finpow() + side) * dir, baseLen * lenscales[l], false);
                    }
                    
                    Tmp.v1.trns((e.rotation + 360f * e.finpow() + side) * dir, baseLen * 1.1f);
                    
                    Drawf.light((Team)e.data, e.x, e.y, e.x + Tmp.v1.x, e.y + Tmp.v1.y, ((16f * 0.75f + Mathf.absin(Time.time, 0.5f, 1f)) * grow * strokes[i] * tscales[j]) / 2f + 60f * e.finpow(), colors[2], lightOpacity);
                }
            }
            Draw.reset();
        }
    }),
    
    everythingGunSwirl = new Effect(120f, 1600f, e -> {
        float[] data = (float[])e.data;
        color(e.color, Color.black, 0.25f + e.fin() * 0.75f);
        Fill.circle(e.x + Angles.trnsx(Mathf.randomSeed(e.id, 360f) + e.rotation * e.fin(), (16f + data[1]) * e.fin()),
            e.y + Angles.trnsy(Mathf.randomSeed(e.id, 360f) + e.rotation * e.fin(), (16f + data[1]) * e.fin()),
            data[0] * e.fout()
        );
    }).layer(Layer.bullet - 0.00999f),
    
    colliderFusion = new Effect(30f, e -> {
        color(Color.gray);
        e.scaled(15f, s -> {
            stroke(s.fout());
            Lines.circle(e.x, e.y, 3f * s.fout());
        });
        
        stroke(1f);

        randLenVectors(e.id, 16, 5f * e.fout(), e.rotation, 180f, (x, y) -> {
            float ang = angle(x, y, 0f, 0f);
            Lines.lineAngle(e.x + x, e.y + y, ang, e.fslope() * 5f);
        });
    }).layer(32.5f),
    
    swordStab = new Effect(24f, e -> {
        color(e.color, Color.violet, e.fin());
        stroke(1f);

        e.scaled(15f, s -> {
            Lines.circle(e.x, e.y, 8f * s.fin());
        });

        randLenVectors(e.id, 16, 8f * e.fin(), e.rotation, 180f, (x, y) -> {
            float ang = angle(x, y);
            Lines.lineAngle(e.x + x, e.y + y, ang, e.fslope() * 4f);
        });
    }),
    
    flare = new Effect(50f, e -> {
        color(e.color, Color.gray, e.fin());

        randLenVectors(e.id, 2, e.fin() * 4f * e.rotation, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.1f + e.fslope() * 0.6f * e.rotation);
        });

        color();

        Drawf.light(Team.derelict, e.x, e.y, 20f * e.fslope(), Pal.lightFlame, 0.5f);
    }),
    
    aimChargeBegin = new Effect(300f, e -> {
        if(e.data instanceof AimLaserTurretBuild d){
            color(e.color);

            Tmp.v1.trns(d.rotation, ((AimLaserTurret)(d.block)).shootLength);
            Fill.circle(d.x + Tmp.v1.x, d.y + Tmp.v1.y, 3f * e.fin());

            color();
        }
    }),
    
    aimCharge = new Effect(30f, e -> {
        if(e.data instanceof AimLaserTurretBuild d){
            color(e.color);

            Tmp.v1.trns(d.rotation, ((AimLaserTurret)(d.block)).shootLength);
            randLenVectors(e.id, 3, 24f * e.fout(), (x, y) -> {
                Fill.circle(d.x + Tmp.v1.x + x, d.y + Tmp.v1.y + y, 2f * e.fin());
            });

            color();
        }
    }),
    
    sentinelBlast = new Effect(80f, e -> {
        color(Pal.missileYellow);

        e.scaled(50f, s -> {
            stroke(5f * s.fout());
            Lines.circle(e.x, e.y, 4f + s.fin() * 40f);
        });

        color(e.color);

        randLenVectors(e.id, 20, 3f + 60f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 1f + e.fout() * 6f);
        });

        color(Pal.missileYellowBack);
        stroke(e.fout());

        randLenVectors(e.id + 1, 11, 2f + 73f * e.finpow(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 2f + e.fout() * 5f);
        });
    }),
    
    staticSpark = new Effect(10f, e -> {
        color(e.color);
        stroke(e.fout() * 1.5f);

        randLenVectors(e.id, 7, e.finpow() * 27f, e.rotation, 45f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 4f + 1f);
        });
    });
}