package com.github.tort32.lifx.device.recieve;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.message.Payload;
import com.github.tort32.lifx.protocol.Types.*;

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
