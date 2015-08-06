package com.github.tort32.lifx.device.send;

import com.github.tort32.lifx.protocol.OutBuffer;
import com.github.tort32.lifx.protocol.Types.CharString;
import com.github.tort32.lifx.protocol.message.Frame;
import com.github.tort32.lifx.protocol.message.FrameAddress;
import com.github.tort32.lifx.protocol.message.Payload;
import com.github.tort32.lifx.protocol.message.ProtocolHeader;

public class EchoRequest extends Payload {
	public static final int ID = 58;
	public static final int LENGTH = 64;
	
	public CharString mPayload = new CharString(64); // byte array, 64 bytes. Payload be echoed back

	public EchoRequest(String str) {
		super(ID, LENGTH);
		mPayload.set(str); 
	}
	
	@Override
	public void setHeader(Frame frame, FrameAddress address, ProtocolHeader header) {
		super.setHeader(frame, address, header);
		address.mResRequired = 1;
	}

	@Override
	public void write(OutBuffer buffer) {
		buffer
			.beginChunk()
			.write(mPayload)
			.endChunk(LENGTH);
	}
}
