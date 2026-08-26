package net.runes.crafting;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.runes.RunesMod;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RuneCraftingBlock extends CraftingTableBlock {
    public static final String NAME = "crafting_altar";
    public static final Identifier ID = Identifier.of(RunesMod.ID, NAME);
    public static final RuneCraftingBlock INSTANCE = new RuneCraftingBlock(AbstractBlock.Settings.create()
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, ID))
            .hardness(2)
            .nonOpaque());
    // 1.21.5+: tooltips are appended by the Item, not the Block.
    public static final BlockItem ITEM = new BlockItem(INSTANCE, new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, ID))
            .useBlockPrefixedTranslationKey()) {
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
            super.appendTooltip(stack, context, displayComponent, textConsumer, type);
            textConsumer.accept(Text.translatable("block." + RunesMod.ID + "." + NAME + ".hint").formatted(Formatting.GRAY, Formatting.ITALIC));
        }
    };
    private static final Text SCREEN_TITLE = Text.translatable("gui.runes.rune_crafting");

    public RuneCraftingBlock(Settings settings) {
        super(settings);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> {
            return new RuneCraftingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos));
        }, SCREEN_TITLE);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            return ActionResult.CONSUME;
        }
    }

    // MARK: Shape

    public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(1, 12, 1, 15, 16, 15);
    public static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(4, 3, 4, 12, 12, 12);
    public static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(1, 0, 1, 15, 3, 15);
    private static final VoxelShape SHAPE = VoxelShapes.union(TOP_SHAPE, MIDDLE_SHAPE, BOTTOM_SHAPE);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    // MARK: Facing

    private static EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        FACING = Properties.HORIZONTAL_FACING;
        builder.add(FACING);
    }

    // MARK: Partial transparency

    @Override
    protected boolean isTransparent(BlockState state) {
        return true;
    }
}
