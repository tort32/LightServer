package com.github.tort32.api.lifx.protocol.light.send;

import com.github.tort32.api.lifx.protocol.OutBuffer;
import com.github.tort32.api.lifx.protocol.message.Frame;
import com.github.tort32.api.lifx.protocol.message.FrameAddress;
import com.github.tort32.api.lifx.protocol.message.Payload;
import com.github.tort32.api.lifx.protocol.message.ProtocolHeader;

public class GetPower extends Payload {
	public static final int ID = 116;
	public static final int LENGTH = 0;

	public GetPower() {
		super(ID, LENGTH);
	}
	
	@Override
	public void setHeader(Frame frame, FrameAddress address, ProtocolHeader header) {
		super.setHeader(frame, address, header);
		address.mResRequired = 1;
	}

	@Override
	public void write(OutBuffer buffer) {
		// Empty
	}
}
