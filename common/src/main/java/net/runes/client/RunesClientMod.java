package net.runes.client;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingScreenHandler;

public class RunesClientMod {
    public static void init() {
        HandledScreens.register(RuneCraftingScreenHandler.HANDLER_TYPE, RuneCraftingScreen::new);
    }
}
