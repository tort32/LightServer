package com.github.tort32.api.lifx.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Stack;

import com.github.tort32.api.lifx.protocol.Types.*;

public class OutBuffer
{
	private ByteBuffer buffer;
	private Stack<Integer> chunkOffset = new Stack<Integer>();
	
	public OutBuffer(int size) {
		buffer = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
	}

	public OutBuffer beginChunk() {
		chunkOffset.push(buffer.position());
		return this;
	}

	public OutBuffer endChunk(int length) {
		int offset = chunkOffset.pop();
		int size = buffer.position() - offset;
		if(size != length) {
			throw new IllegalStateException("Invalid size writen");
		}
		return this;
	}

	public OutBuffer write(byte value) {
		buffer.put(value);
		return this;
	}

	public OutBuffer write(byte[] array) {
		buffer.put(array);
		return this;
	}
	
	public OutBuffer write(Int8 value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(Int16 value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(Int32 value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(Int64 value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(UInt8 value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(UInt16 value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(UInt32 value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(UInt64 value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(CharString value) {
		return write(value.getBytes());
	}
	
	public OutBuffer write(HSBK value) {
		return value.write(this);
	}

	public byte[] toArray() {
		return buffer.array();
	}
}
