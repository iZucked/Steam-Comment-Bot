/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.ui;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.DistanceDataVersion;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class UpdateDistancesWizard extends Wizard implements IImportWizard {

	private UpdatePortsPage portPage;
	private ModelReference modelReference;
	private DistanceDataVersion dataVersion;
	private LocationsVersion locationsEntry;
	private List<AtoBviaCLookupRecord> distancesEntry;
	private List<AtoBviaCLookupRecord> manualRecords;

	public UpdateDistancesWizard(ModelReference modelReference,  DistanceDataVersion dataVersion, LocationsVersion locationsVersion, List<AtoBviaCLookupRecord> distanceRecords, List<AtoBviaCLookupRecord> manualRecords) {
		super();
		this.modelReference = modelReference;
		this.dataVersion = dataVersion;
		this.locationsEntry = locationsVersion;
		this.distancesEntry = distanceRecords;
		this.manualRecords = manualRecords;
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
		portPage = new UpdatePortsPage("Review of port and distance changes", modelReference, dataVersion, locationsEntry, distancesEntry, manualRecords);
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
