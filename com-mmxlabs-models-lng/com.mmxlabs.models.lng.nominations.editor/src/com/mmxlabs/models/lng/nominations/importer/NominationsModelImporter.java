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
import com.mmxlabs.models.lng.nominations.ContractNomination;
import com.mmxlabs.models.lng.nominations.ContractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.SlotNomination;
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

	public static final String SLOT_NOMINATIONS_KEY = "SLOT_NOMINATIONS";
	public static final String SLOT_NOMINATION_SPECS_KEY = "SLOT_NOMINATION_SPECS";
	public static final String CONTRACT_NOMINATIONS_KEY = "CONTRACT_NOMINATIONS";
	public static final String CONTRACT_NOMINATION_SPECS_KEY = "CONTRACT_NOMINATION_SPECS";
	
	final static Map<String, String> inputs = new LinkedHashMap<String, String>();

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter slotNominationsImporter;
	private IClassImporter slotNominationSpecsImporter;
	private IClassImporter contractNominationsImporter;
	private IClassImporter contractNominationSpecsImporter;	
	
	static {
		//These names are also used for forming the filenames of .csv files exported.
		inputs.put(SLOT_NOMINATIONS_KEY, "Slot Nominations");
		inputs.put(SLOT_NOMINATION_SPECS_KEY, "Slot Nomination Specs");
		inputs.put(CONTRACT_NOMINATIONS_KEY, "Contract Nominations");
		inputs.put(CONTRACT_NOMINATION_SPECS_KEY, "Contract Nomination Specs");
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
			slotNominationsImporter = importerRegistry.getClassImporter(NominationsPackage.eINSTANCE.getSlotNomination());
			slotNominationSpecsImporter = importerRegistry.getClassImporter(NominationsPackage.eINSTANCE.getSlotNominationSpec());
			contractNominationsImporter = importerRegistry.getClassImporter(NominationsPackage.eINSTANCE.getContractNomination());
			contractNominationSpecsImporter = importerRegistry.getClassImporter(NominationsPackage.eINSTANCE.getContractNominationSpec());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override	
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final NominationsModel nominationsModel = NominationsModelUtils.createNominationsModel();
		if (inputs.containsKey(SLOT_NOMINATIONS_KEY)) {
			nominationsModel.getSlotNominations().addAll((Collection<? extends SlotNomination>)
					slotNominationsImporter.importObjects(NominationsPackage.eINSTANCE.getSlotNomination(), inputs.get(SLOT_NOMINATIONS_KEY), context));
		}
		if (inputs.containsKey(SLOT_NOMINATION_SPECS_KEY)) {
			nominationsModel.getSlotNominationSpecs().addAll((Collection<? extends SlotNominationSpec>)
					slotNominationSpecsImporter.importObjects(NominationsPackage.eINSTANCE.getSlotNominationSpec(), inputs.get(SLOT_NOMINATION_SPECS_KEY), context));
		}
		if (inputs.containsKey(CONTRACT_NOMINATIONS_KEY)) {
			nominationsModel.getContractNominations().addAll((Collection<? extends ContractNomination>)
			contractNominationsImporter.importObjects(NominationsPackage.eINSTANCE.getContractNomination(), inputs.get(CONTRACT_NOMINATIONS_KEY), context));
		}
		if (inputs.containsKey(CONTRACT_NOMINATION_SPECS_KEY)) {
			nominationsModel.getContractNominationSpecs().addAll((Collection<? extends ContractNominationSpec>)
			contractNominationSpecsImporter.importObjects(NominationsPackage.eINSTANCE.getContractNominationSpec(), inputs.get(CONTRACT_NOMINATION_SPECS_KEY), context));
		}
		return nominationsModel;
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final NominationsModel nm = (NominationsModel) model;
		output.put(SLOT_NOMINATIONS_KEY, slotNominationsImporter.exportObjects(nm.getSlotNominations(), context));
		output.put(SLOT_NOMINATION_SPECS_KEY, slotNominationSpecsImporter.exportObjects(nm.getSlotNominationSpecs(), context));
		output.put(CONTRACT_NOMINATIONS_KEY, contractNominationsImporter.exportObjects(nm.getContractNominations(), context));
		output.put(CONTRACT_NOMINATION_SPECS_KEY, contractNominationSpecsImporter.exportObjects(nm.getContractNominationSpecs(), context));
	}

	@Override
	public EClass getEClass() {
		return NominationsPackage.eINSTANCE.getNominationsModel();
	}
}
