/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.distances.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesToScenarioCopier;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class DistancesToScenarioImportWizard extends Wizard implements IImportWizard {

	private static final Logger LOG = LoggerFactory.getLogger(DistancesToScenarioImportWizard.class);

	private ScenarioSelectionPage scenarioSelectionPage;
	private DistancesSelectionPage distancesSelectionPage;

	private final ScenarioInstance currentInstance;

	private @Nullable final String versionIdentifier;

	private final boolean autoSave;

	public DistancesToScenarioImportWizard(@Nullable final String versionIdentifier, final ScenarioInstance currentInstance, final boolean autoSave) {
		this.versionIdentifier = versionIdentifier;
		this.currentInstance = currentInstance;
		this.autoSave = autoSave;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Import distances to scenario");
		setNeedsProgressMonitor(true);

		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", currentInstance);
		if (versionIdentifier == null) {
			distancesSelectionPage = new DistancesSelectionPage("Distances");
		}
	}

	@Override
	public boolean performFinish() {
		{
			final String versionTag = versionIdentifier != null ? versionIdentifier : distancesSelectionPage.getVersionTag();
			try {
				// Do not fork otherwise this causes a dead lock for me (SG 2018/02/12)
				getContainer().run(false, true, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Copy distances", scenarioSelectionPage.getSelectedScenarios().size() * 3);

						try {
							final DistanceRepository distanceRepository = DistanceRepository.INSTANCE;

							final DistancesVersion version = distanceRepository.getLocalVersion(versionTag);
							for (final ScenarioInstance scenarioInstance : scenarioSelectionPage.getSelectedScenarios()) {

								final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
								try (ModelReference modelReference = modelRecord.aquireReference(DistancesToScenarioImportWizard.class.getSimpleName())) {
									modelReference.executeWithLock(true, () -> {
										monitor.subTask(String.format("Importing %s into %s", versionTag, modelRecord.getName()));

										final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
										final EditingDomain editingDomain = modelReference.getEditingDomain();
										final Command command = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, version, false);

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
		if (distancesSelectionPage != null) {
			addPage(distancesSelectionPage);
		}
	}

	@Override
	public boolean canFinish() {
		return distancesSelectionPage == null || distancesSelectionPage.isPageComplete();
	}
}
