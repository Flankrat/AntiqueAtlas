package hunternif.mc.impl.atlas.network.packet.c2s;

import hunternif.mc.impl.atlas.network.NetworkManager;
import hunternif.mc.impl.atlas.network.packet.AntiqueAtlasPacket;

public abstract class C2SPacket extends AntiqueAtlasPacket {
	public void send() {
		NetworkManager.sendToServer(this.getId(), this);
	}
}
