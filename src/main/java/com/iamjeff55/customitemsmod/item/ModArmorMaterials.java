package com.iamjeff55.CustomItemsMod.item;

import com.iamjeff55.CustomItemsMod.CustomItemsMod;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModArmorMaterials {

    private static final Map<Identifier, CustomArmorMaterial> ARMOR_MATERIALS = new LinkedHashMap<>();

    // Primary Register Method
    public static CustomArmorMaterial register(
            String name,
            Map<ArmorType, Integer> defense,
            int enchantability,
            int durability,
            float toughness,
            float knockbackResist,
            SoundEvent equipSound,
            Item repairItem
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(CustomItemsMod.MODID, name);

        TagKey<Item> repairTag = TagKey.create(
                BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(CustomItemsMod.MODID, "repair_" + name)
        );

        CustomArmorMaterial material = new CustomArmorMaterial(
                durability,
                defense,
                enchantability,
                toughness,
                knockbackResist,
                equipSound,
                repairTag
        );

        ARMOR_MATERIALS.put(id, material);
        return material;
    }

    // FIXED: Added repairItem to the call so it matches the method above
    public static CustomArmorMaterial register(
            String name,
            Map<ArmorType, Integer> defense,
            int enchantability,
            int durability,
            float toughness,
            float knockbackResist,
            Holder<SoundEvent> equipSoundHolder,
            Item repairItem
    ) {
        // We pass ALL arguments, including repairItem, to the primary register method
        return register(name, defense, enchantability, durability, toughness, knockbackResist, equipSoundHolder.value(), repairItem);
    }

    public static class CustomArmorMaterial {
        public final int durability;
        public final Map<ArmorType, Integer> defense;
        public final int enchantability;
        public final float toughness;
        public final float knockbackResistance;
        public final SoundEvent equipSound;
        public final TagKey<Item> repairItem;

        public CustomArmorMaterial(int durability,
                                   Map<ArmorType, Integer> defense,
                                   int enchantability,
                                   float toughness,
                                   float knockbackResistance,
                                   SoundEvent equipSound,
                                   TagKey<Item> repairItem) {
            this.durability = durability;
            this.defense = defense;
            this.enchantability = enchantability;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
            this.equipSound = equipSound;
            this.repairItem = repairItem;
        }

        // Use this in your CustomArmorItem class to get defense values
        public int getDefense(ArmorType type) {
            return this.defense.getOrDefault(type, 0);
        }
    }
}
