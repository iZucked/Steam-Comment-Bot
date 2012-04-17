/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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

	@Override
	public void setAttribute(EObject container, final EAttribute attribute,
			String value, IImportContext context) {
		if (attribute.isMany()) {
			final String[] values = value.split(",");
			final EList eValues = (EList) container.eGet(attribute);
			for (final String v : values) {
				if (v.trim().isEmpty()) continue;
				eValues.add(attributeFromString(container, attribute, v.trim(),
						context));
			}
		} else {
			if (attribute.isUnsettable() && value.trim().isEmpty()) {
				container.eUnset(attribute);
			} else if (!value.isEmpty()) {
				final Object oValue = attributeFromString(container, attribute, value,
						context);
				if (oValue != null) container.eSet(attribute, oValue);
			}
		}
	}

	@Override
	public String writeAttribute(EObject container, EAttribute attribute,
			Object value) {
		if (attribute.isMany()) {
			final EList eValues = (EList) container.eGet(attribute);
			final StringBuffer result = new StringBuffer();
			boolean comma = false;
			for (final Object o : eValues) {
				if (comma)
					result.append(",");
				comma = true;
				result.append(stringFromAttribute(container, attribute, o));
			}
			return result.toString();
		} else {
			return stringFromAttribute(container, attribute, value);
		}
	}

	protected Object attributeFromString(final EObject container,
			final EAttribute attribute, final String value,
			final IImportContext context) {
		final EDataType dt = attribute.getEAttributeType();
		try {
			return dt.getEPackage().getEFactoryInstance().createFromString(dt, value);
		} catch (Exception ex) {
			context.addProblem(context.createProblem("Error parsing value " + value + " for " + attribute.getName() + ":" + ex.toString(), true, true, true));
		}
		return null;
	}

	protected String stringFromAttribute(final EObject container,
			final EAttribute attribute, final Object value) {
		if (attribute.isUnsettable() && container.eIsSet(attribute) == false) return "";
		return attribute.getEAttributeType().getEPackage().getEFactoryInstance().convertToString(attribute.getEAttributeType(), value);
	}
}
