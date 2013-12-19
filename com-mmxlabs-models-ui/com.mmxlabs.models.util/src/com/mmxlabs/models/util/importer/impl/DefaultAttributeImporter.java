/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.text.ParseException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IImportContext;

public class DefaultAttributeImporter implements IAttributeImporter {

	private static final String HTML_AMPERSAND = "&#38;";
	private static final String HTML_COMMA = "&#44;";

	@Override
	public void setAttribute(final EObject container, final EAttribute attribute, final String value, final IImportContext context) {
		if (attribute.isMany()) {
			final String[] values = value.split(",");
			final EList eValues = (EList) container.eGet(attribute);
			for (final String v : values) {
				if (v.trim().isEmpty()) {
					continue;
				}

				// Simple (de)encoding - should expand into something more robust
				final String decodedValue = v.trim().replaceAll(HTML_COMMA, ",").replaceAll(HTML_AMPERSAND, "&");

				eValues.add(attributeFromString(container, attribute, decodedValue, context));
			}
		} else {
			if (attribute.isUnsettable() && value.trim().isEmpty()) {
				container.eUnset(attribute);
			} else if (!value.isEmpty()) {
				final Object oValue = attributeFromString(container, attribute, value, context);
				if (oValue != null)
					container.eSet(attribute, oValue);
			}
		}
	}

	@Override
	public String writeAttribute(final EObject container, final EAttribute attribute, final Object value, final IExportContext context) {
		if (attribute.isMany()) {
			final EList<?> eValues = (EList<?>) container.eGet(attribute);
			final StringBuffer result = new StringBuffer();
			boolean comma = false;
			for (final Object o : eValues) {
				if (o == null) {
					continue;
				}

				if (comma) {
					result.append(",");
				}
				comma = true;
				final String rawValue = stringFromAttribute(container, attribute, o, context);
				// Simple (de)encoding - should expand into something more robust
				final String encodedValue = rawValue.replaceAll("&", HTML_AMPERSAND).replaceAll(",", HTML_COMMA);

				result.append(encodedValue);
			}
			return result.toString();
		} else {
			return stringFromAttribute(container, attribute, value, context);
		}
	}

	protected Object attributeFromString(final EObject container, final EAttribute attribute, final String value, final IImportContext context) {
		final EDataType dt = attribute.getEAttributeType();
		try {

			if (isNumberDataType(dt)) {
				return importNumberDataType(dt, value, context);
			} else {
				return dt.getEPackage().getEFactoryInstance().createFromString(dt, value);
			}
		} catch (final Exception ex) {
			context.addProblem(context.createProblem("Error parsing value " + value + " for " + attribute.getName() + ":" + ex.toString(), true, true, true));
		}
		return null;
	}

	protected String stringFromAttribute(final EObject container, final EAttribute attribute, final Object value, final IExportContext context) {
		final EDataType dt = attribute.getEAttributeType();

		if (attribute.isUnsettable() && container.eIsSet(attribute) == false) {
			return "";
		}

		if (isNumberDataType(dt)) {
			return exportNumberDataType(dt, value, context);
		} else {
			return attribute.getEAttributeType().getEPackage().getEFactoryInstance().convertToString(attribute.getEAttributeType(), value);
		}
	}

	protected boolean isNumberDataType(final EDataType dt) {
		if (dt == EcorePackage.Literals.EBIG_DECIMAL || dt == EcorePackage.Literals.EBIG_INTEGER || dt == EcorePackage.Literals.ESHORT || dt == EcorePackage.Literals.ESHORT_OBJECT) {
			throw new UnsupportedOperationException("Unable to handle this numerical data type: " + dt.getName());
		}

		if (dt == EcorePackage.Literals.EDOUBLE || dt == EcorePackage.Literals.EDOUBLE_OBJECT || dt == EcorePackage.Literals.EINT || dt == EcorePackage.Literals.EINTEGER_OBJECT
				|| dt == EcorePackage.Literals.EFLOAT || dt == EcorePackage.Literals.EFLOAT_OBJECT) {
			return true;
		}
		return false;
	}

	protected Object importNumberDataType(final EDataType dt, final String value, final IImportContext context) throws ParseException {

		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());
		if (dt == EcorePackage.Literals.EINT || dt == EcorePackage.Literals.EINTEGER_OBJECT) {
			return nai.stringToInt(value);
		} else if (dt == EcorePackage.Literals.EFLOAT || dt == EcorePackage.Literals.EFLOAT_OBJECT) {
			return nai.stringToFloat(value);
		} else if (dt == EcorePackage.Literals.EDOUBLE || dt == EcorePackage.Literals.EDOUBLE_OBJECT) {
			return nai.stringToDouble(value);
		}

		return null;
	}

	protected String exportNumberDataType(final EDataType dt, final Object value, final IExportContext context) {

		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());
		if (dt == EcorePackage.Literals.EINT || dt == EcorePackage.Literals.EINTEGER_OBJECT) {
			return nai.intToString((Integer) value);
		} else if (dt == EcorePackage.Literals.EFLOAT || dt == EcorePackage.Literals.EFLOAT_OBJECT) {
			return nai.floatToString((Float) value);
		} else if (dt == EcorePackage.Literals.EDOUBLE || dt == EcorePackage.Literals.EDOUBLE_OBJECT) {
			return nai.doubleToString((Double) value);
		}

		return null;
	}
}
