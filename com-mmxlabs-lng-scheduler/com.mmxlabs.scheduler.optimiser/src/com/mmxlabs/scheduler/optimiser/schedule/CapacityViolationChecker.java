package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
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
			final IAnnotatedSolution annotatedSolution) {

		// Loop over all sequences
		for (final ScheduledSequence scheduledSequence : scheduledSequences) {
			final IResource resource = scheduledSequence.getResource();
			final IVessel vessel = vesselProvider.getVessel(resource);
			final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(resource);

			long remainingHeelInM3 = 0;

			// Determine vessel capacity
			long vesselCapacityInM3 = Long.MAX_VALUE;
			if (nominatedVessel != null) {
				vesselCapacityInM3 = nominatedVessel.getCargoCapacity();
			} else if (vessel != null) {
				vesselCapacityInM3 = vessel.getCargoCapacity();
			}

			// Forced cooldown volumes are stored on the VoyageDetails, so record the last one for use in the next iteration so we can record the cooldown at the port
			long lastForcedCooldownVolume = -1;

			// Loop over all voyage plans in turn. We Use the VoyagePlan directly to obtain allocation annotations
			for (final VoyagePlan voyagePlan : scheduledSequence.getVoyagePlans()) {
				// Get the allocation annotation if this is a cargo, otherwise this will be null
				final IAllocationAnnotation allocationAnnotation = scheduledSequences.getAllocations().get(voyagePlan);

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

							if (volumeInM3 > loadOption.getMaxLoadVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_LOAD, volumeInM3 - loadOption.getMaxLoadVolume());
							} else if (volumeInM3 < loadOption.getMinLoadVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MIN_LOAD, loadOption.getMinLoadVolume() - volumeInM3);
							}

							// TODO: Consider remaining heel on first load!
							if (remainingHeelInM3 + volumeInM3 > vesselCapacityInM3) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.VESSEL_CAPACITY, remainingHeelInM3 + volumeInM3 - vesselCapacityInM3);
							}
							// Reset heel as we have now taken it into account
							remainingHeelInM3 = 0;
						} else if (portSlot instanceof IDischargeOption) {
							final IDischargeOption dischargeOption = (IDischargeOption) portSlot;
							if (volumeInM3 > dischargeOption.getMaxDischargeVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_DISCHARGE, volumeInM3 - dischargeOption.getMaxDischargeVolume());
							} else if (volumeInM3 < dischargeOption.getMinDischargeVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MIN_DISCHARGE, dischargeOption.getMinDischargeVolume() - volumeInM3);
							}

							if (volumeInM3 > vesselCapacityInM3) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.VESSEL_CAPACITY, volumeInM3 - vesselCapacityInM3);
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

								// Check the voyage requirements are within the heel level
								if (voyagePlan.getLNGFuelVolume() + voyagePlan.getRemainingHeelInM3() > initialHeelInM3) {
									addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_HEEL,
											voyagePlan.getLNGFuelVolume() + voyagePlan.getRemainingHeelInM3() - initialHeelInM3);
								}
							}
						}

						// Check for forced cooldowns
						if (lastForcedCooldownVolume > 0) {
							// Record the previously detected forced cooldown problem
							addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.FORCED_COOLDOWN, lastForcedCooldownVolume);
						}
						// Reset, do not re-record cooldown problems
						lastForcedCooldownVolume = -1;

					} else if (e instanceof VoyageDetails) {
						final VoyageDetails voyageDetails = (VoyageDetails) e;

						final boolean shouldBeCold = voyageDetails.getOptions().shouldBeCold();
						final long fuelConsumption = voyageDetails.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3);
						if (shouldBeCold && (fuelConsumption > 0)) {
							// Despite requring to be cold, we still have some cooldown volume. Record this volume so the next port visit can allocate it properly
							lastForcedCooldownVolume = fuelConsumption;
						}

					} else {
						// Unexpected element type
						assert false;
					}
				}

				// TODO: Enable once HEEL_TRACKING branch is merged in
				// remainingHeelInM3 = voyagePlan.getRemainingHeelInM3();
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
	private void addEntryToCapacityViolationAnnotation(final IAnnotatedSolution annotatedSolution, final PortDetails portDetails, final CapacityViolationType cvt, final long volume) {
		// Set port details entry
		portDetails.setCapacityViolation(cvt, volume);

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
