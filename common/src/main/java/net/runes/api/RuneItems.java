package net.runes.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.runes.RunesMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RuneItems {
    public enum RuneType {
        ARCANE, FIRE, FROST, HEALING, LIGHTNING, SOUL;
    }

    public record Entry(Identifier id, RuneType type, Item item) { }
    public static final List<Entry> entries;

    static {
        var all = new ArrayList<Entry>();
        for(var type : RuneType.values()) {
            var id = Identifier.fromNamespaceAndPath(RunesMod.ID, type.toString().toLowerCase(Locale.ENGLISH) + "_stone");
            var item = new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)));
            all.add(new Entry(id, type, item));
        }
        entries = all;
    }

    public static Item get(RuneType type) {
        return entries.stream()
                .filter(entry -> entry.type == type)
                .findFirst()
                .get()
                .item;
    }
}
