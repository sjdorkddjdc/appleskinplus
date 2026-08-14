package com.appleskinplus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FreecamController {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static boolean active = false;
    private static Vec3d cameraPos = Vec3d.ZERO;
    private static float cameraYaw = 0f;
    private static float cameraPitch = 0f;

    private static float savedPlayerYaw = 0f;
    private static float savedPlayerPitch = 0f;

    private static double speed = 0.2;

    public static boolean isActive()   { return active; }
    public static Vec3d getPos()       { return cameraPos; }
    public static float getYaw()       { return cameraYaw; }
    public static float getPitch()     { return cameraPitch; }

    public static void toggle() {
        if (CLIENT.player == null) return;
        if (!active) activate(); else deactivate();
    }

    private static void activate() {
        active = true;
        cameraPos = CLIENT.player.getCameraPosVec(1.0f);
        cameraYaw = CLIENT.player.getYaw();
        cameraPitch = CLIENT.player.getPitch();

        savedPlayerYaw = CLIENT.player.getYaw();
        savedPlayerPitch = CLIENT.player.getPitch();
    }

    private static void deactivate() {
        active = false;
    }

    public static void tick() {
        if (!active || CLIENT.player == null) return;

        // 1. Перехватываем поворот мыши
        float deltaYaw   = CLIENT.player.getYaw()   - savedPlayerYaw;
        float deltaPitch = CLIENT.player.getPitch() - savedPlayerPitch;

        cameraYaw   += deltaYaw;
        cameraPitch = MathHelper.clamp(cameraPitch + deltaPitch, -90.0f, 90.0f);

        // Восстанавливаем углы игрока, чтобы персонаж стоял ровно
        CLIENT.player.setYaw(savedPlayerYaw);
        CLIENT.player.setPitch(savedPlayerPitch);

        // 2. БЛОКИРУЕМ ввод игрока напрямую
        if (CLIENT.player.input != null) {
            CLIENT.player.input.movementForward = 0.0f;
            CLIENT.player.input.movementSideways = 0.0f;
            CLIENT.player.input.jumping = false;
            CLIENT.player.input.sneaking = false;
        }

        // 3. Движение камеры (используем сохранённые состояния клавиш)
        boolean forward  = CLIENT.options.forwardKey.isPressed();
        boolean back     = CLIENT.options.backKey.isPressed();
        boolean right    = CLIENT.options.rightKey.isPressed();
        boolean left     = CLIENT.options.leftKey.isPressed();
        boolean jump     = CLIENT.options.jumpKey.isPressed();
        boolean sneak    = CLIENT.options.sneakKey.isPressed();

        Vec3d move = Vec3d.ZERO;
        Vec3d fwd = Vec3d.fromPolar(0, cameraYaw);
        Vec3d rgt = Vec3d.fromPolar(0, cameraYaw + 90f);
        Vec3d up  = new Vec3d(0, 1, 0);

        if (forward) move = move.add(fwd);
        if (back)    move = move.subtract(fwd);
        if (right)   move = move.add(rgt);
        if (left)    move = move.subtract(rgt);
        if (jump)    move = move.add(up);
        if (sneak)   move = move.subtract(up);

        if (move.lengthSquared() > 0) {
            cameraPos = cameraPos.add(move.normalize().multiply(speed));
        }
    }

    public static void changeSpeed(double delta) {
        speed = MathHelper.clamp(speed + delta * 0.05, 0.05, 5.0);
    }
}
