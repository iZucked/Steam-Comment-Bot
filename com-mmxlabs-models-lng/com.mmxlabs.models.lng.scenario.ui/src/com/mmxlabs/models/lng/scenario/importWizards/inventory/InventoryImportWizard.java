/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.inventory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.IImportProblem;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 */
public class InventoryImportWizard extends Wizard implements IImportWizard {

	private static final Logger log = LoggerFactory.getLogger(InventoryImportWizard.class);

	private InventoryImportPage bip;
	private List<ScenarioInstance> selectedScenarios;
	private String importFilename;
	private char csvSeparator;
	private char decimalSeparator;
	final private ScenarioInstance currentScenario;

	public InventoryImportWizard(final ScenarioInstance scenarioInstance, final String windowTitle) {
		currentScenario = scenarioInstance;
		setWindowTitle(windowTitle);
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		bip = new InventoryImportPage("selectScenarios", currentScenario);
		this.setForcePreviousAndNextButtons(false);
		this.setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(bip);
	}

	@Override
	public boolean performFinish() {
		selectedScenarios = bip.getSelectedScenarios();
		importFilename = bip.getImportFilename();
		csvSeparator = bip.getCsvSeparator();
		decimalSeparator = bip.getDecimalSeparator();

		bip.saveDirectorySetting();

		final List<ScenarioInstance> scenarios = getSelectedScenarios();
		final String importFilename = getImportFilename();

		if (scenarios != null && importFilename != null && !"".equals(importFilename)) {
			final char separator = getCsvSeparator();
			final char decimalSeparator = getDecimalSeparator();

			final IRunnableWithProgress operation = new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor progressMonitor) {
					doImport(scenarios, importFilename, separator, decimalSeparator, progressMonitor);
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
		final File file = new File(bip.getImportFilename());
		return file.isFile() && file.canRead();
	}

	void doImport(final List<ScenarioInstance> instances, final String filename, final char listSeparator, final char decimalSeparator, final IProgressMonitor monitor) {
		monitor.beginTask("Import", instances.size());
		final Set<String> uniqueProblems = new HashSet<>();
		final List<String> allProblems = new ArrayList<>();
		try {

			for (final ScenarioInstance instance : instances) {
				if (instance.isReadonly()) {
					allProblems.add(String.format("Scenario %s is read-only, skipping", instance.getName()));
					continue;
				}

				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
				//
				modelRecord.execute(ref -> ref.executeWithLock(true, () -> {

					doImportAction(filename, listSeparator, decimalSeparator, instance, uniqueProblems, allProblems, false);

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
			// pop up a dialog showing the problems
			final StringBuilder sb = new StringBuilder("There were problems with the import (perhaps a wrong delimiter character was used): \n");
			for (final String problem : allProblems) {
				sb.append("\n- ");
				sb.append(problem);
			}
			sb.append("\n Please check the error log for more details.");
			MessageDialog.openWarning(getShell(), "Import Problems", sb.toString());
		}
	}

	private void doImportAction(final String filename, final char listSeparator, final char decimalSeparator, final ScenarioInstance instance, final Set<String> uniqueProblems,
			final List<String> allProblems, final boolean multipleDetails) {
		@NonNull
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(instance);
		try (final ModelReference modelReference = modelRecord.aquireReference("InventoryImportWizard")) {
			final ImportAction.ImportHooksProvider ihp = getHooksProvider(instance, modelReference, getShell(), filename, listSeparator, decimalSeparator);
			CargoModel cargoModel = ScenarioModelUtil.getCargoModel((LNGScenarioModel) modelReference.getInstance());
			final ImportAction action = new InventoryImportAction(ihp, cargoModel);

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

	private FieldChoice[] getUnifiedChoices() {
		return new FieldChoice[] { FieldChoice.CHOICE_COMMODITY_INDICES, FieldChoice.CHOICE_CHARTER_INDICES, FieldChoice.CHOICE_BASE_FUEL_CURVES, FieldChoice.CHOICE_CURRENCY_CURVES };
	}

	public ImportAction.ImportHooksProvider getHooksProvider(final ScenarioInstance instance, final ModelReference modelReference, final Shell shell, final String importFilePath,
			final char csvSeparator, final char decimalSeparator) {
		return new ImportAction.ImportHooksProvider() {
			ScenarioLock lock;

			@Override
			public Shell getShell() {
				return shell;
			}

			@Override
			public MMXRootObject getRootObject() {
				return (MMXRootObject) modelReference.getInstance();
			}

			@Override
			public EditingDomain getEditingDomain() {
				return (EditingDomain) modelReference.getEditingDomain();
			}

			@Override
			public String getImportFilePath() {
				return importFilePath;
			}

			@Override
			public void lock() {
				((CommandProviderAwareEditingDomain) getEditingDomain()).setCommandProvidersDisabled(true);
				lock = modelReference.getLock();
				lock.lock();
			}

			@Override
			public void unlock() {
				((CommandProviderAwareEditingDomain) getEditingDomain()).setCommandProvidersDisabled(false);
				if (lock != null) {
					lock.unlock();
				}
			}

			@Override
			public char getCsvSeparator() {
				return csvSeparator;
			}

			@Override
			public char getDecimalSeparator() {
				return decimalSeparator;
			}

		};
	}

}
