package com.mmxlabs.lngdataserver.lng.importers.distances.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.activation.Activator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.distances.DefaultPortProvider;
import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.integration.distances.IDistanceProvider;
import com.mmxlabs.lngdataserver.integration.distances.ILocationProvider;
import com.mmxlabs.lngdataserver.integration.distances.LocationRepository;
import com.mmxlabs.lngdataserver.lng.importers.distances.PortAndDistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
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

	private @Nullable String versionIdentifier;

	private boolean autoSave;

	public DistancesToScenarioImportWizard(@Nullable String versionIdentifier, final ScenarioInstance currentInstance, boolean autoSave) {
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
							
							final IDistanceProvider distanceProvider = distanceRepository.getDistances(versionTag);

							final LocationRepository portRepository = new LocationRepository();
							ILocationProvider portProvider;
							portProvider = new DefaultPortProvider(versionTag, portRepository.getPorts(versionTag));
							final PortAndDistancesToScenarioCopier copier = new PortAndDistancesToScenarioCopier();

							for (final ScenarioInstance scenarioInstance : scenarioSelectionPage.getSelectedScenarios()) {

								final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
								try (ModelReference modelReference = modelRecord.aquireReference(DistancesToScenarioImportWizard.class.getSimpleName())) {
									modelReference.executeWithLock(true, () -> {
										monitor.subTask(String.format("Importing %s into %s", distanceProvider.getVersion(), modelRecord.getName()));

										final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
										final EditingDomain editingDomain = modelReference.getEditingDomain();
										final Command command = copier.getUpdateCommand(editingDomain, portProvider, distanceProvider, portModel);

										if (!command.canExecute()) {
											throw new RuntimeException("Unable to execute command");
										}
										RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
										monitor.worked(1);
										if (autoSave) {
											try {
												modelReference.save();
											} catch (IOException e) {
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

	@Override
	public IWizardPage getNextPage(final IWizardPage page) {
		if (page == distancesSelectionPage) {
			distancesSelectionPage.setChecked(true);
		}
		return super.getNextPage(page);
	}

}
