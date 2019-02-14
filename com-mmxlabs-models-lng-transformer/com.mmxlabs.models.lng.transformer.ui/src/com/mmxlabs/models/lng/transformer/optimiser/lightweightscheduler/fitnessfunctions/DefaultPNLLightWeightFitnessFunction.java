/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.CargoWindowData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightCargoDetails;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class DefaultPNLLightWeightFitnessFunction implements ILightWeightFitnessFunction {
	@Inject
	private ILightWeightOptimisationData lightWeightOptimisationData;
	
	private static final long LATENESS_FINE = 10_000_000L * Calculator.ScaleFactor;
	private static final long UNFULFILLED_FINE = 10_000_000L * Calculator.ScaleFactor;
	private static final long VESSEL_EVENT_PNL = 200_000_000L * Calculator.ScaleFactor;

	private static final class CargoTimeDetails {
		int start;
		int end;
		int latenessInHours;
		
		public CargoTimeDetails(int start, int end, int latenessInHours) {
			this.start = start;
			this.end = end;
			this.latenessInHours = latenessInHours;
		}
	}
	
	@Override
	public void init() {

	}

	@Override
	public long evaluate(List<List<Integer>> sequences) {
		long totalCost = 0;
		long totalPNL = 0;
		long totalLateness = 0;
		int used = 0;

		for (int availability = 0; availability < sequences.size(); availability++) {
			List<Integer> sequence = sequences.get(availability);
			used += sequence.size();
			// calculate costs
			long cost = calculateVariableCostOnSequence(sequence, availability, lightWeightOptimisationData.getCargoToCargoCostsOnAvailability());
			// calculate lateness
			List<CargoTimeDetails> fastestStartTimes = getMostCompressedTimes(sequence, availability, lightWeightOptimisationData.getCargoWindows(), lightWeightOptimisationData.getCargoToCargoMinTravelTimes(),
					lightWeightOptimisationData.getCargoMinTravelTimes(), lightWeightOptimisationData.getVesselStartWindows(), lightWeightOptimisationData.getVesselEndWindows(),
					lightWeightOptimisationData.getCargoEndSlotDurations());
			int lateness = fastestStartTimes.stream().mapToInt(c->c.latenessInHours).sum();
			totalLateness += lateness;
			cost += calculateCharterCostOnSequence(sequence, fastestStartTimes, availability, lightWeightOptimisationData.getCargoCharterCostPerAvailability());
			// calculate pnl
			long profit = calculateProfitOnSequence(sequence, lightWeightOptimisationData.getVesselCapacities()[availability], lightWeightOptimisationData.getCargoPNLPerM3(),
					lightWeightOptimisationData.getCargoesVolumesInM3(), lightWeightOptimisationData.getCargoDetails());
			totalPNL += profit;
			totalCost += cost;
		}

		return totalPNL - totalCost - LATENESS_FINE * totalLateness - UNFULFILLED_FINE * (lightWeightOptimisationData.getCargoCount() - used);
	}

	private long calculateVariableCostOnSequence(List<Integer> sequence, int availability, long[][][] cargoToCargoCostsOnAvailability) {
		long total = 0;
		for (int i = 0; i < sequence.size() - 1; i++) {
			int currIdx = sequence.get(i);
			int nextIdx = sequence.get(i + 1);

			total += calculateVariableCost(cargoToCargoCostsOnAvailability[currIdx][nextIdx][availability]);
		}
		return total;
	}
	
	private long calculateVariableCost(long cost) {
		return Calculator.convertFromHighToLow(cost);
	}
	
	private long calculateCharterCostOnSequence(List<Integer> sequence, List<CargoTimeDetails> times, int availability, long[][] cargoCharterCostPerAvailability) {
		long total = 0;
		for (int i = 0; i < sequence.size() - 1; i++) {
			int currIdx = sequence.get(i);
			int cargoTime = times.get(i+1).start - times.get(i).start;
			total += calculateCharterCost(cargoTime, cargoCharterCostPerAvailability[currIdx][availability]);
		}
		// last is start -> end multiplied by two (round trip)
		if (sequence.size() > 0) {
			int currIdx = sequence.get(sequence.size() - 1);
			int cargoTime = times.get(sequence.size() - 1).end - times.get(sequence.size() - 1).start;
			total += calculateCharterCost(cargoTime, cargoCharterCostPerAvailability[currIdx][availability]);
		}
		return total;
	}

	private long calculateCharterCost(int cargoTimeInHours, long costPerDay) {
		return (cargoTimeInHours*costPerDay)/24L;
	}

	private List<CargoTimeDetails> setFastestTimesAndLateness(List<Integer> sequence, int availability, CargoWindowData[] cargoWindows, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, int vesselStartTime, int vesselEndTime, int[] cargoEndDurations) {
		List<CargoTimeDetails> details = new LinkedList<>();
		int current = vesselStartTime;
		for (int i = 0; i < sequence.size(); i++) {
			int lateness = 0;
			int currIdx = sequence.get(i);
			int earliestCargoStart = Math.max(current, cargoWindows[currIdx].getStartOfStartWindow());
			lateness += (Math.min(0, cargoWindows[currIdx].getEndOfStartWindow() - 1 - earliestCargoStart) * -1);

			int earliestCargoEnd = Math.max(earliestCargoStart + cargoMinTravelTimes[currIdx][availability], cargoWindows[currIdx].getStartOfEndWindow());
			lateness += (Math.min(0, cargoWindows[currIdx].getEndOfEndWindow() - 1 - earliestCargoEnd) * -1);

			if (i < sequence.size() - 1) {
				int nextIdx = sequence.get(i+1);
				current = earliestCargoEnd + cargoToCargoMinTravelTimes[currIdx][nextIdx][availability];
			}
			details.add(new CargoTimeDetails(earliestCargoStart, earliestCargoEnd, lateness));
		}
		if (details.size() > 0) {
			// add vessel end date lateness to last cargo
			CargoTimeDetails lastCargoTimeDetails = details.get(details.size() - 1);
			// Note: end duration is used here as it needs to be added to as lastCargoTimeDetails.end is the start of discharge 
			int endofLastDischargeTime = lastCargoTimeDetails.end + cargoEndDurations[sequence.get(sequence.size()-1)];
			lastCargoTimeDetails.latenessInHours += Math.max(0, endofLastDischargeTime - vesselEndTime);
		}
		return details;
	}
	
	private List<CargoTimeDetails> getMostCompressedTimes(List<Integer> sequence, int availability, CargoWindowData[] cargoWindows, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, ITimeWindow[] vesselStartTimeWindows, ITimeWindow[] vesselEndTimeWindows, int[] cargoEndDurations) {
		int vesselStartTime = vesselStartTimeWindows[availability] != null ? vesselStartTimeWindows[availability].getInclusiveStart() : Integer.MIN_VALUE;
		List<CargoTimeDetails> details = setFastestTimesAndLateness(sequence, availability, cargoWindows, cargoToCargoMinTravelTimes, cargoMinTravelTimes, vesselStartTime, vesselEndTimeWindows[availability].getExclusiveEnd(), cargoEndDurations);
		if (details.size() > 0) {
			// compress last load to discharge journey
			CargoTimeDetails cargoTimeDetails = details.get(details.size() - 1);
			int lastCargoIdx = sequence.get(sequence.size() - 1);
			cargoTimeDetails.start = Math.max(cargoTimeDetails.start, // note: max because of lateness
					Math.min(cargoTimeDetails.end - cargoMinTravelTimes[lastCargoIdx][availability],
							cargoWindows[lastCargoIdx].getEndOfStartWindow()
							)
					);
		}
		
		if (details.size() > 1) {
			for (int i = sequence.size() - 2; i >= 0; i--) {
				int currCargoIdx = sequence.get(i);
				int nextCargoIdx = sequence.get(i+1);
				
				CargoTimeDetails currCargoTimeDetails = details.get(i);
				CargoTimeDetails nextCargoTimeDetails = details.get(i+1);
				currCargoTimeDetails.end = Math.max(currCargoTimeDetails.end,
						Math.min(nextCargoTimeDetails.start - cargoToCargoMinTravelTimes[currCargoIdx][nextCargoIdx][availability],
								cargoWindows[currCargoIdx].getEndOfEndWindow()
								)
						);
				currCargoTimeDetails.start = Math.max(currCargoTimeDetails.start, // note: max because of lateness
						Math.min(currCargoTimeDetails.end - cargoMinTravelTimes[currCargoIdx][availability],
								cargoWindows[currCargoIdx].getStartOfEndWindow()
								)
						);
			}
		}

		return details;
	}


	private long calculateProfitOnSequence(List<Integer> sequence, long capacity, long[] cargoPNLPerM3, long[] volumes, LightWeightCargoDetails[] cargoDetails) {
		long sum = 0L;
		for (int i = 0; i < sequence.size(); i++) {
			Integer cargoIndex = sequence.get(i);
			if (cargoDetails[cargoIndex].getType() != PortType.DryDock &&
					cargoDetails[cargoIndex].getType() != PortType.CharterOut) {
				sum += calculateCargoProfit(Math.min(capacity, volumes[sequence.get(i)]), cargoPNLPerM3[sequence.get(i)]);
			} else {
				sum += VESSEL_EVENT_PNL;
			}
		}
		return sum;
	}
	
	private long calculateCargoProfit(long capacity, long cargoPNLPerM3) {
		return capacity*cargoPNLPerM3;
	}

	@Override
	public long annotate(List<List<Integer>> sequences) {
		long totalCost = 0;
		long totalPNL = 0;
		long totalLateness = 0;
		int used = 0;

		for (int availability = 0; availability < sequences.size(); availability++) {
			List<Integer> sequence = sequences.get(availability);
			used += sequence.size();
			// calculate costs
			long cost = calculateVariableCostOnSequence(sequence, availability, lightWeightOptimisationData.getCargoToCargoCostsOnAvailability());
			// calculate lateness
			List<CargoTimeDetails> fastestStartTimes = getMostCompressedTimes(sequence, availability, lightWeightOptimisationData.getCargoWindows(), lightWeightOptimisationData.getCargoToCargoMinTravelTimes(),
					lightWeightOptimisationData.getCargoMinTravelTimes(), lightWeightOptimisationData.getVesselStartWindows(), lightWeightOptimisationData.getVesselEndWindows(),
					lightWeightOptimisationData.getCargoEndSlotDurations());
			int lateness = fastestStartTimes.stream().mapToInt(c->c.latenessInHours).sum();
			totalLateness += lateness;
			cost += calculateCharterCostOnSequence(sequence, fastestStartTimes, availability, lightWeightOptimisationData.getCargoCharterCostPerAvailability());
			// calculate pnl
			long profit = calculateProfitOnSequence(sequence, lightWeightOptimisationData.getVesselCapacities()[availability], lightWeightOptimisationData.getCargoPNLPerM3(),
					lightWeightOptimisationData.getCargoesVolumesInM3(), lightWeightOptimisationData.getCargoDetails());
			totalPNL += profit;
			totalCost += cost;
		}
		System.out.println("Total lateness: "+totalLateness);
		return totalPNL - totalCost - LATENESS_FINE * totalLateness - UNFULFILLED_FINE * (lightWeightOptimisationData.getCargoCount() - used);
	}

}
