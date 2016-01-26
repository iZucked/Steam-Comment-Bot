/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;

public interface IDisplayCompositeFactoryRegistry {

	public abstract IDisplayCompositeFactory getDisplayCompositeFactory(
			final EClass displayedClass);

}