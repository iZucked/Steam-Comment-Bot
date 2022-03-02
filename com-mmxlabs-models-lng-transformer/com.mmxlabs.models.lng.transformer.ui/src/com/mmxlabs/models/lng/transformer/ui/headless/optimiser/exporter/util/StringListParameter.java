/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util;

import java.lang.reflect.ParameterizedType;

public class StringListParameter extends AParameter<StringList> {

	StringListParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;
	    ParameterizedType p = (ParameterizedType) this.getClass().getGenericSuperclass();

		this.parameterClass = StringList.class;
	}
	
	StringListParameter(String key, StringList defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	StringListParameter(String key, StringList defaultValue, StringList value) {
		this(key, defaultValue);
		this.value = value;
	}
	


}
