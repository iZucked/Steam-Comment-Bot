/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
public class ImportCanalCostsWizard extends Wizard implements IImportWizard{
	
	private final ScenarioInstance currentScenario;
	private ImportCanalCostsPage importPage;
	private final BiConsumer<CompoundCommand, IScenarioDataProvider> panamaCostsUpdater;
	private final BiConsumer<CompoundCommand, IScenarioDataProvider> suezCostsUpdater;

	public ImportCanalCostsWizard(final ScenarioInstance scenarioInstance, final String windowTitle, 
			final BiConsumer<CompoundCommand, IScenarioDataProvider> panamaCostsUpdater,
			final BiConsumer<CompoundCommand, IScenarioDataProvider> suezCostsUpdater) {
		this.suezCostsUpdater = suezCostsUpdater;
		this.panamaCostsUpdater = panamaCostsUpdater;
		this.currentScenario = scenarioInstance;
		setWindowTitle(windowTitle);
		
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		importPage = new ImportCanalCostsPage("selectScenarios", currentScenario);
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
		final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioDataProvider:1");
		{	
			final CompoundCommand command = new CompoundCommand();
			suezCostsUpdater.accept(command, scenarioDataProvider);
			command.execute();
		}
		{
			final CompoundCommand command = new CompoundCommand();
			panamaCostsUpdater.accept(command, scenarioDataProvider);
			command.execute();
		}
	}
	

}
