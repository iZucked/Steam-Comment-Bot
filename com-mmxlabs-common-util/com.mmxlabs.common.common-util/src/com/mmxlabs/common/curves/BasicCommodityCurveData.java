/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Basic data for commodity curve
 * @author FM
 *
 */
@NonNullByDefault
public class BasicCommodityCurveData {
	private String name;
	private String volumeUnit;
	private String currencyUnit;
	private @Nullable String expression;

	public BasicCommodityCurveData(final String name, final String volumeUnit, final String currencyUnit, final @Nullable String expression) {
		this.name = name;
		this.volumeUnit = volumeUnit;
		this.currencyUnit = currencyUnit;
		this.expression = expression;
	}
	
	public String getName() {
		return name;
	}

	public String getVolumeUnit() {
		return volumeUnit;
	}

	public String getCurrencyUnit() {
		return currencyUnit;
	}

	@Nullable
	public String getExpression() {
		return expression;
	}

	public boolean isSetExpression() {
		return (this.expression != null);
	}
}
