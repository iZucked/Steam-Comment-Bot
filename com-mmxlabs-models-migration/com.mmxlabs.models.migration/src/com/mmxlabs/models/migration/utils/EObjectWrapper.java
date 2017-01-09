/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.utils;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Extension to the normal {@link EObject} interface defining various function to help navigate an EMF data model during a model migration process.
 * 
 * @author Simon Goodall
 */

public interface EObjectWrapper extends EObject {

	/**
	 * Find the named {@link EReference} and return the result.
	 * 
	 * @param name
	 * @return
	 */
	EObjectWrapper getRef(String name);

	/**
	 * Find the named {@link EReference} and return the result as a {@link List}.
	 * 
	 * @param name
	 * @return
	 */
	List<EObjectWrapper> getRefAsList(String name);

	/**
	 * Find the named {@link EAttribute} and return the result.
	 * 
	 * @param name
	 * @return
	 */
	<T> T getAttrib(String name);

	/**
	 * Find the named {@link EAttribute} and return the result at a typed {@link List}.
	 * 
	 * @param name
	 * @return
	 */
	<T> List<T> getAttribAsList(String name);

	/**
	 * Find the named {@link EReference} and set the value. This can be used for both single values and {@link List} values.
	 * 
	 * @param name
	 * @param value
	 */
	void setRef(String name, Object value);

	/**
	 * Find the named {@link EAttribute} and set the value. This can be used for both single values and {@link List} values.
	 * 
	 * @param name
	 * @param value
	 */
	void setAttrib(String name, Object value);

	/**
	 * Find the named {@link EAttribute} and return it as a {@link Boolean}
	 * 
	 * @param name
	 * @return
	 */
	Boolean getAttribAsBooleanObject(String name);

	/**
	 * Find the named {@link EAttribute} and return it as a boolean primitive.
	 * 
	 * @param name
	 * @return
	 */
	boolean getAttribAsBoolean(String name);

	void unsetFeature(String name);

	boolean isSetFeature(String name);

}
