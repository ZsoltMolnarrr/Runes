package net.runes.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroups;
import net.runes.RunesMod;
import net.runes.api.RuneItems;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RunePouches;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        RunesMod.registerSounds();
        RunesMod.registerRecipeType();
        RunesMod.registerScreenHandler();
        RunesMod.registerBlocks();
        RunesMod.registerItems();
        if (FabricLoader.getInstance().isModLoaded("bundleapi")) {
            RunePouches.register();
        }

        // Creative-tab placement — Fabric API (loader-specific; NeoForge uses BuildCreativeModeTabContentsEvent).
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content ->
                content.add(RuneCraftingBlock.ITEM));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            for (var entry : RuneItems.entries) {
                content.add(entry.item());
            }
            for (var entry : RunePouches.entries) {
                content.add(entry.item());
            }
        });
    }
}
