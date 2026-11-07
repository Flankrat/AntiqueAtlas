# Antique Atlas (Forge 1.20.1) ![Minecraft 1.20.1](https://img.shields.io/badge/minecraft-1.20.1-blue.svg) ![Forge 47](https://img.shields.io/badge/forge-47-orange.svg)

Antique Atlas is a book that acts like a map featuring infinite scrolling, zoom and custom labeled markers. The map is generated around the player by calculating the average biome in each 16x16 chunk.

This is a Forge-only version of Antique Atlas for Minecraft 1.20.1, made for the RLCraft 1.20.1 port. It needs nothing besides Forge: no Architectury API, no Cloth Config, no Fabric compatibility layer. It keeps the RLCraft / RLCraft Dregora behaviour from 1.12.2, including the corner minimap.

## Using it

- Craft an **Empty Antique Atlas** from a book (or book and quill) and a compass, then use it to start a new atlas. Each atlas remembers its own map and markers, so you share a map by handing over the atlas.
- **Copy** an atlas by crafting it with empty atlases, and **merge** two atlases by crafting them together.
- Add a filled **map** to an atlas in a cartography table.
- Open the atlas to see the full map: drag to pan, use the arrow buttons or keys to move, and scroll or press +/- to zoom. Markers can be added, labeled and deleted, and the map can be exported to a PNG image.
- While you **hold an atlas**, a small copy of its map is shown in the top-right corner of the screen (the 1.12.2 minimap).
- Villages, dragon roosts and caves, battle towers, dungeons, the big RLCraft landmarks and many other structures are marked on the map automatically, and the spot where you die gets a death marker (see *Automatic markers* below).
- With the Waystones mod, every waystone you activate is marked with its name on the atlases you carry.

All settings are in `config/antiqueatlas-common.toml`:
- `[gameplay]`: atlas item required, automatic markers, browsing position.
- `[userInterface]`: zoom levels and scroll direction.
- `[performance]`: scan radius and rescanning.
- `[appearance]`: the map drawn on the held atlas (off by default, see `mapOnHeldAtlas`).
- `[minimap]`: the corner minimap's position, size, tile and marker size, whether the atlas has to be held, and optional coordinates and biome name under the map (off by default).
- `[autoMarkers]`: which structures and world generation features get markers, and waystone markers.

### Automatic markers

Like the 1.12.2 AntiqueAtlasAutoMarker mod used by RLCraft Dregora, structures and other places are marked on every atlas once they generate. Each line of `structureMarkers` in `[autoMarkers]` is `structure, marker type, label`:

```
structureMarkers = ["minecraft:stronghold, dungeon, Stronghold", "#minecraft:village, village, Village", "fbt:mega_battletower, megatower, Mega Battle Tower"]
```

- The structure is a structure id, or a structure tag starting with `#`. `/locate structure` lists the ids.
- The marker type is a marker from `atlas/markers` (anvil, bed, book, cave, defiled, diamond, dragon_blue, dragon_gold, dragon_green, dragon_purple, dragon_red, dungeon, end_city, farm, google, lycanites, mansion, megatower, monsterspawner, monument, nether_portal, parasite, pickaxe, quadtower, red_x_large, red_x_small, ruins, scroll, ship, skull, sword, tomb, tower, tracks, village, waystone, wizardtower).
- The label can be empty.

`featureMarkers` works the same way for world generation features that aren't structures, such as Ice and Fire's dragon roosts and caves. The defaults cover the structures of the RLCraft 1.20.1 pack; ids of mods that aren't installed are simply ignored. Only places that generate after a line is added get markers.

