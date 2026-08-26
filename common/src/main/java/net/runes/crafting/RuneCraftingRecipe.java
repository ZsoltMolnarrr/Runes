package net.runes.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RuneCraftingRecipe implements Recipe<RuneCraftingRecipeInput> {
    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;
    @Nullable
    private PlacementInfo ingredientPlacement;

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

    /** The recipe's raw, unmodified result — {@link #assemble} additionally copies components off the base stack. */
    public ItemStack result() {
        return this.result;
    }

    @Override
    public boolean matches(RuneCraftingRecipeInput input, Level world) {
        return this.base.test(input.getItem(0)) && this.addition.test(input.getItem(1));
    }

    @Override
    public ItemStack assemble(RuneCraftingRecipeInput input, HolderLookup.Provider wrapperLookup) {
        ItemStack itemStack = input.base().transmuteCopy(this.result.getItem(), this.result.getCount());
        itemStack.applyComponents(this.result.getComponentsPatch());
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
    public PlacementInfo placementInfo() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = PlacementInfo.createFromOptionals(List.of(Optional.of(this.base), Optional.of(this.addition)));
        }
        return this.ingredientPlacement;
    }

    /** Never shown in the recipe book (no {@link #display()}); the smithing category is the closest fit. */
    @Override
    public RecipeBookCategory recipeBookCategory() {
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
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                        )
                        .apply(instance, RuneCraftingRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, RuneCraftingRecipe> PACKET_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.base,
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.addition,
                ItemStack.STREAM_CODEC, recipe -> recipe.result,
                RuneCraftingRecipe::new
        );

        @Override
        public MapCodec<RuneCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RuneCraftingRecipe> streamCodec() {
            return PACKET_CODEC;
        }
    }
}
