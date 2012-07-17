/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.commands.OpenScenarioCommandHandler;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the provided container. If the container resource (a folder or a project) is selected in the workspace when the wizard is
 * opened, it will accept it as the target container. The wizard creates one file with the extension "scn". If a sample multi-page editor (also available as a template) is registered for the same
 * extension, it will be able to open it.
 */

public class EmptyScenarioWizard extends Wizard implements INewWizard {

	private ScenarioServiceNewScenarioPage page;
	private ISelection selection;

	/**
	 * Constructor for EmptyScenarioWizard.
	 */
	public EmptyScenarioWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void setSelection(final ISelection selection) {
		this.selection = selection;
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		page = new ScenarioServiceNewScenarioPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final Container container = page.getScenarioContainer();
		final String fileName = page.getFileName();

		final IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException {
				monitor.beginTask("Creating " + fileName, 2);
				try {
					final List<EObject> models = new LinkedList<EObject>();
					ManifestJointModel.createEmptySubModels(models);

					final IScenarioService scenarioService = container.getScenarioService();

					final ScenarioInstance instance = scenarioService.insert(container, Collections.<ScenarioInstance> emptySet(), models);
					instance.setName(fileName);

					final Metadata metadata = instance.getMetadata();
					metadata.setCreated(new Date());
					metadata.setLastModified(new Date());
					metadata.setContentType("com.mmxlabs.shiplingo.platform.models.manifest.scnfile");

					monitor.worked(1);
					monitor.setTaskName("Opening file for editing...");
					getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
								OpenScenarioCommandHandler.openScenarioInstance(page, instance);
							} catch (final PartInitException e) {
							}
						}
					});
					monitor.worked(1);
				} catch (final IOException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (final InterruptedException e) {
			return false;
		} catch (final InvocationTargetException e) {
			final Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setSelection(selection);
	}
}