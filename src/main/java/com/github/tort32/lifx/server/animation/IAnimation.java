package com.github.tort32.lifx.server.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IAnimation {
	
	public class AnimationDescriptor {
		public String name;
		public List<AnimationParam> params = new ArrayList<AnimationParam>();
		private Map<String, AnimationParam> paramsByName = new HashMap<String, AnimationParam>();
		
		AnimationDescriptor(String name, AnimationParam... params) {
			this.name = name;
			for(AnimationParam param : params) {
				addParam(param);
			}
		}
		
		public void addParam(AnimationParam param) {
			this.params.add(param);
			this.paramsByName.put(param.name, param);
		}
		
		public void setParam(String name, String value) {
			AnimationParam param = findParamByName(name);
			if (param != null) {
				param.setValue(value);
			}
		}
		
		public AnimationParam findParamByName(String name) {
			return paramsByName.get(name);
		}
	}
	
	public class AnimationParam {
		
		private static Logger logger = LoggerFactory.getLogger(AnimationFrame.class);
		
		public AnimationParamType type;
		public String desc;
		public String name;
		public String minValue;
		public String maxValue;
		public String curValue;
		public String[] values;
		IParamChangeListener listener;
		
		interface IParamChangeListener {
			void onChange(String value);
		}
		
		private AnimationParam(String name, String desc, AnimationParamType type) {
			this.name = name;
			this.desc = desc;
			this.type = type;
		}
		
		AnimationParam(String name, String desc, boolean defaultValue, IParamChangeListener listener) {
			this(name, desc, AnimationParamType.CHECKBOX);
			this.curValue = String.valueOf(defaultValue);
			this.listener = listener;
		}
		
		AnimationParam(String name, String desc, int minValue, int maxValue, int defaultValue, IParamChangeListener listener) {
			this(name, desc, AnimationParamType.SLIDER);
			this.minValue = String.valueOf(minValue);
			this.maxValue = String.valueOf(maxValue);
			this.curValue = String.valueOf(defaultValue);
			this.listener = listener;
		}
		
		AnimationParam(String name, String desc, String[] options, String defaultValue, IParamChangeListener listener) {
			this(name, desc, AnimationParamType.COMBOBOX);
			this.values = options;
			this.curValue = defaultValue;
			this.listener = listener;
		}
		
		public void setValue(String newValue) {
			this.curValue = newValue;
			if (listener != null) {
				try {
					listener.onChange(newValue);
				} catch (NumberFormatException e) {
					logger.debug("Param '" + name + "' value '" + newValue + "' is invalid", e);
				}
			}
		}
	}
	
	public enum AnimationParamType {
		CHECKBOX("checkbox"),
		SLIDER("range"),
		COMBOBOX("select");
		
		private String value;
		
		private AnimationParamType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		};
	}
	
	String getName();
	
	AnimationDescriptor getDescriptor();

	AnimationFrame getNextFrame();
}
