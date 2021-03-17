package progressed.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;

public class PMDrawf{

    public static void plus(float x, float y, float diameter, float angle, Color color, float alpha){
        Draw.color(color, alpha);
        for(int i = 0; i < 2; i++){
            Fill.rect(x, y, diameter / 3, diameter, angle + i * 90f);
        }
    }
}