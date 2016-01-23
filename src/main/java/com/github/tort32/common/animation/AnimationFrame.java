package com.github.tort32.common.animation;

import com.github.tort32.common.animation.IAnimation.AnimationDescriptor;
import com.github.tort32.common.animation.IAnimation.RangeParam;
import com.github.tort32.common.entity.LightColor;

public class AnimationFrame {
	public static final String TRANSITION_NAME = "transition";
	public static final String TRANSITION_DESC = "Color transition (ms)";
	public static final int TRANSITION_MIN = 100;
	public static final int TRANSITION_MAX = 30000;
	public static final int TRANSITION_DEFAULT = 100;
	
	public static final String DURATION_NAME = "duration";
	public static final String DURATION_DESC = "Animation step (ms)";
	public static final int DURATION_MIN = 100;
	public static final int DURATION_MAX = 30000;
	public static final int DURATION_DEFAULT = 1000;
	
	public RangeParam durationParam = new RangeParam(DURATION_NAME, DURATION_DESC, DURATION_MIN, DURATION_MAX, DURATION_DEFAULT);
	public RangeParam transitionParam = new RangeParam(TRANSITION_NAME, TRANSITION_DESC, TRANSITION_MIN, TRANSITION_MAX, TRANSITION_DEFAULT);
	
	public LightColor color = new LightColor(0, 0, 50);
	public int transition = TRANSITION_DEFAULT; // color transition time in ms
	public int duration = DURATION_DEFAULT; // frame duration time in ms
	
	public void addFrameAnimParams(AnimationDescriptor desc) {
		// Add duration
		durationParam.setChangeListener((newValue) -> {
			AnimationFrame.this.duration = newValue;
		});
		desc.addParam(durationParam);
		// Add transition
		transitionParam.setChangeListener((newValue) -> {
			AnimationFrame.this.transition = newValue;
		});
		desc.addParam(transitionParam);
	}
}