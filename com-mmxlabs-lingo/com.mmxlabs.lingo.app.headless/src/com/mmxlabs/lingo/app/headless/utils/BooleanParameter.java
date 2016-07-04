/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

public class BooleanParameter extends AParameter<Boolean> {

	BooleanParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;
		this.parameterClass = Boolean.class;
	}
	
	BooleanParameter(String key, Boolean defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	BooleanParameter(String key, Boolean defaultValue, Boolean value) {
		this(key, defaultValue);
		this.value = value;
	}

}
