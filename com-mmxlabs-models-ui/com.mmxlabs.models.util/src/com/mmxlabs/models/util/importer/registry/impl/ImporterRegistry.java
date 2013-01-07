/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;

import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

public class ImporterRegistry implements IImporterRegistry {
	@Inject AttributeImporterRegistry attributeRegistry;
	@Inject ClassImporterRegistry classRegistry;
	@Inject SubmodelImporterRegistry submodelRegistry;
	
	@Override
	public IAttributeImporter getAttributeImporter(EDataType dataType) {
		return attributeRegistry.getAttributeImporter(dataType);
	}

	@Override
	public ISubmodelImporter getSubmodelImporter(EClass subModelClass) {
		return submodelRegistry.getSubmodelImporter(subModelClass);
	}

	@Override
	public IClassImporter getClassImporter(EClass eClass) {
		return classRegistry.getClassImporter(eClass);
	}
	
	/**
	 * @since 2.0
	 */
	@Override
	public Collection<ISubmodelImporter> getAllSubModelImporters() {
		return submodelRegistry.getAllSubModelImporters();
	}
}
