package net.runes.crafting;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;

public class RuneCrafting {
    public static final String NAME = "crafting";
    public static Identifier ID = Identifier.of(RunesMod.ID, NAME);
    public static SoundEvent SOUND = SoundEvent.of(ID);
    public static int SOUND_DELAY = 20;

    public static RecipeType<RuneCraftingRecipe> RECIPE_TYPE;
    public static RecipeSerializer<RuneCraftingRecipe> RECIPE_SERIALIZER;

    public static void registerRecipe() {
        RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, ID, new RecipeType<RuneCraftingRecipe>() {
            public String toString() {
                return NAME;
            }
        });
        RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, ID, new RuneCraftingRecipe.Serializer());
    }
}
