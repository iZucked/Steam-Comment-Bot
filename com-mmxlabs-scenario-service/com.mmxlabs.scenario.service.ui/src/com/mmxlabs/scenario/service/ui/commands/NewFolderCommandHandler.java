/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceNavigator;

public class NewFolderCommandHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveShellChecked(event);
		final ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {

			@Override
			public void run() {
				// No selection - either the root element is a scenario service or it is the registry (in which case the user should select the service)
				if (selection.isEmpty()) {

					final IWorkbenchPart part = HandlerUtil.getActivePart(event);
					if (part instanceof ScenarioServiceNavigator) {
						final ScenarioServiceNavigator navigator = (ScenarioServiceNavigator) part;
						final Object input = navigator.getCommonViewer().getInput();
						if (input instanceof Container) {
							createFolderInContainer(shell, (Container) input);
						} else if (input instanceof ScenarioModel) {
							final ScenarioModel scenarioModel = (ScenarioModel) input;
							final List<ScenarioService> services = scenarioModel.getScenarioServices();
							if (services.size() == 1) {
								// Exactly 1 - probably filtered out in navigator, so lets compensate
								createFolderInContainer(shell, services.get(0));
							} else {
								// Zero - nothing to do
								// > 1 - Service should be shown in navigator
							}

						}
						return;
					}

					// create a new folder in the top scenario service?
					ServiceHelper.withOptionalService(ScenarioServiceRegistry.class, serviceRegistry -> {
						if (serviceRegistry != null) {
							final Collection<IScenarioService> scenarioServices = serviceRegistry.getScenarioServices();
							if (scenarioServices.size() == 1) {

								final ScenarioService top = scenarioServices.iterator().next().getServiceModel();
								createFolderInContainer(shell, top);
							}
						}
					});
				} else if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					final Object o = strucSelection.getFirstElement();
					if (o instanceof Container) {
						createFolderInContainer(shell, (Container) o);
					}
				}
			}
		});
		return null;
	}

	private void createFolderInContainer(@NonNull final Shell shell, @NonNull final Container o) {

		final Set<String> existingNames = ScenarioServiceModelUtils.getExistingNames(o);

		final String prefix = "New folder";
		final String initialValue = ScenarioServiceModelUtils.getNextName(prefix, existingNames);

		final InputDialog inputDialog = new InputDialog(shell, "Folder Name", "Enter a name for the new folder", initialValue, new IInputValidator() {

			@Override
			public String isValid(final String newText) {
				if (newText.isEmpty()) {
					return "The folder's name cannot be empty";
				} else if (existingNames.contains(newText)) {
					return "Name already exists";
				}
				return null;
			}
		});
		if (inputDialog.open() == Window.OK) {
			final String name = inputDialog.getValue();
			assert name != null;
			o.getScenarioService().makeFolder(o, name);
		}
	}
}
