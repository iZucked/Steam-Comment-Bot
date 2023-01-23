/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.MullUtil;
import com.mmxlabs.models.lng.adp.mull.harmonisation.HarmonisationTransformationState;
import com.mmxlabs.models.lng.adp.mull.profile.DesSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.EntityRow;
import com.mmxlabs.models.lng.adp.mull.profile.IEntityRow;
import com.mmxlabs.models.lng.adp.mull.profile.IMullProfile;
import com.mmxlabs.models.lng.adp.mull.profile.IMullSubprofile;
import com.mmxlabs.models.lng.adp.mull.profile.MullProfile;
import com.mmxlabs.models.lng.adp.mull.profile.MullSubprofile;
import com.mmxlabs.models.lng.adp.mull.profile.SalesContractAllocationRow;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

@NonNullByDefault
public class HarmonisationMap {

	private final IMullProfile originalMullProfile;
	private final IMullProfile harmonisedMullProfile;
	private final Map<Inventory, HarmonisationTransformationState> harmonisationTransformationStates = new HashMap<>();

	public HarmonisationMap(final IMullProfile originalMullProfile, final GlobalStatesContainer globalStates) {
		this.originalMullProfile = originalMullProfile;
		this.harmonisedMullProfile = buildHarmonisedMullProfile(globalStates);
	}

	public IMullProfile getOriginalMullProfile() {
		return originalMullProfile;
	}

	public IMullProfile getHarmonisedMullProfile() {
		return harmonisedMullProfile;
	}

	public Map<Inventory, HarmonisationTransformationState> getHarmonisationTransformationStates() {
		return harmonisationTransformationStates;
	}

	private IMullProfile buildHarmonisedMullProfile(final GlobalStatesContainer globalStates) {
		final List<IMullSubprofile> mullSubprofiles = new ArrayList<>();
		for (final IMullSubprofile mullSubprofile : this.originalMullProfile.getMullSubprofiles()) {
			final HarmonisationTransformationState transformationState = new HarmonisationTransformationState(mullSubprofile);
			harmonisationTransformationStates.put(mullSubprofile.getInventory(), transformationState);
			populateNewCombinations(mullSubprofile, transformationState, globalStates);
			rearrangeSubprofile(mullSubprofile, globalStates, transformationState);
			final IMullSubprofile harmonisedMullSubprofile = buildMullSubprofile(mullSubprofile.getInventory(), transformationState);
			mullSubprofiles.add(harmonisedMullSubprofile);
		}
		return new MullProfile(mullSubprofiles);
	}

