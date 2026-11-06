package hunternif.mc.impl.atlas;

import hunternif.mc.impl.atlas.client.gui.GuiAtlas;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Antique Atlas settings. The rest of the mod reads the public fields directly
 * ({@code AntiqueAtlasMod.CONFIG.itemNeeded} and so on); {@link #SPEC} is the Forge config
 * file ({@code config/antiqueatlas-common.toml}) that fills them in through {@link #bake()}.
 * <p>
 * This used to be a Cloth Config AutoConfig class; the options, defaults and comments are the same.
 */
public class AntiqueAtlasConfig {
    //============ Gameplay settings ==============
    public boolean doSaveBrowsingPos = true;
    public boolean autoDeathMarker = true;
    public boolean autoVillageMarkers = false;
    public boolean autoNetherPortalMarkers = true;
    public boolean itemNeeded = true;

    //============ Interface settings =============
    public boolean doScaleMarkers = false;
    public double defaultScale = 0.5f;
    public double minScale = 1.0 / 32.0;
    public double maxScale = 4;
    public boolean doReverseWheelZoom = false;

    //=========== Performance settings ============
    public int scanRadius = 11;
    public boolean forceChunkLoading = false;
    public float newScanInterval = 1f;
    public boolean doRescan = true;
    public int rescanRate = 4;
    public int markerLimit = 1024;
    public boolean doScanPonds = true;
    public boolean doScanRavines = true;
    public boolean debugRender = false;
    public boolean resourcePackLogging = false;

    //============ Appearance settings ============
    public int tileSize = 8;
    public int markerSize = GuiAtlas.MARKER_SIZE / 2;
    public int playerIconWidth = 14;
    public int playerIconHeight = 16;
    public boolean mapOnHeldAtlas = false;

    //============ Corner minimap (1.12.2 overlay) ============
    // Defaults are the RLCraft Dregora settings (config/antiqueatlas/atlas_overlay.cfg there).
    public boolean minimapEnabled = true;
    public boolean minimapRequiresHold = true;
    public boolean minimapAlignRight = true;
    public boolean minimapAlignBottom = false;
    public int minimapX = 2;
    public int minimapY = 2;
    public int minimapWidth = GuiAtlas.WIDTH / 2;
    public int minimapHeight = GuiAtlas.HEIGHT / 2;
    public int minimapTileSize = 8;
    public int minimapMarkerSize = GuiAtlas.MARKER_SIZE / 2;
    public int minimapPlayerIconWidth = 7;
    public int minimapPlayerIconHeight = 8;
    public float minimapBorderX = 0.0F;
    public float minimapBorderY = 0.0F;

    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.BooleanValue DO_SAVE_BROWSING_POS;
    private static final ForgeConfigSpec.BooleanValue AUTO_DEATH_MARKER;
    private static final ForgeConfigSpec.BooleanValue AUTO_VILLAGE_MARKERS;
    private static final ForgeConfigSpec.BooleanValue AUTO_NETHER_PORTAL_MARKERS;
    private static final ForgeConfigSpec.BooleanValue ITEM_NEEDED;

    private static final ForgeConfigSpec.BooleanValue DO_SCALE_MARKERS;
    private static final ForgeConfigSpec.DoubleValue DEFAULT_SCALE;
    private static final ForgeConfigSpec.DoubleValue MIN_SCALE;
    private static final ForgeConfigSpec.DoubleValue MAX_SCALE;
    private static final ForgeConfigSpec.BooleanValue DO_REVERSE_WHEEL_ZOOM;

    private static final ForgeConfigSpec.IntValue SCAN_RADIUS;
    private static final ForgeConfigSpec.BooleanValue FORCE_CHUNK_LOADING;
    private static final ForgeConfigSpec.DoubleValue NEW_SCAN_INTERVAL;
    private static final ForgeConfigSpec.BooleanValue DO_RESCAN;
    private static final ForgeConfigSpec.IntValue RESCAN_RATE;
    private static final ForgeConfigSpec.IntValue MARKER_LIMIT;
    private static final ForgeConfigSpec.BooleanValue DO_SCAN_PONDS;
    private static final ForgeConfigSpec.BooleanValue DO_SCAN_RAVINES;
    private static final ForgeConfigSpec.BooleanValue DEBUG_RENDER;
    private static final ForgeConfigSpec.BooleanValue RESOURCE_PACK_LOGGING;

    private static final ForgeConfigSpec.IntValue TILE_SIZE;
    private static final ForgeConfigSpec.IntValue MARKER_SIZE;
    private static final ForgeConfigSpec.IntValue PLAYER_ICON_WIDTH;
    private static final ForgeConfigSpec.IntValue PLAYER_ICON_HEIGHT;
    private static final ForgeConfigSpec.BooleanValue MAP_ON_HELD_ATLAS;

    private static final ForgeConfigSpec.BooleanValue MINIMAP_ENABLED;
    private static final ForgeConfigSpec.BooleanValue MINIMAP_REQUIRES_HOLD;
    private static final ForgeConfigSpec.BooleanValue MINIMAP_ALIGN_RIGHT;
    private static final ForgeConfigSpec.BooleanValue MINIMAP_ALIGN_BOTTOM;
    private static final ForgeConfigSpec.IntValue MINIMAP_X;
    private static final ForgeConfigSpec.IntValue MINIMAP_Y;
    private static final ForgeConfigSpec.IntValue MINIMAP_WIDTH;
    private static final ForgeConfigSpec.IntValue MINIMAP_HEIGHT;
    private static final ForgeConfigSpec.IntValue MINIMAP_TILE_SIZE;
    private static final ForgeConfigSpec.IntValue MINIMAP_MARKER_SIZE;
    private static final ForgeConfigSpec.IntValue MINIMAP_PLAYER_ICON_WIDTH;
    private static final ForgeConfigSpec.IntValue MINIMAP_PLAYER_ICON_HEIGHT;
    private static final ForgeConfigSpec.DoubleValue MINIMAP_BORDER_X;
    private static final ForgeConfigSpec.DoubleValue MINIMAP_BORDER_Y;

    static {
        AntiqueAtlasConfig d = new AntiqueAtlasConfig();
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("gameplay");
        DO_SAVE_BROWSING_POS = b.comment(
                "Whether to remember last open browsing position and zoom level for each dimension in every atlas.",
                "If disabled, all dimensions and all atlases will be \"synchronized\" at the same coordinates and zoom level, and map will \"follow\" player by default.")
                .define("doSaveBrowsingPos", d.doSaveBrowsingPos);
        AUTO_DEATH_MARKER = b.comment("Whether to add local marker for the spot where the player died.")
                .define("autoDeathMarker", d.autoDeathMarker);
        AUTO_VILLAGE_MARKERS = b.comment("Whether to add global markers for NPC villages.")
                .define("autoVillageMarkers", d.autoVillageMarkers);
        AUTO_NETHER_PORTAL_MARKERS = b.comment("Whether to add global markers for Nether Portals.")
                .define("autoNetherPortalMarkers", d.autoNetherPortalMarkers);
        ITEM_NEEDED = b.comment("Player will need to craft atlas item to use atlas.",
                        "If false, the atlas opens with a key (M by default) and every player has their own map.")
                .define("itemNeeded", d.itemNeeded);
        b.pop();

        b.push("userInterface");
        DO_SCALE_MARKERS = b.comment("Whether markers get bigger and smaller as you zoom.")
                .define("doScaleMarkers", d.doScaleMarkers);
        DEFAULT_SCALE = b.comment("Default zoom level. The number corresponds to the size of a block on the map relative to the size of a GUI pixel. Preferrably a power of 2.")
                .defineInRange("defaultScale", d.defaultScale, 0.001953125, 16.0);
        MIN_SCALE = b.comment("Minimum zoom level. The number corresponds to the size of a block on the map relative to the size of a GUI pixel. Preferrably a power of 2. Smaller values may decrease performance!")
                .defineInRange("minScale", d.minScale, 0.001953125, 16.0);
        MAX_SCALE = b.comment("Maximum zoom level. The number corresponds to the size of a block on the map relative to the size of a GUI pixel. Preferrably a power of 2.")
                .defineInRange("maxScale", d.maxScale, 0.001953125, 16.0);
        DO_REVERSE_WHEEL_ZOOM = b.comment("If false (by default), then mousewheel up is zoom in, mousewheel down is zoom out.",
                        "If true, then the direction is reversed.")
                .define("doReverseWheelZoom", d.doReverseWheelZoom);
        b.pop();

        b.push("performance");
        SCAN_RADIUS = b.comment("The radius of the area around the player which is scanned by the Atlas at regular intervals.",
                        "Note that this will not force faraway chunks to load, unless forceChunkLoading is enabled.",
                        "Lower value gives better performance.")
                .defineInRange("scanRadius", d.scanRadius, 0, 128);
        FORCE_CHUNK_LOADING = b.comment("Force loading of chunks within scan radius even if it exceeds regular chunk loading distance.",
                        "Enabling this may SEVERELY decrease performance!")
                .define("forceChunkLoading", d.forceChunkLoading);
        NEW_SCAN_INTERVAL = b.comment("Time in seconds between two scans of the area.", "Higher value gives better performance.")
                .defineInRange("newScanInterval", (double) d.newScanInterval, 0.05, 60.0);
        DO_RESCAN = b.comment("Whether to rescan chunks in the area that have been previously mapped. This is useful in case of changes in coastline (including small ponds of water and lava), or if land disappears completely (for sky worlds).",
                        "Disable for better performance.")
                .define("doRescan", d.doRescan);
        RESCAN_RATE = b.comment("The number of area scans between full rescans.", "Higher value gives better performance.")
                .defineInRange("rescanRate", d.rescanRate, 1, 1000);
        MARKER_LIMIT = b.comment("The maximum number of markers a particular atlas can hold.")
                .defineInRange("markerLimit", d.markerLimit, 0, Integer.MAX_VALUE);
        DO_SCAN_PONDS = b.comment("Whether to perform additional scanning to locate small ponds of water or lava.", "Disable for better performance.")
                .define("doScanPonds", d.doScanPonds);
        DO_SCAN_RAVINES = b.comment("Whether to perform additional scanning to locate ravines.", "Disable for better performance.")
                .define("doScanRavines", d.doScanRavines);
        DEBUG_RENDER = b.comment("If true, map render time will be output.")
                .define("debugRender", d.debugRender);
        RESOURCE_PACK_LOGGING = b.comment("If true, all resource pack loading information will be logged during start and reload.")
                .define("resourcePackLogging", d.resourcePackLogging);
        b.pop();

        b.push("appearance");
        MAP_ON_HELD_ATLAS = b.comment("If true, the atlas in your hand shows its map in first person, like a held map (Antique Atlas 7 style).",
                        "If false, it looks like a normal item and the corner minimap shows the map instead (1.12.2 style).",
                        "The settings below in this section only apply to the map on the held atlas.")
                .define("mapOnHeldAtlas", d.mapOnHeldAtlas);
        TILE_SIZE = b.comment("The size (in GUI pixels) of a map's tile.",
                        "Note that this will change with Minecraft's GUI scale configuration.",
                        "When using a small gui scale, the map may look better with a tileSize of 16 or more.")
                .defineInRange("tileSize", d.tileSize, 1, 64);
        MARKER_SIZE = b.comment("The size (in GUI pixels) of a marker on the map.",
                        "Note that this will change with Minecraft's GUI scale configuration.")
                .defineInRange("markerSize", d.markerSize, 0, 256);
        PLAYER_ICON_WIDTH = b.comment("The width (in GUI pixels) of the player's icon.")
                .defineInRange("playerIconWidth", d.playerIconWidth, 0, 256);
        PLAYER_ICON_HEIGHT = b.comment("The height (in GUI pixels) of the player's icon.")
                .defineInRange("playerIconHeight", d.playerIconHeight, 0, 256);
        b.pop();

        b.comment("The corner minimap from 1.12.2: while you hold an atlas, a small copy of its map is shown in a corner of the screen.")
                .push("minimap");
        MINIMAP_ENABLED = b.comment("Set true to enable the minimap.")
                .define("enabled", d.minimapEnabled);
        MINIMAP_REQUIRES_HOLD = b.comment("If true, the minimap shows the map of an atlas only while it is held.",
                        "If false, the minimap shows the map of the atlas in the offhand, or else the first atlas in the hotbar.")
                .define("requiresHold", d.minimapRequiresHold);
        MINIMAP_ALIGN_RIGHT = b.comment("If true, the map position's x axis will align 0 to the right of the screen, increasing towards the left.")
                .define("alignRight", d.minimapAlignRight);
        MINIMAP_ALIGN_BOTTOM = b.comment("If true, the map position's y axis will align 0 to the bottom of the screen, increasing towards the top.")
                .define("alignBottom", d.minimapAlignBottom);
        MINIMAP_X = b.comment("Map's minimum position along the x axis in GUI pixels.",
                        "Note that this will change with Minecraft's GUI scale configuration.")
                .defineInRange("xPosition", d.minimapX, -4096, 4096);
        MINIMAP_Y = b.comment("Map's minimum position along the y axis in GUI pixels.",
                        "Note that this will change with Minecraft's GUI scale configuration.")
                .defineInRange("yPosition", d.minimapY, -4096, 4096);
        MINIMAP_WIDTH = b.comment("Map's width in GUI pixels.", "Note that this will change with Minecraft's GUI scale configuration.")
                .defineInRange("width", d.minimapWidth, 0, 4096);
        MINIMAP_HEIGHT = b.comment("Map's height in GUI pixels.", "Note that this will change with Minecraft's GUI scale configuration.")
                .defineInRange("height", d.minimapHeight, 0, 4096);
        MINIMAP_TILE_SIZE = b.comment("The size (in GUI pixels) of a map's tile.",
                        "Note that this will change with Minecraft's GUI scale configuration.",
                        "When using a small gui scale, the map may look better with a tileSize of 16 or more.")
                .defineInRange("tileSize", d.minimapTileSize, 1, 64);
        MINIMAP_MARKER_SIZE = b.comment("The size (in GUI pixels) of a marker on the map. 0 hides markers and the player icon.",
                        "Note that this will change with Minecraft's GUI scale configuration.")
                .defineInRange("markerSize", d.minimapMarkerSize, 0, 256);
        MINIMAP_PLAYER_ICON_WIDTH = b.comment("The width (in GUI pixels) of the player's icon.")
                .defineInRange("playerIconWidth", d.minimapPlayerIconWidth, 0, 256);
        MINIMAP_PLAYER_ICON_HEIGHT = b.comment("The height (in GUI pixels) of the player's icon.")
                .defineInRange("playerIconHeight", d.minimapPlayerIconHeight, 0, 256);
        MINIMAP_BORDER_X = b.comment("The width of the map border on the left and right sides of the minimap tiles.",
                        "Represented as a fraction of the image width.",
                        "Below a certain threshold, this border will be overtaken by the map border graphic.")
                .defineInRange("borderX", (double) d.minimapBorderX, 0.0, 0.5);
        MINIMAP_BORDER_Y = b.comment("The width of the map border on the top and bottom sides of the minimap tiles.",
                        "Represented as a fraction of the image height.",
                        "Below a certain threshold, this border will be overtaken by the map border graphic.")
                .defineInRange("borderY", (double) d.minimapBorderY, 0.0, 0.5);
        b.pop();

        SPEC = b.build();
    }

    /** Copies the values from the config file into a new settings object. */
    public static AntiqueAtlasConfig bake() {
        AntiqueAtlasConfig c = new AntiqueAtlasConfig();
        c.doSaveBrowsingPos = DO_SAVE_BROWSING_POS.get();
        c.autoDeathMarker = AUTO_DEATH_MARKER.get();
        c.autoVillageMarkers = AUTO_VILLAGE_MARKERS.get();
        c.autoNetherPortalMarkers = AUTO_NETHER_PORTAL_MARKERS.get();
        c.itemNeeded = ITEM_NEEDED.get();

        c.doScaleMarkers = DO_SCALE_MARKERS.get();
        c.defaultScale = DEFAULT_SCALE.get();
        c.minScale = MIN_SCALE.get();
        c.maxScale = MAX_SCALE.get();
        c.doReverseWheelZoom = DO_REVERSE_WHEEL_ZOOM.get();

        c.scanRadius = SCAN_RADIUS.get();
        c.forceChunkLoading = FORCE_CHUNK_LOADING.get();
        c.newScanInterval = NEW_SCAN_INTERVAL.get().floatValue();
        c.doRescan = DO_RESCAN.get();
        c.rescanRate = RESCAN_RATE.get();
        c.markerLimit = MARKER_LIMIT.get();
        c.doScanPonds = DO_SCAN_PONDS.get();
        c.doScanRavines = DO_SCAN_RAVINES.get();
        c.debugRender = DEBUG_RENDER.get();
        c.resourcePackLogging = RESOURCE_PACK_LOGGING.get();

        c.tileSize = TILE_SIZE.get();
        c.markerSize = MARKER_SIZE.get();
        c.playerIconWidth = PLAYER_ICON_WIDTH.get();
        c.playerIconHeight = PLAYER_ICON_HEIGHT.get();
        c.mapOnHeldAtlas = MAP_ON_HELD_ATLAS.get();

        c.minimapEnabled = MINIMAP_ENABLED.get();
        c.minimapRequiresHold = MINIMAP_REQUIRES_HOLD.get();
        c.minimapAlignRight = MINIMAP_ALIGN_RIGHT.get();
        c.minimapAlignBottom = MINIMAP_ALIGN_BOTTOM.get();
        c.minimapX = MINIMAP_X.get();
        c.minimapY = MINIMAP_Y.get();
        c.minimapWidth = MINIMAP_WIDTH.get();
        c.minimapHeight = MINIMAP_HEIGHT.get();
        c.minimapTileSize = MINIMAP_TILE_SIZE.get();
        c.minimapMarkerSize = MINIMAP_MARKER_SIZE.get();
        c.minimapPlayerIconWidth = MINIMAP_PLAYER_ICON_WIDTH.get();
        c.minimapPlayerIconHeight = MINIMAP_PLAYER_ICON_HEIGHT.get();
        c.minimapBorderX = MINIMAP_BORDER_X.get().floatValue();
        c.minimapBorderY = MINIMAP_BORDER_Y.get().floatValue();
        return c;
    }
}
