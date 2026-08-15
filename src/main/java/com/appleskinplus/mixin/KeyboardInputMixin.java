package com.appleskinplus.mixin;

import com.appleskinplus.FreecamController;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {

    @Inject(method = "tick", at = @At("RETURN"))
    private void appleskinplus$blockMovement(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        if (FreecamController.isActive()) {
            Input self = (Input) (Object) this;
            self.movementForward = 0.0f;
            self.movementSideways = 0.0f;
            self.jumping = false;
            self.sneaking = false;
        }
    }
}
