/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.ISubmodelImporterExtension;

public class SubmodelImporterRegistry extends
		AbstractRegistry<EClass, ISubmodelImporter> {
	@Inject Iterable<ISubmodelImporterExtension> extensions;
	
	public synchronized ISubmodelImporter getSubmodelImporter(final EClass subModelClass) {
		return get(subModelClass);
	}
	
	@Override
	protected ISubmodelImporter match(EClass key) {
		for (ISubmodelImporterExtension extension : extensions) {
			if (extension.getSubModelClassName().equals(key.getInstanceClass().getCanonicalName())) {
				final String id = extension.getID();
				if (factoryExistsForID(id)) return getFactoryForID(id);
				else return cacheFactoryForID(id, extension.createInstance());
			}
		}
		
		return null;
	}

}
