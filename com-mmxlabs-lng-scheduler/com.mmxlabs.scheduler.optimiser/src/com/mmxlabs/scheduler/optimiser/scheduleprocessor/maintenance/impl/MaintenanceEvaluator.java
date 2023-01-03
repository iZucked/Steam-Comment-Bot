/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.maintenance.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.impl.MaintenanceVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.maintenance.IMaintenanceEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan.VoyagePlanMetrics;

public class MaintenanceEvaluator implements IMaintenanceEvaluator {

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private Provider<IVolumeAllocator> volumeAllocator;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Override
	public @Nullable List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(long[] startHeelVolumeRangeInM3, IVesselCharter vesselCharter, VoyagePlan vp, @NonNull IPortTimesRecord portTimesRecord,
			@Nullable IAnnotatedSolution annotatedSolution) {

		// Only apply to "real" vessels. Exclude nominal/round-trip vessels.
		if (!(vesselCharter.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselCharter.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER //
				|| vesselCharter.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null;
		}

		// First step, find a maintenance event
		final IDetailsSequenceElement[] currentSequence = vp.getSequence();

		final List<Pair<VoyagePlan, IPortTimesRecord>> newVoyagePlans = generateNewVoyagePlansWithMaintenance(vesselCharter, vp, portTimesRecord, startHeelVolumeRangeInM3, currentSequence,
				annotatedSolution);
		if (newVoyagePlans == null) {
			return null;
		}
		// TODO: sanity checks (see CharterLengthEvaluator for inspiration)
		assert vp.getVoyagePlanMetrics()[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()] == newVoyagePlans.stream().map(Pair::getFirst)
				.mapToLong(lvp -> lvp.getVoyagePlanMetrics()[VoyagePlanMetrics.VOLUME_VIOLATION_COUNT.ordinal()]).sum();

		return newVoyagePlans;
	}

	private List<Pair<VoyagePlan, IPortTimesRecord>> generateNewVoyagePlansWithMaintenance(final IVesselCharter vesselCharter, final VoyagePlan originalPlan,
			@NonNull final IPortTimesRecord originalPortTimesRecord, final long[] originalStartHeelVolumeRangeInM3, final IDetailsSequenceElement[] currentSequence,
			@Nullable IAnnotatedSolution annotatedSolution) {

		final IVessel vessel = vesselCharter.getVessel();
		final int[] baseFuelPricePerMT = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, originalPortTimesRecord);

		final LinkedList<Integer> maintenanceIndices = new LinkedList<>();
		for (int i = 0; i < currentSequence.length; ++i) {
			final Object currentObj = currentSequence[i];
			if (currentObj instanceof PortDetails && ((PortDetails) currentObj).getOptions().getPortSlot().getPortType() == PortType.Maintenance) {
				maintenanceIndices.add(i);
			}
		}

		if (maintenanceIndices.isEmpty()) {
			return null;
		}

		final int numMaintenanceEvents = maintenanceIndices.size();
		final long[] beforeMaintenanceHeels = getBeforeMaintenanceHeels(numMaintenanceEvents, currentSequence, maintenanceIndices, originalPlan.getRemainingHeelInM3());
		for (final long beforeMaintenanceHeel : beforeMaintenanceHeels) {
			assert beforeMaintenanceHeel >= 0;
		}

		final @NonNull MaintenanceVesselEventPortSlot[] maintenancePortSlots = constructNewPortSlots(maintenanceIndices, currentSequence, beforeMaintenanceHeels);
		final List<List<IDetailsSequenceElement>> newSequences = buildNewSequences(maintenanceIndices, currentSequence, maintenancePortSlots);

		final Iterator<List<IDetailsSequenceElement>> sequencesIter = newSequences.iterator();

		List<IDetailsSequenceElement> currentNewSequence = sequencesIter.next();
		// Breaking currentSequence duplicates each maintenancePortSlot between
		// sequences, have to add 1 to count return port
		assert currentSequence.length == newSequences.stream().mapToInt(List::size).sum() - newSequences.size() + 1;

		final List<Pair<VoyagePlan, IPortTimesRecord>> maintenancePlans = new LinkedList<>();

		final long[] startHeelRangeInM3 = new long[2];
		startHeelRangeInM3[0] = originalStartHeelVolumeRangeInM3[0];
		startHeelRangeInM3[1] = originalStartHeelVolumeRangeInM3[1];

		{
			final PortTimesRecord firstPortTimesRecord = new PortTimesRecord();
			// Breaking at maintenance events so any VesselEventPortSlot associated with a
			// maintenance event should not be copied across
			for (int i = 0; i < originalPortTimesRecord.getSlots().size(); ++i) {
				final IPortSlot portSlot = originalPortTimesRecord.getSlots().get(i);
				if (portSlot.getPortType() == PortType.Maintenance) {
					continue;
				}
				firstPortTimesRecord.setSlotTime(portSlot, originalPortTimesRecord.getSlotTime(portSlot));
				firstPortTimesRecord.setSlotDuration(portSlot, originalPortTimesRecord.getSlotDuration(portSlot));
				firstPortTimesRecord.setSlotAdditionalPanamaIdleHours(portSlot, originalPortTimesRecord.getSlotAdditionaPanamaIdleHours(portSlot));
				firstPortTimesRecord.setSlotMaxAvailablePanamaIdleHours(portSlot, originalPortTimesRecord.getSlotMaxAdditionaPanamaIdleHours(portSlot));
				firstPortTimesRecord.setRouteOptionBooking(portSlot, originalPortTimesRecord.getRouteOptionBooking(portSlot));
				for (var type : ExplicitIdleTime.values()) {
					firstPortTimesRecord.setSlotExtraIdleTime(portSlot, type, originalPortTimesRecord.getSlotExtraIdleTime(portSlot, type));
				}
				firstPortTimesRecord.setSlotNextVoyageOptions(portSlot, originalPortTimesRecord.getSlotNextVoyageOptions(portSlot));
			}
			firstPortTimesRecord.setReturnSlotTime(maintenancePortSlots[0], originalPortTimesRecord.getSlotTime(maintenancePortSlots[0].getFormerPortSlot()));

			final VoyagePlan currentPlan = new VoyagePlan();

			final long[] violationMetrics = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselCharter.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT,
					firstPortTimesRecord, currentNewSequence.toArray(new IDetailsSequenceElement[0]));
			currentPlan.setIgnoreEnd(true);

			assert violationMetrics != null;
			assert currentPlan.getStartingHeelInM3() == originalPlan.getStartingHeelInM3();

			currentPlan.setRemainingHeelInM3(beforeMaintenanceHeels[0]);

			final IAllocationAnnotation allocation = volumeAllocator.get().allocate(vesselCharter, currentPlan, firstPortTimesRecord, annotatedSolution);
			if (allocation != null) {
				maintenancePlans.add(Pair.of(currentPlan, allocation));
				assert allocation.getRemainingHeelVolumeInM3() == beforeMaintenanceHeels[0];

				startHeelRangeInM3[0] = allocation.getRemainingHeelVolumeInM3();
				startHeelRangeInM3[1] = allocation.getRemainingHeelVolumeInM3();
			} else {
				maintenancePlans.add(Pair.of(currentPlan, firstPortTimesRecord));

				startHeelRangeInM3[0] = currentPlan.getRemainingHeelInM3();
				startHeelRangeInM3[1] = currentPlan.getRemainingHeelInM3();
			}
			currentPlan.setIgnoreEnd(true);
		}

