/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.ImportFromBaseSelectionPage.DataOptionGroup;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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

						final List<DataOptionGroup> groups = new LinkedList<>();
						groups.add(new DataOptionGroup("Pricing", !checkPricingDataMatch(p, scenarioInstance), true, true, DataOptions.PricingData));
						// groups.add(new DataOptionGroup("Ports && Distances", !checkPortAndDistanceDataMatch(p, scenarioInstance), false, false, DataOptions.PortData));
						// groups.add(new DataOptionGroup("Vessels", !checkFleetDataMatch(p, scenarioInstance), false, false, DataOptions.FleetDatabase));
						// groups.add(new DataOptionGroup("Misc", true, false, false));

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

	private boolean checkPricingDataMatch(final IBaseCaseVersionsProvider provider, final ScenarioInstance target) {
		final String version = provider.getPricingVersion();

		final Manifest manifest = target.getManifest();
		if (manifest != null) {
			for (final ModelArtifact modelDependency : manifest.getModelDependencies()) {
				if (Objects.equals(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), modelDependency.getKey())) {
					return Objects.equals(modelDependency.getDataVersion(), version);
				}
			}
		}
		return false;
	}

	private boolean checkFleetDataMatch(final IBaseCaseVersionsProvider provider, final ScenarioInstance target) {
		final String version = provider.getFleetVersion();

		final Manifest manifest = target.getManifest();
		if (manifest != null) {
			for (final ModelArtifact modelDependency : manifest.getModelDependencies()) {
				if (Objects.equals(LNGScenarioSharedModelTypes.FLEET.getID(), modelDependency.getKey())) {
					return Objects.equals(modelDependency.getDataVersion(), version);
				}
			}
		}
		return false;
	}

	private boolean checkPortAndDistanceDataMatch(final IBaseCaseVersionsProvider provider, final ScenarioInstance target) {
		final String distVersion = provider.getDistancesVersion();
		final String portVersion = provider.getPortsVersion();

		final Manifest manifest = target.getManifest();
		if (manifest != null) {
			boolean portOk = false;
			boolean distanceOk = false;
			for (final ModelArtifact modelDependency : manifest.getModelDependencies()) {
				if (Objects.equals(LNGScenarioSharedModelTypes.DISTANCES.getID(), modelDependency.getKey())) {
					distanceOk = Objects.equals(modelDependency.getDataVersion(), distVersion);
				} else if (Objects.equals(LNGScenarioSharedModelTypes.LOCATIONS.getID(), modelDependency.getKey())) {
					portOk = Objects.equals(modelDependency.getDataVersion(), portVersion);
				}
			}
			return portOk && distanceOk;
		}
		return false;
	}
}
