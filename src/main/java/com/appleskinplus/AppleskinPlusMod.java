package com.appleskinplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class AppleskinPlusMod implements ClientModInitializer {

    public static final KeyMapping TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(
        new KeyMapping(
            "key.appleskinplus.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            "category.appleskinplus"
        )
    );

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_KEY.consumeClick()) {
                FreecamController.toggle();
            }
        });
    }
}
