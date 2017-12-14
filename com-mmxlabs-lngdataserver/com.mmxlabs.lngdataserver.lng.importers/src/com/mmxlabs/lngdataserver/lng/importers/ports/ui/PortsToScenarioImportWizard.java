package com.mmxlabs.lngdataserver.lng.importers.ports.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.distances.DefaultPortProvider;
import com.mmxlabs.lngdataserver.distances.IPortProvider;
import com.mmxlabs.lngdataserver.distances.PortRepository;
import com.mmxlabs.lngdataserver.lng.importers.ports.PortsToScenarioCopier;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class PortsToScenarioImportWizard extends Wizard implements IImportWizard {

	private static final Logger LOG = LoggerFactory.getLogger(PortsToScenarioImportWizard.class);

	private ScenarioSelectionPage scenarioSelectionPage;
	private PortsSelectionPage portsSelectionPage;
	// private DistanceSanityCheckPage distanceSanityCheckPage;
	private boolean canFinish = false;

	private IPortProvider portProvider;

	private final ScenarioInstance currentInstance;

	public PortsToScenarioImportWizard(final ScenarioInstance currentInstance) {
		this.currentInstance = currentInstance;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Import distances to scenario");
		setNeedsProgressMonitor(true);

		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", currentInstance);
		portsSelectionPage = new PortsSelectionPage("Ports");
		// distanceSanityCheckPage = new DistanceSanityCheckPage("Verify Distances",
		// distancesSelectionPage);
	}

	@Override
	public boolean performFinish() {

		final PortRepository dr = new PortRepository(BackEndUrlProvider.INSTANCE.getUrl());
		try {
			portProvider = new DefaultPortProvider(portsSelectionPage.getVersionTag(), dr.getPorts(portsSelectionPage.getVersionTag()));
		} catch (final AuthenticationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (portProvider == null) {
			return false;
		}

		canFinish = true;

		if (canFinish) {
			try {
				getContainer().run(true, true, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Copy ports", scenarioSelectionPage.getSelectedScenarios().size() * 3);

						final PortsToScenarioCopier copier = new PortsToScenarioCopier();

						for (final ScenarioInstance scenarioInstance : scenarioSelectionPage.getSelectedScenarios()) {

							final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
							try (ModelReference modelReference = modelRecord.aquireReference(PortsToScenarioImportWizard.class.getSimpleName())) {
								modelReference.executeWithLock(true, () -> {
									monitor.subTask(String.format("Importing %s into %s", portProvider.getVersion(), modelRecord.getName()));

									final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
									final EditingDomain editingDomain = modelReference.getEditingDomain();
									final Pair<Command, Collection<Port>> command_result = copier.getUpdateCommand(editingDomain, portProvider, portModel);

									Collection<Port> lostPorts = command_result.getSecond();
									if (!lostPorts.isEmpty()) {
										boolean[] ret = new boolean[1];
										// TODO: Copy this to first check
										// TODO: Remove unlinked ports from this list (already covered).
										// TODO: Move this message box stuff out of here!
										String portList = lostPorts.stream().map(p -> p.getName()).reduce((x, y) -> x + "," + y).get();
										RunnerHelper.syncExec(() -> {
											ret[0] = MessageDialog.openQuestion(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Port update", "Some ports (" + portList
													+ ") are not included in the new data and will be removed during update. Merge or link ports before applying update to avoid this. Otherwise press 'Yes' to continue.");
										});

										if (ret[0]) {
											Command command = command_result.getFirst();
											if (!command.canExecute()) {
												throw new RuntimeException("Unable to execute command");
											}

											RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
										}
									}
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
		addPage(portsSelectionPage);
		// addPage(distanceSanityCheckPage);
	}

	@Override
	public boolean canFinish() {
		return portsSelectionPage.isPageComplete();
	}

	public IWizardPage getNextPage(final IWizardPage page) {
		if (page == portsSelectionPage) {
			// checkDistances();
		}
		return super.getNextPage(page);
	}

}
