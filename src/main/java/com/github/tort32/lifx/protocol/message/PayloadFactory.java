package com.github.tort32.lifx.protocol.message;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.github.tort32.lifx.device.recieve.Acknowledgement;
import com.github.tort32.lifx.device.recieve.EchoResponse;
import com.github.tort32.lifx.device.recieve.StateHostFirmware;
import com.github.tort32.lifx.device.recieve.StateHostInfo;
import com.github.tort32.lifx.device.recieve.StateInfo;
import com.github.tort32.lifx.device.recieve.StateLabel;
import com.github.tort32.lifx.device.recieve.StateService;
import com.github.tort32.lifx.device.recieve.StateVersion;
import com.github.tort32.lifx.device.recieve.StateWifiFirmware;
import com.github.tort32.lifx.device.recieve.StateWifiInfo;
import com.github.tort32.lifx.device.send.GetService;
import com.github.tort32.lifx.light.recieve.State;
import com.github.tort32.lifx.protocol.InBuffer;

public class PayloadFactory {
	
	private static final Map<Integer, Class<? extends Payload>> TYPES = new HashMap<Integer, Class<? extends Payload>>();
	
	@SafeVarargs
	public static void register(Class<? extends Payload>... types) {
		for(Class<? extends Payload> type : types) {
			register(type);
		}
	}
	
	public static void register(Class<? extends Payload> type) {
		Field idField = null;
		try {
			idField = type.getField("ID");
		} catch(NoSuchFieldException | SecurityException e) {
			throw new RuntimeException("Failed to find ID field at " + type.getSimpleName(), e);
		}
		int id;
		try {
			id = idField.getInt(null);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Failed to get ID from " + type.getSimpleName(), e);
		}
		TYPES.put(id, type);
	}

	public static Payload createFromBuffer(int id, InBuffer buffer) {
    	Class<? extends Payload> payloadType = TYPES.get(id);
    	if (payloadType == null) {
    		System.out.println("Unregistered payload ID = " + id);
    		return null;
    	}
    	Constructor<? extends Payload> ctor = null;
		try {
			ctor = payloadType.getConstructor(InBuffer.class);
		} catch(NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Constructor with InBuffer not defined for " + payloadType.getSimpleName(), e);
		}
		try {
			return ctor.newInstance(buffer);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Failed to create instance of " + payloadType.getSimpleName(), e);
		}
    }
	
	static {
		// Register response payload types
    	register(GetService.class, StateService.class, StateHostInfo.class, StateHostFirmware.class,
    			StateWifiInfo.class, StateWifiFirmware.class, com.github.tort32.lifx.device.recieve.StatePower.class,
    			StateLabel.class, StateVersion.class, StateInfo.class, Acknowledgement.class, EchoResponse.class); // Device responses
    	register(State.class, com.github.tort32.lifx.light.recieve.StatePower.class); // Light responses
    }
}
