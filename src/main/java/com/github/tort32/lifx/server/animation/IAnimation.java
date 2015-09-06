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
		public List<AnimationParam<?>> params = new ArrayList<AnimationParam<?>>();
		private Map<String, AnimationParam<?>> paramsByName = new HashMap<String, AnimationParam<?>>();
		
		protected IParamChangeListener listener;
		
		interface IParamChangeListener {
			void onChange(String name);
		}
		
		AnimationDescriptor(String name) {
			this.name = name;
		}
		
		public void setChangeListener(IParamChangeListener listener) {
			this.listener = listener;
		}
		
		public void addParam(AnimationParam<?>... params) {
			for(AnimationParam<?> param : params) {
				this.params.add(param);
				this.paramsByName.put(param.name, param);
			}
		}
		
		public void setParam(String name, String value) throws IllegalArgumentException, NullPointerException {
			AnimationParam<?> param = findParamByName(name);
			param.setFromString(value);
			if (listener != null) {
				listener.onChange(name);
			}
		}
		
		public AnimationParam<?> findParamByName(String name) {
			return paramsByName.get(name);
		}
	}
	
	public abstract class AnimationParam<T> {
		
		private static Logger logger = LoggerFactory.getLogger(AnimationParam.class);

		public AnimationParamType type;
		public String name;
		public String desc;
		protected T defaultValue;
		protected T currentValue;
		
		protected IChangeListener<T> listener;
		
		interface IChangeListener<T> {
			void onChange(T newValue);
		}
		
		AnimationParam(AnimationParamType type, String name, String desc, T defaultValue) {
			this.type = type;
			this.name = name;
			this.desc = desc;
			this.currentValue = this.defaultValue = defaultValue;
		}
		
		public void setChangeListener(IChangeListener<T> listener) {
			this.listener = listener;
		}
		
		public void reset() {
			set(defaultValue);
		}
		
		public void setFromString(String newValue) throws IllegalArgumentException {
			try {
				T value = parse(newValue);
				set(value);
			} catch (IllegalArgumentException e) {
				logger.debug("Param '" + name + "' value '" + newValue + "' is invalid", e);
				throw e;
			}
		}
		
		public void set(T newValue) throws IllegalArgumentException {
			validate(newValue);
			currentValue = newValue;
			if (listener != null) {
				listener.onChange(newValue);
			}
		}
		
		public T get() {
			return currentValue;
		}
		
		@SuppressWarnings("unchecked")
		protected T parse(String value) {
			if (currentValue instanceof String) {
				return (T) value;
			} else if (currentValue instanceof Integer) {
				return (T) (Integer) Integer.parseInt(value);
			} else if (currentValue instanceof Boolean) {
				return (T) (Boolean) Boolean.parseBoolean(value);
			} else if (currentValue instanceof Float) {
				return (T) (Float) Float.parseFloat(value);
			}
			throw new RuntimeException("Not supported type");
		}
		
		protected abstract void validate(T value) throws IllegalArgumentException;
	}
	
	public class RangeParam extends AnimationParam<Integer> {
		
		public int minValue;
		public int maxValue;
		
		RangeParam(String name, String desc, int minValue, int maxValue, int defaultValue) {
			super(AnimationParamType.SLIDER, name, desc, defaultValue);
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
		
		public void validate(Integer value) throws IllegalArgumentException {
			if (value == null) {
				throw new IllegalArgumentException("Value should be not null");
			}
			if(value < this.minValue || value > this.maxValue) {
				throw new IllegalArgumentException("Value should be in range (" + this.minValue + ", " + this.maxValue + ")");
			}
		}
	}
	
	public class CheckerParam extends AnimationParam<Boolean> {

		CheckerParam(String name, String desc, boolean defaultValue) {
			super(AnimationParamType.CHECKBOX, name, desc, defaultValue);
		}
		
		public void validate(Boolean value) throws IllegalArgumentException {
			if (value == null) {
				throw new IllegalArgumentException("Value should be not null");
			}
		}
	}
	
	public class SelectorParam extends AnimationParam<String> {
		
		public String[] options;
		
		SelectorParam(String name, String desc, String[] options, String defaultValue) {
			super(AnimationParamType.COMBOBOX, name, desc, defaultValue);
			this.options = options;
		}
		
		public void validate(String value) throws IllegalArgumentException {
			if (value == null) {
				throw new IllegalArgumentException("Value should be not null");
			}
			for (String option : options) {
				if (option.equalsIgnoreCase(value)) {
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
