/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.importers;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

/**
 * @since 2.0
 */
public class InputModelImporter implements ISubmodelImporter {
	
	public  static final String ASSIGNMENTS = "ASSIGNMENTS";
	
//	private final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(InputPackage.eINSTANCE.getAssignment());
	
	private final AssignmentImporter importer = new AssignmentImporter();
	
	@Override
	public Map<String, String> getRequiredInputs() {
		return CollectionsUtil.makeHashMap(ASSIGNMENTS, "Assignments");
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final InputModel input = InputFactory.eINSTANCE.createInputModel();
		if (inputs.containsKey(ASSIGNMENTS)) {
			
//			final Collection<EObject> importObjects = importer.importObjects(InputPackage.eINSTANCE.getAssignment(), inputs.get(ASSIGNMENTS), context);
//			input.getAssignments().addAll((Collection<? extends Assignment>) importObjects);
			importer.importAssignments(inputs.get(ASSIGNMENTS), context);
		}
		
		return input;
	}

	@Override
	public void exportModel(final MMXRootObject root, final UUIDObject model, final Map<String, Collection<Map<String, String>>> output) {
//		output.put(ASSIGNMENTS, importer.exportObjects(((InputModel) model).getAssignments(), root));
		output.put(ASSIGNMENTS, importer.exportAssignments(root.getSubModel(InputModel.class), root.getSubModel(FleetModel.class)));
	}
	
	@Override
	public EClass getEClass() {
		return InputPackage.eINSTANCE.getInputModel();
	}
}
