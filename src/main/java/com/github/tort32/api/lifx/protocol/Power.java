package com.github.tort32.api.lifx.protocol;

import com.github.tort32.api.lifx.protocol.Types.UInt16;

/**
 * Zero implies standby and non-zero sets a corresponding power draw level.
 * Currently only 0 and 65535 are supported. So mapped to boolean.
 */
public class Power extends UInt16 {

	public Power(boolean on) {
		super();
		set(on);
	}
	
	public Power(byte[] arr) {
		super(arr);
	}
	
	public void set(boolean on) {
		if (on) {
			set(UInt16.MAX_VALUE);
		} else {
			set(0);
		}
	}
	
	public boolean isOn() {
		return (getValue() == UInt16.MAX_VALUE);
	}
	
	@Override
	public String toString() {
		return String.format("{%s}", isOn() ? "on" : "off");
	}
}
 