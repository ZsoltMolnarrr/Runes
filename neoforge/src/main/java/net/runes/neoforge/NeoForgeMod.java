package net.runes.neoforge;

import net.neoforged.fml.common.Mod;

import net.runes.RunesMod;

@Mod(RunesMod.ID)
public final class NeoForgeMod {
    public NeoForgeMod() {
        // Run our common setup.
        RunesMod.init();
    }
}
