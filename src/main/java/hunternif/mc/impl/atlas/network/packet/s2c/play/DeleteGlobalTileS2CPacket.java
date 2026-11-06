package hunternif.mc.impl.atlas.network.packet.s2c.play;

import hunternif.mc.impl.atlas.network.NetworkManager;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.core.TileDataStorage;
import hunternif.mc.impl.atlas.network.packet.s2c.S2CPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Sent from server to client to remove a custom global tile.
 * @author Hunternif
 * @author Haven King
 */
public class DeleteGlobalTileS2CPacket extends S2CPacket {
	public static final ResourceLocation ID = AntiqueAtlasMod.id("packet", "c2s", "global_tile", "delete");

	public DeleteGlobalTileS2CPacket(ResourceKey<Level> world, int chunkX, int chunkZ) {
		this.writeResourceLocation(world.location());
		this.writeVarInt(chunkX);
		this.writeVarInt(chunkZ);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@OnlyIn(Dist.CLIENT)
	public static void apply(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ResourceKey<Level> world = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
		int chunkX = buf.readVarInt();
		int chunkZ = buf.readVarInt();

		context.queue(() -> {
			TileDataStorage data = AntiqueAtlasMod.globalTileData.getData(world);
			data.removeTile(chunkX, chunkZ);
		});
	}
}
