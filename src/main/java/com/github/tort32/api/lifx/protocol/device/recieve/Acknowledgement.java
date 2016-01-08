package com.github.tort32.api.lifx.protocol.device.recieve;

import com.github.tort32.api.lifx.protocol.InBuffer;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class Acknowledgement extends Payload {
	public static final int ID = 45;
	public static final int LENGTH = 0;

	public Acknowledgement(InBuffer buffer) {
		super(ID, LENGTH);
	}
}
