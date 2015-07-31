package com.github.tort32.lifx.protocol.message;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.OutBuffer;
import com.github.tort32.lifx.protocol.Types.UInt16;
import com.github.tort32.lifx.protocol.Types.UInt32;
import com.github.tort32.lifx.protocol.Types.UInt8;

public class Frame {
	public UInt16 mSize = new UInt16(0); // 16 bits. Size of entire message in bytes including this field
	public UInt8 mOrigin = new UInt8(0); // 2 bits. ProtocolMessage origin indicator: must be zero (0)
	public byte mTagged; // 1 bit. Determines usage of the Frame Address target field
	public byte mAddressable = 1; // 1 bit. ProtocolMessage includes a target address: must be one (1)
	public UInt16 mProtocol = new UInt16(1024); // 12 bits. Protocol number: must be 1024 (decimal)
	public UInt32 mSource = new UInt32(0); // 32 bits. Source identifier: unique value set by the client, used by responses

	public static final int LENGTH = 8;

	public Frame() {
	}

	public Frame(InBuffer buffer) {
		buffer.checkLength(LENGTH);
		mSize = buffer.readUInt16();
		byte[] b23 = buffer.readBytes(2);
		mOrigin.set((byte) ((b23[0] >> 6) & 0x03));
		mTagged = (byte) ((b23[0] >> 5) & 0x01);
		mAddressable = (byte) ((b23[0] >> 4) & 0x01);
		mProtocol.set(((b23[1] & 0xFF) << 8) | (b23[0] & 0xFF));
		mSource = buffer.readUInt32();
	}

	public void write(OutBuffer buffer) {
		// Do some magic to write 12 bits of uint16 and 4 bits of other fields in 2 bytes
		byte[] b23 = new OutBuffer().write(mProtocol).toArray();
		b23[1] |= (byte) (((mOrigin.getShort() & 0x03) << 6) | ((mTagged & 0x01) << 5) | ((mAddressable & 0x01) << 4));
		// TODO does it correct?
		/*buffer[Offset + 2] = (byte) (mProtocol & 0x00FF);
		buffer[Offset + 3] = (byte) (((mOrigin & 0x03) << 6) | ((mTagged & 0x01) << 5) |
		                             ((mAddressable & 0x01) << 4) | ((mProtocol & 0x0F00) >> 8));*/
		buffer
			.beginChunk()
			.write(mSize) // TODO Do we need to have correct size here? See Message.toArray size inscription
			.write(b23)
			.write(mSource)
			.endChunk(LENGTH);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mSize=" + mSize + "\n");
		sb.append("  mOrigin=" + mOrigin + "\n");
		sb.append("  mTagged=" + mTagged + "\n");
		sb.append("  mAddressable=" + mAddressable + "\n");
		sb.append("  mProtocol=" + mProtocol + "\n");
		sb.append("  mSource=" + mSource + "\n");
		sb.append("}");
		return sb.toString();

	}
}