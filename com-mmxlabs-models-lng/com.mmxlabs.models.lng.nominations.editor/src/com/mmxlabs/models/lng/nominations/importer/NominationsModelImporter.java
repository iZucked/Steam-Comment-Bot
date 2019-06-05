package com.mmxlabs.models.lng.nominations.importer;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.SlotNominationSpec;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

public class NominationsModelImporter implements ISubmodelImporter {

	public static final String NOMINATIONS_KEY = "NOMINATIONS";
	public static final String NOMINATION_SPECS_KEY = "NOMINATION_SPECS";
	
	final static Map<String, String> inputs = new LinkedHashMap<String, String>();

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter nominationsImporter;
	private IClassImporter nominationSpecsImporter;
	
	static {
		//These names are also used for forming the filenames of .csv files exported.
		inputs.put(NOMINATIONS_KEY, "Nominations");
		inputs.put(NOMINATION_SPECS_KEY, "Nomination Specs");
	}
	
	public NominationsModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {
			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			nominationsImporter = importerRegistry.getClassImporter(NominationsPackage.eINSTANCE.getAbstractNomination());
			nominationSpecsImporter = importerRegistry.getClassImporter(NominationsPackage.eINSTANCE.getAbstractNominationSpec());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override	
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final NominationsModel nominationsModel = NominationsModelUtils.createNominationsModel();
		if (inputs.containsKey(NOMINATIONS_KEY)) {
			//Check the kind column.
			nominationsModel.getNominations().addAll((Collection<? extends AbstractNomination>)
					nominationsImporter.importObjects(NominationsPackage.eINSTANCE.getAbstractNomination(), inputs.get(NOMINATIONS_KEY), context));
		}
		if (inputs.containsKey(NOMINATION_SPECS_KEY)) {
			nominationsModel.getNominationSpecs().addAll((Collection<? extends SlotNominationSpec>)
					nominationSpecsImporter.importObjects(NominationsPackage.eINSTANCE.getSlotNominationSpec(), inputs.get(NOMINATION_SPECS_KEY), context));
		}
		return nominationsModel;
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final NominationsModel nm = (NominationsModel) model;
		output.put(NOMINATIONS_KEY, nominationsImporter.exportObjects(nm.getNominations(), context));
		output.put(NOMINATION_SPECS_KEY, nominationSpecsImporter.exportObjects(nm.getNominationSpecs(), context));
	}

	@Override
	public EClass getEClass() {
		return NominationsPackage.eINSTANCE.getNominationsModel();
	}
}
