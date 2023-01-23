/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.browser.ui.context.IDataBrowserContextMenuExtension;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.ImportFromBaseSelectionPage.DataOptionGroup;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;

public class SharedDataCopyDataBrowserMenuExtension implements IDataBrowserContextMenuExtension {
	@Override
	public boolean contributeToScenarioMenu(@NonNull final TreeSelection selection, @NonNull final MenuManager menuManager) {

		boolean itemsAdded = false;
		if (selection.size() == 1) {
			final Object firstElement = selection.getFirstElement();
			if (firstElement instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;

				itemsAdded |= ServiceHelper.<IBaseCaseVersionsProvider, Boolean> withService(IBaseCaseVersionsProvider.class, p -> {
					final ScenarioInstance baseCase = p.getBaseCase();
					if (baseCase != null) {
						Map<String, String> scenarioDataVersions = ScenarioStorageUtil.extractScenarioDataVersions(scenarioInstance.getManifest());
						if (scenarioDataVersions != null) {
							final List<DataOptionGroup> groups = SharedScenarioDataUtils.createGroups(p, scenarioDataVersions);
							menuManager.add(new RunnableAction("Update from base case", () -> {
								final ImportFromBaseImportWizard wizard = new ImportFromBaseImportWizard(baseCase, scenarioInstance, groups);
								wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);
								wizard.setForcePreviousAndNextButtons(true);
								final Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();
								final WizardDialog dialog = new WizardDialog(parent, wizard);
								dialog.create();
								dialog.open();
							}));
							return true;
						}
					}
					return false;
				});

			}
		}
		if (LicenseFeatures.isPermitted("features:hub-experimental") && selection.size() == 1) {
			final Object firstElement = selection.getFirstElement();
			if (firstElement instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;
				menuManager.add(new RunnableAction("Copy reference data to...", () -> {
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

			if (firstElement instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;
				menuManager.add(new RunnableAction("Export reference data", () -> {

					final DirectoryDialog d = new DirectoryDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
					d.setMessage("Select directory for export");
					final String target = d.open();
					if (target != null) {
						final File dest = new File(target);
						dest.mkdirs();
						try {
							LiNGODataExportHelper.exportLingoDataFiles(scenarioInstance, dest);
						} catch (final IOException e) {
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
	public boolean contributeToDataMenu(@NonNull final TreeSelection selection, @NonNull final MenuManager menuManager) {
		return false;
	}

	@Override
	public boolean contributeToBaseCaseMenu(@NonNull MenuManager scenarioMgr) {
		// TODO Auto-generated method stub
		return false;
	}

}
