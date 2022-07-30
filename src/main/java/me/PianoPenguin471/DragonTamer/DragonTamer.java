package me.PianoPenguin471.DragonTamer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DragonTamer implements ModInitializer {
	// Taming wand item
	public static final Item TAMING_WAND = new Item(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.EPIC).maxCount(1));
	public static final int HATCH_TIME = 200;
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("dragontamer");
	public static List<HatchingEgg> HATCHING_EGGS = new ArrayList<>();
	public static HashMap<UUID, UUID> TAMED_DRAGONS = new HashMap<>();
	
	@Override
	public void onInitialize() {
		// Register taming wand to minecraft
		Registry.register(Registry.ITEM, new Identifier("dragontamer", "taming_wand"), TAMING_WAND);
		
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (HatchingEgg egg : HATCHING_EGGS) {
				int aliveTicks = egg.ticksExisted += 1;
				int progress = 0;
				boolean hatching = false;
				if (aliveTicks == 1) progress = 1;
				else if (aliveTicks == 20) progress = 2;
				else if (aliveTicks == 40) progress = 3;
				else if (aliveTicks == 60) progress = 4;
				else if (aliveTicks == 80) progress = 5;
				else if (aliveTicks == 100) progress = 6;
				else if (aliveTicks == 120) progress = 7;
				else if (aliveTicks == 140) hatching = true;
				
				if (hatching) {
					egg.world.setBlockState(egg.pos, Blocks.AIR.getDefaultState());
					EnderDragonEntity newborn = new EnderDragonEntity(EntityType.ENDER_DRAGON, egg.world);
					
					newborn.setPos(egg.pos.getX(), egg.pos.getY(), egg.pos.getZ());
					egg.world.spawnEntity(newborn);
					
					egg.owner.startRiding(newborn, true);
					TAMED_DRAGONS.put(egg.owner.getUuid(), newborn.getUuid());
					
					HATCHING_EGGS.remove(egg);
					return;
				}
				
				if (progress != 0) {
					int finalProgress = progress;
					egg.world.getPlayers().forEach(player -> player.networkHandler.sendPacket(new BlockBreakingProgressS2CPacket(0, egg.pos, finalProgress)));
				}
			}
		});
	}
}
