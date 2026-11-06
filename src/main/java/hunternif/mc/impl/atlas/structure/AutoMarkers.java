package hunternif.mc.impl.atlas.structure;

import hunternif.mc.api.AtlasAPI;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.marker.DimensionMarkersData;
import hunternif.mc.impl.atlas.marker.Marker;
import hunternif.mc.impl.atlas.marker.MarkersData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global markers that are added automatically when structures and world generation features
 * generate. Which ones get a marker, and with which icon and label, comes from the
 * {@code autoMarkers} section of the config, so new structures can be added without code.
 * <p>
 * Structures and features generate on world generation threads; the marker itself is always
 * added on the server thread.
 */
public final class AutoMarkers {
    /** Markers of the same type closer than this (in blocks) are treated as the same place. */
    private static final int SAME_PLACE_DISTANCE = 64;

    public record Rule(ResourceLocation markerType, Component label) {}

    private static final Map<ResourceLocation, Rule> STRUCTURE_RULES = new HashMap<>();
    private static final List<Map.Entry<TagKey<Structure>, Rule>> STRUCTURE_TAG_RULES = new ArrayList<>();
    private static final Map<ResourceLocation, Rule> FEATURE_RULES = new HashMap<>();
    /** Feature objects resolved from {@link #FEATURE_RULES}; built lazily because the registry fills in after config loads. */
    private static volatile Map<Feature<?>, Rule> featureRulesByFeature;

    /** Structures already handled this session, so each one is checked once rather than once per chunk. */
    private static final Set<String> HANDLED = ConcurrentHashMap.newKeySet();

    private AutoMarkers() {}

    /** Reads the rules from the config. Called whenever the config (re)loads. */
    public static synchronized void reload() {
        STRUCTURE_RULES.clear();
        STRUCTURE_TAG_RULES.clear();
        FEATURE_RULES.clear();
        featureRulesByFeature = null;

        if (AntiqueAtlasMod.CONFIG.autoVillageMarkers) {
            STRUCTURE_TAG_RULES.add(Map.entry(
                    TagKey.create(Registries.STRUCTURE, new ResourceLocation("minecraft", "village")),
                    new Rule(AntiqueAtlasMod.id("village"), Component.translatable("gui.antiqueatlas.marker.village"))));
        }

        for (String line : AntiqueAtlasMod.CONFIG.structureMarkers) {
            ParsedLine parsed = parse(line, "structureMarkers");
            if (parsed == null) continue;
            if (parsed.isTag) {
                STRUCTURE_TAG_RULES.add(Map.entry(TagKey.create(Registries.STRUCTURE, parsed.id), parsed.rule));
            } else {
                STRUCTURE_RULES.put(parsed.id, parsed.rule);
            }
        }
        for (String line : AntiqueAtlasMod.CONFIG.featureMarkers) {
            ParsedLine parsed = parse(line, "featureMarkers");
            if (parsed == null) continue;
            if (parsed.isTag) {
                AntiqueAtlasMod.LOG.warn("Antique Atlas config: featureMarkers doesn't support tags, ignoring \"{}\"", line);
                continue;
            }
            FEATURE_RULES.put(parsed.id, parsed.rule);
        }
        AntiqueAtlasMod.LOG.info("Antique Atlas automatic markers: {} structures, {} structure tags, {} features",
                STRUCTURE_RULES.size(), STRUCTURE_TAG_RULES.size(), FEATURE_RULES.size());
    }

    private record ParsedLine(ResourceLocation id, boolean isTag, Rule rule) {}

    private static ParsedLine parse(String line, String option) {
        String[] parts = line.split(",", 3);
        if (parts.length < 2) {
            AntiqueAtlasMod.LOG.warn("Antique Atlas config: {} entry \"{}\" needs at least \"id, marker type\"", option, line);
            return null;
        }
        String idText = parts[0].trim();
        boolean isTag = idText.startsWith("#");
        if (isTag) idText = idText.substring(1);
        ResourceLocation id = ResourceLocation.tryParse(idText);
        String markerText = parts[1].trim();
        ResourceLocation marker = markerText.contains(":") ? ResourceLocation.tryParse(markerText) : AntiqueAtlasMod.id(markerText);
        if (id == null || marker == null) {
            AntiqueAtlasMod.LOG.warn("Antique Atlas config: {} entry \"{}\" has an invalid id or marker type", option, line);
            return null;
        }
        String label = parts.length > 2 ? parts[2].trim() : "";
        return new ParsedLine(id, isTag, new Rule(marker, Component.literal(label)));
    }

