package com.mmxlabs.lngdataserver.lng.importers.distances.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import com.mmxlabs.lngdataserver.integration.distances.IPortProvider;
import com.mmxlabs.lngdataserver.integration.distances.PortRepository;
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
	// private DistanceSanityCheckPage distanceSanityCheckPage;
	private boolean canFinish = false;

	// private IDistanceProvider distanceProvider;
	// private String version;

	private final ScenarioInstance currentInstance;

	public DistancesToScenarioImportWizard(final ScenarioInstance currentInstance) {
		this.currentInstance = currentInstance;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Import distances to scenario");
		setNeedsProgressMonitor(true);

		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", currentInstance);
		distancesSelectionPage = new DistancesSelectionPage("Distances");
		// distanceSanityCheckPage = new DistanceSanityCheckPage("Verify Distances", distancesSelectionPage);
	}

	@Override
	public boolean performFinish() {
		checkDistances();
		//
		// if (distanceProvider == null) {
		// return false;
		// }
		//
		// if (distancesSelectionPage.getLostDistances().isEmpty()) {
		// }
		canFinish = true;

		if (canFinish) {
			try {
				// Do not fork otherwise this causes a dead lock for me (SG 2018/02/12)
				getContainer().run(false, true, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Copy distances", scenarioSelectionPage.getSelectedScenarios().size() * 3);

						try {
							final DistanceRepository distanceRepository = new DistanceRepository(BackEndUrlProvider.INSTANCE.getUrl());
							final String versionTag = distancesSelectionPage.getVersionTag();
							final IDistanceProvider distanceProvider = distanceRepository.getDistances(versionTag);

							final PortRepository portRepository = new PortRepository(BackEndUrlProvider.INSTANCE.getUrl());
							IPortProvider portProvider;
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
									});
								}
							}
						} catch (final Exception e) {
							// TODO Auto-generated catch block
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
		addPage(distancesSelectionPage);
	}

	@Override
	public boolean canFinish() {
		return distancesSelectionPage.isPageComplete();
	}

	@Override
	public IWizardPage getNextPage(final IWizardPage page) {
		if (page == distancesSelectionPage) {
			checkDistances();
		}
		return super.getNextPage(page);
	}

	private void checkDistances() {
		// if (!distancesSelectionPage.isChecked()) {
		// try {
		// getContainer().run(false, true, new IRunnableWithProgress() {
		//
		// @Override
		// public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		// final DistancesToScenarioCopier copier = new DistancesToScenarioCopier();
		// monitor.beginTask("task", scenarioSelectionPage.getSelectedScenarios().size() * 3);
		//
		// final DistanceRepository dr = new DistanceRepository(BackEndUrlProvider.INSTANCE.getUrl());
		// distanceProvider = dr.getDistances(distancesSelectionPage.getVersionTag());
		//
		// for (final ScenarioInstance scenarioInstance : scenarioSelectionPage.getSelectedScenarios()) {
		// if (monitor.isCanceled()) {
		// throw new InterruptedException("Cancelled");
		// }
		// final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		//
		// try (ModelReference modelReference = modelRecord.aquireReference(DistancesToScenarioImportWizard.class.getSimpleName())) {
		// modelReference.executeWithLock(true, () -> {
		//
		// final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
		// final LNGReferenceModel referenceModel = scenarioModel.getReferenceModel();
		//
		// final EditingDomain editingDomain = modelReference.getEditingDomain();
		//
		// monitor.subTask(String.format("Importing %s into %s", distanceProvider.getVersion(), referenceModel.getUuid()));
		//
		// final Pair<Command, Map<RouteOption, List<RouteLine>>> updateDistancesCommand = null;// copier.getUpdateDistancesCommand(editingDomain, distanceProvider,
		// // referenceModel.getPortModel());
		//
		// if (!updateDistancesCommand.getSecond().isEmpty()) {
		// distancesSelectionPage.getLostDistances().put(scenarioInstance, updateDistancesCommand.getSecond());
		// }
		//
		// });
		// }
		//
		// }
		// }
		// });
		// } catch (final InvocationTargetException e) {
		// e.printStackTrace();
		// } catch (final InterruptedException e) {
		// e.printStackTrace();
		// }
		distancesSelectionPage.setChecked(true);
		// }
	}
}
