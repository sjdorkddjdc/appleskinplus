package com.appleskinplus.mixin;

import com.appleskinplus.FreecamController;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void appleskinplus$tick(CallbackInfo ci) {
        FreecamController.tick();
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void appleskinplus$blockAttack(CallbackInfoReturnable<Boolean> cir) {
        if (FreecamController.isActive()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void appleskinplus$blockItemUse(CallbackInfo ci) {
        if (FreecamController.isActive()) {
            ci.cancel();
        }
    }
}
