package hunternif.mc.impl.atlas;

import hunternif.mc.impl.atlas.client.*;
import hunternif.mc.impl.atlas.marker.MarkerTextureConfig;
import hunternif.mc.impl.atlas.registry.MarkerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import hunternif.mc.impl.atlas.client.gui.ExportProgressOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements PreparableReloadListener {
    /**
     * Client setup, called from the Forge mod constructor on the client only. Reload listeners
     * are registered in dependency order (textures, then texture sets, then tile mappings);
     * Minecraft applies them in the order they're registered.
     */
    public void initClient(IEventBus modBus) {
        modBus.addListener((RegisterClientReloadListenersEvent event) -> {
            // read Textures first from assets
            TextureConfig textureConfig = new TextureConfig(Textures.TILE_TEXTURES_MAP);
            event.registerReloadListener(textureConfig);

            // then read TextureSets
            TextureSetMap textureSetMap = TextureSetMap.instance();
            TextureSetConfig textureSetConfig = new TextureSetConfig(textureSetMap);
            event.registerReloadListener(textureSetConfig);

            // After that, we can read the tile mappings
            TileTextureMap tileTextureMap = TileTextureMap.instance();
            TileTextureConfig tileTextureConfig = new TileTextureConfig(tileTextureMap, textureSetMap);
            event.registerReloadListener(tileTextureConfig);

            // Legacy file name:
            event.registerReloadListener(this);

            MarkerTextureConfig markerTextureConfig = new MarkerTextureConfig();
            event.registerReloadListener(markerTextureConfig);
        });

        modBus.addListener((RegisterKeyMappingsEvent event) -> KeyHandler.registerBindings(event));
        // Was a mixin into Gui.render, which Forge's HUD renderer doesn't call.
        MinecraftForge.EVENT_BUS.addListener((RenderGuiEvent.Post event) ->
                ExportProgressOverlay.INSTANCE.draw(event.getGuiGraphics(),
                        event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight()));
        MinecraftForge.EVENT_BUS.addListener(AtlasHandRenderer::onRenderHand);
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                KeyHandler.onClientTick(Minecraft.getInstance());
            }
        });
    }

    /**
     * Assign default textures to biomes defined in the client world, but
     * not part of the BuiltinRegistries.BIOME. This happens for all biomes
     * defined in data packs. Also, as these are only available per world,
     * we need the ClientWorld loaded here.
     */
    public static void assignCustomBiomeTextures(ClientLevel world) {
        for (Map.Entry<ResourceKey<Biome>, Biome> biome : world.registryAccess().registryOrThrow(Registries.BIOME).entrySet()) {
            ResourceLocation id = world.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome.getValue());
            if (!TileTextureMap.instance().isRegistered(id)) {
                TileTextureMap.instance().autoRegister(id, biome.getKey());
            }
        }

        /*for (Map.Entry<RegistryKey<Biome>, Biome> entry : world.getRegistryManager().get(RegistryKeys.BIOME).getEntrySet()) {
            Identifier id = world.getRegistryManager().get(RegistryKeys.BIOME).getId(entry.getValue());
            if (!TileTextureMap.instance().isRegistered(id)) {
                TileTextureMap.instance().autoRegister(id, entry.getKey());
            }
        }*/
    }

    @Override
    public String getName() {
        return AntiqueAtlasMod.id("proxy").toString();
    }

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier synchronizer, ResourceManager manager, ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.completedFuture(null).thenCompose(synchronizer::wait).thenCompose(t -> CompletableFuture.runAsync(() -> {
            for (MarkerType type : MarkerType.REGISTRY) {
                type.initMips();
            }
        }, applyExecutor));
    }
}
