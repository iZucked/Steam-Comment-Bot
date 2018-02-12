package com.mmxlabs.lngdataserver.lng.importers.pricing.ui;

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

import com.mmxlabs.lngdataserver.integration.pricing.IPricingProvider;
import com.mmxlabs.lngdataserver.lng.importers.pricing.PricingToScenarioCopier;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class PricingToScenarioImportWizard extends Wizard implements IImportWizard {

	private ScenarioSelectionPage scenarioSelectionPage;
	private PricingSelectionPage pricingSelectionPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", null);
		pricingSelectionPage = new PricingSelectionPage("Pricing");

	}

	@Override
	public boolean performFinish() {
		IPricingProvider pricingProvider = pricingSelectionPage.getPricingVersion();
		try {
			List<ScenarioInstance> selectedScenarios = scenarioSelectionPage.getSelectedScenarios();

			// Do not fork otherwise this causes a dead lock for me (SG 2018/02/12)
			getContainer().run(false, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("copy into scenario", selectedScenarios.size());

					for (ScenarioInstance scenario : selectedScenarios) {
						ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
						try (ModelReference modelReference = modelRecord.aquireReference(PricingToScenarioImportWizard.class.getSimpleName())) {
							modelReference.getLock().lock();
							LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();

							EditingDomain editingDomain = modelReference.getEditingDomain();

							Command updatePricingCommand = PricingToScenarioCopier.getUpdatePricingCommand(editingDomain, pricingProvider, scenarioModel.getReferenceModel().getPricingModel());

							if (!updatePricingCommand.canExecute()) {
								throw new RuntimeException("Unable to copy pricing information to scenario");
							}
							try {
								RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(updatePricingCommand));
								monitor.worked(1);

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
		addPage(pricingSelectionPage);
	}

	@Override
	public boolean canFinish() {
		return pricingSelectionPage.isPageComplete();
	}
}
