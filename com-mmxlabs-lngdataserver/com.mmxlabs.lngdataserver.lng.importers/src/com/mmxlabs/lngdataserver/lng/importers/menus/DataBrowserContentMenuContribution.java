package com.mmxlabs.lngdataserver.lng.importers.menus;

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
import com.mmxlabs.lngdataserver.lng.importers.distances.ui.DistancesToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.importers.port.ui.PortsToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.importers.pricing.ui.PricingToScenarioImportWizard;
import com.mmxlabs.lngdataserver.lng.importers.vessels.ui.VesselsToScenarioImportWizard;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class DataBrowserContentMenuContribution implements IDataBrowserContextMenuExtension {

	@Override
	public boolean contributeToMenu(@NonNull TreeSelection selection, @NonNull MenuManager menuManager) {
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
							openWizard((currentInstance) -> new DistancesToScenarioImportWizard(versionIdentifier, currentInstance));
						}));
						itemsAdded = true;
					} else if (Objects.equals(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), parentNode.getType())) {
						String versionIdentifier = node.getDisplayName();
						menuManager.add(new RunnableAction("Update scenario(s)", () -> {
							openWizard((currentInstance) -> new PricingToScenarioImportWizard(versionIdentifier, currentInstance));
						}));
						itemsAdded = true;
					} else if (Objects.equals(LNGScenarioSharedModelTypes.FLEET.getID(), parentNode.getType())) {
						String versionIdentifier = node.getDisplayName();
						menuManager.add(new RunnableAction("Update scenario(s)", () -> {
							openWizard((currentInstance) -> new VesselsToScenarioImportWizard(versionIdentifier, currentInstance));
						}));
						itemsAdded = true;
					} else if (Objects.equals(LNGScenarioSharedModelTypes.LOCATIONS.getID(), parentNode.getType())) {
						String versionIdentifier = node.getDisplayName();
						menuManager.add(new RunnableAction("Update scenario(s)", () -> {
							openWizard((currentInstance) -> new PortsToScenarioImportWizard(versionIdentifier, currentInstance));
						}));
						itemsAdded = true;
					}
				}
			}

		}
		return itemsAdded;

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
