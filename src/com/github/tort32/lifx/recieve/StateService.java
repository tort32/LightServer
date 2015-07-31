package com.github.tort32.lifx.recieve;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.Types.UInt32;
import com.github.tort32.lifx.protocol.message.Payload;

public class StateService extends Payload {
	public static final int ID = 3;
	public static final int LENGTH = 5;

	public byte mService; // 8 bits. Maps to Service
	public UInt32 mPort; // 32 bits

	public StateService(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mService = buffer.readByte();
		mPort = buffer.readUInt32();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mService=" + mService + "\n");
		sb.append("  mPort=" + mPort + "\n");
		sb.append("}");
		return sb.toString();
	}
}
