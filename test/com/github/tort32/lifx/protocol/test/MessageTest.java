package com.github.tort32.lifx.protocol.test;

import static org.junit.Assert.*;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import com.github.tort32.lifx.device.send.GetService;
import com.github.tort32.lifx.light.recieve.State;
import com.github.tort32.lifx.protocol.message.Message;

public class MessageTest {

	@Test
	public void testDiscoverPacket() {
		
		String packerStr =
				"2400003455AAADBA" + "0000000000000000" +
				"0000000000000100" + "0000000000000000" +
				"02000000";
		byte[] packerSrc = DatatypeConverter.parseHexBinary(packerStr);
		
		Message msg = new Message(new GetService());
		msg.setBroadcast();
		msg.setSource(0xBAADAA55L);
		byte[] discover = msg.toArray();
		
		assertArrayEquals("Packet serealization failed", packerSrc, discover);
	}
	
	@Test
	public void testStatePacket() {
		
		String packerStr =
				"58000054BEBAFEC0" + "D073D50349270000" +
				"4C49465856320000" + "441C19B2F397F713" +
				"6B0000000000FFFF" + "FF7FAC0D0000FFFF" +
				"48656C6C6F20776F" + "726C642100000000" +
				"0000000000000000" + "0000000000000000" +
				"0000000000000000";
		
		byte[] packerSrc = DatatypeConverter.parseHexBinary(packerStr);
		Message msg = new Message(packerSrc);
		
		assertNotNull(msg);
		assertEquals("Invalid frame size value", 0x58, msg.mFrame.mSize.getValue());
		assertEquals("Invalid frame origin value", 0x01, msg.mFrame.mOrigin.getValue());
		assertEquals("Invalid frame protocol value", 1024, msg.mFrame.mProtocol.getValue());
		assertEquals("Invalid frame source value", 0xC0FEBABEL, msg.mFrame.mSource.getValue());
		
		long target = (msg.mFrameAddress.mTarget.getHighValue() << 32L) | msg.mFrameAddress.mTarget.getLowValue(); // Combine UInt64 value as long
		assertEquals("Invalid frame address target value", 0x274903D573D0L, target);
		
		assertNotNull(msg.mPayload);
		assertEquals("Invalid payload type", State.class, msg.mPayload.getClass());
		State state = (State) msg.mPayload;
		String label = state.mLabel.getValue();
		assertTrue("Invalid label = {" + label + "}", label.startsWith("Hello world!"));
		assertEquals("Invalid hue value", 0, state.mColor.mHue.getValue());
		assertEquals("Invalid kelvin value", 3500, state.mColor.mKelvin.getValue());
		assertEquals("Invalid power value", 0xFFFF, state.mPower.getValue());
	}
	
}
