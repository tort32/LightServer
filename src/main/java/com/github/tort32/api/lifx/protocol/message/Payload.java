package com.github.tort32.api.lifx.protocol.message;

import com.github.tort32.api.lifx.protocol.OutBuffer;
import com.github.tort32.api.lifx.protocol.Types.*;

public abstract class Payload {
	
	// More message types can be found at https://github.com/magicmonkey/lifxjs/blob/master/Protocol.md
	
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
		throw new RuntimeException("Not implemented");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    sb.append(getClass().getSimpleName() + " {}");
	    return sb.toString();  
	}
}
