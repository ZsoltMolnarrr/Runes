package net.runes.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class RuneCraftingRecipe implements Recipe<RuneCraftingRecipeInput> {
    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;
    @Nullable
    private IngredientPlacement ingredientPlacement;

    public RuneCraftingRecipe(Ingredient base, Ingredient addition, ItemStack result) {
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

    /** The recipe's raw, unmodified result — {@link #craft} additionally copies components off the base stack. */
    public ItemStack result() {
        return this.result;
    }

    @Override
    public boolean matches(RuneCraftingRecipeInput input, World world) {
        return this.base.test(input.getStackInSlot(0)) && this.addition.test(input.getStackInSlot(1));
    }

    @Override
    public ItemStack craft(RuneCraftingRecipeInput input, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = input.base().copyComponentsToNewStack(this.result.getItem(), this.result.getCount());
        itemStack.applyUnvalidatedChanges(this.result.getComponentChanges());
        return itemStack;
    }

    @Override
    public RecipeSerializer<RuneCraftingRecipe> getSerializer() {
        return RuneCrafting.RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<RuneCraftingRecipe> getType() {
        return TYPE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forMultipleSlots(List.of(Optional.of(this.base), Optional.of(this.addition)));
        }
        return this.ingredientPlacement;
    }

    /** Never shown in the recipe book (no {@link #getDisplays()}); the smithing category is the closest fit. */
    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.SMITHING;
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
                                Ingredient.CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                                Ingredient.CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition),
                                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                        )
                        .apply(instance, RuneCraftingRecipe::new)
        );
        public static final PacketCodec<RegistryByteBuf, RuneCraftingRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, recipe -> recipe.base,
                Ingredient.PACKET_CODEC, recipe -> recipe.addition,
                ItemStack.PACKET_CODEC, recipe -> recipe.result,
                RuneCraftingRecipe::new
        );

        @Override
        public MapCodec<RuneCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, RuneCraftingRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
