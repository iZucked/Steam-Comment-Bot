/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.importer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
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
public class AnalyticsModelImporter implements ISubmodelImporter {

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.util.importer.ISubmodelImporter#getRequiredInputs()
	 */
	@Override
	public Map<String, String> getRequiredInputs() {
		return Collections.emptyMap();
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.util.importer.ISubmodelImporter#importModel(java.util.Map, com.mmxlabs.models.util.importer.IImportContext)
	 */
	@Override
	public UUIDObject importModel(Map<String, CSVReader> inputs, IImportContext context) {
		return AnalyticsFactory.eINSTANCE.createAnalyticsModel();
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.util.importer.ISubmodelImporter#exportModel(com.mmxlabs.models.mmxcore.UUIDObject, java.util.Map)
	 */
	@Override
	public void exportModel(MMXRootObject root, UUIDObject model, Map<String, Collection<Map<String, String>>> output) {
		// TODO Auto-generated method stub

	}

	@Override
	public EClass getEClass() {
		return AnalyticsPackage.eINSTANCE.getAnalyticsModel();
	}
}
