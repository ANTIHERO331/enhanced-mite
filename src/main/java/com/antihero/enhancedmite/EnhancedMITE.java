package com.antihero.enhancedmite;

import net.fabricmc.api.ModInitializer;
import net.minecraft.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnhancedMITE implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Enhanced MITE");
	public static final String SUITABLE_VERSION = "0.6.4";

	public static final Item RAW_SILVER = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

	public static final TagKey<Block> PORTABLE = TagKey.of(Registry.BLOCK_KEY, new Identifier("minecraft", "portable"));
	public static final Item NETHERITE_DAGGER = new cls_32(128000, ToolMaterials.NETHERITE, 4.0f, -1.5f, new Item.Settings().group(ItemGroup.COMBAT));
	public static final Item NETHERITE_BATTLE_AXE = new cls_42(ToolMaterials.NETHERITE, 512000, 4, -2.8f, new Item.Settings().group(ItemGroup.COMBAT));
	public static final Item NETHERITE_WAR_HAMMER = new cls_239(640000, ToolMaterials.NETHERITE, 4, -2.8f, new Item.Settings().group(ItemGroup.COMBAT));;
	public static final Item NETHERITE_MATTOCK = new cls_24(512000, ToolMaterials.NETHERITE, -2.6f, new Item.Settings().group(ItemGroup.TOOLS));
	public static final Item NETHERITE_SCYTHE = new cls_45(256000, 1.0f, -2.6f, ToolMaterials.NETHERITE, new Item.Settings().group(ItemGroup.TOOLS));
	public static final Item NETHERITE_NUGGET = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

	@Override
	public void onInitialize() {
		LOGGER.info("Enhanced MITE mod loaded. You can see changes later you enter a world.");

		Registry.register(Registry.ITEM, new Identifier("minecraft", "raw_silver"), RAW_SILVER);
		Registry.register(Registry.ITEM, new Identifier("minecraft", "netherite_dagger"), NETHERITE_DAGGER);
		Registry.register(Registry.ITEM, new Identifier("minecraft", "netherite_battle_axe"), NETHERITE_BATTLE_AXE);
		Registry.register(Registry.ITEM, new Identifier("minecraft", "netherite_war_hammer"), NETHERITE_WAR_HAMMER);
		Registry.register(Registry.ITEM, new Identifier("minecraft", "netherite_mattock"), NETHERITE_MATTOCK);
		Registry.register(Registry.ITEM, new Identifier("minecraft", "netherite_scythe"), NETHERITE_SCYTHE);
		Registry.register(Registry.ITEM, new Identifier("minecraft", "netherite_nugget"), NETHERITE_NUGGET);
	}
}
