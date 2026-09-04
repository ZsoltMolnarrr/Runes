package net.runes.fabric.compat.jei;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.runes.compat.jei.SyncedRuneRecipes;
import net.runes.crafting.RuneCrafting;

import java.util.List;

/**
 * Installs the client-side recipe source JEI reads.
 * <p>
 * Fabric exposes the synced recipes as a pull API on the client's recipe manager, so there is nothing to
 * cache or invalidate — an empty world simply yields an empty list.
 */
public final class JeiRecipeSourceClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SyncedRuneRecipes.setSource(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return List.of();
            }
            return List.copyOf(level.recipeAccess().getSynchronizedRecipes().getAllOfType(RuneCrafting.RECIPE_TYPE));
        });
    }
}
