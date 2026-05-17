package com.iamjeff55.CustomItemsMod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ChiselItem extends Item {

    // Map remains the same as your original code
    private static final Map<Block, Block> CHISEL_MAP = Map.ofEntries(
            Map.entry(Blocks.STONE, Blocks.COBBLESTONE),
            Map.entry(Blocks.COBBLESTONE, Blocks.STONE_BRICKS),
            Map.entry(Blocks.STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS),
            // ... (keep all your other Map.entry lines here)
            Map.entry(Blocks.DIRT, Blocks.DIRT_PATH)
    );

    public ChiselItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        BlockState state = level.getBlockState(pos);
        Block currentBlock = state.getBlock();

        if (CHISEL_MAP.containsKey(currentBlock)) {
            if (!level.isClientSide() && player != null) {
                Block newBlock = CHISEL_MAP.get(currentBlock);
                level.setBlock(pos, newBlock.defaultBlockState(), 3);

                // FIXED: Simplified hurtAndBreak logic for modern EquipmentSlot usage
                stack.hurtAndBreak(1, player,
                        context.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND
                                ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

                level.playSound(null, pos, SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    /**
     * FIXED: Correct 26.1 Tooltip Method.
     * The old appendHoverText(..., List<Component>, ...) is now deprecated.
     */
    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            net.minecraft.world.item.component.TooltipDisplay display, // The missing 5th argument
            java.util.function.@NotNull Consumer<net.minecraft.network.chat.Component> tooltipBuilder,
            net.minecraft.world.item.TooltipFlag tooltipFlag
    ) {
        tooltipBuilder.accept(net.minecraft.network.chat.Component.translatable("tooltip.customitemsmod.chisel.description")
                .withStyle(net.minecraft.ChatFormatting.GOLD));

        super.appendHoverText(stack, context, display, tooltipBuilder, tooltipFlag);
    }
}
