package net.runes.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.runes.crafting.RuneCraftingBlock;

/**
 * Registers the Rune Crafting category and its workstation.
 * <p>
 * Deliberately does not register displays: those are built server-side by {@link RunesReiCommonPlugin}
 * and arrive over REI's display sync.
 * <p>
 * Loaded reflectively by REI only: Fabric via the {@code rei_client} entrypoint in {@code fabric.mod.json},
 * NeoForge via the {@code @REIPluginClient} subclass in the neoforge module.
 */
@Environment(EnvType.CLIENT)
public class RunesReiClientPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new RuneCraftingCategory());
        registry.addWorkstations(RuneCraftingDisplay.CATEGORY, EntryStacks.of(RuneCraftingBlock.ITEM));
    }
}
