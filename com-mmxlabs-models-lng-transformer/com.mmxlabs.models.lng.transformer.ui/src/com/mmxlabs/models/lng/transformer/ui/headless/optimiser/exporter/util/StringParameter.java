/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util;

public class StringParameter extends AParameter<String> {

	StringParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;
		this.parameterClass = String.class;
	}
	
	StringParameter(String key, String defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	StringParameter(String key, String defaultValue, String value) {
		this(key, defaultValue);
		this.value = value;
	}

}
