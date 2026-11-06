package hunternif.mc.impl.atlas.client;

import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.client.texture.ITexture;
import hunternif.mc.impl.atlas.core.scaning.TileHeightType;
import hunternif.mc.impl.atlas.util.Log;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import java.util.*;
import java.util.Map.Entry;

/**
 * Maps biome IDs (or pseudo IDs) to textures. <i>Not thread-safe!</i>
 * <p>If several textures are set for one ID, one will be chosen at random when
 * putting tile into Atlas.</p>
 *
 * @author Hunternif
 */
@OnlyIn(Dist.CLIENT)
public class TileTextureMap {
    private static final TileTextureMap INSTANCE = new TileTextureMap();

    public static final ResourceLocation DEFAULT_TEXTURE = AntiqueAtlasMod.id("test");

    public static TileTextureMap instance() {
        return INSTANCE;
    }

    /**
     * This map stores the pseudo biome texture mappings, any biome with ID <0 is assumed to be a pseudo biome
     */
    private final Map<ResourceLocation, TextureSet> textureMap = new HashMap<>();

    /**
     * Assign texture set to pseudo biome
     */
    public void setTexture(ResourceLocation tileId, TextureSet textureSet) {
        if (tileId == null) return;

        if (textureSet == null) {
            if (textureMap.remove(tileId) != null) {
                Log.warn("Removing old texture for %d", tileId);
            }
            return;
        }

        textureMap.put(tileId, textureSet);
    }

    /**
     * Assign the same texture set to all height variations of the tileId
     */
    public void setAllTextures(ResourceLocation tileId, TextureSet textureSet) {
        setTexture(tileId, textureSet);

        for (TileHeightType layer : TileHeightType.values()) {
            setTexture(ResourceLocation.tryParse(tileId + "_" + layer), textureSet);
        }
    }

    public TextureSet getDefaultTexture() {
        return TextureSetMap.instance().getByName(DEFAULT_TEXTURE);
    }

    /**
     * Find the most appropriate standard texture set depending on
     * BiomeDictionary types.
     */
    public void autoRegister(ResourceLocation id, ResourceKey<Biome> biome) {
        if (biome == null || id == null) {
            Log.error("Given biome is null. Cannot autodetect a suitable texture set for that.");
            return;
        }

        Optional<ResourceLocation> texture_set = guessFittingTextureSet(biome);

        if (texture_set.isPresent()) {
            setAllTextures(id, TextureSetMap.instance().getByName(texture_set.get()));
            Log.info("Auto-registered standard texture set for biome %s: %s", id, texture_set.get());
        } else {
            Log.error("Failed to auto-register a standard texture set for the biome '%s'. This is most likely caused by errors in the TextureSet configurations, check your resource packs first before reporting it as an issue!", id.toString());
            setAllTextures(id, getDefaultTexture());
        }
    }