	private IMullSubprofile buildMullSubprofile(final Inventory inventory, final HarmonisationTransformationState harmonisationTransformationState) {
		final List<IEntityRow> entityRows = new ArrayList<>();
		harmonisationTransformationState.getRearrangedProfile().entrySet().forEach(entry -> {
			final BaseLegalEntity entity = entry.getKey();
			final Pair<List<IMullDischargeWrapper>, Map<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>>> pair = entry.getValue();
			if (harmonisationTransformationState.getRearrangedContainment().get(entity).isEmpty()) {
				// This entity has lost all of its allocations to different entities
				// Check that state is valid
				assert harmonisationTransformationState.getRunningRelativeEntitlement().get(entity) == 0.0;
				assert harmonisationTransformationState.getRunningInitialAllocation().get(entity) == 0L;
				assert pair.getFirst().isEmpty() && pair.getSecond().isEmpty();
			} else {
				assert !pair.getFirst().isEmpty() || !pair.getSecond().isEmpty();
				final List<SalesContractAllocationRow> salesContractAllocationRows = new ArrayList<>();
				final List<DesSalesMarketAllocationRow> desSalesMarketAllocationRows = new ArrayList<>();
				// Populate all the unharmonised cases
				for (final IMullDischargeWrapper mullDischargeWrapper : pair.getFirst()) {
					final int aacq = harmonisationTransformationState.getOriginalAacq(entity, mullDischargeWrapper);
					final List<Vessel> vessels = harmonisationTransformationState.getOriginalVessels(entity, mullDischargeWrapper);
					if (mullDischargeWrapper instanceof final MullSalesContractWrapper mullSalesContractWrapper) {
						final SalesContract salesContract = mullSalesContractWrapper.getSalesContract();
						salesContractAllocationRows.add(new SalesContractAllocationRow(salesContract, aacq, vessels));
					} else {
						final DESSalesMarket desSalesMarket = ((MullDesSalesMarketWrapper) mullDischargeWrapper).getDesSalesMarket();
						desSalesMarketAllocationRows.add(new DesSalesMarketAllocationRow(desSalesMarket, aacq, vessels));
					}
				}
				// Populate all the harmonised cases
				for (Entry<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>> harmonisedEntry : pair.getSecond().entrySet()) {
					final IMullDischargeWrapper absorbingDischargeWrapper = harmonisedEntry.getKey();
					final List<Pair<BaseLegalEntity, IMullDischargeWrapper>> liftPairsToAbsorb = harmonisedEntry.getValue();
					final int aacq = harmonisationTransformationState.getOriginalAacq(entity, absorbingDischargeWrapper)
							+ liftPairsToAbsorb.stream().mapToInt(harmonisationTransformationState::getOriginalAacq).sum();
					final List<Vessel> vessels = harmonisationTransformationState.getOriginalVessels(entity, absorbingDischargeWrapper);
					if (absorbingDischargeWrapper instanceof final MullSalesContractWrapper mullSalesContractWrapper) {
						final SalesContract salesContract = mullSalesContractWrapper.getSalesContract();
						salesContractAllocationRows.add(new SalesContractAllocationRow(salesContract, aacq, vessels));
					} else {
						final DESSalesMarket desSalesMarket = ((MullDesSalesMarketWrapper) absorbingDischargeWrapper).getDesSalesMarket();
						desSalesMarketAllocationRows.add(new DesSalesMarketAllocationRow(desSalesMarket, aacq, vessels));
					}
				}
				final int initialAllocation = harmonisationTransformationState.getRunningInitialAllocation().get(entity).intValue();
				final double relativeEntitlement = harmonisationTransformationState.getRunningRelativeEntitlement().get(entity);
				final IEntityRow entityRow = new EntityRow(entity, initialAllocation, relativeEntitlement, salesContractAllocationRows, desSalesMarketAllocationRows);
				entityRows.add(entityRow);
			}
		});
		final IMullSubprofile harmonisedMullSubprofile = new MullSubprofile(inventory, entityRows);
		return harmonisedMullSubprofile;
	}

