package net.runes.forge.client;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.runes.client.RuneCraftingScreen;
import net.runes.client.RunesClientMod;
import net.runes.crafting.RuneCraftingScreenHandler;

public class ForgeClientMod {
    /// Registered from `ForgeMod`'s constructor, behind `FMLEnvironment.dist == Dist.CLIENT`.
    /// Forge 47 has no `RegisterMenuScreensEvent` (that is NeoForge-only): on 1.20.1 the screen goes in
    /// through the plain, public `HandledScreens.register`, enqueued onto the client thread from
    /// `FMLClientSetupEvent` — which runs after every `RegisterEvent` window, so the handler type exists.
    ///
    /// The altar's block model carries `"render_type": "minecraft:cutout"`, a Forge model extension, so
    /// no render-layer registration is needed here (only Fabric needs `BlockRenderLayerMap`).
    public static void register(IEventBus modBus) {
        modBus.addListener(ForgeClientMod::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RunesClientMod.init();
            HandledScreens.register(RuneCraftingScreenHandler.HANDLER_TYPE, RuneCraftingScreen::new);
        });
    }
}
