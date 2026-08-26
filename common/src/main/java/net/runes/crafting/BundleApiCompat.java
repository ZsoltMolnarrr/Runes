package net.runes.crafting;

import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.world.item.Item;

/**
 * Reflective bridge to {@link RunePouches}, which depends on Bundle API.
 * <p>
 * Bundle API has no build for this Minecraft version yet, so {@code RunePouches.java} is excluded from
 * compilation unless {@code enable_bundle_api=true} in {@code gradle.properties}. Platform entrypoints
 * must therefore never reference {@code RunePouches} directly: they go through this class, which only
 * touches it by name — and only once the loader confirms Bundle API is present.
 */
public final class BundleApiCompat {
    public static final String MOD_ID = "bundleapi";
    private static final String POUCHES_CLASS = "net.runes.crafting.RunePouches";

    private BundleApiCompat() { }

    /** Registers the rune pouch items, if Bundle API is loaded AND the pouch code was compiled in. */
    public static void register(BooleanSupplier isBundleApiLoaded) {
        if (!isBundleApiLoaded.getAsBoolean()) return;
        try {
            Class.forName(POUCHES_CLASS).getMethod("register").invoke(null);
        } catch (ClassNotFoundException e) {
            // Bundle API present at runtime but pouch support was built out (enable_bundle_api=false).
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to register rune pouches", e);
        }
    }

    /** Registered pouch items for creative-tab placement; empty when pouches are unavailable. */
    @SuppressWarnings("unchecked")
    public static List<Item> pouchItems(BooleanSupplier isBundleApiLoaded) {
        if (!isBundleApiLoaded.getAsBoolean()) return List.of();
        try {
            var entries = (List<Object>) Class.forName(POUCHES_CLASS).getField("entries").get(null);
            return entries.stream().map(entry -> {
                try {
                    return (Item) entry.getClass().getMethod("item").invoke(entry);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        } catch (ClassNotFoundException e) {
            return List.of();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to read rune pouches", e);
        }
    }
}
