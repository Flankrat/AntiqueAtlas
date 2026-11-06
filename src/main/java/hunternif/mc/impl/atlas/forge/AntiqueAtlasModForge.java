package hunternif.mc.impl.atlas.forge;

import hunternif.mc.impl.atlas.AntiqueAtlasConfig;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.AntiqueAtlasModClient;
import hunternif.mc.impl.atlas.network.NetworkManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(AntiqueAtlasMod.ID)
public class AntiqueAtlasModForge {
    public AntiqueAtlasModForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AntiqueAtlasConfig.SPEC, "antiqueatlas-common.toml");
        modBus.addListener((ModConfigEvent.Loading event) -> onConfig(event.getConfig()));
        modBus.addListener((ModConfigEvent.Reloading event) -> onConfig(event.getConfig()));

        NetworkManager.init();
        AntiqueAtlasMod.init(modBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            AntiqueAtlasModClient.init(modBus);
        }
    }

    private static void onConfig(ModConfig config) {
        if (config.getSpec() == AntiqueAtlasConfig.SPEC) {
            AntiqueAtlasMod.CONFIG = AntiqueAtlasConfig.bake();
        }
    }
}
