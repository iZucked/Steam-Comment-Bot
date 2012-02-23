package com.mmxlabs.models.util.importer.registry.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;

import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

public class ImporterRegistry implements IImporterRegistry {
	
	@Override
	public IAttributeImporter getAttributeImporter(EDataType dataType) {
		return null;
	}

	@Override
	public ISubmodelImporter getSubmodelImporter(EClass subModelClass) {
		return null;
	}

	@Override
	public IClassImporter getClassImporter(EClass eClass) {
		return null;
	}

}
