package com.mmxlabs.lngdataserver.lng.importers.vesselsimport.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ImportSingleVesselWizard extends Wizard implements IImportWizard {

	private ModelReference modelReference;
	private VesselsVersion vesselRecords;
	private ImportSingleVesselPage importSingleVesselPage;

	public ImportSingleVesselWizard(ModelReference modelReference, VesselsVersion vesselRecords) {
		super();
		this.modelReference = modelReference;
		this.vesselRecords = vesselRecords;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Import vessel from LiNGO DB");
		this.setNeedsProgressMonitor(true);
		setForcePreviousAndNextButtons(false);
		importSingleVesselPage = new ImportSingleVesselPage("Available vessels", modelReference, vesselRecords);
	}

	@Override
	public boolean performFinish() {
		return importSingleVesselPage.apply();
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(importSingleVesselPage);
	}
}
