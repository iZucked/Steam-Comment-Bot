package com.mmxlabs.models.lng.actuals.importer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IImportContext;

public class ActualsModelExtraImporter implements IExtraModelImporter {

	public static final String ACTUALS_KEY = "ACTUALS";
	final static Map<String, String> inputs = new LinkedHashMap<String, String>();

	private IClassImporter actualsImporter = new ActualsModelImporter();

	static {
		inputs.put(ACTUALS_KEY, "Cargo Actuals");
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IImportContext context) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
			if (portfolioModel != null) {
				CargoModel cargoModel = portfolioModel.getCargoModel();
				if (cargoModel != null) {

					final CSVReader reader = inputs.get(ACTUALS_KEY);
					if (reader != null) {
						final Collection<EObject> importObjects = actualsImporter.importObjects(ActualsPackage.Literals.CARGO_ACTUALS, reader, context);
						final ActualsModel actualsModel = ActualsFactory.eINSTANCE.createActualsModel();
						for (final EObject e : importObjects) {
							if (e instanceof CargoActuals) {
								CargoActuals cargoActuals = (CargoActuals) e;
								actualsModel.getCargoActuals().add(cargoActuals);
							}
						}
						cargoModel.getExtensions().add(actualsModel);
					}
				}
			}
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, IExportContext context) {
		MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
			if (portfolioModel != null) {
				CargoModel cargoModel = portfolioModel.getCargoModel();
				if (cargoModel != null) {
					for (final EObject eObj : cargoModel.getExtensions()) {
						if (eObj instanceof ActualsModel) {
							final ActualsModel actualsModel = (ActualsModel) eObj;
							output.put(ACTUALS_KEY, actualsImporter.exportObjects(actualsModel.getCargoActuals(), context));
						}
					}
				}
			}
		}
	}
}
