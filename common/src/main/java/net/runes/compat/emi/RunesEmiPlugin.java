package net.runes.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.runes.crafting.RuneCrafting;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingRecipe;

/**
 * Surfaces {@link RuneCraftingRecipe}s — the altar-only recipes, which are a custom recipe type
 * EMI knows nothing about — under their own category, worked at the Rune Crafting Altar.
 * (The `*_hand.json` shapeless variants are vanilla recipes; EMI already lists those itself.)
 * <p>
 * Loaded reflectively by EMI only: Fabric via the `emi` entrypoint in `fabric.mod.json`,
 * NeoForge via the {@link EmiEntrypoint} annotation scan. Nothing in Runes references this
 * class, so it is never class-loaded when EMI is absent.
 */
@EmiEntrypoint
@Environment(EnvType.CLIENT)
public class RunesEmiPlugin implements EmiPlugin {
    /** Category id {@code runes:crafting} → name key {@code emi.category.runes.crafting}. */
    public static final EmiStack ALTAR = EmiStack.of(RuneCraftingBlock.ITEM);
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            RuneCrafting.ID, ALTAR, ALTAR, EmiRecipeSorting.compareOutputThenInput());

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, ALTAR);

        for (var entry : registry.getRecipeManager().listAllOfType(RuneCraftingRecipe.TYPE)) {
            registry.addRecipe(new RuneCraftingEmiRecipe(entry.id(), entry.value()));
        }
    }
}
