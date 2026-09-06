package com.appleskinplus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.glfw.GLFW;

public final class FreecamController {

    private static final MinecraftClient CLIENT =
            MinecraftClient.getInstance();

    private static final double DEFAULT_SPEED = 0.20;
    private static final double MIN_SPEED = 0.05;
    private static final double MAX_SPEED = 5.0;

    private static boolean active = false;

    private static Vec3d cameraPos = Vec3d.ZERO;

    private static float cameraYaw;
    private static float cameraPitch;

    private static double speed = DEFAULT_SPEED;

    private FreecamController() {
    }

    public static boolean isActive() {
        return active;
    }

    public static Vec3d getPos() {
        return cameraPos;
    }

    public static float getYaw() {
        return cameraYaw;
    }

    public static float getPitch() {
        return cameraPitch;
    }

    public static void toggle() {
        if (CLIENT.player == null || CLIENT.world == null) {
            return;
        }

        if (CLIENT.currentScreen != null) {
            return;
        }

        if (active) {
            deactivate();
        } else {
            activate();
        }
    }

    private static void activate() {
        if (CLIENT.player == null) {
            return;
        }

        active = true;

        cameraPos = CLIENT.player.getCameraPosVec(1.0f);

        cameraYaw = CLIENT.player.getYaw();
        cameraPitch = CLIENT.player.getPitch();
    }

    public static void deactivate() {
        active = false;
    }

    public static void rotate(
            double deltaX,
            double deltaY
    ) {
        if (!active) {
            return;
        }

        cameraYaw += (float) deltaX * 0.15f;

        cameraPitch -= (float) deltaY * 0.15f;

        cameraPitch = MathHelper.clamp(
                cameraPitch,
                -90.0f,
                90.0f
        );
    }

    public static void tick() {

        if (CLIENT.player == null || CLIENT.world == null) {
            reset();
            return;
        }

        if (!active) {
            return;
        }

        /*
         * GUI открыт:
         * камера не двигается.
         */
        if (CLIENT.currentScreen != null) {
            return;
        }

        long window =
                CLIENT.getWindow().getHandle();

        Vec3d movement = Vec3d.ZERO;

        /*
         * W/S остаются горизонтальными.
         * Pitch камеры не влияет на движение.
         */
        Vec3d forward =
                Vec3d.fromPolar(0.0f, cameraYaw);

        Vec3d right =
                Vec3d.fromPolar(
                        0.0f,
                        cameraYaw + 90.0f
                );

        Vec3d up =
                new Vec3d(0.0, 1.0, 0.0);

        if (isPressed(window, GLFW.GLFW_KEY_W)) {
            movement = movement.add(forward);
        }

        if (isPressed(window, GLFW.GLFW_KEY_S)) {
            movement = movement.subtract(forward);
        }

        if (isPressed(window, GLFW.GLFW_KEY_A)) {
            movement = movement.subtract(right);
        }

        if (isPressed(window, GLFW.GLFW_KEY_D)) {
            movement = movement.add(right);
        }

        if (isPressed(window, GLFW.GLFW_KEY_SPACE)) {
            movement = movement.add(up);
        }

        if (isPressed(window, GLFW.GLFW_KEY_LEFT_SHIFT)) {
            movement = movement.subtract(up);
        }

        if (movement.lengthSquared() == 0.0) {
            return;
        }

        /*
         * Нормализация:
         *
         * W       = 1.0 speed
         * W + D   = 1.0 speed
         *
         * Нет ускорения по диагонали.
         */
        movement =
                movement.normalize()
                        .multiply(speed);

        /*
         * ВАЖНО:
         *
         * НИКАКИХ COLLISION CHECK.
         *
         * Камера свободно проходит через блоки.
         */
        cameraPos =
                cameraPos.add(movement);
    }

    private static boolean isPressed(
            long window,
            int key
    ) {
        return GLFW.glfwGetKey(
                window,
                key
        ) == GLFW.GLFW_PRESS;
    }

    public static void changeSpeed(
            double amount
    ) {
        speed = MathHelper.clamp(
                speed + amount * 0.05,
                MIN_SPEED,
                MAX_SPEED
        );
    }

    public static void reset() {
        active = false;

        cameraPos = Vec3d.ZERO;

        cameraYaw = 0.0f;
        cameraPitch = 0.0f;

        speed = DEFAULT_SPEED;
    }
}
