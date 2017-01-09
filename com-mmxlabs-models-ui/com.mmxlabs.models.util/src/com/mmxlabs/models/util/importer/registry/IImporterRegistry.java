/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;

import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IPostModelImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

public interface IImporterRegistry {
	public IAttributeImporter getAttributeImporter(final EDataType dataType);

	public ISubmodelImporter getSubmodelImporter(final EClass subModelClass);

	public IClassImporter getClassImporter(final EClass eClass);

	/**
	 */
	public Collection<IExtraModelImporter> getExtraModelImporters();

	/**
	 */
	public Collection<IPostModelImporter> getPostModelImporters();

	/**
	 * 
	 * @return
	 */
	public Collection<ISubmodelImporter> getAllSubModelImporters();
}
