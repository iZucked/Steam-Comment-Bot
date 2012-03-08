/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;

import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

public interface IImporterRegistry {
	public IAttributeImporter getAttributeImporter(final EDataType dataType);
	public ISubmodelImporter getSubmodelImporter(final EClass subModelClass);
	public IClassImporter getClassImporter(final EClass eClass);
}