    /**
     * Picks a standard texture set for a biome from its biome tags (vanilla and Forge), for
     * biomes that no texture config covers.
     */
    static private Optional<ResourceLocation> guessFittingTextureSet(ResourceKey<Biome> biome) {
        if (Minecraft.getInstance().level == null)
            return Optional.empty();

        Holder.Reference<Biome> biomeTag = Minecraft.getInstance().level.registryAccess()
                .registryOrThrow(Registries.BIOME).getHolderOrThrow(biome);

        if (biomeTag.is(BiomeTags.IS_END)) {
            return Optional.of(AntiqueAtlasMod.id("end_island"));
        }

        if (biomeTag.is(BiomeTags.IS_NETHER)) {
            return Optional.of(AntiqueAtlasMod.id("soul_sand_valley"));
        }

        if (biomeTag.is(Tags.Biomes.IS_VOID)) {
            return Optional.of(AntiqueAtlasMod.id("end_void"));
        }

        if (biomeTag.is(Tags.Biomes.IS_SWAMP)) {
            if (biomeTag.is(BiomeTags.IS_HILL)) {
                return Optional.of(AntiqueAtlasMod.id("swamp_hills"));
            } else {
                return Optional.of(AntiqueAtlasMod.id("swamp"));
            }
        }

        if (biomeTag.is(BiomeTags.IS_OCEAN)
                || biomeTag.is(BiomeTags.IS_DEEP_OCEAN)
                || biomeTag.is(BiomeTags.IS_RIVER)
                || biomeTag.is(Tags.Biomes.IS_WATER)) {
            if (biomeTag.is(Tags.Biomes.IS_COLD) || biomeTag.is(Tags.Biomes.IS_SNOWY))
                return Optional.of(AntiqueAtlasMod.id("ice"));

            return Optional.of(AntiqueAtlasMod.id("water"));
        }

        if (biomeTag.is(BiomeTags.IS_BEACH)) {
            return Optional.of(AntiqueAtlasMod.id("shore"));
        }

        if (biomeTag.is(BiomeTags.IS_JUNGLE)) {
            if (biomeTag.is(BiomeTags.IS_HILL)) {
                return Optional.of(AntiqueAtlasMod.id("jungle_hills"));
            } else {
                return Optional.of(AntiqueAtlasMod.id("jungle"));
            }
        }

        if (biomeTag.is(BiomeTags.IS_SAVANNA)) {
            return Optional.of(AntiqueAtlasMod.id("savana"));
        }

        if (biomeTag.is(Tags.Biomes.IS_PLATEAU)) {
            return Optional.of(AntiqueAtlasMod.id("plateau_mesa"));
        }

        if (biomeTag.is(BiomeTags.IS_FOREST)
                || biomeTag.is(Tags.Biomes.IS_DENSE)
                || biomeTag.is(Tags.Biomes.IS_SPARSE)) {
            if (biomeTag.is(Tags.Biomes.IS_COLD) || biomeTag.is(Tags.Biomes.IS_SNOWY)) {
                if (biomeTag.is(BiomeTags.IS_HILL)) {
                    return Optional.of(AntiqueAtlasMod.id("snow_pines_hills"));
                } else {
                    return Optional.of(AntiqueAtlasMod.id("snow_pines"));
                }
            } else {
                if (biomeTag.is(BiomeTags.IS_HILL)) {
                    return Optional.of(AntiqueAtlasMod.id("forest_hills"));
                } else {
                    return Optional.of(AntiqueAtlasMod.id("forest"));
                }
            }
        }

        if (biomeTag.is(Tags.Biomes.IS_PLAINS)) {
            if (biomeTag.is(Tags.Biomes.IS_COLD) || biomeTag.is(Tags.Biomes.IS_SNOWY)) {
                if (biomeTag.is(BiomeTags.IS_HILL)) {
                    return Optional.of(AntiqueAtlasMod.id("snow_hills"));
                } else {
                    return Optional.of(AntiqueAtlasMod.id("snow"));
                }
            } else {
                if (biomeTag.is(BiomeTags.IS_HILL)) {
                    return Optional.of(AntiqueAtlasMod.id("hills"));
                } else {
                    return Optional.of(AntiqueAtlasMod.id("plains"));
                }
            }
        }

        if (biomeTag.is(Tags.Biomes.IS_COLD)) {
            if (biomeTag.is(BiomeTags.IS_HILL)) {
                return Optional.of(AntiqueAtlasMod.id("mountains_snow_caps"));
            } else {
                return Optional.of(AntiqueAtlasMod.id("ice_spikes"));
            }
        }

        if (biomeTag.is(Tags.Biomes.IS_HOT)) {
            if (biomeTag.is(BiomeTags.IS_HILL)) {
                return Optional.of(AntiqueAtlasMod.id("desert_hills"));
            } else {
                return Optional.of(AntiqueAtlasMod.id("desert"));
            }
        }

        if (biomeTag.is(Tags.Biomes.IS_SLOPE)) {
            return Optional.of(AntiqueAtlasMod.id("mountains"));
        }

        if (biomeTag.is(Tags.Biomes.IS_PEAK)) {
            return Optional.of(AntiqueAtlasMod.id("mountains_snow_caps"));
        }

        if (biomeTag.is(Tags.Biomes.IS_MUSHROOM)) {
            return Optional.of(AntiqueAtlasMod.id("mushroom"));
        }

        if (biomeTag.is(BiomeTags.IS_BADLANDS)) {
            return Optional.of(AntiqueAtlasMod.id("mesa"));
        }

        if (biomeTag.is(BiomeTags.IS_HILL)) {
            return Optional.of(AntiqueAtlasMod.id("hills"));
        }

        if (biomeTag.is(Tags.Biomes.IS_UNDERGROUND)) {
            AntiqueAtlasMod.LOG.warn("Underground biomes aren't supported yet.");
        }

        return TileTextureMap.guessFittingTextureSetFallback(biomeTag);
    }

