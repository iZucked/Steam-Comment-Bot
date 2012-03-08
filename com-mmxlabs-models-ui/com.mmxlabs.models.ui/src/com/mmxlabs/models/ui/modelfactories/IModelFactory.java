/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.modelfactories;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IModelFactory {
	public EObject instantiate(final MMXRootObject rootObject, final EClass objectClass);
}
