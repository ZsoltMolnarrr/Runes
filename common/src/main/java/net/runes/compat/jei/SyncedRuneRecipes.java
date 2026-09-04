package net.runes.compat.jei;

import net.minecraft.world.item.crafting.RecipeHolder;
import net.runes.crafting.RuneCraftingRecipe;

import java.util.List;
import java.util.function.Supplier;

/**
 * Client-side access to the altar recipes, for {@link RunesJeiPlugin}.
 * <p>
 * Since 1.21.2 the server sends the client no recipes at all — {@code ClientRecipeContainer} carries only
 * property sets and stonecutter recipes — so a viewer running on the client has nothing to read. JEI has no
 * recipe enumeration of its own and only opts vanilla serializers into sync, so Runes has to opt itself in.
 * Each loader does that differently (Fabric: {@code RecipeSynchronization}, a pull API; NeoForge:
 * {@code OnDatapackSyncEvent#sendRecipes}, push-only), so this holder is the seam between them: the
 * per-platform {@code compat.jei} classes install a source, and the plugin just reads it.
 * <p>
 * REI needs none of this — it runs its own display sync off the server-side recipe manager.
 * <p>
 * Empty until a source is installed and the client has actually received the sync, which is why callers must
 * tolerate an empty list rather than treating it as an error.
 */
public final class SyncedRuneRecipes {
    private static volatile Supplier<List<RecipeHolder<RuneCraftingRecipe>>> source = List::of;

    private SyncedRuneRecipes() {}

    /** Installed once per platform, from the client initializer. */
    public static void setSource(Supplier<List<RecipeHolder<RuneCraftingRecipe>>> supplier) {
        source = supplier;
    }

    /** The altar recipes the server has synced, or an empty list when not connected / not yet synced. */
    public static List<RecipeHolder<RuneCraftingRecipe>> get() {
        return source.get();
    }
}
