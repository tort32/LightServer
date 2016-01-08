package com.github.tort32.api.lifx.protocol.device.recieve;

import com.github.tort32.api.lifx.protocol.InBuffer;
import com.github.tort32.api.lifx.protocol.Types.*;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class StateLabel extends Payload {
	public static final int ID = 25;
	public static final int LENGTH = 32;

	public CharString mLabel; // 32 bytes. String

	public StateLabel(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mLabel = buffer.readString(32);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mLabel=" + mLabel + "\n");
		sb.append("}");
		return sb.toString();
	}
}
