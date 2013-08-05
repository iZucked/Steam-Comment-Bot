/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Collection;
import java.util.HashSet;
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
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class NewFolderCommandHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				// No selection - either the root element is a scenario service or it is the registry (in which case the user should select the service)
				if (selection.isEmpty()) {
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
		for (Container c : o.getElements()) {
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
			public String isValid(String newText) {
				if (newText.isEmpty())
					return "The folder's name cannot be empty";
				return null;
			}
		});
		if (inputDialog.open() == Window.OK) {
			final String name = inputDialog.getValue();

			final Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
			f.setName(name);

			o.getElements().add(f);
		}
	}
}
