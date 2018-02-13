package com.mmxlabs.lngdataserver.lng.exporters.pricing.ui;

import java.io.IOException;
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
import com.mmxlabs.lngdataserver.integration.pricing.PricingClient;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.pricing.PricingToScenarioCopier;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataservice.pricing.model.Version;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class PricingFromScenarioImportWizard extends Wizard implements IImportWizard {

	private ScenarioSelectionPage scenarioSelectionPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", null);
	}

	@Override
	public boolean performFinish() {

		String url = BackEndUrlProvider.INSTANCE.getUrl();

		try {
			List<ScenarioInstance> selectedScenarios = scenarioSelectionPage.getSelectedScenarios();

			// Do not fork otherwise this causes a dead lock for me (SG 2018/02/12)
			getContainer().run(false, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("copy from scenario", selectedScenarios.size());

					for (ScenarioInstance scenario : selectedScenarios) {
						ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
						try (ModelReference modelReference = modelRecord.aquireReference(PricingFromScenarioImportWizard.class.getSimpleName())) {
							modelReference.executeWithLock(false, () -> {
								LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();

								PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
								Version version = PricingFromScenarioCopier.generateVersion(pricingModel);
								try {
									PricingClient.saveVersion(url, version);
								} catch (IOException e) {
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