	private void rearrangeSubprofile(final IMullSubprofile mullSubprofile, final GlobalStatesContainer globalStates, final HarmonisationTransformationState harmonisationTransformationState) {

		final InventoryGlobalState inventoryGlobalState = globalStates.getInventoryGlobalStates().get(mullSubprofile.getInventory());

		// Pairs that do not move
		final List<Pair<BaseLegalEntity, IMullDischargeWrapper>> nonMovingElements = harmonisationTransformationState.getNewCombinations().entrySet().stream() //
				.filter(entry -> entry.getValue().size() == 1) //
				.map(Entry::getKey) //
				.toList();
		// Pairs that are combined
		final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>> movingElements = new HashMap<>();
		harmonisationTransformationState.getNewCombinations().entrySet().stream() //
				.filter(entry -> entry.getValue().size() > 1) //
				.forEach(entry -> movingElements.put(entry.getKey(), entry.getValue()));

		// Rearrangement map:
		// The entity key will be an element of the new profile
		// The list of mullDischargeWrappers are copied from the old profile
		// The map of mullDischargeWrappers to list of pairs are mullDischargeWrappers that absorb other (entity, mullDischargeWrapper) pairs because of vessel sharing
		final Map<BaseLegalEntity, Pair<List<IMullDischargeWrapper>, Map<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>>>> rearrangedProfile = harmonisationTransformationState
				.getRearrangedProfile();
		// Populate with original entities
		mullSubprofile.getEntityRows().stream() //
				.map(IEntityRow::getEntity) //
				.forEach(entity -> rearrangedProfile.put(entity, Pair.of(new LinkedList<>(), new HashMap<>())));
		// Populate the rearranged profile
		// nonMovingElements get placed directly into the lists that are copied directly
		nonMovingElements.forEach(pair -> rearrangedProfile.get(pair.getFirst()).getFirst().add(pair.getSecond()));
		for (final Entry<Pair<BaseLegalEntity, IMullDischargeWrapper>, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>> movingElementEntry : movingElements.entrySet()) {
			final BaseLegalEntity mostMinorEntity = movingElementEntry.getValue().stream() //
					.map(Pair::getFirst) //
					.min((entity1, entity2) -> {
						final double relativeEntitlement1 = harmonisationTransformationState.getOriginalRelativeEntitlement(entity1);
						final double relativeEntitlement2 = harmonisationTransformationState.getOriginalRelativeEntitlement(entity2);
						return Double.compare(relativeEntitlement1, relativeEntitlement2);
					}) //
					.get();

			// If we have the option of using the DesMarket (which is the bucket) use it
			final List<Pair<BaseLegalEntity, IMullDischargeWrapper>> mostMinorEntityPairCandidates = movingElementEntry.getValue().stream() //
					.filter(pair -> pair.getFirst() == mostMinorEntity) //
					.toList();
			final Optional<Pair<BaseLegalEntity, IMullDischargeWrapper>> optDesMinorLifterPair = mostMinorEntityPairCandidates.stream() //
					.filter(pair -> pair.getSecond() instanceof MullDesSalesMarketWrapper) //
					.findAny();
			final Pair<BaseLegalEntity, IMullDischargeWrapper> minorLifterPair = optDesMinorLifterPair.isPresent() ? optDesMinorLifterPair.get() : mostMinorEntityPairCandidates.get(0);
			final List<Pair<BaseLegalEntity, IMullDischargeWrapper>> sharedCombinations = movingElementEntry.getValue().stream() //
					.filter(pair -> pair != minorLifterPair) //
					.toList();
			final Map<BaseLegalEntity, Set<Pair<BaseLegalEntity, IMullDischargeWrapper>>> rearrangedContainment = harmonisationTransformationState.getRearrangedContainment();
			final Map<BaseLegalEntity, Double> runningRelativeEntitlement = harmonisationTransformationState.getRunningRelativeEntitlement();
			final Map<BaseLegalEntity, Long> runningInitialAllocation = harmonisationTransformationState.getRunningInitialAllocation();
			for (final Pair<BaseLegalEntity, IMullDischargeWrapper> lifterBeingAbsorbed : sharedCombinations) {
				if (lifterBeingAbsorbed.getFirst() != minorLifterPair.getFirst()) {
					final BaseLegalEntity otherEntityBeingAbsorbed = lifterBeingAbsorbed.getFirst();
					rearrangedContainment.get(otherEntityBeingAbsorbed).remove(lifterBeingAbsorbed);
					rearrangedContainment.get(minorLifterPair.getFirst()).add(lifterBeingAbsorbed);
					// We may have fully absorbed the contents of another entity
					if (rearrangedContainment.get(otherEntityBeingAbsorbed).isEmpty()) {
						final double relativeEntitlementBeingAbsorbed = runningRelativeEntitlement.get(otherEntityBeingAbsorbed);
						final long initialAllocationBeingAbsorbed = runningInitialAllocation.get(otherEntityBeingAbsorbed);
						final double newAbsorbingEntityRelativeEntitlement = runningRelativeEntitlement.get(minorLifterPair.getFirst()) + relativeEntitlementBeingAbsorbed;
						final long newAbsorbingEntityInitialAllocation = runningInitialAllocation.get(minorLifterPair.getFirst()) + initialAllocationBeingAbsorbed;
						runningRelativeEntitlement.put(minorLifterPair.getFirst(), newAbsorbingEntityRelativeEntitlement);
						runningInitialAllocation.put(minorLifterPair.getFirst(), newAbsorbingEntityInitialAllocation);
						runningRelativeEntitlement.put(otherEntityBeingAbsorbed, 0.0);
						runningInitialAllocation.put(otherEntityBeingAbsorbed, 0L);
					} else {
						final int loadDuration = inventoryGlobalState.getLoadDuration();
						final List<Vessel> vessels = harmonisationTransformationState.getOriginalVessels(minorLifterPair);
						final int averageVolumeLifted = vessels.stream() //
								.mapToInt(v -> MullUtil.calculateVolumeLiftedBarSafetyHeel(v, loadDuration)) //
								.sum() / vessels.size();
						final int totalProd = inventoryGlobalState.getYearlyProduction();
						final double allocatedProduction = totalProd * harmonisationTransformationState.getOriginalRelativeEntitlement(otherEntityBeingAbsorbed);
						final int aacqLiftedVolume = harmonisationTransformationState.getOriginalAacq(lifterBeingAbsorbed) * averageVolumeLifted;
						final double localPercentage = aacqLiftedVolume / allocatedProduction;
						final double reShift = harmonisationTransformationState.getOriginalRelativeEntitlement(otherEntityBeingAbsorbed) * localPercentage;
						final long initialAllocationShift = (long) (harmonisationTransformationState.getOriginalInitialAllocation(otherEntityBeingAbsorbed) * localPercentage);
						runningRelativeEntitlement.put(minorLifterPair.getFirst(), runningRelativeEntitlement.get(minorLifterPair.getFirst()) + reShift);
						runningRelativeEntitlement.put(otherEntityBeingAbsorbed, runningRelativeEntitlement.get(otherEntityBeingAbsorbed) - reShift);
						runningInitialAllocation.put(minorLifterPair.getFirst(), runningInitialAllocation.get(minorLifterPair.getFirst()) + initialAllocationShift);
						runningInitialAllocation.put(otherEntityBeingAbsorbed, runningInitialAllocation.get(otherEntityBeingAbsorbed) - initialAllocationShift);
					}
				}
				rearrangedProfile.get(minorLifterPair.getFirst()).getSecond().put(minorLifterPair.getSecond(), sharedCombinations);
			}
		}
	}

