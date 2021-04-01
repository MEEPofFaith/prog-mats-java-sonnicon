package progressed.content;

import arc.graphics.*;
import mindustry.ctype.*;
import mindustry.type.*;
import progressed.type.*;

public class PMItems implements ContentList{
    public static Item
    techtanite,
    
    missileShell, nukeShell,

    basicMissile, empMissile, quantiumMissile,

    basicNuke, empNuke, clusterNuke, sentryNuke,

    basicSentryBox, strikeSentryBox, dashSentryBox;

    @Override
    public void load(){
        techtanite = new Item("techtanite", Color.valueOf("8C8C8C")){{
            cost = 2.5f;
        }};

        missileShell = new Item("missile-shell", Color.valueOf("FEB380"));

        nukeShell = new Item("nuke-shell", Color.valueOf("F58859"));

        basicMissile = new Item("basic-missile", Color.valueOf("EAB678"));

        empMissile = new Item("emp-missile", Color.valueOf("6974C4"));

        quantiumMissile = new Item("quantum-missile", Color.valueOf("EFCA98"));

        basicNuke = new Item("basic-nuke", Color.valueOf("D4816B"));

        empNuke = new Item("emp-nuke", Color.valueOf("5757C1"));

        clusterNuke = new Item("cluster-nuke", Color.valueOf("EDF3A9"));

        sentryNuke = new Item("sentry-nuke", Color.valueOf("FEB380"));

        basicSentryBox = new SentryItem("basic-sentry-box", Color.valueOf("C9A58F"), PMUnitTypes.basicSentry);
        
        strikeSentryBox = new SentryItem("strike-sentry-box", Color.valueOf("FFA665"), PMUnitTypes.strikeSentry);

        dashSentryBox = new SentryItem("dash-sentry-box", Color.valueOf("84F491"), PMUnitTypes.dashSentry);
    }
}