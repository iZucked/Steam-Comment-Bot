/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.importer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class ActualsModelExtraImporter implements IExtraModelImporter {

	public static final String ACTUALS_KEY = "ACTUALS";
	final static Map<String, String> inputs = new LinkedHashMap<String, String>();

	private ActualsModelImporter actualsImporter = new ActualsModelImporter();

	static {
		inputs.put(ACTUALS_KEY, "Cargo Actuals");
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

			final CSVReader reader = inputs.get(ACTUALS_KEY);
			if (reader != null) {
				final Collection<EObject> importObjects = actualsImporter.importObjects(ActualsPackage.Literals.CARGO_ACTUALS, reader, context);
				final ActualsModel actualsModel = ActualsFactory.eINSTANCE.createActualsModel();
				for (final EObject e : importObjects) {
					if (e instanceof CargoActuals) {
						CargoActuals cargoActuals = (CargoActuals) e;
						actualsModel.getCargoActuals().add(cargoActuals);
						
						if (cargoActuals.getReturnActuals() == null) {
							cargoActuals.setReturnActuals(ActualsFactory.eINSTANCE.createReturnActuals());
						}
					}
				}
				lngScenarioModel.setActualsModel(actualsModel);
			}
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, IMMXExportContext context) {
		MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final ActualsModel actualsModel = lngScenarioModel.getActualsModel();
			if (actualsModel != null) {
				output.put(ACTUALS_KEY, actualsImporter.exportActuals(actualsModel.getCargoActuals(), context));
			}
		}
	}
}
