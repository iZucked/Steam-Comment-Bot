/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoImportAction;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * Class to bulk-import information from CSV files.
 * 
 * Contains modified cut&paste code from ImportAction and SimpleImportAction (class was written in a hurry). TODO: Move duplicated code into a common code object.
 * 
 * @author Simon McGregor
 * 
 */
public abstract class BulkImportCSVHandler extends AbstractHandler {
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

	public ImportAction.ImportHooksProvider getHooksProvider(final ScenarioInstance instance, final ModelReference modelReference, final Shell shell, final String importFilePath, final char csvSeparator, final char decimalSeparator) {
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

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		final Shell shell = HandlerUtil.getActiveShell(event);
		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);

		final BulkImportWizard wizard = new BulkImportWizard(editor.getScenarioInstance(), getFieldToImport());
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);

		final WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();

		final List<ScenarioInstance> scenarios = wizard.getSelectedScenarios();
		final String importFilename = wizard.getImportFilename();

		final FieldChoice importTarget = wizard.getImportedField();

		if (scenarios != null && importFilename != null && !"".equals(importFilename)) {
			final char separator = wizard.getCsvSeparator();
			final char decimalSeparator = wizard.getDecimalSeparator();
			bulkImport(importTarget, shell, scenarios, importFilename, separator, decimalSeparator);
		}

		return null;
	}

	void bulkImport(final FieldChoice importTarget, final Shell shell, final List<ScenarioInstance> instances, final String filename, final char listSeparator, final char decimalSeparator) {
//		final List<ScenarioInstance> badInstances = new LinkedList<ScenarioInstance>();
//
//		for (final ScenarioInstance instance : instances) {
//			if (instance.getInstance() == null) {
//				try {
//					instance.getScenarioService().load(instance);
//				} catch (final Exception e) {
//					e.printStackTrace();
//					badInstances.add(instance);
//				}
//			}
//		}
//
//		if (!badInstances.isEmpty()) {
//			final StringBuilder sb = new StringBuilder("The following scenario(s) have problems and cannot have data imported into them:\n");
//			for (final ScenarioInstance instance : badInstances) {
//				sb.append("\n");
//				sb.append(instance.getName());
//			}
//			MessageDialog.openWarning(shell, "Import Problems", sb.toString());
//		}
//
		final Set<String> uniqueProblems = new HashSet<String>();
		final List<String> allProblems = new ArrayList<String>();
//		final Set<ScenarioInstance> goodInstances = new HashSet<ScenarioInstance>(instances);
//		goodInstances.removeAll(badInstances);

		for (final ScenarioInstance instance : instances) {
			
			try (final ModelReference modelReference = instance.getReference()) {
				final ImportAction.ImportHooksProvider ihp = getHooksProvider(instance, modelReference, shell, filename, listSeparator, decimalSeparator);
				final ImportAction action = getImportAction(importTarget, ihp);

				final DefaultImportContext context = action.safelyImport();
				for (final IImportProblem problem : context.getProblems()) {
					final String description = problem.getProblemDescription();
					if (!uniqueProblems.contains(description)) {
						uniqueProblems.add(description);
						allProblems.add(description);
					}
				}
			}
		}

		if (!allProblems.isEmpty()) {
			// pop up a dialog showing the problems
			final StringBuilder sb = new StringBuilder("There were problems with the import (perhaps the wrong delimeter character was used): \n");
			for (final String problem : allProblems) {
				sb.append("\n");
				sb.append(problem);
			}
			MessageDialog.openWarning(shell, "Import Problems", sb.toString());
		}

	}

	// TODO: replace this by subclass implementations of the getImportAction handler?
	public abstract FieldChoice getFieldToImport();

}
