/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

/**
 */
public class ScheduleModelImporter implements ISubmodelImporter {

	@Override
	public Map<String, String> getRequiredInputs() {
		return Collections.emptyMap();
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final ScheduleModel commercial = ScheduleFactory.eINSTANCE.createScheduleModel();
		return commercial;
	}

	@Override
	public void exportModel(final UUIDObject model, final Map<String, Collection<Map<String, String>>> output, final IExportContext context) {

	}

	@Override
	public EClass getEClass() {
		return SchedulePackage.eINSTANCE.getScheduleModel();
	}
}
