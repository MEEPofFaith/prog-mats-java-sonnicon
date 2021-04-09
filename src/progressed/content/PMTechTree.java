package progressed.content;

import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.game.Objectives.*;
import mindustry.type.*;
import mindustry.world.Block;
import multilib.MultiCrafter;
import progressed.util.*;

import static mindustry.content.Items.*;
import static mindustry.content.Blocks.*;
import static mindustry.content.TechTree.*;
import static progressed.content.PMBlocks.*;
import static progressed.content.PMItems.*;
import static progressed.content.PMUnitTypes.*;

public class PMTechTree implements ContentList{
    //Dont mind me I'ma just yoink some stuff from BetaMindy
    static TechTree.TechNode context = null;

    @Override
    public void load(){
        vanillaNode(meltdown, () ->{
            // Eruptors
            node(flame, () -> {
                node(blaze, Seq.with(new Objectives.SectorComplete(SectorPresets.overgrowth)), () -> {
                    node(inferno, Seq.with(new Objectives.SectorComplete(SectorPresets.nuclearComplex)));

                    //Sword
                    node(masquerade, () -> {
                        node(violet, Seq.with(new Objectives.SectorComplete(SectorPresets.nuclearComplex)));
                    });
                });
            });
        });

        vanillaNode(salvo, () -> {
            //Miniguns
            node(minigun, Seq.with(new Objectives.SectorComplete(SectorPresets.fungalPass)), () -> {
                node(miinigun, Seq.with(new Objectives.SectorComplete(SectorPresets.overgrowth)), () -> {
                    node(mivnigun);
                });
            });

            //Kugelblitz
            node(blackhole, Seq.with(new Objectives.SectorComplete(SectorPresets.nuclearComplex), new Objectives.Research(meltdown)));
        });

        vanillaNode(ripple, () -> {
            //Missile (also painful to look at)
            node(firestorm, Seq.with(new Objectives.Research(launchPad), new Objectives.SectorComplete(SectorPresets.impact0078)), () -> {
                node(strikedown, PMUtls.addItemStacks(new ItemStack[][]{brq(strikedown), brq(shellPress), recipeCost((MultiCrafter)shellPress, 0), brq(missileFactory), recipeCost((MultiCrafter)missileFactory, 0)}), Seq.with(new Objectives.Research(launchPad), new Objectives.SectorComplete(SectorPresets.nuclearComplex)), () -> {
                    node(shellPress, ItemStack.empty, Seq.with(new Objectives.Research(strikedown)), () -> {
                        node(missileShell, ItemStack.empty, Seq.with(new Objectives.Research(strikedown)));
                        node(nukeShell, ItemStack.empty, Seq.with(new Objectives.Research(arbiter)));
                        node(missileFactory, ItemStack.empty, Seq.with(new Objectives.Research(strikedown)), () -> {
                            //Missile
                            node(basicMissile, ItemStack.empty, Seq.with(new Objectives.Research(strikedown)), () -> {
                                node(empMissile, recipeCost((MultiCrafter)missileFactory, 1, 5f));
                                node(quantiumMissile, recipeCost((MultiCrafter)missileFactory, 2, 5f));
                            });
                            //Nuke
                            node(basicNuke, ItemStack.empty, Seq.with(new Objectives.Research(arbiter)), () -> {
                                node(empNuke, recipeCost((MultiCrafter)missileFactory, 4, 5f));
                                node(clusterNuke, recipeCost((MultiCrafter)missileFactory, 5, 5f));
                                node(sentryNuke, recipeCost((MultiCrafter)missileFactory, 6, 5f));
                            });
                        });
                    });
                    node(arbiter, PMUtls.addItemStacks(new ItemStack[][]{brq(arbiter), recipeCost((MultiCrafter)shellPress, 1), recipeCost((MultiCrafter)missileFactory, 3)}), Seq.with(new Objectives.Research(interplanetaryAccelerator), new Objectives.SectorComplete(SectorPresets.planetaryTerminal)));
                });
            });

            //Tinker
            node(tinker, PMUtls.addItemStacks(new ItemStack[][]{brq(tinker), brq(sentryBuilder), recipeCost((MultiCrafter)sentryBuilder, 0)}), Seq.with(new Objectives.SectorComplete(SectorPresets.windsweptIslands)), () -> {
                node(sentryBuilder, ItemStack.empty, Seq.with(new Objectives.Research(tinker)), () -> {
                    node(basicSentryBox, ItemStack.empty, Seq.with(new Objectives.Research(sentryBuilder)), () -> {
                        node(basicSentry, ItemStack.empty, Seq.with(new Objectives.Research(basicSentryBox)));
                    });
                    node(strikeSentryBox, recipeCost((MultiCrafter)sentryBuilder, 1, 5f), Seq.with(new Objectives.Research(firestorm)), () -> {
                        node(strikeSentry, ItemStack.empty, Seq.with(new Objectives.Research(strikeSentryBox)));
                    });
                    node(dashSentryBox, recipeCost((MultiCrafter)sentryBuilder, 2, 5f), Seq.with(new Objectives.Research(lancer)), () -> {
                        node(dashSentry, ItemStack.empty, Seq.with(new Objectives.Research(dashSentryBox)));
                    });
                });
            });
        });

        vanillaNode(arc, () -> {
            //Pixel
            node(bit);
            
            //Coil
            node(shock, () -> {
                node(spark, Seq.with(new Objectives.Research(differentialGenerator)), () -> {
                    node(storm, Seq.with(new Objectives.Research(thoriumReactor)));
                });
            });
        });

        vanillaNode(cyclone, () -> {
            //Sniper
            node(caliber);
        });

        vanillaNode(foreshadow, () -> {
            //P o p e s h a d o w
            node(excalibur, Seq.with(new Objectives.SectorComplete(SectorPresets.nuclearComplex)));
        });

        vanillaNode(armoredConveyor, () -> {
            //Floating Conveyor
            node(floatingConveyor, Seq.with(new Objectives.SectorComplete(SectorPresets.windsweptIslands)));
        });

        vanillaNode(surgeSmelter, () -> {
            //Mindron Collider
            node(mindronCollider);
        });

        //Items
        vanillaNode(surgeAlloy, () -> {
            nodeProduce(techtanite);
        });
    }

