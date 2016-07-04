/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;

import com.mmxlabs.models.lng.scenario.wizards.ScenarioServiceNewScenarioPage;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

public class ImportCSVWizard extends Wizard implements IImportWizard {

	ScenarioServiceNewScenarioPage mainPage;
	private ImportCSVFilesPage filesPage;
	private ImportWarningsPage warnings;

	public ImportCSVWizard() {
		super();
		this.setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final ScenarioInstance instance = filesPage.getScenarioInstance();
		if (instance != null) {
			try {
				OpenScenarioUtils.openScenarioInstance(instance);
			} catch (PartInitException e) {
			}
		}
		filesPage.saveDirectorySetting();

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("CSV Import Wizard"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new ScenarioServiceNewScenarioPage(selection);
		filesPage = new ImportCSVFilesPage("CSV Files", mainPage);
		warnings = new ImportWarningsPage("Warnings", filesPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		addPage(mainPage);
		addPage(filesPage);
		addPage(warnings);
	}

	@Override
	public boolean canFinish() {
		return warnings.isPageComplete();
	}
}
