package hunternif.mc.impl.atlas.compat;

import hunternif.mc.api.AtlasAPI;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.marker.MarkersData;
import hunternif.mc.impl.atlas.structure.AutoMarkers;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneActivatedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

/**
 * Optional Waystones support: when a player activates a waystone, it is marked with its name on
 * every atlas the player carries (like the "activated waystones" option of the 1.12.2
 * AntiqueAtlasAutoMarker mod in RLCraft Dregora).
 * <p>
 * Only loaded when the Waystones mod is installed.
 */
public final class WaystonesCompat {
    /** Two waystone markers this close (in blocks) are the same waystone. */
    private static final int SAME_WAYSTONE_DISTANCE = 2;

    private WaystonesCompat() {}

    public static void init() {
        Balm.getEvents().onEvent(WaystoneActivatedEvent.class, WaystonesCompat::onWaystoneActivated);
        AntiqueAtlasMod.LOG.info("Antique Atlas: Waystones found, activated waystones will be marked");
    }

    private static void onWaystoneActivated(WaystoneActivatedEvent event) {
        if (!AntiqueAtlasMod.CONFIG.waystoneMarkers) return;
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide || player.getServer() == null) return;
        IWaystone waystone = event.getWaystone();
        if (waystone == null) return;

        ServerLevel level = player.getServer().getLevel(waystone.getDimension());
        if (level == null) return;
        String typeText = AntiqueAtlasMod.CONFIG.waystoneMarkerType;
        ResourceLocation type = typeText.contains(":") ? ResourceLocation.tryParse(typeText) : AntiqueAtlasMod.id(typeText);
        if (type == null) return;

        BlockPos pos = waystone.getPos();
        Component label = Component.literal(waystone.hasName() ? waystone.getName() : "Waystone");
        for (int atlasID : AtlasAPI.getPlayerAtlases(player)) {
            MarkersData data = AntiqueAtlasMod.markersData.getMarkersData(atlasID, level);
            if (AutoMarkers.hasMarkerNear(data, level.dimension(), type, pos.getX(), pos.getZ(), SAME_WAYSTONE_DISTANCE)) {
                continue;
            }
            AtlasAPI.getMarkerAPI().putMarker(level, false, atlasID, type, label, pos.getX(), pos.getZ());
        }
    }
}
