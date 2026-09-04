package net.runes.fabric.compat.jei;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.runes.crafting.RuneCrafting;

/**
 * Opts the altar recipes into Fabric's recipe sync, so recipe viewers can see them.
 * <p>
 * Since 1.21.2 vanilla syncs no recipes to the client, and JEI opts in vanilla serializers only — see
 * {@link net.runes.compat.jei.SyncedRuneRecipes}. This has to run on both sides at init, so it is its own
 * {@code main} entrypoint rather than part of the JEI plugin, which only loads on the client and only when
 * JEI is installed.
 */
public final class JeiRecipeSync implements ModInitializer {
    @Override
    public void onInitialize() {
        RecipeSynchronization.synchronizeRecipeSerializer(RuneCrafting.RECIPE_SERIALIZER);
    }
}
