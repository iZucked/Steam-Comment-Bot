package com.mmxlabs.models.lng.scenario.importWizards.portgroups;

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

public class ImportEastOfSuezPortGroupWizard extends Wizard implements IImportWizard {

	private final ScenarioInstance currentScenario;
	private ImportEastOfSuezPortGroupPage importPage;
	private final BiConsumer<CompoundCommand, IScenarioDataProvider> portGroupUpdater;
	

	public ImportEastOfSuezPortGroupWizard(final ScenarioInstance scenarioInstance, final String windowTitle, final BiConsumer<CompoundCommand, IScenarioDataProvider> portGroupUpdater) {
		this.portGroupUpdater = portGroupUpdater;
		this.currentScenario = scenarioInstance;
		setWindowTitle(windowTitle);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		importPage = new ImportEastOfSuezPortGroupPage("selectScenarios", currentScenario);
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
		try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ImportEastOfSuezPortGroupWizard:1")) {
			final CompoundCommand command = new CompoundCommand();
			portGroupUpdater.accept(command, scenarioDataProvider);
			scenarioDataProvider.getCommandStack().execute(command);
		}
	}

}