	private void populateNewCombinations(final IMullSubprofile mullSubprofile, final HarmonisationTransformationState harmonisationTransformationState, final GlobalStatesContainer globalStates) {
		final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>> newCombinations = harmonisationTransformationState.getNewCombinations();
		// Handle first party
		final Set<BaseLegalEntity> firstPartyEntities = globalStates.getMullGlobalState().getFirstPartyEntities();
		final List<IEntityRow> firstParties = mullSubprofile.getEntityRows().stream().filter(row -> firstPartyEntities.contains(row.getEntity())).toList();
		if (globalStates.getMullGlobalState().getFirstPartyEntities().isEmpty()) {
			throw new IllegalStateException("Subprofile should contain a first party entry");
		} else if (firstParties.size() != 1) {
			throw new IllegalStateException("Subprofile should only contain a single first party entry");
		}
		final IEntityRow firstPartyRow = firstParties.get(0);
		final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, Set<Vessel>> firstPartyVesselsMap = new HashMap<>();
		final BaseLegalEntity firstPartyEntity = firstPartyRow.getEntity();
		firstPartyRow.streamAllocationRows().forEach(row -> {
			final Pair<BaseLegalEntity, IMullDischargeWrapper> newPair = Pair.of(firstPartyEntity, MullUtil.buildDischargeWrapper(row));
			newCombinations.put(newPair, new LinkedList<>(Collections.singleton(newPair)));
			firstPartyVesselsMap.put(newPair, new HashSet<>(row.getVessels()));
		});
		// Handle third parties
		for (final IEntityRow thirdPartyRow : mullSubprofile.getEntityRows()) {
			final boolean isThirdParty = !globalStates.getMullGlobalState().getFirstPartyEntities().contains(thirdPartyRow.getEntity());
			if (!isThirdParty) {
				continue;
			}
			final BaseLegalEntity thirdPartyEntity = thirdPartyRow.getEntity();
			thirdPartyRow.streamAllocationRows().forEach(row -> {
				final List<Vessel> thisRowVessels = row.getVessels();
				final Optional<Pair<BaseLegalEntity, IMullDischargeWrapper>> optVesselSharingPair = firstPartyVesselsMap.entrySet().stream() //
						.filter(entry -> {
							final Set<Vessel> firstPartyVessels = entry.getValue();
							return firstPartyVessels.size() == thisRowVessels.size() && thisRowVessels.stream().allMatch(firstPartyVessels::contains);
						}) //
						.map(Entry::getKey) //
						.findAny();
				final Pair<BaseLegalEntity, IMullDischargeWrapper> thisRowPair = Pair.of(thirdPartyEntity, MullUtil.buildDischargeWrapper(row));
				if (optVesselSharingPair.isPresent()) {
					final Pair<BaseLegalEntity, IMullDischargeWrapper> firstPartyPair = optVesselSharingPair.get();
					newCombinations.get(firstPartyPair).add(thisRowPair);
				} else {
					newCombinations.put(thisRowPair, Collections.singletonList(thisRowPair));
				}
			});
		}
	}

}
