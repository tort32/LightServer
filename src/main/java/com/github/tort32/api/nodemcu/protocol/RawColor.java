package com.github.tort32.api.nodemcu.protocol;

import com.github.tort32.common.entity.LightColor;

public class RawColor {
	public static final int RGB_MAX = 255;
	public static final int DUTY_MAX = 1023;
	
	public static final RawColor WHITE = new RawColor(DUTY_MAX, DUTY_MAX, DUTY_MAX);
	public static final RawColor BLACK = new RawColor(0, 0, 0);
	
	public int rawR;
	public int rawG;
	public int rawB;
	
	public RawColor() {
		this(DUTY_MAX, DUTY_MAX, DUTY_MAX);
	}

	public RawColor(int hue, int saturation, int brightness, int kelvin) {
		RGB c = HSBK2RGB(hue, saturation, brightness, kelvin);
		rawR = convertToDuty(c.r);
		rawG = convertToDuty(c.g);
		rawB = convertToDuty(c.b);
	}
	
	public RawColor(int rawR, int rawG, int rawB) {
		this.rawR = rawR;
		this.rawG = rawG;
		this.rawB = rawB;
	}
	
	public RawColor(LightColor color) {
		this(color.hue, color.saturation, color.brightness, color.kelvin);
	}
	
	public LightColor toLightColor() {
		int r = convertToIntensity(rawR);
		int g = convertToIntensity(rawG);
		int b = convertToIntensity(rawB);
		HSB c = RGB2HSB(r, g, b);
		return new LightColor(c.h, c.s, c.b, 6600);
	}
	
	private final double a = 2.0; // log factor
	private final double aScale = Math.exp(a) - 1.0;
	
	private int convertToDuty(int intensity) {
		double dBrightness = (double) intensity / (double) RGB_MAX;
		double dDuty = (Math.exp(a * dBrightness) - 1.0) / aScale;
		int duty = (int) ( dDuty * (double) DUTY_MAX);
		return duty; 
	}
	
	private int convertToIntensity(int duty) {
		double dDuty = (double) duty / (double) DUTY_MAX;
		double dIntensity = Math.log(1.0 + aScale * dDuty) / a;
		int intensity = (int) (dIntensity * (double) RGB_MAX);
		return intensity; 
	}
	
	protected static RGB HSB2RGB(int hue, int sat, int br) {
		double r, g, b;
		double t2 = (100.0 - (double) sat) * (double) br / 100.0;
		double t3 = ((double) sat * (double) br / 100.0) * ((double) (hue % 60) / 60.0);
		int i = hue / 60;
		switch (i % 6) {
			default:
			case 0: r = br; b = t2; g = t2 + t3; break;
			case 1: g = br; b = t2; r = br - t3; break;
			case 2: g = br; r = t2; b = t2 + t3; break;
			case 3: b = br; r = t2; g = br - t3; break;
			case 4: b = br; g = t2; r = t2 + t3; break;
			case 5: r = br; g = t2; b = br - t3; break;
		}
		RGB c = new RGB();
		c.r = (int) (r * (double) RGB_MAX / 100.0);
		c.g = (int) (g * (double) RGB_MAX / 100.0);
		c.b = (int) (b * (double) RGB_MAX / 100.0);
	    return c;
	}
	
	protected static HSB RGB2HSB(int r, int g, int b) {
		HSB c = new HSB();
		int cMax = Math.max(Math.max(r, g), b);
		if (cMax == 0) {
			c.s = 0;
			c.h = 0;
			return c;
		}
		int cMin = Math.min(Math.min(r, g), b);
		int delta = cMax - cMin;
		if (r == cMax) {
			c.h = (int) ((double) (g - b) * 60.0 / (double) delta);
		} else if (g == cMax) {
			c.h = (int) ((double) (b - r) * 60.0 / (double) delta) + 120;
		} else {
			c.h = (int) ((double) (r - g) * 60.0 / (double) delta) + 240;
		}
		if( c.h < 0 ) {
			c.h += 360;
		}
		c.s = (int) ((double) delta * 100.0 / (double) cMax);
		c.b = (int) ((double) cMax * 100.0 / (double) RGB_MAX);
		return c;
	}

	protected static RGB K2RGB(int kelvin) {
		RGB c = new RGB();
	    if (kelvin < 6600) {
	        c.r = RGB_MAX;
			c.g = (int) (100.0 * Math.log((double) kelvin) - 620.0);
			c.b = (int) (200.0 * Math.log((double) kelvin) - 1500.0);
		} else {
	        c.r = (int) (480.0 * Math.pow((double) kelvin - 6000.0, -0.1));
			c.g = (int) (400.0 * Math.pow((double) kelvin - 6000.0, -0.07));
			c.b = RGB_MAX;
	    }
		c.r = Math.min(Math.max(c.r, 0), RGB_MAX);
		c.g = Math.min(Math.max(c.g, 0), RGB_MAX);
		c.b = Math.min(Math.max(c.b, 0), RGB_MAX);
	    return c;
	}
	
	protected static RGB HSBK2RGB(int hue, int sat, int br, int k) {
		RGB c = HSB2RGB(hue, sat, br);
		RGB w = K2RGB(k);
		double a = (double) sat / 100.0;
		double b = (1.0 - a) / (double) RGB_MAX;
		// Color components weighted by saturation
		// Saturation = 100 is not impacted by color temperature
		c.r = (int) ((double) c.r * (a + (double) w.r * b));
		c.g = (int) ((double) c.g * (a + (double) w.g * b));
		c.b = (int) ((double) c.b * (a + (double) w.b * b));
		return c;
	}
	
	protected static class RGB {
		public int r;
		public int g;
		public int b;
		
		public RGB() {
			this(RGB_MAX, RGB_MAX, RGB_MAX);
		}
		
		public RGB(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		@Override
		public int hashCode() {
			return (b << 16) + (g << 8) + r;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj == null || getClass() != obj.getClass()) {
				return false;
			} else {
				RGB other = (RGB) obj;
				return (b == other.b && g == other.g && g == other.g);
			}
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(RGB.class.getSimpleName());
			sb.append("(").append(r).append(", ").append(g).append(", ").append(b).append(")");
			return sb.toString();
		}
	}
	
	protected static class HSB {
		public int h;
		public int s;
		public int b;
		
		public HSB() {
			this(0, 100, 100);
		}
		
		public HSB(int h, int s, int b) {
			this.h = h;
			this.s = s;
			this.b = b;
		}
		
		@Override
		public int hashCode() {
			return (h << 16) + (s << 8) + b;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj == null || getClass() != obj.getClass()) {
				return false;
			} else {
				HSB other = (HSB) obj;
				return (h == other.h && s == other.s && b == other.b);
			}
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(HSB.class.getSimpleName());
			sb.append("(").append(h).append(", ").append(s).append(", ").append(b).append(")");
			return sb.toString();
		}
	}
}
