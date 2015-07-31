package com.github.tort32.lifx.protocol;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.xml.bind.DatatypeConverter;

public class Types {
	public static class Int64 {
		protected long data;

		public Int64(long value) {
			data = value;
		}
		
		public void set(long value) {
			data = value;
		}

		public long getLong() {
			return data;
		}

		@Override
		public String toString() {
			return String.format("{%016X}", data);
		}
	}
	
	public static class Int32 extends Int64 {
		
		public Int32(long value) {
			super(value);
		}
		
		public Int32(int value) {
			super(value);
		}
		
		public void set(int value) {
			data = value;
		}
		
		public int getInt() {
			return (int) data;
		}
		
		@Override
		public String toString() {
			return String.format("{%08X}", data);
		}
	}
	
	public static class Int16 extends Int32 {
		
		public Int16(int value) {
			super(value);
		}
		
		public Int16(short value) {
			super(value);
		}
		
		public void set(short value) {
			data = value;
		}
		
		public short getShort() {
			return (short) data;
		}
		
		@Override
		public String toString() {
			return String.format("{%04X}", data);
		}
	}
	
	public static class Int8 extends Int16 {
		
		public Int8(int value) {
			super(value);
		}
		
		public Int8(byte value) {
			super(value);
		}
		
		public void set(byte value) {
			data = value;
		}
		
		public byte getByte() {
			return (byte) data;
		}
		
		@Override
		public String toString() {
			return String.format("{%02X}", data);
		}
	}
	
	public static class UInt32 extends Int32 {
		public UInt32(long value) {
			super(value);
		}
	}
	
	public static class UInt16 extends Int16 {
		
		public static final float MAX_VALUE = 65535;
		
		public UInt16(int value) {
			super(value);
		}
	}
	
	public static class UInt8 extends Int8 {
		public UInt8(int value) {
			super(value);
		}
	}
	
	public static class UInt64 {
		private byte[] data;
		
		public UInt64(long value) {
			data = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array();
		}
		
		public UInt64(BigInteger value) {
			this(value.toByteArray());
		}
		
		public UInt64(byte[] b) {
			data = new byte[8];
			for(int i = 0; i < b.length; ++i) {
				data[i] = b[i];
			}
		}
		
		public UInt64(String hex) {
			data = DatatypeConverter.parseHexBinary(hex);
		}
		
		public void set(byte[] value) {
			data = value;
		}
		
		public BigInteger getBigInteger() {
			return new BigInteger(data);
		}
		
		public String getHexValue() {
			return DatatypeConverter.printHexBinary(data);
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		@Override
		public String toString() {
			return String.format("{%s}", getHexValue());
		}
	}
	
	public static class HSBK {
		public static HSBK Green = new HSBK(120, 100, 100);
		public static HSBK DarkRed = new HSBK(10, 0, 0);

		public UInt16 mHue = new UInt16(0); // 16 bits. Range 0 to 65535. Scaled to between 0° and 360°
		public UInt16 mSaturation = new UInt16(0); // 16 bits. Range 0 to 65535. Scaled to between 0% and 100%
		public UInt16 mBrightness = new UInt16(0); // 16 bits. Range 0 to 65535. Scaled to between 0% and 100%
		public UInt16 mKelvin = new UInt16(3500); // 16 bits. Range 2500° (warm) to 9000° (cool).

		public static final int LENGTH = 8;

		public HSBK(int hue, int saturation, int brigthness)
	    {
	      mHue.set((int) ((hue * UInt16.MAX_VALUE) / 360));
	      mSaturation.set((int) ((saturation * UInt16.MAX_VALUE) / 100));
	      mBrightness.set((int) ((brigthness * UInt16.MAX_VALUE) / 100));
	    }

		public HSBK(int hue, int saturation, int brigthness, int kelvin) {
			this(hue, saturation, brigthness);
			mKelvin.set(kelvin);
		}

		public HSBK(InBuffer buffer) {
			buffer.checkLength(LENGTH);
			mHue = buffer.readUInt16();
			mSaturation = buffer.readUInt16();
			mBrightness = buffer.readUInt16();
			mKelvin = buffer.readUInt16();
		}
		
		public OutBuffer write(OutBuffer buffer) {
			return buffer
					.beginChunk()
					.write(mHue)
					.write(mSaturation)
					.write(mBrightness)
					.write(mKelvin)
					.endChunk(HSBK.LENGTH);
		}
	}
}