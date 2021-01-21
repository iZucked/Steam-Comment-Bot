/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util;

public class IntegerParameter extends AParameter<Integer> {

	IntegerParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;
		this.parameterClass = Integer.class;
	}
	
	IntegerParameter(String key, Integer defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	IntegerParameter(String key, Integer defaultValue, Integer value) {
		this(key, defaultValue);
		this.value = value;
	}

}
