/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.scenariocombine;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.resource.Resource;
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
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class CombinePADPWizard extends Wizard implements IExportWizard {

	private PADPCombineWizardSupplySideSelectorPage supplySideSelectorPage;
	private final ScenarioInstance demandSideScenario;
	private ScenarioInstance supplySideScenario;

	public CombinePADPWizard(@NonNull final ScenarioInstance demandSideScenario) {
		this.demandSideScenario = demandSideScenario;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("PADP Combine Wizard");
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
		this.supplySideScenario = supplySideSelectorPage.getScenarioInstance();
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
					final Set<PurchaseContract> expectedPurchaseContracts = supplySideCargoModel.getCargoes().stream() //
							.map(c -> c.getSortedSlots().get(0)) //
							.filter(s -> s.getEntity() != null && !s.getEntity().isThirdParty()) //
							.map(LoadSlot.class::cast) //
							.map(LoadSlot::getContract).collect(Collectors.toSet());
					final Set<SalesContract> expectedSalesContracts = getExpectedSalesContracts(supplySideRoot);

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
									MessageDialog.openInformation(getShell(), "Combined model construction",
											String.format("Demand side model does not contain expected purchase contract: %s", expectedPurchaseContract.getName()));
									return;
								}
							}
							final Set<String> demandSideSalesContractLowercaseNames = demandSideCommercialModel.getSalesContracts().stream().map(SalesContract::getName).map(String::toLowerCase)
									.collect(Collectors.toSet());
							for (final SalesContract expectedSalesContract : expectedSalesContracts) {
								final String expectedLowercaseName = expectedSalesContract.getName().toLowerCase();
								if (!demandSideSalesContractLowercaseNames.contains(expectedLowercaseName)) {
									MessageDialog.openInformation(getShell(), "Combined model construction",
											String.format("Demand side model does not contain expected sales contract: %s", expectedSalesContract.getName()));
									return;
								}
							}
							final ADPModel demandSideAdpModel = ScenarioModelUtil.getADPModel(demandSideRoot);
							if (demandSideAdpModel == null) {
								MessageDialog.openInformation(getShell(), "Combined model construction", "Demand side model does not contain an ADP model");
								return;
							}
							final Set<String> expectedSalesContractsLowercaseNames = expectedSalesContracts.stream().map(SalesContract::getName).map(String::toLowerCase).collect(Collectors.toSet());
							int hitContracts = 0;
							for (final SalesContractProfile demandSideSalesContractProfile : demandSideAdpModel.getSalesContractProfiles()) {
								final String lowercaseName = demandSideSalesContractProfile.getContract().getName().toLowerCase();
								if (expectedSalesContractsLowercaseNames.contains(lowercaseName)) {
									++hitContracts;
								}
							}
							if (expectedSalesContractsLowercaseNames.size() != hitContracts) {
								MessageDialog.openInformation(getShell(), "Combined model construction", "ADP sales contract profile missing data");
								return;
							}
						}
					}

					final ScenarioInstance combinedScenarioInstance;
					try {
						combinedScenarioInstance = ScenarioServiceModelUtils.fork(demandSideScenario, "Combined", new NullProgressMonitor());
					} catch (Exception exception) {
						throw new RuntimeException("Unable to fork scenario for combined model.", exception);
					}
					@NonNull
					ScenarioModelRecord combinedModelRecord = SSDataManager.Instance.getModelRecordChecked(combinedScenarioInstance);
					if (combinedModelRecord.isLoadFailure()) {
						throw new RuntimeException("Unable to load combined model data");
					}
					try (final IScenarioDataProvider combinedSDP = combinedModelRecord.aquireScenarioDataProvider("PADPCombineConstruction")) {
						final Object combinedObject = combinedSDP.getScenario();
						if (combinedObject instanceof LNGScenarioModel) {
							final LNGScenarioModel combinedModelRoot = (LNGScenarioModel) combinedObject;

							final LocalDate adpStartDate = ScenarioModelUtil.getADPModel(combinedModelRoot).getYearStart().atDay(1);
							final Map<PurchaseContract, List<LocalDate>> inAdpPurchaseContractLiftingDates = fetchInAdpPurchaseContractLiftingDates(supplySideCargoModel, adpStartDate);
							transferLiftingDates(combinedModelRoot, inAdpPurchaseContractLiftingDates);
							regenerateSlots(combinedSDP, combinedModelRoot, expectedPurchaseContracts, expectedSalesContracts);

							final List<LoadSlot> supplySidePreAdpLoadSlots = supplySideCargoModel.getCargoes().stream().map(c -> c.getSortedSlots().get(0))
									.filter(s -> s.getWindowStart().isBefore(adpStartDate)).map(LoadSlot.class::cast).collect(Collectors.toList());
							transferPreAdpLoadSlots(combinedModelRoot, supplySidePreAdpLoadSlots);

							// Flush command stack just to be sure. On scenario build there should not be any undo available in the combined model.
							combinedSDP.getCommandStack().flush();
							final Map<Object, Object> saveOptions = new HashMap<>();
							saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_FILE_BUFFER);
							saveOptions.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);
							try {
								combinedScenarioInstance.eResource().save(saveOptions);
								combinedModelRoot.eResource().save(saveOptions);
							} catch (final IOException e) {
								throw new RuntimeException("Unable to save combined model.");
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

	private Map<PurchaseContract, List<LocalDate>> fetchInAdpPurchaseContractLiftingDates(final CargoModel cargoModel, final LocalDate adpStartDate) {
		final Map<PurchaseContract, List<LocalDate>> purchaseContractLiftingDates = new HashMap<>();
		cargoModel.getCargoes().stream() //
				.map(c -> c.getSortedSlots().get(0)) //
				.filter(s -> s.getEntity() != null && !s.getEntity().isThirdParty()) //
				.filter(s -> !s.getWindowStart().isBefore(adpStartDate)) //
				.map(LoadSlot.class::cast) //
				.forEach(loadSlot -> purchaseContractLiftingDates.computeIfAbsent(loadSlot.getContract(), pc -> new LinkedList<>()).add(loadSlot.getWindowStart()));
		return purchaseContractLiftingDates;
	}

	private void transferPreAdpLoadSlots(final LNGScenarioModel combinedModelRoot, final List<LoadSlot> supplySidePreAdpLoadSlots) {
		final Set<BaseLegalEntity> expectedEntities = supplySidePreAdpLoadSlots.stream().filter(LoadSlot::isSetEntity).map(LoadSlot::getEntity).collect(Collectors.toSet());
		final Map<BaseLegalEntity, BaseLegalEntity> supplyToCombinedEntityMap = new HashMap<>();
		for (final BaseLegalEntity supplySideEntity : expectedEntities) {
			final Optional<@NonNull BaseLegalEntity> optCombinedSideEntity = combinedModelRoot.getReferenceModel().getCommercialModel().getEntities().stream()
					.filter(e -> e.getName().equalsIgnoreCase(supplySideEntity.getName())).findAny();
			if (optCombinedSideEntity.isPresent()) {
				supplyToCombinedEntityMap.put(supplySideEntity, optCombinedSideEntity.get());
			} else {
				throw new IllegalStateException(String.format("Demand side model does not have expected entity: %s", supplySideEntity.getName()));
			}
		}
		final Set<PurchaseContract> expectedPurchaseContracts = supplySidePreAdpLoadSlots.stream().filter(LoadSlot::isSetContract).map(LoadSlot::getContract).collect(Collectors.toSet());
		final Map<PurchaseContract, PurchaseContract> supplyToCombinedContractMap = new HashMap<>();
		for (final PurchaseContract supplySidePurchaseContract : expectedPurchaseContracts) {
			final Optional<@NonNull PurchaseContract> optCombinedSideContract = combinedModelRoot.getReferenceModel().getCommercialModel().getPurchaseContracts().stream()
					.filter(c -> c.getName().equalsIgnoreCase(supplySidePurchaseContract.getName())).findAny();
			if (optCombinedSideContract.isPresent()) {
				supplyToCombinedContractMap.put(supplySidePurchaseContract, optCombinedSideContract.get());
			} else {
				throw new IllegalStateException(String.format("Demand side model does not have expected purchase contract: %s", supplySidePurchaseContract.getName()));
			}
		}
		final Set<Port> expectedPorts = supplySidePreAdpLoadSlots.stream().map(LoadSlot::getPort).collect(Collectors.toSet());
		final Map<Port, Port> supplyToCombinedPortMap = new HashMap<>();
		for (final Port supplySidePort : expectedPorts) {
			final Optional<@NonNull Port> optCombinedSidePort = combinedModelRoot.getReferenceModel().getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(supplySidePort.getName()))
					.findAny();
			if (optCombinedSidePort.isPresent()) {
				supplyToCombinedPortMap.put(supplySidePort, optCombinedSidePort.get());
			} else {
				throw new IllegalStateException(String.format("Demand side model does not have expected port: %s", supplySidePort.getName()));
			}
		}
		final List<LoadSlot> newCombinedSideLoadSlots = new ArrayList<>();
		for (final LoadSlot supplySideLoadSlot : supplySidePreAdpLoadSlots) {
			final LoadSlot combinedSideLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			combinedSideLoadSlot.setName(supplySideLoadSlot.getName());
			combinedSideLoadSlot.setPort(supplyToCombinedPortMap.get(supplySideLoadSlot.getPort()));
			if (supplySideLoadSlot.isSetEntity()) {
				combinedSideLoadSlot.setEntity(supplyToCombinedEntityMap.get(supplySideLoadSlot.getEntity()));
			}
			if (supplySideLoadSlot.isSetMinQuantity()) {
				combinedSideLoadSlot.setMinQuantity(supplySideLoadSlot.getMinQuantity());
			}
			if (supplySideLoadSlot.isSetMaxQuantity()) {
				combinedSideLoadSlot.setMaxQuantity(supplySideLoadSlot.getMaxQuantity());
			}
			if (supplySideLoadSlot.isSetContract()) {
				combinedSideLoadSlot.setContract(supplyToCombinedContractMap.get(supplySideLoadSlot.getContract()));
			}
			if (supplySideLoadSlot.isSetPriceExpression()) {
				combinedSideLoadSlot.setPriceExpression(supplySideLoadSlot.getPriceExpression());
			}
			combinedSideLoadSlot.setWindowStart(supplySideLoadSlot.getWindowStart());
			if (supplySideLoadSlot.isSetWindowSize()) {
				combinedSideLoadSlot.setWindowSize(supplySideLoadSlot.getWindowSize());
			}
			if (supplySideLoadSlot.isSetWindowSizeUnits()) {
				combinedSideLoadSlot.setWindowSizeUnits(supplySideLoadSlot.getWindowSizeUnits());
			}
			if (supplySideLoadSlot.isSetWindowStartTime()) {
				combinedSideLoadSlot.setWindowStartTime(supplySideLoadSlot.getWindowStartTime());
			}
			newCombinedSideLoadSlots.add(combinedSideLoadSlot);
		}
		combinedModelRoot.getCargoModel().getLoadSlots().addAll(newCombinedSideLoadSlots);
	}

	private void transferLiftingDates(final LNGScenarioModel combinedModelRoot, final Map<PurchaseContract, List<LocalDate>> supplySideLiftingDates) {
		final ADPModel combinedAdpModel = combinedModelRoot.getAdpModel();
		if (combinedAdpModel == null) {
			throw new RuntimeException("Error in creating combined model");
		}
		final CommercialModel combinedCommercialModel = ScenarioModelUtil.getCommercialModel(combinedModelRoot);
		for (final Entry<PurchaseContract, List<LocalDate>> entry : supplySideLiftingDates.entrySet()) {
			final PurchaseContract supplySidePurchaseContract = entry.getKey();
			final Optional<@NonNull PurchaseContract> optCombinedModelPurchaseContract = combinedCommercialModel.getPurchaseContracts().stream()
					.filter(p -> p.getName().equals(supplySidePurchaseContract.getName())).findFirst();
			if (optCombinedModelPurchaseContract.isPresent()) {
				final PurchaseContract combinedModelPurchaseContract = optCombinedModelPurchaseContract.get();
				final Optional<@NonNull PurchaseContractProfile> optCombinedModelPcProfile = combinedAdpModel.getPurchaseContractProfiles().stream()
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
				// Sort dates so that load ids do not appear to be random
				entry.getValue().stream().sorted().map(CombinePADPWizard::wrapDate).forEach(newDistributionModel.getDates()::add);
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

	private void regenerateSlots(final IScenarioDataProvider combinedSDP, @NonNull final LNGScenarioModel combinedModelRoot, final Set<PurchaseContract> expectedPurchaseContracts,
			final Set<SalesContract> expectedSalesContracts) {

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
