/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.vesselsupdate.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class UpdateVesselsWizard extends Wizard implements IImportWizard {

	private UpdateVesselsPage vesselsPage;
	private ModelReference modelReference;
	private VesselsVersion vesselRecords;

	public UpdateVesselsWizard(ModelReference modelReference, VesselsVersion vesselRecords) {
		super();
		this.modelReference = modelReference;
		this.vesselRecords = vesselRecords;
		this.setNeedsProgressMonitor(true);
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		return false;
	}

	@Override
	public boolean performFinish() {
		return vesselsPage.apply();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Reference vessels update wizard"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		vesselsPage = new UpdateVesselsPage("Review of vessel changes", modelReference, vesselRecords);
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(vesselsPage);
	}

	@Override
	public boolean canFinish() {
		return true;
	}
}
