package com.mmxlabs.models.ui.registries;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.modelfactories.IModelFactory;

public interface IModelFactoryRegistry {
	public IModelFactory getModelFactory(final EClass targetEClass);
}
