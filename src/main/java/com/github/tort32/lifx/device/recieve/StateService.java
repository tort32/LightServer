package com.github.tort32.lifx.device.recieve;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.message.Payload;
import com.github.tort32.lifx.protocol.Types.*;

public class StateService extends Payload {
	public static final int ID = 3;
	public static final int LENGTH = 5;
	
	public static final int DEFAULT_PORT = 56700;

	public UInt8 mService; // 8 bits. Maps to Service
	public UInt32 mPort; // 32 bits

	public StateService(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mService = buffer.readUInt8();
		mPort = buffer.readUInt32();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mService=" + mService + "\n");
		sb.append("  mPort=" + mPort + "\n");
		sb.append("}");
		return sb.toString();
	}
	
	public static enum Service {
		UDP(1);
		
		private byte value;
		
		private Service(int value) {
			this.value = (byte) value;
		}
		
		public byte getValue() {
			return value;
		}
	}
}
