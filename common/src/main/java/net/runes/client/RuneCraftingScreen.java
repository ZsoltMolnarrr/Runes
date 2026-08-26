package net.runes.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.runes.RunesMod;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingScreenHandler;

public class RuneCraftingScreen extends ItemCombinerScreen<RuneCraftingScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(RunesMod.ID, "textures/gui/" + RuneCraftingBlock.NAME + ".png");

    public RuneCraftingScreen(RuneCraftingScreenHandler handler, Inventory playerInventory, Component title) {
        super(handler, playerInventory, title, TEXTURE);
        this.titleLabelX = 60;
        this.titleLabelY = 18;
    }

    @Override
    protected void renderErrorIcon(GuiGraphics context, int x, int y) {
//        if (this.hasInvalidRecipe()) {
//            context.drawTexture(TEXTURE, x + 65, y + 46, this.backgroundWidth, 0, 28, 21);
//        }
    }
}
