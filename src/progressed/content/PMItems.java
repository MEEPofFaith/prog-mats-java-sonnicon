package progressed.content;

import arc.graphics.*;
import mindustry.ctype.*;
import mindustry.type.*;
import progressed.type.*;

public class PMItems implements ContentList{
    public static Item
    techtanite,
    
    missileShell, nukeShell,

    basicMissile, empMissile, recursiveMissile,

    basicNuke, clusterNuke, sentryNuke,

    basicSentryBox, strikeSentryBox, dashSentryBox;

    @Override
    public void load(){
        techtanite = new Item("techtanite", Color.valueOf("B0BAC0")){{
            cost = 1.6f;
        }};

        missileShell = new Item("missile-shell", Color.valueOf("FEB380"));

        nukeShell = new Item("nuke-shell", Color.valueOf("F58859"));

        basicMissile = new Item("basic-missile", Color.valueOf("EAB678"));

        empMissile = new Item("emp-missile", Color.valueOf("6974C4"));

        recursiveMissile = new Item("recursive-missile", Color.valueOf("73D188"));

        basicNuke = new Item("basic-nuke", Color.valueOf("D4816B"));

        clusterNuke = new Item("cluster-nuke", Color.valueOf("62AE7F"));

        sentryNuke = new Item("sentry-nuke", Color.valueOf("FEB380"));

        basicSentryBox = new SentryItem("basic-sentry-box", Color.valueOf("C9A58F"), PMUnitTypes.basicSentry);
        
        strikeSentryBox = new SentryItem("strike-sentry-box", Color.valueOf("FFA665"), PMUnitTypes.strikeSentry);

        dashSentryBox = new SentryItem("dash-sentry-box", Color.valueOf("84F491"), PMUnitTypes.dashSentry);
    }
}