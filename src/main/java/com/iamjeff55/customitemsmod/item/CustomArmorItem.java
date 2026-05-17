package com.iamjeff55.CustomItemsMod.item;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import org.slf4j.Logger;

import java.util.function.Consumer;

public class CustomArmorItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ArmorType armorType;
    private final ModArmorMaterials.CustomArmorMaterial material;

    private static final ResourceKey<Registry<EquipmentAsset>> EQUIPMENT_ASSET_REGISTRY =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("minecraft", "equipment_asset"));

    private static final ResourceKey<EquipmentAsset> LAPIS_ASSET_KEY =
            ResourceKey.create(EQUIPMENT_ASSET_REGISTRY, Identifier.fromNamespaceAndPath("customitemsmod", "lapis_armor_material"));

    public CustomArmorItem(ModArmorMaterials.CustomArmorMaterial material, ArmorType type, Properties properties) {
        super(properties
                .component(DataComponents.EQUIPPABLE, Equippable.builder(getPhysicalSlot(type))
                        .setAsset(LAPIS_ASSET_KEY)
                        .setEquipSound(Holder.direct(material.equipSound))
                        .build())
                .attributes(createArmorAttributes(material, type))
        );
        this.armorType = type;
        this.material = material;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.literal("Full Set Bonus: Jump Boost IV & Glowing").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, display, builder, flag);
    }

    private static ItemAttributeModifiers createArmorAttributes(ModArmorMaterials.CustomArmorMaterial material, ArmorType type) {
        EquipmentSlotGroup group = getSlotGroup(type);
        String typeName = type.getSerializedName();
        Identifier id = Identifier.fromNamespaceAndPath("customitemsmod", "armor_" + typeName);

        return ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR, new AttributeModifier(id, (double) material.getDefense(type), AttributeModifier.Operation.ADD_VALUE), group)
                .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(id, (double) material.toughness, AttributeModifier.Operation.ADD_VALUE), group)
                .build();
    }

    private static EquipmentSlotGroup getSlotGroup(ArmorType type) {
        return switch (type) {
            case HELMET -> EquipmentSlotGroup.HEAD;
            case CHESTPLATE -> EquipmentSlotGroup.CHEST;
            case LEGGINGS -> EquipmentSlotGroup.LEGS;
            case BOOTS -> EquipmentSlotGroup.FEET;
            default -> EquipmentSlotGroup.ARMOR;
        };
    }

    private static EquipmentSlot getPhysicalSlot(ArmorType type) {
        return switch (type) {
            case HELMET -> EquipmentSlot.HEAD;
            case CHESTPLATE -> EquipmentSlot.CHEST;
            case LEGGINGS -> EquipmentSlot.LEGS;
            case BOOTS -> EquipmentSlot.FEET;
            default -> EquipmentSlot.BODY;
        };
    }

    public ModArmorMaterials.CustomArmorMaterial getMaterial() {
        return this.material;
    }
}
