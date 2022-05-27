/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.evaluation.HeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord.SlotHeelVolumeRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyVolumeProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * This class performs capacity violation checks on the scenario as a whole after the volume allocator has run so we can use actual load and discharge volumes. This class calls
 * {@link PortDetails#setCapacityViolation(CapacityViolationType, long)} and set the element annotations.
 * 
 * It is currently (2013/12/03) expected to be used in the {@link ScheduleCalculator} class.
 * 
 * @author Simon Goodall
 * 
 */
public class CapacityViolationChecker {

	public static final Set<CapacityViolationType> HARD_CAPACITY_VIOLATIONS = EnumSet.of(//
			CapacityViolationType.MIN_DISCHARGE, //
			CapacityViolationType.MAX_DISCHARGE, //
			CapacityViolationType.MIN_LOAD, //
			CapacityViolationType.MAX_LOAD, //
			CapacityViolationType.VESSEL_CAPACITY);

	public static boolean isHardViolation(CapacityViolationType cvt) {
		return HARD_CAPACITY_VIOLATIONS.contains(cvt);
	}

	public static boolean isSoftViolation(CapacityViolationType cvt) {
		return !HARD_CAPACITY_VIOLATIONS.contains(cvt);
	}

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private ICounterPartyVolumeProvider counterPartyVolumeProvider;

	/**
	 * Calculate the various capacity violation check - min/max load & discharge volumes, max heel, vessel capacity and cooldown. Note the {@link IVolumeAllocator} and {@link LNGVoyageCalculator}
	 * generally feed into these checks.
	 * 
	 * @param sequences
	 * @param scheduledSequences
	 * @param allocations
	 * @param annotatedSolution
	 */
	public void calculateCapacityViolations(final IResource resource, final @NonNull VoyagePlanRecord voyagePlanRecord) {

		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(resource);

		// Determine vessel capacity
		long vesselCapacityInM3 = Long.MAX_VALUE;
		if (nominatedVessel != null) {
			vesselCapacityInM3 = nominatedVessel.getCargoCapacity();
		} else if (vesselCharter != null) {
			vesselCapacityInM3 = vesselCharter.getVessel().getCargoCapacity();
		}

		final boolean isShippedSequence = !(vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE //
				|| vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE);

		IPortTimesRecord portTimesRecord = voyagePlanRecord.getPortTimesRecord();
		for (final IPortSlot portSlot : portTimesRecord.getSlots()) {
			SlotHeelVolumeRecord slotHeelVolumeRecord = voyagePlanRecord.getHeelVolumeRecords().get(portSlot);
			final HeelRecord heelRecord = slotHeelVolumeRecord == null ? null : slotHeelVolumeRecord.portHeelRecord;

			if (isShippedSequence) {
				assert heelRecord != null;

				final long startHeelInM3 = heelRecord.getHeelAtStartInM3();
				final long endHeelInM3 = heelRecord.getHeelAtEndInM3();
				if (portSlot instanceof IHeelOptionConsumerPortSlot heelOptionConsumerPortSlot) {
					final @NonNull IHeelOptionConsumer heelOptionsConsumer = heelOptionConsumerPortSlot.getHeelOptionsConsumer();

					VesselTankState expectedTankState = heelOptionsConsumer.getExpectedTankState();
					if (expectedTankState == VesselTankState.EITHER) {
						// Convert EITHER based on heel.
						if (startHeelInM3 == 0) {
							expectedTankState = VesselTankState.MUST_BE_WARM;
						} else {
							expectedTankState = VesselTankState.MUST_BE_COLD;
						}
					}

					if (expectedTankState == VesselTankState.MUST_BE_WARM) {
						if (startHeelInM3 > 0) {
							addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.LOST_HEEL, startHeelInM3, voyagePlanRecord);
						}
					} else if (expectedTankState == VesselTankState.MUST_BE_COLD) {
						if (startHeelInM3 < heelOptionsConsumer.getMinimumHeelAcceptedInM3()) {
							// Only report if we don't also have a cooldown violation.
							if (!slotHeelVolumeRecord.forcedCooldown) {

								addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_HEEL, heelOptionsConsumer.getMinimumHeelAcceptedInM3() - startHeelInM3, voyagePlanRecord);
							}
						} else if (startHeelInM3 > heelOptionsConsumer.getMaximumHeelAcceptedInM3()) {
							addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MAX_HEEL, startHeelInM3 - heelOptionsConsumer.getMaximumHeelAcceptedInM3(), voyagePlanRecord);
						}
					}

				} else if (portSlot instanceof IVesselEventPortSlot) {
					if (portSlot.getPortType() == PortType.DryDock || portSlot.getPortType() == PortType.Maintenance) {
						// Any heel sent into a dry-dock or maintenance event is lost.
						if (startHeelInM3 > 0) {
							addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.LOST_HEEL, startHeelInM3, voyagePlanRecord);
						}
					}
				}
				if (portSlot instanceof ILoadSlot) {
					final ILoadSlot loadSlot = (ILoadSlot) portSlot;
					checkLoadOptionLimits(loadSlot, voyagePlanRecord);

				}
				if (portSlot instanceof IDischargeSlot) {
					final IDischargeSlot dischargeSlot = (IDischargeSlot) portSlot;
					checkDischargeOptionLimits(dischargeSlot, voyagePlanRecord);

				}
				if (portSlot instanceof IHeelOptionSupplierPortSlot) {
					final IHeelOptionSupplierPortSlot heelOptionSupplierPortSlot = (IHeelOptionSupplierPortSlot) portSlot;
					@NonNull
					final IHeelOptionSupplier heelOptionsSupplier = heelOptionSupplierPortSlot.getHeelOptionsSupplier();

					if (endHeelInM3 < heelOptionsSupplier.getMinimumHeelAvailableInM3()) {
						addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_HEEL, heelOptionsSupplier.getMinimumHeelAvailableInM3() - endHeelInM3, voyagePlanRecord);
					} else if (endHeelInM3 > heelOptionsSupplier.getMaximumHeelAvailableInM3()) {
						addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MAX_HEEL, endHeelInM3 - heelOptionsSupplier.getMaximumHeelAvailableInM3(), voyagePlanRecord);
					}
				}

				// Check start and end heels are within vessel limits
				if (startHeelInM3 > vesselCapacityInM3) {
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.VESSEL_CAPACITY, startHeelInM3 - vesselCapacityInM3, voyagePlanRecord);
				} else if (endHeelInM3 > vesselCapacityInM3) {
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.VESSEL_CAPACITY, endHeelInM3 - vesselCapacityInM3, voyagePlanRecord);
				}

				if (slotHeelVolumeRecord.forcedCooldown) {
					// Record the forced cooldown problem
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.FORCED_COOLDOWN, 0, voyagePlanRecord);
				}
			} else {
				// for DES purchase check that the load volume matches the min or the max load volume range
				// Should be a non-shipped cargo
				if (portSlot instanceof ILoadOption) {
					final ILoadOption loadOption = (ILoadOption) portSlot;
					checkLoadOptionLimits(loadOption, voyagePlanRecord);
				} else if (portSlot instanceof IDischargeOption) {
					final IDischargeOption dischargeOption = (IDischargeOption) portSlot;
					checkDischargeOptionLimits(dischargeOption, voyagePlanRecord);
				}
				if (nominatedVessel != null) {
					// TODO: Check physical vessel capacity (if applicable)

				}
			}
		}
	}

	private void checkDischargeOptionLimits(final @NonNull IDischargeOption portSlot, final @NonNull VoyagePlanRecord voyagePlanRecord) {
		final IAllocationAnnotation allocationAnnotation = voyagePlanRecord.getAllocationAnnotation();
		assert allocationAnnotation != null;
		// We use -1 for the CV as it does not matter - CV is used to convert between m3
		// and mmbtu, but here we are checking type first so we know conversion will not
		// happen.
		final int cargoCV = -1;
		if (portSlot.isVolumeSetInM3()) {
			final long commercialVolumeInM3 = allocationAnnotation.getCommercialSlotVolumeInM3(portSlot);
			if (commercialVolumeInM3 > portSlot.getMaxDischargeVolume(-1)) {
				addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MAX_DISCHARGE, commercialVolumeInM3 - portSlot.getMaxDischargeVolume(-1), voyagePlanRecord);
			} else if (commercialVolumeInM3 < portSlot.getMinDischargeVolume(-1)) {
				addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_DISCHARGE, portSlot.getMinDischargeVolume(-1) - commercialVolumeInM3, voyagePlanRecord);
			}
		} else {
			final long commercialVolumeInMMBTu = allocationAnnotation.getCommercialSlotVolumeInMMBTu(portSlot);
			// input is set in MMBTu
			if (commercialVolumeInMMBTu > portSlot.getMaxDischargeVolumeMMBTU(-1)) {
				final long violationInMMBTu = commercialVolumeInMMBTu - portSlot.getMaxDischargeVolumeMMBTU(-1);
				addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MAX_DISCHARGE, getViolationInM3(violationInMMBTu, cargoCV), voyagePlanRecord);
			} else if (commercialVolumeInMMBTu < portSlot.getMinDischargeVolumeMMBTU(-1)) {
				final long violationInMMBTu = portSlot.getMinDischargeVolumeMMBTU(-1) - commercialVolumeInMMBTu;
				addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_DISCHARGE, getViolationInM3(violationInMMBTu, cargoCV), voyagePlanRecord);
			}
		}
	}

	private void checkLoadOptionLimits(final @NonNull ILoadOption portSlot, final @NonNull VoyagePlanRecord voyagePlanRecord) {
		final IAllocationAnnotation allocationAnnotation = voyagePlanRecord.getAllocationAnnotation();
		assert allocationAnnotation != null;

		final int cargoCV = allocationAnnotation.getSlotCargoCV(portSlot);
		if (portSlot.isVolumeSetInM3()) {
			final long commercialVolumeInM3 = allocationAnnotation.getCommercialSlotVolumeInM3(portSlot);
			if (commercialVolumeInM3 > portSlot.getMaxLoadVolume()) {
				addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MAX_LOAD, commercialVolumeInM3 - portSlot.getMaxLoadVolume(), voyagePlanRecord);
			} else if (commercialVolumeInM3 < portSlot.getMinLoadVolume()) {
				addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_LOAD, portSlot.getMinLoadVolume() - commercialVolumeInM3, voyagePlanRecord);
			}
			if (counterPartyVolumeProvider.isCounterPartyVolume(portSlot)) {
				if (commercialVolumeInM3 > portSlot.getMaxLoadVolume()) {
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MAX_LOAD, commercialVolumeInM3 - portSlot.getMaxLoadVolume(), voyagePlanRecord);
				} else if (commercialVolumeInM3 < portSlot.getMinLoadVolume()) {
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_LOAD, portSlot.getMinLoadVolume() - commercialVolumeInM3, voyagePlanRecord);
				} else if (commercialVolumeInM3 < portSlot.getMaxLoadVolume() && commercialVolumeInM3 > portSlot.getMinLoadVolume()) {
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_LOAD, commercialVolumeInM3 - portSlot.getMaxLoadVolume(), voyagePlanRecord);
				}
			}
		} else {
			final long commercialVolumeInMMBTu = allocationAnnotation.getCommercialSlotVolumeInMMBTu(portSlot);
			// input is set in MMBTu
			if (commercialVolumeInMMBTu > portSlot.getMaxLoadVolumeMMBTU()) {
				final long violationInMMBTu = commercialVolumeInMMBTu - portSlot.getMaxLoadVolumeMMBTU();
				addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MAX_LOAD, getViolationInM3(violationInMMBTu, cargoCV), voyagePlanRecord);
			} else if (commercialVolumeInMMBTu < portSlot.getMinLoadVolumeMMBTU()) {
				final long violationInMMBTu = portSlot.getMinLoadVolumeMMBTU() - commercialVolumeInMMBTu;
				addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_LOAD, getViolationInM3(violationInMMBTu, cargoCV), voyagePlanRecord);
			}

			if (counterPartyVolumeProvider.isCounterPartyVolume(portSlot)) {
				if (commercialVolumeInMMBTu > portSlot.getMaxLoadVolumeMMBTU()) {
					final long violationInMMBTu = commercialVolumeInMMBTu - portSlot.getMaxLoadVolumeMMBTU();
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MAX_LOAD, getViolationInM3(violationInMMBTu, cargoCV), voyagePlanRecord);
				} else if (commercialVolumeInMMBTu < portSlot.getMinLoadVolumeMMBTU()) {
					final long violationInMMBTu = portSlot.getMinLoadVolumeMMBTU() - commercialVolumeInMMBTu;
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_LOAD, getViolationInM3(violationInMMBTu, cargoCV), voyagePlanRecord);
				} else if (commercialVolumeInMMBTu < portSlot.getMaxLoadVolumeMMBTU() && commercialVolumeInMMBTu > portSlot.getMinLoadVolumeMMBTU()) {
					final long violationInMMBTu = commercialVolumeInMMBTu - portSlot.getMaxLoadVolumeMMBTU();
					addEntryToCapacityViolationAnnotation(portSlot, CapacityViolationType.MIN_LOAD, getViolationInM3(violationInMMBTu, cargoCV), voyagePlanRecord);
				}
			}
		}
	}

	/**
	 * Populate the {@link PortDetails} capacity violation members. Set the element annotations on the {@link IAnnotatedSolution}
	 * 
	 * @param annotatedSolution
	 * @param portDetails
	 * @param cvt
	 * @param volume
	 */
	private void addEntryToCapacityViolationAnnotation(final @NonNull IPortSlot portSlot, final @NonNull CapacityViolationType cvt, final long volume,
			final @NonNull VoyagePlanRecord voyagePlanRecord) {

		// Cooldowns do not always have a volume associated with them
		if (cvt != CapacityViolationType.FORCED_COOLDOWN) {
			// Counter party volume violations can be negative
			if (!counterPartyVolumeProvider.isCounterPartyVolume(portSlot)) {
				// Finally check to see if volume is below threshold to ignore.
				if (volume < SchedulerConstants.CAPACITY_VIOLATION_THRESHOLD) {
					return;
				}
			}
		}

		// Set port details entry
		voyagePlanRecord.addCapacityViolation(portSlot, cvt, volume);
	}

	private long getViolationInM3(final long violationInMMBTu, final int cargoCV) {
		return cargoCV > 0 ? Calculator.convertMMBTuToM3(violationInMMBTu, cargoCV) : violationInMMBTu;

	}
}
