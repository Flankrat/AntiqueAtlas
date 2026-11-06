package hunternif.mc.impl.atlas.structure;

import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;

public class Village {
    public static void registerMarkers() {
        if (AntiqueAtlasMod.CONFIG.autoVillageMarkers) {
            StructureHandler.registerMarker(
                    List.of(
                            BuiltinStructures.VILLAGE_DESERT,
                            BuiltinStructures.VILLAGE_PLAINS,
                            BuiltinStructures.VILLAGE_SAVANNA,
                            BuiltinStructures.VILLAGE_SNOWY,
                            BuiltinStructures.VILLAGE_TAIGA
                    ), AntiqueAtlasMod.id("village"), Component.translatable("gui.antiqueatlas.marker.village"));
        }
    }
}
