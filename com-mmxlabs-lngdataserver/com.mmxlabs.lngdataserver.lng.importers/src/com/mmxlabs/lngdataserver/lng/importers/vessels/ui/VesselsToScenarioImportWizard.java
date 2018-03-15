package com.mmxlabs.lngdataserver.lng.importers.vessels.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.lngdataserver.integration.vessels.IVesselsProvider;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsRepository;
import com.mmxlabs.lngdataserver.lng.importers.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

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
		IVesselsProvider vesselsProvider;
		if (versionIdentifier != null) {
			try {
				vesselsProvider = VesselsRepository.INSTANCE.getVesselsProvider(versionIdentifier);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			vesselsProvider = vesselsSelectionPage.getVesselsVersion();
		}

		try {
			List<ScenarioInstance> selectedScenarios = scenarioSelectionPage.getSelectedScenarios();

			// Do not fork otherwise this causes a dead lock for me (SG 2018/02/12)
			getContainer().run(false, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("copy into scenario", selectedScenarios.size());

					for (ScenarioInstance scenario : selectedScenarios) {
						ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
						try (ModelReference modelReference = modelRecord.aquireReference(VesselsToScenarioImportWizard.class.getSimpleName())) {
							modelReference.getLock().lock();
							LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();

							EditingDomain editingDomain = modelReference.getEditingDomain();

							Command updateCommand = VesselsToScenarioCopier.getUpdateVesselsCommand(editingDomain, vesselsProvider, scenarioModel.getReferenceModel().getFleetModel());

							if (!updateCommand.canExecute()) {
								throw new RuntimeException("Unable to copy vessel information to scenario");
							}
							try {
								RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(updateCommand));
								monitor.worked(1);

								if (autoSave) {
									modelReference.save();
								}

							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(false);
								modelReference.getLock().unlock();
							}
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
