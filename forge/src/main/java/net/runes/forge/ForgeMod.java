package net.runes.forge;

import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
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
import net.runes.crafting.RuneCrafting;
import net.runes.crafting.RuneCraftingBlock;
import net.runes.crafting.RuneCraftingRecipe;
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

    /// Registration is duplicated here rather than delegated to `common`'s `registerX()` methods, because a
    /// plain `Registry.register` is not usable on this loader: Forge only clears the vanilla registry's own
    /// lock from 47.4.0 onwards, so on 47.0-47.3 and NeoForge 1.20.1 it throws "Can not register to a locked
    /// registry" even inside the correct `RegisterEvent` window, and `mods.toml` declares `[47,)`. The helper
    /// this event hands out is the API every build of that range sanctions, so Forge iterates the same content
    /// `common` exposes and registers it itself. `common` keeps its vanilla-shaped registration for Fabric.
    ///
    /// `event.register` is a no-op unless its key matches the event's registry, so every block is declared
    /// unconditionally; Forge posts one event per registry and each block runs in exactly its own window.
    /// In particular the recipe TYPE (event 23) and the recipe SERIALIZER (event 24) live in two different
    /// registries and must not share a window — NeoForge was lenient about this, Forge 47 is not.
    ///
    /// `Block` and `Item` constructors take an intrusive registry holder, so the objects are also *created*
    /// inside their own window: touching `RuneItems`/`RunePouches` outside the ITEM window would throw.
    @SuppressWarnings("removal")
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, helper ->
                helper.register(RuneCrafting.ID, RuneCrafting.SOUND));

        // The same TYPE instance `RuneCraftingRecipe#getType` returns — registering a fresh one here would
        // silently break recipe lookup.
        event.register(RegistryKeys.RECIPE_TYPE, helper ->
                helper.register(RuneCrafting.ID, RuneCraftingRecipe.TYPE));

        event.register(RegistryKeys.RECIPE_SERIALIZER, helper ->
                helper.register(RuneCrafting.ID, RuneCraftingRecipe.SERIALIZER));

        event.register(RegistryKeys.SCREEN_HANDLER, helper -> {
            // The `ScreenHandlerType` constructor is vanilla-private on 1.20.1; Forge's public
            // `IForgeMenuType.create` is the loader's way in (Fabric uses an access widener).
            // `IContainerFactory` also handles the null extra-data of a vanilla open packet, which is
            // what `SimpleNamedScreenHandlerFactory` sends.
            RuneCraftingScreenHandler.HANDLER_TYPE = IForgeMenuType.create(
                    (syncId, inventory, buf) -> new RuneCraftingScreenHandler(syncId, inventory, buf));
            helper.register(new Identifier(RunesMod.ID, RuneCraftingRecipe.NAME),
                    RuneCraftingScreenHandler.HANDLER_TYPE);
        });

        event.register(RegistryKeys.BLOCK, helper ->
                helper.register(new Identifier(RunesMod.ID, RuneCraftingBlock.NAME), RuneCraftingBlock.INSTANCE));

        event.register(RegistryKeys.ITEM, helper -> {
            // The altar's BlockItem comes along in this window too.
            helper.register(new Identifier(RunesMod.ID, RuneCraftingBlock.NAME), RuneCraftingBlock.ITEM);
            for (var entry : RuneItems.entries) {
                helper.register(entry.id(), entry.item());
            }
            // Gate BEFORE touching RunePouches — see buildTabContents for why.
            if (PlatformUtils.isModLoaded("bundleapi")) {
                RunePouches.create();
                for (var entry : RunePouches.entries) {
                    helper.register(entry.id(), entry.item());
                }
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
