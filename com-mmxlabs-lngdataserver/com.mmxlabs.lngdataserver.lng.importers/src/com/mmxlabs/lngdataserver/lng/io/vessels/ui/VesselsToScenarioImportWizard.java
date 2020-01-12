/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.vessels.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.integration.vessels.VesselsRepository;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class VesselsToScenarioImportWizard extends Wizard implements IImportWizard {

	private ScenarioSelectionPage scenarioSelectionPage;
	private VesselsSelectionPage vesselsSelectionPage;
	private String versionIdentifier;
	private ScenarioInstance currentInstance;
	private boolean autoSave;

	public VesselsToScenarioImportWizard(String versionIdentifier, ScenarioInstance currentInstance, boolean autoSave) {
		this.versionIdentifier = versionIdentifier;
		this.currentInstance = currentInstance;
		this.autoSave = autoSave;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", currentInstance);
		if (versionIdentifier == null) {
			vesselsSelectionPage = new VesselsSelectionPage("Vessels");
		}
	}

	@Override
	public boolean performFinish() {
		{
			final String versionTag = versionIdentifier != null ? versionIdentifier : vesselsSelectionPage.getVersionTag();
			try {
				// Do not fork otherwise this causes a dead lock for me (SG 2018/02/12)
				getContainer().run(false, true, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Copy ports", scenarioSelectionPage.getSelectedScenarios().size() * 3);

						try {
							final VesselsRepository vesselsRepository = VesselsRepository.INSTANCE;

							final VesselsVersion version = vesselsRepository.getLocalVersion(versionTag);
							for (final ScenarioInstance scenarioInstance : scenarioSelectionPage.getSelectedScenarios()) {

								final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
								try (ModelReference modelReference = modelRecord.aquireReference(VesselsToScenarioImportWizard.class.getSimpleName())) {
									modelReference.executeWithLock(true, () -> {
										monitor.subTask(String.format("Importing %s into %s", versionTag, modelRecord.getName()));

										final FleetModel fleetModel = ScenarioModelUtil.getFleetModel((LNGScenarioModel) modelReference.getInstance());
										final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
										final EditingDomain editingDomain = modelReference.getEditingDomain();
										final Command command = VesselsToScenarioCopier.getUpdateCommand(editingDomain, fleetModel, portModel, version);

										if (!command.canExecute()) {
											throw new RuntimeException("Unable to execute command");
										}
										RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
										monitor.worked(1);
										if (autoSave) {
											try {
												modelReference.save();
											} catch (final IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									});
								}
							}
						} catch (final Exception e) {
							e.printStackTrace();

						}

					}
				});
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
				return false;
			} catch (final InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(scenarioSelectionPage);
		if (vesselsSelectionPage != null) {
			addPage(vesselsSelectionPage);
		}
	}

	@Override
	public boolean canFinish() {
		return vesselsSelectionPage == null || vesselsSelectionPage.isPageComplete();
	}
}
