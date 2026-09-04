package net.runes.neoforge.compat.jei;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.runes.RunesMod;
import net.runes.compat.jei.SyncedRuneRecipes;
import net.runes.crafting.RuneCrafting;

import java.util.List;

/**
 * Installs the client-side recipe source JEI reads.
 * <p>
 * Unlike Fabric, NeoForge only pushes the recipes once per sync and offers no way to read them back, so they
 * have to be cached here — and dropped on disconnect, so a later session cannot show a previous server's
 * recipes.
 */
@EventBusSubscriber(modid = RunesMod.ID, value = Dist.CLIENT)
public final class JeiRecipeSourceClient {
    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        var recipes = List.copyOf(event.getRecipeMap().byType(RuneCrafting.RECIPE_TYPE));
        SyncedRuneRecipes.setSource(() -> recipes);
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        SyncedRuneRecipes.setSource(List::of);
    }
}
