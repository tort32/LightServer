package com.github.tort32.lifx.send;

import com.github.tort32.lifx.protocol.OutBuffer;
import com.github.tort32.lifx.protocol.message.Frame;
import com.github.tort32.lifx.protocol.message.FrameAddress;
import com.github.tort32.lifx.protocol.message.Payload;
import com.github.tort32.lifx.protocol.message.ProtocolHeader;

public class GetService extends Payload {
	public static final int ID = 2;
	public static final int LENGTH = 0;

	public GetService() {
		super(ID, LENGTH);
	}

	@Override
	public void setHeader(Frame frame, FrameAddress address, ProtocolHeader header) {
		super.setHeader(frame, address, header);
		frame.mTagged = 1;
		address.mResRequired = 1;
		frame.mSource.set(0xBAADAA55); // Need to filter response messages
	}

	@Override
	public void write(OutBuffer buffer) {
		// Empty
	}
}
