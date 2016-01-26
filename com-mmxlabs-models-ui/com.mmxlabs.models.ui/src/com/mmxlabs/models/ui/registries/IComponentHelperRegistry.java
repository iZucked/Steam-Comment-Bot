/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries;

import java.util.List;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.IComponentHelper;

public interface IComponentHelperRegistry {

	public abstract List<IComponentHelper> getComponentHelpers(final EClass modelClass);

}