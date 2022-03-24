package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.adp.mull.container.DesMarketTracker;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

@NonNullByDefault
public class HarmonisationMullAlgorithm extends MullAlgorithm {

	private Map<Inventory, SortedMap<LocalDateTime, Cargo>> fixedCargoes = new HashMap<>();
	private Map<Inventory, Iterator<Entry<LocalDateTime, Cargo>>> inventoryToExistingCargoesIter = new HashMap<>();
	private Map<Inventory, Entry<LocalDateTime, Cargo>> inventoryToNextFixedCargo = new HashMap<>();

	private Map<Vessel, Iterator<LocalDateTime>> vesselToForwardUseIterators = new HashMap<>();

	private Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Pair<IMudContainer, IAllocationTracker>>> inventoryToRemappingTrackers = new HashMap<>();
	private final HarmonisationMap harmonisationMap;

	public HarmonisationMullAlgorithm(final GlobalStatesContainer globalStatesContainer, final AlgorithmState algorithmState, final List<InventoryLocalState> inventoryLocalStates,
			final HarmonisationMap harmonisationMap) {
		super(globalStatesContainer, algorithmState, inventoryLocalStates);
		this.harmonisationMap = harmonisationMap;
	}

	public Map<Inventory, SortedMap<LocalDateTime, Cargo>> getFixedCargoes() {
		return fixedCargoes;
	}

	public HarmonisationMap getHarmonisationMap() {
		return harmonisationMap;
	}

	private void initaliseFixedCargoes() {
		final Map<Port, Inventory> portToInventoryMap = globalStatesContainer.getInventoryGlobalStates().values().stream()
				.collect(Collectors.toMap(InventoryGlobalState::getPort, InventoryGlobalState::getInventory));
		globalStatesContainer.getInventoryGlobalStates().keySet().forEach(inventory -> fixedCargoes.put(inventory, new TreeMap<>()));
		globalStatesContainer.getMullGlobalState().getCargoesToKeep().stream() //
				.filter(cargoWrapper -> portToInventoryMap.containsKey(cargoWrapper.getLoadSlot().getPort())) //
				.forEach(cargoWrapper -> {
					final LoadSlot loadSlot = cargoWrapper.getLoadSlot();
					final Inventory inventory = portToInventoryMap.get(loadSlot.getPort());
					final LocalDateTime loadStart = loadSlot.isSetWindowStartTime() ? loadSlot.getWindowStart().atTime(LocalTime.of(loadSlot.getWindowStartTime(), 0))
							: loadSlot.getWindowStart().atStartOfDay();
					fixedCargoes.get(inventory).put(loadStart, loadSlot.getCargo());
				});
		fixedCargoes.entrySet().forEach(entry -> {
			final Inventory inventory = entry.getKey();
			final Iterator<Entry<LocalDateTime, Cargo>> fixedCargoesIter = entry.getValue().entrySet().iterator();
			inventoryToExistingCargoesIter.put(inventory, fixedCargoesIter);
			if (fixedCargoesIter.hasNext()) {
				final Entry<LocalDateTime, Cargo> nextFixedCargo = fixedCargoesIter.next();
				inventoryToNextFixedCargo.put(inventory, nextFixedCargo);
			}
		});
		final List<MullCargoWrapper> sortedCargoesToKeep = globalStatesContainer.getMullGlobalState().getCargoesToKeep().stream() //
				.sorted((m1, m2) -> {
					final LoadSlot loadSlot1 = m1.getLoadSlot();
					final LoadSlot loadSlot2 = m2.getLoadSlot();
					final LocalDate ld1 = loadSlot1.getWindowStart();
					final LocalDate ld2 = loadSlot2.getWindowStart();
					final LocalDateTime ldt1 = loadSlot1.isSetWindowSize() ? ld1.atTime(loadSlot1.getWindowStartTime(), 0) : ld1.atStartOfDay();
					final LocalDateTime ldt2 = loadSlot2.isSetWindowSize() ? ld2.atTime(loadSlot2.getWindowStartTime(), 0) : ld2.atStartOfDay();
					return ldt1.compareTo(ldt2);
				}).toList();
		final Map<Vessel, List<LocalDateTime>> vesselToForwardUseTime = new HashMap<>();
		sortedCargoesToKeep.forEach(cargoWrapper -> {
			final LocalDate localDate = cargoWrapper.getLoadSlot().getWindowStart();
			final LocalDateTime localDateTime = cargoWrapper.getLoadSlot().isSetWindowSize() ? localDate.atTime(cargoWrapper.getLoadSlot().getWindowSize(), 0) : localDate.atStartOfDay();
			final DischargeSlot dischargeSlot = cargoWrapper.getDischargeSlot();
			if (dischargeSlot instanceof final SpotDischargeSlot spotDischargeSlot) {
				final SpotMarket spotMarket = spotDischargeSlot.getMarket();
				if (spotMarket instanceof DESSalesMarket) {
					final VesselAssignmentType vat = dischargeSlot.getCargo().getVesselAssignmentType();
					if (vat instanceof final VesselAvailability va) {
						vesselToForwardUseTime.computeIfAbsent(va.getVessel(), k -> new LinkedList<>()).add(localDateTime);
					}
				} else {
					if (dischargeSlot.isFOBSale() && dischargeSlot.getNominatedVessel() != null) {
						vesselToForwardUseTime.computeIfAbsent(dischargeSlot.getNominatedVessel(), k -> new LinkedList<>()).add(localDateTime);
					} else {
						final VesselAssignmentType vat = dischargeSlot.getCargo().getVesselAssignmentType();
						if (vat instanceof final VesselAvailability va) {
							vesselToForwardUseTime.computeIfAbsent(va.getVessel(), k -> new LinkedList<>()).add(localDateTime);
						}
					}
				}
			}
		});
		for (final Entry<Vessel, List<LocalDateTime>> entry : vesselToForwardUseTime.entrySet()) {
			assert !entry.getValue().isEmpty();
			final Iterator<LocalDateTime> currentUseTimeIter = entry.getValue().iterator();
			vesselToForwardUseIterators.put(entry.getKey(), currentUseTimeIter);
			algorithmState.getVesselUsageLookAhead().put(entry.getKey(), currentUseTimeIter.next());
		}
	}

