package net.runes.neoforge;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.runes.RunesMod;
import net.runes.api.RuneItems;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.BundleApiCompat;

@Mod(RunesMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        // Creative-tab placement — NeoForge mod-bus event (replaces Fabric API's ItemGroupEvents).
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeMod::buildTabContents);
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, reg -> {
            RunesMod.registerSounds();
        });
        event.register(Registries.RECIPE_TYPE, reg -> {
            RunesMod.registerRecipeType();
        });
        event.register(Registries.MENU, reg -> {
            RunesMod.registerScreenHandler();
        });
        event.register(Registries.BLOCK, reg -> {
            RunesMod.registerBlocks();
        });
        event.register(Registries.ITEM, reg -> {
            RunesMod.registerItems();
            BundleApiCompat.register(() -> PlatformUtils.isModLoaded(BundleApiCompat.MOD_ID));
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            event.accept(RuneCraftingBlock.ITEM);
        } else if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            for (var entry : RuneItems.entries) {
                event.accept(entry.item());
            }
            // RunePouches is reached reflectively only: it may not even be compiled in (see BundleApiCompat).
            for (var item : BundleApiCompat.pouchItems(() -> PlatformUtils.isModLoaded(BundleApiCompat.MOD_ID))) {
                event.accept(item);
            }
        }
    }
}
