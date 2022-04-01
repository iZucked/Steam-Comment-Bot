package com.mmxlabs.models.lng.adp.mull.harmonisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.MullUtil;
import com.mmxlabs.models.lng.adp.mull.algorithm.IMullDischargeWrapper;
import com.mmxlabs.models.lng.adp.mull.profile.IAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.IEntityRow;
import com.mmxlabs.models.lng.adp.mull.profile.IMullSubprofile;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class HarmonisationTransformationState {

	private Map<BaseLegalEntity, IEntityRow> entityToOriginalEntityRowMap = new HashMap<>();
	private Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, IAllocationRow> entityDischargePairToOriginalAllocationRowMap = new HashMap<>();

	final Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>> newCombinations = new HashMap<>();
	final Map<BaseLegalEntity, Set<Pair<BaseLegalEntity, IMullDischargeWrapper>>> rearrangedContainment = new HashMap<>();
	final Map<BaseLegalEntity, Double> runningRelativeEntitlement = new HashMap<>();
	final Map<BaseLegalEntity, Long> runningInitialAllocation = new HashMap<>();
	final Map<BaseLegalEntity, Pair<List<IMullDischargeWrapper>, Map<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>>>> rearrangedProfile = new HashMap<>();

	public HarmonisationTransformationState(final IMullSubprofile mullSubprofile) {
		mullSubprofile.getEntityRows().forEach(entityRow -> {
			final BaseLegalEntity entity = entityRow.getEntity();
			entityToOriginalEntityRowMap.put(entity, entityRow);
			entityRow.streamAllocationRows().forEach(allocationRow -> {
				entityDischargePairToOriginalAllocationRowMap.put(Pair.of(entity, MullUtil.buildDischargeWrapper(allocationRow)), allocationRow);
			});
			rearrangedContainment.put(entity, entityRow.streamAllocationRows().map(MullUtil::buildDischargeWrapper).map(wrapper -> Pair.of(entity, wrapper)).collect(Collectors.toSet()));
			runningRelativeEntitlement.put(entity, entityRow.getRelativeEntitlement());
			runningInitialAllocation.put(entity, (long) entityRow.getInitialAllocation());
		});
	}

	public double getOriginalRelativeEntitlement(final BaseLegalEntity entity) {
		return this.entityToOriginalEntityRowMap.get(entity).getRelativeEntitlement();
	}

	public int getOriginalInitialAllocation(final BaseLegalEntity entity) {
		return this.entityToOriginalEntityRowMap.get(entity).getInitialAllocation();
	}

	public List<Vessel> getOriginalVessels(final Pair<BaseLegalEntity, IMullDischargeWrapper> pair) {
		return ImmutableList.copyOf(entityDischargePairToOriginalAllocationRowMap.get(pair).getVessels());
	}

	public List<Vessel> getOriginalVessels(final BaseLegalEntity entity, final IMullDischargeWrapper dischargeWrapper) {
		return getOriginalVessels(Pair.of(entity, dischargeWrapper));
	}

	public int getOriginalAacq(final Pair<BaseLegalEntity, IMullDischargeWrapper> pair) {
		return entityDischargePairToOriginalAllocationRowMap.get(pair).getWeight();
	}

	public int getOriginalAacq(final BaseLegalEntity entity, final IMullDischargeWrapper dischargeWrapper) {
		return getOriginalAacq(Pair.of(entity, dischargeWrapper));
	}

	public Map<Pair<BaseLegalEntity, IMullDischargeWrapper>, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>> getNewCombinations() {
		return newCombinations;
	}

	public Map<BaseLegalEntity, Set<Pair<BaseLegalEntity, IMullDischargeWrapper>>> getRearrangedContainment() {
		return rearrangedContainment;
	}

	public Map<BaseLegalEntity, Long> getRunningInitialAllocation() {
		return runningInitialAllocation;
	}

	public Map<BaseLegalEntity, Double> getRunningRelativeEntitlement() {
		return runningRelativeEntitlement;
	}

	public Map<BaseLegalEntity, Pair<List<IMullDischargeWrapper>, Map<IMullDischargeWrapper, List<Pair<BaseLegalEntity, IMullDischargeWrapper>>>>> getRearrangedProfile() {
		return rearrangedProfile;
	}
}
