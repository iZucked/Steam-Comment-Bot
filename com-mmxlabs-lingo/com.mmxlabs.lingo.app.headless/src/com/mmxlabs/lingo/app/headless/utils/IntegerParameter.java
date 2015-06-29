/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

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
