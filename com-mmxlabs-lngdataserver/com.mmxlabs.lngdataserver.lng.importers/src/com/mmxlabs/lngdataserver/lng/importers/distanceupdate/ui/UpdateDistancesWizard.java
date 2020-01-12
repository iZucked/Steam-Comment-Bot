/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.ui;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.Entry;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class UpdateDistancesWizard extends Wizard implements IImportWizard {

	private UpdatePortsPage portPage;
	private ModelReference modelReference;
	private LocationsVersion locationsEntry;
	private List<AtoBviaCLookupRecord> distancesEntry;

	public UpdateDistancesWizard(ModelReference modelReference, LocationsVersion locationsVersion, List<AtoBviaCLookupRecord> distanceRecords) {
		super();
		this.modelReference = modelReference;
		this.locationsEntry = locationsVersion;
		this.distancesEntry = distanceRecords;
		this.setNeedsProgressMonitor(true);
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		return false;
	}

	@Override
	public boolean performFinish() {
		return portPage.apply();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Distances update wizard"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		portPage = new UpdatePortsPage("Review of port and distance changes", modelReference, locationsEntry, distancesEntry);
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(portPage);
	}

	@Override
	public boolean canFinish() {
		return true;
	}
}
