package net.runes.neoforge;

import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.runes.RunesMod;
import net.runes.api.RuneItems;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RunePouches;

@Mod(RunesMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        // Creative-tab placement — NeoForge mod-bus event (replaces Fabric API's ItemGroupEvents).
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeMod::buildTabContents);
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            RunesMod.registerSounds();
        });
        event.register(RegistryKeys.RECIPE_TYPE, reg -> {
            RunesMod.registerRecipeType();
        });
        event.register(RegistryKeys.SCREEN_HANDLER, reg -> {
            RunesMod.registerScreenHandler();
        });
        event.register(RegistryKeys.BLOCK, reg -> {
            RunesMod.registerBlocks();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            RunesMod.registerItems();
            if (PlatformUtils.isModLoaded("bundleapi")) {
                RunePouches.register();
            }
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ItemGroups.FUNCTIONAL)) {
            event.add(RuneCraftingBlock.ITEM);
        } else if (event.getTabKey().equals(ItemGroups.COMBAT)) {
            for (var entry : RuneItems.entries) {
                event.add(entry.item());
            }
            // Gate BEFORE touching RunePouches: reading the static field forces the JVM to
            // link/verify RunePouches, whose factory references BundleAPI's CustomBundleItem.
            // Without this guard that verification fails with NoClassDefFoundError when
            // BundleAPI is absent — the empty `entries` list never even gets iterated.
            if (PlatformUtils.isModLoaded("bundleapi")) {
                for (var entry : RunePouches.entries) {
                    event.add(entry.item());
                }
            }
        }
    }
}
