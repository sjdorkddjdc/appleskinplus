package com.appleskinplus.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccessor {
    @Accessor("pos")
    void setCameraPos(Vec3d pos);

    @Accessor("yaw")
    void setCameraYaw(float yaw);

    @Accessor("pitch")
    void setCameraPitch(float pitch);
}
