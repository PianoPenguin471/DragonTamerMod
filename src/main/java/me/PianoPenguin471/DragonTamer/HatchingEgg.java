package me.PianoPenguin471.DragonTamer;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class HatchingEgg {
	public BlockPos pos;
	public int ticksExisted;
	public ServerWorld world;
	public ServerPlayerEntity owner;
	
	public HatchingEgg(BlockPos pos, int ticksExisted, ServerWorld world, ServerPlayerEntity owner) {
		this.pos = pos;
		this.ticksExisted = ticksExisted;
		this.world = world;
		this.owner = owner;
	}
}
