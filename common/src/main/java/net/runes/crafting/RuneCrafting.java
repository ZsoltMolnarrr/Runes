package net.runes.crafting;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.runes.RunesMod;

public class RuneCrafting {
    public static final String NAME = "crafting";
    public static Identifier ID = Identifier.fromNamespaceAndPath(RunesMod.ID, NAME);
    public static SoundEvent SOUND = SoundEvent.createVariableRangeEvent(ID);
    public static int SOUND_DELAY = 20;

    public static RecipeType<RuneCraftingRecipe> RECIPE_TYPE;
    public static RecipeSerializer<RuneCraftingRecipe> RECIPE_SERIALIZER;

    public static void registerRecipe() {
        // Must be the same instance RuneCraftingRecipe#getType() returns: 1.21.2+ groups recipes by that object.
        RECIPE_TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, ID, RuneCraftingRecipe.TYPE);
        RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ID, new RuneCraftingRecipe.Serializer());
    }
}
