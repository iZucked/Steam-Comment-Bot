/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.editor.actions;

import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.presentation.ScenarioEditor;

/**
 * Action to take static data from a non-linked scenario.
 * 
 * @author Tom Hinton
 * 
 */
public class ExtractStaticDataAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(final IAction action) {
		if (window.getActivePage().getActiveEditor() instanceof ScenarioEditor) {
			final ScenarioEditor editor = (ScenarioEditor) window
					.getActivePage().getActiveEditor();
			// get selection
			final Scenario scenario = editor.getScenario();

			final ContainerSelectionDialog csd = new ContainerSelectionDialog(
					window.getShell(),
					ResourcesPlugin.getWorkspace().getRoot(), true,
					"Location for new static data");

			if (csd.open() == Window.OK) {
				final InputDialog id = new InputDialog(window.getShell(),
						"Static Data File Name",
						"Enter the name for the static data model",
						scenario.getName() + ".portmodel",
						new IInputValidator() {
							@Override
							public String isValid(String newText) {
								return (newText.endsWith(".portmodel") && newText
										.length() > ".portmodel".length()) ? null
										: "Name must end with .portmodel";
							}
						});

				if (id.open() == Window.OK) {
					
					final Path f = (Path) csd.getResult()[0];
					final String nameString = id.getValue();

					final URI uri = URI.createPlatformResourceURI(f + "/" + nameString, true);
					
					
					final Scenario staticData = ScenarioFactory.eINSTANCE
							.createScenario();
					staticData.setName(nameString.substring(0,
							nameString.length() - ".portmodel".length()));
					
					final Resource newResource = scenario.eResource().getResourceSet().createResource(uri);
					newResource.getContents().add(staticData);

					final EList<EObject> containedModels = staticData.getContainedModels();
					staticData.setPortModel(scenario.getPortModel());
					staticData.setDistanceModel(scenario.getDistanceModel());
					staticData.setCanalModel(scenario.getCanalModel());
					
					containedModels.add(staticData.getPortModel());
					containedModels.add(staticData.getDistanceModel());
					containedModels.add(staticData.getCanalModel());
					try {
						newResource.save(null);
						scenario.eResource().save(null);
					} catch (IOException e) {
						displayErrorMessage("Error creating static data: " +e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Throw up an error dialog
	 * 
	 * @param string
	 */
	private void displayErrorMessage(String string) {

	}

	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void init(final IWorkbenchWindow window) {
		this.window = window;
	}

}
