package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import java.util.List;

import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightCargoDetails;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.metaheuristic.TabuLightWeightSequenceOptimiser.Interval;

public class DefaultPNLLightWeightFitnessFunction implements ILightWeightFitnessFunction {

	public static final double LATENESS_FINE = 10_000_000;
	public static final double UNFULFILLED_FINE = 10_000_000;

	@Override
	public Double evaluate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Interval[] loads, Interval[] discharges, double[] volumes, LightWeightCargoDetails[] cargoDetails) {
		double totalCost = 0;
		double totalPNL = 0;
		long totalLateness = 0;
		int used = 0;

		for (int availability = 0; availability < sequences.size(); availability++) {
			List<Integer> sequence = sequences.get(availability);
			used += sequence.size();
			// calculate costs
			double cost = calculateCostOnSequence(sequence, availability, cargoToCargoCostsOnAvailability);
			totalCost += cost;
			// calculate lateness
			int lateness = calculateLatenessOnSequence(sequence, availability, loads, discharges, cargoToCargoMinTravelTimes, cargoMinTravelTimes);
			totalLateness += lateness;
			// calculate pnl
			double profit = calculateProfitOnSequence(sequence, vesselCapacities[availability], cargoPNL, volumes);
			totalPNL += profit;
		}

		return totalPNL - totalCost - LATENESS_FINE * totalLateness - UNFULFILLED_FINE * (cargoCount - used);
	}

	private double calculateCostOnSequence(List<Integer> sequence, int availability, double[][][] cargoToCargoCostsOnAvailability) {
		double total = 0;
		for (int i = 0; i < sequence.size() - 1; i++) {
			int currIdx = sequence.get(i);
			int nextIdx = sequence.get(i + 1);

			total += cargoToCargoCostsOnAvailability[currIdx][nextIdx][availability];
			
		}
		return total;
	}

	private int calculateLatenessOnSequence(List<Integer> sequence, int availability, Interval[] loads, Interval[] discharges, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes) {
		int current = 0;
		int lateness = 0;
		for (int i = 0; i < sequence.size(); i++) {
			int currIdx = sequence.get(i);
			int earliestCargoStart = Math.max(current, (int) loads[currIdx].getStart());
			lateness += (Math.min(0, (int) loads[currIdx].getEnd() - 1 - earliestCargoStart) * -1);

			int earliestCargoEnd = Math.max(earliestCargoStart + cargoMinTravelTimes[currIdx][availability], (int) discharges[currIdx].getStart());
			lateness += (Math.min(0, (int) discharges[currIdx].getEnd() - 1 - earliestCargoEnd) * -1);

			if (i < sequence.size() - 1) {
				current = earliestCargoEnd + cargoToCargoMinTravelTimes[currIdx][sequence.get(i + 1)][availability];
			}
		}
		return lateness;
	}

	private double calculateProfitOnSequence(List<Integer> sequence, double capacity, double[] cargoPNL, double[] volumes) {
		double sum = 0.0f;
		for (int i = 0; i < sequence.size(); i++) {
			sum += cargoPNL[sequence.get(i)] * Math.min(capacity, volumes[sequence.get(i)]);
		}
		return sum;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
