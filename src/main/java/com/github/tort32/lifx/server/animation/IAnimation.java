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
	
	public abstract class AnimationParam {
		
		private static Logger logger = LoggerFactory.getLogger(AnimationParam.class);

		public AnimationParamType type;
		public String name;
		public String desc;
		
		protected IChangeListener listener;
		
		interface IChangeListener {
			void onChange();
		}
		
		AnimationParam(AnimationParamType type, String name, String desc) {
			this.type = type;
			this.name = name;
			this.desc = desc;
		}
		
		public void setChangeListener(IChangeListener listener) {
			this.listener = listener;
		}
		
		public void setValue(String newValue) throws IllegalArgumentException {
			try {
				setValueInternal(newValue);
				if (listener != null) {
					listener.onChange();
				}
			} catch (IllegalArgumentException e) {
				logger.debug("Param '" + name + "' value '" + newValue + "' is invalid", e);
				throw e;
			}
		}
		
		protected abstract void setValueInternal(String value) throws IllegalArgumentException;
	}
	
	public class RangeParam extends AnimationParam {
		
		public int curValue;
		public int minValue;
		public int maxValue;
		
		RangeParam(String name, String desc, int minValue, int maxValue, int defaultValue) {
			super(AnimationParamType.SLIDER, name, desc);
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.curValue = defaultValue;
		}
		
		public void setValueInternal(String newValue) throws IllegalArgumentException {
			int value = Integer.parseInt(newValue);
			if(value < this.minValue || value > this.maxValue) {
				throw new IllegalArgumentException("Value should be in range (" + this.minValue + ", " + this.maxValue + ")");
			}
			this.curValue = value;
		}
	}
	
	public class CheckerParam extends AnimationParam {
		
		public boolean curValue;
		
		CheckerParam(String name, String desc, boolean defaultValue) {
			super(AnimationParamType.CHECKBOX, name, desc);
			this.curValue = defaultValue;
		}
		
		public void setValueInternal(String newValue) throws IllegalArgumentException {
			boolean value = Boolean.parseBoolean(newValue);
			this.curValue = value;
		}
	}
	
	public class SelectorParam extends AnimationParam {
		
		public String curValue;
		public String[] values;
		
		SelectorParam(String name, String desc, String[] options, String defaultValue) {
			super(AnimationParamType.COMBOBOX, name, desc);
			this.values = options;
			this.curValue = defaultValue;
		}
		
		public void setValueInternal(String newValue) throws IllegalArgumentException {
			for (String value : values) {
				if(value.equalsIgnoreCase(newValue)) {
					this.curValue = newValue;
					return;
				}
			}
			throw new IllegalArgumentException("Value should be one from possible options");
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
