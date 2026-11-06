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

        if (biomeTag.is(BiomeTags.IS_TAIGA) || biomeTag.is(Tags.Biomes.IS_CONIFEROUS)) {
            boolean cold = biomeTag.is(Tags.Biomes.IS_COLD) || biomeTag.is(Tags.Biomes.IS_SNOWY)
                    || biomeTag.value().getBaseTemperature() < 0.15F;
            if (biomeTag.is(BiomeTags.IS_HILL)) {
                return Optional.of(AntiqueAtlasMod.id(cold ? "snow_pines_hills" : "pines_hills"));
            }
            return Optional.of(AntiqueAtlasMod.id(cold ? "snow_pines" : "pines"));
        }

        if (biomeTag.is(Tags.Biomes.IS_DESERT) || biomeTag.is(Tags.Biomes.IS_SANDY)) {
            return Optional.of(AntiqueAtlasMod.id(biomeTag.is(BiomeTags.IS_HILL) ? "desert_hills" : "desert"));
        }

        if (biomeTag.is(Tags.Biomes.IS_UNDERGROUND) || biomeTag.is(Tags.Biomes.IS_CAVE)) {
            return Optional.of(AntiqueAtlasMod.id("cave_walls"));
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

        return TileTextureMap.guessFittingTextureSetFallback(biomeTag);
    }

    /**
     * Last resort for biomes that none of the tag checks above match (biomes from mods with
     * few or no tags): pick a texture set from the biome's climate, so no biome ends up with
     * the placeholder texture.
     */
    static public Optional<ResourceLocation> guessFittingTextureSetFallback(Holder.Reference<Biome> biome) {
        Biome b = biome.value();
        float temperature = b.getBaseTemperature();
        float downfall = b.getModifiedClimateSettings().downfall();
        String set;
        if (temperature < 0.15F) {
            set = "snow";
        } else if (!b.hasPrecipitation() && temperature >= 1.5F) {
            set = "desert";
        } else if (temperature >= 0.9F && downfall >= 0.85F) {
            set = "jungle";
        } else if (downfall >= 0.6F) {
            set = "forest";
        } else if (temperature >= 1.2F) {
            set = "savanna";
        } else {
            set = "plains";
        }
        AntiqueAtlasMod.LOG.info("No biome tags matched {}; chose texture set {} from its climate (temperature {}, downfall {})",
                biome.key().location(), set, temperature, downfall);
        return Optional.of(AntiqueAtlasMod.id(set));
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

        TextureSet set = textureMap.get(tile);
        if (set == null) {
            if (UNKNOWN_TILES_LOGGED.add(tile)) {
                Log.warn("No texture set for map tile %s, drawing the placeholder texture. Add a tile mapping for it (atlas/tiles/<name>.json in a resource pack).", tile);
            }
            return getDefaultTexture();
        }
        return set;
    }

    /** Tiles already reported as missing a texture set, so each is only logged once. */
    private static final Set<ResourceLocation> UNKNOWN_TILES_LOGGED = java.util.concurrent.ConcurrentHashMap.newKeySet();

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
