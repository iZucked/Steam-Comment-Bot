/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class VolumeAllocatedSequence {
	private final @NonNull IResource resource;
	private final @NonNull ISequence sequence;
	private final int startTime;
	private final List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> voyagePlans;

	// Cached Lookup data
	private final Map<IPortSlot, Integer> portSlotToTimeMap = new HashMap<>();
	private final Map<IPortSlot, VoyagePlan> portSlotToVoyagePlanMap = new HashMap<>();
	private final List<@NonNull IPortSlot> sequencePortSlots;

	private final @NonNull Set<@NonNull IPortSlot> lateSlots = new HashSet<>();

	public static class HeelRecord {
		private final long heelAtStart;
		private final long heelAtEnd;

		public HeelRecord(final long heelAtStart, final long heelAtEnd) {
			this.heelAtStart = heelAtStart;
			this.heelAtEnd = heelAtEnd;
		}

		public long getHeelAtStartInM3() {
			return heelAtStart;
		}

		public long getHeelAtEndInM3() {
			return heelAtEnd;
		}
	}

	public static class HeelValueRecord extends HeelRecord {
		private long heelRevenue;
		private long heelCost;

		public HeelValueRecord(final long heelAtStart, final long heelAtEnd) {
			super(heelAtStart, heelAtEnd);
		}

		public long getHeelRevenue() {
			return heelRevenue;
		}

		public void setHeelRevenue(final long heelRevenue) {
			this.heelRevenue = heelRevenue;
		}

		public long getHeelCost() {
			return heelCost;
		}

		public void setHeelCost(final long heelCost) {
			this.heelCost = heelCost;
		}
	}

	private static class SlotRecord {
		public int arrivalTime;
		public int visitDuration;
		public VoyagePlan voyagePlan;
		public IPortTimesRecord portTimesRecord;
		public int violatingIdleHours;
		public long weightedIdleCost;
		public long capacityViolationSum;
		public boolean forcedCooldown;
		public List<@NonNull CapacityViolationType> capacityViolations = new ArrayList<>();
		public Map<@NonNull CapacityViolationType, Long> capacityViolationVolumes = new EnumMap<>(CapacityViolationType.class);

		public long weightedLateness;
		public int latenessWithFlex;
		public int latenessWithoutFlex;
		public Interval interval;
		public VoyageDetails fromVoyageDetails;
		public VoyageDetails toVoyageDetails;
		public PortDetails portDetails;
		public HeelValueRecord portHeelRecord;
		// Records after this port slot
		public HeelRecord travelHeelRecord;
		public HeelRecord idleHeelRecord;
	}

	private final Map<IPortSlot, SlotRecord> slotRecords;
	private final IVesselAvailability vesselAvailability;

	/**
	 */
	public VolumeAllocatedSequence(final @NonNull IResource resource, final @NonNull IVesselAvailability vesselAvailability, final @NonNull ISequence sequence, final int startTime,
			final List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> voyagePlans) {
		super();
		this.vesselAvailability = vesselAvailability;
		this.sequence = sequence;
		this.startTime = startTime;
		this.voyagePlans = voyagePlans;
		this.resource = resource;
		this.sequencePortSlots = new ArrayList<>(sequence.size());

		this.slotRecords = new HashMap<>(sequence.size());

		// Build the lookup data!
		buildLookup();
	}

	protected @NonNull ISequence getSequence() {
		return sequence;
	}

	public @NonNull IResource getResource() {
		return resource;
	}

	public int getStartTime() {
		return startTime;
	}

	public List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> getVoyagePlans() {
		return voyagePlans;
	}

	public int getArrivalTime(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).arrivalTime;
	}

	public int getVisitDuration(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).visitDuration;
	}

	public VoyagePlan getVoyagePlan(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).voyagePlan;
	}

	public VoyageDetails getVoyageDetailsFrom(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).fromVoyageDetails;
	}

	public VoyageDetails getVoyageDetailsTo(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).toVoyageDetails;
	}

	public @Nullable HeelValueRecord getPortHeelRecord(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).portHeelRecord;
	}

	public @Nullable HeelRecord getNextTravelHeelRecord(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).travelHeelRecord;
	}

	public @Nullable HeelRecord getNextIdleHeelRecord(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).idleHeelRecord;
	}

	public PortDetails getPortDetails(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).portDetails;
	}

	public List<@NonNull IPortSlot> getSequenceSlots() {
		return sequencePortSlots;
	}

	public IPortTimesRecord getPortTimesRecord(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).portTimesRecord;
	}

	@Nullable
	public IAllocationAnnotation getAllocationAnnotation(final @NonNull IPortSlot portSlot) {
		final IPortTimesRecord portTimesRecord = getPortTimesRecord(portSlot);
		if (portTimesRecord instanceof IAllocationAnnotation) {
			return (IAllocationAnnotation) portTimesRecord;
		}
		return null;
	}

	public boolean isForcedCooldown(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).forcedCooldown;
	}

	public void addCapacityViolation(final @NonNull IPortSlot portSlot, @NonNull final CapacityViolationType cvt, final long volume) {

		@NonNull
		final SlotRecord record = getOrExceptionSlotRecord(portSlot);
		record.capacityViolationSum += 1;
		record.capacityViolations.add(cvt);
		record.capacityViolationVolumes.put(cvt, volume);
	}

	public long getCapacityViolationVolume(@NonNull final CapacityViolationType violation, @NonNull final IPortSlot portSlot) {
		final SlotRecord record = getOrExceptionSlotRecord(portSlot);

		return record.capacityViolationVolumes.getOrDefault(violation, 0L);
	}

	public long getCapacityViolationCount(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).capacityViolationSum;
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
	public void addLateness(final @NonNull IPortSlot portSlot, final long weightedLateness, final @NonNull Interval interval, final int latenessWithFlex, final int latenessWithoutFlex) {

		final SlotRecord record = getOrExceptionSlotRecord(portSlot);
		record.weightedLateness = weightedLateness;
		record.latenessWithFlex = latenessWithFlex;
		record.latenessWithoutFlex = latenessWithoutFlex;
		record.interval = interval;
		if (latenessWithFlex > 0) {
			lateSlots.add(portSlot);
		}
	}

	public @Nullable Interval getLatenessInterval(@NonNull final IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).interval;
	}

	public int getLatenessWithFlex(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).latenessWithFlex;
	}

	public int getLatenessWithoutFlex(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).latenessWithoutFlex;
	}

	public long getWeightedLatenessCost(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).weightedLateness;
	}

	public void addIdleHoursViolation(@NonNull final IPortSlot portSlot, final int violatingHours) {
		getOrExceptionSlotRecord(portSlot).violatingIdleHours = violatingHours;
	}

	public long getIdleTimeViolationHours(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).violatingIdleHours;
	}

	public void addIdleWeightedCost(@NonNull final IPortSlot slot, final long cost) {
		getOrExceptionSlotRecord(slot).weightedIdleCost = cost;
	}

	public long getIdleWeightedCost(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).weightedIdleCost;
	}

	public List<@NonNull CapacityViolationType> getCapacityViolations(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).capacityViolations;
	}

	public boolean isLateSlot(final @NonNull IPortSlot slot) {
		return lateSlots.contains(slot);
	}

	public @NonNull Set<@NonNull IPortSlot> getLateSlotsSet() {
		return lateSlots;
	}

	private @NonNull SlotRecord getOrCreateSlotRecord(final @NonNull IPortSlot slot) {
		SlotRecord allocation = slotRecords.get(slot);
		if (allocation == null) {
			allocation = new SlotRecord();
			slotRecords.put(slot, allocation);
		}
		return allocation;
	}

	private @NonNull SlotRecord getOrExceptionSlotRecord(final @NonNull IPortSlot slot) {
		final SlotRecord allocation = slotRecords.get(slot);
		if (allocation == null) {
			throw new IllegalArgumentException("Port slot is not part of this sequence");
		}
		return allocation;
	}

	/**
	 * Builds the lookup data. TODO: More efficient if this is done as the sequence data is built up!
	 */
	private void buildLookup() {
		final VoyagePlanIterator vpi = new VoyagePlanIterator(this);
		long currentHeelInM3 = -1L;
		boolean firstObject = true;
		boolean startOfPlan = true;
		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
		final boolean recordHeel = !(vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE);

		// Forced cooldown volumes are stored on the VoyageDetails, so record the last one for use in the next iteration so we can record the cooldown at the port
		boolean isForcedCooldown = false;
		long previousHeelInM3 = -1L;
		while (vpi.hasNextObject()) {
			previousHeelInM3 = currentHeelInM3;
			final boolean resetCurrentHeel = firstObject || vpi.nextObjectIsStartOfPlan();
			startOfPlan = firstObject || vpi.nextObjectIsStartOfPlan();
			firstObject = false;

			final Object e = vpi.nextObject();

			//
			final int currentTime = vpi.getCurrentTime();
			final VoyagePlan currentPlan = vpi.getCurrentPlan();

			assert currentPlan.getLNGFuelVolume() >= 0;
			assert currentPlan.getStartingHeelInM3() >= 0;
			assert currentPlan.getRemainingHeelInM3() >= 0;

			final long charterRatePerDay = currentPlan.getCharterInRatePerDay();
			if (resetCurrentHeel) {
				currentHeelInM3 = currentPlan.getStartingHeelInM3();
			}

			if (e instanceof PortDetails) {
				final PortDetails details = (PortDetails) e;
				final IPortSlot portSlot = details.getOptions().getPortSlot();

				// Set mapping between slot and time / current plan
				sequencePortSlots.add(portSlot);
				@NonNull
				final SlotRecord record = getOrCreateSlotRecord(portSlot);
				record.arrivalTime = currentTime;
				record.visitDuration = details.getOptions().getVisitDuration();
				record.voyagePlan = vpi.getCurrentPlan();
				record.portTimesRecord = vpi.getCurrentPortTimeRecord();

				if (recordHeel) {
					long startHeelInM3 = currentHeelInM3;
					long heelCost = 0L;
					long heelRevenue = 0L;
					if (portSlot instanceof IHeelOptionConsumerPortSlot) {
						long consumedHeelInM3 = currentHeelInM3;
						if (startOfPlan) {
							startHeelInM3 = previousHeelInM3;
							consumedHeelInM3 = previousHeelInM3;
						}

						final IHeelOptionConsumerPortSlot heelOptionConsumerPortSlot = (IHeelOptionConsumerPortSlot) portSlot;
						final IHeelOptionConsumer heelOptions = heelOptionConsumerPortSlot.getHeelOptionsConsumer();

						final int pricePerMMBTU = heelOptions.getHeelPriceCalculator().getHeelPrice(consumedHeelInM3, currentTime, portSlot.getPort());
						final int cv = details.getOptions().getCargoCVValue();

						// Only pay up to max heel. Excess is "free"
						final long heelInMMBTU = Calculator.convertM3ToMMBTu(Math.min(consumedHeelInM3, heelOptions.getMaximumHeelAcceptedInM3()), cv);
						final long thisHeelRevenue = Calculator.costFromConsumption(heelInMMBTU, pricePerMMBTU);
						heelRevenue += thisHeelRevenue;
					}
					if (portSlot.getPortType() == PortType.Load) {
						final IAllocationAnnotation allocationAnnotation = (IAllocationAnnotation) record.portTimesRecord;// scheduledSequence.getAllocationAnnotation(portSlot);

						assert allocationAnnotation.getStartHeelVolumeInM3() >= 0;
						assert allocationAnnotation.getFuelVolumeInM3() >= 0;
						assert allocationAnnotation.getRemainingHeelVolumeInM3() >= 0;

						assert allocationAnnotation.getStartHeelVolumeInM3() == currentPlan.getStartingHeelInM3();
						assert allocationAnnotation.getFuelVolumeInM3() == currentPlan.getLNGFuelVolume();

						// TODO: Probably should be physical here and then ignore the port BOG.
						currentHeelInM3 += allocationAnnotation.getPhysicalSlotVolumeInM3(portSlot);
					} else if (portSlot.getPortType() == PortType.Discharge) {
						final IAllocationAnnotation allocationAnnotation = (IAllocationAnnotation) record.portTimesRecord;

						assert allocationAnnotation.getStartHeelVolumeInM3() >= 0;
						assert allocationAnnotation.getFuelVolumeInM3() >= 0;
						assert allocationAnnotation.getRemainingHeelVolumeInM3() >= 0;

						assert allocationAnnotation.getStartHeelVolumeInM3() == currentPlan.getStartingHeelInM3();
						assert allocationAnnotation.getFuelVolumeInM3() == currentPlan.getLNGFuelVolume();

						currentHeelInM3 -= allocationAnnotation.getPhysicalSlotVolumeInM3(portSlot);
						currentHeelInM3 -= details.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3);
					} else if (portSlot instanceof IHeelOptionSupplierPortSlot) {
						// If the portSlot is a supplier, then the previous heel should be used as the starting heel rather than the voyage plan start.
						if (previousHeelInM3 != -1L) {
							startHeelInM3 = previousHeelInM3;
						}

						final IHeelOptionSupplierPortSlot heelOptionSupplierPortSlot = (IHeelOptionSupplierPortSlot) portSlot;
						final IHeelOptionSupplier heelOptions = heelOptionSupplierPortSlot.getHeelOptionsSupplier();

						final int pricePerMMBTU = heelOptions.getHeelPriceCalculator().getHeelPrice(currentHeelInM3, currentTime, portSlot.getPort());
						final int cv = details.getOptions().getCargoCVValue();

						final long heelInMMBTU = Calculator.convertM3ToMMBTu(currentHeelInM3, cv);
						final long thisHeelCost = Calculator.costFromConsumption(heelInMMBTU, pricePerMMBTU);
						heelCost += thisHeelCost;
					}
					assert currentHeelInM3 + VoyagePlanner.ROUNDING_EPSILON >= 0;

					final long endHeelInM3 = currentHeelInM3;
					record.portHeelRecord = new HeelValueRecord(startHeelInM3, endHeelInM3);
					record.portHeelRecord.heelCost = heelCost;
					record.portHeelRecord.heelRevenue = heelRevenue;
				}
				record.forcedCooldown = isForcedCooldown;
				// Reset, do not re-record cooldown problems
				isForcedCooldown = false;

				record.portDetails = details;

			} else if (e instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) e;
				@NonNull
				final VoyageOptions options = voyageDetails.getOptions();
				// Cooldown performed even though not permitted
				if (!options.getAllowCooldown() && voyageDetails.isCooldownPerformed()) {
					isForcedCooldown = true;
				}
				{
					final SlotRecord record = getOrCreateSlotRecord(options.getFromPortSlot());
					record.fromVoyageDetails = voyageDetails;
				}
				{
					final SlotRecord record = getOrCreateSlotRecord(options.getToPortSlot());
					record.toVoyageDetails = voyageDetails;
				}

				if (recordHeel) {
					assert currentHeelInM3 >= 0;
					final long startHeelInM3 = currentHeelInM3;

					for (final FuelComponent fuel : FuelComponent.getTravelFuelComponents()) {
						final long consumption = voyageDetails.getFuelConsumption(fuel, FuelUnit.M3) + voyageDetails.getRouteAdditionalConsumption(fuel, FuelUnit.M3);
						if (FuelComponent.isLNGFuelComponent(fuel)) {
							currentHeelInM3 -= consumption;
						}
					}
					final long endOfTravelHeel = currentHeelInM3;

					final long consumption = voyageDetails.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3) + voyageDetails.getRouteAdditionalConsumption(FuelComponent.IdleNBO, FuelUnit.M3);
					currentHeelInM3 -= consumption;

					final SlotRecord record = getOrCreateSlotRecord(options.getFromPortSlot());

					record.travelHeelRecord = new HeelRecord(startHeelInM3, endOfTravelHeel);
					record.idleHeelRecord = new HeelRecord(endOfTravelHeel, currentHeelInM3);

				}
			}
		}
	}

	public final boolean isEqual(@NonNull final VolumeAllocatedSequence other) {

		return this.startTime == other.startTime //
				&& Objects.deepEquals(this.resource, other.resource) //
				&& Objects.deepEquals(this.portSlotToTimeMap, other.portSlotToTimeMap) //
				&& Objects.deepEquals(this.portSlotToVoyagePlanMap, other.portSlotToVoyagePlanMap) //
				&& Objects.deepEquals(this.sequencePortSlots, other.sequencePortSlots) //
				&& Objects.deepEquals(this.sequence, other.sequence) //
				&& Objects.deepEquals(this.voyagePlans, other.voyagePlans);
	}
}