Biome textures are chosen from biome tags, so modded biomes get a fitting texture automatically. Resource packs can assign their own textures; see the [wiki on editing textures](https://github.com/Hunternif/AntiqueAtlas/wiki/Editing-Textures).

## Building

```
gradlew build
```

The mod jar is in `build/libs`. Building needs a Java 17 JDK.

Releases are on the [releases page](https://github.com/Flankrat/AntiqueAtlas/releases). Forge's update checker reads `update.json` on the main branch (`1.20.1`), so for a new release: raise `mod_version` in `gradle.properties`, add the version to `update.json` (and point the promos at it), then publish the jar as a release.

## Changes in this version

This is a fork of [ByteDevEs-MC/AntiqueAtlas](https://github.com/ByteDevEs-MC/AntiqueAtlas) (Antique Atlas 7.1.2 for 1.20.1), which is based on [Hunternif/AntiqueAtlas](https://github.com/Hunternif/AntiqueAtlas). The commit history has the details. It has its own version numbers, starting again at 1.0.0 (the first Forge-only build), since it has changed too much to follow Antique Atlas 7's. In short:

- **Forge-only.** The Architectury multi-loader project (common, Forge and Fabric parts, Fabric's Yarn names) is now a single ForgeGradle project using Mojang's official names.
- **No library mods.**
  - Architectury API's events, networking, registration, reload listeners and key bindings are replaced with plain Forge code. Small built-in replacements keep the same method names for events and packets.
  - Cloth Config is replaced with a normal Forge config file, with the same options and defaults.
- **Fixes for Forge.**
  - The export progress bar and the held-atlas rendering used mixins that Forge's renderers bypass; they now use Forge events.
  - The cartography table mixin targets the production name of the method it hooks.
  - Atlas items and recipes are always registered, so a client and server can't disagree about them.
- **Automatic markers** for structures, world generation features and activated waystones, set up in the config like RLCraft Dregora's AntiqueAtlasAutoMarker. Village markers now use the `#minecraft:village` tag, so they work again (they had stopped working in 1.20).
- **The 1.12.2 corner minimap is back.** Antique Atlas 7 had replaced it with drawing the map on the atlas in your hand; that is still available as an option.

## Credits

**Antique Atlas** was created by **Hunternif**, who wrote most of the mod and drew its textures.

**Antique Atlas 5 to 7** (Minecraft 1.14 to 1.18.2, Fabric and Forge) were maintained by **tyra314** and the AntiqueAtlasTeam. Much of the code from that time is by **Haven King** (including the networking), with many more changes from **Stereowalker**, **OsipXD** and **asiekierka**.

**The corner minimap** comes from **Kenkron**'s *Antique Atlas Overlay*, which was merged into Antique Atlas for 1.12.2 and is what RLCraft and RLCraft Dregora show. Its settings and defaults in this version follow RLCraft Dregora.

**The automatic markers** follow **Nischhelm**'s *AntiqueAtlasAutoMarker* and **Fonnymunkey**'s *AARCAddon* for 1.12.2, as set up in RLCraft Dregora. The marker icons in `textures/gui/markers/artsy` are by **Star Artsy** and the ones in `textures/gui/markers/knightgallade` by **Knight Gallade**, made for RLCraft and RLCraft Dregora. Star Artsy's icons are used with permission. The code that places the markers is new in this version.

**The 1.20.1 port** this fork is based on is by **ByteDevEs** ([ByteDevEs-MC/AntiqueAtlas](https://github.com/ByteDevEs-MC/AntiqueAtlas)).

**The Forge-only conversion** and the minimap port to 1.20.1 are by **Flankrat**, for the RLCraft 1.20.1 port.

**Everyone else who contributed** to Antique Atlas over the years, as listed in the mod's credits: Kenkron, osipxd, thecodewarrior, TehNut, crafteverywhere, coolAlias, Ruukas97, Mazdallier, chesterccj305, asiekierka, sam-kirby, esotericist, dadodollabela, Yulife, Waterrail, The-Fireplace, TBM13, Rikshy, GirafiStudios, DrCopernicus, DBotThePony, AugiteSoul, Stereowalker, and the translators.

**Tools:**
- The code was moved from Yarn to Mojang's names with **Fabric Loom**'s `migrateMappings`.
- The built-in event and networking classes use the same method names as **Architectury API** (by shedaniel and the Architectury team), so the existing code didn't need rewriting. They are separate, Forge-only implementations; no Architectury code is included.

## License

- **Code** (`src/main/java`): GNU GPL v3 or later. This modified version is also GPL v3, and its source is this repository.
- **Textures** (`src/main/resources/assets/antiqueatlas/textures`): by Hunternif, under [Creative Commons Attribution-NonCommercial-ShareAlike 3.0](https://creativecommons.org/licenses/by-nc-sa/3.0/), which means credit Hunternif and no commercial use.
- **RLCraft marker icons** (`textures/gui/markers/artsy` and `textures/gui/markers/knightgallade`): by Star Artsy (used with permission) and Knight Gallade for RLCraft; not covered by the licenses above. These files are taken from [AntiqueAtlasAutoMarker](https://github.com/Nischhelm/AntiqueAtlasAutoMarker), which is MIT-licensed; its license notice is in [LICENSE-AntiqueAtlasAutoMarker.txt](LICENSE-AntiqueAtlasAutoMarker.txt).

See [LICENSE](LICENSE) for the full text. Both license files are also included in the mod jar under `META-INF/licenses`.

## API

Other mods can use `hunternif.mc.api.AtlasAPI` to put tiles and markers on atlases (TileAPI and MarkerAPI). See the sources and the [API wiki page](https://github.com/Hunternif/AntiqueAtlas/wiki/API).
