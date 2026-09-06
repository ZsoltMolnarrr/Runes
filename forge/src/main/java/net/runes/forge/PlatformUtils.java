package net.runes.forge;

import net.minecraftforge.fml.loading.LoadingModList;

public class PlatformUtils {
    public static boolean isModLoaded(String modid) {
        // LoadingModList (not ModList): populated during mod discovery, before any constructor runs,
        // so early compat gates in static initializers / init match Fabric's "resolved up front" timing.
        return LoadingModList.get().getModFileById(modid) != null;
    }
}
