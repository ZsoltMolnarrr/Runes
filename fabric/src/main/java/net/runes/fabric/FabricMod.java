package net.runes.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroups;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.runes.crafting.RuneCraftingScreenHandler;
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
        RunesMod.registerRecipeSerializer();
        // The `ScreenHandlerType` constructor is vanilla-private on 1.20.1; Fabric API's
        // `fabric-screen-handler-api-v1` access widener opens it for this module.
        RuneCraftingScreenHandler.HANDLER_TYPE =
                new ScreenHandlerType<>(RuneCraftingScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
        RunesMod.registerScreenHandler();
        RunesMod.registerBlocks();
        RunesMod.registerItems();
        if (FabricLoader.getInstance().isModLoaded("bundleapi")) {
            RunePouches.register();
        }

        // Creative-tab placement — Fabric API (loader-specific; Forge uses BuildCreativeModeTabContentsEvent).
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content ->
                content.add(RuneCraftingBlock.ITEM));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            for (var entry : RuneItems.entries) {
                content.add(entry.item());
            }
            // Gate BEFORE touching RunePouches: reading the static field forces the JVM to
            // link/verify RunePouches, whose factory references BundleAPI's CustomBundleItem.
            // Without this guard that verification fails with NoClassDefFoundError when
            // BundleAPI is absent — the empty `entries` list never even gets iterated.
            if (FabricLoader.getInstance().isModLoaded("bundleapi")) {
                for (var entry : RunePouches.entries) {
                    content.add(entry.item());
                }
            }
        });
    }
}
