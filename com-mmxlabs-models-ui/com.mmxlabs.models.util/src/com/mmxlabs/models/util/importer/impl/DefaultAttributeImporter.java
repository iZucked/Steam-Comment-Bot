/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.util.importer.IAttributeImporter;
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
	public String writeAttribute(final EObject container, final EAttribute attribute, final Object value) {
		if (attribute.isMany()) {
			final EList<?> eValues = (EList<?>) container.eGet(attribute);
			final StringBuffer result = new StringBuffer();
			boolean comma = false;
			for (final Object o : eValues) {
				if (comma) {
					result.append(",");
				}
				comma = true;
				final String rawValue = stringFromAttribute(container, attribute, o);
				// Simple (de)encoding - should expand into something more robust
				final String encodedValue = rawValue.replaceAll("&", HTML_AMPERSAND).replaceAll(",", HTML_COMMA);

				result.append(encodedValue);
			}
			return result.toString();
		} else {
			return stringFromAttribute(container, attribute, value);
		}
	}

	protected Object attributeFromString(final EObject container, final EAttribute attribute, final String value, final IImportContext context) {
		final EDataType dt = attribute.getEAttributeType();
		try {
			return dt.getEPackage().getEFactoryInstance().createFromString(dt, value);
		} catch (final Exception ex) {
			context.addProblem(context.createProblem("Error parsing value " + value + " for " + attribute.getName() + ":" + ex.toString(), true, true, true));
		}
		return null;
	}

	protected String stringFromAttribute(final EObject container, final EAttribute attribute, final Object value) {
		if (attribute.isUnsettable() && container.eIsSet(attribute) == false)
			return "";
		return attribute.getEAttributeType().getEPackage().getEFactoryInstance().convertToString(attribute.getEAttributeType(), value);
	}
}
