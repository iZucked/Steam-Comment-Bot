/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;

import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IPostModelImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

public class ImporterRegistry implements IImporterRegistry {
	@Inject
	private AttributeImporterRegistry attributeRegistry;

	@Inject
	private ClassImporterRegistry classRegistry;

	@Inject
	private SubmodelImporterRegistry submodelRegistry;

	@Inject
	private ExtraModelImporterRegistry extramodelRegistry;

	@Inject
	private PostModelImporterRegistry postmodelRegistry;

	@Override
	public IAttributeImporter getAttributeImporter(final EDataType dataType) {
		return attributeRegistry.getAttributeImporter(dataType);
	}

	@Override
	public ISubmodelImporter getSubmodelImporter(final EClass subModelClass) {
		return submodelRegistry.getSubmodelImporter(subModelClass);
	}

	@Override
	public IClassImporter getClassImporter(final EClass eClass) {
		return classRegistry.getClassImporter(eClass);
	}

	/**
	 */
	@Override
	public Collection<ISubmodelImporter> getAllSubModelImporters() {
		return submodelRegistry.getAllSubModelImporters();
	}

	/**
	 */
	@Override
	public Collection<IPostModelImporter> getPostModelImporters() {
		return postmodelRegistry.getPostModelImporters();
	}

	/**
	 */
	@Override
	public Collection<IExtraModelImporter> getExtraModelImporters() {
		return extramodelRegistry.getExtraModelImporters();
	}
}
