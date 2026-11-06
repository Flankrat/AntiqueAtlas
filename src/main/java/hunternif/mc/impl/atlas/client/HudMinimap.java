package hunternif.mc.impl.atlas.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import hunternif.mc.api.client.AtlasClientAPI;
import hunternif.mc.impl.atlas.AntiqueAtlasConfig;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.client.gui.GuiAtlas;
import hunternif.mc.impl.atlas.client.texture.ITexture;
import hunternif.mc.impl.atlas.client.texture.Texture;
import hunternif.mc.impl.atlas.core.WorldData;
import hunternif.mc.impl.atlas.item.AtlasItem;
import hunternif.mc.impl.atlas.marker.DimensionMarkersData;
import hunternif.mc.impl.atlas.marker.Marker;
import hunternif.mc.impl.atlas.marker.MarkersData;
import hunternif.mc.impl.atlas.registry.MarkerRenderInfo;
import hunternif.mc.impl.atlas.registry.MarkerType;
import hunternif.mc.impl.atlas.util.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The corner minimap: while you hold an atlas, a small copy of its map is drawn in a corner of
 * the screen, centered on you.
 * <p>
 * Ported from Antique Atlas 4.6 for 1.12.2 (Kenkron's Antique Atlas Overlay, merged into the
 * mod there), which RLCraft and RLCraft Dregora use. Antique Atlas 7 replaced it with drawing the
 * map on the atlas in your hand; this brings the 1.12 behaviour and settings back
 * ({@code [minimap]} in the config).
 */
@OnlyIn(Dist.CLIENT)
public final class HudMinimap {
    /** Blocks per chunk. */
    private static final int CHUNK_SIZE = 16;

    private HudMinimap() {}

    public static void onRenderGui(RenderGuiEvent.Post event) {
        AntiqueAtlasConfig config = AntiqueAtlasMod.CONFIG;
        if (!config.minimapEnabled) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        // The minimap hides while the atlas screen is open.
        if (mc.screen instanceof GuiAtlas || mc.level == null || mc.player == null) {
            return;
        }

        Integer atlasID = config.minimapRequiresHold ? getHeldAtlas(mc.player) : getCarriedAtlas(mc.player);
        if (atlasID == null) {
            return;
        }

        int gameWidth = event.getWindow().getGuiScaledWidth();
        int gameHeight = event.getWindow().getGuiScaledHeight();
        // y = 0 is the top of the screen.
        Rect bounds = new Rect().setOrigin(config.minimapX, config.minimapY);
        if (config.minimapAlignRight) {
            bounds.minX = gameWidth - (config.minimapWidth + config.minimapX);
        }
        if (config.minimapAlignBottom) {
            bounds.minY = gameHeight - (config.minimapHeight + config.minimapY);
        }
        bounds.setSize(config.minimapWidth, config.minimapHeight);

        drawMinimap(event.getGuiGraphics(), bounds, atlasID, mc.player, mc.level, config,
                (int) mc.getWindow().getGuiScale());
    }

    /** The atlas in the main hand, else the off hand. */
    private static Integer getHeldAtlas(Player player) {
        if (!AntiqueAtlasMod.CONFIG.itemNeeded) {
            return player.getUUID().hashCode();
        }
        ItemStack main = player.getMainHandItem();
        if (!main.isEmpty() && main.getItem() instanceof AtlasItem) {
            return AtlasItem.getAtlasID(main);
        }
        ItemStack off = player.getOffhandItem();
        if (!off.isEmpty() && off.getItem() instanceof AtlasItem) {
            return AtlasItem.getAtlasID(off);
        }
        return null;
    }

