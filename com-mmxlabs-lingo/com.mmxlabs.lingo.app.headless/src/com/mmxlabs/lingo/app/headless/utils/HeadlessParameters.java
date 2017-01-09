/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;

public class HeadlessParameters {

	private final Map<String, Parameter<?>> parameters = new HashMap<>();
	private final Map<String, Boolean> requiredParameters = new HashMap<>();

	public HeadlessParameters() {

	}

	public JSONParseResult setParametersFromJSON(final Map<String, Pair<Object, Class<?>>> jsonParams) {
		final JSONParseResult parseResult = new JSONParseResult();
		for (final String param : jsonParams.keySet()) {
			final Pair<Object, Class<?>> jsonParam = jsonParams.get(param);
			final Object jsonValue = jsonParam.getFirst();
			final Class<?> jsonClass = jsonParam.getSecond();
			if (parameters.containsKey(param)) {
				final Parameter<?> parameter = parameters.get(param);
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
		final Set<String> unknowns = jsonParams.keySet();
		unknowns.removeAll(parameters.keySet());
		parseResult.setNotInJSON(jsonParams.keySet());
		for (final String defaultKey : requiredParameters.keySet()) {
			if (!requiredParameters.get(defaultKey)) {
				parseResult.setDefaultNotSet(defaultKey);
			}
		}
		return parseResult;
	}

	public <T> void setParameter(final String key, final @Nullable T value, final Class<T> clazz) {
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
		} else if (clazz.isAssignableFrom(LocalDate.class)) {
			parameters.put(key, new LocalDateParameter(key, (LocalDate) value));
		} else if (clazz.isAssignableFrom(YearMonth.class)) {
			parameters.put(key, new YearMonthParameter(key, (YearMonth) value));
		}

	}

	public <T> void setParameter(final String key, final @Nullable T value, final Class<T> clazz, final boolean required) {
		if (required) {
			requiredParameters.put(key, false);
		}
		setParameter(key, value, clazz);
	}

	public Map<String, Parameter<?>> getParameters() {
		return parameters;
	}

	public <T> T getParameter(final String key, final Class<T> parameterClass) {
		final Parameter<?> parameter = parameters.get(key);
		if (parameter != null) {
			if (parameterClass.isInstance(parameter)) {
				return parameterClass.cast(parameter);
			}
		}
		return (T) null;
	}

	public <T> T getParameterValue(final String key, final Class<T> parameterClass) {
		final Parameter<?> parameter = parameters.get(key);
		if (parameter == null || !parameter.isOfType(parameterClass)) {
			return (T) null;
		} else {
			return (T) parameter.getValue();
		}
	}

}
