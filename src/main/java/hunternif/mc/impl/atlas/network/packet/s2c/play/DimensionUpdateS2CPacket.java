package hunternif.mc.impl.atlas.network.packet.s2c.play;

import hunternif.mc.impl.atlas.network.NetworkManager;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.core.AtlasData;
import hunternif.mc.impl.atlas.core.TileInfo;
import hunternif.mc.impl.atlas.network.packet.s2c.S2CPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DimensionUpdateS2CPacket extends S2CPacket {
	public static final ResourceLocation ID = AntiqueAtlasMod.id("packet", "s2c", "dimension", "update");

	public DimensionUpdateS2CPacket(int atlasID, ResourceKey<Level> world, Collection<TileInfo> tiles) {
		this.writeVarInt(atlasID);
		this.writeResourceLocation(world.location());
		this.writeVarInt(tiles.size());

		for (TileInfo tile : tiles) {
			this.writeVarInt(tile.x);
			this.writeVarInt(tile.z);
			this.writeResourceLocation(tile.id);
		}
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@OnlyIn(Dist.CLIENT)
	public static void apply(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		int atlasID = buf.readVarInt();
		ResourceKey<Level> world = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
		int tileCount = buf.readVarInt();

		if (world == null) {
			// TODO FABRIC
			return;
		}

		List<TileInfo> tiles = new ArrayList<>();
		for (int i = 0; i < tileCount; ++i) {
			tiles.add(new TileInfo(
					buf.readVarInt(),
					buf.readVarInt(),
					buf.readResourceLocation())
			);
		}

		context.queue(() -> {
			AtlasData data = AntiqueAtlasMod.tileData.getData(atlasID, context.getPlayer().getCommandSenderWorld());

			for (TileInfo info : tiles) {
				data.getWorldData(world).setTile(info.x, info.z, info.id);
			}
		});
	}
}
