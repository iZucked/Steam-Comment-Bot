package com.mmxlabs.models.ui.registries;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.IComponentHelper;

public interface IComponentHelperRegistry {

	public abstract IComponentHelper getComponentHelper(final EClass modelClass);

}