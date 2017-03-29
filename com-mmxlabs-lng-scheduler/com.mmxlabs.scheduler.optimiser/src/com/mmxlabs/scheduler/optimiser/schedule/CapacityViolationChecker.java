/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
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
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence.HeelRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * This class performs capacity violation checks on the scenario as a whole after the volume allocator has run so we can use actual load and discharge volumes. This class calls
 * {@link PortDetails#setCapacityViolation(CapacityViolationType, long)} and set the element annotations.
 * 
 * It is currently (2013/12/03) expected to be used in the {@link ScheduleCalculator} class.
 * 
 * TODO: This needs to be updated once the Heel Tracking branch is merged in to consider the remaining heel from the previous voyage.
 * 
 * @author Simon Goodall
 * 
 */
public class CapacityViolationChecker {

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IVesselProvider vesselProvider;

	/**
	 * Calculate the various capacity violation check - min/max load & discharge volumes, max heel, vessel capacity and cooldown. Note the {@link IVolumeAllocator} and {@link LNGVoyageCalculator}
	 * generally feed into these checks.
	 * 
	 * @param sequences
	 * @param scheduledSequences
	 * @param allocations
	 * @param annotatedSolution
	 */
	public void calculateCapacityViolations(final @NonNull VolumeAllocatedSequence volumeAllocatedSequence, @Nullable final IAnnotatedSolution annotatedSolution) {

		final IResource resource = volumeAllocatedSequence.getResource();
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(resource);

		// Determine vessel capacity
		long vesselCapacityInM3 = Long.MAX_VALUE;
		if (nominatedVessel != null) {
			vesselCapacityInM3 = nominatedVessel.getCargoCapacity();
		} else if (vesselAvailability != null) {
			vesselCapacityInM3 = vesselAvailability.getVessel().getCargoCapacity();
		}

		final boolean isShippedSequence = !(vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE);

		for (final IPortSlot portSlot : volumeAllocatedSequence.getSequenceSlots()) {
			final HeelRecord heelRecord = volumeAllocatedSequence.getPortHeelRecord(portSlot);

			if (isShippedSequence) {
				assert heelRecord != null;

				final long startHeelInM3 = heelRecord.getHeelAtStartInM3();
				final long endHeelInM3 = heelRecord.getHeelAtEndInM3();
				if (portSlot instanceof IHeelOptionConsumerPortSlot) {
					final IHeelOptionConsumerPortSlot heelOptionConsumerPortSlot = (IHeelOptionConsumerPortSlot) portSlot;

					@NonNull
					final IHeelOptionConsumer heelOptionsConsumer = heelOptionConsumerPortSlot.getHeelOptionsConsumer();

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
							addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.LOST_HEEL, startHeelInM3, volumeAllocatedSequence);
						}
					} else if (expectedTankState == VesselTankState.MUST_BE_COLD) {
						if (startHeelInM3 < heelOptionsConsumer.getMinimumHeelAcceptedInM3()) {
							// Only report if we don't also have a cooldown violation.
							if (!volumeAllocatedSequence.isForcedCooldown(portSlot)) {

								addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MIN_HEEL, heelOptionsConsumer.getMinimumHeelAcceptedInM3() - startHeelInM3,
										volumeAllocatedSequence);
							}
						} else if (startHeelInM3 > heelOptionsConsumer.getMaximumHeelAcceptedInM3()) {
							addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MAX_HEEL, startHeelInM3 - heelOptionsConsumer.getMaximumHeelAcceptedInM3(),
									volumeAllocatedSequence);
						}
					}

				} else if (portSlot instanceof IVesselEventPortSlot) {
					if (portSlot.getPortType() == PortType.DryDock || portSlot.getPortType() == PortType.Maintenance) {
						// Any heel sent into a dry-dock or maintenance event is lost.
						if (startHeelInM3 > 0) {
							addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.LOST_HEEL, startHeelInM3, volumeAllocatedSequence);
						}
					}
				}
				if (portSlot instanceof ILoadSlot) {
					final ILoadSlot loadSlot = (ILoadSlot) portSlot;
					checkLoadOptionLimits(loadSlot, volumeAllocatedSequence, annotatedSolution);
					final IAllocationAnnotation allocationAnnotation = volumeAllocatedSequence.getAllocationAnnotation(portSlot);
					assert allocationAnnotation != null;
				}
				if (portSlot instanceof IDischargeSlot) {
					final IDischargeSlot dischargeSlot = (IDischargeSlot) portSlot;
					checkDischargeOptionLimits(dischargeSlot, volumeAllocatedSequence, annotatedSolution);

				}
				if (portSlot instanceof IHeelOptionSupplierPortSlot) {
					final IHeelOptionSupplierPortSlot heelOptionSupplierPortSlot = (IHeelOptionSupplierPortSlot) portSlot;
					@NonNull
					final IHeelOptionSupplier heelOptionsSupplier = heelOptionSupplierPortSlot.getHeelOptionsSupplier();

					if (endHeelInM3 < heelOptionsSupplier.getMinimumHeelAvailableInM3()) {
						addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MIN_HEEL, heelOptionsSupplier.getMinimumHeelAvailableInM3() - endHeelInM3,
								volumeAllocatedSequence);
					} else if (endHeelInM3 > heelOptionsSupplier.getMaximumHeelAvailableInM3()) {
						addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MAX_HEEL, endHeelInM3 - heelOptionsSupplier.getMaximumHeelAvailableInM3(),
								volumeAllocatedSequence);
					}
				}

				// Check start and end heels are within vessel limits
				if (startHeelInM3 > vesselCapacityInM3) {
					addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.VESSEL_CAPACITY, startHeelInM3 - vesselCapacityInM3, volumeAllocatedSequence);
				} else if (endHeelInM3 > vesselCapacityInM3) {
					addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.VESSEL_CAPACITY, endHeelInM3 - vesselCapacityInM3, volumeAllocatedSequence);
				}

				if (volumeAllocatedSequence.isForcedCooldown(portSlot)) {
					// Record the forced cooldown problem
					addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.FORCED_COOLDOWN, 0, volumeAllocatedSequence);
				}
			} else {
				// Should be a non-shipped cargo
				if (portSlot instanceof ILoadOption) {
					final ILoadOption loadOption = (ILoadOption) portSlot;
					checkLoadOptionLimits(loadOption, volumeAllocatedSequence, annotatedSolution);
				} else if (portSlot instanceof IDischargeOption) {
					final IDischargeOption dischargeOption = (IDischargeOption) portSlot;
					checkDischargeOptionLimits(dischargeOption, volumeAllocatedSequence, annotatedSolution);
				}
				if (nominatedVessel != null) {
					// TODO: Check physical vessel capacity (if applicable)

				}
			}
		}

	}

	private void checkDischargeOptionLimits(final @NonNull IDischargeOption portSlot, final @NonNull VolumeAllocatedSequence volumeAllocatedSequence,
			final @Nullable IAnnotatedSolution annotatedSolution) {

		final IAllocationAnnotation allocationAnnotation = volumeAllocatedSequence.getAllocationAnnotation(portSlot);
		assert allocationAnnotation != null;

		// We use -1 for the CV as it does not matter - CV is used to convert between m3 and mmbtu, but here we are checking type first so we know conversion will not happen.
		final int cargoCV = -1;
		if (portSlot.isVolumeSetInM3()) {
			final long commercialVolumeInM3 = allocationAnnotation.getCommercialSlotVolumeInM3(portSlot);
			if (commercialVolumeInM3 > portSlot.getMaxDischargeVolume(-1)) {
				addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MAX_DISCHARGE, commercialVolumeInM3 - portSlot.getMaxDischargeVolume(-1),
						volumeAllocatedSequence);
			} else if (commercialVolumeInM3 < portSlot.getMinDischargeVolume(-1)) {
				addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MIN_DISCHARGE, portSlot.getMinDischargeVolume(-1) - commercialVolumeInM3,
						volumeAllocatedSequence);
			}
		} else {
			final long commercialVolumeInMMBTu = allocationAnnotation.getCommercialSlotVolumeInMMBTu(portSlot);
			// input is set in MMBTu
			if (commercialVolumeInMMBTu > portSlot.getMaxDischargeVolumeMMBTU(-1)) {
				final long violationInMMBTu = commercialVolumeInMMBTu - portSlot.getMaxDischargeVolumeMMBTU(-1);
				addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MAX_DISCHARGE, getViolationInM3(violationInMMBTu, cargoCV), volumeAllocatedSequence);
			} else if (commercialVolumeInMMBTu < portSlot.getMinDischargeVolumeMMBTU(-1)) {
				final long violationInMMBTu = portSlot.getMinDischargeVolumeMMBTU(-1) - commercialVolumeInMMBTu;
				addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MIN_DISCHARGE, getViolationInM3(violationInMMBTu, cargoCV), volumeAllocatedSequence);
			}
		}
	}

	private void checkLoadOptionLimits(final @NonNull ILoadOption portSlot, final @NonNull VolumeAllocatedSequence volumeAllocatedSequence, final @Nullable IAnnotatedSolution annotatedSolution) {

		final IAllocationAnnotation allocationAnnotation = volumeAllocatedSequence.getAllocationAnnotation(portSlot);
		assert allocationAnnotation != null;

		final int cargoCV = allocationAnnotation.getSlotCargoCV(portSlot);
		if (portSlot.isVolumeSetInM3()) {
			final long commercialVolumeInM3 = allocationAnnotation.getCommercialSlotVolumeInM3(portSlot);
			if (commercialVolumeInM3 > portSlot.getMaxLoadVolume()) {
				addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MAX_LOAD, commercialVolumeInM3 - portSlot.getMaxLoadVolume(), volumeAllocatedSequence);
			} else if (commercialVolumeInM3 < portSlot.getMinLoadVolume()) {
				addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MIN_LOAD, portSlot.getMinLoadVolume() - commercialVolumeInM3, volumeAllocatedSequence);
			}
		} else {
			final long commercialVolumeInMMBTu = allocationAnnotation.getCommercialSlotVolumeInMMBTu(portSlot);
			// input is set in MMBTu
			if (commercialVolumeInMMBTu > portSlot.getMaxLoadVolumeMMBTU()) {
				final long violationInMMBTu = commercialVolumeInMMBTu - portSlot.getMaxLoadVolumeMMBTU();
				addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MAX_LOAD, getViolationInM3(violationInMMBTu, cargoCV), volumeAllocatedSequence);
			} else if (commercialVolumeInMMBTu < portSlot.getMinLoadVolumeMMBTU()) {
				final long violationInMMBTu = portSlot.getMinLoadVolumeMMBTU() - commercialVolumeInMMBTu;
				addEntryToCapacityViolationAnnotation(annotatedSolution, portSlot, CapacityViolationType.MIN_LOAD, getViolationInM3(violationInMMBTu, cargoCV), volumeAllocatedSequence);
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
	private void addEntryToCapacityViolationAnnotation(@Nullable final IAnnotatedSolution annotatedSolution, final @NonNull IPortSlot portSlot, final @NonNull CapacityViolationType cvt,
			final long volume, final @NonNull VolumeAllocatedSequence volumeAllocatedSequence) {
		// Set port details entry
		volumeAllocatedSequence.addCapacityViolation(portSlot, cvt, volume);
	}

	private long getViolationInM3(final long violationInMMBTu, final int cargoCV) {
		return cargoCV > 0 ? Calculator.convertMMBTuToM3(violationInMMBTu, cargoCV) : violationInMMBTu;

	}
}
