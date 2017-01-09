/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jdt.annotation.NonNull;

public class NumberAttributeImporter {

	private final @NonNull DecimalFormatSymbols decimalFormatSymbols;

	private final @NonNull DecimalFormat integerFormatter;

	private final @NonNull DecimalFormat floatFormatter;

	public NumberAttributeImporter(final char decimalSeparator) {
		this.decimalFormatSymbols = new DecimalFormatSymbols();
		this.decimalFormatSymbols.setDecimalSeparator(decimalSeparator);

		this.integerFormatter = new DecimalFormat("#", decimalFormatSymbols);
		this.floatFormatter = new DecimalFormat("#0.000", decimalFormatSymbols);
	}

	public int stringToInt(final String s, @NonNull final EAttribute attribute) throws ParseException {
		return integerFormatter.parse(s.trim()).intValue();
	}

	public float stringToFloat(final String s, @NonNull final EAttribute attribute) throws ParseException {
		return getFloatFormatterForAttribute(attribute).parse(s.trim()).floatValue();
	}

	public double stringToDouble(final String s, @NonNull final EAttribute attribute) throws ParseException {
		return getFloatFormatterForAttribute(attribute).parse(s.trim()).doubleValue();
	}

	public String intToString(final int i, @NonNull final EAttribute attribute) {
		return integerFormatter.format(i);
	}

	public String floatToString(final float f, @NonNull final EAttribute attribute) {
		return getFloatFormatterForAttribute(attribute).format(f);
	}

	public String doubleToString(final double d, @NonNull final EAttribute attribute) {
		return getFloatFormatterForAttribute(attribute).format(d);
	}

	@NonNull
	public DecimalFormat getFloatFormatterForAttribute(@NonNull final EAttribute attribute) {

		final EAnnotation annotation = attribute.getEAnnotation("http://www.mmxlabs.com/models/ui/numberFormat");
		String format = null;

		if (annotation != null) {
			if (annotation.getDetails().containsKey("exportFormatString")) {
				format = annotation.getDetails().get("exportFormatString");
			}
			if (format == null && annotation.getDetails().containsKey("formatString")) {
				format = annotation.getDetails().get("formatString");
			}
		}

		if (format != null) {
			final DecimalFormat formatter = new DecimalFormat(format, decimalFormatSymbols);
			formatter.setRoundingMode(RoundingMode.HALF_EVEN);
			return formatter;
		}

		return floatFormatter;
	}
}
