/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.IImportProblem;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public abstract class AbstractImportWizard extends Wizard implements IImportWizard {

	private static final Logger log = LoggerFactory.getLogger(AbstractImportWizard.class);
	protected final boolean guided;

	private AbstractImportPage bip;
	private List<ScenarioInstance> selectedScenarios;
	private String importFilename;
	private char csvSeparator;
	private char decimalSeparator;
	final private ScenarioInstance currentScenario;

	public AbstractImportWizard(final ScenarioInstance scenarioInstance, final String windowTitle) {
		currentScenario = scenarioInstance;
		setWindowTitle(windowTitle);
		guided = false;
	}

	public AbstractImportWizard(final ScenarioInstance scenarioInstance, final String windowTitle, final String importFilename) {
		currentScenario = scenarioInstance;
		setWindowTitle(windowTitle);
		this.importFilename = importFilename;
		guided = true;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		bip = getImportPage("selectScenarios", currentScenario);
		this.setForcePreviousAndNextButtons(false);
		this.setNeedsProgressMonitor(true);
	}

	protected abstract AbstractImportPage getImportPage(final String pageName, final ScenarioInstance currentScenario);

	@Override
	public void addPages() {
		super.addPages();
		addPage(bip);
	}

	@Override
	public boolean performFinish() {
		selectedScenarios = bip.getSelectedScenarios();
		csvSeparator = bip.getCsvSeparator();
		decimalSeparator = bip.getDecimalSeparator();
		if (!guided)
			importFilename = bip.getImportFilename();

		bip.saveDirectorySetting();

		final List<ScenarioInstance> scenarios = getSelectedScenarios();
		if (scenarios != null && importFilename != null && !"".equals(importFilename)) {

			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
				@Override
				protected void execute(final IProgressMonitor progressMonitor) {
					doImport(scenarios, importFilename, csvSeparator, decimalSeparator, progressMonitor);
				}
			};

			try {
				getContainer().run(false, true, operation);
			} catch (InvocationTargetException | InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}

		return true;
	}

	public List<ScenarioInstance> getSelectedScenarios() {
		return selectedScenarios;
	}

	public String getImportFilename() {
		return importFilename;
	}

	public char getCsvSeparator() {
		return csvSeparator;
	}

	public char getDecimalSeparator() {
		return decimalSeparator;
	}

	@Override
	public boolean canFinish() {
		if (guided) {
			final File file = new File(this.importFilename);
			return file.isFile() && file.canRead();
		}
		final File file = new File(bip.getImportFilename());
		return file.isFile() && file.canRead();
	}

	void doImport(final List<ScenarioInstance> instances, final String filename, final char listSeparator, final char decimalSeparator, final IProgressMonitor monitor) {
		monitor.beginTask("Import", instances.size());
		final Set<String> uniqueProblems = new HashSet<>();
		final List<String> allProblems = new ArrayList<>();
		try {

			for (final ScenarioInstance instance : instances) {
				if (instance.isReadonly() || instance.isCloudLocked()) {
					allProblems.add(String.format("Scenario %s is read-only, skipping", instance.getName()));
					continue;
				}

				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
				//
				modelRecord.execute(ref -> ref.executeWithLock(true, () -> {
					doImportAction(filename, listSeparator, decimalSeparator, modelRecord, uniqueProblems, allProblems);

				}));
				monitor.worked(1);
				if (monitor.isCanceled()) {
					break;
				}
			}
		} catch (final Throwable t) {
			log.error(t.getMessage(), t);
			allProblems.add(String.format("Uncaught exception during import. Import aborted. See error log"));
		} finally {
			monitor.done();
		}
		if (!allProblems.isEmpty()) {
			displayWarningDialog(allProblems);
		}
	}

	protected void displayWarningDialog(final List<String> allProblems) {
		// pop up a dialog showing the problems
		final StringBuilder sb = new StringBuilder("There were problems with the import (perhaps a wrong delimiter character was used): \n");
		for (final String problem : allProblems) {
			sb.append("\n- ");
			sb.append(problem);
		}
		sb.append("\n Please check the error log for more details.");
		MessageDialog.openWarning(getShell(), "Import Problems", sb.toString());
	}

	private void doImportAction(final String filename, final char listSeparator, final char decimalSeparator, final ScenarioModelRecord modelRecord, final Set<String> uniqueProblems,
			final List<String> allProblems) {

		try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider(this.getClass().getSimpleName())) {
			ModelReference modelReference = scenarioDataProvider.getModelReference();
			final ImportAction.ImportHooksProvider ihp = new DefaultImportHooksProvider(modelRecord.getScenarioInstance(), modelReference, getShell(), filename, listSeparator, decimalSeparator);

			final ImportAction action = getImportAction(ihp, scenarioDataProvider);

			final DefaultImportContext context = action.safelyImport();
			for (final IImportProblem problem : context.getProblems()) {
				final String description = problem.getProblemDescription();
				if (!uniqueProblems.contains(description)) {
					uniqueProblems.add(description);
					allProblems.add(description);
				}
			}
		} catch (final Exception e) {
			final String description = e.getMessage();
			log.error(e.getMessage(), e);
			if (!uniqueProblems.contains(description)) {
				uniqueProblems.add(description);
				allProblems.add(description);
			}
		}
	}

	protected abstract ImportAction getImportAction(final ImportAction.ImportHooksProvider ihp, final IScenarioDataProvider scenarioDataProvider);
}
