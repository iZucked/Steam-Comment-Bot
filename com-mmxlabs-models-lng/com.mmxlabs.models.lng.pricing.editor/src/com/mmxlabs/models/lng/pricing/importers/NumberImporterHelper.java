/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Helper class for importing market curves values. This will import the full numeric string then apply the appropriate rounding for use internally.
 * 
 * @author Simon Goodall
 * 
 */
public class NumberImporterHelper {

	private final DecimalFormat doubleImportFormatter = new DecimalFormat();
	private final DecimalFormat doubleFormatter = initDoubleFormatter();
	private final DecimalFormat integerImportFormatter = new DecimalFormat();
	private final DecimalFormat integerFormatter = initIntegerFormatter();

	@NonNull
	private DecimalFormat initDoubleFormatter() {

		final String format = "##########.###";
		final DecimalFormat formatter = format == null ? new DecimalFormat() : new DecimalFormat(format);
		formatter.setRoundingMode(RoundingMode.HALF_EVEN);
		return formatter;
	}

	@NonNull
	private DecimalFormat initIntegerFormatter() {

		final String format = "##########";
		final DecimalFormat formatter = format == null ? new DecimalFormat() : new DecimalFormat(format);
		formatter.setParseIntegerOnly(true);
		formatter.setRoundingMode(RoundingMode.HALF_EVEN);
		return formatter;
	}

	/**
	 * Parse the number string, rounding as required.
	 * 
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	@Nullable
	public Number parseDoubleString(final String s) throws ParseException {
		// Read the full number from the string.
		final Number number = doubleImportFormatter.parse(s);
		if (number != null) {
			// Next export to round the number accordingly and then re-import to get a new number object.
			try {
				return doubleFormatter.parse(doubleFormatter.format(number));
			} catch (final ParseException e) {
				// I do not expect this to happen. The parse exception should only happen from the inputFormatter. Here the formatter should export data it can read.
				assert false;
			}
		}
		return null;
	}

	/**
	 * Parse the number string, rounding as required.
	 * 
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	@Nullable
	public Number parseIntegerString(final String s) throws ParseException {
		// Read the full number from the string.
		final Number number = integerImportFormatter.parse(s);
		if (number != null) {
			// Next export to round the number accordingly and then re-import to get a new number object.
			try {
				return integerFormatter.parse(integerFormatter.format(number));
			} catch (final ParseException e) {
				// I do not expect this to happen. The parse exception should only happen from the inputFormatter. Here the formatter should export data it can read.
				assert false;
			}
		}
		return null;
	}

	@Nullable
	public Number parseNumberString(final String valueStr, final boolean asInteger) throws ParseException {

		// This used to be a ? : statement, but for some reason the int (or even Integer) was always stored as a Double
		// @see http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.25
		// @see http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2

		if (asInteger) {
			final Number value = parseIntegerString(valueStr);
			return value.intValue();
		} else {
			final Number value = parseDoubleString(valueStr);
			return value.doubleValue();
		}
	}
}
