package com.github.tort32.lifx.protocol;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.github.tort32.lifx.protocol.Types.HSBK;
import com.github.tort32.lifx.protocol.Types.Int16;
import com.github.tort32.lifx.protocol.Types.Int32;
import com.github.tort32.lifx.protocol.Types.Int64;
import com.github.tort32.lifx.protocol.Types.Int8;
import com.github.tort32.lifx.protocol.Types.UInt16;
import com.github.tort32.lifx.protocol.Types.UInt32;
import com.github.tort32.lifx.protocol.Types.UInt64;
import com.github.tort32.lifx.protocol.Types.UInt8;

public class InBuffer
{
	private byte[] buffer;
	private int pos;

	public InBuffer(byte[] buffer) {
		this.buffer = buffer;
		pos = 0;
	}

	public byte readByte() {
		return buffer[pos++];
	}

	public byte[] readBytes(int length) {
		byte[] value = java.util.Arrays.copyOfRange(buffer, pos, pos + length);
		pos += length;
		return value;
	}
	
	public Int8 readInt8() {
		byte b = readByte();
		byte value = b;
		return new Int8(value);
	}
	
	public UInt8 readUInt8() {
		byte b = readByte();
		int value = b & 0xFF; // unsigned
		return new UInt8(value);
	}
	
	public Int16 readInt16() {
		byte[] b = readBytes(2);
		short value = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
		return new Int16(value);
	}

	public UInt16 readUInt16() {
		byte[] b = readBytes(2);
		int value = b[1] & 0xFF;
		value = (value << 8) | (b[0] & 0xFF);
		return new UInt16(value);
	}
	
	public Int32 readInt32() {
		byte[] b = readBytes(4);
		int value = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
		return new Int32(value);
	}

	public UInt32 readUInt32() {
		byte[] b = readBytes(4);
		long value = b[3] & 0xFFL;
		value = (value << 8L) | (b[2] & 0xFFL);
		value = (value << 8L) | (b[1] & 0xFFL);
		value = (value << 8L) | (b[0] & 0xFFL);
		return new UInt32(value);
	}
	
	public Int64 readInt64() {
		byte[] b = readBytes(8);
		long value = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getLong();
		return new Int64(value);
	}

	public UInt64 readUInt64() {
		byte[] b = readBytes(8);
		long longValue = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getLong();
		BigInteger value = BigInteger.valueOf(longValue & ~Long.MIN_VALUE);
		if (longValue < 0) value = value.setBit(63);
		return new UInt64(value);
	}

	public char[] readString(int length) {
		byte[] b = readBytes(length);
		char[] value = new char[length];
		for(int i = 0; i < length; ++i) {
			value[i] = (char) (b[i] & 0xFF);
		}
		return value;
	}
	
	public HSBK readHSBK() {
		return new HSBK(this);
	}

	public void checkLength(int length) {
		if(buffer.length - pos < length) {
			throw new IllegalStateException("OutBuffer is too short");
		}
	}
}