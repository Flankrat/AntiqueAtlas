package hunternif.mc.impl.atlas.network;

import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Packet sending and receiving on top of a single Forge {@link SimpleChannel}.
 * <p>
 * Replaces Architectury's NetworkManager (the mod was Architectury-based before it became
 * Forge-only) with the same small API the packets use: every packet is a {@link FriendlyByteBuf}
 * with a {@link ResourceLocation} ID, and receivers are registered per ID and side.
 */
public final class NetworkManager {
    private static final String PROTOCOL = "1";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            AntiqueAtlasMod.id("main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    private static final Map<ResourceLocation, NetworkReceiver> S2C = new HashMap<>();
    private static final Map<ResourceLocation, NetworkReceiver> C2S = new HashMap<>();

    private static boolean channelRegistered;

    private NetworkManager() {}

    public enum Side { S2C, C2S }

    @FunctionalInterface
    public interface NetworkReceiver {
        void receive(FriendlyByteBuf buf, PacketContext context);
    }

    /** What a receiver gets besides the packet data. */
    public interface PacketContext {
        /** The sending player on the server, or the local player on the client. */
        Player getPlayer();

        /** Runs the task on the main game thread (packets arrive on the network thread). */
        void queue(Runnable task);
    }

    /** Called once from the mod constructor, on both sides, before any receiver is registered. */
    public static synchronized void init() {
        if (channelRegistered) return;
        channelRegistered = true;
        CHANNEL.messageBuilder(Message.class, 0)
                .encoder(Message::encode)
                .decoder(Message::decode)
                .consumerMainThread(NetworkManager::handle)
                .add();
    }

    public static void registerReceiver(Side side, ResourceLocation id, NetworkReceiver receiver) {
        (side == Side.S2C ? S2C : C2S).put(id, receiver);
    }

    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        CHANNEL.sendToServer(new Message(id, buf));
    }

    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf buf) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new Message(id, buf));
    }

    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation id, FriendlyByteBuf buf) {
        for (ServerPlayer player : players) {
            sendToPlayer(player, id, buf);
        }
    }

    private static void handle(Message message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        boolean toClient = ctx.getDirection() == NetworkDirection.PLAY_TO_CLIENT
                || ctx.getDirection() == NetworkDirection.LOGIN_TO_CLIENT;
        NetworkReceiver receiver = (toClient ? S2C : C2S).get(message.id);
        if (receiver == null) {
            AntiqueAtlasMod.LOG.warn("Received unknown Antique Atlas packet {}", message.id);
            return;
        }
        PacketContext context = new PacketContext() {
            @Override
            public Player getPlayer() {
                if (toClient) {
                    return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientPlayerGetter::get);
                }
                return ctx.getSender();
            }

            @Override
            public void queue(Runnable task) {
                // consumerMainThread already runs us on the main thread, so run it right away.
                task.run();
            }
        };
        try {
            receiver.receive(message.data, context);
        } finally {
            message.data.release();
        }
    }

    /** Kept in its own class so the server never loads client classes. */
    private static final class ClientPlayerGetter {
        static Player get() {
            return net.minecraft.client.Minecraft.getInstance().player;
        }
    }

    private static final class Message {
        final ResourceLocation id;
        final FriendlyByteBuf data;

        Message(ResourceLocation id, FriendlyByteBuf data) {
            this.id = id;
            this.data = data;
        }

        void encode(FriendlyByteBuf out) {
            out.writeResourceLocation(id);
            out.writeBytes(data, data.readerIndex(), data.readableBytes());
        }

        static Message decode(FriendlyByteBuf in) {
            ResourceLocation id = in.readResourceLocation();
            FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer(in.readableBytes()));
            data.writeBytes(in);
            return new Message(id, data);
        }
    }
}
