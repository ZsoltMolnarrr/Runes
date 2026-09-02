package net.runes.neoforge.compat.jei;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.runes.RunesMod;
import net.runes.crafting.RuneCrafting;

/**
 * Asks NeoForge to send the altar recipes to clients, so recipe viewers can see them.
 * <p>
 * Since 1.21.2 vanilla syncs no recipes to the client, and JEI requests vanilla types only — see
 * {@link net.runes.compat.jei.SyncedRuneRecipes}. Registered by NeoForge's annotation scan, so nothing in
 * the mod proper has to know this exists.
 */
@EventBusSubscriber(modid = RunesMod.ID)
public final class JeiRecipeSync {
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(RuneCrafting.RECIPE_TYPE);
    }
}
