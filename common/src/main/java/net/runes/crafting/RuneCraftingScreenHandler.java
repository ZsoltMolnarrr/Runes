package net.runes.crafting;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

// Mostly copied from SmithingScreenHandler
public class RuneCraftingScreenHandler extends ForgingScreenHandler {
    public static final ScreenHandlerType<RuneCraftingScreenHandler> HANDLER_TYPE = new ScreenHandlerType(RuneCraftingScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
    private final World world;

    public RuneCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public RuneCraftingScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public RuneCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(HANDLER_TYPE, syncId, playerInventory, context, createForgingSlotsManager());
        this.world = playerInventory.player.getEntityWorld();
    }

    private static ForgingSlotsManager createForgingSlotsManager() {
        return ForgingSlotsManager.builder()
                .input(0, 27, 47, stack -> true)
                .input(1, 76, 47, stack -> true)
                .output(2, 134, 47)
                .build();
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(RuneCraftingBlock.INSTANCE);
    }

    private RuneCraftingRecipeInput createRecipeInput() {
        return new RuneCraftingRecipeInput(this.input.getStack(0), this.input.getStack(1));
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraftByPlayer(player, stack.getCount());
        this.output.unlockLastRecipe(player, this.getInputStacks());
        this.decrementStack(0);
        this.decrementStack(1);

//        if(player instanceof ServerPlayerEntity serverPlayer) {
//            RuneCraftingCriteria.INSTANCE.trigger(serverPlayer);
//        }
        var runeCrafter = (RuneCrafter)player;
        // Like vanilla, `onTakeOutput` also runs on the client (prediction); only the server broadcasts the sound.
        if (!world.isClient() && runeCrafter.shouldPlayRuneCraftingSound(player.age)) {
            // Source must be null: a non-null source is the "except" player of the broadcast and would never hear it.
            world.playSound(null, player.getX(), player.getY(), player.getZ(), RuneCrafting.SOUND, SoundCategory.BLOCKS, world.random.nextFloat() * 0.1F + 0.9F, 1);
            runeCrafter.onPlayedRuneCraftingSound(player.age);
        }
    }

    private List<ItemStack> getInputStacks() {
        return List.of(this.input.getStack(0), this.input.getStack(1));
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.input.getStack(slot);
        if (!itemStack.isEmpty()) {
            itemStack.decrement(1);
            this.input.setStack(slot, itemStack);
        }
    }

    @Override
    public void updateResult() {
        var recipeInput = this.createRecipeInput();
        // 1.21.2+: recipes are server-only; the client just receives the result slot.
        Optional<RecipeEntry<RuneCraftingRecipe>> result;
        if (this.world instanceof ServerWorld serverWorld) {
            result = serverWorld.getRecipeManager().getFirstMatch(RuneCraftingRecipe.TYPE, recipeInput, serverWorld);
        } else {
            result = Optional.empty();
        }
        if (result.isPresent()) {
            var recipeEntry = result.get();
            ItemStack itemStack = recipeEntry.value().craft(recipeInput, this.world.getRegistryManager());
            this.output.setLastRecipe(recipeEntry);
            this.output.setStack(0, itemStack);
        } else {
            this.output.setLastRecipe(null);
            this.output.setStack(0, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }
}
