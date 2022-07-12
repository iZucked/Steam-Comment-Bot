/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.AllowedArrivalTimeRecord;
import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.AdpGlobalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.AnyTimeSpecifier;
import com.mmxlabs.models.lng.adp.mull.algorithm.DateTimeConstrainedLiftTimeSpecifier;
import com.mmxlabs.models.lng.adp.mull.algorithm.SingleTimeSetLiftTimeSpecifier;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.ILiftTimeSpecifier;
import com.mmxlabs.models.lng.adp.mull.algorithm.IMullAlgorithm;
import com.mmxlabs.models.lng.adp.mull.algorithm.IMullDischargeWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.IRollingWindow;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryGlobalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryLocalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullDesSalesMarketWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullGlobalState;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullSalesContractWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.RollingLoadWindow;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.FromProfileBuildingStrategy;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.IMullContainerBuildingStrategy;
import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.IMullStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.container.DesMarketTracker;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;
import com.mmxlabs.models.lng.adp.mull.container.SalesContractTracker;
import com.mmxlabs.models.lng.adp.mull.profile.DesSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.IAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.IEntityRow;
import com.mmxlabs.models.lng.adp.mull.profile.IMullProfile;
import com.mmxlabs.models.lng.adp.mull.profile.IMullSubprofile;
import com.mmxlabs.models.lng.adp.mull.profile.MullProfile;
import com.mmxlabs.models.lng.adp.mull.profile.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.presentation.views.ADPEditorData;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ui.commands.ScheduleModelInvalidateCommandProvider;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public class MullUtil {
	private MullUtil() {
	}

	public static boolean isAtStartHourOfMonth(final LocalDateTime localDateTime) {
		return localDateTime.getDayOfMonth() == 1 && localDateTime.getHour() == 0;
	}

	public static boolean isAtEndHourOfMonth(final LocalDateTime localDateTime) {
		final YearMonth ym = YearMonth.from(localDateTime);
		final LocalDate lastDateOfMonth = ym.atEndOfMonth();
		return localDateTime.getDayOfMonth() == lastDateOfMonth.getDayOfMonth() && localDateTime.getHour() == 23;
	}

	public static int calculateVolumeLiftedBarSafetyHeel(final Vessel vessel, final int loadDuration) {
		final int expectedBoiloff = (int) (loadDuration * (vessel.getLadenAttributes().getVesselOrDelegateInPortNBORate() / 24.0));
		return expectedBoiloff + (int) (vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity() - vessel.getVesselOrDelegateSafetyHeel());
	}

	public static IMullDischargeWrapper buildDischargeWrapper(final IAllocationRow allocationRow) {
		if (allocationRow instanceof final SalesContractAllocationRow salesContractAllocationRow) {
			return new MullSalesContractWrapper(salesContractAllocationRow.getSalesContract());
		} else if (allocationRow instanceof final DesSalesMarketAllocationRow desSalesMarketAllocationRow) {
			return new MullDesSalesMarketWrapper(desSalesMarketAllocationRow.getDesSalesMarket());
		} else {
			throw new IllegalStateException("Unexpected allocation row type");
		}
	}

	public static IMullDischargeWrapper buildDischargeWrapper(final IAllocationTracker allocationTracker) {
		if (allocationTracker instanceof final SalesContractTracker salesContractTracker) {
			return new MullSalesContractWrapper(salesContractTracker.getSalesContract());
		} else if (allocationTracker instanceof final DesMarketTracker desMarketTracker) {
			return new MullDesSalesMarketWrapper(desMarketTracker.getSalesMarket());
		} else {
			throw new IllegalStateException("Unexpected allocation tracker type");
		}
	}

	public static AdpGlobalState buildAdpGlobalState(final ADPModel adpModel) {
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

	public static InventoryGlobalState buildInventoryGlobalState(final Inventory inventory, final List<AllowedArrivalTimeRecord> allowedArrivalTimes, final AdpGlobalState adpGlobalState,
			final LNGScenarioModel sm) {
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
		final ILiftTimeSpecifier liftTimeSpecifier = createLiftTimeSpecifier(allowedArrivalTimes, adpGlobalState);
		return new InventoryGlobalState(inventory, inventoryPurchaseContract, adpGlobalState.getStartDateTime(), adpGlobalState.getEndDateTimeExclusive(), liftTimeSpecifier);
	}

	private static ILiftTimeSpecifier createLiftTimeSpecifier(final List<AllowedArrivalTimeRecord> allowedArrivalTimes, final AdpGlobalState adpGlobalState) {
		if (allowedArrivalTimes.isEmpty()) {
			return new AnyTimeSpecifier();
		} else if (allowedArrivalTimes.size() == 1) {
			final AllowedArrivalTimeRecord allowedArrivalTimeRecord = allowedArrivalTimes.get(0);

			if (!allowedArrivalTimeRecord.getPeriodStart().isAfter(adpGlobalState.getStartDate())) {
				final List<Integer> allowedTimes = allowedArrivalTimeRecord.getAllowedTimes();
				if (allowedTimes.size() == 24) {
					return new AnyTimeSpecifier();
				} else {
					return new SingleTimeSetLiftTimeSpecifier(allowedTimes);
				}
			}
		}
		return new DateTimeConstrainedLiftTimeSpecifier(allowedArrivalTimes);
	}

	public static MullGlobalState buildMullGlobalState(final com.mmxlabs.models.lng.adp.MullProfile mullProfile, final LNGScenarioModel sm, final Predicate<BaseLegalEntity> isFirstPartyEntity) {
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

		final Set<BaseLegalEntity> firstPartyEntities = ScenarioModelUtil.getCommercialModel(sm).getEntities().stream() //
				.filter(isFirstPartyEntity) //
				.collect(Collectors.toSet());
		return new MullGlobalState(loadWindowInDays, volumeFlex, fullCargoLotValue, cargoVolume, cargoesToKeep, firstPartyEntities);
	}

	public static GlobalStatesContainer buildDefaultGlobalStates(final com.mmxlabs.models.lng.adp.MullProfile mullProfile, final ADPModel adpModel, final LNGScenarioModel sm, final Predicate<BaseLegalEntity> isFirstPartyEntity) {
		final AdpGlobalState adpGlobalState = MullUtil.buildAdpGlobalState(adpModel);
		final List<InventoryGlobalState> inventoryGlobalStates = new ArrayList<>(mullProfile.getInventories().size());
		for (final com.mmxlabs.models.lng.adp.MullSubprofile subprofile : mullProfile.getInventories()) {
			final Inventory inventory = subprofile.getInventory();
			if (inventory == null) {
				throw new IllegalStateException("Mull subprofile must be associated with an inventory");
			}
			inventoryGlobalStates.add(MullUtil.buildInventoryGlobalState(inventory, subprofile.getAllowedArrivalTimes(), adpGlobalState, sm));
		}
		final MullGlobalState mullGlobalState = MullUtil.buildMullGlobalState(mullProfile, sm, isFirstPartyEntity);
		return new GlobalStatesContainer(adpGlobalState, inventoryGlobalStates, mullGlobalState);
	}

	public static IMullProfile createDefaultInternalMullProfile(final com.mmxlabs.models.lng.adp.MullProfile eMullProfile) {
		return new MullProfile(eMullProfile);
	}

	public static AlgorithmState createDefaultAlgorithmState(final IMullProfile mullProfile, final GlobalStatesContainer globalStatesContainer) {
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

	public static List<InventoryLocalState> createDefaultFromProfileInventoryLocalStates(final IMullProfile mullProfile, final GlobalStatesContainer globalStatesContainer,
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

	public static Command createModelPopulationCommands(final GlobalStatesContainer globalStates, final IMullAlgorithm finalAlgorithm, final ADPEditorData editorData) {

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
		final Map<Vessel, VesselCharter> vesselToVa = ScenarioModelUtil.getCargoModel(sdp).getVesselCharters().stream() //
				.collect(Collectors.toMap(VesselCharter::getVessel, Function.identity()));

		final List<Command> commands = new ArrayList<>();
		for (final InventoryLocalState inventoryLocalState : finalAlgorithm.getInventoryLocalStates()) {
			int i = 0;
			for (final ICargoBlueprint cargoBlueprint : inventoryLocalState.getCargoBlueprintsToGenerate()) {
				final LoadSlot loadSlot = createLoadSlot(cargoBlueprint, cec, commands, ScenarioModelUtil.getCargoModel(sdp), globalStates, i);
				final DischargeSlot dischargeSlot = createDischargeSlot(cargoBlueprint, cec, commands, globalStates, i, loadSlot, sdp, vesselToVa);
				final Cargo cargo = CargoEditingCommands.createNewCargo(editingDomain, commands, ScenarioModelUtil.getCargoModel(sdp), null, 0);
				loadSlot.setCargo(cargo);
				dischargeSlot.setCargo(cargo);
				final boolean isFirstParty = globalStates.getMullGlobalState().getFirstPartyEntities().contains(cargoBlueprint.getMudContainer().getEntity());
				cargo.setAllowRewiring(isFirstParty);
				final Vessel vessel = cargoBlueprint.getVessel();
				if (!dischargeSlot.isFOBSale() && vessel != null) {
					final VesselCharter va = vesselToVa.get(vessel);
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

	private static DischargeSlot createDischargeSlot(final ICargoBlueprint cargoBlueprint, final CargoEditingCommands cec, final List<Command> commands, final GlobalStatesContainer globalStates,
			final int loadIndex, final LoadSlot loadSlot, IScenarioDataProvider sdp, final Map<Vessel, @Nullable VesselCharter> vesselToVa) {
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
}
