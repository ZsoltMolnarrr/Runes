package net.runes.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.runes.client.RuneCraftingScreen;
import net.runes.client.RunesClientMod;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingScreenHandler;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RunesClientMod.init();

        // Screen registration — Fabric API (loader-specific; NeoForge uses RegisterMenuScreensEvent).
        MenuScreens.register(RuneCraftingScreenHandler.HANDLER_TYPE, RuneCraftingScreen::new);

        // Fabric-specific render layer registration
        BlockRenderLayerMap.putBlock(RuneCraftingBlock.INSTANCE, ChunkSectionLayer.CUTOUT);
    }
}
