package com.github.tort32.api.nodemcu.protocol;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.tort32.api.nodemcu.protocol.RawColor;

public class RawColorTest {

	@Test
	public void HSB2RGB() {
		assertEquals(RawColor.HSB2RGB(0, 100, 100),
				new RawColor.RGB(RawColor.RGB_MAX, 0, 0)); // Red
		assertEquals(RawColor.HSB2RGB(60, 100, 100),
				new RawColor.RGB(RawColor.RGB_MAX, RawColor.RGB_MAX, 0)); // Yellow
		assertEquals(RawColor.HSB2RGB(120, 100, 100),
				new RawColor.RGB(0, RawColor.RGB_MAX, 0)); // Green
		assertEquals(RawColor.HSB2RGB(180, 100, 100),
				new RawColor.RGB(0, RawColor.RGB_MAX, RawColor.RGB_MAX)); // Cyan
		assertEquals(RawColor.HSB2RGB(240, 100, 100),
				new RawColor.RGB(0, 0, RawColor.RGB_MAX)); // Blue
		assertEquals(RawColor.HSB2RGB(300, 100, 100),
				new RawColor.RGB(0, 0, RawColor.RGB_MAX)); // Magenta
		assertEquals(RawColor.HSB2RGB(360, 100, 100),
				new RawColor.RGB(RawColor.RGB_MAX, 0, 0)); // Red 360
		
		for(int hue = 0; hue <= 360; hue += 10) {
			assertEquals(RawColor.HSB2RGB(hue, 100, 0),
					new RawColor.RGB(0, 0, 0)); // Blacks
		}
		
		for(int hue = 0; hue <= 360; hue += 10) {
			assertEquals(RawColor.HSB2RGB(hue, 0, 100),
					new RawColor.RGB(RawColor.RGB_MAX, RawColor.RGB_MAX, RawColor.RGB_MAX)); // Whites
		}
		
		assertEquals(RawColor.HSB2RGB(0, 0, 50),
				new RawColor.RGB(RawColor.RGB_MAX / 2, RawColor.RGB_MAX / 2, RawColor.RGB_MAX / 2)); // White 50 brightness
	}
	
	@Test
	public void HSBK2RGB() {
		final int KELVIN = 6599; // Pure white
		
		assertEquals(RawColor.HSBK2RGB(0, 100, 100, KELVIN),
				new RawColor.RGB(RawColor.RGB_MAX, 0, 0)); // Red
		assertEquals(RawColor.HSBK2RGB(60, 100, 100, KELVIN),
				new RawColor.RGB(RawColor.RGB_MAX, RawColor.RGB_MAX, 0)); // Yellow
		assertEquals(RawColor.HSBK2RGB(120, 100, 100, KELVIN),
				new RawColor.RGB(0, RawColor.RGB_MAX, 0)); // Green
		assertEquals(RawColor.HSBK2RGB(180, 100, 100, KELVIN),
				new RawColor.RGB(0, RawColor.RGB_MAX, RawColor.RGB_MAX)); // Cyan
		assertEquals(RawColor.HSBK2RGB(240, 100, 100, KELVIN),
				new RawColor.RGB(0, 0, RawColor.RGB_MAX)); // Blue
		assertEquals(RawColor.HSBK2RGB(300, 100, 100, KELVIN),
				new RawColor.RGB(0, 0, RawColor.RGB_MAX)); // Magenta
		assertEquals(RawColor.HSBK2RGB(360, 100, 100, KELVIN),
				new RawColor.RGB(RawColor.RGB_MAX, 0, 0)); // Red 360
		
		for(int hue = 0; hue <= 360; hue += 10) {
			assertEquals(RawColor.HSBK2RGB(hue, 100, 0, KELVIN),
					new RawColor.RGB(0, 0, 0)); // Blacks
		}
		
		for(int hue = 0; hue <= 360; hue += 10) {
			assertEquals(RawColor.HSBK2RGB(hue, 0, 100, KELVIN),
					new RawColor.RGB(RawColor.RGB_MAX, RawColor.RGB_MAX, RawColor.RGB_MAX)); // Whites
		}
		
		assertEquals(RawColor.HSBK2RGB(0, 0, 50, KELVIN),
				new RawColor.RGB(RawColor.RGB_MAX / 2, RawColor.RGB_MAX / 2, RawColor.RGB_MAX / 2)); // White 50 brightness
	}

}
