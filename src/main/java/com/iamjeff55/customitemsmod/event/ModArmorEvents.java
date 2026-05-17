package com.iamjeff55.CustomItemsMod.event;

import com.iamjeff55.CustomItemsMod.effect.ModEffects;
import com.iamjeff55.CustomItemsMod.item.CustomArmorItem;
import com.iamjeff55.CustomItemsMod.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "customitemsmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModArmorEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent.Post event) {
        if (event.side() == LogicalSide.CLIENT) return;

        Player player = event.player();

        if (hasFullLapisSet(player)) {
            // Apply infinite effects with logic to prevent UI flicker
            applyInfiniteEffect(player, MobEffects.JUMP_BOOST, 3);
            applyInfiniteEffect(player, MobEffects.GLOWING, 0);
            applyInfiniteEffect(player, MobEffects.SPEED, 4);
            applyInfiniteEffect(player, ModEffects.FALL_IMMUNITY.getHolder().get(), 0);
        } else {
            removeLapisEffects(player);
        }
    }

    private static void applyInfiniteEffect(Player player, Holder<MobEffect> effect, int amplifier) {
        if (!player.hasEffect(effect)) {
            player.addEffect(new MobEffectInstance(effect, -1, amplifier, false, false, true));
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingDamageEvent event) {
        // Use getEntity() and getSource() getters
        if (event.getEntity() instanceof Player player) {
            if (event.getSource().is(DamageTypes.FALL)) {
                if (player.hasEffect(ModEffects.FALL_IMMUNITY.getHolder().get())) {
                    // This event is cancelable
                    event.setAmount(0.0f);
                }
            }
        }
    }

    private static boolean hasFullLapisSet(Player player) {
        for (EquipmentSlot slot : List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof CustomArmorItem armor) || armor.getMaterial() != ModItems.LAPIS_MATERIAL) {
                return false;
            }
        }
        return true;
    }

    private static void removeLapisEffects(Player player) {
        removeIfInfinite(player, MobEffects.JUMP_BOOST);
        removeIfInfinite(player, MobEffects.GLOWING);
        removeIfInfinite(player, MobEffects.SPEED);
        removeIfInfinite(player, ModEffects.FALL_IMMUNITY.getHolder().get());
    }

    private static void removeIfInfinite(Player player, Holder<MobEffect> effectHolder) {
        if (player.hasEffect(effectHolder)) {
            MobEffectInstance inst = player.getEffect(effectHolder);
            // Replace isInfinite() with direct duration check if method is missing
            if (inst != null && inst.getDuration() == -1) {
                player.removeEffect(effectHolder);
            }
        }
    }
}
