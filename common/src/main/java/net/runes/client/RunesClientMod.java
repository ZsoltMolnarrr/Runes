package net.runes.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingScreenHandler;

public class RunesClientMod {
    public static void initialize() {
        HandledScreens.register(RuneCraftingScreenHandler.HANDLER_TYPE, RuneCraftingScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(RuneCraftingBlock.INSTANCE, RenderLayer.getCutout());
    }
}
