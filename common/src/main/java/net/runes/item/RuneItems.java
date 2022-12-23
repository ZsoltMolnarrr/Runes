package net.runes.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.runes.RunesMod;

import java.util.HashMap;
import java.util.Map;

public class RuneItems {

    private enum MagicSchool {
        ARCANE, FIRE, FROST, HEALING, LIGHTNING, SOUL;
    }

    public static Map<Identifier, Item> all;
    static {
        all = new HashMap<>();
        for(var school : MagicSchool.values()) {
            var id = new Identifier(RunesMod.ID, school.toString().toLowerCase() + "_stone");
            var item = new Item(new FabricItemSettings().group(ItemGroup.COMBAT));
            all.put(id, item);
        }
    }
}
