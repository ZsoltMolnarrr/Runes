package net.runes;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.runes.api.RuneItems;
import net.runes.crafting.*;

public class RunesMod {
    public static final String ID = "runes";

    // One method per target registry: Forge 47's `RegisterEvent` opens exactly one registry at a time,
    // so anything registered in the wrong window hits a locked registry.

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
