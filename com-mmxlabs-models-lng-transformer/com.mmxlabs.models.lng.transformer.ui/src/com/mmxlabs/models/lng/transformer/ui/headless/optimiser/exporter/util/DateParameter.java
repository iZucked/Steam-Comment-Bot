/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util;

import java.util.Date;

public class DateParameter extends AParameter<Date> {

	DateParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;
		this.parameterClass = Date.class;
	}
	
	DateParameter(String key, Date defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	DateParameter(String key, Date defaultValue, Date value) {
		this(key, defaultValue);
		this.value = value;
	}

}
