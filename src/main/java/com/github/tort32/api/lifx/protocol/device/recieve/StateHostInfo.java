package com.github.tort32.api.lifx.protocol.device.recieve;

import com.github.tort32.api.lifx.protocol.InBuffer;
import com.github.tort32.api.lifx.protocol.Types.*;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class StateHostInfo extends Payload {
	public static final int ID = 13;
	public static final int LENGTH = 14;

	public Float32 mSignal; // radio receive signal strength in milliWatts
	public UInt32 mTx; // bytes transmitted since power on
	public UInt32 mRx; // bytes received since power on
	public Int16 mReserved;

	public StateHostInfo(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mSignal = buffer.readFloat32();
		mTx = buffer.readUInt32();
		mRx = buffer.readUInt32();
		mReserved = buffer.readInt16();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mSignal=" + mSignal + "\n");
		sb.append("  mTx=" + mTx + "\n");
		sb.append("  mRx=" + mRx + "\n");
		sb.append("  mReserved=" + mReserved + "\n");
		sb.append("}");
		return sb.toString();
	}
}
