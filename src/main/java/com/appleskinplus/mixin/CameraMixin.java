package com.appleskinplus.mixin;

import com.appleskinplus.FreecamController;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Invoker("setPos")
    protected abstract void appleskinplus$setPos(
            Vec3d pos
    );

    @Invoker("setRotation")
    protected abstract void appleskinplus$setRotation(
            float yaw,
            float pitch
    );

    @Inject(
            method = "update",
            at = @At("RETURN")
    )
    private void appleskinplus$overrideCamera(
            World area,
            Entity focusedEntity,
            boolean thirdPerson,
            boolean inverseView,
            float tickProgress,
            CallbackInfo ci
    ) {
        if (!FreecamController.isActive()) {
            return;
        }

        /*
         * Оставляем обычную логику Minecraft для F5,
         * но после неё ставим freecam.
         */
        appleskinplus$setPos(
                FreecamController.getPos()
        );

        appleskinplus$setRotation(
                FreecamController.getYaw(),
                FreecamController.getPitch()
        );
    }
}
