/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mmxlabs.common.exposures.BasicExposureRecord;
import com.mmxlabs.scheduler.optimiser.cache.AbstractWriteLockable;
import com.mmxlabs.scheduler.optimiser.cache.IWriteLockable;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.exposures.OptimiserExposureRecords;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public class VoyagePlanRecord extends AbstractWriteLockable {

	private final boolean isCargo;

	private final VoyagePlan voyagePlan;
	private final IPortTimesRecord portTimesRecord;

	private @Nullable IAllocationAnnotation allocationAnnotation;
	private @Nullable ICargoValueAnnotation cargoValueAnnotation;
	private @Nullable ImmutableMap<IPortSlot, HeelValueRecord> heelValueRecords = ImmutableMap.of();
	private ImmutableMap<IPortSlot, SlotHeelVolumeRecord> heelVolumeRecords = ImmutableMap.of();
	private ImmutableMap<IPortSlot, OptimiserExposureRecords> exposureRecords = ImmutableMap.of();

	// TODO: These should be linked
	private ImmutableMap<IPortSlot, CapacityRecord> capacityRecords = ImmutableMap.of();
	private ImmutableMap<IPortSlot, LatenessRecord> latenessRecords = ImmutableMap.of();
	private ImmutableMap<IPortSlot, IdleRecord> idleRecords = ImmutableMap.of();
	private ImmutableSet<IPortSlot> lateSlots = ImmutableSet.of();

	private long profitAndLoss;

	public static class LatenessRecord {

		public long weightedLateness;
		public int latenessWithFlex;
		public int latenessWithoutFlex;
		public @Nullable Interval interval;
	}

	public static class IdleRecord {

		public int violatingIdleHours;
		public long weightedIdleCost;
	}

	public static class CapacityRecord {

		public List<CapacityViolationType> capacityViolations = new ArrayList<>();
		public Map<CapacityViolationType, Long> capacityViolationVolumes = new EnumMap<>(CapacityViolationType.class);
	}

	public static class SlotHeelVolumeRecord {

		public @Nullable HeelRecord portHeelRecord;
		// Records after this port slot
		public @Nullable HeelRecord travelHeelRecord;
		public @Nullable HeelRecord idleHeelRecord;
		public boolean forcedCooldown;

		@Override
		public boolean equals(final @Nullable Object obj) {
			if (obj == this) {
				return true;
			}

			if (obj instanceof SlotHeelVolumeRecord) {
				final SlotHeelVolumeRecord other = (SlotHeelVolumeRecord) obj;
				if (forcedCooldown != other.forcedCooldown) {
					return false;
				}
				if (!Objects.equals(portHeelRecord, other.portHeelRecord)) {
					return false;
				}
				if (!Objects.equals(travelHeelRecord, other.travelHeelRecord)) {
					return false;
				}
				if (!Objects.equals(idleHeelRecord, other.idleHeelRecord)) {
					return false;
				}

				return true;
			}

			return false;
		}
	}

	public VoyagePlanRecord(final VoyagePlan voyagePlan, final IPortTimesRecord ptr, final Map<IPortSlot, HeelValueRecord> heelValueRecords, final long profitAndLoss) {
		this.voyagePlan = voyagePlan;
		this.portTimesRecord = ptr;
		this.heelValueRecords = ImmutableMap.copyOf(heelValueRecords);
		this.profitAndLoss = profitAndLoss;
		this.isCargo = false;

		IWriteLockable.writeLock(voyagePlan);
		IWriteLockable.writeLock(ptr);
	}

	public VoyagePlanRecord(final VoyagePlan voyagePlan, final IPortTimesRecord ptr) {
		this.voyagePlan = voyagePlan;
		this.portTimesRecord = ptr;
		if (ptr instanceof IAllocationAnnotation) {
			this.allocationAnnotation = (IAllocationAnnotation) ptr;
		}
		if (ptr instanceof ICargoValueAnnotation) {
			this.cargoValueAnnotation = (ICargoValueAnnotation) ptr;
			this.profitAndLoss = cargoValueAnnotation.getTotalProfitAndLoss();
		}
		this.isCargo = true;

		IWriteLockable.writeLock(voyagePlan);
		IWriteLockable.writeLock(ptr);

	}

	public VoyagePlan getVoyagePlan() {
		return voyagePlan;
	}

	public IPortTimesRecord getPortTimesRecord() {
		return portTimesRecord;
	}

	public @Nullable IAllocationAnnotation getAllocationAnnotation() {
		return allocationAnnotation;
	}

	public @Nullable ICargoValueAnnotation getCargoValueAnnotation() {
		return cargoValueAnnotation;
	}

	public HeelValueRecord getHeelValueRecord(final IPortSlot slot) {

		 return heelValueRecords.getOrDefault(slot, new HeelValueRecord(0, 0, 0, 0));
	}

	public long getProfitAndLoss() {
		return profitAndLoss;
	}

	public boolean isCargoRecord() {
		return isCargo;
	}

	public Map<IPortSlot, SlotHeelVolumeRecord> getHeelVolumeRecords() {
		return heelVolumeRecords;
	}

	public @Nullable HeelRecord getNextTravelHeelRecord(final IPortSlot portSlot) {
		return getHeelVolumeRecords().get(portSlot).travelHeelRecord;
	}

	public @Nullable HeelRecord getNextIdleHeelRecord(final IPortSlot portSlot) {
		return getHeelVolumeRecords().get(portSlot).idleHeelRecord;
	}

	public @Nullable HeelRecord getPortHeelRecord(final IPortSlot portSlot) {
		return getHeelVolumeRecords().get(portSlot).portHeelRecord;
	}

	public void setHeelVolumeRecords(final Map<IPortSlot, SlotHeelVolumeRecord> heelVolumeRecords) {
		checkWritable();

		this.heelVolumeRecords = ImmutableMap.copyOf(heelVolumeRecords);
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof VoyagePlanRecord other) {

			if (isCargo != other.isCargo) {
				return false;
			}
			if (profitAndLoss != other.profitAndLoss) {
				return false;
			}
			if (!Objects.equals(portTimesRecord, other.portTimesRecord)) {
				return false;
			}
			if (!Objects.equals(allocationAnnotation, other.allocationAnnotation)) {
				return false;
			}
			if (!Objects.equals(cargoValueAnnotation, other.cargoValueAnnotation)) {
				return false;
			}
			if (!Objects.equals(heelValueRecords, other.heelValueRecords)) {
				return false;
			}
			if (!Objects.equals(heelVolumeRecords, other.heelVolumeRecords)) {
				return false;
			}
			if (!Objects.equals(voyagePlan, other.voyagePlan)) {
				return false;
			}

			return true;
		}
		return false;
	}

	/**
	 * Record lateness. Slot will only be marked as late if latenessWithFlex is greater than zero.
	 * 
	 * @param portSlot
	 * @param weightedLateness
	 * @param interval
	 * @param latenessWithFlex
	 * @param latenessWithoutFlex
	 */
	public void addLateness(final IPortSlot portSlot, final long weightedLateness, final Interval interval, final int latenessWithFlex, final int latenessWithoutFlex) {
		checkWritable();
		final LatenessRecord record = new LatenessRecord();
		record.weightedLateness = weightedLateness;
		record.latenessWithFlex = latenessWithFlex;
		record.latenessWithoutFlex = latenessWithoutFlex;
		record.interval = interval;

		latenessRecords = ImmutableMap.<IPortSlot, LatenessRecord> builder().putAll(latenessRecords).put(portSlot, record).build();

		if (latenessWithFlex > 0) {
			lateSlots = ImmutableSet.<IPortSlot> builder().addAll(lateSlots).add(portSlot).build();

		}
	}

	public void addCapacityViolation(final IPortSlot portSlot, final CapacityViolationType cvt, final long volume) {
		checkWritable();

		if (!capacityRecords.containsKey(portSlot)) {
			capacityRecords = ImmutableMap.<IPortSlot, CapacityRecord> builder().putAll(capacityRecords).put(portSlot, new CapacityRecord()).build();
		}

		final CapacityRecord record = capacityRecords.get(portSlot);
		record.capacityViolations.add(cvt);
		record.capacityViolationVolumes.put(cvt, volume);
	}

	public long getCapacityViolationVolume(final CapacityViolationType violation, final IPortSlot portSlot) {
		final CapacityRecord record = capacityRecords.getOrDefault(portSlot, new CapacityRecord());
		return record.capacityViolationVolumes.getOrDefault(violation, 0L);
	}

	public List<CapacityViolationType> getCapacityViolations(final IPortSlot portSlot) {
		final CapacityRecord record = capacityRecords.getOrDefault(portSlot, new CapacityRecord());

		return record.capacityViolations;
	}

	public @Nullable Interval getLatenessInterval(final IPortSlot slot) {
		return latenessRecords.getOrDefault(slot, new LatenessRecord()).interval;
	}

	public int getLatenessWithFlex(final IPortSlot slot) {
		return latenessRecords.getOrDefault(slot, new LatenessRecord()).latenessWithFlex;
	}

	public int getLatenessWithoutFlex(final IPortSlot slot) {
		return latenessRecords.getOrDefault(slot, new LatenessRecord()).latenessWithoutFlex;
	}

	public long getWeightedLatenessCost(final IPortSlot slot) {
		return latenessRecords.getOrDefault(slot, new LatenessRecord()).weightedLateness;
	}

	public @Nullable LatenessRecord getLatenessRecord(final IPortSlot slot) {
		return latenessRecords.get(slot);
	}

	public ImmutableSet<IPortSlot> getLateSlotsSet() {
		return lateSlots;
	}

	public void addIdleHoursViolation(final IPortSlot portSlot, final int violatingHours) {
		checkWritable();

		if (!idleRecords.containsKey(portSlot)) {
			idleRecords = ImmutableMap.<IPortSlot, IdleRecord> builder().putAll(idleRecords).put(portSlot, new IdleRecord()).build();
		}

		idleRecords.get(portSlot).violatingIdleHours = violatingHours;
	}

	public long getIdleTimeViolationHours(final IPortSlot portSlot) {
		return idleRecords.getOrDefault(portSlot, new IdleRecord()).violatingIdleHours;
	}

	public void addIdleWeightedCost(final IPortSlot portSlot, final long cost) {
		checkWritable();

		if (!idleRecords.containsKey(portSlot)) {
			idleRecords = ImmutableMap.<IPortSlot, IdleRecord> builder().putAll(idleRecords).put(portSlot, new IdleRecord()).build();
		}

		idleRecords.get(portSlot).weightedIdleCost = cost;
	}

	public long getIdleWeightedCost(final IPortSlot portSlot) {
		return idleRecords.getOrDefault(portSlot, new IdleRecord()).weightedIdleCost;
	}

	public @Nullable VoyageDetails getVoyageDetailsFrom(final IPortSlot portSlot) {

		for (final IDetailsSequenceElement e : voyagePlan.getSequence())
			if (e instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) e;
				if (voyageDetails.getOptions().getFromPortSlot() == portSlot) {
					return voyageDetails;
				}
			}
		return null;
	}

	public void addPortExposureRecord(final IPortSlot slot, final List<BasicExposureRecord> records) {

		checkWritable();

		if (exposureRecords.get(slot) != null) {
			final OptimiserExposureRecords existingRecords = exposureRecords.get(slot);
			existingRecords.records.addAll(records);
		} else {
			final OptimiserExposureRecords newRecords = new OptimiserExposureRecords();
			newRecords.records.addAll(records);
			exposureRecords = ImmutableMap.<IPortSlot, OptimiserExposureRecords> builder().putAll(exposureRecords).put(slot, newRecords).build();
		}
	}

	public @Nullable OptimiserExposureRecords getPortExposureRecord(final IPortSlot slot) {
		return exposureRecords.get(slot);
	}

	public ImmutableMap<IPortSlot, OptimiserExposureRecords> getPortExposureRecords() {
		return exposureRecords;
	}
}
