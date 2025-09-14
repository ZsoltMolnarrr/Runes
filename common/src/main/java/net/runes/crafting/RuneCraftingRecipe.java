package net.runes.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class RuneCraftingRecipe implements Recipe<RuneCraftingRecipeInput> {
    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;

    public RuneCraftingRecipe(Ingredient base, Ingredient addition, ItemStack result) {
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public boolean matches(RuneCraftingRecipeInput input, World world) {
        return this.base.test(input.getStackInSlot(0)) && this.addition.test(input.getStackInSlot(1));
    }

    public ItemStack craft(RuneCraftingRecipeInput input, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = input.base().copyComponentsToNewStack(this.result.getItem(), this.result.getCount());
        itemStack.applyUnvalidatedChanges(this.result.getComponentChanges());
        return itemStack;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.result;
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public ItemStack createIcon() {
        return new ItemStack(RuneCraftingBlock.INSTANCE);
    }

    public RecipeSerializer<?> getSerializer() {
        return RuneCrafting.RECIPE_SERIALIZER;
    }

    public RecipeType<?> getType() {
        return TYPE;
    }

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

    public static class Serializer implements RecipeSerializer<RuneCraftingRecipe> {
        private static final MapCodec<RuneCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition),
                                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                        )
                        .apply(instance, RuneCraftingRecipe::new)
        );
        public static final PacketCodec<RegistryByteBuf, RuneCraftingRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                RuneCraftingRecipe.Serializer::write, RuneCraftingRecipe.Serializer::read
        );

        @Override
        public MapCodec<RuneCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, RuneCraftingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static RuneCraftingRecipe read(RegistryByteBuf buf) {
            Ingredient ingredient2 = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient ingredient3 = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack itemStack = ItemStack.PACKET_CODEC.decode(buf);
            return new RuneCraftingRecipe(ingredient2, ingredient3, itemStack);
        }

        private static void write(RegistryByteBuf buf, RuneCraftingRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.base);
            Ingredient.PACKET_CODEC.encode(buf, recipe.addition);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
        }
    }
}
