package net.runes.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.runes.RunesMod;
import net.runes.client.RuneCraftingScreen;
import net.runes.client.RunesClientMod;
import net.runes.crafting.RuneCraftingScreenHandler;

@EventBusSubscriber(modid = RunesMod.ID, value = Dist.CLIENT)
public class NeoForgeClientMod {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RunesClientMod.init();
    }

    // Screen registration — NeoForge mod-bus event (replaces Fabric API's HandledScreens.register).
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(RuneCraftingScreenHandler.HANDLER_TYPE, RuneCraftingScreen::new);
    }
}
