/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Additional context for validations; replaces the functionality of the validation support class
 * 
 * @author hinton
 * 
 */
public interface IExtraValidationContext {
	/**
	 * Get the root object wherein the current set of objects are being validated
	 * 
	 * @return
	 */
	public MMXRootObject getRootObject();

	/**
	 * Get the intended container for the given object
	 * 
	 * @param object
	 * @return
	 */
	public EObject getContainer(final EObject object);

	/**
	 * Get the intended containment reference for the given EObject
	 * 
	 * @param object
	 * @return
	 */
	public EReference getContainment(final EObject object);

	/**
	 * Get the intended contents for the given container and containment reference on the container
	 * 
	 * @param container
	 * @param reference
	 * @return
	 */
	public List<EObject> getContents(final EObject container, final EReference reference);

	/**
	 * Get any other EObjects which will be next to the given EObject -- <code>getSiblings(x)</code> is equivalent to {@link #getContents(EObject, EReference) getContents}(
	 * {@link #getContainer(EObject) getContainer(x)}, {@link #getContainment(EObject) getContainment(x)})
	 * 
	 * @param object
	 * @return
	 */
	public List<EObject> getSiblings(final EObject object);

	/**
	 * Return the replacement for this EObject, if there is one.
	 * 
	 * @param object
	 * @return
	 */
	public EObject getReplacement(final EObject object);

	/**
	 * Returns the original EObject for this reference, if there is one.
	 * 
	 * @param object
	 * @return
	 */
	public EObject getOriginal(EObject object);

	/**
	 * Returns true if we are validating a cloned copy of the object rather than the original. This can happen when running validation in a dialog box.
	 * 
	 * @return
	 */
	boolean isValidatingClone();
}
