package net.runes;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.runes.api.RuneItems;
import net.runes.crafting.*;

public class RunesMod {
    public static final String ID = "runes";

    // These are the Fabric registration path. Forge cannot use them — a plain `Registry.register` throws
    // "Can not register to a locked registry" on 47.0-47.3 — so `net.runes.forge.ForgeMod` deliberately
    // duplicates these loops through the helper its `RegisterEvent` hands out. Keep the two in step.
    //
    // One method per target registry, mirroring the one-registry-per-`RegisterEvent`-window shape the
    // Forge side has to obey (recipe TYPE and recipe SERIALIZER are two separate windows).

    public static void registerSounds() {
        Registry.register(Registries.SOUND_EVENT, RuneCrafting.ID, RuneCrafting.SOUND);
    }

    public static void registerScreenHandler() {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(ID, RuneCraftingRecipe.NAME), RuneCraftingScreenHandler.HANDLER_TYPE);
    }

    public static void registerRecipeType() {
        RuneCrafting.registerRecipeType();
    }

    public static void registerRecipeSerializer() {
        RuneCrafting.registerRecipeSerializer();
    }

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, new Identifier(ID, RuneCraftingBlock.NAME), RuneCraftingBlock.INSTANCE);
    }

    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier(ID, RuneCraftingBlock.NAME), RuneCraftingBlock.ITEM);
        for(var entry: RuneItems.entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
    }
}
