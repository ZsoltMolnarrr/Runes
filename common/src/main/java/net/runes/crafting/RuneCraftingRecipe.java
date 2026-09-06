package net.runes.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.stream.Stream;

/**
 * The altar-only recipe: base + addition → result.
 *
 * <p>1.20.1 shape: {@code Recipe<Inventory>} (there is no {@code RecipeInput}), the recipe carries its
 * own {@link #getId() id}, and the serializer is a hand-written JSON/packet pair instead of a
 * {@code MapCodec} + {@code PacketCodec}.
 */
public class RuneCraftingRecipe implements Recipe<Inventory> {
    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;
    private final Identifier id;

    public RuneCraftingRecipe(Identifier id, Ingredient base, Ingredient addition, ItemStack result) {
        this.id = id;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public Ingredient base() {
        return this.base;
    }

    public Ingredient addition() {
        return this.addition;
    }

    /** The recipe's raw, unmodified result — {@link #craft} additionally copies the base stack's NBT. */
    public ItemStack result() {
        return this.result;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.base.test(inventory.getStack(0)) && this.addition.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        // 1.20.1 has no data components: the 1.21 `copyComponentsToNewStack` + `applyUnvalidatedChanges`
        // pair is the NBT copy the legacy branch did.
        ItemStack itemStack = this.result.copy();
        NbtCompound nbtCompound = inventory.getStack(0).getNbt();
        if (nbtCompound != null) {
            itemStack.setNbt(nbtCompound.copy());
        }
        return itemStack;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.result;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public boolean testAddition(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(RuneCraftingBlock.INSTANCE);
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean isEmpty() {
        return Stream.of(this.base, this.addition).anyMatch((ingredient) -> {
            return ingredient.getMatchingStacks().length == 0;
        });
    }

    public static final String NAME = "crafting";

    public static final RecipeType<RuneCraftingRecipe> TYPE = new RecipeType<RuneCraftingRecipe>() {
        public String toString() {
            return NAME;
        }
    };

    public static final Serializer SERIALIZER = new Serializer();

    public static class Serializer implements RecipeSerializer<RuneCraftingRecipe> {
        @Override
        public RuneCraftingRecipe read(Identifier identifier, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "base"));
            Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "addition"));
            ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new RuneCraftingRecipe(identifier, ingredient, ingredient2, itemStack);
        }

        @Override
        public RuneCraftingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();
            return new RuneCraftingRecipe(identifier, ingredient, ingredient2, itemStack);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, RuneCraftingRecipe recipe) {
            recipe.base.write(packetByteBuf);
            recipe.addition.write(packetByteBuf);
            packetByteBuf.writeItemStack(recipe.result);
        }
    }
}
