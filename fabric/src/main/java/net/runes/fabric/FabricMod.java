package net.runes.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.CreativeModeTabs;
import net.runes.RunesMod;
import net.runes.api.RuneItems;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.BundleApiCompat;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        RunesMod.registerSounds();
        RunesMod.registerRecipeType();
        RunesMod.registerScreenHandler();
        RunesMod.registerBlocks();
        RunesMod.registerItems();
        BundleApiCompat.register(() -> FabricLoader.getInstance().isModLoaded(BundleApiCompat.MOD_ID));

        // Creative-tab placement — Fabric API (loader-specific; NeoForge uses BuildCreativeModeTabContentsEvent).
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content ->
                content.accept(RuneCraftingBlock.ITEM));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(content -> {
            for (var entry : RuneItems.entries) {
                content.accept(entry.item());
            }
            // RunePouches is reached reflectively only: it may not even be compiled in (see BundleApiCompat).
            for (var item : BundleApiCompat.pouchItems(() -> FabricLoader.getInstance().isModLoaded(BundleApiCompat.MOD_ID))) {
                content.accept(item);
            }
        });
    }
}
