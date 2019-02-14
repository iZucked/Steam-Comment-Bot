/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.UpdateJob;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class SharedScenarioDataImportWizard extends Wizard implements IImportWizard {

	private SharedDataScenariosSelectionPage destinationPage;
	private final ScenarioInstance currentScenario;

	public SharedScenarioDataImportWizard(final ScenarioInstance currentScenario) {
		super();
		this.currentScenario = currentScenario;
		this.setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		final UpdateJob job = new UpdateJob(destinationPage.getSelectedDataOptions(), currentScenario, destinationPage.getSelectedScenarios(), false);

		try {
			getContainer().run(true, true, job);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Shared Scenario Data Import"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		destinationPage = new SharedDataScenariosSelectionPage("Scenario Selection");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		addPage(destinationPage);
	}

	@Override
	public boolean canFinish() {
		return destinationPage.isPageComplete();
	}
}
