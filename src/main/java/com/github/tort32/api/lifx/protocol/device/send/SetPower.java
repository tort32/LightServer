package com.github.tort32.api.lifx.protocol.device.send;

import com.github.tort32.api.lifx.protocol.OutBuffer;
import com.github.tort32.api.lifx.protocol.Power;
import com.github.tort32.api.lifx.protocol.message.Frame;
import com.github.tort32.api.lifx.protocol.message.FrameAddress;
import com.github.tort32.api.lifx.protocol.message.Payload;
import com.github.tort32.api.lifx.protocol.message.ProtocolHeader;

public class SetPower extends Payload {
	public static final int ID = 21;
	public static final int LENGTH = 2;
	
	public Power mLevel; // Power level.

	public SetPower(boolean enable) {
		super(ID, LENGTH);
		mLevel = new Power(enable);
	}
	
	@Override
	public void setHeader(Frame frame, FrameAddress address, ProtocolHeader header) {
		super.setHeader(frame, address, header);
	}

	@Override
	public void write(OutBuffer buffer) {
		buffer
			.beginChunk()
			.write(mLevel)
			.endChunk(LENGTH);
	}
}
