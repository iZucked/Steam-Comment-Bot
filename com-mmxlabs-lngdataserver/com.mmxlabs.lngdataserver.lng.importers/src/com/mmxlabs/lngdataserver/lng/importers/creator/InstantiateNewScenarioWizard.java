/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.creator;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;

import com.mmxlabs.models.lng.scenario.wizards.ScenarioServiceNewScenarioPage;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

public class InstantiateNewScenarioWizard extends Wizard implements IImportWizard {

	ScenarioServiceNewScenarioPage mainPage;
	private ScenarioInstance newInstance;

	public InstantiateNewScenarioWizard() {
		super();
		this.setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		doDoImport();

		final ScenarioInstance instance = newInstance;
		
		if (instance != null) {
			try {
				OpenScenarioUtils.openScenarioInstance(instance);
			} catch (PartInitException e) {
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("CSV Import Wizard"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new ScenarioServiceNewScenarioPage(selection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	public void doDoImport() {
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {

				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					monitor.beginTask("Import Scenario", 3);
					try {

						
						ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
						sb.loadDefaultData();
						sb.createDummyPricingData();
						
						final IScenarioDataProvider scenarioDataProvider = sb.getScenarioDataProvider();
						if (scenarioDataProvider != null) {

							monitor.worked(1);

							final Container container = mainPage.getScenarioContainer();
							assert container != null;

							final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(container);

							try {
								final ScenarioInstance newInstance = scenarioService.copyInto(container, scenarioDataProvider, mainPage.getFileName(), new SubProgressMonitor(monitor, 1));
								monitor.worked(1);

								InstantiateNewScenarioWizard.this.setScenarioInstance(newInstance);

							} catch (final Exception e) {
								// NOTE: in Java SE 7 we can incorporate this into the previous
								// exception block as catch(final IllegalArgumentException|IOException e)
								// getContainer().setErrorMessage(e.getMessage());
							}
						}
					} catch ( Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void setScenarioInstance(ScenarioInstance newInstance) {
		this.newInstance = newInstance;

	}
}
