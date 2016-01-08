package com.github.tort32.api.lifx.protocol.device.recieve;

import com.github.tort32.api.lifx.protocol.InBuffer;
import com.github.tort32.api.lifx.protocol.Types.*;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class StateVersion extends Payload {
	public static final int ID = 33;
	public static final int LENGTH = 12;

	public UInt32 mVendor; // vendor ID
	public UInt32 mProduct; // product ID
	public UInt32 mVersion; // hardware version

	public StateVersion(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mVendor = buffer.readUInt32();
		mProduct = buffer.readUInt32();
		mVersion = buffer.readUInt32();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mVendor=" + mVendor + "\n");
		sb.append("  mProduct=" + mProduct + "\n");
		sb.append("  mVersion=" + mVersion + "\n");
		sb.append("}");
		return sb.toString();
	}
}
