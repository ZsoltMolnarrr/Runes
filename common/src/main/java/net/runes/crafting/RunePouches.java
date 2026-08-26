package net.runes.crafting;

import com.github.theredbrain.bundleapi.BundleAPI;
import com.github.theredbrain.bundleapi.component.type.CustomBundleContentsComponent;
import com.github.theredbrain.bundleapi.item.CustomBundleItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
import net.runes.RunesMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RunePouches {
    private static final TagKey<Item> RUNES = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(RunesMod.ID, "runes"));

    public static final List<Entry> entries = new ArrayList<>();
    public record Entry(Identifier id, int capacity, Item item) {  }
    public static Entry entry(String name, int capacity, @Nullable Rarity rarity) {
        var id = Identifier.fromNamespaceAndPath(RunesMod.ID, name);
        var settings = new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .stacksTo(1)
                .component(
                        DataComponents.LORE,
                        new ItemLore(List.of(
                                Component.translatable("item.runes.rune_pouch.hint")
                                        .withStyle(ChatFormatting.GRAY)
                        ))
                )
                .component(
                        BundleAPI.CUSTOM_BUNDLE_CONTENTS_COMPONENT,
                        CustomBundleContentsComponent.builder().size_multiplier(capacity).build()
                );
        if (rarity != null) {
            settings.rarity(rarity);
        }
        var bundle = new CustomBundleItem(RUNES, Component.translatable("item.runes.rune_pouch.empty.description"), settings);
        var entry = new Entry(id, capacity, bundle);
        entries.add(entry);
        return entry;
    }

    public static void register() {
        entry("small_rune_pouch", 4, null);
        entry("medium_rune_pouch", 8, null);
        entry("large_rune_pouch", 12, Rarity.UNCOMMON);

        for(var entry: entries) {
            Registry.register(BuiltInRegistries.ITEM, entry.id(), entry.item());
        }
        // Creative-tab placement (COMBAT group) is registered per-platform from each loader's entrypoint,
        // iterating RunePouches.entries — no Fabric API ItemGroupEvents in common.
    }
}