		for (int i = 0; i < numMaintenanceEvents - 1; ++i) {
			currentNewSequence = sequencesIter.next();
			final PortTimesRecord currentPortTimesRecord = new PortTimesRecord();
			currentPortTimesRecord.setSlotTime(maintenancePortSlots[i], originalPortTimesRecord.getSlotTime(maintenancePortSlots[i].getFormerPortSlot()));
			currentPortTimesRecord.setSlotDuration(maintenancePortSlots[i], originalPortTimesRecord.getSlotDuration(maintenancePortSlots[i].getFormerPortSlot()));

			currentPortTimesRecord.setSlotAdditionalPanamaIdleHours(maintenancePortSlots[i], originalPortTimesRecord.getSlotAdditionaPanamaIdleHours(maintenancePortSlots[i].getFormerPortSlot()));
			currentPortTimesRecord.setSlotMaxAvailablePanamaIdleHours(maintenancePortSlots[i], originalPortTimesRecord.getSlotMaxAdditionaPanamaIdleHours(maintenancePortSlots[i].getFormerPortSlot()));
			currentPortTimesRecord.setRouteOptionBooking(maintenancePortSlots[i], originalPortTimesRecord.getRouteOptionBooking(maintenancePortSlots[i].getFormerPortSlot()));
			for (var type : ExplicitIdleTime.values()) {
				currentPortTimesRecord.setSlotExtraIdleTime(maintenancePortSlots[i], type, originalPortTimesRecord.getSlotExtraIdleTime(maintenancePortSlots[i].getFormerPortSlot(), type));
			}
			currentPortTimesRecord.setSlotNextVoyageOptions(maintenancePortSlots[i], originalPortTimesRecord.getSlotNextVoyageOptions(maintenancePortSlots[i].getFormerPortSlot()));
			currentPortTimesRecord.setReturnSlotTime(maintenancePortSlots[i + 1], originalPortTimesRecord.getSlotTime(maintenancePortSlots[i + 1].getFormerPortSlot()));

			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setIgnoreEnd(true);

			final long[] violationMetrics = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselCharter.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT,
					currentPortTimesRecord, currentNewSequence.toArray(new IDetailsSequenceElement[0]));
			assert violationMetrics != null;

