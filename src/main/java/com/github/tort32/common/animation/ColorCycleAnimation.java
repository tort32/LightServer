package com.github.tort32.common.animation;

import com.github.tort32.api.lifx.protocol.HSBK;

public class ColorCycleAnimation extends BaseAnimation {
	public static final String NAME = "cycle";
	
	private static final int SPEED_DEFAULT = 5;
	private static final int BRIGHTNESS_DEFAULT = 50;
	private static final boolean REVERSE_DEFAULT = false;
	
	private static final String PRESET_NAME = "preset";
	private static final String PRESET_NONE = "";
	private static final String PRESET_DEFAULT = "default";
	private static final String PRESET_QUICK = "quick";
	private static final String PRESET_SMOOTH = "smooth";
	private static final String PRESET_TWILIGHT = "twilight";
	private static final String PRESET_NIGHT = "night";
	private static final String[] PRESETS = new String[] {
		PRESET_NONE, PRESET_DEFAULT, PRESET_QUICK, PRESET_SMOOTH, PRESET_TWILIGHT, PRESET_NIGHT
	};
	
	protected SelectorParam presetParam = new SelectorParam(PRESET_NAME, "Preset configuration", PRESETS, PRESET_NONE);
	protected RangeParam speedParam = new RangeParam("speed", "Hue change speed", 1, 10, SPEED_DEFAULT);
	protected RangeParam brightnessParam = new RangeParam("brightness", "Color brightness", 0, HSBK.BRIGHTNESS_MAX, BRIGHTNESS_DEFAULT);
	protected CheckerParam reversParam = new CheckerParam("reverse", "Reverse color change", REVERSE_DEFAULT);
	
	private int speed = SPEED_DEFAULT;
	private int brightness = BRIGHTNESS_DEFAULT;
	private boolean reverse = REVERSE_DEFAULT;
	private int hue = 0;
	
	private AnimationParam.IChangeListener<String> presetChangeListener = new AnimationParam.IChangeListener<String>() {
		@Override
		public void onChange(String newValue) {
			switch(newValue) {
			case PRESET_NONE:
				break;
			case PRESET_DEFAULT:
				frame.durationParam.reset();
				frame.transitionParam.reset();
				speedParam.reset();
				brightnessParam.reset();
				reversParam.reset();
				break;
			case PRESET_QUICK:
				frame.durationParam.set(200); 
				frame.transitionParam.set(200);
				speedParam.set(10);
				brightnessParam.set(100);
				reversParam.set(false);
				break;
			case PRESET_SMOOTH:
				frame.durationParam.set(200); 
				frame.transitionParam.set(200);
				speedParam.set(1);
				brightnessParam.set(100);
				reversParam.set(false);
				break;
			case PRESET_TWILIGHT:
				frame.durationParam.set(100); 
				frame.transitionParam.set(100);
				speedParam.set(1);
				brightnessParam.set(30);
				reversParam.set(false);
				break;
			case PRESET_NIGHT:
				frame.durationParam.set(200); 
				frame.transitionParam.set(200);
				speedParam.set(1);
				brightnessParam.set(10);
				reversParam.set(false);
				break;
			}
		}
	};
	
	public ColorCycleAnimation() {
		desc = new AnimationDescriptor(NAME);
		desc.addParam(presetParam);
		desc.setChangeListener((name) -> {
			if (!PRESET_NAME.equals(name)) {
				presetParam.reset();
			}
		});
		frame.addFrameAnimParams(desc);
		
		presetParam.setChangeListener(presetChangeListener);
		speedParam.setChangeListener((newValue) -> {
			ColorCycleAnimation.this.speed = newValue;
		});
		brightnessParam.setChangeListener((newValue) -> {
			ColorCycleAnimation.this.brightness = newValue;
		});
		reversParam.setChangeListener((newValue) -> {
			ColorCycleAnimation.this.reverse = newValue;
		});
		desc.addParam(speedParam, brightnessParam, reversParam);
	}

	@Override
	public AnimationFrame getNextFrame() {
		hue += reverse ? -speed : speed;
		if (hue < 0) {
			hue += HSBK.HUE_MAX;
		}
		if (hue >= HSBK.HUE_MAX) {
			hue -= HSBK.HUE_MAX;
		}
		frame.color.hue = hue;
		frame.color.saturation = HSBK.SATURATION_MAX;
		frame.color.brightness = brightness;
		return frame;
	}

}
