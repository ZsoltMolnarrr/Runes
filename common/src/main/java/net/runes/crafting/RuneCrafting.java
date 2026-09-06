package net.runes.crafting;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;

public class RuneCrafting {
    public static final String NAME = "crafting";
    public static Identifier ID = new Identifier(RunesMod.ID, NAME);
    public static SoundEvent SOUND = SoundEvent.of(ID);
    public static int SOUND_DELAY = 20;

    // 1.20.1: the recipe type and serializer are plain static finals on the recipe class (there is no
    // codec/`MapCodec` plumbing), so registration is just two `Registry.register` calls. They go into
    // TWO different registries, and Forge 47 hands out exactly one registry per `RegisterEvent`
    // window — hence two separate methods.

    public static void registerRecipeType() {
        Registry.register(Registries.RECIPE_TYPE, ID, RuneCraftingRecipe.TYPE);
    }

    public static void registerRecipeSerializer() {
        Registry.register(Registries.RECIPE_SERIALIZER, ID, RuneCraftingRecipe.SERIALIZER);
    }
}
