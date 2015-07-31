package com.github.tort32.lifx.protocol.message;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.github.tort32.lifx.protocol.OutBuffer;
import com.github.tort32.lifx.protocol.Types.UInt16;

public abstract class Payload {
	public int type;

	public int length;

	public static final int OFFSET = Frame.LENGTH + FrameAddress.LENGTH + ProtocolHeader.LENGTH;

	protected Payload(int type, int length) {
		this.type = type;
		this.length = length;
	}

	public void setHeader(Frame frame, FrameAddress address, ProtocolHeader header) {
		frame.mSize = new UInt16(OFFSET + length);
		header.mType = new UInt16(type);
	}

	public void write(OutBuffer buffer) {
		throw new NotImplementedException();
	}
}
