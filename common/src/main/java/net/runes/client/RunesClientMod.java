package net.runes.client;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.runes.internals.RuneCraftingScreenHandler;

public class RunesClientMod {
    public static void initialize() {
        HandledScreens.register(RuneCraftingScreenHandler.HANDLER_TYPE, RuneCraftingScreen::new);
    }
}
