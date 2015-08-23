package com.github.tort32.lifx.server.animation;

import com.github.tort32.lifx.protocol.HSBK;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationDescriptor;
import com.github.tort32.lifx.server.animation.IAnimation.AnimationParam;

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
	
	private AnimationParam.IParamChangeListener durationListener = new AnimationParam.IParamChangeListener() {
		
		@Override
		public void onChange(String value) {
			AnimationFrame.this.duration = Integer.parseInt(value);
		}
	}; 
	
	private AnimationParam.IParamChangeListener transitionListener = new AnimationParam.IParamChangeListener() {
		
		@Override
		public void onChange(String value) {
			AnimationFrame.this.transition = Integer.parseInt(value);
		}
	};
	
	public void addFrameAnimParams(AnimationDescriptor desc) {
		desc.addParam(new AnimationParam(DURATION_NAME, DURATION_DESC, DURATION_MIN, DURATION_MAX, DEFAULT_DURATION, durationListener));
		desc.addParam(new AnimationParam(TRANSITION_NAME, TRANSITION_DESC, TRANSITION_MIN, TRANSITION_MAX, DEFAULT_TRANSITION, transitionListener));
	}
}