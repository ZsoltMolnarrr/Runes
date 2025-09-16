package net.runes;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.runes.api.RuneItems;
import net.runes.crafting.*;

public class RunesMod {
    public static final String ID = "runes";

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.add(RuneCraftingBlock.ITEM);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            for(var entry: RuneItems.entries) {
                content.add(entry.item());
            }
        });
    }

    public static void registerSounds() {
        Registry.register(Registries.SOUND_EVENT, RuneCrafting.ID, RuneCrafting.SOUND);
    }

    public static void registerScreenHandler() {
        Registry.register(Registries.SCREEN_HANDLER, Identifier.of(ID, RuneCraftingRecipe.NAME), RuneCraftingScreenHandler.HANDLER_TYPE);
    }

    public static void registerRecipeType() {
        RuneCrafting.registerRecipe();
    }

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, Identifier.of(ID, RuneCraftingBlock.NAME), RuneCraftingBlock.INSTANCE);
    }

    public static void registerItems() {
        Registry.register(Registries.ITEM, Identifier.of(ID, RuneCraftingBlock.NAME), RuneCraftingBlock.ITEM);
        for(var entry: RuneItems.entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        if (FabricLoader.getInstance().isModLoaded("bundleapi")) {
            RunePouches.register();
        }
    }
}