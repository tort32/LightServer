package com.github.tort32.lifx.server.animation;

import java.util.Random;

import com.github.tort32.lifx.protocol.HSBK;

public class RandomAnimation implements IAnimation {
	public static final String NAME = "random";
	
	private static final int DEFAULT_BRIGHTNESS = 50;
	private static final boolean DEFAULT_RAND_SATURATION = false;
	
	private final AnimationDescriptor desc;
	private AnimationFrame frame;
	private int brightness = DEFAULT_BRIGHTNESS;
	private boolean randSaturation = DEFAULT_RAND_SATURATION;
	private Random rand = new Random();
	
	public RandomAnimation() {
		frame = new AnimationFrame();
		desc = new AnimationDescriptor(NAME);
		frame.addFrameAnimParams(desc);
		desc.addParam(new AnimationParam("brightness", "Color brightness",
				0, HSBK.BRIGHTNESS_MAX, DEFAULT_BRIGHTNESS,
				new AnimationParam.IParamChangeListener() {
			@Override
			public void onChange(String value) throws NumberFormatException {
				RandomAnimation.this.brightness = Integer.parseInt(value);
			}
		}));
		desc.addParam(new AnimationParam("randSaturation", "Randomize stauration",
				DEFAULT_RAND_SATURATION,
				new AnimationParam.IParamChangeListener() {
			@Override
			public void onChange(String value) throws NumberFormatException {
				RandomAnimation.this.randSaturation = Boolean.parseBoolean(value);
			}
		}));
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public AnimationDescriptor getDescriptor() {
		return desc;
	}

	@Override
	public AnimationFrame getNextFrame() {
		frame.color.setHue(rand.nextInt(HSBK.HUE_MAX));
		frame.color.setSaturation(randSaturation ? rand.nextInt(HSBK.SATURATION_MAX + 1) : HSBK.SATURATION_MAX);
		frame.color.setBrightness(brightness);
		return frame;
	}

}
