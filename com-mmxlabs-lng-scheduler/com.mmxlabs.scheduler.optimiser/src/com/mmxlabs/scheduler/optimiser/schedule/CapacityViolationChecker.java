/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil.SimpleCargoType;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl.CapacityAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl.CapacityEntry;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
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

		// Forced cooldown volumes are stored on the VoyageDetails, so record the last one for use in the next iteration so we can record the cooldown at the port
		boolean isForcedCooldown = false;
		long remainingHeelInM3 = 0;
		PortDetails lastHeelDetails = null;
		// Loop over all voyage plans in turn. We Use the VoyagePlan directly to obtain allocation annotations
		for (final Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> entry : volumeAllocatedSequence.getVoyagePlans()) {
			final VoyagePlan voyagePlan = entry.getFirst();
			final IPortTimesRecord portTimesRecord = entry.getThird();
			// Get the allocation annotation if this is a cargo, otherwise this will be null
			final IAllocationAnnotation allocationAnnotation = (portTimesRecord instanceof IAllocationAnnotation) ? (IAllocationAnnotation) portTimesRecord : null;

			ILoadOption buy = null;
			IDischargeOption sell = null;
			int offset = voyagePlan.isIgnoreEnd() ? 1 : 0;
			for (int idx = 0; idx < voyagePlan.getSequence().length - offset; ++idx) {
				final IDetailsSequenceElement e = voyagePlan.getSequence()[idx];
				if (e instanceof PortDetails) {
					// Cargo based checks
					final PortDetails portDetails = (PortDetails) e;

					final IPortSlot portSlot = portDetails.getOptions().getPortSlot();
					// If this is a cargo, get the load or discharge volume
					final long volumeInM3 = allocationAnnotation == null ? 0 : allocationAnnotation.getCommercialSlotVolumeInM3(portSlot);
					final long volumeInMMBTu = allocationAnnotation == null ? 0 : allocationAnnotation.getSlotVolumeInMMBTu(portSlot);

					if (portSlot instanceof ILoadOption) {
						final ILoadOption loadOption = (ILoadOption) portSlot;

						buy = loadOption;

						if (loadOption.isVolumeSetInM3()) {
							if (volumeInM3 > loadOption.getMaxLoadVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_LOAD, volumeInM3 - loadOption.getMaxLoadVolume(),
										volumeAllocatedSequence);
							} else if (volumeInM3 < loadOption.getMinLoadVolume()) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MIN_LOAD, loadOption.getMinLoadVolume() - volumeInM3,
										volumeAllocatedSequence);
							}

							if (remainingHeelInM3 + volumeInM3 > vesselCapacityInM3) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.VESSEL_CAPACITY, remainingHeelInM3 + volumeInM3 - vesselCapacityInM3,
										volumeAllocatedSequence);
							}
						} else {
							// input is set in MMBTu
							assert allocationAnnotation != null;
							int cargoCV = allocationAnnotation.getSlotCargoCV(portSlot);
							if (volumeInMMBTu > loadOption.getMaxLoadVolumeMMBTU()) {
								long violationInMMBTu = volumeInMMBTu - loadOption.getMaxLoadVolumeMMBTU();
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_LOAD, getViolationInM3(violationInMMBTu, cargoCV),
										volumeAllocatedSequence);
							} else if (volumeInMMBTu < loadOption.getMinLoadVolumeMMBTU()) {
								long violationInMMBTu = loadOption.getMinLoadVolumeMMBTU() - volumeInMMBTu;
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MIN_LOAD, getViolationInM3(violationInMMBTu, cargoCV),
										volumeAllocatedSequence);
							}
							if (cargoCV > 0) {
								if (volumeInM3 > vesselCapacityInM3) {
									addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.VESSEL_CAPACITY, volumeInM3 - vesselCapacityInM3,
											volumeAllocatedSequence);
								}
							}
						}
						// Reset heel as we have now taken it into account
						remainingHeelInM3 = 0;
					} else if (portSlot instanceof IDischargeOption) {
						final IDischargeOption dischargeOption = (IDischargeOption) portSlot;

						sell = dischargeOption;

						// We use -1 for the CV as it does not matter - CV is used to convert between m3 and mmbtu, but here we are checking type first so we know conversion will not happen.

						if (dischargeOption.isVolumeSetInM3()) {
							if (volumeInM3 > dischargeOption.getMaxDischargeVolume(-1)) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_DISCHARGE, volumeInM3 - dischargeOption.getMaxDischargeVolume(-1),
										volumeAllocatedSequence);
							} else if (volumeInM3 < dischargeOption.getMinDischargeVolume(-1)) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MIN_DISCHARGE, dischargeOption.getMinDischargeVolume(-1) - volumeInM3,
										volumeAllocatedSequence);
							}

							if (volumeInM3 > vesselCapacityInM3) {
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.VESSEL_CAPACITY, volumeInM3 - vesselCapacityInM3, volumeAllocatedSequence);
							}
						} else {
							// volumes set in MMBTu
							assert allocationAnnotation != null;
							int cargoCV = allocationAnnotation.getSlotCargoCV(portSlot);
							if (volumeInMMBTu > dischargeOption.getMaxDischargeVolumeMMBTU(-1)) {
								long violationInMMBTu = volumeInMMBTu - dischargeOption.getMaxDischargeVolumeMMBTU(-1);
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_DISCHARGE, getViolationInM3(violationInMMBTu, cargoCV),
										volumeAllocatedSequence);
							} else if (volumeInMMBTu < dischargeOption.getMinDischargeVolumeMMBTU(-1)) {
								long violationInMMBTu = dischargeOption.getMinDischargeVolumeMMBTU(-1) - volumeInMMBTu;
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MIN_DISCHARGE, getViolationInM3(violationInMMBTu, cargoCV),
										volumeAllocatedSequence);
							}

							if (cargoCV > 0) {
								if (volumeInM3 > vesselCapacityInM3) {
									addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.VESSEL_CAPACITY, volumeInM3 - vesselCapacityInM3,
											volumeAllocatedSequence);
								}
							}

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
								addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.MAX_HEEL,
										voyagePlan.getLNGFuelVolume() + remainingHeelInM3 - initialHeelInM3, volumeAllocatedSequence);
							}
						}
					}

					// Check for forced cooldowns
					if (isForcedCooldown) {
						// Record the previously detected forced cooldown problem
						addEntryToCapacityViolationAnnotation(annotatedSolution, portDetails, CapacityViolationType.FORCED_COOLDOWN, 0, volumeAllocatedSequence);
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
				final SimpleCargoType cargoType = CargoTypeUtil.getSimpleCargoType(buy, sell);
				if (cargoType == SimpleCargoType.SHIPPED) {
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
								volumeAllocatedSequence);
					}
				}
			}
			if (isForcedCooldown) {
				// Record the previously detected forced cooldown problem
				addEntryToCapacityViolationAnnotation(annotatedSolution, lastHeelDetails, CapacityViolationType.FORCED_COOLDOWN, 0, volumeAllocatedSequence);
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
	private void addEntryToCapacityViolationAnnotation(@Nullable final IAnnotatedSolution annotatedSolution, final @NonNull PortDetails portDetails, final @NonNull CapacityViolationType cvt,
			final long volume, final @NonNull VolumeAllocatedSequence volumeAllocatedSequence) {
		// Set port details entry
		volumeAllocatedSequence.addCapacityViolation(portDetails.getOptions().getPortSlot(),cvt);

		if (annotatedSolution != null) {

			// Set annotation
			final List<@NonNull ICapacityEntry> entries = new ArrayList<>(1);
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

	private long getViolationInM3(long violationInMMBTu, int cargoCV) {
		return cargoCV > 0 ? Calculator.convertMMBTuToM3(violationInMMBTu, cargoCV) : violationInMMBTu;

	}
}
