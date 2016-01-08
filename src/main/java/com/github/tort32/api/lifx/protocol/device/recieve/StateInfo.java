package com.github.tort32.api.lifx.protocol.device.recieve;

import com.github.tort32.api.lifx.protocol.InBuffer;
import com.github.tort32.api.lifx.protocol.Types.*;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class StateInfo extends Payload {
	public static final int ID = 35;
	public static final int LENGTH = 24;

	public UInt64 mTime; // current time (absolute time in nanoseconds since epoch)
	public UInt64 mUpTime; // time since last power on (relative time in nanoseconds)
	public UInt64 mDownTime; // last power off period, 5 second accuracy (in nanoseconds)

	public StateInfo(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mTime = buffer.readUInt64();
		mUpTime = buffer.readUInt64();
		mDownTime = buffer.readUInt64();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mTime=" + mTime + "\n");
		sb.append("  mUpTime=" + mUpTime + "\n");
		sb.append("  mDownTime=" + mDownTime + "\n");
		sb.append("}");
		return sb.toString();
	}
}
