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
        return c;
    }
}
