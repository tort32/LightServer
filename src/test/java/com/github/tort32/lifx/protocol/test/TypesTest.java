package com.github.tort32.lifx.protocol.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.tort32.lifx.protocol.MacAddress;
import com.github.tort32.lifx.protocol.Types.*;

public class TypesTest {

	@Test
	public void testLimitsInt8() {
		int maxValue = Byte.MAX_VALUE;
		int minValue = Byte.MIN_VALUE;
		assertEquals("Int8 max store failed", maxValue, new Int8(maxValue).getValue());
		assertEquals("Int8 min store failed", minValue, new Int8(minValue).getValue());
	}
	
	@Test
	public void testOrderInt8() {
		Int8 data = new Int8(0x12);
		assertEquals("Int8 store failed", 0x12, data.getValue());
		
		byte[] b = data.getBytes();
		assertEquals("Int8 store failed", 0x12, b[0] & 0xFF);
	}
	
	@Test
	public void testLimitsUInt8() {
		int maxValue = UInt8.MAX_VALUE;
		int minValue = 0;
		assertEquals("UInt8 max store failed", maxValue, new UInt8(maxValue).getValue());
		assertEquals("UInt8 max store failed", "FF", new UInt8(maxValue).getHexValue());
		assertEquals("UInt8 min store failed", minValue, new UInt8(minValue).getValue());
		assertEquals("UInt8 min store failed", "00", new UInt8(minValue).getHexValue());
	}
	
	@Test
	public void testOrderUInt8() {
		UInt8 data = new UInt8(0xFE);
		assertEquals("UInt8 order failed", 0xFE, data.getValue());
		assertEquals("UInt8 order failed", "FE", data.getHexValue());
		
		byte[] b = data.getBytes();
		assertEquals("UInt8 order failed", 0xFE, b[0] & 0xFF);
	}
	
	@Test
	public void testLimitsInt16() {
		int maxValue = Short.MAX_VALUE;
		int minValue = Short.MIN_VALUE;
		assertEquals("Int16 max store failed", maxValue, new Int16(maxValue).getValue());
		assertEquals("Int16 min store failed", minValue, new Int16(minValue).getValue());
	}
	
	@Test
	public void testOrderInt16() {
		Int16 data = new Int16(0x1234);
		assertEquals("Int16 store failed", 0x1234, data.getValue());
		
		byte[] b = data.getBytes();
		assertEquals("Int16 store failed", 0x34, b[0] & 0xFF);
		assertEquals("Int16 store failed", 0x12, b[1] & 0xFF);
	}
	
	@Test
	public void testLimitsUInt16() {
		int maxValue = UInt16.MAX_VALUE;
		int minValue = 0;
		assertEquals("UInt16 max store failed", maxValue, new UInt16(maxValue).getValue());
		assertEquals("UInt16 max store failed", "FFFF", new UInt16(maxValue).getHexValue());
		assertEquals("UInt16 min store failed", minValue, new UInt16(minValue).getValue());
		assertEquals("UInt16 min store failed", "0000", new UInt16(minValue).getHexValue());
	}
	
	@Test
	public void testOrderUInt16() {
		UInt16 data = new UInt16(0xFEDC);
		assertEquals("UInt16 order failed", 0xFEDC, data.getValue());
		assertEquals("UInt16 order failed", "FEDC", data.getHexValue());
		
		byte[] b = data.getBytes();
		assertEquals("UInt16 order failed", 0xDC, b[0] & 0xFF);
		assertEquals("UInt16 order failed", 0xFE, b[1] & 0xFF);
	}
	
	@Test
	public void testLimitsInt32() {
		int maxValue = Integer.MAX_VALUE;
		int minValue = Integer.MIN_VALUE;
		assertEquals("Int32 max store failed", maxValue, new Int32(maxValue).getValue());
		assertEquals("Int32 min store failed", minValue, new Int32(minValue).getValue());
	}
	
	@Test
	public void testOrderInt32() {
		Int32 data = new Int32(0x12345678);
		assertEquals("Int32 store failed", 0x12345678, data.getValue());
		
		byte[] b = data.getBytes();
		assertEquals("Int32 store failed", 0x78, b[0] & 0xFF);
		assertEquals("Int32 store failed", 0x56, b[1] & 0xFF);
		assertEquals("Int32 store failed", 0x34, b[2] & 0xFF);
		assertEquals("Int32 store failed", 0x12, b[3] & 0xFF);
	}
	
