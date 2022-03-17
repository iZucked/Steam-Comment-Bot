/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.port.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.integration.ports.PortsRepository;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.lng.io.port.PortFromScenarioCopier;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class PortsFromScenarioImportWizard extends Wizard implements IImportWizard {

	private ScenarioSelectionPage scenarioSelectionPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", null);
	}

	@Override
	public boolean performFinish() {

		try {
			List<ScenarioInstance> selectedScenarios = scenarioSelectionPage.getSelectedScenarios();

			// Do not fork otherwise this causes a dead lock for me (SG 2018/02/12)
			getContainer().run(false, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("copy from scenario", selectedScenarios.size());

					for (ScenarioInstance scenario : selectedScenarios) {
						ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
						try (ModelReference modelReference = modelRecord.aquireReference(PortsFromScenarioImportWizard.class.getSimpleName())) {
							modelReference.executeWithLock(false, () -> {
								LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();

								PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
								PortsVersion version = PortFromScenarioCopier.generateVersion(portModel);
								try {
									PortsRepository.INSTANCE.publishVersion(version);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								monitor.worked(1);
							});
						}
					}
				}
			});
		} catch (

		final InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (final InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(scenarioSelectionPage);
	}

	@Override
	public boolean canFinish() {
		return scenarioSelectionPage.isPageComplete();
	}
}
