package com.appleskinplus.mixin;

import com.appleskinplus.FreecamController;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @Shadow
    private MinecraftClient client;

    @Inject(
            method = "updateMouse",
            at = @At("HEAD"),
            cancellable = true
    )
    private void appleskinplus$freecamMouse(
            double timeDelta,
            CallbackInfo ci
    ) {
        if (!FreecamController.isActive()) {
            return;
        }

        if (client.currentScreen != null) {
            cursorDeltaX = 0.0;
            cursorDeltaY = 0.0;
            return;
        }

        double sensitivity =
                client.options
                        .getMouseSensitivity()
                        .getValue();

        double f =
                sensitivity * 0.6 + 0.2;

        double multiplier =
                f * f * f * 8.0;

        double deltaX =
                cursorDeltaX * multiplier;

        double deltaY =
                cursorDeltaY * multiplier;

        if (deltaX != 0.0 || deltaY != 0.0) {
            FreecamController.rotate(
                    deltaX,
                    deltaY
            );
        }

        /*
         * Игрок не получает эти delta.
         */
        cursorDeltaX = 0.0;
        cursorDeltaY = 0.0;

        ci.cancel();
    }

    @Inject(
            method = "onMouseScroll",
            at = @At("HEAD"),
            cancellable = true
    )
    private void appleskinplus$scroll(
            long window,
            double horizontal,
            double vertical,
            CallbackInfo ci
    ) {
        if (!FreecamController.isActive()) {
            return;
        }

        if (client.currentScreen != null) {
            return;
        }

        FreecamController.changeSpeed(vertical);

        ci.cancel();
    }
}
