/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

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
