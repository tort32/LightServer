package com.github.tort32.lifx.protocol;

import com.github.tort32.lifx.protocol.Types.UInt16;


public class HSBK {
	public static HSBK Green = new HSBK(120, 100, 100);
	public static HSBK DarkGreen = new HSBK(120, 100, 10);
	public static HSBK Red = new HSBK(0, 100, 50);
	public static HSBK DarkRed = new HSBK(0, 100, 10);

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