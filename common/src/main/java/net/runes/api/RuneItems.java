package net.runes.api;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
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
            var id = new Identifier(RunesMod.ID, type.toString().toLowerCase(Locale.ENGLISH) + "_stone");
            var item = new Item(new FabricItemSettings());
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
