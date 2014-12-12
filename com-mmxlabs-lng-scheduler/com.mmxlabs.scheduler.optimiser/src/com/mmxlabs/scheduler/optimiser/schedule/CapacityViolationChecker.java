/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil.CargoType;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl.CapacityAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl.CapacityEntry;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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

	@Inject
	private IPortSlotProvider portSlotProvider;

	/**
	 * TODO: Break out into separate class Calculate the various capacity violation check - min/max load & discharge volumes, max heel, vessel capacity and cooldown. Note the {@link IVolumeAllocator}
	 * and {@link LNGVoyageCalculator} generally feed into these checks.
	 * 
	 * @param sequences
	 * @param scheduledSequences
	 * @param allocations
	 * @param annotatedSolution
	 */
	public void calculateCapacityViolations(final ISequences sequences, final ScheduledSequences scheduledSequences, final Map<VoyagePlan, IAllocationAnnotation> allocations,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		// Loop over all sequences
		for (final ScheduledSequence scheduledSequence : scheduledSequences) {
			final IResource resource = scheduledSequence.getResource();
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(resource);

			// Determine vessel capacity
			long vesselCapacityInM3 = Long.MAX_VALUE;
			if (nominatedVessel != null) {
				vesselCapacityInM3 = nominatedVessel.getCargoCapacity();
			} else if (vesselAvailability != null) {
				vesselCapacityInM3 = vesselAvailability.getVessel().getCargoCapacity();
			}

			// Forced cooldown volumes are stored on the VoyageDetails, so record the last one for use in the next iteration so we can record the cooldown at the port
			boolean isForcedCooldown = false;
			long remainingHeelInM3 = 0;
			PortDetails lastHeelDetails = null;
			// Loop over all voyage plans in turn. We Use the VoyagePlan directly to obtain allocation annotations
			for (final VoyagePlan voyagePlan : scheduledSequence.getVoyagePlans()) {
				// Get the allocation annotation if this is a cargo, otherwise this will be null
				final IAllocationAnnotation allocationAnnotation = scheduledSequences.getAllocations().get(voyagePlan);

				ILoadOption buy = null;
				IDischargeOption sell = null;
				for (int idx = 0; idx < voyagePlan.getSequence().length - 1; ++idx) {
					final IDetailsSequenceElement e = voyagePlan.getSequence()[idx];
					if (e instanceof PortDetails) {
						// Cargo based checks
						final PortDetails portDetails = (PortDetails) e;

						final IPortSlot portSlot = portDetails.getOptions().getPortSlot();
						// If this is a cargo, get the load or discharge volume
						final long volumeInM3 = allocationAnnotation == null ? 0 : allocationAnnotation.getSlotVolumeInM3(portSlot);

						if (portSlot instanceof ILoadOption) {
							final ILoadOption loadOption = (ILoadOption) portSlot;

							buy = loadOption;

							if (volumeInM3 > loadOption.getMaxLoadVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_LOAD, volumeInM3 - loadOption.getMaxLoadVolume(), scheduledSequences);
							} else if (volumeInM3 < loadOption.getMinLoadVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MIN_LOAD, loadOption.getMinLoadVolume() - volumeInM3, scheduledSequences);
							}

							if (remainingHeelInM3 + volumeInM3 > vesselCapacityInM3) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.VESSEL_CAPACITY, remainingHeelInM3 + volumeInM3 - vesselCapacityInM3,
										scheduledSequences);
							}
							// Reset heel as we have now taken it into account
							remainingHeelInM3 = 0;
						} else if (portSlot instanceof IDischargeOption) {
							final IDischargeOption dischargeOption = (IDischargeOption) portSlot;

							sell = dischargeOption;

							if (volumeInM3 > dischargeOption.getMaxDischargeVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_DISCHARGE, volumeInM3 - dischargeOption.getMaxDischargeVolume(),
										scheduledSequences);
							} else if (volumeInM3 < dischargeOption.getMinDischargeVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MIN_DISCHARGE, dischargeOption.getMinDischargeVolume() - volumeInM3,
										scheduledSequences);
							}

							if (volumeInM3 > vesselCapacityInM3) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.VESSEL_CAPACITY, volumeInM3 - vesselCapacityInM3, scheduledSequences);
							}

						} else {

							// TODO: Remaining heel is assumed to be lost at this point

							// Non-cargo code path, if this is the first entry, grab the initial heel value
							if (idx == 0) {
								long initialHeelInM3 = 0;
								if (portSlot instanceof IHeelOptionsPortSlot) {
									final IHeelOptions heelOptions = ((IHeelOptionsPortSlot) portSlot).getHeelOptions();
									if (heelOptions != null) {
										initialHeelInM3 = heelOptions.getHeelLimit();
									}
								}

								if (remainingHeelInM3 > 0) {
									// addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.LOST_HEEL, remainingHeelInM3);
									// Reset as we do not handle pushing remaining heel into a vessel event voyage
									remainingHeelInM3 = 0;
								}

								// Check the voyage requirements are within the heel level
								if (voyagePlan.getLNGFuelVolume() + remainingHeelInM3 > initialHeelInM3) {
									addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_HEEL, voyagePlan.getLNGFuelVolume() + remainingHeelInM3
											- initialHeelInM3, scheduledSequences);
								}
							}
						}

						// Check for forced cooldowns
						if (isForcedCooldown) {
							// Record the previously detected forced cooldown problem
							addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.FORCED_COOLDOWN, 0, scheduledSequences);
						}
						// Reset, do not re-record cooldown problems
						isForcedCooldown = false;

					} else if (e instanceof VoyageDetails) {
						final VoyageDetails voyageDetails = (VoyageDetails) e;

						final boolean shouldBeCold = voyageDetails.getOptions().shouldBeCold() && !voyageDetails.getOptions().getAllowCooldown();
						if (shouldBeCold && voyageDetails.isCooldownPerformed()) {
							// Despite requiring to be cold, we still have some cooldown volume. Mark as > 0
							isForcedCooldown = true;
						}

					} else {
						// Unexpected element type
						assert false;
					}
				}

				// TODO: Handle multiple load/discharge case
				if (buy != null && sell != null) {
					CargoType cargoType = CargoTypeUtil.getCargoType(buy, sell);
					if (cargoType == CargoType.SHIPPED) {
						lastHeelDetails = (PortDetails) voyagePlan.getSequence()[voyagePlan.getSequence().length - 1];
					} else {
						lastHeelDetails = (PortDetails) voyagePlan.getSequence()[1];
					}
				} else {
					lastHeelDetails = (PortDetails) voyagePlan.getSequence()[voyagePlan.getSequence().length - 1];
				}

				if (allocationAnnotation != null) {
					remainingHeelInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
				} else {
					remainingHeelInM3 = voyagePlan.getRemainingHeelInM3();
				}
			}

			// Handle anything left over at the end of the schedule
			if (lastHeelDetails != null) {
				final IPortSlot toPortSlot = lastHeelDetails.getOptions().getPortSlot();
				if (toPortSlot instanceof EndPortSlot) {
					final EndPortSlot endPortSlot = (EndPortSlot) toPortSlot;
					if (endPortSlot.isEndCold() && remainingHeelInM3 != endPortSlot.getTargetEndHeelInM3()) {
						// NOTE: This can be negative and as such does not feed into capacity component. Note negative values are also now deemed to be "unset"
						// addEntryToCapacityViolationAnnotation(annotatedSolution, lastHeelDetails, CapacityViolationType.LOST_HEEL, endPortSlot.getTargetEndHeelInM3() - remainingHeelInM3);

						// Alternative for period opt, just flag up if we expected heel but arrived with none. We could put a tolerance on the mismatch? E.g. only flag up if diff is greater than e.g.
						// 500? It can be very hard for optimiser to get exactly the right m3 value, and often a small difference makes no real impact on overall P&L.
						if (endPortSlot.isEndCold() && remainingHeelInM3 == 0) {
							addEntryToCapacityViolationAnnotation(annotatedSolution, lastHeelDetails, CapacityViolationType.LOST_HEEL, endPortSlot.getTargetEndHeelInM3() - remainingHeelInM3,
									scheduledSequences);
						}
					}
				}
				if (isForcedCooldown) {
					// Record the previously detected forced cooldown problem
					addEntryToCapacityViolationAnnotation(annotatedSolution, lastHeelDetails, CapacityViolationType.FORCED_COOLDOWN, 0, scheduledSequences);
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
	private void addEntryToCapacityViolationAnnotation(@Nullable final IAnnotatedSolution annotatedSolution, final PortDetails portDetails, final CapacityViolationType cvt, final long volume,
			ScheduledSequences scheduledSequences) {
		// Set port details entry
		scheduledSequences.addCapacityViolation(portDetails.getOptions().getPortSlot());

		if (annotatedSolution != null) {

			// Set annotation
			final List<ICapacityEntry> entries = new ArrayList<>(1);
			final ISequenceElement element = portSlotProvider.getElement(portDetails.getOptions().getPortSlot());
			// Add in existing entries as we will replace the annotation
			{
				final ICapacityAnnotation annotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, ICapacityAnnotation.class);
				if (annotation != null) {
					entries.addAll(annotation.getEntries());
				}
			}
			entries.add(new CapacityEntry(cvt, volume));
			if (!entries.isEmpty()) {
				final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
			}
		}
	}

}
