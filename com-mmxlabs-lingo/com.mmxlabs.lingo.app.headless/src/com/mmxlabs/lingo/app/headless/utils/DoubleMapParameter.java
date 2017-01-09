/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

public class DoubleMapParameter extends AParameter<DoubleMap> {

	DoubleMapParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;

		this.parameterClass = DoubleMap.class;
	}
	
	DoubleMapParameter(String key, DoubleMap defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	DoubleMapParameter(String key, DoubleMap defaultValue, DoubleMap value) {
		this(key, defaultValue);
		this.value = value;
	}
	


}
