package hunternif.mc.impl.atlas;

import hunternif.mc.impl.atlas.client.gui.GuiAtlas;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

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
    public boolean autoVillageMarkers = true;
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
    public boolean minimapShowCoordinates = false;
    public boolean minimapShowBiome = false;

    //============ Automatic markers ============
    public List<String> structureMarkers = new ArrayList<>(DEFAULT_STRUCTURE_MARKERS);
    public List<String> featureMarkers = new ArrayList<>(DEFAULT_FEATURE_MARKERS);
    public boolean waystoneMarkers = true;
    public String waystoneMarkerType = "antiqueatlas:waystone";

    /** Defaults for {@link #structureMarkers}: "structure id or #tag, marker type, label". */
    public static final List<String> DEFAULT_STRUCTURE_MARKERS = List.of(
                "minecraft:end_city, end_city, ",
                "minecraft:mansion, mansion, Woodland Mansion",
                "minecraft:monument, monument, Ocean Monument",
                "minecraft:stronghold, dungeon, Stronghold",
                "minecraft:ancient_city, dungeon, Ancient City",
                "minecraft:fortress, sword, Nether Fortress",
                "minecraft:bastion_remnant, bastion, Bastion Remnant",
                "minecraft:pillager_outpost, tower, Pillager Outpost",
                "minecraft:desert_pyramid, dungeon, Desert Pyramid",
                "minecraft:jungle_pyramid, dungeon, Jungle Temple",
                "minecraft:swamp_hut, wizardtower, Witch Hut",
                "rec_migrated:p_aeriveth, google, Aeriveth",
                "rec_migrated:p_icecity, google, Ice City",
                "rec_migrated:p_sanctuaryofadmys, google, Sanctuary of Admys",
                "rec_migrated:p_townmeridor, google, Meridor",
                "rec_migrated:rlcraft_2_9_update_build, google, RLCraft 2.9 Update Build",
                "rec_migrated:shivaxi, google, Shivaxi Monument",
                "rec_migrated:p_wizardtower, wizardtower, Wizard Tower",
                "rec_migrated:witchtower, wizardtower, Witch Tower",
                "rec_migrated:threetowers, quadtower, Three Towers",
                "rec_migrated:rlcraft_4towers, quadtower, Four Towers",
                "rec_migrated:p_skycastle, diamond, Sky Castle",
                "rec_migrated:p_cathedralofmadness, dungeon, Cathedral of Madness",
                "rec_migrated:p_templeoferolith, dungeon, Temple of Erolith",
                "rec_migrated:p_mayantemple1, dungeon, Mayan Temple",
                "rec_migrated:p_mayantemple2, dungeon, Mayan Temple",
                "rec_migrated:p_mayantemple3, dungeon, Mayan Temple",
                "rec_migrated:p_mayantemple4, dungeon, Mayan Temple",
                "rec_migrated:p_deserttemple, dungeon, Desert Temple",
                "rec_migrated:p_snowtemple, dungeon, Snow Temple",
                "rec_migrated:p_desertdungeon, dungeon, Desert Dungeon",
                "rec_migrated:p_treedungeon, dungeon, Tree Dungeon",
                "rec_migrated:p_witchdungeon, dungeon, Witch Dungeon",
                "rec_migrated:p_largeship, ship, Large Ship",
                "rec_migrated:p_smallship, ship, Small Ship",
                "rec_migrated:flagship, ship, Flagship",
                "rec_migrated:piratehideout, ship, Pirate Hideout",
                "rec_migrated:p_lighthouse, tower, Lighthouse",
                "rec_migrated:p_seasideport, village, Seaside Port",
                "rec_migrated:p_plainscity, village, Plains City",
                "rec_migrated:p_islandcity, village, Island City",
                "rec_migrated:p_junglevillage, village, Jungle Village",
                "rec_migrated:p_deqing, village, Deqing",
                "rec_migrated:rlcraft_smalltown, village, Small Town",
                "rec_migrated:rlcraft_deserttown, village, Desert Town",
                "rec_migrated:rlcraft_largeundergroundtown, village, Underground Town",
                "rec_migrated:rlcraft_foreststronghold, sword, Forest Stronghold",
                "rec_migrated:rlcraft_friendlyoutpost, village, Friendly Outpost",
                "rec_migrated:rlcraft_stranding_vikingmansion, village, Viking Mansion",
                "rec_migrated:rlcraft_nethercity_church, dungeon, Nether City Church",
                "rec_migrated:rlcraft_netherportal1_chiom, nether_portal, Nether Portal",
                "rec_migrated:rlcraft_netherportal2_felix, nether_portal, Nether Portal",
                "rec_migrated:p_ancientteleporter, nether_portal, Ancient Teleporter",
                "rec_migrated:rlcraft_defiled_cult, defiled, Defiled Cult",
                "rec_migrated:rlcraft_defiled_cult_og, defiled, Defiled Cult",
                "rec_migrated:p_cultisland, skull, Cult Island",
                "rec_migrated:p_forestguardian, dungeon, Forest Guardian",
                "rec_migrated:p_spookyhouse, skull, Spooky House",
                "rec_migrated:rlcraft_end_megacity_start, village, End Megacity",
                "rec_migrated:rlcraft_end_endislandcastle, dungeon, End Island Castle",
                "rec_migrated:rlcraft_end_flipped_castle, dungeon, Flipped Castle",
                "rec_migrated:rlcraft_end_spiral_portal_tower, tower, Spiral Portal Tower",
                "rec_migrated:rlcraft_end_battletower_broken, tower, Broken Battle Tower",
                "rec_migrated:rlcraft_ruinsparkour, ruins, Parkour Ruins",
                "rec_migrated:smallfortruinsjapanese, ruins, Fort Ruins",
                "rec_migrated:hobbithole, village, Hobbit Hole",
                "fbt:tall_battletower, tower, Battle Tower",
                "fbt:deep_battletower, tower, Battle Tower",
                "fbt:mega_battletower, megatower, Mega Battle Tower",
                "iceandfire:gorgon_temple, dungeon, Gorgon Temple",
                "iceandfire:graveyard, tomb, Graveyard",
                "iceandfire:mausoleum, tomb, Mausoleum",
                "dldungeonsjbg:dldungeon, dungeon, Dungeon",
                "dungeoncrawl:dungeon, dungeon, Dungeon Crawl",
                "seafaring:mossy_ocean_dungeon, dungeon, Ocean Dungeon",
                "seafaring:mossy_ocean_dungeon2, dungeon, Ocean Dungeon",
                "seafaring:sandstone_ocean_dungeon, dungeon, Ocean Dungeon",
                "seafaring:sandstone_ocean_dungeon2, dungeon, Ocean Dungeon",
                "seafaring:undergrounddungeon, dungeon, Underground Dungeon",
                "dungeons_arise:abandoned_temple, dungeon, Abandoned Temple",
                "dungeons_arise:aviary, tower, Aviary",
                "dungeons_arise:bandit_towers, tower, Bandit Towers",
                "dungeons_arise:bandit_village, village, Bandit Village",
                "dungeons_arise:ceryneian_hind, dungeon, Ceryneian Hind",
                "dungeons_arise:coliseum, sword, Coliseum",
                "dungeons_arise:foundry, dungeon, Foundry",
                "dungeons_arise:heavenly_challenger, tower, Heavenly Challenger",
                "dungeons_arise:heavenly_conqueror, tower, Heavenly Conqueror",
                "dungeons_arise:heavenly_rider, tower, Heavenly Rider",
                "dungeons_arise:illager_corsair, ship, Illager Corsair",
                "dungeons_arise:illager_fort, sword, Illager Fort",
                "dungeons_arise:illager_galley, ship, Illager Galley",
                "dungeons_arise:infested_temple, dungeon, Infested Temple",
                "dungeons_arise:keep_kayra, dungeon, Keep Kayra",
                "dungeons_arise:lighthouse, tower, Lighthouse",
                "dungeons_arise:mechanical_nest, dungeon, Mechanical Nest",
                "dungeons_arise:mining_system, pickaxe, Mining System",
                "dungeons_arise:monastery, dungeon, Monastery",
                "dungeons_arise:mushroom_mines, pickaxe, Mushroom Mines",
                "dungeons_arise:mushroom_village, village, Mushroom Village",
                "dungeons_arise:plague_asylum, dungeon, Plague Asylum",
                "dungeons_arise:scorched_mines, pickaxe, Scorched Mines",
                "dungeons_arise:shiraz_palace, dungeon, Shiraz Palace",
                "dungeons_arise:small_blimp, ship, Blimp",
                "dungeons_arise:thornborn_towers, tower, Thornborn Towers",
                "dungeons_arise:typhon, ship, Typhon",
                "dungeons_arise:undead_pirate_ship, ship, Undead Pirate Ship",
                "cataclysm:abandoned_spire, tower, Abandoned Spire",
                "cataclysm:abandoned_temple, dungeon, Abandoned Temple",
                "cataclysm:abandoned_village, village, Abandoned Village",
                "cataclysm:acropolis, dungeon, Acropolis",
                "cataclysm:amethyst_nest, cave, Amethyst Nest",
                "cataclysm:ancient_factory, dungeon, Ancient Factory",
                "cataclysm:burning_arena, dungeon, Burning Arena",
                "cataclysm:cursed_pyramid, dungeon, Cursed Pyramid",
                "cataclysm:desert_occupied_village, village, Occupied Village",
                "cataclysm:desert_site, ruins, Desert Site",
                "cataclysm:frosted_prison, dungeon, Frosted Prison",
                "cataclysm:ruined_citadel, dungeon, Ruined Citadel",
                "cataclysm:soul_black_smith, anvil, Soul Blacksmith",
                "cataclysm:sunken_city, dungeon, Sunken City",
                "ebwizardry:wizard_tower, wizardtower, Wizard Tower",
                "ebwizardry:library_ruins, book, Library Ruins",
                "ebwizardry:earth_shrine, ruins, Earth Shrine",
                "ebwizardry:fire_shrine, ruins, Fire Shrine",
                "ebwizardry:healing_shrine, ruins, Healing Shrine",
                "ebwizardry:ice_shrine, ruins, Ice Shrine",
                "ebwizardry:lightning_shrine, ruins, Lightning Shrine",
                "ebwizardry:necromancy_shrine, ruins, Necromancy Shrine",
                "ebwizardry:sorcery_shrine, ruins, Sorcery Shrine",
                "illagerwarship:warship, ship, Illager Warship",
                "takesapillage:bastille, sword, Bastille",
                "takesapillage:pillager_camp, sword, Pillager Camp",
                "totw_additions:end_tower, tower, Tower",
                "totw_additions:nether_tower, tower, Tower",
                "lios_outlandish_villages:hobbit_village, village, Hobbit Village",
                "lios_outlandish_villages:spiral_tower_village, village, Spiral Tower Village",
                "lios_outlandish_villages:spiral_tower_village_sea, village, Spiral Tower Village",
                "biomeswevegone:village/forgotten, village, Village",
                "biomeswevegone:village/pumpkin_patch, village, Village",
                "biomeswevegone:village/red_rock, village, Village",
                "biomeswevegone:village/salem, village, Village",
                "biomeswevegone:village/skyris, village, Village",
                "biomeswevegone:village/swamp, village, Village",
                "betterend:end_village, village, End Village",
                "betternether:nether_city, village, Nether City",
                "betternether:jungle_temples, dungeon, Nether Jungle Temple",
                "mes:phantom_citadel, dungeon, Phantom Citadel",
                "mes:starlight_voyager, ship, Starlight Voyager",
                "mes:mega_ship, ship, End Ship",
                "mes:mega_ship_basic, ship, End Ship",
                "mes:mega_ship_deepslate, ship, End Ship",
                "mes:mega_ship_deepslate_2, ship, End Ship",
                "mes:mega_ship_deepslate_3, ship, End Ship",
                "mes:mega_ship_crashed, ship, Crashed End Ship",
                "mes:mega_ship_crashed_2, ship, Crashed End Ship",
                "mes:mega_ship_crashed_deepslate, ship, Crashed End Ship",
                "mns:mega_fortress, dungeon, Mega Fortress",
                "mns:dragon_arena, sword, Dragon Arena",
                "mns:large_arena, sword, Arena",
                "mvs:castle_ruins, ruins, Castle Ruins",
                "mvs:cathedral, dungeon, Cathedral",
                "mvs:cartographer_tower, tower, Cartographer Tower",
                "additionalstructures:maya_temple, dungeon, Maya Temple",
                "additionalstructures:pyramid_1, dungeon, Pyramid",
                "additionalstructures:pyramid_2, dungeon, Pyramid",
                "additionalstructures:pyramid_dungeon, dungeon, Pyramid Dungeon",
                "additionalstructures:sphinx, dungeon, Sphinx",
                "additionalstructures:underground_dungeon_1, dungeon, Underground Dungeon",
                "additionalstructures:underground_dungeon_2, dungeon, Underground Dungeon",
                "additionalstructures:end_dungeon_1, dungeon, End Dungeon",
                "additionalstructures:nether_dungeon_1, dungeon, Nether Dungeon",
                "additionalstructures:nether_dungeon_2, dungeon, Nether Dungeon",
                "additionalstructures:nether_dungeon_3, dungeon, Nether Dungeon"
    );

    /** Defaults for {@link #featureMarkers}: "feature id, marker type, label". */
    public static final List<String> DEFAULT_FEATURE_MARKERS = List.of(
                "iceandfire:fire_dragon_roost, dragon_red, Fire Dragon Roost",
                "iceandfire:ice_dragon_roost, dragon_blue, Ice Dragon Roost",
                "iceandfire:lightning_dragon_roost, dragon_gold, Lightning Dragon Roost",
                "iceandfire:fire_dragon_cave, dragon_red, Fire Dragon Cave",
                "iceandfire:ice_dragon_cave, dragon_blue, Ice Dragon Cave",
                "iceandfire:lightning_dragon_cave, dragon_gold, Lightning Dragon Cave",
                "iceandfire:hydra_cave, dragon_green, Hydra Cave",
                "iceandfire:cyclops_cave, cave, Cyclops Cave",
                "iceandfire:siren_island, skull, Siren Island",
                "iceandfire:myrmex_hive_desert, monsterspawner, Myrmex Hive",
                "iceandfire:myrmex_hive_jungle, monsterspawner, Myrmex Hive"
    );

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
    private static final ForgeConfigSpec.BooleanValue MINIMAP_SHOW_COORDINATES;
    private static final ForgeConfigSpec.BooleanValue MINIMAP_SHOW_BIOME;

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> STRUCTURE_MARKERS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> FEATURE_MARKERS;
    private static final ForgeConfigSpec.BooleanValue WAYSTONE_MARKERS;
    private static final ForgeConfigSpec.ConfigValue<String> WAYSTONE_MARKER_TYPE;

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
        MINIMAP_SHOW_COORDINATES = b.comment("Show your X and Z coordinates under the minimap (above it if alignBottom is true).")
                .define("showCoordinates", d.minimapShowCoordinates);
        MINIMAP_SHOW_BIOME = b.comment("Show the name of the biome you're in under the minimap (above it if alignBottom is true).")
                .define("showBiome", d.minimapShowBiome);
        b.pop();

        b.comment("Markers that are added to the map automatically, like the 1.12.2 AntiqueAtlasAutoMarker mod in RLCraft Dregora.",
                        "Structure and feature markers are global: they show on every atlas once that part of the map has been explored.")
                .push("autoMarkers");
        STRUCTURE_MARKERS = b.comment("Structures that get a marker when they generate. One entry per line: structure, marker type, label.",
                        "The structure is a structure id (like minecraft:stronghold) or a structure tag starting with # (like #minecraft:village).",
                        "The marker type is any marker from atlas/markers (like dungeon, tower, ship, google); without a namespace it means antiqueatlas:.",
                        "The label can be empty. Remove a line to stop marking that structure. Villages are controlled by gameplay.autoVillageMarkers.",
                        "Only structures generated after this is set get markers.")
                .defineListAllowEmpty(List.of("structureMarkers"), () -> DEFAULT_STRUCTURE_MARKERS, o -> o instanceof String);
        FEATURE_MARKERS = b.comment("World generation features (not structures) that get a marker when they generate, in the same format: feature, marker type, label.",
                        "Ice and Fire's dragon roosts and caves, hydra and cyclops caves and siren islands are features.")
                .defineListAllowEmpty(List.of("featureMarkers"), () -> DEFAULT_FEATURE_MARKERS, o -> o instanceof String);
        WAYSTONE_MARKERS = b.comment("If the Waystones mod is installed: when you activate a waystone, mark it with its name on every atlas you carry.")
                .define("waystoneMarkers", d.waystoneMarkers);
        WAYSTONE_MARKER_TYPE = b.comment("The marker type used for waystones.")
                .define("waystoneMarkerType", d.waystoneMarkerType);
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
        c.minimapShowCoordinates = MINIMAP_SHOW_COORDINATES.get();
        c.minimapShowBiome = MINIMAP_SHOW_BIOME.get();

        c.structureMarkers = new ArrayList<>(STRUCTURE_MARKERS.get());
        c.featureMarkers = new ArrayList<>(FEATURE_MARKERS.get());
        c.waystoneMarkers = WAYSTONE_MARKERS.get();
        c.waystoneMarkerType = WAYSTONE_MARKER_TYPE.get();
        return c;
    }
}
