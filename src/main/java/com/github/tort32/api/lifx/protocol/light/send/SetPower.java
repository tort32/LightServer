package com.github.tort32.api.lifx.protocol.light.send;

import com.github.tort32.api.lifx.protocol.OutBuffer;
import com.github.tort32.api.lifx.protocol.Power;
import com.github.tort32.api.lifx.protocol.Types.UInt32;
import com.github.tort32.api.lifx.protocol.message.Frame;
import com.github.tort32.api.lifx.protocol.message.FrameAddress;
import com.github.tort32.api.lifx.protocol.message.Payload;
import com.github.tort32.api.lifx.protocol.message.ProtocolHeader;

public class SetPower extends Payload {
	public static final int ID = 117;
	public static final int LENGTH = 2;
	
	public Power mLevel; // Power level
	public UInt32 mDuration; // Power level transition time in milliseconds.
	
	private boolean requestState; // Request the device to transmit a State message.

	public SetPower(boolean requestState, boolean enable, long duration) {
		super(ID, LENGTH);
		this.requestState = requestState;
		mLevel = new Power(enable);
		mDuration = new UInt32(duration);
	}
	
	@Override
	public void setHeader(Frame frame, FrameAddress address, ProtocolHeader header) {
		super.setHeader(frame, address, header);
		if (requestState) {
			address.mResRequired = 1;
		}
	}

	@Override
	public void write(OutBuffer buffer) {
		buffer
			.beginChunk()
			.write(mLevel)
			.endChunk(LENGTH);
	}
}
