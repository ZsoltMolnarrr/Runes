package net.runes.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.runes.client.RuneCraftingScreen;
import net.runes.client.RunesClientMod;
import net.runes.crafting.RuneCraftingScreenHandler;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RunesClientMod.init();

        // Screen registration — Fabric API (loader-specific; NeoForge uses RegisterMenuScreensEvent).
        MenuScreens.register(RuneCraftingScreenHandler.HANDLER_TYPE, RuneCraftingScreen::new);
        // 26.1: the chunk section layer is derived automatically from the block model; no BlockRenderLayerMap call.
    }
}
