/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EDataType;

import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.registry.IAttributeImporterExtension;
import com.mmxlabs.models.util.importer.registry.IAttributeImporterExtension.IEDataTypeMatch;

public class AttributeImporterRegistry extends AbstractRegistry<EDataType, IAttributeImporter> {
	@Inject Iterable<IAttributeImporterExtension> extensions;
	@Override
	protected IAttributeImporter match(EDataType key) {
		final String uri = key.getEPackage().getNsURI();
		final String name = key.getName();
		for (final IAttributeImporterExtension extension : extensions) {
			for (final IEDataTypeMatch match : extension.getDataTypes()) {
				if (match.getAttributeTypeURI().equals(uri) && match.getAttributeTypeName().equals(name)) {
					final String id = extension.getID();
					if (factoryExistsForID(id)) return getFactoryForID(id);
					else return cacheFactoryForID(id, extension.createImporter());
				}
			}
		}
		return null;
	}
	public synchronized IAttributeImporter getAttributeImporter(EDataType dataType) {
		return get(dataType);
	}
}
