package net.runes.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.runes.client.RuneCraftingScreen;
import net.runes.client.RunesClientMod;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingScreenHandler;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RunesClientMod.init();

        // Screen registration — `HandledScreens.register` is vanilla-private on 1.20.1; Fabric API's
        // `fabric-screen-handler-api-v1` access widener opens it for this module (Forge patches it public).
        HandledScreens.register(RuneCraftingScreenHandler.HANDLER_TYPE, RuneCraftingScreen::new);

        // Fabric-specific render layer registration (Forge reads `render_type` from the block model).
        BlockRenderLayerMap.INSTANCE.putBlock(RuneCraftingBlock.INSTANCE, RenderLayer.getCutout());
    }
}
