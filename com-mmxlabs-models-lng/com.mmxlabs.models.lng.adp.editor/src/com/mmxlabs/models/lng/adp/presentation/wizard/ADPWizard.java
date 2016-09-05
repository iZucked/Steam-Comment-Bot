/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

/**
 * Export the selected scenario to the filesystem somehow.
 * 
 * @author hinton
 * 
 */
public class ADPWizard extends Wizard implements IWorkbenchWizard {

	private LNGScenarioModel scenarioModel;
	private ADPModel adpModel;
	// private boolean modelPopulated = false;
	private ADPParametersPage paramsPage;
	private ADPContractsPage contractsPurchasePage;
	private ADPContractsPage contractsSalesPage;
	private SelectionWizardPage selectionPage;
	private ScenarioInstance instance;
	private ADPBindingPage bindingsPage;

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("ADP Wizard");
		selectionPage = new SelectionWizardPage(selection);

	}

	@Override
	public boolean canFinish() {
		return bindingsPage != null && super.canFinish();
	}

	@Override
	public boolean performFinish() {
		ScenarioInstance[] fork = new ScenarioInstance[1];
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					monitor.beginTask("Generate slot bindings", IProgressMonitor.UNKNOWN);
					try {
						ADPModelUtil.populateModel(scenarioModel, adpModel);
						// modelPopulated = true;
						ADPModelUtil.makeBindings(scenarioModel, adpModel);

						try {
							fork[0] = instance.getScenarioService().insert(instance, scenarioModel);
							fork[0].setName("ADP Plan");

							// Copy across various bits of information
							fork[0].getMetadata().setContentType(instance.getMetadata().getContentType());
							fork[0].getMetadata().setCreated(new Date());
							fork[0].getMetadata().setLastModified(new Date());

							// Copy version context information
							fork[0].setVersionContext(instance.getVersionContext());
							fork[0].setScenarioVersion(instance.getScenarioVersion());

							fork[0].setClientVersionContext(instance.getClientVersionContext());
							fork[0].setClientScenarioVersion(instance.getClientScenarioVersion());

						} catch (final IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} finally {
						monitor.done();
					}

				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}

		try {
			OpenScenarioUtils.openScenarioInstance(fork[0]);
		} catch (final PartInitException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if (page == selectionPage && paramsPage == null) {
			if (adpModel == null) {
				final Collection<ScenarioInstance> scenarioInstances = selectionPage.getScenarioInstance();
				if (scenarioInstances.size() == 1) {
					instance = scenarioInstances.iterator().next();

					try {
						getContainer().run(true, false, new IRunnableWithProgress() {

							@Override
							public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

								monitor.beginTask("Generate Model", IProgressMonitor.UNKNOWN);

								try (ModelReference ref = instance.getReference("ADPWizard")) {
									final LNGScenarioModel parentModel = (LNGScenarioModel) ref.getInstance();
									scenarioModel = ADPModelUtil.prepareModel(parentModel);
									adpModel = ADPModelUtil.createADPModel(scenarioModel);
									// Add to model now for EMF Forms to work correctly.
									scenarioModel.getExtensions().add(adpModel);
								} finally {
									monitor.done();
								}

							}
						});
					} catch (InvocationTargetException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					paramsPage = new ADPParametersPage(adpModel);
					contractsPurchasePage = new ADPContractsPage(adpModel, scenarioModel, true);
					contractsSalesPage = new ADPContractsPage(adpModel, scenarioModel, false);
					ADPModelUtil.createBindings(scenarioModel, adpModel);

					addPage(paramsPage);
					addPage(contractsPurchasePage);
					addPage(contractsSalesPage);

					bindingsPage = new ADPBindingPage(adpModel, scenarioModel);
					addPage(bindingsPage);
					return paramsPage;
					// getContainer().showPage(paramsPage);
				}
			}
			// } else if (contractsSalesPage != null && page == contractsSalesPage ) {
			// // } else if (!modelPopulated) {
			// try {
			// getContainer().run(true, false, new IRunnableWithProgress() {
			//
			// @Override
			// public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			//
			// monitor.beginTask("Generate Slots", IProgressMonitor.UNKNOWN);
			// try {
			//
			//
			// } finally {
			// monitor.done();
			// }
			//
			// }
			// });
			// } catch (InvocationTargetException | InterruptedException e) {
			// e.printStackTrace();
			// }
			//
			//
			//
			// return bindingsPage;
		}
		return super.getNextPage(page);
	}

	@Override
	public void addPages() {
		addPage(selectionPage);
	}
}