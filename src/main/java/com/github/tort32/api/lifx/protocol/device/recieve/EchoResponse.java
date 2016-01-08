package com.github.tort32.api.lifx.protocol.device.recieve;

import com.github.tort32.api.lifx.protocol.InBuffer;
import com.github.tort32.api.lifx.protocol.Types.*;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class EchoResponse extends Payload {
	public static final int ID = 59;
	public static final int LENGTH = 64;

	public CharString mPayload; // byte array, 64 bytes

	public EchoResponse(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mPayload = buffer.readString(64);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mPayload=" + mPayload + "\n");
		sb.append("}");
		return sb.toString();
	}
}
