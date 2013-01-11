/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.optimiser.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.optimiser.OptimiserFactory;
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

/**
 * @author hinton
 * @since 2.0
 *
 */
public class OptimiserModelImporter implements ISubmodelImporter {

	@Override
	public Map<String, String> getRequiredInputs() {
		return Collections.emptyMap();
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.util.importer.ISubmodelImporter#importModel(java.util.Map, com.mmxlabs.models.util.importer.IImportContext)
	 */
	@Override
	public UUIDObject importModel(Map<String, CSVReader> inputs, IImportContext context) {
		final OptimiserModel model = OptimiserFactory.eINSTANCE.createOptimiserModel();
		
		
		
		return model;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.util.importer.ISubmodelImporter#exportModel(com.mmxlabs.models.mmxcore.UUIDObject, java.util.Map)
	 */
	@Override
	public void exportModel(MMXRootObject root, UUIDObject model, Map<String, Collection<Map<String, String>>> output) {
		
		
	}
	
	@Override
	public EClass getEClass() {
		return OptimiserPackage.eINSTANCE.getOptimiserModel();
	}
}
