/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoImportAction;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 */
public class BulkImportWizard extends Wizard implements IImportWizard {

	private static final Logger log = LoggerFactory.getLogger(BulkImportWizard.class);

	private BulkImportPage bip;
	private List<ScenarioInstance> selectedScenarios;
	private String importFilename;
	private char csvSeparator;
	private char decimalSeparator;
	final private ScenarioInstance currentScenario;
	private final FieldChoice importedField;

	public BulkImportWizard(final ScenarioInstance scenarioInstance, final FieldChoice fieldChoice) {
		currentScenario = scenarioInstance;
		importedField = fieldChoice;
		setWindowTitle("Bulk data import");
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		bip = new BulkImportPage("selectScenarios", getImportedField(), currentScenario);
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

		final FieldChoice importTarget = getImportedField();

		if (scenarios != null && importFilename != null && !"".equals(importFilename)) {
			final char separator = getCsvSeparator();
			final char decimalSeparator = getDecimalSeparator();

			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
				@Override
				protected void execute(final IProgressMonitor progressMonitor) {
					bulkImport(importTarget, scenarios, importFilename, separator, decimalSeparator, progressMonitor);
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

	public FieldChoice getImportedField() {
		return importedField;
	}

	@Override
	public boolean canFinish() {
		final File file = new File(bip.getImportFilename());
		return file.isFile() && file.canRead();
	}

	void bulkImport(final FieldChoice importTarget, final List<ScenarioInstance> instances, final String filename, final char listSeparator, final char decimalSeparator, final IProgressMonitor monitor) {
		monitor.beginTask("Import", instances.size());
		final Set<String> uniqueProblems = new HashSet<String>();
		final List<String> allProblems = new ArrayList<String>();
		try {

			for (final ScenarioInstance instance : instances) {

				try (final ModelReference modelReference = instance.getReference()) {
					final ImportAction.ImportHooksProvider ihp = getHooksProvider(instance, modelReference, getShell(), filename, listSeparator, decimalSeparator);
					final ImportAction action = getImportAction(importTarget, ihp);

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
				monitor.worked(1);
				if (monitor.isCanceled()) {
					break;
				}
			}
		} finally {
			monitor.done();
		}
		if (!allProblems.isEmpty()) {
			// pop up a dialog showing the problems
			final StringBuilder sb = new StringBuilder("There were problems with the import (perhaps the wrong delimeter character was used). Check the error log.: \n");
			for (final String problem : allProblems) {
				sb.append("\n");
				sb.append(problem);
			}
			MessageDialog.openWarning(getShell(), "Import Problems", sb.toString());
		}
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
				return (EditingDomain) instance.getAdapters().get(EditingDomain.class);
			}

			@Override
			public String getImportFilePath() {
				return importFilePath;
			}

			@Override
			public void lock() {
				((CommandProviderAwareEditingDomain) getEditingDomain()).setCommandProvidersDisabled(true);
				lock = instance.getLock(ScenarioLock.EDITORS);
				lock.awaitClaim();
			}

			@Override
			public void unlock() {
				((CommandProviderAwareEditingDomain) getEditingDomain()).setCommandProvidersDisabled(false);
				if (lock != null) {
					lock.release();
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

	public ImportAction getImportAction(final FieldChoice importTarget, final ImportAction.ImportHooksProvider ihp) {
		final MMXRootObject root = ihp.getRootObject();
		if (root instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) root;
			switch (importTarget) {
			case CHOICE_COMMODITY_INDICES: {
				return new SimpleImportAction(ihp, scenarioModel.getPricingModel(), PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES);
			}
			case CHOICE_CARGOES: {
				return new CargoImportAction(ihp, scenarioModel.getPortfolioModel().getCargoModel(), CargoPackage.Literals.CARGO_MODEL__CARGOES);
			}
			case CHOICE_CHARTER_INDICES: {
				return new SimpleImportAction(ihp, scenarioModel.getPricingModel(), PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES);
			}
			case CHOICE_BASE_FUEL_CURVES: {
				return new SimpleImportAction(ihp, scenarioModel.getPricingModel(), PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES);
			}
			}
		}

		return null;
	}

}
