package com.github.tort32.lifx.light.recieve;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.Power;
import com.github.tort32.lifx.protocol.message.Payload;

public class StatePower extends Payload {
	public static final int ID = 118;
	public static final int LENGTH = 2;

	public Power mLevel; // Power level

	public StatePower(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mLevel = buffer.readPower();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mLevel=" + mLevel + "\n");
		sb.append("}");
		return sb.toString();
	}
}
