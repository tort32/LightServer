package com.github.tort32.api.lifx.protocol.light.recieve;

import com.github.tort32.api.lifx.protocol.HSBK;
import com.github.tort32.api.lifx.protocol.InBuffer;
import com.github.tort32.api.lifx.protocol.Power;
import com.github.tort32.api.lifx.protocol.Types.*;
import com.github.tort32.api.lifx.protocol.message.Payload;

public class State extends Payload {
	public static final int ID = 107;
	public static final int LENGTH = HSBK.LENGTH + 44; // 52

	public HSBK mColor; // HSBK. Color
	public Int16 mReserved1; //	16 bit. Reserved
	public Power mPower; // 16 bit. Power level
	public CharString mLabel; // 32 bytes. String
	public UInt64 mReserved2; // 64 bit. Reserved

	public State(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mColor = buffer.readHSBK();
		mReserved1 = buffer.readInt16();
		mPower = buffer.readPower();
		mLabel = buffer.readString(32);
		mReserved2 = buffer.readUInt64();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " {\n");
		sb.append("  mColor=" + mColor + "\n");
		sb.append("  mReserved1=" + mReserved1 + "\n");
		sb.append("  mPower=" + mPower + "\n");
		sb.append("  mLabel=" + mLabel + "\n");
		sb.append("  mReserved2=" + mReserved2 + "\n");
		sb.append("}");
		return sb.toString();
	}
}
