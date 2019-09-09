/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.lng.io.distances.ui.DistancesToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.io.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.ui.PortsToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.io.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.pricing.ui.PricingFromScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.io.pricing.ui.PricingToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.io.vessels.ui.VesselsToScenarioImportWizard;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
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
	public boolean contributeToDataMenu(@NonNull final TreeSelection selection, @NonNull final MenuManager menuManager) {
		boolean itemsAdded = false;
		if (selection.size() == 1) {
			final Object firstElement = selection.getFirstElement();
			if (firstElement instanceof Node) {
				final Node node = (Node) firstElement;
				final CompositeNode parentNode = (CompositeNode) node.eContainer();
				if (parentNode != null) {
					final String versionIdentifier = node.getVersionIdentifier();
					// if (Objects.equals(LNGScenarioSharedModelTypes.DISTANCES.getID(), parentNode.getType())) {
					// menuManager.add(new RunnableAction("Update scenario(s)", () -> {
					// openWizard((currentInstance) -> new DistancesToScenarioImportWizard(versionIdentifier, currentInstance, true));
					// }));
					// itemsAdded = true;
					// } else
					if (Objects.equals(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), parentNode.getType())) {
						menuManager.add(new RunnableAction("Update scenario(s)", () -> {
							openWizard((currentInstance) -> new PricingToScenarioImportWizard(versionIdentifier, currentInstance, true));
						}));
						itemsAdded = true;
						// } else if (Objects.equals(LNGScenarioSharedModelTypes.FLEET.getID(), parentNode.getType())) {
						// menuManager.add(new RunnableAction("Update scenario(s)", () -> {
						// openWizard((currentInstance) -> new VesselsToScenarioImportWizard(versionIdentifier, currentInstance, true));
						// }));
						// itemsAdded = true;
						// } else if (Objects.equals(LNGScenarioSharedModelTypes.LOCATIONS.getID(), parentNode.getType())) {
						// menuManager.add(new RunnableAction("Update scenario(s)", () -> {
						// openWizard((currentInstance) -> new PortsToScenarioImportWizard(versionIdentifier, currentInstance, true));
						// }));
						// itemsAdded = true;
					}
				}
			}

		}
		return itemsAdded;

	}

	@Override
	public boolean contributeToBaseCaseMenu(@NonNull final MenuManager menuManager) {
		boolean itemsAdded = false;
		if (UpstreamUrlProvider.INSTANCE.isAvailable() && BaseCaseServiceClient.INSTANCE.needsLocking()) {
			if (BaseCaseServiceClient.INSTANCE.isServiceLockedByMe()) {

				menuManager.add(new RunnableAction("Unlock base case", () -> {
					try {
						BaseCaseServiceClient.INSTANCE.unlock();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}));
				itemsAdded = true;
			} else if (!BaseCaseServiceClient.INSTANCE.isServiceLocked()) {
				menuManager.add(new RunnableAction("Lock base case", () -> {
					try {
						BaseCaseServiceClient.INSTANCE.lock();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}));
				itemsAdded = true;
			}
		}

		return itemsAdded;

	}

	@Override
	public boolean contributeToScenarioMenu(@NonNull final TreeSelection selection, @NonNull final MenuManager menuManager) {
		boolean itemsAdded = false;
		if (selection.size() == 1) {
			final Object firstElement = selection.getFirstElement();
			if (firstElement instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;
				final Manifest manifest = scenarioInstance.getManifest();
				if (UpstreamUrlProvider.INSTANCE.isAvailable() && BaseCaseServiceClient.INSTANCE.canPublish()) {
					menuManager.add(new RunnableAction("Publish scenario as current base case", () -> {
						ScenarioServicePublishAction.publishScenario(scenarioInstance);
					}));
					itemsAdded = true;
				}
				if (manifest != null) {
					for (final ModelArtifact modelArtifact : manifest.getModelDependencies()) {
						// if (Objects.equals(LNGScenarioSharedModelTypes.DISTANCES.getID(), modelArtifact.getKey())) {
						// String versionIdentifier = modelArtifact.getDataVersion();
						// menuManager.add(new RunnableAction("Export distance data", () -> {
						// openWizard((currentInstance) -> new DistancesFromScenarioImportWizard(versionIdentifier, currentInstance));
						// }));
						// itemsAdded = true;
						// }
						// if (Objects.equals(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), modelArtifact.getKey())) {
						// menuManager.add(new RunnableAction("Export pricing data", () -> {
						// exportPricing(scenarioInstance);
						// }));
						// itemsAdded = true;
						// }
						// if (Objects.equals(LNGScenarioSharedModelTypes.LOCATIONS.getID(), modelArtifact.getKey())) {
						// menuManager.add(new RunnableAction("Export ports data", () -> {
						// exportPorts(scenarioInstance);
						// }));
						// itemsAdded = true;
						// }

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
		if (!itemsAdded && !UpstreamUrlProvider.INSTANCE.isAvailable()) {
			final RunnableAction action = new RunnableAction("Please wait...", () -> {
			});
			action.setEnabled(false);
			menuManager.add(action);
			itemsAdded = true;
		}
		return itemsAdded;

	}

	private void exportPricing(final ScenarioInstance scenario) {
		final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
		try (ModelReference modelReference = modelRecord.aquireReference(PricingFromScenarioImportWizard.class.getSimpleName())) {
			modelReference.executeWithLock(false, () -> {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();

				final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
				final PricingVersion version = PricingFromScenarioCopier.generateVersion(pricingModel);
				try {
					PricingRepository.INSTANCE.publishVersion(version);
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

	private void exportPorts(final ScenarioInstance scenario) {
		final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
		try (ModelReference modelReference = modelRecord.aquireReference(PricingFromScenarioImportWizard.class.getSimpleName())) {
			modelReference.executeWithLock(false, () -> {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();

				final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
				final PortsVersion version = PortFromScenarioCopier.generateVersion(portModel);
				try {
					// PortsUploaderClient.saveVersion(BackEndUrlProvider.INSTANCE.getUrl(), version);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	private void openWizard(final Function<ScenarioInstance, IImportWizard> wizardFactory) {
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return;
		}

		ScenarioInstance currentInstance = null;
		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage != null) {
			final IEditorPart activeEditor = activePage.getActiveEditor();
			if (activeEditor != null) {
				final IEditorInput activeEditorInput = activeEditor.getEditorInput();
				if (activeEditorInput instanceof IScenarioServiceEditorInput) {
					final IScenarioServiceEditorInput editorInput = (IScenarioServiceEditorInput) activeEditorInput;
					currentInstance = editorInput.getScenarioInstance();
				}
			}
		}
		final IImportWizard wizard = wizardFactory.apply(currentInstance);

		wizard.init(activeWorkbenchWindow.getWorkbench(), null);

		final Shell parent = activeWorkbenchWindow.getShell();

		final WizardDialog dialog = new WizardDialog(parent, wizard);
		dialog.create();
		dialog.open();
	}
}
