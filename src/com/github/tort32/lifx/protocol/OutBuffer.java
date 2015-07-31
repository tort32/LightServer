package com.github.tort32.lifx.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.github.tort32.lifx.protocol.Types.HSBK;
import com.github.tort32.lifx.protocol.Types.Int16;
import com.github.tort32.lifx.protocol.Types.Int32;
import com.github.tort32.lifx.protocol.Types.Int64;
import com.github.tort32.lifx.protocol.Types.Int8;
import com.github.tort32.lifx.protocol.Types.UInt16;
import com.github.tort32.lifx.protocol.Types.UInt32;
import com.github.tort32.lifx.protocol.Types.UInt64;
import com.github.tort32.lifx.protocol.Types.UInt8;

public class OutBuffer
{
	private List<Byte> buffer = new ArrayList<Byte>();
	private Stack<Integer> chunkOffset = new Stack<Integer>();

	public OutBuffer beginChunk() {
		chunkOffset.push(buffer.size());
		return this;
	}

	public OutBuffer endChunk(int length) {
		int offset = chunkOffset.pop();
		int size = buffer.size() - offset;
		if(size != length) {
			throw new IllegalStateException("Invalid size writen");
		}
		return this;
	}

	public OutBuffer write(byte value) {
		buffer.add(value);
		return this;
	}

	public OutBuffer write(byte[] array) {
		for(int i = 0; i < array.length; ++i) {
			buffer.add(array[i]);
		}
		return this;
	}
	
	public OutBuffer write(Int8 value) {
		//byte[] b = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN).put(value.getByte()).array();
		write(value.getByte());
		return this;
	}
	
	public OutBuffer write(Int16 value) {
		byte[] b = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value.getShort()).array();
		write(b);
		return this;
	}
	
	public OutBuffer write(Int32 value) {
		byte[] b = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value.getInt()).array();
		write(b);
		return this;
	}
	
	public OutBuffer write(Int64 value) {
		byte[] b = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value.getLong()).array();
		write(b);
		return this;
	}
	
	public OutBuffer write(UInt8 value) {
		//byte[] b = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN).put(value.getByte()).array();
		byte byteValue = (byte) (value.getByte() & 0xFF);
		write(byteValue);
		return this;
	}
	
	public OutBuffer write(UInt16 value) {
		short shortValue = (short) (value.getLong() & 0xFFFFL);
		byte[] b = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(shortValue).array();
		write(b);
		return this;
	}
	
	public OutBuffer write(UInt32 value) {
		int intValue = (int) (value.getLong() & 0xFFFFFFFFL);
		byte[] b = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(intValue).array();
		write(b);
		return this;
	}
	
	public OutBuffer write(UInt64 value) {
		byte[] b = value.getBytes();
		write(b);
		return this;
	}

	public OutBuffer write(char[] value) {
		for(int i = 0; i < value.length; ++i) {
			byte b = (byte) value[i];
			write(b);
		}
		return this;
	}
	
	public OutBuffer write(HSBK value) {
		value.write(this);
		return this;
	}

	public byte[] toArray() {
		byte[] arr = new byte[buffer.size()];
		for(int i = 0; i < arr.length; ++i) {
			arr[i] = buffer.get(i);
		}
		return arr;
	}
}
