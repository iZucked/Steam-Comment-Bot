package com.mmxlabs.lngdataserver.lng.importers.distances.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.distances.IDistanceProvider;
import com.mmxlabs.lngdataserver.lng.importers.distances.DistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class DistancesToScenarioImportWizard extends Wizard implements IImportWizard {

	private static final Logger LOG = LoggerFactory.getLogger(DistancesToScenarioImportWizard.class);

	private ScenarioSelectionPage scenarioSelectionPage;
	private DistancesSelectionPage distancesSelectionPage;
	private DistanceSanityCheckPage distanceSanityCheckPage;
	private boolean canFinish = false;

	private IDistanceProvider distanceProvider;

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Import distances to scenario");
		setNeedsProgressMonitor(true);

		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", null);
		distancesSelectionPage = new DistancesSelectionPage("Distances");
		distanceSanityCheckPage = new DistanceSanityCheckPage("Verify Distances", distancesSelectionPage);
	}

	@Override
	public boolean performFinish() {
		checkDistances();

		if (distanceProvider == null) {
			return false;
		}

		if (distancesSelectionPage.getLostDistances().isEmpty()) {
			canFinish = true;
		}

		if (canFinish) {
			try {
				getContainer().run(true, true, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Copy distances", scenarioSelectionPage.getSelectedScenarios().size() * 3);

						final DistancesToScenarioCopier copier = new DistancesToScenarioCopier();

						for (final ScenarioInstance scenarioInstance : scenarioSelectionPage.getSelectedScenarios()) {

							final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
							try (ModelReference modelReference = modelRecord.aquireReference(DistancesToScenarioImportWizard.class.getSimpleName())) {
								modelReference.executeWithLock(true, () -> {
									monitor.subTask(String.format("Importing %s into %s", distanceProvider.getVersion(), modelRecord.getName()));

									PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
									EditingDomain editingDomain = modelReference.getEditingDomain();
									final Pair<Command, Map<RouteOption, List<RouteLine>>> updateDistancesCommand = copier.getUpdateDistancesCommand(editingDomain, distanceProvider, portModel);

									Command command = updateDistancesCommand.getFirst();

									if (!command.canExecute()) {
										throw new RuntimeException("Unable to execute command");
									}

									RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
									monitor.worked(1);
								});
							}
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
		addPage(distanceSanityCheckPage);
	}

	@Override
	public boolean canFinish() {
		return distancesSelectionPage.isPageComplete();
	}

	public IWizardPage getNextPage(final IWizardPage page) {
		if (page == distancesSelectionPage) {
			checkDistances();
		}
		return super.getNextPage(page);
	}

	private void checkDistances() {
		if (!distancesSelectionPage.isChecked()) {
			try {
				getContainer().run(false, true, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						final DistancesToScenarioCopier copier = new DistancesToScenarioCopier();
						monitor.beginTask("task", scenarioSelectionPage.getSelectedScenarios().size() * 3);

						final DistanceRepository dr = new DistanceRepository(BackEndUrlProvider.INSTANCE.getUrl());
						distanceProvider = dr.getDistances(distancesSelectionPage.getVersionTag());

						for (final ScenarioInstance scenarioInstance : scenarioSelectionPage.getSelectedScenarios()) {
							if (monitor.isCanceled()) {
								throw new InterruptedException("Cancelled");
							}
							final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

							try (ModelReference modelReference = modelRecord.aquireReference(DistancesToScenarioImportWizard.class.getSimpleName())) {
								modelReference.executeWithLock(true, () -> {

									final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
									final LNGReferenceModel referenceModel = scenarioModel.getReferenceModel();

									final EditingDomain editingDomain = modelReference.getEditingDomain();

									monitor.subTask(String.format("Importing %s into %s", distanceProvider.getVersion(), referenceModel.getUuid()));

									final Pair<Command, Map<RouteOption, List<RouteLine>>> updateDistancesCommand = copier.getUpdateDistancesCommand(editingDomain, distanceProvider,
											referenceModel.getPortModel());

									if (!updateDistancesCommand.getSecond().isEmpty()) {
										distancesSelectionPage.getLostDistances().put(scenarioInstance, updateDistancesCommand.getSecond());
									}

								});
							}

						}
					}
				});
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			distancesSelectionPage.setChecked(true);
		}
	}
}
