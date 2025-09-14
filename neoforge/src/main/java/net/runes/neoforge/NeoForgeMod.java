package net.runes.neoforge;

import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.runes.RunesMod;

@Mod(RunesMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        // Run our common setup.
        RunesMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
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
        });
    }
}
