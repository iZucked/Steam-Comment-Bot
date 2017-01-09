/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

import java.time.YearMonth;

public class YearMonthParameter extends AParameter<YearMonth> {

	YearMonthParameter(String key) {
		this.key = key;
		this.defaultValue = null;
		this.value = null;
		this.parameterClass = YearMonth.class;
	}
	
	YearMonthParameter(String key, YearMonth defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}
	
	YearMonthParameter(String key, YearMonth defaultValue, YearMonth value) {
		this(key, defaultValue);
		this.value = value;
	}

}
