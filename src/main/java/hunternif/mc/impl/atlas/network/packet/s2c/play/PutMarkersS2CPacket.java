package hunternif.mc.impl.atlas.network.packet.s2c.play;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import hunternif.mc.impl.atlas.network.NetworkManager;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.AntiqueAtlasModClient;
import hunternif.mc.impl.atlas.marker.Marker;
import hunternif.mc.impl.atlas.marker.MarkersData;
import hunternif.mc.impl.atlas.network.packet.s2c.S2CPacket;
import hunternif.mc.impl.atlas.registry.MarkerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import java.util.Collection;
import java.util.List;

/**
 * Sends markers set via API from server to client.
 * Only one dimension per packet.
 * The markers in one packet are either all global or all local.
 *
 * @author Hunternif
 * @author Haven King
 */
public class PutMarkersS2CPacket extends S2CPacket {
    public static final ResourceLocation ID = AntiqueAtlasMod.id("packet", "s2c", "marker", "put");

    private static final int GLOBAL = -1;

    public PutMarkersS2CPacket(int atlasID, ResourceKey<Level> world, Collection<Marker> markers) {
        ListMultimap<ResourceLocation, Marker> markersByType = ArrayListMultimap.create();
        for (Marker marker : markers) {
            markersByType.put(marker.getType(), marker);
        }

        this.writeVarInt(atlasID);
        this.writeResourceLocation(world.location());
        this.writeVarInt(markersByType.keySet().size());

        for (ResourceLocation type : markersByType.keySet()) {
            this.writeResourceLocation(type);
            List<Marker> markerList = markersByType.get(type);
            this.writeVarInt(markerList.size());
            for (Marker marker : markerList) {
                marker.write(this);
            }
        }
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @OnlyIn(Dist.CLIENT)
    public static void apply(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        int atlasID = buf.readVarInt();
        ResourceKey<Level> world = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        int typesLength = buf.readVarInt();

        ListMultimap<ResourceLocation, Marker.Precursor> markersByType = ArrayListMultimap.create();
        for (int i = 0; i < typesLength; ++i) {
            ResourceLocation type = buf.readResourceLocation();
            int markersLength = buf.readVarInt();
            for (int j = 0; j < markersLength; ++j) {
                markersByType.put(type, new Marker.Precursor(buf));
            }
        }

        context.queue(() -> {
            MarkersData markersData = atlasID == GLOBAL
                    ? AntiqueAtlasMod.globalMarkersData.getData()
                    : AntiqueAtlasMod.markersData.getMarkersDataCached(atlasID, world);

            for (ResourceLocation type : markersByType.keys()) {
                MarkerType markerType = MarkerType.REGISTRY.get(type);
                for (Marker.Precursor precursor : markersByType.get(type)) {
                    markersData.loadMarker(new Marker(MarkerType.REGISTRY.getKey(markerType), world, precursor));
                }
            }

            AntiqueAtlasModClient.getAtlasGUI().updateBookmarkerList();
        });
    }
}
