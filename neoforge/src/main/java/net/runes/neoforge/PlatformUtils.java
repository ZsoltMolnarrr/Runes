package net.runes.neoforge;

import net.neoforged.fml.loading.FMLLoader;

public class PlatformUtils {
    public static boolean isModLoaded(String modid) {
        // LoadingModList (not ModList): populated during mod discovery, before any constructor runs,
        // so early compat gates in static initializers / init match Fabric's "resolved up front" timing.
        // FML 11: `LoadingModList.get()` is deprecated for removal in favour of the FMLLoader accessor.
        return FMLLoader.getCurrent().getLoadingModList().getModFileById(modid) != null;
    }
}
