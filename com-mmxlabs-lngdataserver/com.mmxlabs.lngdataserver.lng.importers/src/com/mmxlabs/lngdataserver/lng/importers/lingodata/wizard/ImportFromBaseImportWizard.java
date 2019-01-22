/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.UpdateJob;
import com.mmxlabs.rcp.common.wizards.MessagePage;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ImportFromBaseImportWizard extends Wizard implements IImportWizard {

	private ScenarioInstance baseCase;

	private ScenarioInstance destination;

	private Set<DataOptions> options;

	public ImportFromBaseImportWizard(final ScenarioInstance baseCase, final ScenarioInstance destination, Set<DataOptions> options) {
		super();
		this.baseCase = baseCase;
		this.destination = destination;
		this.options = options;
		this.setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		
		final UpdateJob job = new UpdateJob(options, baseCase, Collections.singletonList(destination), true);

		try {
			getContainer().run(true, true, job);
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
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
		setWindowTitle("Import from base case"); // NON-NLS-1
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean canFinish() {
		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(new MessagePage("Import from base case", "Copy pricing data from base case."));
	}
}
