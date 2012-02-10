/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;

import scenario.ScenarioPackage;

public final class NumberTypes {
	public final static EDataType l = EcorePackage.eINSTANCE.getELong();
	public final static EDataType f = EcorePackage.eINSTANCE.getEFloat();
	public final static EDataType d = EcorePackage.eINSTANCE.getEDouble();
	public final static EDataType i = EcorePackage.eINSTANCE.getEInt();
	public final static EDataType p = ScenarioPackage.eINSTANCE.getPercentage();

	public static NumberFormat getFormatter(final EDataType type) {
		final NumberFormat formatter;

		if (type == l) {
			formatter = DecimalFormat.getIntegerInstance();
		} else if (type == i) {
			formatter = DecimalFormat.getIntegerInstance();
		} else if (type == p) {
			formatter = DecimalFormat.getNumberInstance();
		} else if (type == f) {
			formatter = DecimalFormat.getNumberInstance();
		} else if (type == d) {
			formatter = DecimalFormat.getNumberInstance();
		} else {
			throw new RuntimeException("Unknown type of numeric field");
		}

		formatter.setMaximumFractionDigits(getDigits(type));
		return formatter;
	}

	public static Object toNumber(final EDataType type, final String textValue) throws ParseException {

		final NumberFormat formatter = getFormatter(type);

		Object num = formatter.parse(textValue);

		if (type == p) {
			if (num instanceof Number) {
				double d = ((Number) num).doubleValue();
				d /= 100.0;
				return d;
			}
		}

		// Convert formatter output to required type
		if (type == i) {
			num = Integer.valueOf(((Number) num).intValue());
		} else if (type == i) {
			num = Long.valueOf(((Number) num).longValue());
		} else if (type == f) {
			num = Float.valueOf(((Number) num).floatValue());
		} else if (type == f) {
			num = Double.valueOf(((Number) num).doubleValue());
		}

		return num;
	}

	public static String toString(final EDataType type, final Object value) {

		final NumberFormat formatter = getFormatter(type);

		if (type == p) {
			if (value instanceof Number) {
				double d = ((Number) value).doubleValue();
				d *= 100.0;
				return formatter.format(d);
			}
		}
		return formatter.format(value);
	}

	public static int getDigits(final EDataType type) {
		if ((type == l) || (type == i)) {
			return 0;
		} else if ((type == f) || (type == d)) {
			return 2;
		} else if (type == p) {
			return 1;
		} else {
			return 0;
		}
	}

	public static Object getDefaultValue(final EDataType type) {
		if (type == i) {
			return 0;
		}
		if (type == l) {
			return 0l;
		}
		if ((type == d) || (type == p)) {
			return 0d;
		}
		if (type == f) {
			return 0f;
		}

		throw new RuntimeException("Unknown type for number : " + type.getName());
	}
}