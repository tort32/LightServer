package com.github.tort32.common.animation;

import java.util.Random;

import com.github.tort32.api.lifx.protocol.HSBK;

public class RandomAnimation extends BaseAnimation {
	public static final String NAME = "random";
	
	private static final int BRIGHTNESS_DEFAULT = 50;
	private static final boolean RAND_SATURATION_DEFAULT = false;
	
	protected RangeParam brightnessParam = new RangeParam("brightness", "Color brightness", 0, HSBK.BRIGHTNESS_MAX, BRIGHTNESS_DEFAULT);
	protected CheckerParam randSatParam = new CheckerParam("randSaturation", "Randomize stauration", RAND_SATURATION_DEFAULT);
	
	private int brightness = BRIGHTNESS_DEFAULT;
	private boolean randSaturation = RAND_SATURATION_DEFAULT;
	private Random rand = new Random();
	
	public RandomAnimation() {
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
	public AnimationFrame getNextFrame() {
		frame.color.hue = rand.nextInt(HSBK.HUE_MAX);
		frame.color.saturation = randSaturation ? rand.nextInt(HSBK.SATURATION_MAX + 1) : HSBK.SATURATION_MAX;
		frame.color.brightness = brightness;
		return frame;
	}

}
