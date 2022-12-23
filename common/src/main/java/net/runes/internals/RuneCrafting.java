package net.runes.internals;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;

public class RuneCrafting {
    public static Identifier ID = new Identifier(RunesMod.ID, "crafting");
    public static SoundEvent SOUND = new SoundEvent(ID);
}