	@Override
	protected void runPostMonthStartTasks(final LocalDateTime currentDateTime, final IMullContainer mullContainer) {
		final YearMonth currentYm = YearMonth.from(currentDateTime);
		mullContainer.updateCurrentMonthAllocations(currentYm);
	}

	@Override
	protected void runFixedCargoHandlingTasks(final LocalDateTime currentDateTime, final InventoryLocalState inventoryLocalState) {
		final Inventory inventory = inventoryLocalState.getMullContainer().getInventory();
		final Entry<LocalDateTime, Cargo> nextFixedCargo = inventoryToNextFixedCargo.get(inventory);
		if (nextFixedCargo != null && nextFixedCargo.getKey().equals(currentDateTime)) {
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Pair<IMudContainer, IAllocationTracker>> remappingTrackers = inventoryToRemappingTrackers.get(inventory);
			final LinkedList<ICargoBlueprint> cargoBlueprintsToGenerate = inventoryLocalState.getCargoBlueprintsToGenerate();
			final List<ICargoBlueprint> cargoBlueprintsToUndo = inventoryLocalState.getRollingLoadWindow().startFixedLoad(nextFixedCargo.getValue(), cargoBlueprintsToGenerate);
			final LoadSlot loadSlot = (LoadSlot) nextFixedCargo.getValue().getSlots().get(0);
			final DischargeSlot dischargeSlot = (DischargeSlot) nextFixedCargo.getValue().getSlots().get(1);
			@Nullable
			Vessel fixedCargoAssignedVessel = null;

			if (dischargeSlot instanceof final SpotDischargeSlot spotDischargeSlot) {
				final SpotMarket spotMarket = spotDischargeSlot.getMarket();
				if (spotMarket instanceof final DESSalesMarket desSalesMarket) {
					final Pair<BaseLegalEntity, IMullDischargeWrapper> pair = Pair.of(loadSlot.getEntity(), new MullDesSalesMarketWrapper(desSalesMarket));
					final Pair<IMudContainer, IAllocationTracker> remappedPair = remappingTrackers.get(pair);
					if (remappedPair != null) {
						final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
						assert inventoryLocalState.getMullContainer().getMudContainers().contains(remappedPair.getFirst());
						remappedPair.getFirst().dropFixedLoad(expectedVolumeLoaded);
						remappedPair.getSecond().dropFixedLoad(expectedVolumeLoaded);
						final VesselAssignmentType vat = nextFixedCargo.getValue().getVesselAssignmentType();
						if (vat instanceof final VesselAvailability va) {
							final Vessel vess = va.getVessel();
							if (remappedPair.getSecond().getVessels().contains(vess)) {
								fixedCargoAssignedVessel = vess;
							}
						}
					}
				}
			} else {
				if (dischargeSlot.isFOBSale()) {
					Optional<IMudContainer> optMudContainer = inventoryLocalState.getMullContainer().getMudContainers().stream().filter(m -> m.getEntity() == loadSlot.getEntity()).findAny();
					if (optMudContainer.isPresent()) {
						final IMudContainer mudContainer = optMudContainer.get();
						final DesMarketTracker desMarketTracker = mudContainer.getDesMarketTracker();
						final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
						mudContainer.dropFixedLoad(expectedVolumeLoaded);
						desMarketTracker.dropFixedLoad(expectedVolumeLoaded);
						@Nullable
						final Vessel vess = dischargeSlot.getNominatedVessel();
						if (vess != null && desMarketTracker.getVessels().contains(vess)) {
							fixedCargoAssignedVessel = vess;
						}
					}
				} else {
					@Nullable
					final SalesContract salesContract = dischargeSlot.getContract();
					if (salesContract != null) {
						final Pair<BaseLegalEntity, IMullDischargeWrapper> pair = Pair.of(loadSlot.getEntity(), new MullSalesContractWrapper(salesContract));
						@Nullable
						final Pair<IMudContainer, IAllocationTracker> remappedPair = remappingTrackers.get(pair);
						if (remappedPair != null) {
							final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
							assert inventoryLocalState.getMullContainer().getMudContainers().contains(remappedPair.getFirst());
							remappedPair.getFirst().dropFixedLoad(expectedVolumeLoaded);
							remappedPair.getSecond().dropFixedLoad(expectedVolumeLoaded);
							final VesselAssignmentType vat = nextFixedCargo.getValue().getVesselAssignmentType();
							if (vat instanceof final VesselAvailability va) {
								final Vessel vess = va.getVessel();
								if (remappedPair.getSecond().getVessels().contains(vess)) {
									fixedCargoAssignedVessel = vess;
								}
							}
						}
					}
				}
			}
			final Set<Vessel> vesselsToUndo = new HashSet<>();
			for (final ICargoBlueprint cargoBlueprint : cargoBlueprintsToUndo) {
				inventoryLocalState.getMullContainer().undo(cargoBlueprint);
				@Nullable
				final Vessel undoneVessel = cargoBlueprint.getVessel();
				if (undoneVessel != null) {
					vesselsToUndo.add(undoneVessel);
				}
			}
			final Iterator<ICargoBlueprint> reverseCargoBlueprints = cargoBlueprintsToGenerate.descendingIterator();
			while (!vesselsToUndo.isEmpty()) {
				if (!reverseCargoBlueprints.hasNext()) {
					break;
				}
				final ICargoBlueprint cargoBlueprint = reverseCargoBlueprints.next();
				final Vessel assignedVessel = cargoBlueprint.getVessel();
				if (assignedVessel != null && vesselsToUndo.contains(assignedVessel)) {
					algorithmState.getVesselUsageLookBehind().put(assignedVessel, cargoBlueprint.getWindowStart());
					vesselsToUndo.remove(assignedVessel);
				}
			}
			if (cargoBlueprintsToGenerate.isEmpty()) {
				inventoryLocalState.setInventorySlotCount(0);
			} else {
				inventoryLocalState.setInventorySlotCount(cargoBlueprintsToGenerate.getLast().getLoadCounter() + 1);
			}
			for (final Vessel vesselToUndo : vesselsToUndo) {
				algorithmState.getVesselUsageLookBehind().put(vesselToUndo, globalStatesContainer.getAdpGlobalState().getDateTimeBeforeAdpStart());
			}
			if (fixedCargoAssignedVessel != null) {
				algorithmState.getVesselUsageLookBehind().put(fixedCargoAssignedVessel, currentDateTime);
				final Iterator<LocalDateTime> iter = vesselToForwardUseIterators.get(fixedCargoAssignedVessel);
				if (iter.hasNext()) {
					algorithmState.getVesselUsageLookAhead().put(fixedCargoAssignedVessel, iter.next());
				} else {
					algorithmState.getVesselUsageLookAhead().remove(fixedCargoAssignedVessel);
				}
			}
			final Iterator<Entry<LocalDateTime, Cargo>> nextFixedCargoesIter = inventoryToExistingCargoesIter.get(inventory);
			if (nextFixedCargoesIter.hasNext()) {
				inventoryToNextFixedCargo.put(inventory, nextFixedCargoesIter.next());
			} else {
				inventoryToNextFixedCargo.remove(inventory);
			}
		}
	}

	@Override
	protected Vessel calculateAssignedVessel(final LocalDateTime currentDateTime, final List<Vessel> vessels) {
		return vessels.stream() //
				.max((v1, v2) -> {
					// Get minimum difference in hours
					final int hoursDiff1 = calculateMinimumHoursUsageDifference(currentDateTime, v1);
					final int hoursDiff2 = calculateMinimumHoursUsageDifference(currentDateTime, v2);
					// Compare hour differences
					// largest is vessel farthest from being used
					return Integer.compare(hoursDiff1, hoursDiff2);
				}) //
				.get();
	}

	private int calculateMinimumHoursUsageDifference(final LocalDateTime currentDateTime, final Vessel vessel) {
		@Nullable
		final LocalDateTime lookahead = algorithmState.getVesselUsageLookAhead().get(vessel);
		final LocalDateTime lookbehind = algorithmState.getVesselUsageLookBehind().get(vessel);
		if (lookahead != null) {
			return Math.min(Hours.between(lookbehind, currentDateTime), Hours.between(currentDateTime, lookahead));
		} else {
			return Hours.between(lookbehind, currentDateTime);
		}
	}

}
