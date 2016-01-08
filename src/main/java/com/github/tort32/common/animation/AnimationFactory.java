package com.github.tort32.common.animation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnimationFactory {
	
	private static final Map<String, Class<? extends IAnimation>> ANIMATIONS = new HashMap<String, Class<? extends IAnimation>>();
	
	public static void registerAnimation(Class<? extends IAnimation> type) {
		Field idField;
		try {
			idField = type.getField("NAME");
		} catch(NoSuchFieldException | SecurityException e) {
			throw new RuntimeException("Failed to find ID field at " + type.getSimpleName(), e);
		}
		String name;
		try {
			name = (String) idField.get(null);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Failed to get ID from " + type.getSimpleName(), e);
		}
		ANIMATIONS.put(name, type);
	}
	
	public static IAnimation createAnimationByName(String name) {
    	Class<? extends IAnimation> type = ANIMATIONS.get(name);
    	if (type == null) {
    		return null;
    	}
    	Constructor<? extends IAnimation> ctor = null;
		try {
			ctor = type.getConstructor();
		} catch(NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Constructor defined for " + type.getSimpleName(), e);
		}
		try {
			return ctor.newInstance();
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Failed to create instance of " + type.getSimpleName(), e);
		}
    }
	
	public static Collection<String> getAnimationNames() {
		return ANIMATIONS.keySet();
	}
	
	static {
		registerAnimation(RandomAnimation.class);
		registerAnimation(ColorCycleAnimation.class);
	}
}
