package com.github.tort32.api.lifx.protocol.device.send;

import com.github.tort32.api.lifx.protocol.OutBuffer;
import com.github.tort32.api.lifx.protocol.Types.CharString;
import com.github.tort32.api.lifx.protocol.message.Frame;
import com.github.tort32.api.lifx.protocol.message.FrameAddress;
import com.github.tort32.api.lifx.protocol.message.Payload;
import com.github.tort32.api.lifx.protocol.message.ProtocolHeader;

public class SetLabel extends Payload {
	public static final int ID = 24;
	public static final int LENGTH = 32;
	
	public CharString mLabel = new CharString(32); // 32 bytes. String

	public SetLabel(String label) {
		super(ID, LENGTH);
		mLabel.set(label);
	}
	
	@Override
	public void setHeader(Frame frame, FrameAddress address, ProtocolHeader header) {
		super.setHeader(frame, address, header);
	}

	@Override
	public void write(OutBuffer buffer) {
		buffer
			.beginChunk()
			.write(mLabel)
			.endChunk(LENGTH);
	}
}
