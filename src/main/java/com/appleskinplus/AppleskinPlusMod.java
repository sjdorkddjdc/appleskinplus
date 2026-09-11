package com.appleskinplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AppleskinPlusMod implements ClientModInitializer {

    // Объявляем переменную, но НЕ регистрируем её статически
    public static KeyBinding TOGGLE_KEY;

    @Override
    public void onInitializeClient() {
        // Регистрация бинда строго внутри метода инициализации клиента
        TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.appleskinplus.toggle",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F4,
                        "category.appleskinplus"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            /*
             * Мир закончился / игрок вышел.
             * Полностью сбрасываем freecam.
             */
            if (client.world == null || client.player == null) {
                FreecamController.reset();
                return;
            }

            /*
             * Не переключаем freecam поверх GUI.
             */
            if (client.currentScreen == null) {
                while (TOGGLE_KEY != null && TOGGLE_KEY.wasPressed()) {
                    FreecamController.toggle();
                }
            }

            FreecamController.tick();
        });
    }
}
