package hunternif.mc.impl.atlas.core.watcher;

import hunternif.mc.api.AtlasAPI;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Puts an skull marker to the player's death spot.
 *
 * @author Hunternif, Haven King
 */
public class DeathWatcher {
    public static void onPlayerDeath(Player player) {
        if (AntiqueAtlasMod.CONFIG.autoDeathMarker) {
            for (int atlasID : AtlasAPI.getPlayerAtlases(player)) {
                AtlasAPI.getMarkerAPI().putMarker(player.getCommandSenderWorld(), true, atlasID, new ResourceLocation("antiqueatlas:tomb"),
                        Component.translatable("gui.antiqueatlas.marker.tomb", player.getName()),
                        (int) player.getX(), (int) player.getZ());
            }
        }
    }
}
