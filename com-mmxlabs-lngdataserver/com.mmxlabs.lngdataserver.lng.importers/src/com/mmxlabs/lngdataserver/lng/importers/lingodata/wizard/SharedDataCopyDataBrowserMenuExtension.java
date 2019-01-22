/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lngdataserver.browser.ui.context.IDataBrowserContextMenuExtension;
import com.mmxlabs.lngdataserver.commons.IBaseCaseVersionsProvider;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class SharedDataCopyDataBrowserMenuExtension implements IDataBrowserContextMenuExtension {
	@Override
	public boolean contributeToScenarioMenu(@NonNull TreeSelection selection, @NonNull MenuManager menuManager) {

		boolean itemsAdded = false;
		if (selection.size() == 1) {
			final Object firstElement = selection.getFirstElement();
			if (firstElement instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;

				itemsAdded |= ServiceHelper.<IBaseCaseVersionsProvider, Boolean> withService(IBaseCaseVersionsProvider.class, p -> {
					ScenarioInstance baseCase = p.getBaseCase();
					if (baseCase != null) {
						final Set<DataOptions> options = EnumSet.of(DataOptions.PricingData);
						menuManager.add(new RunnableAction("Update from base case", () -> {
							final ImportFromBaseImportWizard wizard = new ImportFromBaseImportWizard(baseCase, scenarioInstance, options);
							wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);
							wizard.setForcePreviousAndNextButtons(true);
							final Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();
							final WizardDialog dialog = new WizardDialog(parent, wizard);
							dialog.create();
							dialog.open();
						}));
						return true;
					}
					return false;
				});

			}
		}
		if (false && selection.size() == 1) {
			final Object firstElement = selection.getFirstElement();
			if (firstElement instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;
				menuManager.add(new RunnableAction("Copy reference data", () -> {
					final SharedScenarioDataImportWizard wizard = new SharedScenarioDataImportWizard(scenarioInstance);
					wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);
					wizard.setForcePreviousAndNextButtons(true);
					final Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();
					final WizardDialog dialog = new WizardDialog(parent, wizard);
					dialog.create();
					dialog.open();
				}));
				itemsAdded = true;
			}
		}
		if (false && selection.size() == 1) {
			final Object firstElement = selection.getFirstElement();
			if (firstElement instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;
				menuManager.add(new RunnableAction("Export reference data", () -> {

					DirectoryDialog d = new DirectoryDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
					d.setMessage("Select directory for export");
					String target = d.open();
					if (target != null) {
						File dest = new File(target);
						dest.mkdirs();
						try {
							LiNGODataExportHelper.exportLingoDataFiles(scenarioInstance, dest);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}));
				itemsAdded = true;
			}
		}
		return itemsAdded;
	}

	@Override
	public boolean contributeToDataMenu(@NonNull TreeSelection selection, @NonNull MenuManager menuManager) {
		return false;
	}

}
