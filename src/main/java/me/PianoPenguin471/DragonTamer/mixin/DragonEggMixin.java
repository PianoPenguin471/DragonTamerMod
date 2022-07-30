package me.PianoPenguin471.DragonTamer.mixin;

import me.PianoPenguin471.DragonTamer.DragonTamer;
import me.PianoPenguin471.DragonTamer.HatchingEgg;
import net.minecraft.block.BlockState;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DragonEggBlock.class)
public class DragonEggMixin {
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	public void onOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if (player.getStackInHand(hand).isOf(DragonTamer.TAMING_WAND) && !world.isClient) {
			player.sendMessage(Text.of("The egg is hatching!").copy().setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
			player.getStackInHand(hand).decrement(1);
			ServerWorld serverWorld = (ServerWorld) world;
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			DragonTamer.HATCHING_EGGS.add(new HatchingEgg(pos, 0, serverWorld, serverPlayer));
			cir.setReturnValue(ActionResult.CONSUME);
		}
	}
}
