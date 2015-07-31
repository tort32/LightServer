package com.github.tort32.lifx.send;

import com.github.tort32.lifx.protocol.OutBuffer;
import com.github.tort32.lifx.protocol.Types.HSBK;
import com.github.tort32.lifx.protocol.Types.UInt32;
import com.github.tort32.lifx.protocol.Types.UInt8;
import com.github.tort32.lifx.protocol.message.Frame;
import com.github.tort32.lifx.protocol.message.FrameAddress;
import com.github.tort32.lifx.protocol.message.Payload;
import com.github.tort32.lifx.protocol.message.ProtocolHeader;

public class SetColor extends Payload {
	public static final int ID = 102;
	public static final int LENGTH = 1 + HSBK.LENGTH + 4; // 13

	private UInt8 mReserved; // 8 bits. Reserved
	public HSBK mColor; // 8 bytes. Color.
	public UInt32 mDuration; // 32 bits. The color transition time in milliseconds.

	private boolean requestState; // Request the device to transmit a State message.

	public SetColor(boolean requestState) {
		super(ID, LENGTH);
		this.requestState = requestState;
	}

	@Override
	public void setHeader(Frame frame, FrameAddress address, ProtocolHeader header) {
		super.setHeader(frame, address, header);
		frame.mTagged = 1;
		if(requestState) {
			address.mResRequired = 1;
		}
	}

	@Override
	public void write(OutBuffer buffer) {
		buffer
			.beginChunk()
			.write(mReserved)
			.write(mColor)
			.write(mDuration)
			.endChunk(LENGTH);
	}
}
