/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.ISubmodelImporterExtension;

public class SubmodelImporterRegistry extends AbstractRegistry<EClass, ISubmodelImporter> {

	@Inject
	private Iterable<ISubmodelImporterExtension> extensions;

	public synchronized ISubmodelImporter getSubmodelImporter(final EClass subModelClass) {
		return get(subModelClass);
	}

	@Override
	protected ISubmodelImporter match(final EClass key) {
		for (final ISubmodelImporterExtension extension : extensions) {
			if (extension.getSubModelClassName().equals(key.getInstanceClass().getCanonicalName())) {
				final String id = extension.getID();
				if (factoryExistsForID(id)) {
					return getFactoryForID(id);
				} else {
					return cacheFactoryForID(id, extension.createInstance());
				}
			}
		}

		return null;
	}

	/**
	 * Return all the submodel importer instances.
	 */
	public Collection<ISubmodelImporter> getAllSubModelImporters() {
		final List<ISubmodelImporter> l = new ArrayList<ISubmodelImporter>();
		for (final ISubmodelImporterExtension extension : extensions) {
			final String id = extension.getID();
			if (factoryExistsForID(id)) {
				l.add(getFactoryForID(id));
			} else {
				l.add(cacheFactoryForID(id, extension.createInstance()));
			}
		}

		return l;
	}
}
