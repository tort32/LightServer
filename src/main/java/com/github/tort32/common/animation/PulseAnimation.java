package com.github.tort32.common.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.tort32.common.entity.LightColor;

public class PulseAnimation extends BaseAnimation {
	public static final String NAME = "pulse";
	
	public static final String PERIOD_NAME = "period";
	public static final String PERIOD_DESC = "Cycle period (ms)";
	public static final int PERIOD_MIN = 100;
	public static final int PERIOD_MAX = 3000;
	public static final int PERIOD_DEFAULT = 1000;
	
	enum ModType {
		SINE("sine"),
		SQUARE("square"),
		TRIANGLE("triangle"),
		SAW("saw"),
		SAW_REV("sawrev");
		
		public String name;
		private static HashMap<String, ModType> modByName = new HashMap<String, ModType>();
		private static String[] names;
		
		ModType(String name) {
			this.name = name;
		}
		
		public static ModType getByName(String name) {
			return modByName.get(name);
		}
		
		public static String[] getAllNames() {
			return names;
		}
		
		static {
			List<String> namesList = new ArrayList<String>();
			for(ModType m : ModType.values()) {
				modByName.put(m.name, m);
				namesList.add(m.name);
			}
			names = namesList.toArray(new String[0]);
		}
	}
	
	private static final String MOD_NAME = "modulation";
	
	protected RangeParam periodParam = new RangeParam(PERIOD_NAME, PERIOD_DESC, PERIOD_MIN, PERIOD_MAX, PERIOD_DEFAULT);
	protected SelectorParam modParam = new SelectorParam(MOD_NAME, "Pulse form", ModType.getAllNames());
	
	private ModType type = ModType.SINE;
	private int period = PERIOD_DEFAULT;
	private int time;
	private LightColor initColor = new LightColor(0, 0, 100);
	
	public PulseAnimation() {
		desc = new AnimationDescriptor(NAME);
		setFrameDuration(PERIOD_DEFAULT);
		modParam.setChangeListener((newValue) -> {
			type = ModType.getByName(newValue);
			setFrameDuration(period);
		});
		periodParam.setChangeListener((newValue) -> {
			period = newValue;
			setFrameDuration(period);
		});
		desc.addParam(modParam, periodParam);
	}
	
	@Override
	public void setInitColor(LightColor color) {
		initColor = color;
	}

	@Override
	public AnimationFrame getNextFrame() {
		double mod = getNextModulationRatio();
		frame.color.hue = initColor.hue;
		frame.color.saturation = initColor.saturation;
		frame.color.brightness = (int) ((double) initColor.brightness * mod);
		return frame;
	}
	
	private void setFrameDuration(int period) {
		frame.duration = Math.min(period / 32, PERIOD_MIN / 2);
		if (type == ModType.SQUARE) {
			frame.transition = 0; // instant
		} else {
			frame.transition = frame.duration; // smooth
		}
	}
	
	private double getNextModulationRatio() {
		time += frame.duration;
		while (time > period) {
			time -= period;
		}
		double x = (double) time / (double) period; // 0..1
		double mod = 0; // 0..1
		switch (type) {
		case SINE:
			mod = 0.5 * (Math.sin(2.0 * Math.PI * x) + 1.0);
			break;
		case SAW:
			mod = x;
			break;
		case SAW_REV:
			mod = 1.0 - x;
			break;
		case TRIANGLE:
			mod = (x < 0.5) ? (1.0 - 2.0 * x) : (2.0 * x - 1.0);
			break;
		case SQUARE:
			mod = (x < 0.5) ? 1 : 0;
			break;
		}
		return mod;
	}

}
