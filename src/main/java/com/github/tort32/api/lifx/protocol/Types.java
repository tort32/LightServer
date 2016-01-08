package com.github.tort32.api.lifx.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.xml.bind.DatatypeConverter;

public class Types {
	
	public static class Int8 {
		public static final int LENGTH = 1;
		protected byte[] data;
		
		public Int8(byte value) {
			set(value);
		}
		
		public Int8(int value) {
			set((byte) value);
		}
		
		public Int8(byte[] arr) {
			set(arr);
		}
		
		public void set(byte value) {
			data = new byte[] { value };
		}
		
		public void set(byte[] arr) {
			if (arr.length != LENGTH) {
				throw new IllegalArgumentException("Int8 store 1 byte");
			}
			data = arr;
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public byte getValue() {
			return data[0];
		}
		
		@Override
		public String toString() {
			return String.format("{%d}", getValue());
		}
	}
	
	public static class Int16 {
		public static final int LENGTH = 2;
		protected byte[] data;
		
		public Int16(short value) {
			set(value);
		}
		
		public Int16(int value) {
			set((short) value);
		}
		
		public Int16(byte[] arr) {
			set(arr);
		}
		
		public void set(short value) {
			data = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
		}
		
		public void set(byte[] arr) {
			if (arr.length != LENGTH) {
				throw new IllegalArgumentException("Int16 store 2 bytes");
			}
			data = arr;
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public short getValue() {
			return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
		}
		
		@Override
		public String toString() {
			return String.format("{%d}", getValue());
		}
	}
	
	public static class Int32 {
		public static final int LENGTH = 4;
		protected byte[] data;
		
		public Int32(int value) {
			set(value);
		}
		
		public Int32(byte[] arr) {
			set(arr);
		}
		
		public void set(int value) {
			data = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
		}
		
		public void set(byte[] arr) {
			if (arr.length != LENGTH) {
				throw new IllegalArgumentException("Int32 store 4 bytes");
			}
			data = arr;
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public int getValue() {
			return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt();
		}
		
		@Override
		public String toString() {
			return String.format("{%d}", getValue());
		}
	}
	
	public static class Int64 {
		public static final int LENGTH = 8;
		protected byte[] data;
		
		public Int64(long value) {
			set(value);
		}
		
		public Int64(byte[] arr) {
			set(arr);
		}
		
		public void set(long value) {
			data = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array();
		}
		
		public void set(byte[] arr) {
			if (arr.length != LENGTH) {
				throw new IllegalArgumentException("Int64 store 8 bytes");
			}
			data = arr;
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public long getValue() {
			return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getLong();
		}
		
		@Override
		public String toString() {
			return String.format("{%d}", getValue());
		}
	}
	
	public static class UInt8 {
		public static final int LENGTH = 1;
		protected byte[] data;
		
		public static final int MAX_VALUE = 255;
		
		public UInt8(int value) {
			set(value);
		}
		
		public UInt8(String hex) {
			set(hex);
		}
		
		public UInt8(byte[] arr) {
			set(arr);
		}
		
		public void set(int value) {
			byte ub = (byte) (value & 0xFF);
			data = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN).put(ub).array();
		}
		
		public void set(byte[] arr) {
			if (arr.length != LENGTH) {
				throw new IllegalArgumentException("UInt8 store 1 byte");
			}
			data = arr;
		}
		
		public void set(String hex) {
			byte[] arr = DatatypeConverter.parseHexBinary(hex);
			set(arr);
		}
		
		public String getHexValue() {
			return DatatypeConverter.printHexBinary(data);
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public int getValue() {
			byte byteValue = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).get();
			return byteValue & 0xFF;
		}
		
		@Override
		public String toString() {
			return String.format("{%02X}", getValue());
		}
	}
	
	public static class UInt16 {
		public static final int LENGTH = 2;
		protected byte[] data; // Little endian
		
		public static final int MAX_VALUE = 65535;
		
		protected UInt16() {
			// Empty
		}
		
		public UInt16(int value) {
			set(value);
		}
		
		public UInt16(String hex) {
			set(hex);
		}
		
		public UInt16(byte[] arr) {
			set(arr);
		}
		
		public void set(int value) {
			short us = (short) (value & 0xFFFF);
			data = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN).putShort(us).array();
		}
		
		public void set(byte[] arr) {
			if (arr.length != LENGTH) {
				throw new IllegalArgumentException("UInt16 store 2 bytes");
			}
			data = arr;
		}
		
		public void set(String hex) {
			byte[] bigEndian = DatatypeConverter.parseHexBinary(hex);
			set(reverseBytes(bigEndian));
		}
		
