package net.runes.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.runes.client.RunesClientMod;

public final class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RunesClientMod.init();
    }
}
