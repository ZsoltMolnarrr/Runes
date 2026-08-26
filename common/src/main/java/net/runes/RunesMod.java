package net.runes;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.runes.api.RuneItems;
import net.runes.crafting.*;

public class RunesMod {
    public static final String ID = "runes";

    public static void registerSounds() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, RuneCrafting.ID, RuneCrafting.SOUND);
    }

    public static void registerScreenHandler() {
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(ID, RuneCraftingRecipe.NAME), RuneCraftingScreenHandler.HANDLER_TYPE);
    }

    public static void registerRecipeType() {
        RuneCrafting.registerRecipe();
    }

    public static void registerBlocks() {
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(ID, RuneCraftingBlock.NAME), RuneCraftingBlock.INSTANCE);
    }

    public static void registerItems() {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(ID, RuneCraftingBlock.NAME), RuneCraftingBlock.ITEM);
        for(var entry: RuneItems.entries) {
            Registry.register(BuiltInRegistries.ITEM, entry.id(), entry.item());
        }
    }
}