package net.runes;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.runes.internals.RuneCrafting;
import net.runes.internals.RuneCraftingBlock;
import net.runes.internals.RuneCraftingRecipe;
import net.runes.internals.RuneCraftingScreenHandler;
import net.runes.item.RuneItems;

public class RunesMod {
    public static final String ID = "runes";

    public static void init() {
        Registry.register(Registry.SOUND_EVENT, RuneCrafting.ID, RuneCrafting.SOUND);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(ID, RuneCraftingRecipe.NAME), RuneCraftingRecipe.TYPE);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ID, RuneCraftingRecipe.NAME), RuneCraftingRecipe.SERIALIZER);

        Registry.register(Registry.BLOCK, new Identifier(ID, RuneCraftingBlock.NAME), RuneCraftingBlock.INSTANCE);
        Registry.register(Registry.ITEM, new Identifier(ID, RuneCraftingBlock.NAME), new BlockItem(RuneCraftingBlock.INSTANCE, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        BlockRenderLayerMap.INSTANCE.putBlock(RuneCraftingBlock.INSTANCE, RenderLayer.getCutout());
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(ID, RuneCraftingRecipe.NAME), RuneCraftingScreenHandler.HANDLER_TYPE);

        for(var entry: RuneItems.all.entrySet()) {
            Registry.register(Registry.ITEM, entry.getKey(), entry.getValue());
        }
    }
}