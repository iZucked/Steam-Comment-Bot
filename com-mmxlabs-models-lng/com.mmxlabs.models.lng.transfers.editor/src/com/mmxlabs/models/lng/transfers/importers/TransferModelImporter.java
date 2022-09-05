package com.mmxlabs.models.lng.transfers.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersFactory;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

public class TransferModelImporter implements ISubmodelImporter {

	public static final String TRANSFER_AGREEMENT_KEY = "TRANSFER_AGREEMENTS";
	public static final String TRANSFER_RECORD_KEY = "TRANSFER_RECORDS";
	
	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter transferAgreementImporter;
	private IClassImporter transferRecordsImporter;
	
	public TransferModelImporter(){
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}
	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			transferAgreementImporter = importerRegistry.getClassImporter(TransfersPackage.eINSTANCE.getTransferAgreement());
			transferRecordsImporter = importerRegistry.getClassImporter(TransfersPackage.eINSTANCE.getTransferRecord());
		}
	}
	
	@Override
	public @NonNull EClass getEClass() {
		return TransfersPackage.eINSTANCE.getTransferModel();
	}

	@Override
	public @NonNull Map<String, String> getRequiredInputs() {
		final HashMap<String, String> inputs = new HashMap<>();
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL)) {
			inputs.put(TRANSFER_AGREEMENT_KEY, "Transfer Agreements");
			inputs.put(TRANSFER_RECORD_KEY, "Transfer Records");
		}
		return inputs;
	}

	@Override
	public UUIDObject importModel(@NonNull Map<String, CSVReader> inputs, @NonNull IMMXImportContext context) {
		final TransferModel transfers = TransfersFactory.eINSTANCE.createTransferModel();
		if (inputs.containsKey(TRANSFER_AGREEMENT_KEY)) {
			transfers.getTransferAgreements().addAll((Collection<? extends TransferAgreement>) transferAgreementImporter.importObjects(TransfersPackage.eINSTANCE.getTransferAgreement(), inputs.get(TRANSFER_AGREEMENT_KEY), context));
		}
		if (inputs.containsKey(TRANSFER_RECORD_KEY)) {
			transfers.getTransferRecords().addAll((Collection<? extends TransferRecord>) transferRecordsImporter.importObjects(TransfersPackage.eINSTANCE.getTransferRecord(), inputs.get(TRANSFER_RECORD_KEY), context));
		}
		return transfers;
	}

	@Override
	public void exportModel(@NonNull EObject model, @NonNull Map<String, Collection<Map<String, String>>> output, @NonNull IMMXExportContext context) {
		final TransferModel transfers = (TransferModel) model;
		output.put(TRANSFER_AGREEMENT_KEY, transferAgreementImporter.exportObjects(transfers.getTransferAgreements(), context));
		output.put(TRANSFER_RECORD_KEY, transferRecordsImporter.exportObjects(transfers.getTransferRecords(), context));
	}

}