	@Test
	public void testLimitsUInt32() {
		long maxValue = UInt32.MAX_VALUE;
		long minValue = 0;
		assertEquals("UInt32 max store failed", maxValue, new UInt32(maxValue).getValue());
		assertEquals("UInt32 max store failed", "FFFFFFFF", new UInt32(maxValue).getHexValue());
		assertEquals("UInt32 min store failed", minValue, new UInt32(minValue).getValue());
		assertEquals("UInt32 min store failed", "00000000", new UInt32(minValue).getHexValue());
	}
	
	@Test
	public void testOrderUInt32() {
		UInt32 data = new UInt32(0xFEDCBA09L);
		assertEquals("UInt32 order failed", 0xFEDCBA09L, data.getValue());
		assertEquals("UInt32 order failed", "FEDCBA09", data.getHexValue());
		
		byte[] b = data.getBytes();
		assertEquals("UInt32 order failed", 0x09, b[0] & 0xFF);
		assertEquals("UInt32 order failed", 0xBA, b[1] & 0xFF);
		assertEquals("UInt32 order failed", 0xDC, b[2] & 0xFF);
		assertEquals("UInt32 order failed", 0xFE, b[3] & 0xFF);
	}
	
	@Test
	public void testLimitsInt64() {
		long maxValue = Long.MAX_VALUE;
		long minValue = Long.MIN_VALUE;
		assertEquals("Int64 max store failed", maxValue, new Int64(maxValue).getValue());
		assertEquals("Int64 min store failed", minValue, new Int64(minValue).getValue());
	}
	
	@Test
	public void testOrderInt64() {
		Int64 data = new Int64(0x1234567890ABCDEFL);
		assertEquals("Int64 store failed", 0x1234567890ABCDEFL, data.getValue());
		
		byte[] b = data.getBytes();
		assertEquals("Int64 store failed", 0xEF, b[0] & 0xFF);
		assertEquals("Int64 store failed", 0xCD, b[1] & 0xFF);
		assertEquals("Int64 store failed", 0xAB, b[2] & 0xFF);
		assertEquals("Int64 store failed", 0x90, b[3] & 0xFF);
		assertEquals("Int64 store failed", 0x78, b[4] & 0xFF);
		assertEquals("Int64 store failed", 0x56, b[5] & 0xFF);
		assertEquals("Int64 store failed", 0x34, b[6] & 0xFF);
		assertEquals("Int64 store failed", 0x12, b[7] & 0xFF);
	}
	
	@Test
	public void testLimitsUInt64() {
		long[] maxValue = {0xFFFFFFFFL, 0xFFFFFFFFL};
		long[] minValue = {0x00000000L, 0x00000000L};
		UInt64 value = new UInt64(0, 0);
		
		value.set(maxValue[0], maxValue[1]);
		assertEquals("UInt64 max store failed", maxValue[0], value.getHighValue());
		assertEquals("UInt64 max store failed", maxValue[1], value.getLowValue());
		assertEquals("UInt64 max store failed", "FFFFFFFF" + "FFFFFFFF", value.getHexValue());
		
		value.set(minValue[0], minValue[1]);
		assertEquals("UInt64 min store failed", minValue[0], value.getHighValue());
		assertEquals("UInt64 min store failed", minValue[1], value.getLowValue());
		assertEquals("UInt64 min store failed", "00000000" + "00000000", value.getHexValue());
	}
	
	@Test
	public void testOrderUInt64() {
		UInt64 data = new UInt64(0xFEDCBA09L, 0x87654321L);
		assertEquals("UInt64 order failed", 0xFEDCBA09L, data.getHighValue());
		assertEquals("UInt64 order failed", 0x87654321L, data.getLowValue());
		assertEquals("UInt64 order failed", "FEDCBA09" + "87654321", data.getHexValue());
		
		byte[] b = data.getBytes();
		assertEquals("UInt64 order failed", 0x21, b[0] & 0xFF);
		assertEquals("UInt64 order failed", 0x43, b[1] & 0xFF);
		assertEquals("UInt64 order failed", 0x65, b[2] & 0xFF);
		assertEquals("UInt64 order failed", 0x87, b[3] & 0xFF);
		assertEquals("UInt64 order failed", 0x09, b[4] & 0xFF);
		assertEquals("UInt64 order failed", 0xBA, b[5] & 0xFF);
		assertEquals("UInt64 order failed", 0xDC, b[6] & 0xFF);
		assertEquals("UInt64 order failed", 0xFE, b[7] & 0xFF);
	}
	
	@Test
	public void testMacAddress() {
		MacAddress mac = new MacAddress(new UInt64("0000274903D573D0").getBytes());
		String macStr = mac.getHexValue();
		assertEquals("Mac address store failed", "D073D5034927", macStr);
		
		mac.set("D073D5034927");
		UInt64 value = new UInt64(mac.getBytes());
		assertEquals("Mac address store failed", "0000274903D573D0", value.getHexValue());
		
	}

}
