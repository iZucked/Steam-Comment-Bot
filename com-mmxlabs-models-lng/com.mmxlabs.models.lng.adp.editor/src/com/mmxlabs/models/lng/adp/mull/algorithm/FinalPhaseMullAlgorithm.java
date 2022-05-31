/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.MullUtil;
import com.mmxlabs.models.lng.adp.mull.container.CargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.DesMarketTracker;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;
import com.mmxlabs.models.lng.adp.mull.harmonisation.HarmonisationTransformationState;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public class FinalPhaseMullAlgorithm extends MullAlgorithm {

	final HarmonisationMullAlgorithm harmonisationMullAlgorithm;
	final Map<Inventory, Iterator<ICargoBlueprint>> previousCargoBlueprintIters = new HashMap<>();
	final Map<Inventory, ICargoBlueprint[]> cargoBlueprints = new HashMap<>();
	final Map<Inventory, Integer> cargoBlueprintIndices = new HashMap<>();
	final Map<Inventory, Integer> previousCargoBlueprintIndexStart = new HashMap<>();
	final Map<Inventory, ICargoBlueprint> nextPreviousCargoBlueprint = new HashMap<>();
	final Map<Inventory, InventoryLocalState> inventoryLocalStateMap = new HashMap<>();
	final Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Pair<IMudContainer, IAllocationTracker>>> inventoryToGeneralKeyToLocalTrackingMap = new HashMap<>();

	final Map<Inventory, Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, List<Pair<IMudContainer, IAllocationTracker>>>> rearrangedProfileToLocalTrackingMap = new HashMap<>();
	final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> inventoryToMonthlyCounts;

	final Map<Inventory, Map<YearMonth, List<Cargo>>> monthMappedExistingCargoes;

	public FinalPhaseMullAlgorithm(GlobalStatesContainer globalStatesContainer, AlgorithmState algorithmState, List<InventoryLocalState> inventoryLocalStates,
			final HarmonisationMullAlgorithm previousMullAlgorithm, final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>>> originalInventoryToMonthlyCounts,
			final Map<Inventory, Map<YearMonth, List<Cargo>>> monthMappedExistingCargoes) {
		super(globalStatesContainer, algorithmState, inventoryLocalStates, true);
		this.harmonisationMullAlgorithm = previousMullAlgorithm;
		this.inventoryLocalStates.forEach(ils -> inventoryLocalStateMap.put(ils.getInventoryGlobalState().getInventory(), ils));
		for (final InventoryLocalState inventoryLocalState : harmonisationMullAlgorithm.getInventoryLocalStates()) {
			final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
			cargoBlueprints.put(inventory, new ICargoBlueprint[inventoryLocalState.getCargoBlueprintsToGenerate().size()]);
			cargoBlueprintIndices.put(inventory, 0);
			previousCargoBlueprintIndexStart.put(inventory, 0);
			final Iterator<ICargoBlueprint> cargoBlueprintIter = inventoryLocalState.getCargoBlueprintsToGenerate().iterator();
			if (cargoBlueprintIter.hasNext()) {
				nextPreviousCargoBlueprint.put(inventory, cargoBlueprintIter.next());
			}
			previousCargoBlueprintIters.put(inventory, cargoBlueprintIter);
		}
		for (final InventoryLocalState inventoryLocalState : inventoryLocalStates) {
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Pair<IMudContainer, IAllocationTracker>> generalKeyToLocalTracking = new HashMap<>();
			inventoryToGeneralKeyToLocalTrackingMap.put(inventoryLocalState.getInventoryGlobalState().getInventory(), generalKeyToLocalTracking);
			for (final IMudContainer mudContainer : inventoryLocalState.getMullContainer().getMudContainers()) {
				final BaseLegalEntity entity = mudContainer.getEntity();
				for (final IAllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
					final IMullDischargeWrapper dischargeWrapper = MullUtil.buildDischargeWrapper(allocationTracker);
					final Pair<BaseLegalEntity, IMullDischargeWrapper> keyPair = Pair.of(entity, dischargeWrapper);
					final Pair<IMudContainer, IAllocationTracker> valuePair = Pair.of(mudContainer, allocationTracker);
					generalKeyToLocalTracking.put(keyPair, valuePair);
				}
			}
		}
		for (final Entry<Inventory, HarmonisationTransformationState> harmonisationMapEntry : harmonisationMullAlgorithm.getHarmonisationMap().getHarmonisationTransformationStates().entrySet()) {
			final Inventory inventory = harmonisationMapEntry.getKey();
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Pair<IMudContainer, IAllocationTracker>> localTrackers = inventoryToGeneralKeyToLocalTrackingMap.get(inventory);
			final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, List<Pair<IMudContainer, IAllocationTracker>>> rearrangementToLocalTrackers = new HashMap<>();
			rearrangedProfileToLocalTrackingMap.put(inventory, rearrangementToLocalTrackers);
			final Map<BaseLegalEntity, Pair<List<IMullDischargeWrapper>, Map<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>>>> thisInventoryRearrangement = harmonisationMapEntry
					.getValue().getRearrangedProfile();
			thisInventoryRearrangement.entrySet().forEach(harmonisingEntityEntry -> {
				final BaseLegalEntity harmonisationEntity = harmonisingEntityEntry.getKey();
				harmonisingEntityEntry.getValue().getFirst().forEach(directMappingDischargeWrapper -> {
					final Pair<BaseLegalEntity, IMullDischargeWrapper> keyPair = Pair.of(harmonisationEntity, directMappingDischargeWrapper);
					final List<Pair<IMudContainer, IAllocationTracker>> valuePairs = Collections.singletonList(localTrackers.get(keyPair));
					rearrangementToLocalTrackers.put(keyPair, valuePairs);
				});
				harmonisingEntityEntry.getValue().getSecond().entrySet().forEach(harmonisingMappingEntry -> {
					final IMullDischargeWrapper harmonisingDischargeWrapper = harmonisingMappingEntry.getKey();
					final Pair<BaseLegalEntity, IMullDischargeWrapper> keyPair = Pair.of(harmonisationEntity, harmonisingDischargeWrapper);
					// Add key pair to value pairs
					final Stream<Pair<BaseLegalEntity, IMullDischargeWrapper>> concatenatedStream = Stream.concat(Collections.singletonList(keyPair).stream(),
							harmonisingMappingEntry.getValue().stream());

					final List<Pair<IMudContainer, IAllocationTracker>> valuePairs = concatenatedStream.map(localTrackers::get).toList();
					rearrangementToLocalTrackers.put(keyPair, valuePairs);
				});
			});
		}
		inventoryToMonthlyCounts = originalInventoryToMonthlyCounts.entrySet().stream().collect(Collectors.toMap(Entry::getKey, inventoryKeyEntry -> {
			return inventoryKeyEntry.getValue().entrySet().stream().collect(Collectors.toMap(Entry::getKey, entityKeyEntry -> {
				return entityKeyEntry.getValue().entrySet().stream().collect(Collectors.toMap(Entry::getKey, yearMonthKeyEntry -> {
					return yearMonthKeyEntry.getValue().entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
				}));
			}));
		}));
		this.monthMappedExistingCargoes = monthMappedExistingCargoes;
	}

	@Override
	public void run() {
		if (!inventoryLocalStates.stream().map(InventoryLocalState::getRollingLoadWindow).map(IRollingWindow::getStartDateTime)
				.allMatch(globalStatesContainer.getAdpGlobalState().getStartDateTime()::equals)) {
			throw new IllegalStateException("All inventories should start on ADP start date");
		}
		final YearMonth adpYearStart = globalStatesContainer.getAdpGlobalState().getAdpStartYearMonth();
		final YearMonth adpYearEndExc = globalStatesContainer.getAdpGlobalState().getAdpEndExclusiveYearMonth();

		final Map<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> inventoryToEventIterators = buildInventoryToEventIterators();

		for (YearMonth currentYm = adpYearStart; currentYm.isBefore(adpYearEndExc); currentYm = currentYm.plusMonths(1L)) {
			final Map<Inventory, List<Entry<LocalDateTime, InventoryDateTimeEvent>>> persistedInventoryEvents = new HashMap<>();
			final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints = new HashMap<>();
			final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices = new HashMap<>();
			runLocalMonthInitialisation(currentYm, persistedInventoryEvents, inventoryToEventIterators, persistedPreviousCargoBlueprints, persistedPreviousCargoBlueprintIndices);
			runLocalMonthCargoBlueprintAssignment(currentYm, persistedInventoryEvents, persistedPreviousCargoBlueprints, persistedPreviousCargoBlueprintIndices);
		}
		for (final ICargoBlueprint[] cargoBlueprintsArray : cargoBlueprints.values()) {
			for (final ICargoBlueprint cargoBlueprint : cargoBlueprintsArray) {
				assert cargoBlueprint != null;
			}
		}
		for (final InventoryLocalState inventoryLocalState : inventoryLocalStates) {
			final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
			final ICargoBlueprint[] cargoBlueprintsArray = cargoBlueprints.get(inventory);
			for (ICargoBlueprint cargoBlueprint : cargoBlueprintsArray) {
				inventoryLocalState.getCargoBlueprintsToGenerate().add(cargoBlueprint);
			}
		}
	}

	private void runLocalMonthCargoBlueprintAssignment(final YearMonth currentYm, final Map<Inventory, List<Entry<LocalDateTime, InventoryDateTimeEvent>>> persistedInventoryEvents,
			final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints, final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices) {
		for (final Entry<Inventory, List<Entry<LocalDateTime, InventoryDateTimeEvent>>> currentPersistedEventsEntry : persistedInventoryEvents.entrySet()) {
			final InventoryLocalState inventoryLocalState = inventoryLocalStateMap.get(currentPersistedEventsEntry.getKey());
			final Set<Integer> deferredIndices = new HashSet<>();
			assert MullUtil.isAtStartHourOfMonth(currentPersistedEventsEntry.getValue().get(0).getKey());
			final int monthIn = currentPersistedEventsEntry.getValue().stream() //
					.map(Entry::getValue) //
					.mapToInt(InventoryDateTimeEvent::getNetVolumeIn) //
					.sum();
			inventoryLocalState.getMullContainer().updateCurrentMonthAbsoluteEntitlement(monthIn);

			// Update status of fixed cargoes
			final Map<YearMonth, List<Cargo>> currentMonthMappedFixedCargoes = monthMappedExistingCargoes.computeIfAbsent(inventoryLocalState.getInventoryGlobalState().getInventory(),
					k -> Collections.emptyMap());
			final List<Cargo> currentCargoes = currentMonthMappedFixedCargoes.get(currentYm);
			if (currentCargoes != null) {
				currentCargoes.forEach(inventoryLocalState.getMullContainer()::dropFixedLoad);
			}

			// Handle direct mappings (those that are not harmonised)
			handleDirectMappings(inventoryLocalState, currentYm, persistedPreviousCargoBlueprints, persistedPreviousCargoBlueprintIndices);
			// Handle when the harmonised entity only has one mapping with a positive monthly allocation
			handleMonthlyAllocationLeavesObviousMapping(inventoryLocalState, currentYm, persistedPreviousCargoBlueprints, persistedPreviousCargoBlueprintIndices);

			// Find cargoes where AACQ satisfaction leaves an obvious entity or cargo choice, e.g. minor lifter has met their AACQ
			final List<Pair<Integer, ICargoBlueprint>> deferredAacqSatisfactionCargoBlueprints = new LinkedList<>();
			handleAacqSatifactionLeavesObviousEntityOrCargoChoice(inventoryLocalState, currentYm, persistedPreviousCargoBlueprints, persistedPreviousCargoBlueprintIndices,
					deferredAacqSatisfactionCargoBlueprints, deferredIndices);

			// TODO: Check whether the below handleSameEntityDeferments() check is captured by the preceeding handleAacqSatifactionLeavesObviousEntityOrCargoChoice() check
			// TODO: Check whether deferredAacqSatisfactionCargoBlueprints can be combined with deferredCargoBlueprints
			final List<Pair<Integer, ICargoBlueprint>> deferredCargoBlueprints = new LinkedList<>();
			handleSameEntityDeferments(inventoryLocalState, currentYm, persistedPreviousCargoBlueprints, persistedPreviousCargoBlueprintIndices, deferredCargoBlueprints, deferredIndices);

			// Find entities/cargoes where all but one alternative cause an fcl lower violation
			handleObviousFclViolation(inventoryLocalState, currentYm, persistedPreviousCargoBlueprints, persistedPreviousCargoBlueprintIndices, deferredCargoBlueprints, deferredIndices);

			// At this point all obvious assignments should be made (and deferred cargoes should be accounted for) default to MULL comparison
			runFinalMullPass(inventoryLocalState, currentYm, persistedPreviousCargoBlueprints, persistedPreviousCargoBlueprintIndices, deferredCargoBlueprints, deferredIndices,
					deferredAacqSatisfactionCargoBlueprints, currentPersistedEventsEntry);
		}
	}

	private void runFinalMullPass(final InventoryLocalState inventoryLocalState, final YearMonth currentYm, final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints,
			final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices, final List<Pair<Integer, ICargoBlueprint>> deferredCargoBlueprints, final Set<Integer> deferredIndices,
			final List<Pair<Integer, ICargoBlueprint>> deferredAacqSatisfactionCargoBlueprints,
			final Entry<Inventory, List<Entry<LocalDateTime, InventoryDateTimeEvent>>> currentPersistedEventsEntry) {
		final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
		final Iterator<ICargoBlueprint> iterCargoBlueprint = persistedPreviousCargoBlueprints.get(inventory).iterator();
		final Iterator<Integer> iterCargoBlueprintIndices = persistedPreviousCargoBlueprintIndices.get(inventory).iterator();
		final List<Pair<Integer, ICargoBlueprint>> persistedPairList = new ArrayList<>();

		while (iterCargoBlueprint.hasNext()) {
			persistedPairList.add(Pair.of(iterCargoBlueprintIndices.next(), iterCargoBlueprint.next()));
		}
		final Iterator<Pair<Integer, ICargoBlueprint>> combinedIter = new CombinedCargoBlueprintIterator(persistedPairList, deferredCargoBlueprints, deferredAacqSatisfactionCargoBlueprints);
		@Nullable
		Pair<Integer, ICargoBlueprint> nextPair = combinedIter.hasNext() ? combinedIter.next() : null;
		if (nextPair != null) {
			final Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>> monthlyCounts = inventoryToMonthlyCounts.get(inventory);
			ICargoBlueprint nextCargoBlueprint = nextPair.getSecond();
			int nextIndex = nextPair.getFirst();
			for (Entry<LocalDateTime, InventoryDateTimeEvent> entry : currentPersistedEventsEntry.getValue()) {
				final InventoryDateTimeEvent currentEvent = entry.getValue();
				inventoryLocalState.getMullContainer().updateRunningAllocation(currentEvent.getNetVolumeIn());
				if (entry.getKey().equals(nextCargoBlueprint.getWindowStart())) {
					final BaseLegalEntity harmonisingEntity = nextCargoBlueprint.getMudContainer().getEntity();
					final IMullDischargeWrapper harmonisingDischargeWrapper = MullUtil.buildDischargeWrapper(nextCargoBlueprint.getAllocationTracker());
					final Pair<BaseLegalEntity, IMullDischargeWrapper> harmonisationKey = Pair.of(harmonisingEntity, harmonisingDischargeWrapper);
					final List<Pair<IMudContainer, IAllocationTracker>> combinedTrackers = rearrangedProfileToLocalTrackingMap.get(inventory).get(harmonisationKey);

					// Check if monthly allocations leave an obvious choice

					final List<Pair<IMudContainer, IAllocationTracker>> monthAllocatedTrackers = combinedTrackers.stream() //
							.filter(pair -> monthlyCounts.get(pair.getFirst().getEntity()).get(currentYm).get(MullUtil.buildDischargeWrapper(pair.getSecond())) > 0) //
							.toList();
					if (monthAllocatedTrackers.size() == 1) {
						final Pair<IMudContainer, IAllocationTracker> replacementPair = monthAllocatedTrackers.get(0);

						final Map<IMullDischargeWrapper, Integer> thisCounts = monthlyCounts.get(replacementPair.getFirst().getEntity()).get(currentYm);
						final IMullDischargeWrapper replacementDischargeWrapper = MullUtil.buildDischargeWrapper(replacementPair.getSecond());
						final int currentCount = thisCounts.get(replacementDischargeWrapper);
						if (currentCount == 0) {
							int i = 0;
						}
						thisCounts.put(replacementDischargeWrapper, currentCount - 1);

						final ICargoBlueprint replacementCargoBlueprint = replaceCargoBlueprintLiftingDetails(nextCargoBlueprint, inventoryLocalState.getMullContainer(), replacementPair.getFirst(),
								replacementPair.getSecond());
						if (!deferredIndices.contains(nextIndex)) {
							replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
						}
						replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());

						cargoBlueprints.get(inventory)[nextIndex] = replacementCargoBlueprint;
					} else {
						final int allocationDrop = nextCargoBlueprint.getAllocatedVolume();
						final Set<IMudContainer> mudChoices = combinedTrackers.stream() //
								.filter(p -> !p.getSecond().satisfiesAacq()) //
								.map(Pair::getFirst) //
								.collect(Collectors.toSet());
						boolean dumpIntoDes = false;
						if (mudChoices.isEmpty()) {
							dumpIntoDes = true;
							for (final Pair<IMudContainer, IAllocationTracker> pair : combinedTrackers) {
								if (pair.getSecond() instanceof DesMarketTracker) {
									mudChoices.add(pair.getFirst());
								}
							}
							if (mudChoices.isEmpty()) {
								throw new IllegalStateException("No DES Sales Market option found");
							}
						}
						final IMudContainer mostEntitledMudContainer = mudChoices.stream() //
								.max(new FinalPhaseComparator(allocationDrop, globalStatesContainer.getMullGlobalState().getFullCargoLotValue())) //
								.get();
						final List<IAllocationTracker> potentialAllocationTrackers;
						if (dumpIntoDes) {
							potentialAllocationTrackers = combinedTrackers.stream() //
									.filter(p -> p.getFirst() == mostEntitledMudContainer) //
									.map(Pair::getSecond) //
									.filter(DesMarketTracker.class::isInstance) //
									.toList();
						} else {
							potentialAllocationTrackers = combinedTrackers.stream() //
									.filter(p -> p.getFirst() == mostEntitledMudContainer) //
									.map(Pair::getSecond) //
									.filter(allocationTracker -> !allocationTracker.satisfiesAacq()) //
									.toList();
						}
						if (potentialAllocationTrackers.size() == 1) {
							final IAllocationTracker replacementAllocationTracker = potentialAllocationTrackers.get(0);
							// final Pair<IMudContainer, IAllocationTracker> replacementPair = Pair.of(mostEntitledMudContainer, )

							final ICargoBlueprint replacementCargoBlueprint = replaceCargoBlueprintLiftingDetails(nextCargoBlueprint, inventoryLocalState.getMullContainer(), mostEntitledMudContainer,
									replacementAllocationTracker);
							assert cargoBlueprints.get(inventory)[nextIndex] == null;
							cargoBlueprints.get(inventory)[nextIndex] = replacementCargoBlueprint;
							if (!deferredIndices.contains(nextIndex)) {
								mostEntitledMudContainer.dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
							}
							replacementAllocationTracker.dropAllocation(replacementCargoBlueprint.getAllocatedVolume());

							final Map<IMullDischargeWrapper, Integer> thisCounts = monthlyCounts.get(mostEntitledMudContainer.getEntity()).get(currentYm);
							final IMullDischargeWrapper replacementDischargeWrapper = MullUtil.buildDischargeWrapper(replacementAllocationTracker);
							final int currentCount = thisCounts.get(replacementDischargeWrapper);
							if (currentCount > 0) {
								thisCounts.put(replacementDischargeWrapper, currentCount - 1);
							}
						} else {
							final IAllocationTracker mudAllocationTracker = potentialAllocationTrackers.stream() //
									.max((t1, t2) -> Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation())) //
									.get();
							final ICargoBlueprint replacementCargoBlueprint = replaceCargoBlueprintLiftingDetails(nextCargoBlueprint, inventoryLocalState.getMullContainer(), mostEntitledMudContainer,
									mudAllocationTracker);
							assert cargoBlueprints.get(inventory)[nextIndex] == null;
							cargoBlueprints.get(inventory)[nextIndex] = replacementCargoBlueprint;

							if (!deferredIndices.contains(nextIndex)) {
								mostEntitledMudContainer.dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
							}
							mudAllocationTracker.dropAllocation(replacementCargoBlueprint.getAllocatedVolume());

							final Map<IMullDischargeWrapper, Integer> thisCounts = monthlyCounts.get(mostEntitledMudContainer.getEntity()).get(currentYm);
							final IMullDischargeWrapper replacementDischargeWrapper = MullUtil.buildDischargeWrapper(mudAllocationTracker);
							final int currentCount = thisCounts.get(replacementDischargeWrapper);
							if (currentCount > 0) {
								thisCounts.put(replacementDischargeWrapper, currentCount - 1);
							}
						}
					}

					if (combinedIter.hasNext()) {
						nextPair = combinedIter.next();
						nextCargoBlueprint = nextPair.getSecond();
						nextIndex = nextPair.getFirst();
					} else {
						break;
					}
				}
			}
		}
	}

	private void handleObviousFclViolation(final InventoryLocalState inventoryLocalState, final YearMonth currentYm, final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints,
			final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices, final List<Pair<Integer, ICargoBlueprint>> deferredCargoBlueprints, final Set<Integer> deferredIndices) {
		final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
		final Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>> monthlyCounts = inventoryToMonthlyCounts.get(inventory);
		boolean madeChanges = true;
		while (madeChanges) {
			madeChanges = false;
			final Iterator<ICargoBlueprint> iterCargoBlueprint = persistedPreviousCargoBlueprints.get(inventory).iterator();
			final Iterator<Integer> iterCargoBlueprintIndices = persistedPreviousCargoBlueprintIndices.get(inventory).iterator();
			while (iterCargoBlueprint.hasNext()) {
				final ICargoBlueprint cargoBlueprint = iterCargoBlueprint.next();
				final int cargoBlueprintIndex = iterCargoBlueprintIndices.next();
				final BaseLegalEntity harmonisationEntity = cargoBlueprint.getMudContainer().getEntity();
				final IMullDischargeWrapper harmonisationDischargeWrapper = MullUtil.buildDischargeWrapper(cargoBlueprint.getAllocationTracker());
				final Pair<BaseLegalEntity, IMullDischargeWrapper> harmonisationKey = Pair.of(harmonisationEntity, harmonisationDischargeWrapper);
				final List<Pair<IMudContainer, IAllocationTracker>> combinedTrackers = rearrangedProfileToLocalTrackingMap.get(inventory).get(harmonisationKey);

				final int allocationDrop = cargoBlueprint.getAllocatedVolume();
				final List<IMudContainer> uniqueMudContainers = new ArrayList<>(combinedTrackers.stream().map(Pair::getFirst).collect(Collectors.toSet()));
				final List<AllocationDropType> mappedToAllocationTypes = uniqueMudContainers.stream() //
						.map(mudContainer -> {
							final int currentMonthEntitlement = mudContainer.getCurrentMonthAbsoluteEntitlement();
							final int afterDrop = currentMonthEntitlement - allocationDrop;
							final int fclVal = globalStatesContainer.getMullGlobalState().getFullCargoLotValue();
							if (currentMonthEntitlement > fclVal) {
								if (afterDrop > fclVal) {
									return AllocationDropType.ABOVE_ABOVE;
								} else if (afterDrop >= -fclVal) {
									return AllocationDropType.ABOVE_IN;
								} else {
									return AllocationDropType.ABOVE_BELOW;
								}
							} else if (currentMonthEntitlement >= -fclVal) {
								return afterDrop >= -fclVal ? AllocationDropType.IN_IN : AllocationDropType.IN_BELOW;
							} else {
								return AllocationDropType.BELOW_BELOW;
							}
						}) //
						.toList();
				final long countAboveAbove = mappedToAllocationTypes.stream().filter(v -> v == AllocationDropType.ABOVE_ABOVE).count();
				if (countAboveAbove <= 1) {
					final long countNotEndingBelow = mappedToAllocationTypes.stream() //
							.filter(v -> v != AllocationDropType.ABOVE_BELOW && v != AllocationDropType.IN_BELOW && v != AllocationDropType.BELOW_BELOW) //
							.count();
					if (countNotEndingBelow == 1L) {
						// Only one entity does not violate lower FCL bound - assign this one
						final Iterator<AllocationDropType> dropTypeIter = mappedToAllocationTypes.iterator();
						final Iterator<IMudContainer> mudIter = uniqueMudContainers.iterator();
						AllocationDropType dropType = dropTypeIter.next();
						IMudContainer mudContainer = mudIter.next();
						while (dropType == AllocationDropType.IN_BELOW || dropType == AllocationDropType.ABOVE_BELOW || dropType == AllocationDropType.BELOW_BELOW) {
							dropType = dropTypeIter.next();
							mudContainer = mudIter.next();
						}
						final IMudContainer selectedMudContainer = mudContainer;
						final List<Pair<IMudContainer, IAllocationTracker>> trackersToAssign = combinedTrackers.stream() //
								.filter(p -> p.getFirst() == selectedMudContainer) //
								.toList();
						if (trackersToAssign.size() == 1) {
							final Pair<IMudContainer, IAllocationTracker> replacementPair = trackersToAssign.get(0);
							final Map<IMullDischargeWrapper, Integer> thisCounts = monthlyCounts.get(replacementPair.getFirst().getEntity()).get(currentYm);
							final IMullDischargeWrapper replacementDischargeWrapper = MullUtil.buildDischargeWrapper(replacementPair.getSecond());
							final int currentCount = thisCounts.get(replacementDischargeWrapper);
							if (currentCount == 0) {
								int i = 0;
							}
							thisCounts.put(replacementDischargeWrapper, currentCount - 1);

							final ICargoBlueprint replacementCargoBlueprint = replaceCargoBlueprintLiftingDetails(cargoBlueprint, inventoryLocalState.getMullContainer(), replacementPair.getFirst(),
									replacementPair.getSecond());
							replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
							replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());

							iterCargoBlueprint.remove();
							iterCargoBlueprintIndices.remove();
							cargoBlueprints.get(inventory)[cargoBlueprintIndex] = replacementCargoBlueprint;
						} else {
							deferredCargoBlueprints.add(Pair.of(cargoBlueprintIndex, cargoBlueprint));
							deferredIndices.add(cargoBlueprintIndex);

							// Only drop allocation on MUD container, allocation tracker updates are deferred
							selectedMudContainer.dropAllocation(cargoBlueprint.getAllocatedVolume());

							iterCargoBlueprint.remove();
							iterCargoBlueprintIndices.remove();
						}
						madeChanges = true;
					}
				}
			}
		}
	}

	private void handleSameEntityDeferments(final InventoryLocalState inventoryLocalState, final YearMonth currentYm, final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints,
			final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices, final List<Pair<Integer, ICargoBlueprint>> deferredCargoBlueprints, final Set<Integer> deferredIndices) {
		final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
		final Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>> monthlyCounts = inventoryToMonthlyCounts.get(inventory);
		final Iterator<ICargoBlueprint> iterCargoBlueprint = persistedPreviousCargoBlueprints.get(inventory).iterator();
		final Iterator<Integer> iterCargoBlueprintIndices = persistedPreviousCargoBlueprintIndices.get(inventory).iterator();
		while (iterCargoBlueprint.hasNext()) {
			final ICargoBlueprint cargoBlueprint = iterCargoBlueprint.next();
			final int cargoBlueprintIndex = iterCargoBlueprintIndices.next();
			final BaseLegalEntity harmonisationEntity = cargoBlueprint.getMudContainer().getEntity();
			final IMullDischargeWrapper harmonisationDischargeWrapper = MullUtil.buildDischargeWrapper(cargoBlueprint.getAllocationTracker());
			final Pair<BaseLegalEntity, IMullDischargeWrapper> harmonisationKey = Pair.of(harmonisationEntity, harmonisationDischargeWrapper);
			final List<Pair<IMudContainer, IAllocationTracker>> combinedTrackers = rearrangedProfileToLocalTrackingMap.get(inventory).get(harmonisationKey);
			assert combinedTrackers.size() > 1;
			final Set<BaseLegalEntity> entitledEntities = combinedTrackers.stream() //
					.map(Pair::getFirst) //
					.map(IMudContainer::getEntity) //
					.collect(Collectors.toSet());
			if (entitledEntities.size() == 1) {
				deferredCargoBlueprints.add(Pair.of(cargoBlueprintIndex, cargoBlueprint));
				deferredIndices.add(cargoBlueprintIndex);

				// Only drop allocation on MUD container, allocation tracker updates are deferred
				final IMudContainer entitledMudContainer = combinedTrackers.get(0).getFirst();
				entitledMudContainer.dropAllocation(cargoBlueprint.getAllocatedVolume());

				iterCargoBlueprint.remove();
				iterCargoBlueprintIndices.remove();
			}
		}
	}

	private void handleAacqSatifactionLeavesObviousEntityOrCargoChoice(final InventoryLocalState inventoryLocalState, final YearMonth currentYm,
			final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints, final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices,
			final List<Pair<Integer, ICargoBlueprint>> deferredAacqSatisfactionCargoBlueprints, final Set<Integer> deferredIndices) {
		final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
		final Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>> monthlyCounts = inventoryToMonthlyCounts.get(inventory);
		final Iterator<ICargoBlueprint> iterCargoBlueprint = persistedPreviousCargoBlueprints.get(inventory).iterator();
		final Iterator<Integer> iterCargoBlueprintIndices = persistedPreviousCargoBlueprintIndices.get(inventory).iterator();
		while (iterCargoBlueprint.hasNext()) {
			final ICargoBlueprint cargoBlueprint = iterCargoBlueprint.next();
			final int cargoBlueprintIndex = iterCargoBlueprintIndices.next();
			final BaseLegalEntity harmonisationEntity = cargoBlueprint.getMudContainer().getEntity();
			final IMullDischargeWrapper harmonisationDischargeWrapper = MullUtil.buildDischargeWrapper(cargoBlueprint.getAllocationTracker());
			final Pair<BaseLegalEntity, IMullDischargeWrapper> harmonisationKey = Pair.of(harmonisationEntity, harmonisationDischargeWrapper);
			final List<Pair<IMudContainer, IAllocationTracker>> combinedTrackers = rearrangedProfileToLocalTrackingMap.get(inventory).get(harmonisationKey);
			final List<Pair<IMudContainer, IAllocationTracker>> nonAacqSatisfiedTrackers = combinedTrackers.stream() //
					.filter(p -> !p.getSecond().satisfiesAacq()) //
					.toList();
			if (nonAacqSatisfiedTrackers.size() == 1) {
				final Pair<IMudContainer, IAllocationTracker> replacementPair = nonAacqSatisfiedTrackers.get(0);
				final Map<IMullDischargeWrapper, Integer> thisCounts = monthlyCounts.get(replacementPair.getFirst().getEntity()).get(currentYm);
				final IMullDischargeWrapper replacementDischargeWrapper = MullUtil.buildDischargeWrapper(replacementPair.getSecond());
				final int currentCount = thisCounts.get(replacementDischargeWrapper);
				if (currentCount == 0) {
					int i = 0;
				}
				thisCounts.put(replacementDischargeWrapper, currentCount - 1);

				final ICargoBlueprint replacementCargoBlueprint = replaceCargoBlueprintLiftingDetails(cargoBlueprint, inventoryLocalState.getMullContainer(), replacementPair.getFirst(),
						replacementPair.getSecond());
				replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
				replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());

				iterCargoBlueprint.remove();
				iterCargoBlueprintIndices.remove();
				cargoBlueprints.get(inventory)[cargoBlueprintIndex] = replacementCargoBlueprint;
			} else {
				final Set<BaseLegalEntity> entitledEntities = nonAacqSatisfiedTrackers.stream() //
						.map(Pair::getFirst) //
						.map(IMudContainer::getEntity) //
						.collect(Collectors.toSet());
				if (entitledEntities.size() == 1) {
					deferredAacqSatisfactionCargoBlueprints.add(Pair.of(cargoBlueprintIndex, cargoBlueprint));
					deferredIndices.add(cargoBlueprintIndex);

					// Only drop allocation on MUD container, allocation tracker updates are deferred
					final IMudContainer entitledMudContainer = nonAacqSatisfiedTrackers.get(0).getFirst();
					entitledMudContainer.dropAllocation(cargoBlueprint.getAllocatedVolume());

					iterCargoBlueprint.remove();
					iterCargoBlueprintIndices.remove();
				}
			}
		}
	}

	private void handleMonthlyAllocationLeavesObviousMapping(final InventoryLocalState inventoryLocalState, final YearMonth currentYm,
			final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints, final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices) {
		final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
		final Map<BaseLegalEntity, Map<YearMonth, Map<IMullDischargeWrapper, Integer>>> monthlyCounts = inventoryToMonthlyCounts.get(inventory);
		final Iterator<ICargoBlueprint> iterCargoBlueprint = persistedPreviousCargoBlueprints.get(inventory).iterator();
		final Iterator<Integer> iterCargoBlueprintIndices = persistedPreviousCargoBlueprintIndices.get(inventory).iterator();
		while (iterCargoBlueprint.hasNext()) {
			final ICargoBlueprint cargoBlueprint = iterCargoBlueprint.next();
			final int cargoBlueprintIndex = iterCargoBlueprintIndices.next();
			final BaseLegalEntity harmonisationEntity = cargoBlueprint.getMudContainer().getEntity();
			final IMullDischargeWrapper harmonisationDischargeWrapper = MullUtil.buildDischargeWrapper(cargoBlueprint.getAllocationTracker());
			final Pair<BaseLegalEntity, IMullDischargeWrapper> harmonisationKey = Pair.of(harmonisationEntity, harmonisationDischargeWrapper);
			final List<Pair<IMudContainer, IAllocationTracker>> combinedTrackers = rearrangedProfileToLocalTrackingMap.get(inventory).get(harmonisationKey);
			final List<Pair<IMudContainer, IAllocationTracker>> monthAllocatedTrackers = combinedTrackers.stream() //
					.filter(pair -> monthlyCounts.get(pair.getFirst().getEntity()).get(currentYm).get(MullUtil.buildDischargeWrapper(pair.getSecond())) > 0) //
					.toList();
			if (monthAllocatedTrackers.size() == 1) {
				final Pair<IMudContainer, IAllocationTracker> replacementPair = monthAllocatedTrackers.get(0);
				final Map<IMullDischargeWrapper, Integer> thisCounts = monthlyCounts.get(replacementPair.getFirst().getEntity()).get(currentYm);
				final IMullDischargeWrapper replacementDischargeWrapper = MullUtil.buildDischargeWrapper(replacementPair.getSecond());
				final int currentCount = thisCounts.get(replacementDischargeWrapper);
				if (currentCount == 0) {
					int i = 0;
				}
				thisCounts.put(replacementDischargeWrapper, currentCount - 1);

				final ICargoBlueprint replacementCargoBlueprint = replaceCargoBlueprintLiftingDetails(cargoBlueprint, inventoryLocalState.getMullContainer(), replacementPair.getFirst(),
						replacementPair.getSecond());
				replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
				replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());

				iterCargoBlueprint.remove();
				iterCargoBlueprintIndices.remove();
				cargoBlueprints.get(inventory)[cargoBlueprintIndex] = replacementCargoBlueprint;
			}
		}
	}

	private void handleDirectMappings(final InventoryLocalState inventoryLocalState, final YearMonth currentYm, final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints,
			final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices) {
		final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
		final Iterator<ICargoBlueprint> iterCargoBlueprint = persistedPreviousCargoBlueprints.get(inventory).iterator();
		final Iterator<Integer> iterCargoBlueprintIndices = persistedPreviousCargoBlueprintIndices.get(inventory).iterator();
		while (iterCargoBlueprint.hasNext()) {
			final ICargoBlueprint cargoBlueprint = iterCargoBlueprint.next();
			final int cargoBlueprintIndex = iterCargoBlueprintIndices.next();
			final BaseLegalEntity harmonisationEntity = cargoBlueprint.getMudContainer().getEntity();
			final IMullDischargeWrapper harmonisationDischargeWrapper = MullUtil.buildDischargeWrapper(cargoBlueprint.getAllocationTracker());
			final Pair<BaseLegalEntity, IMullDischargeWrapper> harmonisationKey = Pair.of(harmonisationEntity, harmonisationDischargeWrapper);
			final List<Pair<IMudContainer, IAllocationTracker>> combinedTrackers = rearrangedProfileToLocalTrackingMap.get(inventory).get(harmonisationKey);
			if (combinedTrackers.size() == 1) {
				final Pair<IMudContainer, IAllocationTracker> replacementPair = combinedTrackers.get(0);
				final BaseLegalEntity replacementEntity = replacementPair.getFirst().getEntity();
				final IMullDischargeWrapper replacementDischargeWrapper = MullUtil.buildDischargeWrapper(replacementPair.getSecond());
				if (harmonisationEntity != replacementEntity || !harmonisationDischargeWrapper.equals(replacementDischargeWrapper)) {
					throw new IllegalStateException("Directly mapping should not correspond to a different pair");
				}
				final Map<IMullDischargeWrapper, Integer> thisCounts = inventoryToMonthlyCounts.get(inventory).get(harmonisationEntity).get(currentYm);
				final int currentCount = thisCounts.get(harmonisationDischargeWrapper);
				if (currentCount == 0) {
					int i = 0;
				}
				thisCounts.put(harmonisationDischargeWrapper, currentCount - 1);

				final ICargoBlueprint replacementCargoBlueprint = replaceCargoBlueprintLiftingDetails(cargoBlueprint, inventoryLocalStateMap.get(inventory).getMullContainer(),
						replacementPair.getFirst(), replacementPair.getSecond());

				replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
				replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());

				iterCargoBlueprint.remove();
				iterCargoBlueprintIndices.remove();
				cargoBlueprints.get(inventory)[cargoBlueprintIndex] = replacementCargoBlueprint;
			}
		}
	}

	private ICargoBlueprint replaceCargoBlueprintLiftingDetails(final ICargoBlueprint originalCargoBlueprint, final IMullContainer replacementMullContainer,
			final IMudContainer replacementMudContainer, final IAllocationTracker replacementAllocationTracker) {
		return new CargoBlueprint(//
				originalCargoBlueprint.getLoadCounter(), //
				replacementMullContainer, //
				replacementMudContainer, //
				replacementAllocationTracker, //
				originalCargoBlueprint.getVessel(), //
				originalCargoBlueprint.getAllocatedVolume(), //
				originalCargoBlueprint.getWindowStart(), //
				originalCargoBlueprint.getWindowSizeHours());

	}

	private void runLocalMonthInitialisation(final YearMonth currentYm, final Map<Inventory, List<Entry<LocalDateTime, InventoryDateTimeEvent>>> persistedInventoryEvents,
			final Map<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> inventoryToEventIterators, final Map<Inventory, List<ICargoBlueprint>> persistedPreviousCargoBlueprints,
			final Map<Inventory, List<Integer>> persistedPreviousCargoBlueprintIndices) {
		for (final InventoryLocalState inventoryLocalState : inventoryLocalStates) {
			final Inventory inventory = inventoryLocalState.getInventoryGlobalState().getInventory();
			// persist events for this month
			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> currentIterator = inventoryToEventIterators.get(inventory);
			final List<Entry<LocalDateTime, InventoryDateTimeEvent>> currentPersistedInventoryEvents = initialiseLocalInventoryEventPersisting(currentYm, currentIterator);
			persistedInventoryEvents.put(inventory, currentPersistedInventoryEvents);

			// persist generated cargoes for this month
			final List<ICargoBlueprint> currentPersistedPreviousCargoBlueprints = intialiseCargoBlueprintPersisting(currentYm, inventory);
			persistedPreviousCargoBlueprints.put(inventory, currentPersistedPreviousCargoBlueprints);
			final List<Integer> currentPersistedPreviousCargoBlueprintIndices = intialiseCargoBlueprintIndexPersisting(currentYm, inventory, currentPersistedPreviousCargoBlueprints);
			persistedPreviousCargoBlueprintIndices.put(inventory, currentPersistedPreviousCargoBlueprintIndices);

		}
	}

	private List<ICargoBlueprint> intialiseCargoBlueprintPersisting(final YearMonth currentYm, final Inventory inventory) {
		final List<ICargoBlueprint> persistedCargoBlueprints = new LinkedList<>();
		final Iterator<ICargoBlueprint> previousCargoBlueprintsIter = previousCargoBlueprintIters.get(inventory);
		@Nullable
		ICargoBlueprint c = nextPreviousCargoBlueprint.get(inventory);
		if (c != null) {
			while (c != null && YearMonth.from(c.getWindowStart()).equals(currentYm)) {
				persistedCargoBlueprints.add(c);
				if (previousCargoBlueprintsIter.hasNext()) {
					c = previousCargoBlueprintsIter.next();
					nextPreviousCargoBlueprint.put(inventory, c);
				} else {
					c = null;
					nextPreviousCargoBlueprint.remove(inventory);
				}
			}
		}
		return persistedCargoBlueprints;
	}

	private List<Integer> intialiseCargoBlueprintIndexPersisting(final YearMonth currentYm, final Inventory inventory, final List<ICargoBlueprint> persistedCargoBlueprints) {
		final List<Integer> currentCargoBlueprintIndices = new LinkedList<>();
		if (persistedCargoBlueprints.isEmpty()) {
			return currentCargoBlueprintIndices;
		}
		final int indexStart = previousCargoBlueprintIndexStart.get(inventory);
		for (int i = 0; i < persistedCargoBlueprints.size(); ++i) {
			currentCargoBlueprintIndices.add(indexStart + i);
		}
		previousCargoBlueprintIndexStart.put(inventory, indexStart + persistedCargoBlueprints.size());
		return currentCargoBlueprintIndices;
	}

	private List<Entry<LocalDateTime, InventoryDateTimeEvent>> initialiseLocalInventoryEventPersisting(final YearMonth currentYm,
			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> currentIterator) {
		final List<Entry<LocalDateTime, InventoryDateTimeEvent>> currentPersistedInventoryEvents = new LinkedList<>();
		assert currentIterator.hasNext();
		Entry<LocalDateTime, InventoryDateTimeEvent> currentEntry = currentIterator.next();
		assert YearMonth.from(currentEntry.getKey()).equals(currentYm);
		final LocalDateTime lastExpectedDateTime = currentYm.atEndOfMonth().atTime(23, 0);
		while (currentEntry.getKey().isBefore(lastExpectedDateTime)) {
			currentPersistedInventoryEvents.add(currentEntry);
			assert currentIterator.hasNext();
			currentEntry = currentIterator.next();
		}
		assert currentEntry.getKey().equals(lastExpectedDateTime);
		currentPersistedInventoryEvents.add(currentEntry);
		return currentPersistedInventoryEvents;
	}

	private Map<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> buildInventoryToEventIterators() {
		return globalStatesContainer.getInventoryGlobalStates().values().stream() //
				.collect(Collectors.toMap(InventoryGlobalState::getInventory, igs -> igs.getInsAndOuts().entrySet().iterator()));
	}
}
