package net.runes.forge;

import net.minecraftforge.fml.ModList;
import net.runes.Platform;

import static net.runes.Platform.Type.FORGE;

public class PlatformImpl {
    public static Platform.Type getPlatformType() {
        return FORGE;
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
