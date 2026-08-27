package net.runes.crafting;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runes.RunesMod;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class RuneCraftingBlock extends CraftingTableBlock {
    public static final String NAME = "crafting_altar";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(RunesMod.ID, NAME);
    public static final RuneCraftingBlock INSTANCE = new RuneCraftingBlock(BlockBehaviour.Properties.of()
            .setId(ResourceKey.create(Registries.BLOCK, ID))
            .destroyTime(2)
            .noOcclusion());
    // 1.21.5+: tooltips are appended by the Item, not the Block.
    public static final BlockItem ITEM = new BlockItem(INSTANCE, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ID))
            .useBlockDescriptionPrefix()) {
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
            super.appendHoverText(stack, context, displayComponent, textConsumer, type);
            textConsumer.accept(Component.translatable("block." + RunesMod.ID + "." + NAME + ".hint").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    };
    private static final Component SCREEN_TITLE = Component.translatable("gui.runes.rune_crafting");

    public RuneCraftingBlock(Properties settings) {
        super(settings);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        return new SimpleMenuProvider((syncId, inventory, player) -> {
            return new RuneCraftingScreenHandler(syncId, inventory, ContainerLevelAccess.create(world, pos));
        }, SCREEN_TITLE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(world, pos));
            return InteractionResult.CONSUME;
        }
    }

    // MARK: Shape

    public static final VoxelShape TOP_SHAPE = Block.box(1, 12, 1, 15, 16, 15);
    public static final VoxelShape MIDDLE_SHAPE = Block.box(4, 3, 4, 12, 12, 12);
    public static final VoxelShape BOTTOM_SHAPE = Block.box(1, 0, 1, 15, 3, 15);
    private static final VoxelShape SHAPE = Shapes.or(TOP_SHAPE, MIDDLE_SHAPE, BOTTOM_SHAPE);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // MARK: Facing

    private static EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        builder.add(FACING);
    }

    // MARK: Partial transparency

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }
}
