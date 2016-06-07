/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.Map;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

/**
 */
public class AssignmentModelImporter implements IExtraModelImporter {

	public static final String ASSIGNMENTS = "ASSIGNMENTS";

	private final AssignmentImporter importer = new AssignmentImporter();

	@Override
	public Map<String, String> getRequiredInputs() {
		return CollectionsUtil.makeHashMap(ASSIGNMENTS, "Assignments");
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		if (inputs.containsKey(ASSIGNMENTS)) {
			importer.importAssignments(inputs.get(ASSIGNMENTS), context);
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final SpotMarketsModel spotMarketsModel = lngScenarioModel.getReferenceModel().getSpotMarketsModel();
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

			if (cargoModel != null) {
				output.put(ASSIGNMENTS, importer.exportObjects(cargoModel, portModel, spotMarketsModel, context));
			}
		}
	}
}
