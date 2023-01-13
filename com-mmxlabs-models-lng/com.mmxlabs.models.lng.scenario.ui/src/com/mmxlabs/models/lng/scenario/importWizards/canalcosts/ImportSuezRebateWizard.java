/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.canalcosts;

import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 */
public class ImportSuezRebateWizard extends Wizard implements IImportWizard {

	private final ScenarioInstance currentScenario;
	private ImportSuezRebatePage importPage;
	private final BiConsumer<CompoundCommand, IScenarioDataProvider> rebateUpdater;

	public ImportSuezRebateWizard(final ScenarioInstance scenarioInstance, final String windowTitle, final BiConsumer<CompoundCommand, IScenarioDataProvider> rebateUpdater) {
		this.rebateUpdater = rebateUpdater;
		this.currentScenario = scenarioInstance;
		setWindowTitle(windowTitle);

	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		importPage = new ImportSuezRebatePage("selectScenarios", currentScenario);
		this.setForcePreviousAndNextButtons(false);
		this.setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {

		final List<ScenarioInstance> scenarios = importPage.getSelectedScenarios();

		for (final ScenarioInstance scenarioInstance : scenarios) {
			doImport(scenarioInstance);
		}

		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(importPage);
	}

	private void doImport(final ScenarioInstance scenarioInstance) {
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		
		try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ImportSuezRebateWizard:1")) {
			final CompoundCommand command = new CompoundCommand();
			rebateUpdater.accept(command, scenarioDataProvider);
			scenarioDataProvider.getCommandStack().execute(command);
		}
	}

}
