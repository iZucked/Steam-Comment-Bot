/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.mergeWizards;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.mergeWizards.SharedDataScenariosSelectionPage.DataOptions;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.merge.EMFModelMergeTools;
import com.mmxlabs.models.ui.merge.IMappingDescriptor;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class SharedScenarioDataImportWizard extends Wizard implements IImportWizard {

	private static final Logger log = LoggerFactory.getLogger(SharedScenarioDataImportWizard.class);

	private SharedDataScenariosSelectionPage sourcePage;
	private ScenarioSelectionPage destinationPage;
	private final ScenarioInstance currentScenario;

	// private ImportWarningsPage warnings;

	public SharedScenarioDataImportWizard(final ScenarioInstance currentScenario) {
		super();
		this.currentScenario = currentScenario;
		this.setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		final UpdateJob job = new UpdateJob(sourcePage.getSelectedDataOptions(), sourcePage.getScenarioInstance(), destinationPage.getSelectedScenarios());

		try {
			getContainer().run(true, true, job);
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Shared Scenario Data Import"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		sourcePage = new SharedDataScenariosSelectionPage(selection);
		destinationPage = new ScenarioSelectionPage("Scenario Selection", currentScenario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		addPage(sourcePage);
		addPage(destinationPage);
		// addPage(warnings);
	}

	@Override
	public boolean canFinish() {
		return destinationPage.isPageComplete();// warnings.isPageComplete();
	}

	private class UpdateJob implements IRunnableWithProgress {

		private final ScenarioInstance sourceScenario;
		private final List<ScenarioInstance> destinationScenarios;
		private final Set<DataOptions> dataOptions;

		public UpdateJob(final Set<DataOptions> enumSet, final ScenarioInstance sourceScenario, final List<ScenarioInstance> destinationScenarios) {
			this.dataOptions = enumSet;
			this.sourceScenario = sourceScenario;
			this.destinationScenarios = destinationScenarios;
		}

		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			if (sourceScenario == null) {
				return;
			}

			try (final ModelReference sourceModelRef = sourceScenario.getReference()) {
				final EObject sourceRoot = sourceModelRef.getInstance();

				if (sourceRoot == null) {
					log.error("Unable to load scenario " + sourceScenario.getName() + ".", new RuntimeException());
					return;
				}

				monitor.beginTask("Update Scenarios", destinationScenarios.size());
				try {
					for (final ScenarioInstance destScenario : destinationScenarios) {

						if (monitor.isCanceled()) {
							return;
						}

						monitor.subTask("Update " + destScenario.getName());
						// Do not import into self
						if (destScenario == sourceScenario) {
							monitor.worked(1);
							continue;
						}

						try (final ModelReference destModelRef = destScenario.getReference()) {

							final EObject destRoot = destModelRef.getInstance();

							if (destRoot == null) {
								log.error("Unable to load scenario " + destScenario.getName() + ". skipping.", new RuntimeException());
								monitor.worked(1);
								continue;
							}

							final ScenarioLock editorLock = destScenario.getLock(ScenarioLock.EDITORS);
							if (editorLock.awaitClaim()) {
								try {
									mergeScenarioData(sourceRoot, destRoot, (EditingDomain) destScenario.getAdapters().get(EditingDomain.class), dataOptions);
								} finally {
									editorLock.release();
								}
							}

						}
						monitor.worked(1);
					}

				} finally {
					monitor.done();
				}
			}
		}
	}

	public void mergeScenarioData(final EObject sourceRoot, final EObject destRoot, final EditingDomain destEditingDomain, final Set<DataOptions> dataOptions) {
		final LNGScenarioModel sourceScenarioModel;
		if (sourceRoot instanceof LNGScenarioModel) {
			sourceScenarioModel = (LNGScenarioModel) sourceRoot;
		} else {
			log.error("Source scenario is not a LNGScenarioModel", new RuntimeException());
			return;
		}

		final LNGScenarioModel destScenarioModel;
		if (destRoot instanceof LNGScenarioModel) {
			destScenarioModel = (LNGScenarioModel) destRoot;
		} else {
			log.error("Destination scenario is not a LNGScenarioModel", new RuntimeException());
			return;
		}

		if (destEditingDomain instanceof CommandProviderAwareEditingDomain) {
			final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) destEditingDomain;

			// Normally we disable command providers, but in this can we will not. Specifically for canal cost maintenance. The user can not add or remove these objects should the number of vessel
			// classes or the number of route change. These are intended to be maintained by a command provider.

			// commandProviderAwareEditingDomain.setCommandProvidersDisabled(true);

			commandProviderAwareEditingDomain.setAdaptersEnabled(false);
			commandProviderAwareEditingDomain.startBatchCommand();
		}

		try {
			final List<IMappingDescriptor> descriptors = new LinkedList<IMappingDescriptor>();
			final LNGScenarioModel copiedModel = EcoreUtil.copy(sourceScenarioModel);

			if (dataOptions.contains(DataOptions.PortData)) {
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getPortModel(), destScenarioModel.getReferenceModel().getPortModel(), PortPackage.eINSTANCE.getPortModel_Ports()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getPortModel(), destScenarioModel.getReferenceModel().getPortModel(), PortPackage.eINSTANCE.getPortModel_Routes()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getPortModel(), destScenarioModel.getReferenceModel().getPortModel(), PortPackage.eINSTANCE.getPortModel_PortGroups()));
			}

			if (dataOptions.contains(DataOptions.FleetDatabase)) {
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getFleetModel(), destScenarioModel.getReferenceModel().getFleetModel(), FleetPackage.eINSTANCE.getFleetModel_VesselClasses()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getFleetModel(), destScenarioModel.getReferenceModel().getFleetModel(), FleetPackage.eINSTANCE.getFleetModel_Vessels()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getFleetModel(), destScenarioModel.getReferenceModel().getFleetModel(), FleetPackage.eINSTANCE.getFleetModel_VesselGroups()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getFleetModel(), destScenarioModel.getReferenceModel().getFleetModel(), FleetPackage.eINSTANCE.getFleetModel_BaseFuels()));
			}

			if (dataOptions.contains(DataOptions.CommercialData)) {
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getCommercialModel(), destScenarioModel.getReferenceModel().getCommercialModel(),
						CommercialPackage.eINSTANCE.getCommercialModel_Entities()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getCommercialModel(), destScenarioModel.getReferenceModel().getCommercialModel(),
						CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getCommercialModel(), destScenarioModel.getReferenceModel().getCommercialModel(),
						CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts()));
			}

			if (dataOptions.contains(DataOptions.PricingData)) {
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getPricingModel(), destScenarioModel.getReferenceModel().getPricingModel(),
						PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getPricingModel(), destScenarioModel.getReferenceModel().getPricingModel(),
						PricingPackage.eINSTANCE.getPricingModel_CharterIndices()));
				descriptors.add(EMFModelMergeTools.generateMappingDescriptor(copiedModel.getReferenceModel().getPricingModel(), destScenarioModel.getReferenceModel().getPricingModel(),
						PricingPackage.eINSTANCE.getPricingModel_BaseFuelPrices()));
			}

			// Fix up the descriptor data references to point to destination objects in the cases where the source is not being transferred across
			EMFModelMergeTools.rewriteMappingDescriptors(descriptors, copiedModel, destScenarioModel);

			// TODO: If multiple stages, then add into a compound command
			final Command cmd = EMFModelMergeTools.applyMappingDescriptors(destEditingDomain, destScenarioModel, descriptors);
			if (cmd != null) {
				if (cmd.canExecute()) {

					destEditingDomain.getCommandStack().execute(cmd);
				} else {
					log.error("Unable to execute merge command", new RuntimeException());
				}
			}
		} finally {
			if (destEditingDomain instanceof CommandProviderAwareEditingDomain) {
				final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) destEditingDomain;
				// commandProviderAwareEditingDomain.setCommandProvidersDisabled(false);
				commandProviderAwareEditingDomain.endBatchCommand();
				commandProviderAwareEditingDomain.setAdaptersEnabled(true);
			}
		}
	}
}
