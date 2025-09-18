package net.runes.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.runes.client.RunesClientMod;
import net.runes.crafting.RuneCraftingBlock;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RunesClientMod.init();

        // Fabric-specific render layer registration
        BlockRenderLayerMap.INSTANCE.putBlock(RuneCraftingBlock.INSTANCE, RenderLayer.getCutout());
    }
}
