package com.appleskinplus.mixin;

import com.appleskinplus.FreecamController;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private Vec3d pos;
    @Shadow private float yaw;
    @Shadow private float pitch;

    @Inject(method = "update", at = @At("RETURN"))
    private void appleskinplus$overrideCamera(CallbackInfo ci) {
        if (FreecamController.isActive()) {
            this.pos = FreecamController.getPos();
            this.yaw = FreecamController.getYaw();
            this.pitch = FreecamController.getPitch();
        }
    }
}
