/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.internal.Activator;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceNavigator;

public class NewFolderCommandHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				// No selection - either the root element is a scenario service or it is the registry (in which case the user should select the service)
				if (selection.isEmpty()) {

					final IWorkbenchPart part = HandlerUtil.getActivePart(event);
					if (part instanceof ScenarioServiceNavigator) {
						final ScenarioServiceNavigator navigator = (ScenarioServiceNavigator) part;
						final Object input = navigator.getCommonViewer().getInput();
						if (input instanceof Container) {
							createFolderInContainer(activePage, (Container) input);
						} else if (input instanceof ScenarioModel) {
							final ScenarioModel scenarioModel = (ScenarioModel) input;
							final List<ScenarioService> services = scenarioModel.getScenarioServices();
							if (services.size() == 1) {
								// Exactly 1 - probably filtered out in navigator, so lets compensate
								createFolderInContainer(activePage, services.get(0));
							} else {
								// Zero - nothing to do
								// > 1 - Service should be shown in navigator
							}

						}
						return;
					}

					// create a new folder in the top scenario service?
					final ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(Activator.getDefault()
							.getBundle().getBundleContext(), ScenarioServiceRegistry.class, null);
					tracker.open();

					final ScenarioServiceRegistry serviceRegistry = tracker.getService();
					if (serviceRegistry != null) {

						final Collection<IScenarioService> scenarioServices = serviceRegistry.getScenarioServices();
						if (scenarioServices.size() == 1) {

							final ScenarioService top = scenarioServices.iterator().next().getServiceModel();
							createFolderInContainer(activePage, top);
						}
					}

					tracker.close();
				} else if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					final Object o = strucSelection.getFirstElement();
					if (o instanceof Container) {
						createFolderInContainer(activePage, (Container) o);
					}
				}
			}
		});
		return null;
	}

	private void createFolderInContainer(final IWorkbenchPage activePage, final Container o) {

		final Set<String> folderNames = new HashSet<String>();
		for (final Container c : o.getElements()) {
			if (c instanceof Folder) {
				folderNames.add(((Folder) c).getName());
			} else if (c instanceof ScenarioInstance) {
				folderNames.add(((ScenarioInstance) c).getName());
			}
		}

		final String prefix = "New folder";
		String initialValue = prefix;
		int counter = 1;
		while (folderNames.contains(initialValue)) {
			initialValue = prefix + " (" + counter++ + ")";
		}

		final InputDialog inputDialog = new InputDialog(activePage.getActivePart().getSite().getShell(), "Folder Name", "Enter a name for the new folder", initialValue, new IInputValidator() {

			@Override
			public String isValid(final String newText) {
				if (newText.isEmpty())
					return "The folder's name cannot be empty";
				return null;
			}
		});
		if (inputDialog.open() == Window.OK) {
			final String name = inputDialog.getValue();

			o.getScenarioService().makeFolder(o, name);
		}
	}
}
