package com.iamjeff55.CustomItemsMod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class HammerItem extends Item {

    public HammerItem(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Returns a 3x3 area based on the face hit (1 block deep)
     */
    public static List<BlockPos> getBlocksToBeDestroyed(BlockPos origin, Direction face) {
        List<BlockPos> positions = new ArrayList<>(9);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                BlockPos target;

                switch (face) {
                    case UP:
                    case DOWN:
                        // Horizontal plane (floor/ceiling)
                        target = origin.offset(i, 0, j);
                        break;

                    case NORTH:
                    case SOUTH:
                        // Vertical wall (X/Y)
                        target = origin.offset(i, j, 0);
                        break;

                    case EAST:
                    case WEST:
                        // Vertical wall (Y/Z)
                        target = origin.offset(0, i, j);
                        break;

                    default:
                        target = origin;
                }

                positions.add(target);
            }
        }

        return positions;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        Level world = player.level();

        if (!world.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            // 🔥 Get EXACT block face the player is hitting
            HitResult hit = player.pick(5.0D, 0.0F, false);

            if (hit instanceof BlockHitResult blockHit) {

                Direction face = blockHit.getDirection();

                List<BlockPos> area = getBlocksToBeDestroyed(pos, face);

                for (BlockPos targetPos : area) {
                    if (targetPos.equals(pos)) continue;

                    if (!world.isInWorldBounds(targetPos)) continue;

                    BlockState targetState = world.getBlockState(targetPos);

                    if (!targetState.isAir() &&
                            targetState.getDestroySpeed(world, targetPos) >= 0) {

                        world.destroyBlock(targetPos, true, serverPlayer);
                    }
                }

                // Damage hammer once per swing
                itemstack.hurtAndBreak(1, serverPlayer, EquipmentSlot.MAINHAND);
            }
        }

        return false; // Let vanilla break the original block
    }
}
