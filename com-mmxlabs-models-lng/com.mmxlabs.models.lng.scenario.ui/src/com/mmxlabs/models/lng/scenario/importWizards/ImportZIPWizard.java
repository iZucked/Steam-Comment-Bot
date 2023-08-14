/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

public class ImportZIPWizard extends Wizard implements IImportWizard {

	ScenarioServiceNewScenarioPage mainPage;
	private ImportZIPFilesPage filesPage;
	private ImportWarningsPage warnings;

	public ImportZIPWizard() {
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
		setWindowTitle("ZIP Import Wizard"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new ScenarioServiceNewScenarioPage(selection);
		filesPage = new ImportZIPFilesPage("ZIP Files", mainPage);
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