			currentPlan.setIgnoreEnd(true);

			assert currentPlan.getStartingHeelInM3() == beforeMaintenanceHeels[i];
			assert currentPlan.getRemainingHeelInM3() == beforeMaintenanceHeels[i + 1];

			maintenancePlans.add(Pair.of(currentPlan, currentPortTimesRecord));
			startHeelRangeInM3[0] = currentPlan.getRemainingHeelInM3();
			startHeelRangeInM3[1] = currentPlan.getRemainingHeelInM3();
			currentPlan.setIgnoreEnd(true);
		}

		currentNewSequence = sequencesIter.next();
		{
			final PortTimesRecord finalPortTimesRecord = new PortTimesRecord();
			finalPortTimesRecord.setSlotTime(maintenancePortSlots[numMaintenanceEvents - 1], originalPortTimesRecord.getSlotTime(maintenancePortSlots[numMaintenanceEvents - 1].getFormerPortSlot()));
			finalPortTimesRecord.setSlotDuration(maintenancePortSlots[numMaintenanceEvents - 1],
					originalPortTimesRecord.getSlotDuration(maintenancePortSlots[numMaintenanceEvents - 1].getFormerPortSlot()));
			finalPortTimesRecord.setSlotAdditionalPanamaIdleHours(maintenancePortSlots[numMaintenanceEvents - 1],
					originalPortTimesRecord.getSlotAdditionaPanamaIdleHours(maintenancePortSlots[numMaintenanceEvents - 1].getFormerPortSlot()));
			finalPortTimesRecord.setSlotMaxAvailablePanamaIdleHours(maintenancePortSlots[numMaintenanceEvents - 1],
					originalPortTimesRecord.getSlotMaxAdditionaPanamaIdleHours(maintenancePortSlots[numMaintenanceEvents - 1].getFormerPortSlot()));
			finalPortTimesRecord.setRouteOptionBooking(maintenancePortSlots[numMaintenanceEvents - 1],
					originalPortTimesRecord.getRouteOptionBooking(maintenancePortSlots[numMaintenanceEvents - 1].getFormerPortSlot()));
			for (var type : ExplicitIdleTime.values()) {
				finalPortTimesRecord.setSlotExtraIdleTime(maintenancePortSlots[numMaintenanceEvents - 1], type,
						originalPortTimesRecord.getSlotExtraIdleTime(maintenancePortSlots[numMaintenanceEvents - 1].getFormerPortSlot(), type));
			}

			finalPortTimesRecord.setSlotNextVoyageOptions(maintenancePortSlots[numMaintenanceEvents - 1],
					originalPortTimesRecord.getSlotNextVoyageOptions(maintenancePortSlots[numMaintenanceEvents - 1].getFormerPortSlot()));
			IPortSlot returnSlot = originalPortTimesRecord.getReturnSlot();
			if (returnSlot != null) {
				finalPortTimesRecord.setReturnSlotTime(returnSlot, originalPortTimesRecord.getSlotTime(returnSlot));
			}

			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			final long[] violationMetrics = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselCharter.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT,
					finalPortTimesRecord, currentNewSequence.toArray(new IDetailsSequenceElement[0]));
			assert violationMetrics != null;

			maintenancePlans.add(Pair.of(currentPlan, finalPortTimesRecord));
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			assert currentPlan.getStartingHeelInM3() == beforeMaintenanceHeels[numMaintenanceEvents - 1];
			assert currentPlan.getRemainingHeelInM3() == originalPlan.getRemainingHeelInM3();
		}

		// Should have emptied the iterator by now
		assert !sequencesIter.hasNext();

		return maintenancePlans;
	}

	private long[] getBeforeMaintenanceHeels(final int numMaintenanceEvents, final IDetailsSequenceElement[] currentSequence, final LinkedList<Integer> maintenanceIndices,
			final long originalRemainingHeel) {
		final long[] beforeMaintenanceHeels = new long[numMaintenanceEvents];
		// Check originalPlan.ignoreEnd ?
		final int startIdx = currentSequence.length - 2;

		final Iterator<Integer> iter = maintenanceIndices.descendingIterator();
		int currentMaintenanceIndex = iter.next();
		int currentMaintenanceHeelIndex = numMaintenanceEvents - 1;
		int i = startIdx;
		long currentBeforeMaintenanceHeel = originalRemainingHeel;
		while (currentMaintenanceIndex >= 0) {
			final IDetailsSequenceElement o = currentSequence[i];
			if (o instanceof PortDetails) {
				final PortDetails details = (PortDetails) o;
				for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
					currentBeforeMaintenanceHeel += details.getFuelConsumption(fuelKey);
				}
			} else if (o instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) o;
				for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
					currentBeforeMaintenanceHeel += details.getFuelConsumption(fuelKey);
					currentBeforeMaintenanceHeel += details.getRouteAdditionalConsumption(fuelKey);
				}
			}
			--i;
			if (currentMaintenanceIndex > i) {
				beforeMaintenanceHeels[currentMaintenanceHeelIndex] = currentBeforeMaintenanceHeel;
				if (!iter.hasNext()) {
					break;
				}
				currentMaintenanceIndex = iter.next();
				--currentMaintenanceHeelIndex;
			}
		}
		return beforeMaintenanceHeels;
	}

	@SuppressWarnings("null")
	private @NonNull MaintenanceVesselEventPortSlot[] constructNewPortSlots(final List<Integer> maintenanceIndices, final IDetailsSequenceElement[] currentSequence,
			final long[] beforeMaintenanceHeels) {
		final MaintenanceVesselEventPortSlot[] maintenancePortSlots = new MaintenanceVesselEventPortSlot[maintenanceIndices.size()];
		final Iterator<Integer> maintenanceIndexIter = maintenanceIndices.iterator();
		for (int i = 0; i < maintenanceIndices.size(); ++i) {
			final int currentMaintenanceIndex = maintenanceIndexIter.next();
			final long beforeMaintenanceHeel = beforeMaintenanceHeels[i];
			final int heelCost = ((VoyageDetails) currentSequence[currentMaintenanceIndex - 1]).getFuelUnitPrice(FuelComponent.NBO);
			final HeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(beforeMaintenanceHeel, beforeMaintenanceHeel, VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(heelCost),
					false);
			final HeelOptionSupplier heelOptionSupplier = new HeelOptionSupplier(beforeMaintenanceHeel, beforeMaintenanceHeel,
					((PortDetails) currentSequence[currentMaintenanceIndex]).getOptions().getCargoCVValue(), new ConstantHeelPriceCalculator(heelCost));
			final VesselEventPortSlot vesselEventPortSlot = (VesselEventPortSlot) ((PortDetails) currentSequence[currentMaintenanceIndex]).getOptions().getPortSlot();
			final VesselEvent vesselEvent = (VesselEvent) vesselEventPortSlot.getVesselEvent();
			maintenancePortSlots[i] = new MaintenanceVesselEventPortSlot(vesselEventPortSlot.getId(), vesselEventPortSlot.getTimeWindow(), vesselEventPortSlot.getPort(),
					vesselEvent.getDurationHours(), heelOptionConsumer, heelOptionSupplier, vesselEventPortSlot);
		}
		return maintenancePortSlots;
	}

	private List<List<IDetailsSequenceElement>> buildNewSequences(final List<Integer> maintenanceIndices, final IDetailsSequenceElement[] currentSequence,
			final @NonNull MaintenanceVesselEventPortSlot[] maintenancePortSlots) {
		final VoyageDetails[] newTravelsToMaintenance = new VoyageDetails[maintenanceIndices.size()];
		final PortDetails[] newMaintenancePortDetails = new PortDetails[maintenanceIndices.size()];
		final VoyageDetails[] newTravelsFromMaintenance = new VoyageDetails[maintenanceIndices.size()];

		Iterator<Integer> maintenanceIndexIter = maintenanceIndices.iterator();
		int currentMaintenanceIndex = maintenanceIndexIter.next();

		// Pre-visit ballast
		final VoyageDetails newTravelToFirstMaintenance = ((VoyageDetails) currentSequence[currentMaintenanceIndex - 1]).copy();
		newTravelToFirstMaintenance.getOptions().setToPortSlot(maintenancePortSlots[0]);
		newTravelsToMaintenance[0] = newTravelToFirstMaintenance;
		// Port
		final PortOptions newFirstMaintenancePortOptions = new PortOptions(maintenancePortSlots[0]);
		final PortDetails oldFirstMaintenanceDetails = (PortDetails) currentSequence[currentMaintenanceIndex];
		newFirstMaintenancePortOptions.setVessel(oldFirstMaintenanceDetails.getOptions().getVessel());
		newFirstMaintenancePortOptions.setVisitDuration(oldFirstMaintenanceDetails.getOptions().getVisitDuration());
		newFirstMaintenancePortOptions.setCargoCVValue(oldFirstMaintenanceDetails.getOptions().getCargoCVValue());
		newMaintenancePortDetails[0] = new PortDetails(newFirstMaintenancePortOptions);
		// Post-visit ballast
		final VoyageOptions firstMaintenanceToNextPortVoyageOptions = ((VoyageDetails) currentSequence[currentMaintenanceIndex + 1]).getOptions().copy();
		firstMaintenanceToNextPortVoyageOptions.setFromPortSlot(maintenancePortSlots[0]);
		newTravelsFromMaintenance[0] = ((VoyageDetails) currentSequence[currentMaintenanceIndex + 1]).copy();
		newTravelsFromMaintenance[0].setOptions(firstMaintenanceToNextPortVoyageOptions);
		for (int i = 1; i < maintenanceIndices.size(); ++i) {
			// pre-visit ballast
			currentMaintenanceIndex = maintenanceIndexIter.next();
			newTravelsFromMaintenance[i - 1].getOptions().setToPortSlot(maintenancePortSlots[i]);
			// Port
			final PortDetails oldMaintenanceDetails = (PortDetails) currentSequence[currentMaintenanceIndex];
			final PortOptions currentMaintenancePortOptions = new PortOptions(maintenancePortSlots[i]);
			currentMaintenancePortOptions.setVessel(oldMaintenanceDetails.getOptions().getVessel());
			currentMaintenancePortOptions.setVisitDuration(oldMaintenanceDetails.getOptions().getVisitDuration());
			currentMaintenancePortOptions.setCargoCVValue(oldMaintenanceDetails.getOptions().getCargoCVValue());
			newMaintenancePortDetails[i] = new PortDetails(currentMaintenancePortOptions);
			// Post-visit ballast
			final VoyageOptions currentMaintenanceToNextPortVoyageOptions = ((VoyageDetails) currentSequence[currentMaintenanceIndex + 1]).getOptions().copy();
			currentMaintenanceToNextPortVoyageOptions.setFromPortSlot(maintenancePortSlots[i]);
			newTravelsFromMaintenance[i] = ((VoyageDetails) currentSequence[currentMaintenanceIndex + 1]).copy();
			newTravelsFromMaintenance[i].setOptions(currentMaintenanceToNextPortVoyageOptions);
		}

		final List<List<IDetailsSequenceElement>> newSequences = new LinkedList<>();
		final List<IDetailsSequenceElement> firstSequence = new LinkedList<>();
		maintenanceIndexIter = maintenanceIndices.iterator();
		currentMaintenanceIndex = maintenanceIndexIter.next();
		for (int i = 0; i < currentMaintenanceIndex - 1; ++i) {
			final IDetailsSequenceElement o = currentSequence[i];
			if (o instanceof PortDetails) {
				firstSequence.add(((PortDetails) o).copy());
			} else if (o instanceof VoyageDetails) {
				firstSequence.add(((VoyageDetails) o).copy());
			}
		}
		firstSequence.add(newTravelsToMaintenance[0]);
		firstSequence.add(newMaintenancePortDetails[0]);
		newSequences.add(firstSequence);

		for (int i = 0; i < maintenanceIndices.size() - 1; ++i) {
			final List<IDetailsSequenceElement> currentPartialSequence = new LinkedList<>();
			currentPartialSequence.add(newMaintenancePortDetails[i]);
			currentPartialSequence.add(newTravelsFromMaintenance[i]);
			currentPartialSequence.add(newMaintenancePortDetails[i + 1]);
			newSequences.add(currentPartialSequence);
			currentMaintenanceIndex = maintenanceIndexIter.next();
		}

		final List<IDetailsSequenceElement> finalSequence = new LinkedList<>();
		finalSequence.add(newMaintenancePortDetails[maintenanceIndices.size() - 1]);
		finalSequence.add(newTravelsFromMaintenance[maintenanceIndices.size() - 1]);
		for (int i = currentMaintenanceIndex + 2; i < currentSequence.length; ++i) {
			final IDetailsSequenceElement o = currentSequence[i];
			if (o instanceof PortDetails) {
				finalSequence.add(((PortDetails) o).copy());
			} else if (o instanceof VoyageDetails) {
				finalSequence.add(((VoyageDetails) o).copy());
			}
		}
		newSequences.add(finalSequence);
		return newSequences;
	}
}
