package com.github.tort32.lifx.server.animation;

import com.github.tort32.lifx.protocol.HSBK;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationDescriptor;
import com.github.tort32.lifx.server.animation.IAnimation.RangeParam;

public class AnimationFrame {
	public static final String TRANSITION_NAME = "transition";
	public static final String TRANSITION_DESC = "Color transition (ms)";
	public static final int TRANSITION_MIN = 100;
	public static final int TRANSITION_MAX = 30000;
	public static final int DEFAULT_TRANSITION = 100;
	
	public static final String DURATION_NAME = "duration";
	public static final String DURATION_DESC = "Animation step (ms)";
	public static final int DURATION_MIN = 100;
	public static final int DURATION_MAX = 30000;
	public static final int DEFAULT_DURATION = 1000;
	
	public HSBK color = new HSBK();
	public int transition = DEFAULT_TRANSITION; // color transition time in ms
	public int duration = DEFAULT_DURATION; // frame duration time in ms
	
	public void addFrameAnimParams(AnimationDescriptor desc) {
		// Add duration
		RangeParam durationParam = new RangeParam(DURATION_NAME, DURATION_DESC, DURATION_MIN, DURATION_MAX, DEFAULT_DURATION);
		durationParam.setChangeListener(() -> {
			AnimationFrame.this.duration = durationParam.curValue;
		});
		desc.addParam(durationParam);
		// Add transition
		RangeParam transitionParam = new RangeParam(TRANSITION_NAME, TRANSITION_DESC, TRANSITION_MIN, TRANSITION_MAX, DEFAULT_TRANSITION);
		transitionParam.setChangeListener(() -> {
			AnimationFrame.this.transition = transitionParam.curValue;
		});
		desc.addParam(transitionParam);
	}
}