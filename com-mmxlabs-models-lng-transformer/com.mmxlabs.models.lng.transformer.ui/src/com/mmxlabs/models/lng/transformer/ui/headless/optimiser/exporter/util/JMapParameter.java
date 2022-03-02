/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util;

public class JMapParameter extends AParameter<JMap> {

	JMapParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;

		this.parameterClass = JMap.class;
	}

	JMapParameter(String key, JMap defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}

	JMapParameter(String key, JMap defaultValue, JMap value) {
		this(key, defaultValue);
		this.value = value;
	}

}
