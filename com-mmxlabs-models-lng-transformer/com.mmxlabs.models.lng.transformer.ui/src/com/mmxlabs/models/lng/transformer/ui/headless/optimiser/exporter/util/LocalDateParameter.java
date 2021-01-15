/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util;

import java.time.LocalDate;

public class LocalDateParameter extends AParameter<LocalDate> {

	LocalDateParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;
		this.parameterClass = LocalDate.class;
	}
	
	LocalDateParameter(String key, LocalDate defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	LocalDateParameter(String key, LocalDate defaultValue, LocalDate value) {
		this(key, defaultValue);
		this.value = value;
	}

}
