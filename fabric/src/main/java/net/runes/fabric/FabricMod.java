package net.runes.fabric;

import net.fabricmc.api.ModInitializer;

import net.runes.RunesMod;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        RunesMod.init();
        RunesMod.registerSounds();
        RunesMod.registerRecipeType();
        RunesMod.registerScreenHandler();
        RunesMod.registerBlocks();
        RunesMod.registerItems();
    }
}
