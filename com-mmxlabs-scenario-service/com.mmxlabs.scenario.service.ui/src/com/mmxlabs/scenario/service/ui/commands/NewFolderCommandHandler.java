/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class NewFolderCommandHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection.isEmpty()) {
			// create a new folder in the top scenario service?
			final ServiceTracker<IScenarioService, IScenarioService> tracker = new ServiceTracker<IScenarioService, IScenarioService>(Activator.getDefault().getBundle().getBundleContext(), IScenarioService.class, null);
			tracker.open();
			
			final IScenarioService service = tracker.getService();
			if (service != null) {
				final ScenarioService top = service.getServiceModel();
				createFolderInContainer(activePage, top);
			}
			
			tracker.close();
		} else if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			final Object o = strucSelection.getFirstElement();
			if (o instanceof Container) {
				createFolderInContainer(activePage, (Container)o);
			}
		}
		return null;
	}

	private void createFolderInContainer(final IWorkbenchPage activePage,
			final Container o) {
		final InputDialog inputDialog = new InputDialog(activePage.getActivePart().getSite().getShell(), "Folder Name", "Enter a name for the new folder", "New folder", 
				new IInputValidator() {
					
					@Override
					public String isValid(String newText) {
						if (newText.isEmpty()) return "The folder's name cannot be empty";
						return null;
					}
				}
				);
		if (inputDialog.open() == Window.OK) {
			final String name = inputDialog.getValue();
			
			final Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
			f.setName(name);
			
			o.getElements().add(f);
		}
	}

}
