/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.importers;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

/**
 * @since 2.0
 */
public class AssignmentModelImporter implements ISubmodelImporter {

	public static final String ASSIGNMENTS = "ASSIGNMENTS";

	// private final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(AssignmentPackage.eINSTANCE.getAssignment());

	private final AssignmentImporter importer = new AssignmentImporter();

	@Override
	public Map<String, String> getRequiredInputs() {
		return CollectionsUtil.makeHashMap(ASSIGNMENTS, "Assignments");
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final AssignmentModel input = AssignmentFactory.eINSTANCE.createAssignmentModel();
		if (inputs.containsKey(ASSIGNMENTS)) {

			// final Collection<EObject> importObjects = importer.importObjects(AssignmentPackage.eINSTANCE.getAssignment(), inputs.get(ASSIGNMENTS), context);
			// input.getAssignments().addAll((Collection<? extends Assignment>) importObjects);
			importer.importAssignments(inputs.get(ASSIGNMENTS), context);
		}

		return input;
	}

	@Override
	public void exportModel(final MMXRootObject root, final UUIDObject model, final Map<String, Collection<Map<String, String>>> output) {

		if (root instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) root;
			final LNGPortfolioModel portfolioModel = scenarioModel.getPortfolioModel();
			output.put(ASSIGNMENTS, importer.exportAssignments(portfolioModel.getAssignmentModel(), portfolioModel.getScenarioFleetModel()));
		}
	}

	@Override
	public EClass getEClass() {
		return AssignmentPackage.eINSTANCE.getAssignmentModel();
	}
}
