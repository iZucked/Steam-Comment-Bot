/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.importer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

/**
 * @author hinton
 * 
 */
public class AnalyticsModelImporter implements ISubmodelImporter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.util.importer.ISubmodelImporter#getRequiredInputs()
	 */
	@Override
	public Map<String, String> getRequiredInputs() {
		return Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.util.importer.ISubmodelImporter#importModel(java.util.Map, com.mmxlabs.models.util.importer.IImportContext)
	 */
	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		return AnalyticsFactory.eINSTANCE.createAnalyticsModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.util.importer.ISubmodelImporter#exportModel(com.mmxlabs.models.mmxcore.UUIDObject, java.util.Map)
	 */
	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {

	}

	@Override
	public EClass getEClass() {
		return AnalyticsPackage.eINSTANCE.getAnalyticsModel();
	}
}
