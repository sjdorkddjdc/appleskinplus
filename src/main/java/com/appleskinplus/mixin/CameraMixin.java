package com.appleskinplus.mixin;

import com.appleskinplus.FreecamController;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "update", at = @At("RETURN"))
    private void appleskinplus$overrideCamera(CallbackInfo ci) {
        if (FreecamController.isActive()) {
            CameraAccessor accessor = (CameraAccessor)(Object)this;
            accessor.setCameraPos(FreecamController.getPos());
            accessor.setCameraYaw(FreecamController.getYaw());
            accessor.setCameraPitch(FreecamController.getPitch());
        }
    }
}
