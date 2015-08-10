package com.github.tort32.lifx.protocol.message;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.MacAddress;
import com.github.tort32.lifx.protocol.OutBuffer;
import com.github.tort32.lifx.protocol.Types.*;

public class FrameAddress {
	public MacAddress mTarget = new MacAddress(); // 64 bits. 6 byte device address (MAC address) or zero (0) means all devices
	public UInt8[] mReserved1 = new UInt8[6]; // 48 bits. Must all be zero (0)
	public byte mReserved2; // 6 bits. Reserved
	public byte mAckRequired; // 1 bit. Acknowledgement message required
	public byte mResRequired; // 1 bit. Response message required
	public UInt8 mSequence = new UInt8(0); // 8 bits. Wrap around message sequence number

	public static final int LENGTH = 16;

	public FrameAddress() {
		mReserved1[5] = new UInt8(0);
		mReserved1[4] = new UInt8(0);
		mReserved1[3] = new UInt8(0);
		mReserved1[2] = new UInt8(0);
		mReserved1[1] = new UInt8(0);
		mReserved1[0] = new UInt8(0);
	}

	public FrameAddress(InBuffer buffer) {
		buffer.checkLength(LENGTH);
		mTarget = buffer.readMacAddress();
		mReserved1[5] = buffer.readUInt8();
		mReserved1[4] = buffer.readUInt8();
		mReserved1[3] = buffer.readUInt8();
		mReserved1[2] = buffer.readUInt8();
		mReserved1[1] = buffer.readUInt8();
		mReserved1[0] = buffer.readUInt8();
		byte b14 = buffer.readByte();
		mReserved2 = (byte) ((b14 >> 2) & 0x3F);
		mAckRequired = (byte) ((b14 >> 1) & 0x01);
		mResRequired = (byte) (b14 & 0x01);
		mSequence = buffer.readUInt8();
	}

	public void write(OutBuffer buffer) {
		byte b14 = (byte) (((mReserved2 & 0x3F) << 2) | ((mAckRequired & 0x01) << 1) | (mResRequired & 0x01));
		buffer
			.beginChunk()
			.write(mTarget)
			.write(mReserved1[5])
			.write(mReserved1[4])
			.write(mReserved1[3])
			.write(mReserved1[2])
			.write(mReserved1[1])
			.write(mReserved1[0])
			.write(b14)
			.write(mSequence)
			.endChunk(LENGTH);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mTarget=" + mTarget + "\n");
		sb.append("  mReserved1=" + mReserved1[5] + mReserved1[4] + mReserved1[3] + mReserved1[2] + mReserved1[1] + mReserved1[0] + "\n");
		sb.append("  mReserved2=" + mReserved2 + "\n");
		sb.append("  mAckRequired=" + mAckRequired + "\n");
		sb.append("  mResRequired=" + mResRequired + "\n");
		sb.append("  mSequence=" + mSequence + "\n");
		sb.append("}");
		return sb.toString();
	}
}
