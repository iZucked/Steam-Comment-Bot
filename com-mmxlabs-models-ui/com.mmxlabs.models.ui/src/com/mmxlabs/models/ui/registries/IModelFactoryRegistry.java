/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries;

import java.util.List;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.modelfactories.IModelFactory;

public interface IModelFactoryRegistry {
	public List<IModelFactory> getModelFactories(final EClass targetEClass);
}
