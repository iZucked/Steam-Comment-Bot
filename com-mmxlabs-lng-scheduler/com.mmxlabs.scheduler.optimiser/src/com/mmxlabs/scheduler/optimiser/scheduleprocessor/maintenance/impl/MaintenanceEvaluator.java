package com.mmxlabs.scheduler.optimiser.scheduleprocessor.maintenance.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
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

public class MaintenanceEvaluator implements IMaintenanceEvaluator {

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private Provider<IVolumeAllocator> volumeAllocator;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Override
	public @Nullable List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(long[] startHeelVolumeRangeInM3, IVesselAvailability vesselAvailability, VoyagePlan vp, IPortTimesRecord portTimesRecord,
			@Nullable IAnnotatedSolution annotatedSolution) {
		
		// Only apply to "real" vessels. Exclude nominal/round-trip vessels.
		if (!(vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null;
		}

		// First step, find a maintenance event
		final Object[] currentSequence = vp.getSequence();
		int maintenanceIdx = -1;
		for (int idx = 0; idx < currentSequence.length; ++idx) {
			final Object obj = currentSequence[idx];
			if (obj instanceof PortDetails) {
				final PortDetails portDetails = (PortDetails) obj;
				if (portDetails.getOptions().getPortSlot().getPortType() == PortType.Maintenance) {
					maintenanceIdx = idx;
					break;
				}
			}
		}

		// check if maintenance found
		if (maintenanceIdx == -1) {
			return null;
		}

		List<Pair<VoyagePlan, IPortTimesRecord>> r = generateNewVoyagePlansWithMaintenance(vesselAvailability, vp, portTimesRecord, currentSequence, maintenanceIdx, annotatedSolution);

		// TODO: Put sanity checks in (see CharterLengthEvaluator for inspiration)

		return r;
	}

	private List<Pair<VoyagePlan, IPortTimesRecord>> generateNewVoyagePlansWithMaintenance(final IVesselAvailability vesselAvailability, final VoyagePlan originalPlan,
			final IPortTimesRecord originalPortTimesRecord, final Object[] currentSequence, final int maintenanceIdx, @Nullable IAnnotatedSolution annotatedSolution) {

		final IVessel vessel = vesselAvailability.getVessel();
		final int[] baseFuelPricePerMT = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, originalPortTimesRecord);

		final VoyageDetails preMaintenanceBallast = (VoyageDetails) currentSequence[maintenanceIdx-1];

		long beforeMaintenanceHeel = originalPlan.getRemainingHeelInM3();
		// Check originalPlan.ignoreEnd ?
		final int startIdx = currentSequence.length - 2;
		for (int i = startIdx; i >= maintenanceIdx; --i) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails) {
				PortDetails details = (PortDetails) o;
				for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
					beforeMaintenanceHeel += details.getFuelConsumption(fuelKey);
				}
			} else if (o instanceof VoyageDetails) {
				VoyageDetails details = (VoyageDetails) o;
				for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
					beforeMaintenanceHeel += details.getFuelConsumption(fuelKey);
					beforeMaintenanceHeel += details.getRouteAdditionalConsumption(fuelKey);
				}
			}
		}

		assert beforeMaintenanceHeel >= 0;

		final int heelCost = preMaintenanceBallast.getFuelUnitPrice(FuelComponent.NBO);
		final HeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(beforeMaintenanceHeel, beforeMaintenanceHeel, VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(heelCost), false);
		final HeelOptionSupplier heelOptionSupplier = new HeelOptionSupplier(beforeMaintenanceHeel, beforeMaintenanceHeel, ((PortDetails) currentSequence[maintenanceIdx]).getOptions().getCargoCVValue(), new ConstantHeelPriceCalculator(heelCost));

		final PortDetails oldPortDetails = (PortDetails) currentSequence[maintenanceIdx];
		final VesselEventPortSlot vesselEventPortSlot = (VesselEventPortSlot) oldPortDetails.getOptions().getPortSlot();
		final VesselEvent vesselEvent = (VesselEvent) vesselEventPortSlot.getVesselEvent();

		final MaintenanceVesselEventPortSlot maintenancePortSlot = new MaintenanceVesselEventPortSlot(vesselEventPortSlot.getId(), vesselEventPortSlot.getTimeWindow(), vesselEventPortSlot.getPort(), vesselEvent.getDurationHours(), heelOptionConsumer, heelOptionSupplier, vesselEventPortSlot);

		// Construct new voyage plans
		
		// (1) Ballast to maintenance
		final VoyageOptions ballastStartToMaintenancePortVoyageOptions = preMaintenanceBallast.getOptions().copy();
		ballastStartToMaintenancePortVoyageOptions.setToPortSlot(maintenancePortSlot);
		final VoyageDetails newTravelToMaintenance = preMaintenanceBallast.clone();
		newTravelToMaintenance.setOptions(ballastStartToMaintenancePortVoyageOptions);

		// (2) New maintenance port visit
		final PortOptions maintenancePortOptions = new PortOptions(maintenancePortSlot);
		maintenancePortOptions.setVessel(oldPortDetails.getOptions().getVessel());
		maintenancePortOptions.setVisitDuration(oldPortDetails.getOptions().getVisitDuration());
		maintenancePortOptions.setCargoCVValue(oldPortDetails.getOptions().getCargoCVValue());

		// (3) New maintenance to next load
		final VoyageOptions maintenanceToReturnPortVoyageOptions = ((VoyageDetails) currentSequence[maintenanceIdx+1]).getOptions().copy();
		maintenanceToReturnPortVoyageOptions.setFromPortSlot(maintenancePortSlot);
		final VoyageDetails newTravelFromMaintenance = ((VoyageDetails) currentSequence[maintenanceIdx+1]).clone();
		newTravelFromMaintenance.setOptions(maintenanceToReturnPortVoyageOptions);

		// build new sequence up to but not including pre maintenance voyage
		final IDetailsSequenceElement[] partialSequenceUpToMaintenance = new IDetailsSequenceElement[maintenanceIdx+1];
		for (int i = 0; i < maintenanceIdx-1; ++i) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails) {
				partialSequenceUpToMaintenance[i] = ((PortDetails) o).clone();
			} else if (o instanceof VoyageDetails) {
				partialSequenceUpToMaintenance[i] = ((VoyageDetails) o).clone();
			}
		}
		// Add new port details
		final PortDetails portDetails = new PortDetails(maintenancePortOptions);
		partialSequenceUpToMaintenance[maintenanceIdx-1] = newTravelToMaintenance;
		partialSequenceUpToMaintenance[maintenanceIdx] = portDetails;

		// build new sequence maintenance onwards
		final int newSequenceLength = currentSequence.length-maintenanceIdx;
		final IDetailsSequenceElement[] partialSequenceFromMaintenance = new IDetailsSequenceElement[newSequenceLength];
		partialSequenceFromMaintenance[0] = portDetails;
		partialSequenceFromMaintenance[1] = newTravelFromMaintenance;
		for (int i = 2; i < newSequenceLength; ++i) {
			final Object o = currentSequence[maintenanceIdx+i];
			if (o instanceof PortDetails) {
				partialSequenceFromMaintenance[i] = ((PortDetails) o).clone();
			} else if (o instanceof VoyageDetails) {
				partialSequenceFromMaintenance[i] = ((VoyageDetails) o).clone();
			}
		}

		final List<Pair<VoyagePlan, IPortTimesRecord>> maintenancePlans = new LinkedList<>();
		final long[] startHeelRangeInM3 = new long[2];
		startHeelRangeInM3[0] = originalPlan.getStartingHeelInM3();
		startHeelRangeInM3[1] = originalPlan.getStartingHeelInM3();
		{
			final PortTimesRecord portTimesRecord1 = new PortTimesRecord();

			// Clone all the existing data
			for (int i = 0; i < originalPortTimesRecord.getSlots().size(); ++i) {
				final IPortSlot portSlot = originalPortTimesRecord.getSlots().get(i);
				portTimesRecord1.setSlotTime(portSlot, originalPortTimesRecord.getSlotTime(portSlot));
				portTimesRecord1.setSlotDuration(portSlot, originalPortTimesRecord.getSlotDuration(portSlot));
				portTimesRecord1.setSlotExtraIdleTime(portSlot, originalPortTimesRecord.getSlotExtraIdleTime(portSlot));
				portTimesRecord1.setSlotNextVoyageOptions(portSlot, originalPortTimesRecord.getSlotNextVoyageOptions(portSlot), originalPortTimesRecord.getSlotNextVoyagePanamaPeriod(portSlot));
			}
			portTimesRecord1.setReturnSlotTime(maintenancePortSlot, originalPortTimesRecord.getSlotTime(vesselEventPortSlot));

			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setIgnoreEnd(true);

			final int violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselAvailability.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT, portTimesRecord1, partialSequenceUpToMaintenance);
			assert violationCount != Integer.MAX_VALUE;
			assert currentPlan.getStartingHeelInM3() == originalPlan.getStartingHeelInM3();

			currentPlan.setRemainingHeelInM3(beforeMaintenanceHeel);

			final IAllocationAnnotation allocation = volumeAllocator.get().allocate(vesselAvailability, currentPlan, portTimesRecord1, annotatedSolution);
			if (allocation != null) {
				maintenancePlans.add(Pair.of(currentPlan, allocation));
				assert allocation.getRemainingHeelVolumeInM3() == beforeMaintenanceHeel;

				startHeelRangeInM3[0] = allocation.getRemainingHeelVolumeInM3();
				startHeelRangeInM3[1] = allocation.getRemainingHeelVolumeInM3();
			} else {
				maintenancePlans.add(Pair.of(currentPlan, portTimesRecord1));

				startHeelRangeInM3[0] = currentPlan.getRemainingHeelInM3();
				startHeelRangeInM3[1] = currentPlan.getRemainingHeelInM3();
			}
			currentPlan.setIgnoreEnd(true);
		}

		// New voyage plan representing the maintenance to return slot
		{
			final PortTimesRecord portTimesRecord2 = new PortTimesRecord();
			portTimesRecord2.setSlotTime(maintenancePortSlot, originalPortTimesRecord.getSlotTime(vesselEventPortSlot));
			portTimesRecord2.setSlotDuration(maintenancePortSlot, originalPortTimesRecord.getSlotDuration(vesselEventPortSlot));
			portTimesRecord2.setSlotExtraIdleTime(maintenancePortSlot, originalPortTimesRecord.getSlotExtraIdleTime(vesselEventPortSlot));

			IPortSlot returnSlot = originalPortTimesRecord.getReturnSlot();
			if (returnSlot != null) {
				portTimesRecord2.setReturnSlotTime(returnSlot, originalPortTimesRecord.getSlotTime(returnSlot));
			}

			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			final int violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselAvailability.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT, portTimesRecord2, partialSequenceFromMaintenance);
			assert violationCount != Integer.MAX_VALUE;

			maintenancePlans.add(Pair.of(currentPlan, portTimesRecord2));
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			assert currentPlan.getStartingHeelInM3() == beforeMaintenanceHeel;
			assert currentPlan.getRemainingHeelInM3() == originalPlan.getRemainingHeelInM3();
		}

		return maintenancePlans;
	}
}
