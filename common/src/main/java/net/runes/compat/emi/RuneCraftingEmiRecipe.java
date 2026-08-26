package net.runes.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.Identifier;
import net.runes.crafting.RuneCraftingRecipe;

import java.util.List;

/**
 * Display of a single altar recipe: base + addition → result.
 * Laid out like EMI's own two-input recipes (anvil repairing).
 */
public class RuneCraftingEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiIngredient base;
    private final EmiIngredient addition;
    private final EmiStack result;

    public RuneCraftingEmiRecipe(Identifier id, RuneCraftingRecipe recipe) {
        this.id = id;
        this.base = EmiIngredient.of(recipe.base());
        this.addition = EmiIngredient.of(recipe.addition());
        this.result = EmiStack.of(recipe.result());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return RunesEmiPlugin.CATEGORY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(base, addition);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(result);
    }

    @Override
    public int getDisplayWidth() {
        return 125;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.PLUS, 27, 3);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 1);
        widgets.addSlot(base, 0, 0);
        widgets.addSlot(addition, 49, 0);
        widgets.addSlot(result, 107, 0).recipeContext(this);
    }
}
