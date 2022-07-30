package me.PianoPenguin471.DragonTamer.mixin;

import me.PianoPenguin471.DragonTamer.DragonTamer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonEntity.class)
public class DragonEntityMixin {
	@Inject(method = "canStartRiding", at = @At("HEAD"), cancellable = true)
	public void onCanStartRiding(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
		if (entity.getUuid().equals(DragonTamer.TAMED_DRAGONS.get(entity.getUuid()))) {
			cir.setReturnValue(true);
		}
	}
}
