package hunternif.mc.impl.atlas.client;

import com.mojang.blaze3d.platform.InputConstants;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import hunternif.mc.impl.atlas.AntiqueAtlasModClient;
import hunternif.mc.impl.atlas.client.gui.GuiAtlas;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;


@OnlyIn(Dist.CLIENT)
public class KeyHandler {
    public static final KeyMapping ATLAS_KEYMAPPING = new KeyMapping("key.openatlas.desc", InputConstants.Type.KEYSYM, 77, "key.antiqueatlas.category");

    /** Always registered; the key only does something when the atlas item isn't required. */
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(ATLAS_KEYMAPPING);
    }

    public static void onClientTick(Minecraft client) {
        while (ATLAS_KEYMAPPING.consumeClick()) {
            if (AntiqueAtlasMod.CONFIG.itemNeeded) {
                continue;
            }
            Screen currentScreen = Minecraft.getInstance().screen;
            if (currentScreen instanceof GuiAtlas) {
                currentScreen.onClose();
            } else {
                AntiqueAtlasModClient.openAtlasGUI();
            }
        }
    }
}