		public String getHexValue() {
			byte[] bigEndian = reverseBytes(data);
			return DatatypeConverter.printHexBinary(bigEndian);
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public int getValue() {
			short shortValue = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
			return shortValue & 0xFFFF;
		}
		
		@Override
		public String toString() {
			return String.format("{%04X}", getValue());
		}
	}
	
	public static class UInt32 {
		public static final int LENGTH = 4;
		protected byte[] data; // Little endian
		
		public static final long MAX_VALUE = 4294967295L;
		
		public UInt32(long value) {
			set(value);
		}
		
		public UInt32(String hex) {
			set(hex);
		}
		
		public UInt32(byte[] arr) {
			set(arr);
		}
		
		public void set(long value) {
			int ui = (int) (value & 0xFFFFFFFFL);
			data = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN).putInt(ui).array();
		}
		
		public void set(byte[] arr) {
			if (arr.length != LENGTH) {
				throw new IllegalArgumentException("UInt32 store 4 bytes");
			}
			data = arr;
		}
		
		public void set(String hex) {
			byte[] bigEndian = DatatypeConverter.parseHexBinary(hex);
			set(reverseBytes(bigEndian));
		}
		
		public String getHexValue() {
			byte[] bigEndian = reverseBytes(data);
			return DatatypeConverter.printHexBinary(bigEndian);
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public long getValue() {
			int intValue = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt();
			return intValue & 0xFFFFFFFFL;
		}
		
		@Override
		public String toString() {
			return String.format("{%08X}", getValue());
		}
	}
	
	public static class UInt64 {
		public static final int LENGTH = 8;
		protected byte[] data; // Little endian
		
		public UInt64(long high, long low) {
			set(high, low);
		}
		
		public UInt64(byte[] arr) {
			set(arr);
		}
		
		public UInt64(String hex) {
			set(hex);
		}
		
		public void set(long high, long low) {
			int hiValue = (int) (high & 0xFFFFFFFFL);
			int loValue = (int) (low & 0xFFFFFFFFL);
			data = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN).putInt(loValue).putInt(hiValue).array();
		}
		
		public void set(byte[] arr) {
			if (arr.length != LENGTH) {
				throw new IllegalArgumentException("UInt64 store 8 bytes");
			}
			data = arr;
		}
		
		public void set(String hex) {
			byte[] bigEndian = DatatypeConverter.parseHexBinary(hex);
			byte[] littleEndian = reverseBytes(bigEndian);
			set(littleEndian);
		}
		
		public String getHexValue() {
			byte[] bigEndian = reverseBytes(data);
			return DatatypeConverter.printHexBinary(bigEndian);
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public long getLowValue() {
			long intValue = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt();
			return (intValue & 0xFFFFFFFFL);
		}
		
		public long getHighValue() {
			ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
			buffer.position(LENGTH / 2);
			long intValue = buffer.getInt();
			return (intValue & 0xFFFFFFFFL);
		}
		
		@Override
		public String toString() {
			return String.format("{%s}", getHexValue());
		}
	}
	
	public static class CharString {
		protected byte[] data;
		
		public CharString(int size) {
			data = new byte[size];
		}
		
		public CharString(byte[] arr) {
			data = arr;
		}
		
		public void set(String str) {
			for(int i = 0; i < data.length; ++i) {
				data[i] = (i < str.length()) ? (byte) str.charAt(i) : 0;
			}
		}
		
		public String getValue() {
			StringBuilder sb = new StringBuilder(data.length);
			for(int i = 0; i < data.length; ++i) {
				char ch = (char) (data[i] & 0xFF);
				sb.append(ch);
			}
			return sb.toString();
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		@Override
		public String toString() {
			return String.format("{%s}", getValue());
		}
	}
	
	public static class Float32 {
		protected byte[] data;
		
		public Float32(float value) {
			set(value);
		}
		
		public Float32(byte[] arr) {
			set(arr);
		}
		
		public void set(float value) {
			data = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();
		}
		
		public void set(byte[] arr) {
			if (arr.length != 4) {
				throw new IllegalArgumentException("Float32 store 4 bytes");
			}
			data = arr;
		}
		
		public byte[] getBytes() {
			return data;
		}
		
		public float getValue() {
			return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		}
		
		@Override
		public String toString() {
			return String.format("{%e}", getValue());
		}
	}
	
	public static byte[] reverseBytes(byte[] b) {
		byte[] reversed = new byte[b.length];
		for(int i = 0; i < b.length; ++i) {
			reversed[i] = b[b.length - 1 - i];
		}
		return reversed;
	}
	
	public static String dumpBytes(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for(byte b : a) {
			sb.append(String.format("%02X ", b));
		}
		return sb.toString();
	}
}