    private static ItemStack[] brq(Block content){
        return content.researchRequirements();
    }

    private static ItemStack[] recipeCost(MultiCrafter crafter, int rec, float multiplier){
        return PMUtls.researchRequirements(crafter.recs[rec].input.items, multiplier);
    }

    private static ItemStack[] recipeCost(MultiCrafter crafter, int rec){
        return recipeCost(crafter, rec, 1f);
    }
    
    //Dont mind me I'ma just yoink some stuff from BetaMindy
    private static void vanillaNode(UnlockableContent parent, Runnable children){
        TechNode parnode = TechTree.all.find(t -> t.content == parent);
        context = parnode;
        children.run();
    }

    private static void node(UnlockableContent content, ItemStack[] requirements, Seq<Objective> objectives, Runnable children){
        TechNode node = new TechNode(context, content, requirements);
        if(objectives != null) node.objectives = objectives;

        TechNode prev = context;
        context = node;
        children.run();
        context = prev;
    }

    private static void node(UnlockableContent content, ItemStack[] requirements, Seq<Objective> objectives){
        node(content, requirements, objectives, () -> {});
    }

    private static void node(UnlockableContent content, Seq<Objective> objectives){
        node(content, content.researchRequirements(), objectives, () -> {});
    }

    private static void node(UnlockableContent content, ItemStack[] requirements){
        node(content, requirements, Seq.with(), () -> {});
    }

    private static void node(UnlockableContent content, ItemStack[] requirements, Runnable children){
        node(content, requirements, null, children);
    }

    private static void node(UnlockableContent content, Seq<Objective> objectives, Runnable children){
        node(content, content.researchRequirements(), objectives, children);
    }

    private static void node(UnlockableContent content, Runnable children){
        node(content, content.researchRequirements(), children);
    }

    private static void node(UnlockableContent block){
        node(block, () -> {});
    }

    private static void nodeProduce(UnlockableContent content, Seq<Objective> objectives, Runnable children){
        node(content, content.researchRequirements(), objectives.and(new Produce(content)), children);
    }

    private static void nodeProduce(UnlockableContent content, Seq<Objective> objectives){
        nodeProduce(content, objectives, () -> {});
    }

    private static void nodeProduce(UnlockableContent content, Runnable children){
        nodeProduce(content, Seq.with(), children);
    }

    private static void nodeProduce(UnlockableContent content){
        nodeProduce(content, Seq.with(), () -> {});
    }
}