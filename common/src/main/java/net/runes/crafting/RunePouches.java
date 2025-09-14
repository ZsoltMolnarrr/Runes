package net.runes.crafting;

import com.github.theredbrain.bundleapi.BundleAPI;
import com.github.theredbrain.bundleapi.component.type.CustomBundleContentsComponent;
import com.github.theredbrain.bundleapi.item.CustomBundleItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.runes.RunesMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RunePouches {
    private static final TagKey<Item> RUNES = TagKey.of(Registries.ITEM.getKey(), Identifier.of(RunesMod.ID, "runes"));

    public static final List<Entry> entries = new ArrayList<>();
    public record Entry(Identifier id, int capacity, Item item) {  }
    public static Entry entry(String name, int capacity, @Nullable Rarity rarity) {
        var settings = new Item.Settings()
                .maxCount(1)
                .component(
                        DataComponentTypes.LORE,
                        new LoreComponent(List.of(
                                Text.translatable("item.runes.rune_pouch.hint")
                                        .formatted(Formatting.GRAY)
                        ))
                )
                .component(
                        BundleAPI.CUSTOM_BUNDLE_CONTENTS_COMPONENT,
                        CustomBundleContentsComponent.builder().size_multiplier(capacity).build()
                );
        if (rarity != null) {
            settings.rarity(rarity);
        }
        var bundle = new CustomBundleItem(RUNES, settings);
        var id = Identifier.of(RunesMod.ID, name);
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
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            for(var entry: entries) {
                content.add(entry.item());
            }
        });
    }
}
