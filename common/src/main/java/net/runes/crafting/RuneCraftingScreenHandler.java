package net.runes.crafting;

import java.util.List;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

// Mostly copied from SmithingScreenHandler
public class RuneCraftingScreenHandler extends ItemCombinerMenu {
    public static final MenuType<RuneCraftingScreenHandler> HANDLER_TYPE = new MenuType(RuneCraftingScreenHandler::new, FeatureFlags.VANILLA_SET);
    private final Level world;

    public RuneCraftingScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL);
    }

    public RuneCraftingScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL);
    }

    public RuneCraftingScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(HANDLER_TYPE, syncId, playerInventory, context, createForgingSlotsManager());
        this.world = playerInventory.player.level();
    }

    private static ItemCombinerMenuSlotDefinition createForgingSlotsManager() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, stack -> true)
                .withSlot(1, 76, 47, stack -> true)
                .withResultSlot(2, 134, 47)
                .build();
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(RuneCraftingBlock.INSTANCE);
    }

    private RuneCraftingRecipeInput createRecipeInput() {
        return new RuneCraftingRecipeInput(this.inputSlots.getItem(0), this.inputSlots.getItem(1));
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        stack.onCraftedBy(player, stack.getCount());
        this.resultSlots.awardUsedRecipes(player, this.getInputStacks());
        this.decrementStack(0);
        this.decrementStack(1);

//        if(player instanceof ServerPlayerEntity serverPlayer) {
//            RuneCraftingCriteria.INSTANCE.trigger(serverPlayer);
//        }
        var runeCrafter = (RuneCrafter)player;
        // Like vanilla, `onTakeOutput` also runs on the client (prediction); only the server broadcasts the sound.
        if (!world.isClientSide() && runeCrafter.shouldPlayRuneCraftingSound(player.tickCount)) {
            // Source must be null: a non-null source is the "except" player of the broadcast and would never hear it.
            world.playSound(null, player.getX(), player.getY(), player.getZ(), RuneCrafting.SOUND, SoundSource.BLOCKS, world.getRandom().nextFloat() * 0.1F + 0.9F, 1);
            runeCrafter.onPlayedRuneCraftingSound(player.tickCount);
        }
    }

    private List<ItemStack> getInputStacks() {
        return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1));
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.inputSlots.getItem(slot);
        if (!itemStack.isEmpty()) {
            itemStack.shrink(1);
            this.inputSlots.setItem(slot, itemStack);
        }
    }

    @Override
    public void createResult() {
        var recipeInput = this.createRecipeInput();
        // 1.21.2+: recipes are server-only; the client just receives the result slot.
        Optional<RecipeHolder<RuneCraftingRecipe>> result;
        if (this.world instanceof ServerLevel serverWorld) {
            result = serverWorld.recipeAccess().getRecipeFor(RuneCraftingRecipe.TYPE, recipeInput, serverWorld);
        } else {
            result = Optional.empty();
        }
        if (result.isPresent()) {
            var recipeEntry = result.get();
            ItemStack itemStack = recipeEntry.value().assemble(recipeInput);
            this.resultSlots.setRecipeUsed(recipeEntry);
            this.resultSlots.setItem(0, itemStack);
        } else {
            this.resultSlots.setRecipeUsed(null);
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }
}
