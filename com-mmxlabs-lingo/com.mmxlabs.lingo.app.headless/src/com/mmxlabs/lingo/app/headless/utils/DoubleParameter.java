/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

public class DoubleParameter extends AParameter<Double> {

	DoubleParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;
		this.parameterClass = Double.class;
	}
	
	DoubleParameter(String key, Double defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	DoubleParameter(String key, Double defaultValue, Double value) {
		this(key, defaultValue);
		this.value = value;
	}
}
