package com.mmxlabs.lingo.app.headless.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.Pair;

public class HeadlessParameters {
	
	private Map<String, Parameter<?>> parameters = new HashMap<>();
	private Map<String, Boolean> requiredParameters = new HashMap<>();
	
	public HeadlessParameters() {

	}
	
	public JSONParseResult setParametersFromJSON(Map<String, Pair<Object, Class<?>>> jsonParams) {
		JSONParseResult parseResult = new JSONParseResult();
		for (String param : jsonParams.keySet()) {
			Pair<Object, Class<?>> jsonParam = jsonParams.get(param);
			Object jsonValue = jsonParam.getFirst();
			Class<?> jsonClass = jsonParam.getSecond();
			if (parameters.containsKey(param)) {
				Parameter<?> parameter = parameters.get(param);
				// check type
				if (parameter.isOfType(jsonClass)) {
					parameter.setValue(jsonValue);
					if (requiredParameters.containsKey(param)) {
						requiredParameters.put(param, true);
					}
				} else {
					parseResult.addToNotInType(param, jsonValue);
				}
			} else {
				parseResult.addToNotInParams(param);
			}
		}
		// note destructive to JSON
		Set<String> unknowns = jsonParams.keySet();
		unknowns.removeAll(parameters.keySet());
		parseResult.setNotInJSON(jsonParams.keySet());
		for (String defaultKey : requiredParameters.keySet()) {
			if (!requiredParameters.get(defaultKey)) {
				parseResult.setDefaultNotSet(defaultKey);
			}
		}
		return parseResult;
	}
	
	public <T> void setParameter(String key, T value, Class<T> clazz) {
		if (clazz.isAssignableFrom(Boolean.class)) {
			parameters.put(key, new BooleanParameter(key, (Boolean) value));
		} else if (clazz.isAssignableFrom(Integer.class)) {
			parameters.put(key, new IntegerParameter(key, (Integer) value));
		} else if (clazz.isAssignableFrom(Double.class)) {
			parameters.put(key, new DoubleParameter(key, (Double) value));
		} else if (clazz.isAssignableFrom(String.class)) {
			parameters.put(key, new StringParameter(key, (String) value));
		} else if (clazz.isAssignableFrom(StringList.class)) {
			parameters.put(key, new StringListParameter(key, (StringList) value));
		} else if (clazz.isAssignableFrom(DoubleMap.class)) {
			parameters.put(key, new DoubleMapParameter(key, (DoubleMap) value));
		} else if (clazz.isAssignableFrom(Date.class)) {
			parameters.put(key, new DateParameter(key, (Date) value));
		}

	}
	
	public <T> void setParameter(String key, T value, Class<T> clazz, boolean required) {
		if (required) {
			requiredParameters.put(key, false);
		}
		setParameter(key, value, clazz);
	}
	
	public Map<String, Parameter<?>> getParameters() {
		return parameters;
	}
	
	public <T> T getParameter(String key, Class<T> parameterClass) {
		Parameter<?> parameter = parameters.get(key);
		if (parameter != null) {
			if (parameterClass.isInstance(parameter)) {
				return parameterClass.cast(parameter);
			}
		}
		return null;
	}
	
	public <T> T getParameterValue(String key, Class<T> parameterClass) {
		Parameter<?> parameter = parameters.get(key);
		if (parameter == null || !parameter.isOfType(parameterClass)) {
			return null;
		} else {
			return (T) parameter.getValue();
		}
	}

}
