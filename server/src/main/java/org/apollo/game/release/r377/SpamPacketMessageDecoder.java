package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SpamPacketMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link SpamPacketMessage}.
 *
 * @author Major
 */
public final class SpamPacketMessageDecoder extends MessageDecoder<SpamPacketMessage> {

	@Override
	public SpamPacketMessage decode(GamePacket packet) {
		if (packet.content().hasArray()) {
			return new SpamPacketMessage(packet.content().array());
		} else {
			byte[] bytes = new byte[packet.content().readableBytes()];
			packet.content().getBytes(packet.content().readerIndex(), bytes);
			return new SpamPacketMessage(bytes);
		}
	}
}