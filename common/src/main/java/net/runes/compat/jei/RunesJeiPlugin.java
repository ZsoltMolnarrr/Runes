package net.runes.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;
import net.runes.crafting.RuneCraftingBlock;

/**
 * Surfaces {@link net.runes.crafting.RuneCraftingRecipe}s — the altar-only recipes, which are a custom recipe
 * type JEI knows nothing about — under their own category, worked at the Rune Crafting Altar.
 * (The {@code *_hand.json} shapeless variants are vanilla recipes; JEI already lists those itself.)
 * <p>
 * JEI has no recipe enumeration of its own, and vanilla stopped syncing recipes to the client in 1.21.2, so
 * the recipe list comes from {@link SyncedRuneRecipes} — see there for how each platform fills it.
 * <p>
 * Loaded reflectively by JEI only: Fabric via the {@code jei_mod_plugin} entrypoint in {@code fabric.mod.json},
 * NeoForge via the {@link JeiPlugin} annotation scan. Nothing in Runes references this class, so it is never
 * class-loaded when JEI is absent.
 */
@JeiPlugin
public class RunesJeiPlugin implements IModPlugin {
    private static final Identifier UID = Identifier.of(RunesMod.ID, "jei");

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RuneCraftingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RuneCraftingCategory.TYPE, SyncedRuneRecipes.get());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(RuneCraftingCategory.TYPE, RuneCraftingBlock.ITEM);
    }
}
