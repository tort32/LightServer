package com.github.tort32.api.lifx.protocol;

import javax.xml.bind.DatatypeConverter;

import com.github.tort32.api.lifx.protocol.Types.UInt64;

/**
 * Represent MAC as 6 high bytes from UInt64. Two low bytes is zero.
 */
public class MacAddress extends UInt64 {
	
	public MacAddress() {
		super(0, 0);
	}
	
	public MacAddress(byte[] arr) {
		super(arr);
	}
	
	@Override
	public void set(String mac) {
		super.set(DatatypeConverter.parseHexBinary(mac + "0000"));
	}
	
	@Override
	public String getHexValue() {
		String hex = DatatypeConverter.printHexBinary(data);
		return hex.substring(0, 12);
	}

}
