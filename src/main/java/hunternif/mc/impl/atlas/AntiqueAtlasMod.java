package hunternif.mc.impl.atlas;

import hunternif.mc.impl.atlas.core.AtlasIdData;
import hunternif.mc.impl.atlas.core.GlobalTileDataHandler;
import hunternif.mc.impl.atlas.core.PlayerEventHandler;
import hunternif.mc.impl.atlas.core.TileDataHandler;
import hunternif.mc.impl.atlas.core.scaning.WorldScanner;
import hunternif.mc.impl.atlas.event.RecipeCraftedCallback;
import hunternif.mc.impl.atlas.event.RecipeCraftedHandler;
import hunternif.mc.impl.atlas.item.AntiqueAtlasItems;
import hunternif.mc.impl.atlas.marker.GlobalMarkersDataHandler;
import hunternif.mc.impl.atlas.marker.MarkersDataHandler;
import hunternif.mc.impl.atlas.mixinhooks.NewPlayerConnectionCallback;
import hunternif.mc.impl.atlas.mixinhooks.NewServerConnectionCallback;
import hunternif.mc.impl.atlas.network.AntiqueAtlasNetworking;
import hunternif.mc.impl.atlas.structure.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AntiqueAtlasMod {
    public static final String ID = "antiqueatlas";
    public static final String NAME = "Antique Atlas";

    public static Logger LOG = LogManager.getLogger(NAME);

    public static final WorldScanner worldScanner = new WorldScanner();
    public static final TileDataHandler tileData = new TileDataHandler();
    public static final MarkersDataHandler markersData = new MarkersDataHandler();

    public static final GlobalTileDataHandler globalTileData = new GlobalTileDataHandler();
    public static final GlobalMarkersDataHandler globalMarkersData = new GlobalMarkersDataHandler();

    public static AntiqueAtlasConfig CONFIG = new AntiqueAtlasConfig();

    public static ResourceLocation id(String... path) {
        return path[0].contains(":") ? new ResourceLocation(String.join(".", path)) : new ResourceLocation(ID, String.join(".", path));
    }

    public static AtlasIdData getAtlasIdData(Level world) {
        if (world.isClientSide()) {
            LOG.warn("Tried to access server only data from client.");
            return null;
        }

        return ((ServerLevel) world).getDataStorage().computeIfAbsent(AtlasIdData::fromNbt, AtlasIdData::new, "antiqueatlas:global_atlas_data");
    }

    /** Called from the Forge mod constructor (both sides). */
    public static void init(IEventBus modBus) {
        //We just check for tags now
        //TileDetectorBase.scanBiomeTypes();

        AntiqueAtlasItems.register(modBus);

        AntiqueAtlasNetworking.registerC2SListeners();

        NewServerConnectionCallback.EVENT.register(tileData::onClientConnectedToServer);
        NewServerConnectionCallback.EVENT.register(markersData::onClientConnectedToServer);
        NewServerConnectionCallback.EVENT.register(globalMarkersData::onClientConnectedToServer);

        NewPlayerConnectionCallback.EVENT.register(globalMarkersData::onPlayerLogin);
        NewPlayerConnectionCallback.EVENT.register(globalTileData::onPlayerLogin);
        NewPlayerConnectionCallback.EVENT.register(PlayerEventHandler::onPlayerLogin);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener((LevelEvent.Load event) -> {
            if (event.getLevel() instanceof ServerLevel level) {
                globalMarkersData.onWorldLoad(level);
                globalTileData.onWorldLoad(level);
            }
        });

        RecipeCraftedCallback.EVENT.register(new RecipeCraftedHandler());

        StructurePieceAddedCallback.EVENT.register(StructureHandler::resolve);
        StructureAddedCallback.EVENT.register(StructureHandler::resolve);

        NetherFortress.registerPieces();
        EndCity.registerMarkers();
        Village.registerMarkers();
        Overworld.registerPieces();

        JigsawConfig jigsawConfig = new JigsawConfig();
        forgeBus.addListener((AddReloadListenerEvent event) -> event.addListener(jigsawConfig));
    }
}
