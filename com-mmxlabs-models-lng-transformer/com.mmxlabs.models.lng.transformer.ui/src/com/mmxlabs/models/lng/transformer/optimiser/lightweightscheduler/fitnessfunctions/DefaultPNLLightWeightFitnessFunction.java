package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightCargoDetails;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.metaheuristic.TabuLightWeightSequenceOptimiser.Interval;

public class DefaultPNLLightWeightFitnessFunction implements ILightWeightFitnessFunction {

	public static final double LATENESS_FINE = 10_000_000;
	public static final double UNFULFILLED_FINE = 10_000_000;

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
	public Double evaluate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Interval[] loads, Interval[] discharges,
			double[] volumes, LightWeightCargoDetails[] cargoDetails, double[][] cargoDailyCharterCostPerAvailabilityProcessed) {
		double totalCost = 0;
		double totalPNL = 0;
		long totalLateness = 0;
		int used = 0;

		for (int availability = 0; availability < sequences.size(); availability++) {
			List<Integer> sequence = sequences.get(availability);
			used += sequence.size();
			// calculate costs
			double cost = calculateVariableCostOnSequence(sequence, availability, cargoToCargoCostsOnAvailability);
			// calculate lateness
			List<CargoTimeDetails> fastestStartTimes = getMostCompressedTimes(sequence, availability, loads, discharges, cargoToCargoMinTravelTimes, cargoMinTravelTimes);
			int lateness = fastestStartTimes.stream().mapToInt(c->c.latenessInHours).sum();
			totalLateness += lateness;
			cost += calculateCharterCostOnSequence(sequence, fastestStartTimes, availability, cargoDailyCharterCostPerAvailabilityProcessed);
			// calculate pnl
			double profit = calculateProfitOnSequence(sequence, vesselCapacities[availability], cargoPNL, volumes);
			totalPNL += profit;
			totalCost += cost;
		}

		return totalPNL - totalCost - LATENESS_FINE * totalLateness - UNFULFILLED_FINE * (cargoCount - used);
	}

	private double calculateVariableCostOnSequence(List<Integer> sequence, int availability, double[][][] cargoToCargoCostsOnAvailability) {
		double total = 0;
		for (int i = 0; i < sequence.size() - 1; i++) {
			int currIdx = sequence.get(i);
			int nextIdx = sequence.get(i + 1);

			total += cargoToCargoCostsOnAvailability[currIdx][nextIdx][availability];
			
		}
		return total;
	}
	
	private double calculateCharterCostOnSequence(List<Integer> sequence, List<CargoTimeDetails> times, int availability, double[][] cargoCharterCostPerAvailability) {
		double total = 0;
		for (int i = 0; i < sequence.size() - 1; i++) {
			int currIdx = sequence.get(i);
			int cargoTime = times.get(i+1).start - times.get(i).start;
			total += (cargoTime * cargoCharterCostPerAvailability[currIdx][availability]) / 24.0;
		}
		// last is start -> end multiplied by two (round trip)
		if (sequence.size() > 0) {
			int currIdx = sequence.get(sequence.size() - 1);
			int cargoTime = times.get(sequence.size() - 1).end - times.get(sequence.size() - 1).start;
			total += ((cargoTime * cargoCharterCostPerAvailability[currIdx][availability]) / 24.0)*2;
		}
		return total;
	}

	private List<CargoTimeDetails> getFastestStartTimes(List<Integer> sequence, int availability, Interval[] loads, Interval[] discharges, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes) {
		List<CargoTimeDetails> details = new LinkedList<>();
		int current = 0;
		for (int i = 0; i < sequence.size(); i++) {
			int lateness = 0;
			int currIdx = sequence.get(i);
			int earliestCargoStart = Math.max(current, (int) loads[currIdx].getStart());
			lateness += (Math.min(0, (int) loads[currIdx].getEnd() - 1 - earliestCargoStart) * -1);

			int earliestCargoEnd = Math.max(earliestCargoStart + cargoMinTravelTimes[currIdx][availability], (int) discharges[currIdx].getStart());
			lateness += (Math.min(0, (int) discharges[currIdx].getEnd() - 1 - earliestCargoEnd) * -1);

			if (i < sequence.size() - 1) {
				int nextIdx = sequence.get(i+1);
				current = earliestCargoEnd + cargoToCargoMinTravelTimes[currIdx][nextIdx][availability];
			}
			details.add(new CargoTimeDetails(earliestCargoStart, earliestCargoEnd, lateness));
		}
		return details;
	}
	
	private List<CargoTimeDetails> getMostCompressedTimes(List<Integer> sequence, int availability, Interval[] loads, Interval[] discharges, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes) {
		List<CargoTimeDetails> details = getFastestStartTimes(sequence, availability, loads, discharges, cargoToCargoMinTravelTimes, cargoMinTravelTimes);
		if (details.size() > 0) {
			// compress last load to discharge journey
			CargoTimeDetails cargoTimeDetails = details.get(details.size() - 1);
			int lastCargoIdx = sequence.get(sequence.size() - 1);
			cargoTimeDetails.start = Math.max(cargoTimeDetails.start, // note: max because of lateness
					Math.min(cargoTimeDetails.end - cargoMinTravelTimes[lastCargoIdx][availability],
							loads[lastCargoIdx].getEnd()
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
								discharges[currCargoIdx].getEnd()
								)
						);
				currCargoTimeDetails.start = Math.max(currCargoTimeDetails.start, // note: max because of lateness
						Math.min(currCargoTimeDetails.end - cargoMinTravelTimes[currCargoIdx][availability],
								loads[currCargoIdx].getEnd()
								)
						);
			}
		}

		return details;
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
