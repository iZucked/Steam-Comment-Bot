/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.registry.IClassImporterExtension;

public class ClassImporterRegistry extends
		AbstractRegistry<EClass, IClassImporter> {
	@Inject Iterable<IClassImporterExtension> extensions;
	
	public synchronized IClassImporter getClassImporter(final EClass key) {
		return get(key);
	}
	
	@Override
	protected IClassImporter match(EClass key) {
		int bestMatch = Integer.MAX_VALUE;
		IClassImporterExtension bestExtension = null;
		
		for (final IClassImporterExtension extension : extensions) {
			final int match = getMinimumGenerations(key, extension.getEClassName());
			if (match == 0) {
				bestExtension = extension;
				break;
			} else if (match < bestMatch && Boolean.parseBoolean(extension.isInheritable())) {
				bestMatch = match;
				bestExtension = extension;
			}
		}
		
		if (bestExtension == null) return null;
		final String id = bestExtension.getID();
		if (factoryExistsForID(id)) return getFactoryForID(id);
		else return cacheFactoryForID(id, bestExtension.createImporter());
	}

}
