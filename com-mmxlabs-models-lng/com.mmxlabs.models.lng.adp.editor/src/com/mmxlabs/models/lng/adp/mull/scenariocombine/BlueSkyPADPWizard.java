/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.scenariocombine;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.PreDefinedDate;
import com.mmxlabs.models.lng.adp.PreDefinedDistributionModel;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class BlueSkyPADPWizard extends Wizard implements IExportWizard {

	private PADPCombineWizardSupplySideSelectorPage supplySideSelectorPage;
	private final ScenarioInstance demandSideScenario;

	public BlueSkyPADPWizard(ScenarioInstance demandSideScenario) {
		this.demandSideScenario = demandSideScenario;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("PADP Blue Sky Wizard");
		setForcePreviousAndNextButtons(false);
		supplySideSelectorPage = new PADPCombineWizardSupplySideSelectorPage();
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(supplySideSelectorPage);
	}

	@Override
	public boolean performFinish() {
		final ScenarioInstance supplySideScenario = supplySideSelectorPage.getScenarioInstance();
		BusyIndicator.showWhile(this.getContainer().getShell().getDisplay(), () -> {
			@NonNull
			ScenarioModelRecord supplySideModelRecord = SSDataManager.Instance.getModelRecordChecked(supplySideScenario);
			if (supplySideModelRecord.isLoadFailure()) {
				throw new RuntimeException("Unable to load supply side model data");
			}
			try (final IScenarioDataProvider supplySideSDP = supplySideModelRecord.aquireScenarioDataProvider("PADPCombineLoadDateFetch")) {
				final Object supplySideObject = supplySideSDP.getScenario();
				if (supplySideObject instanceof LNGScenarioModel) {
					final LNGScenarioModel supplySideRoot = (LNGScenarioModel) supplySideObject;
					final CargoModel supplySideCargoModel = ScenarioModelUtil.getCargoModel(supplySideRoot);
					// final Map<PurchaseContract, List<LocalDate>> purchaseContractLiftingDates = fetchPurchaseContractLiftingDates(supplySideCargoModel);
					final Map<PurchaseContract, Map<YearMonth, Integer>> purchaseContractMonthlyCounts = fetchPurchaseContractMonthlyCount(supplySideCargoModel);
					final Set<PurchaseContract> expectedPurchaseContracts = purchaseContractMonthlyCounts.entrySet().stream().map(Entry::getKey).collect(Collectors.toSet());
					// final Set<SalesContract> expectedSalesContracts = getExpectedSalesContracts(supplySideRoot);

					// Check demand side scenario has expected sales contracts before fork
					@NonNull
					ScenarioModelRecord demandSideModelRecord = SSDataManager.Instance.getModelRecordChecked(demandSideScenario);
					if (demandSideModelRecord.isLoadFailure()) {
						throw new RuntimeException("Unable to load demand side model data");
					}
					try (final IScenarioDataProvider demandSideSDP = demandSideModelRecord.aquireScenarioDataProvider("PADPCombineDemandContractCheck")) {

						final Object demandSideObject = demandSideSDP.getScenario();
						if (demandSideObject instanceof LNGScenarioModel) {
							final LNGScenarioModel demandSideRoot = (LNGScenarioModel) demandSideObject;
							final CommercialModel demandSideCommercialModel = ScenarioModelUtil.getCommercialModel(demandSideRoot);
							final Set<String> demandSidePurchaseContractLowercaseNames = demandSideCommercialModel.getPurchaseContracts().stream().map(PurchaseContract::getName)
									.map(String::toLowerCase).collect(Collectors.toSet());
							for (final PurchaseContract expectedPurchaseContract : expectedPurchaseContracts) {
								final String expectedLowerCaseName = expectedPurchaseContract.getName().toLowerCase();
								if (!demandSidePurchaseContractLowercaseNames.contains(expectedLowerCaseName)) {
									MessageDialog.openInformation(getShell(), "Blue sky model construction",
											String.format("Demand side model does not contain expected purchase contract: %s", expectedPurchaseContract.getName()));
									return;
								}
							}
							/*
							 * final Set<String> demandSideSalesContractLowercaseNames = demandSideCommercialModel.getSalesContracts().stream().map(SalesContract::getName).map(String::toLowerCase)
							 * .collect(Collectors.toSet()); for (final SalesContract expectedSalesContract : expectedSalesContracts) { final String expectedLowercaseName =
							 * expectedSalesContract.getName().toLowerCase(); if (!demandSideSalesContractLowercaseNames.contains(expectedLowercaseName)) { MessageDialog.openInformation(getShell(),
							 * "Blue sky model construction", String.format("Demand side model does not contain expected sales contract: %s", expectedSalesContract.getName())); return; } }
							 */
							final ADPModel demandSideAdpModel = ScenarioModelUtil.getADPModel(demandSideRoot);
							if (demandSideAdpModel == null) {
								MessageDialog.openInformation(getShell(), "Blue sky model construction", "Demand side model does not contain an ADP model");
								return;
							}
							// final Set<String> expectedSalesContractsLowercaseNames =
							// expectedSalesContracts.stream().map(SalesContract::getName).map(String::toLowerCase).collect(Collectors.toSet());
							// int hitContracts = 0;
							// for (final SalesContractProfile demandSideSalesContractProfile : demandSideAdpModel.getSalesContractProfiles()) {
							// final String lowercaseName = demandSideSalesContractProfile.getContract().getName().toLowerCase();
							// if (expectedSalesContractsLowercaseNames.contains(lowercaseName)) {
							// ++hitContracts;
							// }
							// }
							/*
							 * if (expectedSalesContractsLowercaseNames.size() != hitContracts) { MessageDialog.openInformation(getShell(), "Blue sky model construction",
							 * "ADP sales contract profile missing data"); return; }
							 */
						}
					}

					final ScenarioInstance combinedScenarioInstance;
					try {
						combinedScenarioInstance = ScenarioServiceModelUtils.fork(demandSideScenario, "Blue sky", new NullProgressMonitor());
					} catch (Exception exception) {
						throw new RuntimeException("Unable to fork scenario for blue sky model.", exception);
					}
					@NonNull
					ScenarioModelRecord combinedModelRecord = SSDataManager.Instance.getModelRecordChecked(combinedScenarioInstance);
					if (combinedModelRecord.isLoadFailure()) {
						throw new RuntimeException("Unable to load blue sky model data");
					}
					try (final IScenarioDataProvider combinedSDP = combinedModelRecord.aquireScenarioDataProvider("PADPBlueSkyConstruction")) {
						final Object combinedObject = combinedSDP.getScenario();
						if (combinedObject instanceof LNGScenarioModel) {
							final LNGScenarioModel combinedModelRoot = (LNGScenarioModel) combinedObject;
							transferLiftingDates(combinedModelRoot, purchaseContractMonthlyCounts);
							regenerateSlots(combinedSDP, combinedModelRoot, expectedPurchaseContracts);

							final Map<Object, Object> saveOptions = new HashMap<>();
							saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_FILE_BUFFER);
							saveOptions.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);
							try {
								combinedScenarioInstance.eResource().save(saveOptions);
								combinedModelRoot.eResource().save(saveOptions);
							} catch (final IOException e) {
								throw new RuntimeException("Unable to save blue sky model.");
							}
						}
					}
				}
			}
		});
		return true;
	}

	public ScenarioInstance getDemandSideScenarioInstance() {
		return this.demandSideScenario;
	}

	private Map<PurchaseContract, Map<YearMonth, Integer>> fetchPurchaseContractMonthlyCount(final CargoModel cargoModel) {
		final Map<PurchaseContract, Map<YearMonth, Integer>> purchaseContractMonthlyCount = new HashMap<>();
		cargoModel.getCargoes().stream() //
				.map(c -> c.getSlots().get(0)) //
				.filter(s -> s.getEntity() != null && !s.getEntity().isThirdParty()) //
				.map(LoadSlot.class::cast) //
				.forEach(loadSlot -> purchaseContractMonthlyCount.computeIfAbsent(loadSlot.getContract(), pc -> new HashMap<>()).compute(YearMonth.from(loadSlot.getWindowStart()),
						(k, v) -> v == null ? 1 : v + 1));
		return purchaseContractMonthlyCount;
	}

	private void transferLiftingDates(final LNGScenarioModel combinedModelRoot, final Map<PurchaseContract, Map<YearMonth, Integer>> supplySideMonthlyCounts) {
		final ADPModel combinedAdpModel = combinedModelRoot.getAdpModel();
		if (combinedAdpModel == null) {
			throw new RuntimeException("Error in creating blue sky model");
		}
		final CommercialModel combinedCommercialModel = ScenarioModelUtil.getCommercialModel(combinedModelRoot);
		for (final Entry<PurchaseContract, Map<YearMonth, Integer>> entry : supplySideMonthlyCounts.entrySet()) {
			final PurchaseContract supplySidePurchaseContract = entry.getKey();
			final Optional<PurchaseContract> optCombinedModelPurchaseContract = combinedCommercialModel.getPurchaseContracts().stream()
					.filter(p -> p.getName().equals(supplySidePurchaseContract.getName())).findFirst();
			if (optCombinedModelPurchaseContract.isPresent()) {
				final PurchaseContract combinedModelPurchaseContract = optCombinedModelPurchaseContract.get();
				final Optional<PurchaseContractProfile> optCombinedModelPcProfile = combinedAdpModel.getPurchaseContractProfiles().stream()
						.filter(profile -> profile.getContract() == combinedModelPurchaseContract).findFirst();
				final PurchaseContractProfile combinedModelPcProfile = optCombinedModelPcProfile.isPresent() ? optCombinedModelPcProfile.get()
						: ADPModelUtil.createProfile(combinedModelRoot, combinedAdpModel, combinedModelPurchaseContract);
				final List<SubContractProfile<LoadSlot, PurchaseContract>> subContractProfiles = combinedModelPcProfile.getSubProfiles();
				final SubContractProfile<LoadSlot, PurchaseContract> subContractProfile = subContractProfiles.get(0);
				final DistributionModel originalDistributionModel = subContractProfile.getDistributionModel();
				final PreDefinedDistributionModel newDistributionModel;
				if (originalDistributionModel instanceof PreDefinedDistributionModel) {
					newDistributionModel = (PreDefinedDistributionModel) originalDistributionModel;
					newDistributionModel.getDates().clear();
				} else {
					newDistributionModel = ADPFactory.eINSTANCE.createPreDefinedDistributionModel();
					subContractProfile.setDistributionModel(newDistributionModel);
				}
				// Sort entries so generated load IDs do not seem random
				entry.getValue().entrySet().stream().sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey())) //
						.flatMap(innerEntry -> {
							final LocalDate startOfMonth = innerEntry.getKey().atDay(1);
							return Stream.generate(() -> wrapDate(startOfMonth)).limit(innerEntry.getValue());
						}).forEach(newDistributionModel.getDates()::add);
			} else {
				throw new UserFeedbackException(String.format("Demand side model does not have purchase contract named: %s.", supplySidePurchaseContract.getName()));
			}
		}
	}

	private static PreDefinedDate wrapDate(final LocalDate localDate) {
		final PreDefinedDate preDefinedDate = ADPFactory.eINSTANCE.createPreDefinedDate();
		preDefinedDate.setDate(localDate);
		return preDefinedDate;
	}

	private void regenerateSlots(final IScenarioDataProvider combinedSDP, @NonNull final LNGScenarioModel combinedModelRoot, final Set<PurchaseContract> expectedPurchaseContracts) {
		final CargoModel combinedCargoModel = ScenarioModelUtil.getCargoModel(combinedModelRoot);
		final EditingDomain combinedEditingDomain = combinedSDP.getEditingDomain();
		// Clear all slots
		final CompoundCommand clearCmd = new CompoundCommand("Clear slots");
		final List<EObject> objectsToDelete = new ArrayList<>(combinedCargoModel.getCargoes().size() + combinedCargoModel.getLoadSlots().size() + combinedCargoModel.getDischargeSlots().size());
		objectsToDelete.addAll(combinedCargoModel.getCargoes());
		objectsToDelete.addAll(combinedCargoModel.getLoadSlots());
		objectsToDelete.addAll(combinedCargoModel.getDischargeSlots());
		if (!objectsToDelete.isEmpty()) {
			clearCmd.append(DeleteCommand.create(combinedEditingDomain, objectsToDelete));
			combinedSDP.getCommandStack().execute(clearCmd);
		}

		// Generate new slots - profiles assumed to be validated and present
		final ADPModel combinedAdpModel = ScenarioModelUtil.getADPModel(combinedModelRoot);
		final CompoundCommand generateCmd = new CompoundCommand("Generate ADP slots");

		final Map<String, PurchaseContractProfile> purchaseContractProfileMap = new HashMap<>();
		for (final PurchaseContractProfile purchaseContractProfile : combinedAdpModel.getPurchaseContractProfiles()) {
			purchaseContractProfileMap.put(purchaseContractProfile.getContract().getName().toLowerCase(), purchaseContractProfile);
		}
		for (final PurchaseContract expectedPurchaseContract : expectedPurchaseContracts) {
			final PurchaseContractProfile purchaseContractProfile = purchaseContractProfileMap.get(expectedPurchaseContract.getName().toLowerCase());
			final Command populateModelCommand = ADPModelUtil.populateModel(combinedSDP.getEditingDomain(), combinedModelRoot, combinedAdpModel, purchaseContractProfile);
			if (populateModelCommand != null) {
				generateCmd.append(populateModelCommand);
			}
		}

		// final Map<String, SalesContractProfile> salesContractProfileMap = new HashMap<>();
		// for (final SalesContractProfile salesContractProfile : combinedAdpModel.getSalesContractProfiles()) {
		// salesContractProfileMap.put(salesContractProfile.getContract().getName().toLowerCase(), salesContractProfile);
		// }
		// for (final SalesContract expectedSalesContract : expectedSalesContracts) {
		// final SalesContractProfile salesContractProfile = salesContractProfileMap.get(expectedSalesContract.getName().toLowerCase());
		// final Command populateModelCommand = ADPModelUtil.populateModel(combinedSDP.getEditingDomain(), combinedModelRoot, combinedAdpModel, salesContractProfile);
		// if (populateModelCommand != null) {
		// generateCmd.append(populateModelCommand);
		// }
		// }

		if (!generateCmd.isEmpty()) {
			combinedSDP.getCommandStack().execute(generateCmd);
			// Flush commands in new (unseen) scenario - undoing these commands makes no sense
			combinedSDP.getCommandStack().flush();
		}
	}

	private Set<SalesContract> getExpectedSalesContracts(final LNGScenarioModel supplySideRoot) {
		final ADPModel supplySideAdpModel = supplySideRoot.getAdpModel();
		if (supplySideAdpModel == null) {
			throw new UserFeedbackException("Supply side model is not an ADP model");
		}
		final MullProfile supplySideMullProfile = supplySideAdpModel.getMullProfile();
		if (supplySideMullProfile == null) {
			throw new UserFeedbackException("Supply side model does not contain MULL data");
		}
		final Set<SalesContract> expectedSalesContracts = supplySideMullProfile.getInventories().stream().flatMap(m -> m.getEntityTable().stream()).filter(r -> !r.getEntity().isThirdParty())
				.flatMap(r -> r.getSalesContractAllocationRows().stream()).map(SalesContractAllocationRow::getContract).collect(Collectors.toSet());
		if (expectedSalesContracts.isEmpty()) {
			throw new UserFeedbackException("Supply side model MULL data does not contain sales contract allocations");
		}
		return expectedSalesContracts;
	}
}
