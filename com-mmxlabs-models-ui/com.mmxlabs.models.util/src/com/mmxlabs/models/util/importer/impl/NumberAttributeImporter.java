/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class NumberAttributeImporter {

	private final DecimalFormatSymbols decimalFormatSymbols;

	private final DecimalFormat integerFormatter;

	private final DecimalFormat floatFormatter;

	public NumberAttributeImporter(final char decimalSeparator) {
		this.decimalFormatSymbols = new DecimalFormatSymbols();
		this.decimalFormatSymbols.setDecimalSeparator(decimalSeparator);

		this.integerFormatter = new DecimalFormat("#", decimalFormatSymbols);
		this.floatFormatter = new DecimalFormat("#0.000", decimalFormatSymbols);
	}

	public int stringToInt(final String s) throws ParseException {
		return integerFormatter.parse(s.trim()).intValue();
	}

	public float stringToFloat(final String s) throws ParseException {
		return floatFormatter.parse(s.trim()).floatValue();
	}

	public double stringToDouble(final String s) throws ParseException {
		return floatFormatter.parse(s.trim()).doubleValue();
	}

	public String intToString(final int i) {
		return integerFormatter.format(i);
	}

	public String floatToString(final float f) {
		return floatFormatter.format(f);
	}

	public String doubleToString(final double d) {
		return floatFormatter.format(d);
	}

}
