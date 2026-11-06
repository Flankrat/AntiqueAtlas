package hunternif.mc.impl.atlas.structure;

import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;

public class EndCity {

    public static void registerMarkers() {
        StructureHandler.registerMarker(List.of(BuiltinStructures.END_CITY), AntiqueAtlasMod.id("end_city"), Component.literal(""));
    }

}
