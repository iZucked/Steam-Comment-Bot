/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

/**
 * Instances of this are used to convert strings into EAttribute values, and vice-versa
 * 
 * @author hinton
 * 
 */
public interface IAttributeImporter {
	/**
	 * This method will set the given attribute on the given EObject, using whatever data is in the value string.
	 * 
	 * @param container
	 * @param attribute
	 * @param value
	 * @param context
	 */
	void setAttribute(@NonNull EObject container, @NonNull EAttribute attribute, @NonNull String value, @NonNull IMMXImportContext context);

	/**
	 * This method should turn the given attribute on given container <em>already gotten as the given value</em> into a string, hopefully inverse to
	 * {@link #setAttribute(EObject, EAttribute, String, IMMXImportContext)}
	 * 
	 * @param container
	 * @param attribute
	 * @param value
	 * @return
	 */
	String writeAttribute(@NonNull EObject container, @NonNull EAttribute attribute, @NonNull Object value, @NonNull IMMXExportContext context);
}
