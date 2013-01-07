/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * Instances of this are used to convert strings into EAttribute values, and vice-versa
 * @author hinton
 *
 */
public interface IAttributeImporter {
	/**
	 * This method will set the given attribute on the given EObject, using whatever data is in the value string.
	 * @param container
	 * @param attribute
	 * @param value
	 * @param context
	 */
	public void setAttribute(final EObject container, EAttribute attribute, final String value, final IImportContext context);
	/**
	 * This method should turn the given attribute on given container <em>already gotten as the given value</em> into a string,
	 * hopefully inverse to {@link #setAttribute(EObject, EAttribute, String, IImportContext)}
	 * @param container
	 * @param attribute
	 * @param value
	 * @return
	 */
	public String writeAttribute(final EObject container, final EAttribute attribute, final Object value);
}
