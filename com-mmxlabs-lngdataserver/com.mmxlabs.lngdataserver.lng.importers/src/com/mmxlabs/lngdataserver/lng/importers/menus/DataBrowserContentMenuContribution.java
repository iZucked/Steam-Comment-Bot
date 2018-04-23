/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.browser.ui.context.IDataBrowserContextMenuExtension;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Version;
import com.mmxlabs.lngdataserver.integration.ports.PortsRepository;
import com.mmxlabs.lngdataserver.integration.pricing.PricingClient;
//import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.ScenarioServicePublishAction;
import com.mmxlabs.lngdataserver.lng.exporters.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.ui.PricingFromScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.importers.distances.ui.DistancesToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.importers.port.ui.PortsToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.importers.pricing.ui.PricingToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.importers.vessels.ui.VesselsToScenarioImportWizard;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class DataBrowserContentMenuContribution implements IDataBrowserContextMenuExtension {

	@Override
	public boolean contributeToDataMenu(@NonNull TreeSelection selection, @NonNull MenuManager menuManager) {
		boolean itemsAdded = false;
		if (selection.size() == 1) {
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof Node) {
				Node node = (Node) firstElement;
				CompositeNode parentNode = (CompositeNode) node.eContainer();
				if (parentNode != null) {
					if (Objects.equals(LNGScenarioSharedModelTypes.DISTANCES.getID(), parentNode.getType())) {
						String versionIdentifier = node.getDisplayName();
						menuManager.add(new RunnableAction("Update scenario(s)", () -> {
							openWizard((currentInstance) -> new DistancesToScenarioImportWizard(versionIdentifier, currentInstance, true));
						}));
						itemsAdded = true;
					} else if (Objects.equals(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), parentNode.getType())) {
						String versionIdentifier = node.getDisplayName();
						menuManager.add(new RunnableAction("Update scenario(s)", () -> {
							openWizard((currentInstance) -> new PricingToScenarioImportWizard(versionIdentifier, currentInstance, true));
						}));
						itemsAdded = true;
					} else if (Objects.equals(LNGScenarioSharedModelTypes.FLEET.getID(), parentNode.getType())) {
						String versionIdentifier = node.getDisplayName();
						menuManager.add(new RunnableAction("Update scenario(s)", () -> {
							openWizard((currentInstance) -> new VesselsToScenarioImportWizard(versionIdentifier, currentInstance, true));
						}));
						itemsAdded = true;
					} else if (Objects.equals(LNGScenarioSharedModelTypes.LOCATIONS.getID(), parentNode.getType())) {
						String versionIdentifier = node.getDisplayName();
						menuManager.add(new RunnableAction("Update scenario(s)", () -> {
							openWizard((currentInstance) -> new PortsToScenarioImportWizard(versionIdentifier, currentInstance, true));
						}));
						itemsAdded = true;
					}
				}
			}

		}
		return itemsAdded;

	}

	@Override
	public boolean contributeToScenarioMenu(@NonNull TreeSelection selection, @NonNull MenuManager menuManager) {
		boolean itemsAdded = false;
		if (selection.size() == 1) {
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof ScenarioInstance) {
				ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;
				Manifest manifest = scenarioInstance.getManifest();

				menuManager.add(new RunnableAction("Publish scenario as current base case", () -> {
					ScenarioServicePublishAction.publishScenario(scenarioInstance);
				}));
				itemsAdded = true;

				if (manifest != null) {
					for (ModelArtifact modelArtifact : manifest.getModelDependencies()) {
						// if (Objects.equals(LNGScenarioSharedModelTypes.DISTANCES.getID(), modelArtifact.getKey())) {
						// String versionIdentifier = modelArtifact.getDataVersion();
						// menuManager.add(new RunnableAction("Export distance data", () -> {
						// openWizard((currentInstance) -> new DistancesFromScenarioImportWizard(versionIdentifier, currentInstance));
						// }));
						// itemsAdded = true;
						// }
						if (Objects.equals(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), modelArtifact.getKey())) {
							menuManager.add(new RunnableAction("Export pricing data", () -> {
								exportPricing(scenarioInstance);
							}));
							itemsAdded = true;
						}
//						if (Objects.equals(LNGScenarioSharedModelTypes.LOCATIONS.getID(), modelArtifact.getKey())) {
//							menuManager.add(new RunnableAction("Export ports data", () -> {
//								exportPorts(scenarioInstance);
//							}));
//							itemsAdded = true;
//						}
						
						// if (Objects.equals(LNGScenarioSharedModelTypes.FLEET.getID(), modelArtifact.getKey())) {
						// menuManager.add(new RunnableAction("Export pricing data", () -> {
						// exportVessels(scenarioInstance);
						// }));
						// itemsAdded = true;
						// }
					}
				}

			}

		}
		return itemsAdded;
	}

	private void exportPricing(ScenarioInstance scenario) {
		String url = BackEndUrlProvider.INSTANCE.getUrl();
		ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
		try (ModelReference modelReference = modelRecord.aquireReference(PricingFromScenarioImportWizard.class.getSimpleName())) {
			modelReference.executeWithLock(false, () -> {
				LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();

				PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
				Version version = PricingFromScenarioCopier.generateVersion(pricingModel);
				try {
					PricingClient.saveVersion(url, version);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

	private void exportPorts(ScenarioInstance scenario) {
		String url = BackEndUrlProvider.INSTANCE.getUrl();
		ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
		try (ModelReference modelReference = modelRecord.aquireReference(PricingFromScenarioImportWizard.class.getSimpleName())) {
			modelReference.executeWithLock(false, () -> {
				LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();

				PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
				com.mmxlabs.lngdataservice.client.ports.model.Version version = PortFromScenarioCopier.generateVersion(portModel);
				try {
					PortsRepository repo = new PortsRepository(null, null);
					repo.isReady();
					repo.saveVersion(version);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

	private void openWizard(Function<ScenarioInstance, IImportWizard> wizardFactory) {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return;
		}

		ScenarioInstance currentInstance = null;
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage != null) {
			IEditorPart activeEditor = activePage.getActiveEditor();
			if (activeEditor != null) {
				final IEditorInput activeEditorInput = activeEditor.getEditorInput();
				if (activeEditorInput instanceof IScenarioServiceEditorInput) {
					final IScenarioServiceEditorInput editorInput = (IScenarioServiceEditorInput) activeEditorInput;
					currentInstance = editorInput.getScenarioInstance();
				}
			}
		}
		IImportWizard wizard = wizardFactory.apply(currentInstance);

		wizard.init(activeWorkbenchWindow.getWorkbench(), null);

		Shell parent = activeWorkbenchWindow.getShell();

		final WizardDialog dialog = new WizardDialog(parent, wizard);
		dialog.create();
		dialog.open();
	}
}
