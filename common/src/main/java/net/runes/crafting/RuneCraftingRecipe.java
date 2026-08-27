package net.runes.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import net.minecraft.world.level.Level;

// Mirrors SmithingTransformRecipe (26.1): recipe results are ItemStackTemplates (no ItemStack may exist
// before registries load), and the serializer is a plain record of MapCodec + StreamCodec.
public class RuneCraftingRecipe implements Recipe<RuneCraftingRecipeInput> {
    final Recipe.CommonInfo commonInfo;
    final Ingredient base;
    final Ingredient addition;
    final ItemStackTemplate result;
    private @Nullable PlacementInfo ingredientPlacement;

    public RuneCraftingRecipe(Recipe.CommonInfo commonInfo, Ingredient base, Ingredient addition, ItemStackTemplate result) {
        this.commonInfo = commonInfo;
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

    /** The recipe's raw, unmodified result template — {@link #assemble} additionally copies components off the base stack. */
    public ItemStackTemplate result() {
        return this.result;
    }

    @Override
    public boolean matches(RuneCraftingRecipeInput input, Level world) {
        return this.base.test(input.getItem(0)) && this.addition.test(input.getItem(1));
    }

    @Override
    public ItemStack assemble(RuneCraftingRecipeInput input) {
        return TransmuteRecipe.createWithOriginalComponents(this.result, input.base());
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<RuneCraftingRecipe> getSerializer() {
        return SERIALIZER;
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

    public static final MapCodec<RuneCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
                            Ingredient.CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                            Ingredient.CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition),
                            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                    )
                    .apply(instance, RuneCraftingRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RuneCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, recipe -> recipe.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.base,
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.addition,
            ItemStackTemplate.STREAM_CODEC, recipe -> recipe.result,
            RuneCraftingRecipe::new
    );
    public static final RecipeSerializer<RuneCraftingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
}