    //TODO: This is not implemented, because I do not want to do this.
    static public Optional<ResourceLocation> guessFittingTextureSetFallback(Holder.Reference<Biome> biome) {
        AntiqueAtlasMod.LOG.error("FALLBACK WAS CALLED");
        /**Identifier texture_set = switch (biome.getCategory()) {
            case SWAMP -> AntiqueAtlasMod.id("swamp");
            case OCEAN, RIVER ->
                    biome.getPrecipitation() == Biome.Precipitation.SNOW ? AntiqueAtlasMod.id("ice") : AntiqueAtlasMod.id("water");
            case BEACH -> AntiqueAtlasMod.id("shore");
            case JUNGLE -> AntiqueAtlasMod.id("jungle");
            case SAVANNA -> AntiqueAtlasMod.id("savanna");
            case MESA -> AntiqueAtlasMod.id("plateau_mesa");
            case FOREST ->
                    biome.getPrecipitation() == Biome.Precipitation.SNOW ? AntiqueAtlasMod.id("snow_pines") : AntiqueAtlasMod.id("forest");
            case PLAINS ->
                    biome.getPrecipitation() == Biome.Precipitation.SNOW ? AntiqueAtlasMod.id("snow") : AntiqueAtlasMod.id("plains");
            case ICY -> AntiqueAtlasMod.id("ice_spikes");
            case DESERT -> AntiqueAtlasMod.id("desert");
            case TAIGA -> AntiqueAtlasMod.id("snow");
            case EXTREME_HILLS -> AntiqueAtlasMod.id("hills");
            case MOUNTAIN -> AntiqueAtlasMod.id("mountains");
            case THEEND -> {
                List<RegistryEntryList<PlacedFeature>> features = biome.getGenerationSettings().getFeatures();
                PlacedFeature chorus_plant_feature = BuiltinRegistries.PLACED_FEATURE.get(new Identifier("chorus_plant"));
                assert chorus_plant_feature != null;
                boolean has_chorus_plant = features.stream().anyMatch(entries -> entries.stream().anyMatch(feature -> feature.value() == chorus_plant_feature));
                if (has_chorus_plant) {
                    yield AntiqueAtlasMod.id("end_island_plants");
                } else {
                    yield AntiqueAtlasMod.id("end_island");
                }
            }
            case MUSHROOM -> AntiqueAtlasMod.id("mushroom");
            case NETHER -> AntiqueAtlasMod.id("soul_sand_valley");
            case NONE -> AntiqueAtlasMod.id("end_void");
            case UNDERGROUND -> {
                Log.warn("Underground biomes aren't supported yet.");
                yield null;
            }
        };**/

        return Optional.ofNullable(null);
    }

    public boolean isRegistered(ResourceLocation id) {
        return textureMap.containsKey(id);
    }

    /**
     * If unknown biome, auto-registers a texture set. If null, returns default set.
     */
    public TextureSet getTextureSet(ResourceLocation tile) {
        if (tile == null) {
            return getDefaultTexture();
        }

        return textureMap.getOrDefault(tile, getDefaultTexture());
    }

    public ITexture getTexture(SubTile subTile) {
        return getTextureSet(subTile.tile).getTexture(subTile.variationNumber);
    }

    public List<ResourceLocation> getAllTextures() {
        List<ResourceLocation> list = new ArrayList<>();

        for (Entry<ResourceLocation, TextureSet> entry : textureMap.entrySet()) {
            Arrays.stream(entry.getValue().textures).forEach(iTexture -> list.add(iTexture.getTexture()));
        }

        return list;
    }
}
