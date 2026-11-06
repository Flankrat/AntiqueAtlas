package hunternif.mc.impl.atlas.network.packet.s2c.play;

import hunternif.mc.impl.atlas.network.NetworkManager;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.client.gui.GuiAtlas;
import hunternif.mc.impl.atlas.core.AtlasData;
import hunternif.mc.impl.atlas.network.packet.s2c.S2CPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Used to sync atlas data from server to client.
 *
 * @author Hunternif
 * @author Haven King
 */
public class MapDataS2CPacket extends S2CPacket {
    public static final ResourceLocation ID = AntiqueAtlasMod.id("packet", "s2c", "map", "data");

    public MapDataS2CPacket(int atlasID, CompoundTag data) {
        this.writeVarInt(atlasID);
        this.writeNbt(data);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @OnlyIn(Dist.CLIENT)
    public static void apply(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        int atlasID = buf.readVarInt();
        CompoundTag data = buf.readNbt();

        if (data == null) return;

        context.queue(() -> {
            AtlasData atlasData = AntiqueAtlasMod.tileData.getData(atlasID, context.getPlayer().getCommandSenderWorld());
            atlasData.updateFromNbt(data);

            if (AntiqueAtlasMod.CONFIG.doSaveBrowsingPos && Minecraft.getInstance().screen instanceof GuiAtlas) {
                ((GuiAtlas) Minecraft.getInstance().screen).loadSavedBrowsingPosition();
            }
        });
    }
}
