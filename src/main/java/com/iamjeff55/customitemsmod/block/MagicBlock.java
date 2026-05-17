package com.iamjeff55.CustomItemsMod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MagicBlock extends Block {
    public MagicBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos,
                                               Player pPlayer, BlockHitResult pHitResult) {
        pLevel.playSound(pPlayer, pPos, SoundEvents.AMETHYST_CLUSTER_PLACE, SoundSource.BLOCKS, 1f, 1f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pLevel.isClientSide() && pEntity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            Item input = stack.getItem();
            Item resultItem = null;
            int multiplier = 1;

            // --- Your Item Conversion Logic ---
            if (input == Items.DEEPSLATE_COAL_ORE) resultItem = Items.COAL_ORE;
            else if (input == Items.DEEPSLATE_IRON_ORE) resultItem = Items.IRON_ORE;
            else if (input == Items.DEEPSLATE_GOLD_ORE) resultItem = Items.GOLD_ORE;
            else if (input == Items.DEEPSLATE_COPPER_ORE) resultItem = Items.COPPER_ORE;
            else if (input == Items.COAL_ORE) { resultItem = Items.COAL; multiplier = 3; }
            else if (input == Items.IRON_ORE) { resultItem = Items.IRON_INGOT; multiplier = 3; }
            else if (input == Items.GOLD_ORE) { resultItem = Items.GOLD_INGOT; multiplier = 3; }
            else if (input == Items.COPPER_ORE) { resultItem = Items.COPPER_INGOT; multiplier = 3; }
            else if (input == Items.RAW_COPPER_BLOCK) { resultItem = Items.RAW_COPPER; multiplier = 3; }
            else if (input == Items.RAW_COPPER) { resultItem = Items.COPPER_INGOT; multiplier = 3; }
            else if (input == Items.RAW_GOLD_BLOCK) { resultItem = Items.RAW_GOLD; multiplier = 3; }
            else if (input == Items.RAW_GOLD) { resultItem = Items.GOLD_INGOT; multiplier = 3; }
            else if (input == Items.RAW_IRON_BLOCK) { resultItem = Items.RAW_IRON; multiplier = 3; }
            else if (input == Items.RAW_IRON) { resultItem = Items.IRON_INGOT; multiplier = 3; }
            else if (input == Items.RABBIT_FOOT) { resultItem = Items.EMERALD; }
            // ----------------------------------

            if (resultItem != null) {
                // Apply the multiplier to the current stack count
                int newCount = Math.min(stack.getCount() * multiplier, 64);

                // Perform the transformation
                itemEntity.setItem(new ItemStack(resultItem, newCount));

                // Play the sound once per transformation
                pLevel.playSound(null, pPos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1f, 1f);
            }
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
