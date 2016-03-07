package com.github.tort32.common.animation;

import com.github.tort32.common.entity.LightColor;

public class GradientAnimation extends BaseAnimation {
	public static final String NAME = "gradient";
	
	public static final String PERIOD_NAME = "period";
	public static final String PERIOD_DESC = "Period time (s)";
	public static final int PERIOD_MIN = 1;
	public static final int PERIOD_MAX = 3600; // 1h
	public static final int PERIOD_DEFAULT = 60;
	
	private static final boolean REPEAT_DEFAULT = false;
	
	protected RangeParam periodParam = new RangeParam(PERIOD_NAME, PERIOD_DESC, PERIOD_MIN, PERIOD_MAX, PERIOD_DEFAULT);
	protected CheckerParam repeatParam = new CheckerParam("repeat", "Repeat by loop", REPEAT_DEFAULT);
	
	private int time = 0;
	
	public GradientAnimation() {
		desc = new AnimationDescriptor(NAME);
		setFrameDuration(PERIOD_DEFAULT);
		periodParam.setChangeListener((newValue) -> {
			setFrameDuration(newValue);
		});
		desc.addParam(periodParam, repeatParam);
	}

	@Override
	public AnimationFrame getNextFrame() {
		time += frame.duration;
		int periodMs = 1000 * periodParam.get(); 
		if (time >= periodMs) {
			boolean repeat = repeatParam.get();
			if (repeat) {
				time -= periodMs;
			} else {
				time = periodMs;
			}
		}
		double x = (double) time / ((double) periodMs); // 0..1
		setColor(frame.color, x);
		return frame;
	}
	
	private void setFrameDuration(int period) {
		frame.duration = Math.min(Math.max(100, period * 17), 5000); // ms
		frame.transition = frame.duration;
	}
	
	private void setColor(LightColor c, double x) {
		double y = x * 3.0;
		if (y < 1.0) {
			c.hue = 0;
			c.saturation = 100;
			c.brightness = (int) (y * 25.0);
		} else if (y < 2.0) {
			c.hue = (int) ((y - 1.0) * 45.0);
			c.saturation = 100;
			c.brightness = (int) ((y - 1.0) * 50.0 + 25.0);
		} else {
			c.hue = 45;
			c.saturation = (int) ((3.0 - y) * 100.0);
			c.brightness = (int) ((y - 2.0) * 25.0 + 75.0);
		}
		c.kelvin = 6600;
	}

}
