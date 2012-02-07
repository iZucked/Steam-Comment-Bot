/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.base;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * An interface which provides value providers given an EClass.
 * 
 * @author hinton
 * 
 */
public interface IValueProviderProvider {
	public IReferenceValueProvider getValueProvider(final EClass referenceClass);

	public EObject getModel();
}