    // ---------------------------------------------------------------- structures

    /** Called on a world generation thread each time part of a structure is placed in a chunk. */
    public static void onStructurePlaced(StructureStart start, ServerLevel level) {
        if (STRUCTURE_RULES.isEmpty() && STRUCTURE_TAG_RULES.isEmpty()) return;

        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Structure structure = start.getStructure();
        ResourceLocation structureId = registry.getKey(structure);
        if (structureId == null) return;

        BoundingBox box = start.getBoundingBox();
        String key = level.dimension().location() + "|" + structureId + "|" + box.minX() + "," + box.minY() + "," + box.minZ();
        if (!HANDLED.add(key)) return;

        Rule rule = STRUCTURE_RULES.get(structureId);
        if (rule == null && !STRUCTURE_TAG_RULES.isEmpty()) {
            Holder<Structure> holder = registry.wrapAsHolder(structure);
            for (Map.Entry<TagKey<Structure>, Rule> tagRule : STRUCTURE_TAG_RULES) {
                if (holder.is(tagRule.getKey())) {
                    rule = tagRule.getValue();
                    break;
                }
            }
        }
        if (rule == null) return;

        BlockPos center = box.getCenter();
        addGlobalMarker(level, rule, center.getX(), center.getZ());
    }

    // ---------------------------------------------------------------- features

    /** Called on a world generation thread after a configured feature placed successfully. */
    public static void onFeaturePlaced(Feature<?> feature, ServerLevel level, BlockPos origin) {
        if (FEATURE_RULES.isEmpty()) return;
        Map<Feature<?>, Rule> byFeature = featureRulesByFeature;
        if (byFeature == null) {
            byFeature = new IdentityHashMap<>();
            for (Map.Entry<ResourceLocation, Rule> entry : FEATURE_RULES.entrySet()) {
                Feature<?> f = BuiltInRegistries.FEATURE.getOptional(entry.getKey()).orElse(null);
                if (f != null) byFeature.put(f, entry.getValue());
            }
            featureRulesByFeature = byFeature;
        }
        Rule rule = byFeature.get(feature);
        if (rule == null) return;
        addGlobalMarker(level, rule, origin.getX(), origin.getZ());
    }

    // ---------------------------------------------------------------- shared

    private static void addGlobalMarker(ServerLevel level, Rule rule, int x, int z) {
        level.getServer().execute(() -> {
            MarkersData data = AntiqueAtlasMod.globalMarkersData.getData();
            if (hasMarkerNear(data, level.dimension(), rule.markerType(), x, z, SAME_PLACE_DISTANCE)) return;
            AtlasAPI.getMarkerAPI().putGlobalMarker(level, false, rule.markerType(), rule.label(), x, z);
        });
    }

    /** Whether the markers already hold a marker of this type at (about) this place. */
    public static boolean hasMarkerNear(MarkersData data, ResourceKey<Level> dimension, ResourceLocation type, int x, int z, int distance) {
        DimensionMarkersData dimData = data.getMarkersDataInWorld(dimension);
        if (dimData == null) return false;
        // Same keys as DimensionMarkersData.insertMarker: chunk coordinates divided by CHUNK_STEP.
        int cellX = (x >> 4) / MarkersData.CHUNK_STEP;
        int cellZ = (z >> 4) / MarkersData.CHUNK_STEP;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Collection<Marker> markers = dimData.getMarkersAtChunk(cellX + dx, cellZ + dz);
                if (markers == null) continue;
                for (Marker marker : markers) {
                    if (marker.getType().equals(type)
                            && Math.abs(marker.getX() - x) <= distance
                            && Math.abs(marker.getZ() - z) <= distance) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Forgets which structures were handled, e.g. when the server stops. */
    public static void clearSession() {
        HANDLED.clear();
    }
}
