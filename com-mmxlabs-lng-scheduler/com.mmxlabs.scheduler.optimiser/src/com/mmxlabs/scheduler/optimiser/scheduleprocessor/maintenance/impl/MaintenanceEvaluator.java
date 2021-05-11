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
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.impl.MaintenanceVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEvent;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
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
		final IDetailsSequenceElement[] currentSequence = vp.getSequence();

		final List<Pair<VoyagePlan, IPortTimesRecord>> newVoyagePlans = generateNewVoyagePlansWithMaintenance(vesselAvailability, vp, portTimesRecord, startHeelVolumeRangeInM3, currentSequence, annotatedSolution);
		if (newVoyagePlans == null) {
			return null;
		}
		// TODO: sanity checks (see CharterLengthEvaluator for inspiration)
		final int originalViolations = vp.getViolationsCount();
		final int violationsSum = newVoyagePlans.stream().map(Pair::getFirst).mapToInt(VoyagePlan::getViolationsCount).sum();
		
		if (originalViolations != violationsSum) {
			final int[] baseFuelPricePerMT = vesselBaseFuelCalculator.getBaseFuelPrices(vesselAvailability.getVessel(), portTimesRecord);
			final long[] startHeelRangeInM3 = new long[2];
			startHeelRangeInM3[0] = vp.getStartingHeelInM3();
			startHeelRangeInM3[1] = vp.getStartingHeelInM3();
			// original vp
			final VoyagePlan originalPlan2 = new VoyagePlan();
			final IDetailsSequenceElement[] originalSequence2 = new IDetailsSequenceElement[vp.getSequence().length];
			for (int i = 0; i < originalSequence2.length; ++i) {
				final IDetailsSequenceElement elem = vp.getSequence()[i];
				originalSequence2[i] = elem instanceof PortDetails ? ((PortDetails) elem).clone() : ((VoyageDetails) elem).clone();
			}
			final IVessel vessel = vesselAvailability.getVessel();
			final ICharterCostCalculator charterCostCalculator = vesselAvailability.getCharterCostCalculator();
			final int originalViolationCount = voyageCalculator.calculateVoyagePlan(originalPlan2, vessel, charterCostCalculator, startHeelRangeInM3, baseFuelPricePerMT, portTimesRecord, originalSequence2);
			final int i = 0;
		}
		boolean check = originalViolations == violationsSum;
		assert vp.getViolationsCount() == newVoyagePlans.stream().map(Pair::getFirst).mapToInt(VoyagePlan::getViolationsCount).sum();


		return newVoyagePlans;
	}

	private List<Pair<VoyagePlan, IPortTimesRecord>> generateNewVoyagePlansWithMaintenance(final IVesselAvailability vesselAvailability, final VoyagePlan originalPlan,
			final IPortTimesRecord originalPortTimesRecord, final long[] originalStartHeelVolumeRangeInM3, final IDetailsSequenceElement[] currentSequence, @Nullable IAnnotatedSolution annotatedSolution) {

		final IVessel vessel = vesselAvailability.getVessel();
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

		long totalBoiloff = 0;
		for (final IDetailsSequenceElement elem : originalPlan.getSequence()) {
			if (elem instanceof PortDetails) {
				final PortDetails portDetails = (PortDetails) elem;
				for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
					totalBoiloff += portDetails.getFuelConsumption(fuelKey);
				}
			} else if (elem instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) elem;
				for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
					totalBoiloff += voyageDetails.getFuelConsumption(fuelKey);
					totalBoiloff += voyageDetails.getRouteAdditionalConsumption(fuelKey);
				}
			} else {
				throw new IllegalStateException();
			}
		}

		final @NonNull MaintenanceVesselEventPortSlot[] maintenancePortSlots = constructNewPortSlots(maintenanceIndices, currentSequence, beforeMaintenanceHeels);
		final List<List<IDetailsSequenceElement>> newSequences = buildNewSequences(maintenanceIndices, currentSequence, maintenancePortSlots);

		final Iterator<List<IDetailsSequenceElement>> sequencesIter = newSequences.iterator();

		List<IDetailsSequenceElement> currentNewSequence = sequencesIter.next();
		// Breaking currentSequence duplicates each maintenancePortSlot between sequences, have to add 1 to count return port
		assert currentSequence.length == newSequences.stream().mapToInt(List::size).sum() - newSequences.size() + 1;

		final List<Pair<VoyagePlan, IPortTimesRecord>> maintenancePlans = new LinkedList<>();
		
		final long[] startHeelRangeInM3 = new long[2];
		startHeelRangeInM3[0] = originalStartHeelVolumeRangeInM3[0];
		startHeelRangeInM3[1] = originalStartHeelVolumeRangeInM3[1];

		{
			final PortTimesRecord firstPortTimesRecord = new PortTimesRecord();

			// Clone all the existing data
			for (int i = 0; i < originalPortTimesRecord.getSlots().size(); ++i) {
				final IPortSlot portSlot = originalPortTimesRecord.getSlots().get(i);
				if (portSlot != maintenancePortSlots[0].getFormerPortSlot()) {
					firstPortTimesRecord.setSlotTime(portSlot, originalPortTimesRecord.getSlotTime(portSlot));
					firstPortTimesRecord.setSlotDuration(portSlot, originalPortTimesRecord.getSlotDuration(portSlot));
					firstPortTimesRecord.setSlotExtraIdleTime(portSlot, originalPortTimesRecord.getSlotExtraIdleTime(portSlot));
					firstPortTimesRecord.setSlotNextVoyageOptions(portSlot, originalPortTimesRecord.getSlotNextVoyageOptions(portSlot), originalPortTimesRecord.getSlotNextVoyagePanamaPeriod(portSlot));
				}
			}
			firstPortTimesRecord.setReturnSlotTime(maintenancePortSlots[0], originalPortTimesRecord.getSlotTime(maintenancePortSlots[0].getFormerPortSlot()));

			// Delete below
			final VoyagePlan originalPlan2 = new VoyagePlan();
			final IDetailsSequenceElement[] originalSequence2 = new IDetailsSequenceElement[originalPlan.getSequence().length];
			for (int i = 0; i < originalSequence2.length; ++i) {
				final IDetailsSequenceElement elem = originalPlan.getSequence()[i];
				originalSequence2[i] = elem instanceof PortDetails ? ((PortDetails) elem).clone() : ((VoyageDetails) elem).clone();
			}
			
			final int originalViolationCount = voyageCalculator.calculateVoyagePlan(originalPlan2, vessel, vesselAvailability.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT, originalPortTimesRecord, originalSequence2);
			// Delete until here
			int originalConsumptionUptoMaintenance = 0;
			for (int i = 0; i < 2; ++i) {
				final IDetailsSequenceElement elem = originalPlan.getSequence()[i];
				if (elem instanceof PortDetails) {
					final PortDetails details = (PortDetails) elem;
					for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
						originalConsumptionUptoMaintenance += details.getFuelConsumption(fuelKey);
					}
				} else if (elem instanceof VoyageDetails) {
					final VoyageDetails details = (VoyageDetails) elem;
					for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
						originalConsumptionUptoMaintenance += details.getFuelConsumption(fuelKey);
						originalConsumptionUptoMaintenance += details.getRouteAdditionalConsumption(fuelKey);
					}
				}
			}
			
			final VoyagePlan currentPlan = new VoyagePlan();
			
			final int violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselAvailability.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT, firstPortTimesRecord, currentNewSequence.toArray(new IDetailsSequenceElement[0]));
			currentPlan.setIgnoreEnd(true);

			int currentConsumptionUptoMaintenance = 0;
			for (int i = 0; i < 2; ++i) {
				final IDetailsSequenceElement elem = currentPlan.getSequence()[i];
				if (elem instanceof PortDetails) {
					final PortDetails details = (PortDetails) elem;
					for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
						currentConsumptionUptoMaintenance += details.getFuelConsumption(fuelKey);
					}
				} else if (elem instanceof VoyageDetails) {
					final VoyageDetails details = (VoyageDetails) elem;
					for (final FuelKey fuelKey : LNGFuelKeys.LNG_In_m3) {
						currentConsumptionUptoMaintenance += details.getFuelConsumption(fuelKey);
						currentConsumptionUptoMaintenance += details.getRouteAdditionalConsumption(fuelKey);
					}
				}
			}
			assert violationCount != Integer.MAX_VALUE;
			// This assertion needs to be added back in!
			assert currentPlan.getStartingHeelInM3() == originalPlan.getStartingHeelInM3();

			currentPlan.setRemainingHeelInM3(beforeMaintenanceHeels[0]);

			final IAllocationAnnotation allocation = volumeAllocator.get().allocate(vesselAvailability, currentPlan, firstPortTimesRecord, annotatedSolution);
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
			currentPortTimesRecord.setSlotExtraIdleTime(maintenancePortSlots[i], originalPortTimesRecord.getSlotExtraIdleTime(maintenancePortSlots[i].getFormerPortSlot()));
			currentPortTimesRecord.setSlotNextVoyageOptions(maintenancePortSlots[i], originalPortTimesRecord.getSlotNextVoyageOptions(maintenancePortSlots[i].getFormerPortSlot()), originalPortTimesRecord.getSlotNextVoyagePanamaPeriod(maintenancePortSlots[i].getFormerPortSlot()));

			currentPortTimesRecord.setReturnSlotTime(maintenancePortSlots[i+1], originalPortTimesRecord.getSlotTime(maintenancePortSlots[i+1].getFormerPortSlot()));

			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setIgnoreEnd(true);

			final int violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselAvailability.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT, currentPortTimesRecord, currentNewSequence.toArray(new IDetailsSequenceElement[0]));
			assert violationCount != Integer.MAX_VALUE;

			currentPlan.setIgnoreEnd(true);

			assert currentPlan.getStartingHeelInM3() == beforeMaintenanceHeels[i];
			assert currentPlan.getRemainingHeelInM3() == beforeMaintenanceHeels[i+1];

			maintenancePlans.add(Pair.of(currentPlan, currentPortTimesRecord));
			startHeelRangeInM3[0] = currentPlan.getRemainingHeelInM3();
			startHeelRangeInM3[1] = currentPlan.getRemainingHeelInM3();
			currentPlan.setIgnoreEnd(true);
		}

		currentNewSequence = sequencesIter.next();
		{
			final PortTimesRecord finalPortTimesRecord = new PortTimesRecord();
			finalPortTimesRecord.setSlotTime(maintenancePortSlots[numMaintenanceEvents-1], originalPortTimesRecord.getSlotTime(maintenancePortSlots[numMaintenanceEvents-1].getFormerPortSlot()));
			finalPortTimesRecord.setSlotDuration(maintenancePortSlots[numMaintenanceEvents-1], originalPortTimesRecord.getSlotDuration(maintenancePortSlots[numMaintenanceEvents-1].getFormerPortSlot()));
			finalPortTimesRecord.setSlotExtraIdleTime(maintenancePortSlots[numMaintenanceEvents-1], originalPortTimesRecord.getSlotExtraIdleTime(maintenancePortSlots[numMaintenanceEvents-1].getFormerPortSlot()));
			finalPortTimesRecord.setSlotNextVoyageOptions(maintenancePortSlots[numMaintenanceEvents-1], originalPortTimesRecord.getSlotNextVoyageOptions(maintenancePortSlots[numMaintenanceEvents-1].getFormerPortSlot()), originalPortTimesRecord.getSlotNextVoyagePanamaPeriod(maintenancePortSlots[numMaintenanceEvents-1].getFormerPortSlot()));

			IPortSlot returnSlot = originalPortTimesRecord.getReturnSlot();
			if (returnSlot != null) {
				finalPortTimesRecord.setReturnSlotTime(returnSlot, originalPortTimesRecord.getSlotTime(returnSlot));
			}

			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			final int violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselAvailability.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricePerMT, finalPortTimesRecord, currentNewSequence.toArray(new IDetailsSequenceElement[0]));
			assert violationCount != Integer.MAX_VALUE;

			maintenancePlans.add(Pair.of(currentPlan, finalPortTimesRecord));
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			assert currentPlan.getStartingHeelInM3() == beforeMaintenanceHeels[numMaintenanceEvents-1];
			assert currentPlan.getRemainingHeelInM3() == originalPlan.getRemainingHeelInM3();
		}

		return maintenancePlans;
	}

	private long[] getBeforeMaintenanceHeels(final int numMaintenanceEvents, final IDetailsSequenceElement[] currentSequence, final LinkedList<Integer> maintenanceIndices, final long originalRemainingHeel) {
		final long[] beforeMaintenanceHeels = new long[numMaintenanceEvents];
		// Check originalPlan.ignoreEnd ?
		final int startIdx = currentSequence.length - 2;
		
		final Iterator<Integer> iter = maintenanceIndices.descendingIterator();
		int currentMaintenanceIndex = iter.next();
		int currentMaintenanceHeelIndex = numMaintenanceEvents - 1;
		int i = startIdx;
		long currentBeforeMaintenanceHeel = originalRemainingHeel;
		while(currentMaintenanceIndex >= 0) {
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

	private @NonNull MaintenanceVesselEventPortSlot[] constructNewPortSlots(final List<Integer> maintenanceIndices, final IDetailsSequenceElement[] currentSequence, final long[] beforeMaintenanceHeels) {
		final MaintenanceVesselEventPortSlot[] maintenancePortSlots = new MaintenanceVesselEventPortSlot[maintenanceIndices.size()];
		final Iterator<Integer> maintenanceIndexIter = maintenanceIndices.iterator();
		for (int i = 0; i < maintenanceIndices.size(); ++i) {
			final int currentMaintenanceIndex = maintenanceIndexIter.next();
			final long beforeMaintenanceHeel = beforeMaintenanceHeels[i];
			final int heelCost = ((VoyageDetails) currentSequence[currentMaintenanceIndex-1]).getFuelUnitPrice(FuelComponent.NBO);
			final HeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(beforeMaintenanceHeel, beforeMaintenanceHeel, VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(heelCost), false);
			final HeelOptionSupplier heelOptionSupplier = new HeelOptionSupplier(beforeMaintenanceHeel, beforeMaintenanceHeel, ((PortDetails) currentSequence[currentMaintenanceIndex]).getOptions().getCargoCVValue(), new ConstantHeelPriceCalculator(heelCost));
			final VesselEventPortSlot vesselEventPortSlot = (VesselEventPortSlot) ((PortDetails) currentSequence[currentMaintenanceIndex]).getOptions().getPortSlot();
			final VesselEvent vesselEvent = (VesselEvent) vesselEventPortSlot.getVesselEvent();
			maintenancePortSlots[i] = new MaintenanceVesselEventPortSlot(vesselEventPortSlot.getId(), vesselEventPortSlot.getTimeWindow(), vesselEventPortSlot.getPort(), vesselEvent.getDurationHours(), heelOptionConsumer, heelOptionSupplier, vesselEventPortSlot);
		}
		return maintenancePortSlots;
	}

	private List<List<IDetailsSequenceElement>> buildNewSequences(final List<Integer> maintenanceIndices, final IDetailsSequenceElement[] currentSequence, final @NonNull MaintenanceVesselEventPortSlot[] maintenancePortSlots) {
		final VoyageDetails newTravelsToMaintenance[] = new VoyageDetails[maintenanceIndices.size()];
		final PortDetails newMaintenancePortDetails[] = new PortDetails[maintenanceIndices.size()];
		final VoyageDetails newTravelsFromMaintenance[] = new VoyageDetails[maintenanceIndices.size()];

		Iterator<Integer> maintenanceIndexIter = maintenanceIndices.iterator();
		int currentMaintenanceIndex = maintenanceIndexIter.next();

		// Pre-visit ballast
		final VoyageDetails newTravelToFirstMaintenance = ((VoyageDetails) currentSequence[currentMaintenanceIndex-1]).clone();
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
		final VoyageOptions firstMaintenanceToNextPortVoyageOptions = ((VoyageDetails) currentSequence[currentMaintenanceIndex+1]).getOptions().copy();
		firstMaintenanceToNextPortVoyageOptions.setFromPortSlot(maintenancePortSlots[0]);
		newTravelsFromMaintenance[0] = ((VoyageDetails) currentSequence[currentMaintenanceIndex+1]).clone();
		newTravelsFromMaintenance[0].setOptions(firstMaintenanceToNextPortVoyageOptions);
		for (int i = 1; i < maintenanceIndices.size(); ++i) {
			// pre-visit ballast
			currentMaintenanceIndex = maintenanceIndexIter.next();
			newTravelsFromMaintenance[i-1].getOptions().setToPortSlot(maintenancePortSlots[i]);
			// Port
			final PortDetails oldMaintenanceDetails = (PortDetails) currentSequence[currentMaintenanceIndex];
			final PortOptions currentMaintenancePortOptions = new PortOptions(maintenancePortSlots[i]);
			currentMaintenancePortOptions.setVessel(oldMaintenanceDetails.getOptions().getVessel());
			currentMaintenancePortOptions.setVisitDuration(oldMaintenanceDetails.getOptions().getVisitDuration());
			currentMaintenancePortOptions.setCargoCVValue(oldMaintenanceDetails.getOptions().getCargoCVValue());
			newMaintenancePortDetails[i] = new PortDetails(currentMaintenancePortOptions);
			// Post-visit ballast
			final VoyageOptions currentMaintenanceToNextPortVoyageOptions = ((VoyageDetails) currentSequence[currentMaintenanceIndex+1]).getOptions().copy();
			currentMaintenanceToNextPortVoyageOptions.setFromPortSlot(maintenancePortSlots[i]);
			newTravelsFromMaintenance[i] = ((VoyageDetails) currentSequence[currentMaintenanceIndex+1]).clone();
			newTravelsFromMaintenance[i].setOptions(currentMaintenanceToNextPortVoyageOptions);
		}

		final List<List<IDetailsSequenceElement>> newSequences = new LinkedList<>();
		final List<IDetailsSequenceElement> firstSequence = new LinkedList<>();
		maintenanceIndexIter = maintenanceIndices.iterator();
		currentMaintenanceIndex = maintenanceIndexIter.next();
		for(int i = 0; i < currentMaintenanceIndex-1; ++i) {
			final IDetailsSequenceElement o = currentSequence[i];
			if (o instanceof PortDetails) {
				firstSequence.add(((PortDetails) o).clone());
			} else if (o instanceof VoyageDetails) {
				firstSequence.add(((VoyageDetails) o).clone());
			}
		}
		firstSequence.add(newTravelsToMaintenance[0]);
		firstSequence.add(newMaintenancePortDetails[0]);
		newSequences.add(firstSequence);

		for (int i = 0; i < maintenanceIndices.size()-1; ++i) {
			final List<IDetailsSequenceElement> currentPartialSequence = new LinkedList<>();
			currentPartialSequence.add(newMaintenancePortDetails[i]);
			currentPartialSequence.add(newTravelsFromMaintenance[i]);
			currentPartialSequence.add(newMaintenancePortDetails[i+1]);
			newSequences.add(currentPartialSequence);
			currentMaintenanceIndex = maintenanceIndexIter.next();
		}

		final List<IDetailsSequenceElement> finalSequence = new LinkedList<>();
		finalSequence.add(newMaintenancePortDetails[maintenanceIndices.size()-1]);
		finalSequence.add(newTravelsFromMaintenance[maintenanceIndices.size()-1]);
		for (int i = currentMaintenanceIndex + 2; i < currentSequence.length; ++i) {
			final IDetailsSequenceElement o = currentSequence[i];
			if (o instanceof PortDetails) {
				finalSequence.add(((PortDetails) o).clone());
			} else if (o instanceof VoyageDetails) {
				finalSequence.add(((VoyageDetails) o).clone());
			}
		}
		newSequences.add(finalSequence);
		return newSequences;
	}
}
