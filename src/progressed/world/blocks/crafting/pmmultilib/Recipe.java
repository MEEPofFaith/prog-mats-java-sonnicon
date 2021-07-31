package progressed.world.blocks.crafting.pmmultilib;

import mindustry.type.*;

public class Recipe{
    public Inputs inputs;
    public Outputs outputs;
    public boolean requireResearch;

    public Recipe(Inputs inputs, Outputs outputs, boolean requireResearch){
        this.inputs = inputs;
        this.outputs = outputs;
        this.requireResearch = requireResearch;
    }

    public Recipe(Inputs inputs, Outputs outputs){
        this(inputs, outputs, false);
    }

    public ItemStack[] inputItems(){
        return inputs.inputItems;
    }

    public float inputPower(){
        return inputs.inputPower;
    }

    public float craftTime(){
        return inputs.time;
    }

    public boolean canRequirePower(){
        return inputs.inputPower > 0;
    }

    public ItemStack[] outputItems(){
        return outputs.outputItems;
    }

    public static class Inputs{
        ItemStack[] inputItems;
        float inputPower;
        float time;

        public Inputs(ItemStack[] inputItems, float inputPower, float time){
            this.inputItems = inputItems;
            this.inputPower = inputPower;
            this.time = time;
        }
    }

    public static class Outputs{
        ItemStack[] outputItems;

        public Outputs(ItemStack[] outputItems){
            this.outputItems = outputItems;
        }
    }
}
