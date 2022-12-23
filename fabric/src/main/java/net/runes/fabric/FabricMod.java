package net.runes.fabric;

import net.runes.RunesMod;
import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        RunesMod.init();
    }
}