    /** The atlas in the off hand, else the first one in the hotbar (requiresHold = false). */
    private static Integer getCarriedAtlas(Player player) {
        if (!AntiqueAtlasMod.CONFIG.itemNeeded) {
            return player.getUUID().hashCode();
        }
        ItemStack stack = player.getOffhandItem();
        if (!stack.isEmpty() && stack.getItem() instanceof AtlasItem) {
            return AtlasItem.getAtlasID(stack);
        }
        for (int i = 0; i < 9; i++) {
            stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof AtlasItem) {
                return AtlasItem.getAtlasID(stack);
            }
        }
        return null;
    }

    private static void drawMinimap(GuiGraphics graphics, Rect shape, int atlasID, LocalPlayer player, Level world,
                                    AntiqueAtlasConfig config, int screenScale) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Textures.BOOK.draw(graphics, shape.minX, shape.minY, shape.getWidth(), shape.getHeight());

        Rect innerShape = new Rect(
                shape.minX + Math.round(config.minimapBorderX * shape.getWidth()),
                shape.minY + Math.round(config.minimapBorderY * shape.getHeight()),
                shape.maxX - Math.round(config.minimapBorderX * shape.getWidth()),
                shape.maxY - Math.round(config.minimapBorderY * shape.getHeight()));

        Vec3 position = player.position();
        drawTiles(graphics, innerShape, atlasID, position, world, config);

        if (config.minimapMarkerSize > 0) {
            drawMarkers(graphics, innerShape, atlasID, position, world, config, screenScale);
            int shapeMiddleX = (shape.minX + shape.maxX) / 2;
            int shapeMiddleY = (shape.minY + shape.maxY) / 2;
            drawPlayer(graphics, shapeMiddleX, shapeMiddleY, player.getYHeadRot(), config);
        }

        // Overlay the frame so that the edges of the map are smooth:
        RenderSystem.setShaderColor(1, 1, 1, 1);
        Textures.BOOK_FRAME.draw(graphics, shape.minX, shape.minY, shape.getWidth(), shape.getHeight());
        RenderSystem.disableBlend();
    }

    private static void drawTiles(GuiGraphics graphics, Rect shape, int atlasID, Vec3 position, Level world,
                                  AntiqueAtlasConfig config) {
        graphics.enableScissor(shape.minX, shape.minY, shape.maxX, shape.maxY);

        Rect iteratorScope = getChunkCoverage(position, shape, config.minimapTileSize);
        TileRenderIterator iter = AtlasClientAPI.getTileAPI().getTiles(world, atlasID, iteratorScope, 1);

        double chunkX = position.x / CHUNK_SIZE;
        double chunkZ = position.z / CHUNK_SIZE;
        int shapeMiddleX = (shape.minX + shape.maxX) / 2;
        int shapeMiddleY = (shape.minY + shape.maxY) / 2;
        int tileHalfSize = config.minimapTileSize / 2;

        // Group the quarter-tiles by texture so each texture is bound once.
        Map<ResourceLocation, List<int[]>> byTexture = new HashMap<>();
        while (iter.hasNext()) {
            SubTileQuartet subtiles = iter.next();
            for (SubTile subtile : subtiles) {
                if (subtile == null || subtile.tile == null)
                    continue;
                // Position of this subtile (in chunks) relative to the player
                float relativeChunkPositionX = (float) (subtile.x / 2.0 + iteratorScope.minX - chunkX);
                float relativeChunkPositionY = (float) (subtile.y / 2.0 + iteratorScope.minY - chunkZ);
                ResourceLocation texture = TileTextureMap.instance().getTexture(subtile).getTexture();
                byTexture.computeIfAbsent(texture, k -> new ArrayList<>()).add(new int[]{
                        shapeMiddleX + (int) Math.floor(relativeChunkPositionX * config.minimapTileSize),
                        shapeMiddleY + (int) Math.floor(relativeChunkPositionY * config.minimapTileSize),
                        subtile.getTextureU(),
                        subtile.getTextureV()});
            }
        }
        for (Map.Entry<ResourceLocation, List<int[]>> entry : byTexture.entrySet()) {
            // Tile textures are a 4 x 6 grid of quarter-tile pieces.
            ITexture texture = new Texture(entry.getKey(), 4, 6, false);
            for (int[] c : entry.getValue()) {
                texture.draw(graphics, c[0], c[1], tileHalfSize, tileHalfSize, c[2], c[3], 1, 1);
            }
        }

        graphics.disableScissor();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static void drawMarkers(GuiGraphics graphics, Rect shape, int atlasID, Vec3 position, Level world,
                                    AntiqueAtlasConfig config, int screenScale) {
        graphics.enableScissor(shape.minX, shape.minY, shape.maxX, shape.maxY);

        // The tile data is needed to hide markers in places that haven't been explored yet.
        WorldData biomeData = AntiqueAtlasMod.tileData.getData(atlasID, world).getWorldData(world.dimension());

        // Global markers:
        DimensionMarkersData globalMarkersData = AntiqueAtlasMod.globalMarkersData.getData()
                .getMarkersDataInWorld(world.dimension());
        drawMarkersData(graphics, globalMarkersData, shape, biomeData, position, config, screenScale);

        // This atlas' own markers:
        MarkersData markersData = AntiqueAtlasMod.markersData.getMarkersData(atlasID, world);
        if (markersData != null) {
            drawMarkersData(graphics, markersData.getMarkersDataInWorld(world.dimension()), shape, biomeData,
                    position, config, screenScale);
        }

        graphics.disableScissor();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static void drawMarkersData(GuiGraphics graphics, DimensionMarkersData markersData, Rect shape,
                                        WorldData biomeData, Vec3 position, AntiqueAtlasConfig config, int screenScale) {
        if (markersData == null) {
            return;
        }
        int markerSize = config.minimapMarkerSize;
        int tileSize = config.minimapTileSize;

        // Large enough to include markers that are bigger than tiles.
        Rect markerShape = new Rect(shape.minX - markerSize / 2, shape.minY - markerSize / 2,
                shape.maxX + markerSize / 2, shape.maxY + markerSize / 2);

        Rect mcchunks = getChunkCoverage(position, markerShape, tileSize);
        Rect chunks = new Rect((int) Math.floor(mcchunks.minX / (double) MarkersData.CHUNK_STEP),
                (int) Math.floor(mcchunks.minY / (double) MarkersData.CHUNK_STEP),
                (int) Math.ceil(mcchunks.maxX / (double) MarkersData.CHUNK_STEP),
                (int) Math.ceil(mcchunks.maxY / (double) MarkersData.CHUNK_STEP));

        int shapeMiddleX = (shape.minX + shape.maxX) / 2;
        int shapeMiddleY = (shape.minY + shape.maxY) / 2;

        for (int x = chunks.minX; x <= chunks.maxX; x++) {
            for (int z = chunks.minY; z <= chunks.maxY; z++) {
                // A marker "chunk" is CHUNK_STEP Minecraft chunks wide.
                List<Marker> markers = markersData.getMarkersAtChunk(x, z);
                if (markers == null)
                    continue;
                for (Marker marker : markers) {
                    // Position of this marker relative to the player, rounded to the nearest even number
                    int relativeX = tileSize * (2 * (marker.getX() / 2) - 2 * (int) Math.floor(position.x / 2)) / CHUNK_SIZE;
                    int relativeY = tileSize * (2 * (marker.getZ() / 2) - 2 * (int) Math.floor(position.z / 2)) / CHUNK_SIZE;
                    int guiX = shapeMiddleX - markerSize / 2 + relativeX;
                    int guiY = shapeMiddleY - markerSize / 2 + relativeY;
                    renderMarker(graphics, marker, guiX, guiY, biomeData, markerSize, tileSize, screenScale);
                }
            }
        }
    }

    private static void renderMarker(GuiGraphics graphics, Marker marker, int x, int y, WorldData biomeData,
                                     int markerSize, int tileSize, int screenScale) {
        if (!marker.isVisibleAhead() && !biomeData.hasTileAt(marker.getChunkX(), marker.getChunkZ())) {
            return;
        }
        MarkerType type = MarkerType.REGISTRY.get(marker.getType());
        if (type == null) {
            return;
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
        MarkerRenderInfo info = type.getRenderInfo(1, tileSize, screenScale);
        info.tex.draw(graphics, x, y, markerSize, markerSize);
    }

    private static void drawPlayer(GuiGraphics graphics, float x, float y, float rotation, AntiqueAtlasConfig config) {
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(180 + rotation));
        graphics.pose().translate(-config.minimapPlayerIconWidth / 2.0, -config.minimapPlayerIconHeight / 2.0, 0);
        Textures.PLAYER.draw(graphics, 0, 0, config.minimapPlayerIconWidth, config.minimapPlayerIconHeight);
        graphics.pose().popPose();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    /** The chunks (with a margin) that the window around the player covers. */
    private static Rect getChunkCoverage(Vec3 position, Rect windowShape, int tileSize) {
        int minChunkX = (int) Math.floor(position.x / CHUNK_SIZE - windowShape.getWidth() / (2f * tileSize)) - 1;
        int minChunkY = (int) Math.floor(position.z / CHUNK_SIZE - windowShape.getHeight() / (2f * tileSize)) - 1;
        int maxChunkX = (int) Math.ceil(position.x / CHUNK_SIZE + windowShape.getWidth() / (2f * tileSize)) + 1;
        int maxChunkY = (int) Math.ceil(position.z / CHUNK_SIZE + windowShape.getHeight() / (2f * tileSize)) + 1;
        return new Rect(minChunkX, minChunkY, maxChunkX, maxChunkY);
    }
}
