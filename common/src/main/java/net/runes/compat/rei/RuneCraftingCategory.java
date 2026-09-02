package net.runes.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.runes.crafting.RuneCraftingBlock;

import java.util.ArrayList;
import java.util.List;

/** Laid out like REI's own two-input categories: base + addition → result. */
@Environment(EnvType.CLIENT)
public class RuneCraftingCategory implements DisplayCategory<RuneCraftingDisplay> {
    @Override
    public CategoryIdentifier<? extends RuneCraftingDisplay> getCategoryIdentifier() {
        return RuneCraftingDisplay.CATEGORY;
    }

    /** Reuses the altar screen's own title, so no new lang key needs translating. */
    @Override
    public Text getTitle() {
        return Text.translatable("gui.runes.rune_crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(RuneCraftingBlock.ITEM);
    }

    @Override
    public int getDisplayHeight() {
        return 49;
    }

    @Override
    public List<Widget> setupDisplay(RuneCraftingDisplay display, Rectangle bounds) {
        // base [+] addition [->] result, centred in the panel.
        Point start = new Point(bounds.getCenterX() - 47, bounds.getCenterY() - 9);
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createSlot(new Point(start.x, start.y))
                .entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(start.x + 22, start.y))
                .entries(display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createArrow(new Point(start.x + 44, start.y)));
        widgets.add(Widgets.createResultSlotBackground(new Point(start.x + 76, start.y)));
        widgets.add(Widgets.createSlot(new Point(start.x + 76, start.y))
                .entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }
}
