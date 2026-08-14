package com.appleskinplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AppleskinPlusMod implements ClientModInitializer {

    public static final KeyBinding TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(
        new KeyBinding(
            "key.appleskinplus.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            "category.appleskinplus"
        )
    );

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_KEY.wasPressed()) {
                FreecamController.toggle();
            }
            // Теперь tick() вызывается здесь, а не через миксин
            FreecamController.tick();
        });
    }
}
