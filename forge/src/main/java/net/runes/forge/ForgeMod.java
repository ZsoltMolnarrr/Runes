package net.runes.forge;

import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import net.runes.RunesMod;
import net.runes.api.RuneItems;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingScreenHandler;
import net.runes.crafting.RunePouches;
import net.runes.forge.client.ForgeClientMod;

@Mod(RunesMod.ID)
public final class ForgeMod {
    @SuppressWarnings("removal")
    public ForgeMod() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        // Creative-tab placement — Forge mod-bus event (replaces Fabric API's ItemGroupEvents).
        modBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class, ForgeMod::buildTabContents);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientMod.register(modBus);
        }
    }

    /// Forge 47 unfreezes exactly one registry per `RegisterEvent` window, so every registry gets its own.
    /// In particular the recipe TYPE and the recipe SERIALIZER live in two different registries and must
    /// not share a window (NeoForge was lenient about this; Forge 47 is not).
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            RunesMod.registerSounds();
        });
        event.register(RegistryKeys.RECIPE_TYPE, reg -> {
            RunesMod.registerRecipeType();
        });
        event.register(RegistryKeys.RECIPE_SERIALIZER, reg -> {
            RunesMod.registerRecipeSerializer();
        });
        event.register(RegistryKeys.SCREEN_HANDLER, reg -> {
            // The `ScreenHandlerType` constructor is vanilla-private on 1.20.1; Forge's public
            // `IForgeMenuType.create` is the loader's way in (Fabric uses an access widener).
            // `IContainerFactory` also handles the null extra-data of a vanilla open packet, which is
            // what `SimpleNamedScreenHandlerFactory` sends.
            RuneCraftingScreenHandler.HANDLER_TYPE = IForgeMenuType.create(
                    (syncId, inventory, buf) -> new RuneCraftingScreenHandler(syncId, inventory, buf));
            RunesMod.registerScreenHandler();
        });
        event.register(RegistryKeys.BLOCK, reg -> {
            RunesMod.registerBlocks();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            // The altar's BlockItem comes along in this window too.
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
