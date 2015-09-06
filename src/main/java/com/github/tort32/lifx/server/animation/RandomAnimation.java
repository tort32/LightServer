package com.github.tort32.lifx.server.animation;

import java.util.Random;

import com.github.tort32.lifx.protocol.HSBK;

public class RandomAnimation implements IAnimation {
	public static final String NAME = "random";
	
	private static final int DEFAULT_BRIGHTNESS = 50;
	private static final boolean DEFAULT_RAND_SATURATION = false;
	
	protected RangeParam brightnessParam = new RangeParam("brightness", "Color brightness", 0, HSBK.BRIGHTNESS_MAX, DEFAULT_BRIGHTNESS);
	protected CheckerParam randSatParam = new CheckerParam("randSaturation", "Randomize stauration", DEFAULT_RAND_SATURATION);
	
	private final AnimationDescriptor desc;
	private AnimationFrame frame;
	private int brightness = DEFAULT_BRIGHTNESS;
	private boolean randSaturation = DEFAULT_RAND_SATURATION;
	private Random rand = new Random();
	
	public RandomAnimation() {
		frame = new AnimationFrame();
		desc = new AnimationDescriptor(NAME);
		frame.addFrameAnimParams(desc);
		
		brightnessParam.setChangeListener((newValue) -> {	
			RandomAnimation.this.brightness = newValue;
		});
		randSatParam.setChangeListener((newValue) -> {
			RandomAnimation.this.randSaturation = newValue;
		});
		desc.addParam(brightnessParam, randSatParam);
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
