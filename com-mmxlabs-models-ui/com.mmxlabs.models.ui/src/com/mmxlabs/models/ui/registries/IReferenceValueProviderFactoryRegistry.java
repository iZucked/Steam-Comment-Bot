/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public interface IReferenceValueProviderFactoryRegistry {
	public abstract IReferenceValueProviderFactory getValueProviderFactory(final EClass owner, final EReference reference);
	public abstract IReferenceValueProviderFactory getValueProviderFactory(final EClass owner, final EClass referenceClass);
}