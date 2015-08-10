package com.github.tort32.lifx.protocol;

import java.nio.ByteBuffer;

import com.github.tort32.lifx.protocol.Types.*;

public class InBuffer
{
	private ByteBuffer buffer;

	public InBuffer(byte[] buffer) {
		this.buffer = ByteBuffer.wrap(buffer);
	}

	public byte readByte() {
		return buffer.get();
	}

	public byte[] readBytes(int length) {
		byte[] arr = new byte[length];
		buffer.get(arr);
		return arr;
	}
	
	public Int8 readInt8() {
		return new Int8(readBytes(1));
	}
	
	public UInt8 readUInt8() {
		return new UInt8(readBytes(1));
	}
	
	public Int16 readInt16() {
		return new Int16(readBytes(2));
	}

	public UInt16 readUInt16() {
		return new UInt16(readBytes(2));
	}
	
	public Int32 readInt32() {
		return new Int32(readBytes(4));
	}

	public UInt32 readUInt32() {
		return new UInt32(readBytes(4));
	}
	
	public Int64 readInt64() {
		return new Int64(readBytes(8));
	}

	public UInt64 readUInt64() {
		return new UInt64(readBytes(8));
	}
	
	public MacAddress readMacAddress() {
		return new MacAddress(readBytes(8));
	}
	
	public Power readPower() {
		return new Power(readBytes(2));
	}

	public CharString readString(int length) {
		return new CharString(readBytes(length));
	}
	
	public Float32 readFloat32() {
		return new Float32(readBytes(4));
	}
	
	public HSBK readHSBK() {
		return new HSBK(this);
	}

	public void checkLength(int length) {
		if(buffer.capacity() - buffer.position() < length) {
			throw new IllegalStateException("OutBuffer is too short");
		}
	}
}