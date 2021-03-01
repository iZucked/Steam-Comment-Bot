/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.SplitCharterOutVesselEventEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord.SlotHeelVolumeRecord;
import com.mmxlabs.scheduler.optimiser.exposures.ExposuresCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanner;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.CapacityViolationChecker;
import com.mmxlabs.scheduler.optimiser.schedule.IdleTimeChecker;
import com.mmxlabs.scheduler.optimiser.schedule.LatenessChecker;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public class DefaultVoyagePlanEvaluator implements IVoyagePlanEvaluator {

	@Inject
	private IVolumeAllocator volumeAllocator;

	@Inject
	private IVoyagePlanner voyagePlanner;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private ExposuresCalculator exposuresCalculator;

	@Inject
	private LatenessChecker latenessChecker;

	@Inject
	private CapacityViolationChecker capacityChecker;

	@Inject
	private IdleTimeChecker idleTimeChecker;

	@Override
	public ScheduledVoyagePlanResult evaluateNonShipped(final IResource resource, final IVesselAvailability vesselAvailability, //
			final IPortTimesRecord portTimesRecord, //
			final boolean keepDetails, //
			@Nullable final IAnnotatedSolution annotatedSolution) {

		final Pair<VoyagePlan, IAllocationAnnotation> p = voyagePlanner.makeNonShippedVoyagePlan(resource, portTimesRecord, true, annotatedSolution);
		final VoyagePlan vp = p.getFirst();
		final IAllocationAnnotation allocationAnnotation = p.getSecond();
		assert allocationAnnotation != null;

		ICargoValueAnnotation cargoValueAnnotation = null;
		if (allocationAnnotation instanceof ICargoValueAnnotation) {
			// We may have already computed P&L
			cargoValueAnnotation = (ICargoValueAnnotation) allocationAnnotation;
		}
		if (cargoValueAnnotation == null) {
			final Pair<CargoValueAnnotation, Long> pp = entityValueCalculator.evaluate(vp, allocationAnnotation, vesselAvailability, null, annotatedSolution);
			cargoValueAnnotation = pp.getFirst();
		}
		assert cargoValueAnnotation != null;
		final long[] metrics = new long[MetricType.values().length];
		metrics[MetricType.PNL.ordinal()] += cargoValueAnnotation.getTotalProfitAndLoss();
		metrics[MetricType.CAPACITY.ordinal()] += vp.getViolationsCount();
		metrics[MetricType.COMPULSARY_SLOT.ordinal()] = 0; // Always zero

		final VoyagePlanRecord vpr = new VoyagePlanRecord(vp, cargoValueAnnotation);
		vpr.setHeelVolumeRecords(new HashMap<>());

		final ScheduledVoyagePlanResult result = new ScheduledVoyagePlanResult();

		final List<Integer> times = new LinkedList<>();
		for (final IPortSlot s : portTimesRecord.getSlots()) {
			times.add(portTimesRecord.getSlotTime(s));
		}

		result.arrivalTimes = times;
		IPortSlot returnSlot = portTimesRecord.getReturnSlot();
		if (returnSlot != null) {
			result.returnTime = portTimesRecord.getSlotTime(returnSlot);
		}

		latenessChecker.calculateLateness(resource, vpr, annotatedSolution);
		for (final IPortSlot slot : portTimesRecord.getSlots()) {
			metrics[MetricType.LATENESS.ordinal()] += vpr.getLatenessWithFlex(slot);
		}
		capacityChecker.calculateCapacityViolations(resource, vpr);
		idleTimeChecker.calculateIdleTime(vpr, annotatedSolution);

		// Populating exposures record
		if (annotatedSolution != null) {
			for (final IPortSlot portSlot : cargoValueAnnotation.getSlots()) {
				if (!cargoValueAnnotation.getSlotEntity(portSlot).isThirdparty()) {
					vpr.addPortExposureRecord(portSlot, exposuresCalculator.calculateExposures(cargoValueAnnotation, portSlot));
				}
			}
		}

		result.metrics = metrics;

		if (keepDetails) {
			result.voyagePlans.add(vpr);
		}

		return result;
	}

	@Override
	public List<ScheduledVoyagePlanResult> evaluateRoundTrip(final IResource resource, //
			final IVesselAvailability vesselAvailability, //
			final ICharterCostCalculator charterCostCalculator, //
			final IPortTimesRecord initialPortTimesRecord, //
			final boolean returnAll, //
			final boolean keepDetails, //
			@Nullable final IAnnotatedSolution annotatedSolution) {

		// Some defaults for a round trip
		final int vesselStartTime = 0;
		final PreviousHeelRecord previousHeelRecord = new PreviousHeelRecord();
		final boolean lastPlan = true;

		return evaluateShipped(resource, vesselAvailability, charterCostCalculator, vesselStartTime, null, previousHeelRecord, initialPortTimesRecord, lastPlan, returnAll, keepDetails, annotatedSolution);
	}

	@Override
	public List<ScheduledVoyagePlanResult> evaluateShipped(final IResource resource, //
			final IVesselAvailability vesselAvailability, //
			final ICharterCostCalculator charterCostCalculator, //
			final int vesselStartTime, //
			@Nullable IPort firstLoadPort, 
			final PreviousHeelRecord previousHeelRecord, //
			final IPortTimesRecord initialPortTimesRecord, //
			final boolean lastPlan, //
			final boolean returnAll, //
			final boolean keepDetails, //
			@Nullable final IAnnotatedSolution annotatedSolution) {

		// Only expect a single result here
		final List<ScheduledVoyagePlanResult> results = new LinkedList<>();

		final Consumer<List<@NonNull Pair<VoyagePlan, IPortTimesRecord>>> hook = vpList -> {

			final long[] metrics = new long[MetricType.values().length];

			final ScheduledVoyagePlanResult result = new ScheduledVoyagePlanResult();

			final Pair<VoyagePlan, IPortTimesRecord> lastPP = vpList.get(vpList.size() - 1);

			// Rolling values, updated with each voyage plan
			int lastHeelPrice = previousHeelRecord.lastHeelPricePerMMBTU;
			PreviousHeelRecord planPreviousHeelRecord = previousHeelRecord;
			for (final Pair<VoyagePlan, IPortTimesRecord> pp : vpList) {
				final VoyagePlan vp = pp.getFirst();
				final IPortTimesRecord ptr = pp.getSecond();

				if (lastPlan && lastPP == pp) {
					vp.setIgnoreEnd(false);
				}
				// Some code paths may have already calculated volume and/or P&L
				// This happens if e.g. GCO or charter length has kicked in
				IAllocationAnnotation allocationAnnotation = null;
				ICargoValueAnnotation cargoValueAnnotation = null;
				if (pp.getSecond() instanceof ICargoValueAnnotation) {
					cargoValueAnnotation = (ICargoValueAnnotation) pp.getSecond();
					allocationAnnotation = cargoValueAnnotation;
				}
				if (allocationAnnotation == null && pp.getSecond() instanceof IAllocationAnnotation) {
					allocationAnnotation = (IAllocationAnnotation) pp.getSecond();
				}
				if (allocationAnnotation == null) {
					allocationAnnotation = volumeAllocator.allocate(vesselAvailability, vp, ptr, annotatedSolution);
				}
				if (cargoValueAnnotation == null && allocationAnnotation != null) {
					final Pair<CargoValueAnnotation, Long> p = entityValueCalculator.evaluate(vp, allocationAnnotation, vesselAvailability, null, annotatedSolution);
					cargoValueAnnotation = p.getFirst();
				}

				VoyagePlanRecord vpr = null;
				if (cargoValueAnnotation != null) {
					metrics[MetricType.PNL.ordinal()] += cargoValueAnnotation.getTotalProfitAndLoss();
					pp.setSecond(cargoValueAnnotation);
					final int numSlots = cargoValueAnnotation.getSlots().size();
					final IPortSlot lastSlot = cargoValueAnnotation.getSlots().get(numSlots - 1);
					result.lastCV = cargoValueAnnotation.getSlotCargoCV(lastSlot);

					result.lastHeelPricePerMMBTU = cargoValueAnnotation.getSlotPricePerMMBTu(lastSlot);
					lastHeelPrice = result.lastHeelPricePerMMBTU;
					result.lastHeelVolumeInM3 = cargoValueAnnotation.getRemainingHeelVolumeInM3();
					vpr = new VoyagePlanRecord(vp, cargoValueAnnotation);

					// Populating exposures record
					if (annotatedSolution != null) {
						for (final IPortSlot portSlot : cargoValueAnnotation.getSlots()) {
							if (!cargoValueAnnotation.getSlotEntity(portSlot).isThirdparty()) {
								vpr.addPortExposureRecord(portSlot, exposuresCalculator.calculateExposures(cargoValueAnnotation, portSlot));
							}
						}
					}
				}
				final Map<IPortSlot, SlotHeelVolumeRecord> heelVolumeRecords = new HashMap<>();

				final boolean recordHeel = !(vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE
						|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE);

				final boolean endedWithForcedCooldown = computeHeelVolumeRecords(planPreviousHeelRecord, vp, allocationAnnotation, heelVolumeRecords, recordHeel);

				// Non-cargo codepath
				if (allocationAnnotation == null) {
					assert vpr == null;
					final Pair<Map<IPortSlot, HeelValueRecord>, @NonNull Long> p = entityValueCalculator.evaluateNonCargoPlan(vp, ptr, vesselAvailability, vesselStartTime, ptr.getFirstSlotTime(),
							 firstLoadPort, lastHeelPrice, heelVolumeRecords, annotatedSolution);

					result.lastHeelVolumeInM3 = vp.getRemainingHeelInM3();
					metrics[MetricType.PNL.ordinal()] += p.getSecond();

					// Lookup last heel price
					for (final IPortSlot slot : ptr.getSlots()) {
						if (slot instanceof IHeelOptionConsumerPortSlot) {
							// Heel consumed, so reset the price
							result.lastHeelPricePerMMBTU = 0;
							result.lastCV = 0;
						}
						if (slot instanceof IHeelOptionSupplierPortSlot) {
							final IHeelOptionSupplierPortSlot iHeelOptionSupplierPortSlot = (IHeelOptionSupplierPortSlot) slot;
							final Map<IPortSlot, HeelValueRecord> first = p.getFirst();
							final HeelValueRecord heelValueRecord = first.get(slot);
							if (heelValueRecord != null) {
								result.lastHeelPricePerMMBTU = heelValueRecord.getCostUnitPrice();
							}
							result.lastCV = iHeelOptionSupplierPortSlot.getHeelOptionsSupplier().getHeelCVValue();
						}
					}
					lastHeelPrice = result.lastHeelPricePerMMBTU;
					vpr = new VoyagePlanRecord(vp, ptr, p.getFirst(), p.getSecond());
				}
				result.forcedCooldown = endedWithForcedCooldown;
				vpr.forcedCooldown = endedWithForcedCooldown;
				vpr.setHeelVolumeRecords(heelVolumeRecords);
				result.voyagePlans.add(vpr);

				metrics[MetricType.CAPACITY.ordinal()] += vp.getViolationsCount();
				metrics[MetricType.COMPULSARY_SLOT.ordinal()] = 0; // Always zero

				final List<Integer> times = new LinkedList<>();
				for (final IPortSlot s : initialPortTimesRecord.getSlots()) {
					times.add(initialPortTimesRecord.getSlotTime(s));
				}

				result.arrivalTimes = times;
				IPortSlot returnSlot = initialPortTimesRecord.getReturnSlot();
				if (returnSlot != null) {
					result.returnTime = initialPortTimesRecord.getSlotTime(returnSlot);
				} else {
					IPortSlot s = initialPortTimesRecord.getSlots().get(initialPortTimesRecord.getSlots().size() - 1);
					result.returnTime = initialPortTimesRecord.getSlotTime(s);
				}

				final PreviousHeelRecord planHeelRecord = new PreviousHeelRecord();
				planHeelRecord.heelVolumeInM3 = result.lastHeelVolumeInM3;
				planHeelRecord.lastHeelPricePerMMBTU = result.lastHeelPricePerMMBTU;
				planHeelRecord.lastCV = result.lastCV;
				planHeelRecord.forcedCooldown = endedWithForcedCooldown;

				planPreviousHeelRecord = planHeelRecord;

				latenessChecker.calculateLateness(resource, vpr, annotatedSolution);
				for (final IPortSlot slot : ptr.getSlots()) {
					metrics[MetricType.LATENESS.ordinal()] += vpr.getLatenessWithFlex(slot);
				}

				capacityChecker.calculateCapacityViolations(resource, vpr);
				idleTimeChecker.calculateIdleTime(vpr, annotatedSolution);

			}

			result.metrics = metrics;

			if (!keepDetails) {
				result.voyagePlans.clear();

			}

			results.add(result);
		};

		final long[] heelVolumeRangeInM3 = new long[2];
		final IPortSlot thisPortSlot = initialPortTimesRecord.getFirstSlot();

		// Sanity check for round trip cargoes.
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			heelVolumeRangeInM3[0] = vesselAvailability.getVessel().getSafetyHeel();
			heelVolumeRangeInM3[1] = vesselAvailability.getVessel().getSafetyHeel();
		} else if (thisPortSlot instanceof IHeelOptionSupplierPortSlot) {
			final IHeelOptionSupplierPortSlot supplier = (IHeelOptionSupplierPortSlot) thisPortSlot;
			heelVolumeRangeInM3[0] = supplier.getHeelOptionsSupplier().getMinimumHeelAvailableInM3();
			heelVolumeRangeInM3[1] = supplier.getHeelOptionsSupplier().getMaximumHeelAvailableInM3();
			assert heelVolumeRangeInM3[0] >= 0;
			assert heelVolumeRangeInM3[1] >= 0;
		} else if (thisPortSlot.getPortType() == PortType.DryDock || thisPortSlot.getPortType() == PortType.Maintenance) {
			// No heel coming out of these events.
			heelVolumeRangeInM3[0] = 0;
			heelVolumeRangeInM3[1] = 0;
		} else {
			heelVolumeRangeInM3[0] = previousHeelRecord.heelVolumeInM3;
			heelVolumeRangeInM3[1] = previousHeelRecord.heelVolumeInM3;
		}

		voyagePlanner.makeShippedVoyagePlans(resource, charterCostCalculator, initialPortTimesRecord, heelVolumeRangeInM3, previousHeelRecord.lastCV, lastPlan, returnAll, true, hook,
				annotatedSolution);

		return results;

	}

	private boolean computeHeelVolumeRecords(final PreviousHeelRecord previousHeelRecord, final VoyagePlan vp, @Nullable final IAllocationAnnotation allocationAnnotation,
			final Map<IPortSlot, SlotHeelVolumeRecord> heelRecords, final boolean recordHeel) {

		// Forced cooldown volumes are stored on the VoyageDetails, so record the last
		// one for use in the next iteration so we can record the cooldown at the port
		boolean isForcedCooldown = previousHeelRecord.forcedCooldown;

		long currentHeelInM3 = vp.getStartingHeelInM3();

		long totalVoyageBOGInM3 = 0;
		for (int i = 0; i < vp.getSequence().length; ++i) {
			final Object e = vp.getSequence()[i];

			final boolean startOfPlan = i == 0;

			assert vp.getLNGFuelVolume() >= 0;
			assert vp.getStartingHeelInM3() >= 0;
			assert vp.getRemainingHeelInM3() >= 0;

			if (allocationAnnotation != null) {
				assert allocationAnnotation.getStartHeelVolumeInM3() >= 0;
				assert allocationAnnotation.getRemainingHeelVolumeInM3() >= 0;
			}

			if (e instanceof PortDetails) {
				final PortDetails details = (PortDetails) e;
				final IPortSlot portSlot = details.getOptions().getPortSlot();

				// Check to see if we need to include the last object.
				// vp#IsIgnoreEnd is not reliable at this point as we are still building up the
				// data
				if (i == vp.getSequence().length - 1 && !startOfPlan) {
					if (portSlot.getPortType() != PortType.Round_Trip_Cargo_End) {
						continue;
					}
				}

				final @NonNull SlotHeelVolumeRecord record = heelRecords.computeIfAbsent(portSlot, k -> new SlotHeelVolumeRecord());

				if (recordHeel) {
					long startHeelInM3 = currentHeelInM3;

					if (portSlot instanceof StartPortSlot) {
						// Start port slots do not have any prior state. The start and end heel level is
						// the same
						startHeelInM3 = vp.getStartingHeelInM3();
						currentHeelInM3 = startHeelInM3;
					} else {
						if (startOfPlan && portSlot instanceof IHeelOptionConsumerPortSlot) {
							// If this is the first slot, we consume the heel from the previous plan.
							// The current level is the start of the voyage plan.
							startHeelInM3 = previousHeelRecord.heelVolumeInM3;
						}
						if (portSlot instanceof IHeelOptionSupplierPortSlot) {
							// We currently only expect a supplier to be at the start of the plan.

							// The start heel is either plan start or if we are also a consumer then
							// previous plan heel.
							assert startOfPlan;
							if (portSlot instanceof SplitCharterOutVesselEventEndPortSlot) {
								// Special case for redirected charter outs. We reach here when we start a new
								// plan (after the charter out) and the heel needs to be pulled from the
								// previous record. We still want the heel level from going into the charter out
								// event. (Only because that is how we have always tracked it. Could also have
								// zero heel across event).
								startHeelInM3 = previousHeelRecord.heelVolumeInM3;
							}
							// The end heel is the start of the plan.
							currentHeelInM3 = vp.getStartingHeelInM3();
						}

						if (portSlot.getPortType() == PortType.Load) {
							assert allocationAnnotation.getStartHeelVolumeInM3() >= 0;
							assert allocationAnnotation.getFuelVolumeInM3() >= 0;
							assert allocationAnnotation.getRemainingHeelVolumeInM3() >= 0;

							assert allocationAnnotation.getStartHeelVolumeInM3() == vp.getStartingHeelInM3();
							assert allocationAnnotation.getFuelVolumeInM3() == vp.getLNGFuelVolume();

							// TODO: Probably should be physical here and then ignore the port BOG.
							currentHeelInM3 += allocationAnnotation.getPhysicalSlotVolumeInM3(portSlot);
						} else if (portSlot.getPortType() == PortType.Discharge) {
							assert allocationAnnotation.getStartHeelVolumeInM3() >= 0;
							assert allocationAnnotation.getFuelVolumeInM3() >= 0;
							assert allocationAnnotation.getRemainingHeelVolumeInM3() >= 0;

							assert allocationAnnotation.getStartHeelVolumeInM3() == vp.getStartingHeelInM3();
							assert allocationAnnotation.getFuelVolumeInM3() == vp.getLNGFuelVolume();

							currentHeelInM3 -= allocationAnnotation.getPhysicalSlotVolumeInM3(portSlot);
							currentHeelInM3 -= details.getFuelConsumption(LNGFuelKeys.NBO_In_m3);
						}
					}

					assert currentHeelInM3 + IVoyagePlanner.ROUNDING_EPSILON >= 0;

					final long endHeelInM3 = currentHeelInM3;
					record.portHeelRecord = new HeelRecord(startHeelInM3, endHeelInM3);

					totalVoyageBOGInM3 += details.getFuelConsumption(LNGFuelKeys.NBO_In_m3);

				}
				// TODO: This really needs some way to pass into the next voyage plan.
				// Maybe part of last heel record?
				record.forcedCooldown = isForcedCooldown;
				// Reset, do not re-record cooldown problems
				isForcedCooldown = false;
			} else if (e instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) e;
				final VoyageOptions options = voyageDetails.getOptions();
				// Cooldown performed even though not permitted
				if (!options.getAllowCooldown() && voyageDetails.isCooldownPerformed()) {
					// Record as forced cooldown (unless this is on a charter length, then it is not
					// a "forced" violation)
					if (!(options.getFromPortSlot() instanceof IGeneratedCharterLengthEventPortSlot)) {
						isForcedCooldown = true;
					}
				}
				if (recordHeel) {
					assert currentHeelInM3 + IVoyagePlanner.ROUNDING_EPSILON >= 0;
					final long startHeelInM3 = currentHeelInM3;

					for (final FuelKey fuel : LNGFuelKeys.Travel_LNG_In_m3) {
						assert fuel != null;
						final long consumptionInM3 = voyageDetails.getFuelConsumption(fuel) + voyageDetails.getRouteAdditionalConsumption(fuel);
						currentHeelInM3 -= consumptionInM3;
						totalVoyageBOGInM3 += consumptionInM3;
					}
					final long endOfTravelHeel = currentHeelInM3;

					final long consumption = voyageDetails.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3) //
							+ voyageDetails.getRouteAdditionalConsumption(LNGFuelKeys.IdleNBO_In_m3);
					currentHeelInM3 -= consumption;
					totalVoyageBOGInM3 += consumption;

					final @NonNull SlotHeelVolumeRecord record = heelRecords.computeIfAbsent(options.getFromPortSlot(), k -> new SlotHeelVolumeRecord());
					record.travelHeelRecord = new HeelRecord(startHeelInM3, endOfTravelHeel);
					record.idleHeelRecord = new HeelRecord(endOfTravelHeel, currentHeelInM3);
					assert currentHeelInM3 + IVoyagePlanner.ROUNDING_EPSILON >= 0;

				}
			}
		}

		// Sanity checks
		assert currentHeelInM3 + IVoyagePlanner.ROUNDING_EPSILON >= 0;
		assert totalVoyageBOGInM3 == vp.getLNGFuelVolume();
		final long expextedEndHeel = allocationAnnotation == null ? vp.getRemainingHeelInM3() : allocationAnnotation.getRemainingHeelVolumeInM3();
		assert (Math.abs(expextedEndHeel - currentHeelInM3) <= IVoyagePlanner.ROUNDING_EPSILON);

		return isForcedCooldown;
	}
}
