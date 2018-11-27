/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.pricing.ui;

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
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.lng.importers.pricing.PricingToScenarioCopier;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.scenario.mergeWizards.ScenarioSelectionPage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class PricingToScenarioImportWizard extends Wizard implements IImportWizard {

	private ScenarioSelectionPage scenarioSelectionPage;
	private PricingSelectionPage pricingSelectionPage;
	private final String versionIdentifier;
	private final ScenarioInstance currentInstance;
	private final boolean autoSave;

	public PricingToScenarioImportWizard(final String versionIdentifier, final ScenarioInstance currentInstance, final boolean autoSave) {
		this.versionIdentifier = versionIdentifier;
		this.currentInstance = currentInstance;
		this.autoSave = autoSave;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		scenarioSelectionPage = new ScenarioSelectionPage("Scenario", currentInstance);
		if (versionIdentifier == null) {
			pricingSelectionPage = new PricingSelectionPage("Pricing");
		}

	}

	@Override
	public boolean performFinish() {
		IPricingProvider pricingProvider;
		if (versionIdentifier != null) {
			try {
				pricingProvider = PricingRepository.INSTANCE.getPricingProvider(versionIdentifier);
			} catch (final IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			pricingProvider = pricingSelectionPage.getPricingVersion();
		}
		try {
			final List<ScenarioInstance> selectedScenarios = scenarioSelectionPage.getSelectedScenarios();

			// Do not fork otherwise this causes a dead lock for me (SG 2018/02/12)
			getContainer().run(false, true, new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("copy into scenario", selectedScenarios.size());

					for (final ScenarioInstance scenario : selectedScenarios) {
						final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenario);
						try (ModelReference modelReference = modelRecord.aquireReference(PricingToScenarioImportWizard.class.getSimpleName())) {
							modelReference.executeWithLock(true, () -> {
								final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
								final EditingDomain editingDomain = modelReference.getEditingDomain();
								final Command updatePricingCommand = PricingToScenarioCopier.getUpdatePricingCommand(editingDomain, pricingProvider, ScenarioModelUtil.getPricingModel(scenarioModel));

								if (!updatePricingCommand.canExecute()) {
									throw new RuntimeException("Unable to copy pricing information to scenario");
								}
								try {
									RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(updatePricingCommand));
									monitor.worked(1);
									if (autoSave) {
										modelReference.save();
									}
								} catch (final Exception e) {
									e.printStackTrace();

								}
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
		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(scenarioSelectionPage);
		if (pricingSelectionPage != null) {
			addPage(pricingSelectionPage);
		}
	}

	@Override
	public boolean canFinish() {
		return pricingSelectionPage == null || pricingSelectionPage.isPageComplete();
	}
}
