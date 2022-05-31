/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.AdpGlobalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.FinalPhaseMullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.FixedCargoMatch;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.HarmonisationMap;
import com.mmxlabs.models.lng.adp.mull.algorithm.HarmonisationMullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.HarmonisationPhaseCargoMatchingComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.HarmonisationPhaseSameEntityCargoMatchingComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.IMonthEndTrackingAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.IMullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.IMullDischargeWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.IRollingWindow;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryGlobalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryLocalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullDesSalesMarketWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullGlobalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullSalesContractWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.PhaseOneAMullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.PhaseOneMullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.RollingLoadWindow;
import com.mmxlabs.models.lng.adp.mull.algorithm.VanillaMullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.VanillaRollingLoadWindow;
import com.mmxlabs.models.lng.adp.mull.algorithm.VanillaStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.FinalPhaseStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.FromProfileBuildingStrategy;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.HarmonisationPhaseStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.IMullContainerBuildingStrategy;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.IMullStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.PhaseOneAStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.PhaseOneStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.container.DesMarketTracker;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;
import com.mmxlabs.models.lng.adp.mull.container.SalesContractTracker;
import com.mmxlabs.models.lng.adp.mull.harmonisation.HarmonisationTransformationState;
import com.mmxlabs.models.lng.adp.mull.profile.DesSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.EntityRow;
import com.mmxlabs.models.lng.adp.mull.profile.IAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.IEntityRow;
import com.mmxlabs.models.lng.adp.mull.profile.IMullProfile;
import com.mmxlabs.models.lng.adp.mull.profile.IMullSubprofile;
import com.mmxlabs.models.lng.adp.mull.profile.MullProfile;
import com.mmxlabs.models.lng.adp.mull.profile.MullSubprofile;
import com.mmxlabs.models.lng.adp.mull.profile.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.presentation.views.ADPEditorData;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ui.commands.ScheduleModelInvalidateCommandProvider;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public class MullSolver {
	private MullSolver() {

	}

	public static Command populateVanillaModel(final ADPEditorData editorData, final com.mmxlabs.models.lng.adp.MullProfile eMullProfile) {
		final LNGScenarioModel sm = ADPModelUtil.getNullCheckedScenarioModel(editorData);
		final ADPModel adpModel = ADPModelUtil.getNullCheckedAdpModel(editorData);

		final GlobalStatesContainer globalStates = buildVanillaGlobalStates(eMullProfile, adpModel, sm);
		final IMullProfile mullProfile = createInternalMullProfile(eMullProfile);

		final IMullAlgorithm vanillaMullAlgorithm;
		{
			final IMullProfile vanillaMullProfile = createVanillaMullProfile(mullProfile);
			final IMullStrategyContainer vanillaStrategyContainer = new VanillaStrategyContainer();
			final AlgorithmState vanillaAlgorithmState = initialiseAlgorithmState(vanillaMullProfile, globalStates);
			final List<InventoryLocalState> vanillaLocalStates = createFromProfileVanillaInventoryLocalStates(vanillaMullProfile, globalStates, vanillaAlgorithmState, vanillaStrategyContainer);
			vanillaMullAlgorithm = new VanillaMullAlgorithm(globalStates, vanillaAlgorithmState, vanillaLocalStates);
		}
		vanillaMullAlgorithm.run();
		return createModelPopulationCommands(globalStates, vanillaMullAlgorithm, editorData);
	}

	public static Command populateModelFromMultipleInventories(final ADPEditorData editorData, final com.mmxlabs.models.lng.adp.MullProfile eMullProfile) {
		final LNGScenarioModel sm = ADPModelUtil.getNullCheckedScenarioModel(editorData);
		final ADPModel adpModel = ADPModelUtil.getNullCheckedAdpModel(editorData);

		final GlobalStatesContainer globalStates = buildGlobalStates(eMullProfile, adpModel, sm);
		final IMullProfile mullProfile = createInternalMullProfile(eMullProfile);

		// Phase 1 - Work out des market aacqs
		final MullAlgorithm phase1MullAlgorithm;
		{
			final IMullStrategyContainer phase1Strategies = new PhaseOneStrategyContainer();
			final AlgorithmState phase1AlgorithmState = initialiseAlgorithmState(mullProfile, globalStates);
			final List<InventoryLocalState> phase1LocalStates = createFromProfileInventoryLocalStates(mullProfile, globalStates, phase1AlgorithmState, phase1Strategies);
			phase1MullAlgorithm = new PhaseOneMullAlgorithm(globalStates, phase1AlgorithmState, phase1LocalStates);
		}
		phase1MullAlgorithm.run();

		// Phase 1a - work out monthly entitlements
		final IMonthEndTrackingAlgorithm phase1aMullAlgorithm;
		{
			System.out.println("Phase 1");
			printYearlyCounts(phase1MullAlgorithm);
			// Phase 1 to 1a transition
			final IMullProfile phase1aMullProfile = buildPhase1aMullProfile(mullProfile, phase1MullAlgorithm.getInventoryLocalStates());

			// Phase 1a
			final IMullStrategyContainer phase1aStrategies = new PhaseOneAStrategyContainer();
			final AlgorithmState phase1AAlgorithmState = initialiseAlgorithmState(phase1aMullProfile, globalStates);
			final List<InventoryLocalState> phase1aLocalStates = createFromProfileInventoryLocalStates(phase1aMullProfile, globalStates, phase1AAlgorithmState, phase1aStrategies);
			phase1aMullAlgorithm = new PhaseOneAMullAlgorithm(globalStates, phase1AAlgorithmState, phase1aLocalStates);
		}
		phase1aMullAlgorithm.run();

		// Harmonisation phase
		// TODO: split harmonisation from final phase
		final IMullAlgorithm harmonisationMullAlgorithm;
		final IMullAlgorithm finalPhaseMullAlgorithm;
		{
			// Phase 1a to harmonisation transition
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts = buildHarmonisationMap(phase1aMullAlgorithm);

			System.out.println("Phase 1a");
			printYearlyCounts(phase1aMullAlgorithm);

			final HarmonisationMap harmonisationMap = new HarmonisationMap(phase1aMullAlgorithm.getAlgorithmState().getMullProfile(), globalStates);
			final IMullProfile harmonisationProfile = harmonisationMap.getHarmonisedMullProfile();
			final IMullStrategyContainer harmonisationPhaseStrategies = new HarmonisationPhaseStrategyContainer();
			final AlgorithmState harmonisationPhaseAlgorithmState = initialiseAlgorithmState(harmonisationProfile, globalStates);
			final List<InventoryLocalState> harmonisationLocalStates = createFromProfileInventoryLocalStates(harmonisationProfile, globalStates, harmonisationPhaseAlgorithmState,
					harmonisationPhaseStrategies);
			final Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Integer>> inventoryToAacqsSatisfiedByFixedCargoes = getAacqsSatisfiedByFixedCargoes(
					phase1aMullAlgorithm.getAlgorithmState().getMullProfile(), globalStates);
			setMonthlyCounts(harmonisationLocalStates, harmonisationMap.getHarmonisationTransformationStates(), inventoryToAacqsSatisfiedByFixedCargoes, inventoryToMonthlyCounts, globalStates);

			final HarmonisationMullAlgorithm harmonisationMullAlgorithmObj = new HarmonisationMullAlgorithm(globalStates, harmonisationPhaseAlgorithmState, harmonisationLocalStates, harmonisationMap);
			harmonisationMullAlgorithm = harmonisationMullAlgorithmObj;
			harmonisationMullAlgorithm.run();

			System.out.println("Harmonisation");
			printYearlyCounts(harmonisationMullAlgorithmObj);

			// Perform final phase - invert harmonised pairs into actual (entity-contract/market) pairs
			// TODO: refactor to split the final phase as something that consumes the harmonisation phase
			final Map<Inventory, Map<YearMonth, List<Cargo>>> monthMappedExistingCargoes = buildMonthMappedExistingCargoes(harmonisationMullAlgorithmObj);

			final IMullStrategyContainer finalPhaseStrategies = new FinalPhaseStrategyContainer();
			final IMullProfile finalPhaseMullProfile = phase1aMullAlgorithm.getAlgorithmState().getMullProfile();
			final AlgorithmState finalPhaseAlgorithmState = initialiseAlgorithmState(finalPhaseMullProfile, globalStates);
			final List<InventoryLocalState> finalPhaseLocalStates = createFromProfileInventoryLocalStates(finalPhaseMullProfile, globalStates, finalPhaseAlgorithmState, finalPhaseStrategies);
			finalPhaseMullAlgorithm = new FinalPhaseMullAlgorithm(globalStates, finalPhaseAlgorithmState, finalPhaseLocalStates, harmonisationMullAlgorithmObj, inventoryToMonthlyCounts,
					monthMappedExistingCargoes);
			finalPhaseMullAlgorithm.run();

			System.out.println("Final");
			printYearlyCounts(finalPhaseMullAlgorithm);
		}

		return createModelPopulationCommands(globalStates, finalPhaseMullAlgorithm, editorData);
	}

	private static void printYearlyCounts(final IMullAlgorithm mullAlgorithm) {
		final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts = calculateMonthlyCounts(mullAlgorithm.getInventoryLocalStates(),
				mullAlgorithm.getGlobalStatesContainer());
		final Map<Inventory, Map<BaseLegalEntity, Map<IMullDischargeWrapper, Integer>>> yearlyCounts = new HashMap<>();
		inventoryToMonthlyCounts.forEach((inventory, entityMap) -> {
			final Map<BaseLegalEntity, Map<IMullDischargeWrapper, Integer>> yearlyEntityMap = yearlyCounts.computeIfAbsent(inventory, k -> new HashMap<>());
			entityMap.forEach((entity, ymMap) -> {
				final Map<IMullDischargeWrapper, Integer> yearlyDischargeWrapperMap = yearlyEntityMap.computeIfAbsent(entity, k -> new HashMap<>());
				ymMap.forEach((ym, dischargeWrapperMap) -> {
					dischargeWrapperMap.forEach((wrapper, count) -> {
						yearlyDischargeWrapperMap.compute(wrapper, (k, v) -> v != null ? v + count : count);
					});
				});
			});
		});
		yearlyCounts.entrySet().stream().sorted((e1, e2) -> e1.getKey().getName().compareTo(e2.getKey().getName())).forEach(entry -> {
			entry.getValue().entrySet().stream().sorted((e1, e2) -> e1.getKey().getName().compareTo(e2.getKey().getName())).forEach(entry2 -> {
				System.out.println(String.format("%s--%s", entry.getKey().getName(), entry2.getKey().getName()));
				entry2.getValue().entrySet().stream().sorted((e1, e2) -> {
					if (e1 instanceof MullSalesContractWrapper salesContractWrapper) {
						if (e2 instanceof MullSalesContractWrapper salesContractWrapper2) {
							return salesContractWrapper.getSalesContract().getName().compareTo(salesContractWrapper2.getSalesContract().getName());
						} else {
							return -1;
						}
					} else {
						if (e2 instanceof MullSalesContractWrapper salesContractWrapper) {
							return 1;
						} else {
							return 0;
						}
					}
				}).forEach(entry3 -> {
					final String name = entry3.getKey() instanceof MullSalesContractWrapper ? ((MullSalesContractWrapper) entry3.getKey()).getSalesContract().getName()
							: ((MullDesSalesMarketWrapper) entry3.getKey()).getDesSalesMarket().getName();
					System.out.println(String.format("\t%s: %d", name, entry3.getValue()));
				});
			});
		});
	}

	private static Command createModelPopulationCommands(final GlobalStatesContainer globalStates, final IMullAlgorithm finalAlgorithm, final ADPEditorData editorData) {

		final IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
		final EditingDomain editingDomain = ADPModelUtil.getNullCheckedEditingDomain(editorData);
		final LNGScenarioModel sm = ADPModelUtil.getNullCheckedScenarioModel(editorData);
		@Nullable
		final Command deleteModelsCommand = ScheduleModelInvalidateCommandProvider.createClearModelsCommand(editingDomain, sm, ScenarioModelUtil.getAnalyticsModel(sm));

		final CompoundCommand cmd = new CompoundCommand("Generate ADP slots");
		addDeleteCommands(globalStates, cmd, sdp, editingDomain);

		final IModelFactoryRegistry modelFactoryRegistry = ADPModelUtil.getNullCheckedModelFactoryRegistry();
		final CargoEditingCommands cec = new CargoEditingCommands(editingDomain, sm, ScenarioModelUtil.getCargoModel(sm), ScenarioModelUtil.getCommercialModel(sm), modelFactoryRegistry);
		addCargoCreationCommands(globalStates, finalAlgorithm, cec, sdp, editingDomain, cmd);

		if (cmd.isEmpty()) {
			return IdentityCommand.INSTANCE;
		} else {
			if (deleteModelsCommand != null) {
				editorData.getDefaultCommandHandler().handleCommand(deleteModelsCommand);
			}
			try {
				editorData.setDisableUpdates(true);
				editorData.getDefaultCommandHandler().handleCommand(cmd);
			} finally {
				editorData.setDisableUpdates(false);
			}
		}

		return cmd;
	}

	private static void addCargoCreationCommands(final GlobalStatesContainer globalStates, final IMullAlgorithm finalAlgorithm, final CargoEditingCommands cec, final IScenarioDataProvider sdp,
			final EditingDomain editingDomain, final CompoundCommand cmd) {
		final Map<Vessel, VesselAvailability> vesselToVa = ScenarioModelUtil.getCargoModel(sdp).getVesselAvailabilities().stream() //
				.collect(Collectors.toMap(VesselAvailability::getVessel, Function.identity()));

		final List<Command> commands = new ArrayList<>();
		for (final InventoryLocalState inventoryLocalState : finalAlgorithm.getInventoryLocalStates()) {
			int i = 0;
			for (final ICargoBlueprint cargoBlueprint : inventoryLocalState.getCargoBlueprintsToGenerate()) {
				final LoadSlot loadSlot = createLoadSlot(cargoBlueprint, cec, commands, ScenarioModelUtil.getCargoModel(sdp), globalStates, i);
				final DischargeSlot dischargeSlot = createDischargeSlot(cargoBlueprint, cec, commands, globalStates, i, loadSlot, sdp, vesselToVa);
				final Cargo cargo = CargoEditingCommands.createNewCargo(editingDomain, commands, ScenarioModelUtil.getCargoModel(sdp), null, 0);
				loadSlot.setCargo(cargo);
				dischargeSlot.setCargo(cargo);
				cargo.setAllowRewiring(!cargoBlueprint.getMudContainer().getEntity().isThirdParty());
				final Vessel vessel = cargoBlueprint.getVessel();
				if (!dischargeSlot.isFOBSale() && vessel != null) {
					final VesselAvailability va = vesselToVa.get(vessel);
					if (va != null) {
						cargo.setVesselAssignmentType(va);
					}
					if (globalStates.getAdpGlobalState().getNominalMarket() != null) {
						final Vessel adpNominalVessel = globalStates.getAdpGlobalState().getNominalMarket().getVessel();
						if (adpNominalVessel != null && dischargeSlot.isRestrictedVesselsOverride()) {
							final List<AVesselSet<Vessel>> restrictedVessels = dischargeSlot.getRestrictedVessels();
							if (!restrictedVessels.contains(adpNominalVessel)) {
								restrictedVessels.add(adpNominalVessel);
							}
						}
					}
				}
				++i;
			}
		}
		commands.forEach(cmd::append);
	}

	private static IMullProfile createVanillaMullProfile(final IMullProfile originalMullProfile) {
		final Set<Vessel> seenVessels = new HashSet<>();
		final List<IMullSubprofile> vanillaSubprofiles = new ArrayList<>();
		for (final IMullSubprofile mullSubprofile : originalMullProfile.getMullSubprofiles()) {
			final List<IEntityRow> vanillaEntityRows = new ArrayList<>();
			for (final IEntityRow entityRow : mullSubprofile.getEntityRows()) {
				// Ignore sales contracts
				final List<DesSalesMarketAllocationRow> vanillaDesMarketRows = new ArrayList<>();
				for (final DesSalesMarketAllocationRow desMarketRow : entityRow.getDesSalesMarketRows()) {
					if (desMarketRow.getVessels().size() != 1) {
						throw new IllegalStateException("Market rows should only have one vessel");
					}
					if (seenVessels.contains(desMarketRow.getVessels().get(0))) {
						throw new IllegalStateException("Vessels must not be shared");
					}
					vanillaDesMarketRows.add(new DesSalesMarketAllocationRow(desMarketRow.getDesSalesMarket(), desMarketRow.getWeight(), new ArrayList<>(desMarketRow.getVessels())));
				}
				vanillaEntityRows.add(new EntityRow(entityRow.getEntity(), entityRow.getInitialAllocation(), entityRow.getRelativeEntitlement(), Collections.emptyList(), vanillaDesMarketRows));
			}
			vanillaSubprofiles.add(new MullSubprofile(mullSubprofile.getInventory(), vanillaEntityRows));
		}
		return new MullProfile(vanillaSubprofiles);
	}

	private static DischargeSlot createDischargeSlot(final ICargoBlueprint cargoBlueprint, final CargoEditingCommands cec, final List<Command> commands, final GlobalStatesContainer globalStates,
			final int loadIndex, final LoadSlot loadSlot, IScenarioDataProvider sdp, final Map<Vessel, @Nullable VesselAvailability> vesselToVa) {
		final IAllocationTracker allocationTracker = cargoBlueprint.getAllocationTracker();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		if (allocationTracker instanceof final SalesContractTracker salesContractTracker) {
			final DischargeSlot dischargeSlot = cec.createNewDischarge(commands, cargoModel, false);
			dischargeSlot.setContract(salesContractTracker.getSalesContract());
			dischargeSlot.setEntity(loadSlot.getEntity());
			dischargeSlot.setPort(salesContractTracker.getSalesContract().getPreferredPort());
			final Vessel vessel = cargoBlueprint.getVessel();
			if (vessel == null) {
				throw new IllegalStateException("Shipped cargoes must have a vessel");
			}
			final LocalDate dischargeDate = IAllocationTracker.calculateDischargeDate(loadSlot, dischargeSlot, vessel, sdp, vesselToVa);
			dischargeSlot.setWindowStart(dischargeDate);
			dischargeSlot.setWindowStartTime(0);
			dischargeSlot.setWindowSize(1);
			dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);
			if (!cargoBlueprint.getAllocationTracker().getVessels().isEmpty()) {
				dischargeSlot.setRestrictedVesselsArePermissive(true);
				dischargeSlot.setRestrictedVesselsOverride(true);
				dischargeSlot.getRestrictedVessels().addAll(cargoBlueprint.getAllocationTracker().getVessels());
			}
			dischargeSlot.setRestrictedPortsArePermissive(true);
			dischargeSlot.setRestrictedPortsOverride(true);
			dischargeSlot.getRestrictedPorts().add(loadSlot.getPort());
			final String id = String.format("des-sale-%s", loadSlot.getName());
			dischargeSlot.setName(id);
			return dischargeSlot;
		} else {
			final DischargeSlot dischargeSlot;
			if (allocationTracker.isSharingVessels()) {
				final DESSalesMarket desSalesMarket = ((DesMarketTracker) allocationTracker).getSalesMarket();
				dischargeSlot = cec.createNewSpotDischarge(commands, cargoModel, desSalesMarket);
				final Vessel vessel = cargoBlueprint.getVessel();
				if (vessel == null) {
					throw new IllegalStateException("Shipped cargoes must have a vessel");
				}
				final LocalDate dischargeDate = IAllocationTracker.calculateDischargeDate(loadSlot, dischargeSlot, vessel, sdp, vesselToVa);
				dischargeSlot.setWindowStart(dischargeDate);
				dischargeSlot.setWindowStartTime(0);
				dischargeSlot.setWindowSize(1);
				dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);
				final String id = String.format("market-%s", loadSlot.getName());
				dischargeSlot.setName(id);
			} else {
				dischargeSlot = cec.createNewDischarge(commands, cargoModel, true);
				dischargeSlot.setWindowStart(loadSlot.getWindowStart());
				dischargeSlot.setWindowStartTime(loadSlot.getWindowStartTime());
				dischargeSlot.setWindowSize(loadSlot.getWindowSize());
				dischargeSlot.setWindowSizeUnits(loadSlot.getWindowSizeUnits());
				final String id = String.format("fob-sale-%s", loadSlot.getName());
				dischargeSlot.setName(id);
				dischargeSlot.setPriceExpression("JKM");
				dischargeSlot.setPort(loadSlot.getPort());
				dischargeSlot.setFobSaleDealType(FOBSaleDealType.SOURCE_ONLY);
				dischargeSlot.setEntity(loadSlot.getEntity());
				final Vessel vessel = cargoBlueprint.getVessel();
				if (vessel != null) {
					dischargeSlot.setNominatedVessel(vessel);
				}
			}
			return dischargeSlot;
		}
	}

	private static LoadSlot createLoadSlot(final ICargoBlueprint cargoBlueprint, final CargoEditingCommands cec, final List<Command> commands, final CargoModel cargoModel,
			final GlobalStatesContainer globalStates, final int loadIndex) {
		final LoadSlot loadSlot = cec.createNewLoad(commands, cargoModel, false);
		loadSlot.setPort(cargoBlueprint.getMullContainer().getInventory().getPort());
		loadSlot.setContract(globalStates.getInventoryGlobalStates().get(cargoBlueprint.getMullContainer().getInventory()).getPurchaseContract());
		loadSlot.setVolumeLimitsUnit(VolumeUnits.M3);
		final int volumeHigh = cargoBlueprint.getAllocatedVolume() + globalStates.getMullGlobalState().getVolumeFlex();
		final int volumeLow = cargoBlueprint.getAllocatedVolume() - globalStates.getMullGlobalState().getVolumeFlex();
		loadSlot.setMinQuantity(volumeLow);
		loadSlot.setMaxQuantity(volumeHigh);
		final String loadSlotName = String.format("%s-%03d", cargoBlueprint.getMullContainer().getInventory().getName(), loadIndex);
		loadSlot.setName(loadSlotName);
		loadSlot.setWindowStart(cargoBlueprint.getWindowStart().toLocalDate());
		loadSlot.setWindowStartTime(cargoBlueprint.getWindowStart().getHour());
		loadSlot.setWindowSize(cargoBlueprint.getWindowSizeHours());
		loadSlot.setWindowSizeUnits(TimePeriod.HOURS);
		loadSlot.setEntity(cargoBlueprint.getMudContainer().getEntity());
		return loadSlot;
	}

	private static void addDeleteCommands(final GlobalStatesContainer globalStates, final CompoundCommand cmd, final IScenarioDataProvider sdp, final EditingDomain editingDomain) {
		final List<Cargo> cargoesToDelete;
		final List<LoadSlot> loadSlotsToDelete;
		final List<DischargeSlot> dischargeSlotsToDelete;

		final YearMonth adpStart = globalStates.getAdpGlobalState().getAdpStartYearMonth();
		final YearMonth adpEndExc = globalStates.getAdpGlobalState().getAdpEndExclusiveYearMonth();

		if (globalStates.getMullGlobalState().getCargoesToKeep().isEmpty()) {
			cargoesToDelete = ScenarioModelUtil.getCargoModel(sdp).getCargoes().stream() //
					.filter(c -> {
						final YearMonth loadYm = YearMonth.from(((LoadSlot) c.getSlots().get(0)).getWindowStart());
						return !loadYm.isBefore(adpStart) && loadYm.isBefore(adpEndExc);
					}) //
					.toList();
			loadSlotsToDelete = cargoesToDelete.stream() //
					.map(c -> (LoadSlot) c.getSlots().get(0)) //
					.toList();
			dischargeSlotsToDelete = cargoesToDelete.stream() //
					.map(c -> (DischargeSlot) c.getSlots().get(1)) //
					.toList();
		} else {
			final Set<Cargo> cargoesToKeepSet = globalStates.getMullGlobalState().getCargoesToKeep().stream() //
					.map(wrapper -> wrapper.getLoadSlot().getCargo()) //
					.collect(Collectors.toSet());
			final int numSlotsToDelete = ScenarioModelUtil.getCargoModel(sdp).getCargoes().size() - cargoesToKeepSet.size();
			loadSlotsToDelete = new ArrayList<>(numSlotsToDelete);
			dischargeSlotsToDelete = new ArrayList<>(numSlotsToDelete);
			cargoesToDelete = new ArrayList<>(numSlotsToDelete);
			for (final Cargo cargo : ScenarioModelUtil.getCargoModel(sdp).getCargoes()) {
				if (!cargoesToKeepSet.contains(cargo)) {
					final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
					final YearMonth loadYm = YearMonth.from(loadSlot.getWindowStart());
					if (!loadYm.isBefore(adpStart) && loadYm.isBefore(adpEndExc)) {
						cargoesToDelete.add(cargo);
						loadSlotsToDelete.add(loadSlot);
						dischargeSlotsToDelete.add((DischargeSlot) cargo.getSlots().get(1));
					}
				}
			}
		}
		if (!cargoesToDelete.isEmpty()) {
			final List<EObject> objectsToDelete = new ArrayList<>(cargoesToDelete.size() + loadSlotsToDelete.size() + dischargeSlotsToDelete.size());
			objectsToDelete.addAll(cargoesToDelete);
			objectsToDelete.addAll(loadSlotsToDelete);
			objectsToDelete.addAll(dischargeSlotsToDelete);
			cmd.append(DeleteCommand.create(editingDomain, objectsToDelete));
		}
	}

	private static GlobalStatesContainer buildGlobalStates(final com.mmxlabs.models.lng.adp.MullProfile mullProfile, final ADPModel adpModel, final LNGScenarioModel sm) {
		final AdpGlobalState adpGlobalState = buildAdpGlobalState(adpModel);
		final List<InventoryGlobalState> inventoryGlobalStates = new ArrayList<>(mullProfile.getInventories().size());
		for (final com.mmxlabs.models.lng.adp.MullSubprofile subprofile : mullProfile.getInventories()) {
			final Inventory inventory = subprofile.getInventory();
			if (inventory == null) {
				throw new IllegalStateException("Mull subprofile must be associated with an inventory");
			}
			inventoryGlobalStates.add(buildInventoryGlobalState(inventory, adpGlobalState, sm));
		}
		final MullGlobalState mullGlobalState = buildMullGlobalState(mullProfile, sm);
		return new GlobalStatesContainer(adpGlobalState, inventoryGlobalStates, mullGlobalState);
	}

	private static GlobalStatesContainer buildVanillaGlobalStates(final com.mmxlabs.models.lng.adp.MullProfile mullProfile, final ADPModel adpModel, final LNGScenarioModel sm) {
		final AdpGlobalState adpGlobalState = buildAdpGlobalState(adpModel);
		final List<InventoryGlobalState> inventoryGlobalStates = new ArrayList<>(mullProfile.getInventories().size());
		for (final com.mmxlabs.models.lng.adp.MullSubprofile subprofile : mullProfile.getInventories()) {
			final Inventory inventory = subprofile.getInventory();
			if (inventory == null) {
				throw new IllegalStateException("Mull subprofile must be associated with an inventory");
			}
			inventoryGlobalStates.add(buildInventoryGlobalState(inventory, adpGlobalState, sm));
		}
		final MullGlobalState mullGlobalState = buildVanillaMullGlobalState(mullProfile, sm);
		return new GlobalStatesContainer(adpGlobalState, inventoryGlobalStates, mullGlobalState);
	}

	private static AdpGlobalState buildAdpGlobalState(final ADPModel adpModel) {
		final YearMonth adpStart = adpModel.getYearStart();
		if (adpStart == null) {
			throw new IllegalStateException("ADP year start must not be null");
		}
		final YearMonth adpEnd = adpModel.getYearEnd();
		if (adpEnd == null) {
			throw new IllegalStateException("ADP year end must not be null");
		}
		final CharterInMarket nominalMarket = adpModel.getFleetProfile().getDefaultNominalMarket();
		if (nominalMarket == null) {
			throw new IllegalStateException("ADP nominal market must be provided");
		}
		return new AdpGlobalState(adpStart, adpEnd, nominalMarket);
	}

	private static InventoryGlobalState buildInventoryGlobalState(final Inventory inventory, final AdpGlobalState adpGlobalState, final LNGScenarioModel sm) {
		if (inventory.getPort() == null) {
			throw new IllegalStateException("Inventory must be at a port");
		}
		final Port inventoryPort = inventory.getPort();
		final Optional<PurchaseContract> optInventoryPurchaseContract = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts().stream() //
				.filter(c -> inventoryPort == c.getPreferredPort()) //
				.findFirst();
		if (optInventoryPurchaseContract.isEmpty()) {
			throw new IllegalStateException("Inventory must correspond to purchase contract");
		}
		final PurchaseContract inventoryPurchaseContract = optInventoryPurchaseContract.get();
		return new InventoryGlobalState(inventory, inventoryPurchaseContract, adpGlobalState.getStartDateTime(), adpGlobalState.getEndDateTimeExclusive());
	}

	private static MullGlobalState buildMullGlobalState(final com.mmxlabs.models.lng.adp.MullProfile mullProfile, final LNGScenarioModel sm) {
		final int loadWindowInDays = mullProfile.getWindowSize();
		final int volumeFlex = mullProfile.getVolumeFlex();
		final int fullCargoLotValue = mullProfile.getFullCargoLotValue();
		final int cargoVolume = 160_000;
		final List<MullCargoWrapper> cargoesToKeep = new ArrayList<>(mullProfile.getCargoesToKeep().size());
		for (final MullCargoWrapper mullCargoWrapper : mullProfile.getCargoesToKeep()) {
			if (mullCargoWrapper == null) {
				throw new IllegalStateException("Mull fixed cargo must be non-null");
			}
			cargoesToKeep.add(mullCargoWrapper);
		}
		final Set<BaseLegalEntity> firstPartyEntities = new HashSet<>();
		for (final BaseLegalEntity entity : ScenarioModelUtil.getCommercialModel(sm).getEntities()) {
			if (!entity.isThirdParty()) {
				firstPartyEntities.add(entity);
			}
		}
		return new MullGlobalState(loadWindowInDays, volumeFlex, fullCargoLotValue, cargoVolume, cargoesToKeep, firstPartyEntities);
	}

	private static MullGlobalState buildVanillaMullGlobalState(final com.mmxlabs.models.lng.adp.MullProfile mullProfile, final LNGScenarioModel sm) {
		final int loadWindowInDays = mullProfile.getWindowSize();
		final int volumeFlex = 0;
		final int fullCargoLotValue = mullProfile.getFullCargoLotValue();
		final int cargoVolume = 160_000;
		final List<MullCargoWrapper> cargoesToKeep = new ArrayList<>(mullProfile.getCargoesToKeep().size());
		for (final MullCargoWrapper mullCargoWrapper : mullProfile.getCargoesToKeep()) {
			if (mullCargoWrapper == null) {
				throw new IllegalStateException("Mull fixed cargo must be non-null");
			}
			cargoesToKeep.add(mullCargoWrapper);
		}
		final Set<BaseLegalEntity> firstPartyEntities = new HashSet<>();
		for (final BaseLegalEntity entity : ScenarioModelUtil.getCommercialModel(sm).getEntities()) {
			if (!entity.isThirdParty()) {
				firstPartyEntities.add(entity);
			}
		}
		return new MullGlobalState(loadWindowInDays, volumeFlex, fullCargoLotValue, cargoVolume, cargoesToKeep, firstPartyEntities);
	}

	private static IMullProfile createInternalMullProfile(final com.mmxlabs.models.lng.adp.MullProfile eMullProfile) {
		return new MullProfile(eMullProfile);
	}

	private static Map<Inventory, Map<YearMonth, List<Cargo>>> buildMonthMappedExistingCargoes(final HarmonisationMullAlgorithm mullAlgorithm) {
		final Map<Inventory, Map<YearMonth, List<Cargo>>> monthMappedExistingCargoes = new HashMap<>();
		for (final Entry<Inventory, SortedMap<LocalDateTime, Cargo>> inventoryToMapEntry : mullAlgorithm.getFixedCargoes().entrySet()) {
			final Map<YearMonth, List<Cargo>> ymToCargoesMap = new HashMap<>();
			for (final Entry<LocalDateTime, Cargo> cargoEntry : inventoryToMapEntry.getValue().entrySet()) {
				ymToCargoesMap.computeIfAbsent(YearMonth.from(cargoEntry.getKey()), k -> new LinkedList<>()).add(cargoEntry.getValue());
			}
			monthMappedExistingCargoes.put(inventoryToMapEntry.getKey(), ymToCargoesMap);
		}
		return monthMappedExistingCargoes;
	}

	private static void setMonthlyCounts(final List<InventoryLocalState> inventoryLocalStates, final Map<Inventory, HarmonisationTransformationState> harmonisationTransformationsStates,
			final Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Integer>> inventoryToAacqsSatisfiedByFixedCargoes,
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts, final GlobalStatesContainer globalStates) {
		final Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, IAllocationTracker>> inventoryToAllocationTrackerMap = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, IMudContainer>> inventoryToMudContainerMap = new HashMap<>();
		final Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Pair<IMudContainer, IAllocationTracker>>> inventoryToRemappingTrackers = new HashMap<>();
		for (final InventoryLocalState inventoryLocalState : inventoryLocalStates) {
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, IAllocationTracker> allocationTrackerMap = new HashMap<>();
			final Map<BaseLegalEntity, IMudContainer> mudContainerMap = new HashMap<>();
			inventoryToAllocationTrackerMap.put(inventoryLocalState.getInventoryGlobalState().getInventory(), allocationTrackerMap);
			inventoryToMudContainerMap.put(inventoryLocalState.getInventoryGlobalState().getInventory(), mudContainerMap);
			for (final IMudContainer mudContainer : inventoryLocalState.getMullContainer().getMudContainers()) {
				final BaseLegalEntity entity = mudContainer.getEntity();
				mudContainerMap.put(entity, mudContainer);
				for (final IAllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
					final IMullDischargeWrapper dischargeWrapper = MullUtil.buildDischargeWrapper(allocationTracker);
					allocationTrackerMap.put(Pair.of(entity, dischargeWrapper), allocationTracker);
				}
			}
			inventoryToRemappingTrackers.put(inventoryLocalState.getInventoryGlobalState().getInventory(), new HashMap<>());
		}
		for (final InventoryLocalState inventoryLocalState : inventoryLocalStates) {
			final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, IAllocationTracker> allocationTrackerMap = inventoryToAllocationTrackerMap.get(inventory);
			final Map<BaseLegalEntity, IMudContainer> mudContainerMap = inventoryToMudContainerMap.get(inventory);
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Pair<IMudContainer, IAllocationTracker>> remappingTrackers = inventoryToRemappingTrackers.get(inventory);
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Integer> aacqsSatisfiedByFixedCargoes = inventoryToAacqsSatisfiedByFixedCargoes.get(inventory);
			final Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>> monthlyCounts = inventoryToMonthlyCounts.get(inventory);
			final HarmonisationTransformationState harmonisationTransformationState = harmonisationTransformationsStates.get(inventory);
			final Map<IAllocationTracker, Map<YearMonth, Integer>> harmonisedMonthlyCounts = new HashMap<>();
			final Map<BaseLegalEntity, Pair<List<IMullDischargeWrapper>, Map<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>>>> rearrangement = harmonisationTransformationState
					.getRearrangedProfile();
			for (final Entry<BaseLegalEntity, Pair<List<IMullDischargeWrapper>, Map<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>>>> remappingEntry : rearrangement
					.entrySet()) {
				final List<IMullDischargeWrapper> directMappings = remappingEntry.getValue().getFirst();
				final IMudContainer harmonisationMudContainer = mudContainerMap.get(remappingEntry.getKey());
				for (final IMullDischargeWrapper dischargeWrapper : directMappings) {
					final Pair<BaseLegalEntity, IMullDischargeWrapper> pair = Pair.of(remappingEntry.getKey(), dischargeWrapper);

					final IAllocationTracker harmonisationTracker = allocationTrackerMap.get(pair);
					harmonisationTracker.setAllocatedAacq(aacqsSatisfiedByFixedCargoes.get(pair));
					final Map<YearMonth, Integer> localMonthlyCounts = new HashMap<>();
					harmonisedMonthlyCounts.put(harmonisationTracker, localMonthlyCounts);
					monthlyCounts.get(remappingEntry.getKey()).entrySet().forEach(entry -> localMonthlyCounts.put(entry.getKey(), entry.getValue().get(dischargeWrapper)));
					harmonisationTracker.setMonthlyAllocations(localMonthlyCounts);
					remappingTrackers.put(pair, Pair.of(harmonisationMudContainer, harmonisationTracker));
				}
				final Map<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>> combinedMappings = remappingEntry.getValue().getSecond();
				for (final Entry<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>> combinedMappingsEntry : combinedMappings.entrySet()) {
					assert !combinedMappingsEntry.getValue().isEmpty();
					final Pair<BaseLegalEntity, IMullDischargeWrapper> pair = Pair.of(remappingEntry.getKey(), combinedMappingsEntry.getKey());
					final IAllocationTracker harmonisationTracker = allocationTrackerMap.get(pair);
					final List<Pair<BaseLegalEntity, IMullDischargeWrapper>> outputList = new LinkedList<>(combinedMappingsEntry.getValue());
					outputList.add(Pair.of(remappingEntry.getKey(), combinedMappingsEntry.getKey()));
					harmonisationTracker.setAllocatedAacq(outputList.stream().mapToInt(aacqsSatisfiedByFixedCargoes::get).sum());
					final Map<YearMonth, Integer> localMonthlyCounts = new HashMap<>();
					harmonisedMonthlyCounts.put(harmonisationTracker, localMonthlyCounts);
					for (YearMonth ym = globalStates.getAdpGlobalState().getAdpStartYearMonth(); ym.isBefore(globalStates.getAdpGlobalState().getAdpEndExclusiveYearMonth()); ym = ym.plusMonths(1L)) {
						final YearMonth currentYm = ym;
						final int count = outputList.stream().mapToInt(p -> monthlyCounts.get(p.getFirst()).get(currentYm).get(p.getSecond())).sum();
						localMonthlyCounts.put(currentYm, count);
					}
					harmonisationTracker.setMonthlyAllocations(localMonthlyCounts);
					final Pair<IMudContainer, IAllocationTracker> harmonisationPair = Pair.of(harmonisationMudContainer, harmonisationTracker);
					outputList.forEach(p -> remappingTrackers.put(p, harmonisationPair));
				}
			}
		}
	}

	private static Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Integer>> getAacqsSatisfiedByFixedCargoes(final IMullProfile mullProfile,
			final GlobalStatesContainer globalStates) {
		final Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Integer>> inventoryToAacqsSatisfiedByFixedCargoes = new HashMap<>();
		for (final IMullSubprofile mullSubprofile : mullProfile.getMullSubprofiles()) {
			final Inventory inventory = mullSubprofile.getInventory();
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Integer> aacqsSatisfiedByFixedCargoes = new HashMap<>();
			inventoryToAacqsSatisfiedByFixedCargoes.put(inventory, aacqsSatisfiedByFixedCargoes);
			for (final IEntityRow entityRow : mullSubprofile.getEntityRows()) {
				entityRow.streamAllocationRows().forEach(allocationRow -> {
					aacqsSatisfiedByFixedCargoes.put(Pair.of(entityRow.getEntity(), MullUtil.buildDischargeWrapper(allocationRow)), 0);
				});
			}
		}
		final Map<Port, @Nullable Inventory> portToInventoryMap = globalStates.getInventoryGlobalStates().values().stream()
				.collect(Collectors.toMap(InventoryGlobalState::getPort, InventoryGlobalState::getInventory));
		globalStates.getMullGlobalState().getCargoesToKeep().forEach(cargoToKeep -> {
			final LoadSlot loadSlot = cargoToKeep.getLoadSlot();
			final DischargeSlot dischargeSlot = cargoToKeep.getDischargeSlot();
			@Nullable
			final BaseLegalEntity entity = loadSlot.getEntity();
			if (entity == null) {
				return;
			}
			@Nullable
			final Inventory inventory = portToInventoryMap.get(loadSlot.getPort());
			if (inventory == null) {
				return;
			}
			final Pair<BaseLegalEntity, IMullDischargeWrapper> pair;
			if (dischargeSlot instanceof final SpotDischargeSlot spotDischargeSlot) {
				@Nullable
				final SpotMarket spotMarket = spotDischargeSlot.getMarket();
				if (spotMarket != null && spotMarket instanceof final DESSalesMarket desSalesMarket) {
					pair = Pair.of(entity, new MullDesSalesMarketWrapper(desSalesMarket));
				} else {
					return;
				}
			} else {
				@Nullable
				final SalesContract salesContract = dischargeSlot.getContract();
				if (salesContract != null) {
					pair = Pair.of(entity, new MullSalesContractWrapper(salesContract));
				} else {
					return;
				}
			}
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Integer> aacqsSatisfiedByFixedCargoes = inventoryToAacqsSatisfiedByFixedCargoes.get(inventory);
			aacqsSatisfiedByFixedCargoes.computeIfPresent(pair, (key, currentCount) -> currentCount + 1);
		});
		return inventoryToAacqsSatisfiedByFixedCargoes;
	}

	private static AlgorithmState initialiseAlgorithmState(final IMullProfile mullProfile, final GlobalStatesContainer globalStatesContainer) {
		final Map<Vessel, LocalDateTime> vesselUsageLookBehind = new HashMap<>();
		final Map<Vessel, LocalDateTime> vesselUsageLookAhead = new HashMap<>();
		final Set<Vessel> firstPartyVessels = new HashSet<>();

		for (final IMullSubprofile subprofile : mullProfile.getMullSubprofiles()) {
			for (final IEntityRow entityRow : subprofile.getEntityRows()) {
				entityRow.streamAllocationRows() //
						.map(IAllocationRow::getVessels) //
						.flatMap(List::stream) //
						.forEach(vessel -> vesselUsageLookBehind.put(vessel, globalStatesContainer.getAdpGlobalState().getDateTimeBeforeAdpStart()));
				if (globalStatesContainer.getMullGlobalState().getFirstPartyEntities().contains(entityRow.getEntity())) {
					entityRow.streamAllocationRows() //
							.map(IAllocationRow::getVessels) //
							.flatMap(List::stream) //
							.forEach(firstPartyVessels::add);
				}
			}
		}
		return new AlgorithmState(mullProfile, vesselUsageLookBehind, vesselUsageLookAhead, firstPartyVessels);
	}

	private static IMullProfile buildPhase1aMullProfile(final IMullProfile previousMullProfile, final List<InventoryLocalState> inventoryLocalStates) {
		final Map<Inventory, @Nullable InventoryLocalState> inventoryLocalStateMap = inventoryLocalStates.stream()
				.collect(Collectors.toMap(state -> state.getInventoryGlobalState().getInventory(), Function.identity()));
		final Map<DesSalesMarketAllocationRow, DesMarketTracker> oldDesMarketRowToNewWeightMap = new HashMap<>();
		for (final IMullSubprofile mullSubprofile : previousMullProfile.getMullSubprofiles()) {
			final InventoryLocalState inventoryLocalState = inventoryLocalStateMap.get(mullSubprofile.getInventory());
			if (inventoryLocalState == null) {
				throw new IllegalStateException("Could not find expected inventory");
			}
			final Map<BaseLegalEntity, @Nullable IMudContainer> mudContainerMap = inventoryLocalState.getMullContainer().getMudContainers().stream()
					.collect(Collectors.toMap(IMudContainer::getEntity, Function.identity()));
			for (final IEntityRow entityRow : mullSubprofile.getEntityRows()) {
				final IMudContainer mudContainer = mudContainerMap.get(entityRow.getEntity());
				if (mudContainer == null) {
					throw new IllegalStateException("Could not find expected entity container");
				}
				if (entityRow.getDesSalesMarketRows().size() != 1) {
					throw new IllegalStateException("Found more DES Sales Market rows than expected");
				}
				final DesSalesMarketAllocationRow desSalesMarketAllocationRow = entityRow.getDesSalesMarketRows().get(0);
				final DesMarketTracker desMarketTracker = mudContainer.getDesMarketTracker();
				if (desMarketTracker.getSalesMarket() != desSalesMarketAllocationRow.getDesSalesMarket()) {
					throw new IllegalStateException("Found mismatching DES sales markets");
				}
				oldDesMarketRowToNewWeightMap.put(desSalesMarketAllocationRow, desMarketTracker);
			}
		}

		final Map<DesMarketTracker, Integer> desMarketTrackerCount = inventoryLocalStates.stream() //
				.map(InventoryLocalState::getCargoBlueprintsToGenerate) //
				.flatMap(List::stream) //
				.map(ICargoBlueprint::getAllocationTracker) //
				.filter(DesMarketTracker.class::isInstance) //
				.map(DesMarketTracker.class::cast) //
				.collect(Collectors.toMap(Function.identity(), k -> 1, (i1, i2) -> i1 + i2));

		final List<IMullSubprofile> newMullSubprofiles = new ArrayList<>();
		for (final IMullSubprofile oldMullSubprofile : previousMullProfile.getMullSubprofiles()) {
			final List<IEntityRow> newEntityRows = new ArrayList<>();
			for (final IEntityRow oldEntityRow : oldMullSubprofile.getEntityRows()) {
				final List<SalesContractAllocationRow> newSalesContractAllocationRows = oldEntityRow.getSalesContractRows().stream() //
						.map(row -> new SalesContractAllocationRow(row.getSalesContract(), row.getWeight(), new ArrayList<>(row.getVessels()))) //
						.toList();
				final List<DesSalesMarketAllocationRow> newDesMarketAllocationRows = oldEntityRow.getDesSalesMarketRows().stream() //
						.map(row -> new DesSalesMarketAllocationRow(row.getDesSalesMarket(), desMarketTrackerCount.compute(oldDesMarketRowToNewWeightMap.get(row), (k, v) -> v != null ? v : 0),
								new ArrayList<>(row.getVessels()))) //
						.toList();
				newEntityRows.add(new EntityRow(oldEntityRow.getEntity(), oldEntityRow.getInitialAllocation(), oldEntityRow.getRelativeEntitlement(), newSalesContractAllocationRows,
						newDesMarketAllocationRows));
			}
			newMullSubprofiles.add(new MullSubprofile(oldMullSubprofile.getInventory(), newEntityRows));
		}
		return new MullProfile(newMullSubprofiles);
	}

	private static List<InventoryLocalState> createFromProfileVanillaInventoryLocalStates(final IMullProfile mullProfile, final GlobalStatesContainer globalStatesContainer,
			final AlgorithmState algorithmState, final IMullStrategyContainer mullStrategies) {
		final List<InventoryLocalState> inventoryLocalStates = new ArrayList<>();

		for (final IMullSubprofile mullSubprofile : mullProfile.getMullSubprofiles()) {
			final IMullContainerBuildingStrategy mullContainerBuildingStrategy = new FromProfileBuildingStrategy(mullSubprofile, mullStrategies);
			final IMullContainer mullContainer = mullContainerBuildingStrategy.build(globalStatesContainer, algorithmState);

			mullContainer.getMudContainers().stream() //
					.map(IMudContainer::getAllocationTrackers) //
					.flatMap(List::stream) //
					.forEach(allocationTracker -> allocationTracker.setVesselSharing(algorithmState.getFirstPartyVessels()));

			final InventoryGlobalState inventoryGlobalState = globalStatesContainer.getInventoryGlobalStates().get(mullSubprofile.getInventory());
			final IRollingWindow rollingLoadWindow = new VanillaRollingLoadWindow(inventoryGlobalState, inventoryGlobalState.getInsAndOuts().entrySet().iterator(),
					inventoryGlobalState.getLoadDuration(), inventoryGlobalState.getPreAdpStartTankVolume(), LocalTime.of(0, 0));
			final InventoryLocalState inventoryLocalState = new InventoryLocalState(inventoryGlobalState, rollingLoadWindow, mullContainer);
			inventoryLocalStates.add(inventoryLocalState);
		}
		return inventoryLocalStates;
	}

	private static List<InventoryLocalState> createFromProfileInventoryLocalStates(final IMullProfile mullProfile, final GlobalStatesContainer globalStatesContainer,
			final AlgorithmState algorithmState, final IMullStrategyContainer mullStrategies) {
		final List<InventoryLocalState> inventoryLocalStates = new ArrayList<>();

		for (final IMullSubprofile mullSubprofile : mullProfile.getMullSubprofiles()) {
			final IMullContainerBuildingStrategy mullContainerBuildingStrategy = new FromProfileBuildingStrategy(mullSubprofile, mullStrategies);
			final IMullContainer mullContainer = mullContainerBuildingStrategy.build(globalStatesContainer, algorithmState);

			mullContainer.getMudContainers().stream() //
					.map(IMudContainer::getAllocationTrackers) //
					.flatMap(List::stream) //
					.forEach(allocationTracker -> allocationTracker.setVesselSharing(algorithmState.getFirstPartyVessels()));

			final InventoryGlobalState inventoryGlobalState = globalStatesContainer.getInventoryGlobalStates().get(mullSubprofile.getInventory());
			final IRollingWindow rollingLoadWindow = new RollingLoadWindow(inventoryGlobalState, inventoryGlobalState.getInsAndOuts().entrySet().iterator(), inventoryGlobalState.getLoadDuration(),
					inventoryGlobalState.getPreAdpStartTankVolume());
			final InventoryLocalState inventoryLocalState = new InventoryLocalState(inventoryGlobalState, rollingLoadWindow, mullContainer);
			inventoryLocalStates.add(inventoryLocalState);
		}
		return inventoryLocalStates;
	}

	private static Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> buildHarmonisationMap(final IMonthEndTrackingAlgorithm mullAlgorithm) {
		final Map<Port, @Nullable InventoryLocalState> portToInventoryLocalStateMap = mullAlgorithm.getInventoryLocalStates().stream()
				.collect(Collectors.toMap(ils -> ils.getInventoryGlobalState().getInventory().getPort(), Function.identity()));

		final Set<ICargoBlueprint> sameMonthShiftCargoBlueprints = new HashSet<>();
		final List<Pair<ICargoBlueprint, LocalDateTime>> differentMonthShiftCargoBlueprints = new ArrayList<>();
		final Set<ICargoBlueprint> usedCargoBlueprints = new HashSet<>();
		for (final MullCargoWrapper fixedCargoWrapper : mullAlgorithm.getGlobalStatesContainer().getMullGlobalState().getCargoesToKeep()) {
			@Nullable
			final InventoryLocalState inventoryLocalState = portToInventoryLocalStateMap.get(fixedCargoWrapper.getLoadSlot().getPort());
			if (inventoryLocalState == null) {
				continue;
			}
			@Nullable
			final FixedCargoMatch fixedCargoMatch = findAllocationTracker(inventoryLocalState.getMullContainer(), fixedCargoWrapper);
			if (fixedCargoMatch == null) {
				throw new IllegalStateException("Could not match inventory loading cargo to existing allocation tracker");
			}
			matchCargoBlueprintsWithFixedCargoes(fixedCargoMatch, sameMonthShiftCargoBlueprints, differentMonthShiftCargoBlueprints, usedCargoBlueprints,
					inventoryLocalState.getCargoBlueprintsToGenerate());
		}

		final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedEntityCargoBlueprints = new HashMap<>();
		final Map<Inventory, Map<YearMonth, Set<ICargoBlueprint>>> monthMappedCargoBlueprints = new HashMap<>();
		final Map<Inventory, Map<IAllocationTracker, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedAllocationTrackerCargoBlueprints = new HashMap<>();
		fillMonthMappingDetails(monthMappedEntityCargoBlueprints, monthMappedCargoBlueprints, monthMappedAllocationTrackerCargoBlueprints, mullAlgorithm.getInventoryLocalStates(),
				usedCargoBlueprints);

		final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts = calculateMonthlyCounts(mullAlgorithm.getInventoryLocalStates(),
				mullAlgorithm.getGlobalStatesContainer());
		reduceUsedCargoBlueprints(inventoryToMonthlyCounts, usedCargoBlueprints);
		handleDifferentMonthShiftCargoes(differentMonthShiftCargoBlueprints, monthMappedCargoBlueprints, monthMappedEntityCargoBlueprints, monthMappedAllocationTrackerCargoBlueprints,
				inventoryToMonthlyCounts, mullAlgorithm.getMonthEndEntitlements(), mullAlgorithm.getGlobalStatesContainer());
		return inventoryToMonthlyCounts;
	}

	private static void handleDifferentMonthShiftCargoes(final List<Pair<ICargoBlueprint, LocalDateTime>> differentMonthShiftCargoBlueprints,
			final Map<Inventory, Map<YearMonth, Set<ICargoBlueprint>>> monthMappedCargoBlueprints,
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedEntityCargoBlueprints,
			final Map<Inventory, Map<IAllocationTracker, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedAllocationTrackerCargoBlueprints,
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts,
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Long>>> inventoryToEntityToMonthEndEntitlements, final GlobalStatesContainer globalStates) {
		if (differentMonthShiftCargoBlueprints.isEmpty()) {
			return;
		}
		final HarmonisationPhaseSameEntityCargoMatchingComparatorWrapper sameEntityComparatorWrapper = new HarmonisationPhaseSameEntityCargoMatchingComparatorWrapper();
		for (final Pair<ICargoBlueprint, LocalDateTime> pair : differentMonthShiftCargoBlueprints) {
			final ICargoBlueprint fixedCargoMatchedBlueprint = pair.getFirst();
			final LocalDateTime movedFromDate = fixedCargoMatchedBlueprint.getWindowStart();
			final LocalDateTime movedToDate = pair.getSecond();
			final Inventory inventory = fixedCargoMatchedBlueprint.getMullContainer().getInventory();
			final YearMonth fromYm = YearMonth.from(movedFromDate);
			final YearMonth toYm = YearMonth.from(movedToDate);
			assert !fromYm.equals(toYm);
			final BaseLegalEntity entity = fixedCargoMatchedBlueprint.getMudContainer().getEntity();
			Map<YearMonth, Set<ICargoBlueprint>> entityCargoBlueprints = monthMappedEntityCargoBlueprints.get(inventory).get(entity);

			// Try to make a swap with a same entity cargo
			{
				// Variable naming in the context of a cargo to swap with the fixedCargoMatchedBlueprint
				// The cargoBlueprint selected for the swap will be released from toYm and accepted into fromYm, if one exists.
				final YearMonth releasingYearMonth = toYm;
				final YearMonth acceptingYearMonth = fromYm;
				final boolean performedSameEntitySwap = trySameEntitySwap(entityCargoBlueprints, releasingYearMonth, acceptingYearMonth, sameEntityComparatorWrapper, fixedCargoMatchedBlueprint,
						monthMappedCargoBlueprints, monthMappedEntityCargoBlueprints, monthMappedAllocationTrackerCargoBlueprints, inventoryToMonthlyCounts);
				if (performedSameEntitySwap) {
					continue;
				}
			}

			// If we are here, we could not find a same entity cargo blueprint
			final Map<BaseLegalEntity, Map<YearMonth, Long>> entityToMonthEndEntitlements = inventoryToEntityToMonthEndEntitlements.get(inventory);
			final HarmonisationPhaseCargoMatchingComparatorWrapper comparatorWrapper = new HarmonisationPhaseCargoMatchingComparatorWrapper(entityToMonthEndEntitlements, globalStates);

			if (fromYm.isBefore(toYm)) {
				// If we get here then we could not find a same entity cargo to switch cargoes with
				// Find entity that causes minimal violation on either side if brought forward

				// Switch cargoes on a case-by-case basis
				@Nullable
				ICargoBlueprint transferredCargoBlueprint = null;

				YearMonth releasingYm = toYm;
				@Nullable
				YearMonth acceptingYm = null;
				for (YearMonth currentYm = toYm.minusMonths(1L); !fromYm.isAfter(currentYm); currentYm = currentYm.minusMonths(1L)) {
					final Set<ICargoBlueprint> cbSet = monthMappedCargoBlueprints.get(inventory).get(currentYm);
					if (cbSet != null && !cbSet.isEmpty()) {
						acceptingYm = currentYm;
						break;
					}
				}

				if (acceptingYm == null) {
					throw new IllegalStateException("Accepting YM should not be null");
				}

				while (!fromYm.isAfter(releasingYm)) {
					final YearMonth finalReleasingYm = releasingYm;
					final ICargoBlueprint selectedCargoBlueprint;
					if (transferredCargoBlueprint == null) {
						final Comparator<ICargoBlueprint> comparator = comparatorWrapper.getComparator(acceptingYm, releasingYm, true);
						selectedCargoBlueprint = monthMappedAllocationTrackerCargoBlueprints.get(inventory).entrySet().stream() //
								.map(Entry::getValue) //
								.filter(currentYmMap -> {
									final Set<ICargoBlueprint> set = currentYmMap.get(finalReleasingYm);
									return set != null && !set.isEmpty();
								})//
								.flatMap(currentYmMap -> currentYmMap.get(finalReleasingYm).stream()) //
								.min(comparator) //
								.get();
					} else {
						final ICargoBlueprint finalTransferredCargoBlueprint = transferredCargoBlueprint;
						final Comparator<ICargoBlueprint> comparator = comparatorWrapper.getComparator(acceptingYm, releasingYm, true);
						final IAllocationTracker transferenceAllocationTracker = finalTransferredCargoBlueprint.getAllocationTracker();
						final Optional<ICargoBlueprint> optSelectedCargoBlueprint = monthMappedAllocationTrackerCargoBlueprints.get(inventory).entrySet().stream() //
								.filter(entry -> {
									final Set<ICargoBlueprint> set = entry.getValue().get(finalReleasingYm);
									return set != null && !set.isEmpty();
								}) //
								.flatMap(entry -> {
									final Stream<ICargoBlueprint> stream = entry.getValue().get(finalReleasingYm).stream();
									if (entry.getKey() == transferenceAllocationTracker) {
										return stream.filter(cb -> cb != finalTransferredCargoBlueprint);
									} else {
										return stream;
									}
								}) //
								.min(comparator);
						if (optSelectedCargoBlueprint.isPresent()) {
							selectedCargoBlueprint = optSelectedCargoBlueprint.get();
						} else {
							selectedCargoBlueprint = transferredCargoBlueprint;
						}
					}

					updateSelectedCargoTrackingObjects(selectedCargoBlueprint, releasingYm, acceptingYm, monthMappedCargoBlueprints, monthMappedEntityCargoBlueprints,
							monthMappedAllocationTrackerCargoBlueprints, inventoryToMonthlyCounts);

					// We've moved cargoes for a different entity and the current entity. Have to update their month end entitlements to accomodate this.
					// Using match cargoblueprint for volume - this might be a bad assumption
					final BaseLegalEntity selectedEntity = selectedCargoBlueprint.getMudContainer().getEntity();
					for (YearMonth ym = acceptingYm; ym.isBefore(finalReleasingYm); ym = ym.plusMonths(1L)) {
						final long newEntitlement = entityToMonthEndEntitlements.get(selectedEntity).get(ym).longValue() - selectedCargoBlueprint.getAllocatedVolume();
						entityToMonthEndEntitlements.get(selectedEntity).put(ym, newEntitlement);
						final long newEntitlementForFixedCargo = entityToMonthEndEntitlements.get(entity).get(ym).longValue() + fixedCargoMatchedBlueprint.getAllocatedVolume();
						entityToMonthEndEntitlements.get(entity).put(ym, newEntitlementForFixedCargo);
					}

					transferredCargoBlueprint = selectedCargoBlueprint;
					releasingYm = acceptingYm;
					acceptingYm = null;
					for (YearMonth currentYm = releasingYm.minusMonths(1L); !fromYm.isAfter(currentYm); currentYm = currentYm.minusMonths(1L)) {
						final Set<ICargoBlueprint> cbSet = monthMappedCargoBlueprints.get(inventory).get(currentYm);
						if (cbSet != null && !cbSet.isEmpty()) {
							acceptingYm = currentYm;
							break;
						}
					}
					if (acceptingYm == null) {
						break;
					}
				}
			} else {
				// If we get here then we could not find a same entity cargo to switch cargoes with
				// Find entity that causes minimal violation on either side if brought forward
				// Switch cargoes on a case-by-case basis
				@Nullable
				ICargoBlueprint transferredCargoBlueprint = null;

				YearMonth releasingYm = toYm;
				@Nullable
				YearMonth acceptingYm = null;

				for (YearMonth currentYm = toYm.plusMonths(1L); !fromYm.isBefore(currentYm); currentYm = currentYm.plusMonths(1L)) {
					final Set<ICargoBlueprint> cbSet = monthMappedCargoBlueprints.get(inventory).get(currentYm);
					if (cbSet != null && !cbSet.isEmpty()) {
						acceptingYm = currentYm;
						break;
					}
				}
				if (acceptingYm == null) {
					throw new IllegalStateException("Accepting YM should not be null");
				}
				while (!fromYm.isBefore(releasingYm)) {
					final YearMonth finalRelasingYm = releasingYm;
					final ICargoBlueprint selectedCargoBlueprint;
					if (transferredCargoBlueprint == null) {
						final Comparator<ICargoBlueprint> comparator = comparatorWrapper.getComparator(acceptingYm, releasingYm, false);
						selectedCargoBlueprint = monthMappedAllocationTrackerCargoBlueprints.get(inventory).entrySet().stream() //
								.map(Entry::getValue) //
								.filter(map -> {
									final Set<ICargoBlueprint> cbSet = map.get(finalRelasingYm);
									return cbSet != null && !cbSet.isEmpty();
								}) //
								.flatMap(map -> map.get(finalRelasingYm).stream()) //
								.min(comparator) //
								.get();
					} else {
						final ICargoBlueprint finalTransferredCargoBlueprint = transferredCargoBlueprint;
						final Comparator<ICargoBlueprint> comparator = comparatorWrapper.getComparator(acceptingYm, releasingYm, false);
						final IAllocationTracker transferenceAllocationTracker = transferredCargoBlueprint.getAllocationTracker();
						final Optional<ICargoBlueprint> optSelectedCargoBlueprint = monthMappedAllocationTrackerCargoBlueprints.get(inventory).entrySet().stream() //
								.filter(entry -> {
									final Set<ICargoBlueprint> set = entry.getValue().get(finalRelasingYm);
									return set != null && !set.isEmpty();
								}) //
								.flatMap(entry -> {
									final Stream<ICargoBlueprint> stream = entry.getValue().get(finalRelasingYm).stream();
									if (entry.getKey() == transferenceAllocationTracker) {
										return stream.filter(cb -> cb != finalTransferredCargoBlueprint);
									} else {
										return stream;
									}
								}) //
								.min(comparator);
						selectedCargoBlueprint = optSelectedCargoBlueprint.isPresent() ? optSelectedCargoBlueprint.get() : transferredCargoBlueprint;
					}

					updateSelectedCargoTrackingObjects(selectedCargoBlueprint, releasingYm, acceptingYm, monthMappedCargoBlueprints, monthMappedEntityCargoBlueprints,
							monthMappedAllocationTrackerCargoBlueprints, inventoryToMonthlyCounts);

					// We've moved cargoes for a different entity and the current entity have to update their reference entitlements to accomodate this.
					// Using matched cargoblueprint for volume - this might be a bad assumption
					final BaseLegalEntity selectedEntity = selectedCargoBlueprint.getMudContainer().getEntity();
					for (YearMonth ym = acceptingYm; ym.isAfter(finalRelasingYm); ym = ym.minusMonths(1L)) {
						final long newEntitlement = entityToMonthEndEntitlements.get(selectedEntity).get(ym).longValue() + selectedCargoBlueprint.getAllocatedVolume();
						entityToMonthEndEntitlements.get(selectedEntity).put(ym, newEntitlement);
						final long newEntitlementForFixedCargo = entityToMonthEndEntitlements.get(entity).get(ym).longValue() - fixedCargoMatchedBlueprint.getAllocatedVolume();
						entityToMonthEndEntitlements.get(entity).put(ym, newEntitlementForFixedCargo);
					}

					transferredCargoBlueprint = selectedCargoBlueprint;
					releasingYm = acceptingYm;
					acceptingYm = null;
					for (YearMonth currentYm = releasingYm.plusMonths(1L); !fromYm.isBefore(currentYm); currentYm = currentYm.plusMonths(1L)) {
						final Set<ICargoBlueprint> cbSet = monthMappedCargoBlueprints.get(inventory).get(currentYm);
						if (cbSet != null && !cbSet.isEmpty()) {
							acceptingYm = currentYm;
							break;
						}
					}
					if (acceptingYm == null) {
						break;
					}
				}
			}
		}
	}

	private static boolean trySameEntitySwap(@Nullable final Map<YearMonth, Set<ICargoBlueprint>> entityCargoBlueprints, final YearMonth releasingYm, final YearMonth acceptingYm,
			final HarmonisationPhaseSameEntityCargoMatchingComparatorWrapper sameEntityComparatorWrapper, final ICargoBlueprint fixedCargoMatchedBlueprint,
			final Map<Inventory, Map<YearMonth, Set<ICargoBlueprint>>> monthMappedCargoBlueprints,
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedEntityCargoBlueprints,
			final Map<Inventory, Map<IAllocationTracker, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedAllocationTrackerCargoBlueprints,
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts) {
		if (entityCargoBlueprints != null) {
			final Set<ICargoBlueprint> cbSet = entityCargoBlueprints.get(releasingYm);
			if (cbSet != null && !cbSet.isEmpty()) {
				// Find cargo that has a similar lifting (already checked emptiness of set)
				final Comparator<ICargoBlueprint> comparator = sameEntityComparatorWrapper.getComparator(fixedCargoMatchedBlueprint);
				final ICargoBlueprint selectedCargoBlueprint = cbSet.stream().min(comparator).get();
				// Moves the selected cargo into same month as cargo matched to fixed cargo. Just in case selectedCargoBlueprint is selected a second time
				selectedCargoBlueprint.setWindowStart(fixedCargoMatchedBlueprint.getWindowStart());
				// remove selected cargo blueprint from toYm since fixedCargoMatchedBlueprint is supposed to take its place
				updateSelectedCargoTrackingObjects(selectedCargoBlueprint, releasingYm, acceptingYm, monthMappedCargoBlueprints, monthMappedEntityCargoBlueprints,
						monthMappedAllocationTrackerCargoBlueprints, inventoryToMonthlyCounts);
				// We have done a one for for one swap on the same entity, assume that the changes in entitlement aren't that different - hence no updating of entitlements.
				// This might be a bad assumption

				// Made switch - entity swap was successful
				return true;
			}
		}
		// No switch - entity swap was unsuccessful
		return false;
	}

	private static void updateSelectedCargoTrackingObjects(final ICargoBlueprint selectedCargoBlueprint, final YearMonth releasingYm, final YearMonth acceptingYm,
			final Map<Inventory, Map<YearMonth, Set<ICargoBlueprint>>> monthMappedCargoBlueprints,
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedEntityCargoBlueprints,
			final Map<Inventory, Map<IAllocationTracker, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedAllocationTrackerCargoBlueprints,
			final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts) {
		final Inventory inventory = selectedCargoBlueprint.getMullContainer().getInventory();
		final BaseLegalEntity entity = selectedCargoBlueprint.getMudContainer().getEntity();
		// selected cargo leaves the releasingYm
		monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoBlueprint.getAllocationTracker()).get(releasingYm).remove(selectedCargoBlueprint);
		monthMappedCargoBlueprints.get(inventory).get(releasingYm).remove(selectedCargoBlueprint);
		monthMappedEntityCargoBlueprints.get(inventory).get(entity).get(releasingYm).remove(selectedCargoBlueprint);
		// selected cargo enters the acceptingYm
		monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoBlueprint.getAllocationTracker()).computeIfAbsent(acceptingYm, k -> new HashSet<>()).add(selectedCargoBlueprint);
		monthMappedCargoBlueprints.get(inventory).computeIfAbsent(acceptingYm, k -> new HashSet<>()).add(selectedCargoBlueprint);
		monthMappedEntityCargoBlueprints.get(inventory).get(entity).computeIfAbsent(acceptingYm, k -> new HashSet<>()).add(selectedCargoBlueprint);
		final IAllocationTracker allocationTracker = selectedCargoBlueprint.getAllocationTracker();
		final IMullDischargeWrapper dischargeWrapper = MullUtil.buildDischargeWrapper(allocationTracker);
		final Map<YearMonth, Map<IMullDischargeWrapper, Integer>> monthlyCounts = inventoryToMonthlyCounts.get(inventory).get(entity);
		final Map<IMullDischargeWrapper, Integer> releasingYmCounts = monthlyCounts.get(releasingYm);
		final Map<IMullDischargeWrapper, Integer> acceptingYmCounts = monthlyCounts.get(acceptingYm);
		final int oldReleasingMonthCount = releasingYmCounts.get(dischargeWrapper);
		final int oldAcceptingMonthCount = acceptingYmCounts.get(dischargeWrapper);
		// selectedCargoBlueprint moved from releasingYm to acceptingYm
		// decrement releasingMonthCount and increment acceptingMonthCount
		releasingYmCounts.put(dischargeWrapper, oldReleasingMonthCount - 1);
		acceptingYmCounts.put(dischargeWrapper, oldAcceptingMonthCount + 1);
	}

	private static void reduceUsedCargoBlueprints(final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts,
			final Set<ICargoBlueprint> usedCargoBlueprints) {
		usedCargoBlueprints.forEach(cargoBlueprint -> {
			final Inventory inventory = cargoBlueprint.getMullContainer().getInventory();
			final BaseLegalEntity entity = cargoBlueprint.getMudContainer().getEntity();
			final YearMonth ym = YearMonth.from(cargoBlueprint.getWindowStart());
			final IAllocationTracker allocationTracker = cargoBlueprint.getAllocationTracker();
			final IMullDischargeWrapper dischargeWrapper = MullUtil.buildDischargeWrapper(allocationTracker);
			final Map<IMullDischargeWrapper, Integer> counts = inventoryToMonthlyCounts.get(inventory).get(entity).get(ym);
			final int currentCount = counts.get(dischargeWrapper);
			assert currentCount > 0;
			counts.put(dischargeWrapper, currentCount - 1);
		});
	}

	private static Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> calculateMonthlyCounts(final List<InventoryLocalState> inventoryLocalStates,
			final GlobalStatesContainer globalStates) {
		final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts = new HashMap<>();
		// Initialise counts to zero - we might not have generated some so this saves on null checks later
		inventoryLocalStates.stream().forEach(inventoryLocalState -> {
			final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
			final Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>> entityToMonthlyCount = new HashMap<>();
			inventoryToMonthlyCounts.put(inventory, entityToMonthlyCount);
			inventoryLocalState.getMullContainer().getMudContainers().stream().forEach(mudContainer -> {
				final BaseLegalEntity entity = mudContainer.getEntity();
				final Map<YearMonth, Map<IMullDischargeWrapper, Integer>> monthlyCount = new HashMap<>();
				entityToMonthlyCount.put(entity, monthlyCount);
				final YearMonth adpEndExclusive = globalStates.getAdpGlobalState().getAdpEndExclusiveYearMonth();
				for (YearMonth ym = globalStates.getAdpGlobalState().getAdpStartYearMonth(); ym.isBefore(adpEndExclusive); ym = ym.plusMonths(1L)) {
					final Map<IMullDischargeWrapper, Integer> counts = new HashMap<>();
					monthlyCount.put(ym, counts);
					mudContainer.getAllocationTrackers().stream().map(MullUtil::buildDischargeWrapper).forEach(wrapper -> counts.put(wrapper, 0));
				}
			});
		});
		// Count cargoes generated in each month for each sales contract/des sales market
		inventoryLocalStates.stream().forEach(inventoryLocalState -> {
			final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
			final Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>> entityToMonthlyCounts = inventoryToMonthlyCounts.get(inventory);
			inventoryLocalState.getCargoBlueprintsToGenerate().stream().forEach(cargoBlueprint -> {
				final BaseLegalEntity entity = cargoBlueprint.getMudContainer().getEntity();
				final YearMonth ym = YearMonth.from(cargoBlueprint.getWindowStart());
				final IAllocationTracker allocationTracker = cargoBlueprint.getAllocationTracker();
				final IMullDischargeWrapper dischargeWrapper = MullUtil.buildDischargeWrapper(allocationTracker);
				entityToMonthlyCounts.get(entity).get(ym).computeIfPresent(dischargeWrapper, (wrapper, currentCount) -> currentCount + 1);
			});
		});
		return inventoryToMonthlyCounts;
	}

	private static void fillMonthMappingDetails(final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedEntityCargoBlueprints,
			final Map<Inventory, Map<YearMonth, Set<ICargoBlueprint>>> monthMappedCargoBlueprints,
			final Map<Inventory, Map<IAllocationTracker, Map<YearMonth, Set<ICargoBlueprint>>>> monthMappedAllocationTrackerCargoBlueprints, final List<InventoryLocalState> inventoryLocalStates,
			final Set<ICargoBlueprint> usedCargoBlueprints) {
		for (final InventoryLocalState inventoryLocalState : inventoryLocalStates) {
			final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
			monthMappedEntityCargoBlueprints.put(inventory, new HashMap<>());
			monthMappedCargoBlueprints.put(inventory, new HashMap<>());
			monthMappedAllocationTrackerCargoBlueprints.put(inventory, new HashMap<>());
			for (final ICargoBlueprint cargoBlueprint : inventoryLocalState.getCargoBlueprintsToGenerate()) {
				if (usedCargoBlueprints.contains(cargoBlueprint)) {
					continue;
				}
				final YearMonth ym = YearMonth.from(cargoBlueprint.getWindowStart());
				monthMappedEntityCargoBlueprints.get(inventory).computeIfAbsent(cargoBlueprint.getMudContainer().getEntity(), k -> new HashMap<>()).computeIfAbsent(ym, k -> new HashSet<>())
						.add(cargoBlueprint);
				monthMappedCargoBlueprints.get(inventory).computeIfAbsent(ym, k -> new HashSet<>()).add(cargoBlueprint);
				monthMappedAllocationTrackerCargoBlueprints.get(inventory).computeIfAbsent(cargoBlueprint.getAllocationTracker(), k -> new HashMap<>()).computeIfAbsent(ym, k -> new HashSet<>())
						.add(cargoBlueprint);
			}
		}
	}

	@Nullable
	private static FixedCargoMatch findAllocationTracker(final IMullContainer mullContainer, final MullCargoWrapper fixedCargo) {
		final DischargeSlot dischargeSlot = fixedCargo.getDischargeSlot();
		for (final IMudContainer mudContainer : mullContainer.getMudContainers()) {
			if (mudContainer.getEntity() == fixedCargo.getLoadSlot().getEntity()) {
				if (dischargeSlot instanceof final SpotDischargeSlot spotDischargeSlot) {
					if (spotDischargeSlot.getMarket() instanceof final DESSalesMarket desSalesMarket && mudContainer.getDesMarketTracker().getSalesMarket() == desSalesMarket) {
						return new FixedCargoMatch(fixedCargo, mullContainer, mudContainer, mudContainer.getDesMarketTracker());
					} else {
						return null;
					}
				} else if (dischargeSlot.getContract() != null) {
					final SalesContract expectedContract = dischargeSlot.getContract();
					final Optional<SalesContractTracker> optSct = mudContainer.getAllocationTrackers().stream() //
							.filter(SalesContractTracker.class::isInstance) //
							.map(SalesContractTracker.class::cast) //
							.filter(sct -> expectedContract == sct.getSalesContract()) //
							.findAny();
					if (optSct.isPresent()) {
						final SalesContractTracker salesContractTracker = optSct.get();
						return new FixedCargoMatch(fixedCargo, mullContainer, mudContainer, salesContractTracker);
					} else {
						return null;
					}
				} else if (dischargeSlot.isFOBSale()) {
					return new FixedCargoMatch(fixedCargo, mullContainer, mudContainer, mudContainer.getDesMarketTracker());
				}
			}
		}
		return null;
	}

	private static void matchCargoBlueprintsWithFixedCargoes(final FixedCargoMatch fixedCargoMatch, final Set<ICargoBlueprint> sameMonthShiftCargoBlueprints,
			final List<Pair<ICargoBlueprint, LocalDateTime>> differentMonthShiftCargoBlueprints, final Set<ICargoBlueprint> usedCargoBlueprints, final List<ICargoBlueprint> cargoBlueprints) {
		final LocalDateTime liftingDateTime = fixedCargoMatch.getLiftingTime();
		ICargoBlueprint previousCargoBlueprint = null;
		for (final ICargoBlueprint cargoBlueprint : cargoBlueprints) {
			if (usedCargoBlueprints.contains(cargoBlueprint) || cargoBlueprint.getAllocationTracker() != fixedCargoMatch.allocationTracker()
					|| cargoBlueprint.getMudContainer() != fixedCargoMatch.mudContainer()) {
				continue;
			}
			final LocalDateTime currentDateTime = cargoBlueprint.getWindowStart();
			if (currentDateTime.isBefore(liftingDateTime)) {
				previousCargoBlueprint = cargoBlueprint;
			} else if (currentDateTime.equals(liftingDateTime)) {
				sameMonthShiftCargoBlueprints.add(cargoBlueprint);
				usedCargoBlueprints.add(cargoBlueprint);
				return;
			} else if (currentDateTime.isAfter(liftingDateTime)) {
				if (previousCargoBlueprint == null) {
					final YearMonth liftingYm = YearMonth.from(liftingDateTime);
					final YearMonth cargoYm = YearMonth.from(currentDateTime);
					if (liftingYm.equals(cargoYm)) {
						sameMonthShiftCargoBlueprints.add(cargoBlueprint);
					} else {
						differentMonthShiftCargoBlueprints.add(Pair.of(cargoBlueprint, liftingDateTime));
					}
					usedCargoBlueprints.add(cargoBlueprint);
				} else {
					final int hoursDifferencePrevious = Hours.between(previousCargoBlueprint.getWindowStart(), liftingDateTime);
					final int hoursDifferenceLater = Hours.between(liftingDateTime, currentDateTime);
					final ICargoBlueprint selectedCargoBlueprint = hoursDifferencePrevious <= hoursDifferenceLater ? previousCargoBlueprint : cargoBlueprint;
					final YearMonth liftingYm = YearMonth.from(liftingDateTime);
					final YearMonth cargoYm = YearMonth.from(selectedCargoBlueprint.getWindowStart());
					if (liftingYm.equals(cargoYm)) {
						sameMonthShiftCargoBlueprints.add(selectedCargoBlueprint);
					} else {
						differentMonthShiftCargoBlueprints.add(Pair.of(selectedCargoBlueprint, liftingDateTime));
					}
					usedCargoBlueprints.add(selectedCargoBlueprint);
				}
				return;
			}
		}
		if (previousCargoBlueprint != null) {
			final YearMonth liftingYm = YearMonth.from(liftingDateTime);
			final YearMonth cargoYm = YearMonth.from(previousCargoBlueprint.getWindowStart());
			if (liftingYm.equals(cargoYm)) {
				sameMonthShiftCargoBlueprints.add(previousCargoBlueprint);
			} else {
				differentMonthShiftCargoBlueprints.add(Pair.of(previousCargoBlueprint, liftingDateTime));
			}
			usedCargoBlueprints.add(previousCargoBlueprint);
		}
	}
}
