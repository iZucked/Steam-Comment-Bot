/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.importWizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.commands.OpenScenarioCommandHandler;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;
import com.mmxlabs.shiplingo.platform.models.manifest.wizards.ScenarioServiceNewScenarioPage;

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
				final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				OpenScenarioCommandHandler.openScenarioInstance(page, instance);
			} catch (PartInitException e) {
			}
		}
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
