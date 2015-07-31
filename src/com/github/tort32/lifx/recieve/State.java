package com.github.tort32.lifx.recieve;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.Types.HSBK;
import com.github.tort32.lifx.protocol.Types.Int16;
import com.github.tort32.lifx.protocol.Types.UInt16;
import com.github.tort32.lifx.protocol.Types.UInt64;
import com.github.tort32.lifx.protocol.message.Payload;

public class State extends Payload {
	public static final int ID = 107;
	public static final int LENGTH = HSBK.LENGTH + 44; // 52

	public HSBK mColor; // HSBK. Color
	public Int16 mReserved1; //	16 bit. Reserved
	public UInt16 mPower; // 16 bit
	public char[] mLabel; // 32 bytes. String
	public UInt64 mReserved2; // 64 bit. Reserved

	public State(InBuffer buffer) {
		super(ID, LENGTH);
		buffer.checkLength(LENGTH);
		mColor = buffer.readHSBK();
		mReserved1 = buffer.readInt16();
		mPower = buffer.readUInt16();
		mLabel = buffer.readString(32);
		mReserved2 = buffer.readUInt64();
	}
}
