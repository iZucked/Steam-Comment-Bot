/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.ImportFromBaseSelectionPage.DataOptionGroup;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.UpdateJob;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ImportFromBaseImportWizard extends Wizard implements IImportWizard {

	private ScenarioInstance baseCase;

	private ScenarioInstance destination;

	private List<DataOptionGroup> data;

	public ImportFromBaseImportWizard(final ScenarioInstance baseCase, final ScenarioInstance destination, List<DataOptionGroup> data) {
		super();
		this.baseCase = baseCase;
		this.destination = destination;
		this.setNeedsProgressMonitor(true);
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		Set<DataOptions> options = data.stream() //
				.filter(e -> (e.enabled && e.selected)) //
				.flatMap(e -> e.options.stream())//
				.collect(Collectors.toSet());

		ScenarioModelRecord currentScenarioModelRecord = SSDataManager.Instance.getModelRecordChecked(baseCase);
		ScenarioModelRecord destinationScenarioModelRecord = SSDataManager.Instance.getModelRecordChecked(destination);

		final UpdateJob job = new UpdateJob(options, currentScenarioModelRecord, Collections.singletonList(destinationScenarioModelRecord), true);

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
		return data.stream() //
				.filter(e -> (e.enabled && e.selected && e.needsUpdate)) //
				.count() > 0;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(new ImportFromBaseSelectionPage("Import from base case", "Data to update.", data));
	}
}
