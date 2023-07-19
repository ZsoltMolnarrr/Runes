package net.runes.crafting;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;

public class RuneCrafting {
    public static Identifier ID = new Identifier(RunesMod.ID, "crafting");
    public static SoundEvent SOUND = SoundEvent.of(ID);
    public static int SOUND_DELAY = 20;
}
