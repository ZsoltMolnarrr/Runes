package net.runes.compat.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;
import net.runes.crafting.RuneCrafting;
import net.runes.crafting.RuneCraftingRecipe;

/**
 * Turns the altar's {@link RuneCraftingRecipe}s into REI displays, on the logical server.
 * <p>
 * This has to run server-side: since 1.21.2 the client receives no recipes, so REI reads the real
 * {@code RecipeManager} on the server and syncs the resulting displays to every player who has REI.
 * Consequently the altar recipes only show up when Runes and REI are both installed server-side too —
 * which is the normal setup, since Runes must be present for the recipes to exist at all.
 * <p>
 * Loaded reflectively by REI only: Fabric via the {@code rei_common} entrypoint in {@code fabric.mod.json},
 * NeoForge via the {@code @REIPluginCommon} subclass in the neoforge module.
 */
public class RunesReiCommonPlugin implements REICommonPlugin {
    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(RuneCraftingRecipe.class)
                .filterType(RuneCraftingRecipe.TYPE)
                .fill(RuneCraftingDisplay::new);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        // Runs on both sides: the server encodes displays with this, the client decodes them.
        registry.register(Identifier.of(RunesMod.ID, RuneCrafting.NAME), RuneCraftingDisplay.SERIALIZER);
    }
}
