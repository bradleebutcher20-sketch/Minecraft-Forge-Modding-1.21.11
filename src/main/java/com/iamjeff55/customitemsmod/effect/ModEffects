package com.iamjeff55.CustomItemsMod.effect;

import com.iamjeff55.CustomItemsMod.CustomItemsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, CustomItemsMod.MODID);

    public static final RegistryObject<MobEffect> FALL_IMMUNITY = MOB_EFFECTS.register("fall_immunity",
            () -> new FallImmunityEffect(MobEffectCategory.BENEFICIAL, 0xADD8E6)); // Light blue color
}
