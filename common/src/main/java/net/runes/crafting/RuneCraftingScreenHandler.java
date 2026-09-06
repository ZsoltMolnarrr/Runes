package net.runes.crafting;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Mostly copied from SmithingScreenHandler
public class RuneCraftingScreenHandler extends ForgingScreenHandler {
    /// Platform seam. On 1.20.1 the `ScreenHandlerType` constructor (and its `Factory` interface) are
    /// PRIVATE in vanilla — Fabric API widens them with an access widener, and Forge exposes
    /// `IForgeMenuType.create`. `common` has no Fabric API on this line (no Forgified Fabric API), so
    /// each platform entrypoint builds the type and assigns it here BEFORE
    /// `RunesMod.registerScreenHandler()` runs.
    public static ScreenHandlerType<RuneCraftingScreenHandler> HANDLER_TYPE;
    private final World world;
    @Nullable
    private RuneCraftingRecipe currentRecipe;
    private final List<RuneCraftingRecipe> recipes;

    public RuneCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public RuneCraftingScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public RuneCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(HANDLER_TYPE, syncId, playerInventory, context);
        this.world = playerInventory.player.getWorld();
        this.recipes = this.world.getRecipeManager().listAllOfType(RuneCraftingRecipe.TYPE);
    }

    protected ForgingSlotsManager getForgingSlotsManager() {
        return ForgingSlotsManager.create().input(0, 27, 47, (stack) -> {
            return true;
        }).input(1, 76, 47, (stack) -> {
            return true;
        }).output(2, 134, 47).build();
    }

    protected boolean canUse(BlockState state) {
        return state.isOf(RuneCraftingBlock.INSTANCE);
    }

    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return this.currentRecipe != null && this.currentRecipe.matches(this.input, this.world);
    }

    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.getWorld(), player, stack.getCount());
        this.output.unlockLastRecipe(player, this.getInputStacks());
        this.decrementStack(0);
        this.decrementStack(1);

//        if(player instanceof ServerPlayerEntity serverPlayer) {
//            RuneCraftingCriteria.INSTANCE.trigger(serverPlayer);
//        }
        var runeCrafter = (RuneCrafter)player;
        if (runeCrafter.shouldPlayRuneCraftingSound(player.age)) {
            world.playSound(player.getX(), player.getY(), player.getZ(), RuneCrafting.SOUND, SoundCategory.BLOCKS, world.random.nextFloat() * 0.1F + 0.9F, 1, true);
            runeCrafter.onPlayedRuneCraftingSound(player.age);
        }
    }

    private List<ItemStack> getInputStacks() {
        return List.of(this.input.getStack(0), this.input.getStack(1));
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.input.getStack(slot);
        itemStack.decrement(1);
        this.input.setStack(slot, itemStack);
    }

    public void updateResult() {
        // 1.20.1: the forging inventory IS the recipe input (no `RecipeInput` record).
        Inventory recipeInput = this.input;
        var result = this.world.getRecipeManager().getFirstMatch(RuneCraftingRecipe.TYPE, recipeInput, this.world);
        if (result.isPresent()) {
            var recipe = result.get();
            ItemStack itemStack = recipe.craft(recipeInput, this.world.getRegistryManager());
            this.currentRecipe = recipe;
            this.output.setLastRecipe(recipe);
            this.output.setStack(0, itemStack);
        } else {
            this.output.setStack(0, ItemStack.EMPTY);
        }
    }

//    protected boolean isUsableAsAddition(ItemStack stack) {
//        return this.recipes.stream().anyMatch((recipe) -> {
//            return recipe.testAddition(stack);
//        });
//    }

    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }
}
