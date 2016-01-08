package com.github.tort32.api.lifx.protocol;

import com.github.tort32.api.lifx.protocol.Types.UInt16;


public class HSBK {
	public static final int HUE_MAX = 360;
	public static final int SATURATION_MAX = 100;
	public static final int BRIGHTNESS_MAX = 100;
	public static final int KELVIN_MIN = 2500;
	public static final int KELVIN_DEFAULT = 3500;
	public static final int KELVIN_MAX = 9000;
	
	public static HSBK Green = new HSBK(120, 100, 100);
	public static HSBK DarkGreen = new HSBK(120, 100, 10);
	public static HSBK Red = new HSBK(0, 100, 50);
	public static HSBK DarkRed = new HSBK(0, 100, 10);

	public UInt16 mHue = new UInt16(0); // 16 bits. Range 0 to 65535. Scaled to between 0° and 360°
	public UInt16 mSaturation = new UInt16(0); // 16 bits. Range 0 to 65535. Scaled to between 0% and 100%
	public UInt16 mBrightness = new UInt16(0); // 16 bits. Range 0 to 65535. Scaled to between 0% and 100%
	public UInt16 mKelvin = new UInt16(KELVIN_DEFAULT); // 16 bits. Range 2500° (warm) to 9000° (cool).

	public static final int LENGTH = 8;
	
	public HSBK() {
		// Empty
	}
	
	public HSBK(HSBK value) {
		this.mHue.set(value.mHue.getValue());
		this.mSaturation.set(value.mSaturation.getValue());
		this.mBrightness.set(value.mBrightness.getValue());
		this.mKelvin.set(value.mKelvin.getValue());
	}

	public HSBK(int hue, int saturation, int brigthness)
    {
		setHue(hue);
		setSaturation(saturation);
		setBrightness(brigthness);
    }

	public HSBK(int hue, int saturation, int brigthness, int kelvin) {
		this(hue, saturation, brigthness);
		setKelvin(kelvin);
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
	
	public void setHue(int hue) {
		mHue.set((int) ((hue * UInt16.MAX_VALUE) / 360));
	}
	
	public void setSaturation(int saturation) {
		mSaturation.set((int) ((saturation * UInt16.MAX_VALUE) / 100));
	}
	
	public void setBrightness(int brigthness) {
		mBrightness.set((int) ((brigthness * UInt16.MAX_VALUE) / 100));
	}
	
	public void setKelvin(int kelvin) {
		mKelvin.set(kelvin);
	}
	
	public int getHue() {
		return (int) ((mHue.getValue() * 360 ) / UInt16.MAX_VALUE);
	}
	
	public int getSaturation() {
		return (int) ((mSaturation.getValue() * 100) / UInt16.MAX_VALUE);
	}
	
	public int getBrightness() {
		return (int) ((mBrightness.getValue() * 100) / UInt16.MAX_VALUE);
	}
	
	public int getKelvin() {
		return mKelvin.getValue();
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" {");
		sb.append(getHue()).append(", ");
		sb.append(getSaturation()).append(", ");
		sb.append(getBrightness()).append(", ");
		sb.append(getKelvin()).append("}");
		return sb.toString();
	}
}