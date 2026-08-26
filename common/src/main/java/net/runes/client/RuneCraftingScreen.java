package net.runes.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingScreenHandler;

public class RuneCraftingScreen extends ForgingScreen<RuneCraftingScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(RunesMod.ID, "textures/gui/" + RuneCraftingBlock.NAME + ".png");

    public RuneCraftingScreen(RuneCraftingScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, TEXTURE);
        this.titleX = 60;
        this.titleY = 18;
    }

    @Override
    protected void drawInvalidRecipeArrow(DrawContext context, int x, int y) {
//        if (this.hasInvalidRecipe()) {
//            context.drawTexture(TEXTURE, x + 65, y + 46, this.backgroundWidth, 0, 28, 21);
//        }
    }
}
