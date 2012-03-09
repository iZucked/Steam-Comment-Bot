/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.extensions.IModelFactoryExtension;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;

public class ModelFactoryRegistry implements IModelFactoryRegistry {
	@Inject Iterable<IModelFactoryExtension> extensions;
	
	public List<IModelFactory> getModelFactories(final EClass targetClass) {
		final ArrayList<IModelFactory> result = new ArrayList<IModelFactory>();
		
		final String targetClassName = targetClass.getInstanceClass().getCanonicalName();
		for (final IModelFactoryExtension extension : extensions) {
			if (extension.getTargetEClass().equals(targetClassName)) {
				final IModelFactory factory = extension.createInstance();
				factory.initFromExtension(extension.getID(), extension.getOutputEClass(), extension.getLabel());
				result.add(factory);
			}
		}
		
		Collections.sort(result, new Comparator<IModelFactory>() {
			@Override
			public int compare(IModelFactory o1, IModelFactory o2) {
				return o1.getLabel().compareTo(o2.getLabel());
			}
		});
		
		return result;
	}
}
