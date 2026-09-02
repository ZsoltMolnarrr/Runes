package net.runes.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.text.Text;
import net.runes.crafting.RuneCrafting;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingRecipe;

/** Laid out like JEI's own two-input categories: base + addition → result. */
public class RuneCraftingCategory extends AbstractRecipeCategory<RecipeEntry<RuneCraftingRecipe>> {
    /**
     * Derives the JEI type id from the vanilla recipe type's registry key, i.e. {@code runes:crafting}.
     * That lookup throws if the recipe type is not registered yet — safe here, because JEI only ever loads
     * this class long after mod init.
     */
    public static final IRecipeHolderType<RuneCraftingRecipe> TYPE = IRecipeHolderType.create(RuneCrafting.RECIPE_TYPE);

    public RuneCraftingCategory(IGuiHelper guiHelper) {
        // Reuses the altar screen's own title, so no new lang key needs translating.
        super(TYPE,
                Text.translatable("gui.runes.rune_crafting"),
                guiHelper.createDrawableItemLike(RuneCraftingBlock.ITEM),
                124, 18);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeEntry<RuneCraftingRecipe> entry, IFocusGroup focuses) {
        RuneCraftingRecipe recipe = entry.value();
        builder.addInputSlot(1, 1).setStandardSlotBackground().add(recipe.base());
        builder.addInputSlot(28, 1).setStandardSlotBackground().add(recipe.addition());
        builder.addOutputSlot(103, 1).setOutputSlotBackground().add(recipe.result());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeEntry<RuneCraftingRecipe> entry, IFocusGroup focuses) {
        builder.addRecipeArrowWidget().setPosition(54, 1);
    }
}
