package net.runes.crafting;

import com.github.theredbrain.bundleapi.item.CustomBundleItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import net.runes.RunesMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RunePouches {
    private static final TagKey<Item> RUNES = TagKey.of(Registries.ITEM.getKey(), new Identifier(RunesMod.ID, "runes"));

    public static final List<Entry> entries = new ArrayList<>();
    public record Entry(Identifier id, int capacity, Item item) {  }

    /// A rune pouch: a whitelist bundle that also carries the "holds runes" hint line.
    ///
    /// 1.20.1 / BundleAPI 1.1 has no data components, so:
    /// - the capacity multiplier is a CONSTRUCTOR argument (it used to ride in
    ///   `Item.Settings#component(CUSTOM_BUNDLE_CONTENTS, …)`), and
    /// - the hint line is an `appendTooltip` override (there is no `LoreComponent` to set as an
    ///   item default).
    public static class RunePouchItem extends CustomBundleItem {
        public RunePouchItem(@Nullable TagKey<Item> tag, int sizeMultiplier, Settings settings) {
            super(tag, sizeMultiplier, settings);
        }

        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            super.appendTooltip(stack, world, tooltip, context);
            tooltip.add(Text.translatable("item.runes.rune_pouch.hint").formatted(Formatting.GRAY));
        }
    }

    public static Entry entry(String name, int capacity, @Nullable Rarity rarity) {
        var settings = new Item.Settings().maxCount(1);
        if (rarity != null) {
            settings.rarity(rarity);
        }
        var bundle = new RunePouchItem(RUNES, capacity, settings);
        var id = new Identifier(RunesMod.ID, name);
        var entry = new Entry(id, capacity, bundle);
        entries.add(entry);
        return entry;
    }

    public static void register() {
        entry("small_rune_pouch", 4, null);
        entry("medium_rune_pouch", 8, null);
        entry("large_rune_pouch", 12, Rarity.UNCOMMON);

        for(var entry: entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        // Creative-tab placement (COMBAT group) is registered per-platform from each loader's entrypoint,
        // iterating RunePouches.entries — no Fabric API ItemGroupEvents in common.
    }
}
