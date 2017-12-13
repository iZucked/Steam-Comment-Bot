package com.mmxlabs.models.lng.transformer.longterm.metaheuristic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.minimaxlabs.rnd.representation.ShipmentData;
import com.mmxlabs.models.lng.transformer.longterm.ILightWeightSequenceOptimiser;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class TabuLightWeightSequenceOptimiser implements ILightWeightSequenceOptimiser {
	public static final double LATENESS_FINE = 1_000_000;
	public static final double UNFULFILLED_FINE = 10_000_000;

	public static final double MINUTES = 1.0 / 60;
	public static final double HOURS = 1.0 / 3600;
	private long totalLateness;

	private int iterations;
	private int maxAge;
	private int search;
	private int seed;
	private Random r;
	private List<List<Double>> series = Stream.generate(LinkedList<Double>::new).limit(4).collect(Collectors.toList());

	public class Interval {
		private final double start;
		private final double end;

		public Interval(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
			this.start = startDateTime.toEpochSecond() * HOURS;
			this.end = endDateTime.toEpochSecond() * HOURS;
		}

		public Interval(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public double getEnd() {
			return end;
		}

		public double getStart() {
			return start;
		}
	}

	public TabuLightWeightSequenceOptimiser() {
		this(5000, 5000, 6, 0);
	}

	public TabuLightWeightSequenceOptimiser(int globalIterations, int searchIterations, int maxAge, int randomSeed) {
		this.iterations = globalIterations;
		this.search = searchIterations;
		this.maxAge = maxAge;
		this.seed = randomSeed;
		this.r = new Random(seed);
	}

	public String getParams() {
		return new String(iterations + " " + search + " " + maxAge);
	}

	public void setParams(String args) {
		String[] params = args.split(" ");
		this.iterations = Integer.parseInt(params[0]);
		this.search = Integer.parseInt(params[1]);
		this.maxAge = Integer.parseInt(params[2]);
		this.seed = Integer.parseInt(params[3]);
		this.r = new Random(seed);
	}

	public void setSeed(int seed) {
		this.seed = seed;
		r = new Random(seed);
	}

	public List<List<Integer>> optimise(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, long[] cargoPNL, Long[][][] cargoToCargoCostsOnAvailability,
			ArrayList<Set<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes) {
		double[] capacity = vessels.stream().mapToDouble(v -> v.getVessel().getCargoCapacity() / 1000).toArray();

		double[] cargoPNLasDouble = new double[cargoPNL.length];
		for (int i = 0; i < cargoPNLasDouble.length; i++) {
			cargoPNLasDouble[i] = (double) cargoPNL[i];
		}

		List<List<Integer>> cargoVesselRestrictionAsList = new ArrayList<List<Integer>>(cargoVesselRestrictions.size());
		for (Set<Integer> restriction : cargoVesselRestrictions) {
			cargoVesselRestrictionAsList.add(restriction.stream().collect(Collectors.toList()));
		}
		
		double[][][] cargoToCargoCostsProcessed = new double[cargoToCargoCostsOnAvailability.length][cargoToCargoCostsOnAvailability[0].length][cargoToCargoCostsOnAvailability[0][0].length];
		for (int j = 0; j < cargoToCargoCostsOnAvailability.length; j++) {
			for (int j2 = 0; j2 < cargoToCargoCostsOnAvailability[j].length; j2++) {
				for (int k = 0; k < cargoToCargoCostsOnAvailability[j][j2].length; k++) {
					cargoToCargoCostsProcessed[j][j2][k] = (double) cargoToCargoCostsOnAvailability[j][j2][k] / 1_000_000.0;
				}
			}
		}

		Interval[] loads = new Interval[cargoes.size()];
		Interval[] discharges = new Interval[cargoes.size()];

		for (int i = 0; i < cargoes.size(); i++) {
			ITimeWindow loadTW = cargoes.get(i).get(0).getTimeWindow();
			ITimeWindow dischargeTW = cargoes.get(i).get(1).getTimeWindow();
			
			loads[i] = new Interval(loadTW.getInclusiveStart(), loadTW.getExclusiveEnd());
			discharges[i] = new Interval(dischargeTW.getInclusiveStart(), dischargeTW.getExclusiveEnd());
		}
		
		createExternalData(cargoes, vessels, cargoToCargoMinTravelTimes, cargoMinTravelTimes, capacity, cargoPNLasDouble, cargoToCargoCostsProcessed, loads, discharges);
		
		return optimise(0, false,
				cargoes.size(), vessels.size(),
				cargoPNLasDouble, capacity,
				cargoToCargoCostsProcessed, cargoVesselRestrictionAsList,
				cargoToCargoMinTravelTimes, cargoMinTravelTimes,
				loads, discharges);
	}

	private void createExternalData(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, double[] capacity,
			double[] cargoPNLasDouble, double[][][] cargoToCargoCostsProcessed, Interval[] loads, Interval[] discharges) {
		com.minimaxlabs.rnd.representation.Interval[] arrivals = Arrays.stream(loads).map(a -> new com.minimaxlabs.rnd.representation.Interval((int) a.start, (int) a.end)).toArray(size -> new com.minimaxlabs.rnd.representation.Interval[size]);
		com.minimaxlabs.rnd.representation.Interval[] departures = Arrays.stream(discharges).map(a -> new com.minimaxlabs.rnd.representation.Interval((int) a.start, (int) a.end)).toArray(size -> new com.minimaxlabs.rnd.representation.Interval[size]);

		ShipmentData sd = new ShipmentData(cargoes.size(), vessels.size(), cargoPNLasDouble, capacity, cargoToCargoMinTravelTimes, cargoToCargoCostsProcessed, cargoMinTravelTimes, arrivals, departures);

	      try {
	          FileOutputStream fileOut =
	          new FileOutputStream("/tmp/shippedData.sd");
	          ObjectOutputStream out = new ObjectOutputStream(fileOut);
	          out.writeObject(sd);
	          out.close();
	          fileOut.close();
	          System.out.printf("Serialized data is saved in /tmp/shippedData.sd");
	       }catch(IOException iff) {
	          iff.printStackTrace();
	       }
	}

	public List<List<Integer>> optimise(int id, boolean loggingFlag,
			int cargoCount, int vesselCount,
			double[] cargoPNL, double[] vesselCapacities,
			double[][][] cargoToCargoCostsOnAvailability, List<List<Integer>> cargoVesselRestrictions,
			int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes,
			Interval[] loads, Interval[] discharges) {
		
		List<HashSet<Integer>> tabu = new LinkedList<>();
		List<List<Integer>> schedules = Arrays.stream(vesselCapacities, 0, vesselCount)
				.mapToObj(v -> (List<Integer>) new LinkedList<Integer>())
				.collect(Collectors.toList());
		List<List<Integer>> bestSchedules = null;

		long lateness = 0;
		int bestIteration = 0;
		Double bestScore = -UNFULFILLED_FINE * cargoCount;
		TabuLightWeightSequenceOptimiserMoves mover = new TabuLightWeightSequenceOptimiserMoves(r, cargoCount);

		System.out.println("\nRUN " + id);
		for (int i = 0; i < iterations; i++) {
			if (i % (iterations / 10) == 0) {
				System.out.print(i * 100 / iterations + "% ");
			}
			// add back tabu elements
			if (tabu.size() == maxAge) {
				HashSet<Integer> old = tabu.remove(0);
				mover.addTabus(old);
			}

			int newCargo = 0;
			HashSet<Integer> tabuSet = new HashSet<>();
			List<List<Integer>> currentSchedules = mover.move(schedules);
			double currentScore = evaluate(schedules, cargoCount, cargoPNL, vesselCapacities,
					cargoToCargoCostsOnAvailability, cargoVesselRestrictions, cargoToCargoMinTravelTimes,
					cargoMinTravelTimes, loads, discharges);

			TabuLightWeightSequenceOptimiserMoves newMover = TabuLightWeightSequenceOptimiserMoves.newInstance(mover);
			newMover.registerDiff();

			newCargo = mover.getCargo();
			// tabuSet.add(mover.getCargo());

			for (int j = 0; j < search; j++) {
				List<List<Integer>> newSchedules = mover.move(schedules);
				Double newScore = evaluate(newSchedules, cargoCount, cargoPNL, vesselCapacities,
						cargoToCargoCostsOnAvailability, cargoVesselRestrictions, cargoToCargoMinTravelTimes,
						cargoMinTravelTimes, loads, discharges);

				if (newScore > currentScore) {
					currentSchedules = newSchedules;
					currentScore = newScore;
					newCargo = mover.getCargo();
					newMover = TabuLightWeightSequenceOptimiserMoves.newInstance(mover);
					newMover.registerDiff();
				}
				if (loggingFlag) {
					series.get(0).add(currentScore);
					series.get(1).add(newScore);
					// series.get(2).add(bestScore);
				}
			}
			if (currentScore > bestScore) {
				bestSchedules = currentSchedules;
				bestScore = currentScore;
				lateness = totalLateness;
				series.get(3).add((double) i);
				bestIteration = i;
			}
			schedules = currentSchedules;
			mover = newMover;

			tabuSet.add(newCargo); // add new element to current set
			mover.removeTabus(tabuSet);
			tabu.add(tabuSet); // add new tabu elements to master set
		}

		Double bestScoreBest = evaluate(bestSchedules, cargoCount, cargoPNL, vesselCapacities,
				cargoToCargoCostsOnAvailability, cargoVesselRestrictions, cargoToCargoMinTravelTimes,
				cargoMinTravelTimes, loads, discharges);

		System.out.printf("\n\nFitness best (re-evaluated): %f\n", bestScoreBest);
		System.out.printf("Lateness best (re-evaluated): %d hrs\n", totalLateness);

		System.out.println("\nOrdering");
		for (List schedule : bestSchedules) {
			System.out.print("Ship " + bestSchedules.indexOf(schedule) + ": ");
			if (schedule.isEmpty()) {
				System.out.print("empty");
			} else {
				for (Object i : schedule) {
					System.out.print((int) i + " ");
				}
			}
			System.out.println();
		}

		return bestSchedules;
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

	private boolean checkRestrictions(List<Integer> sequence, int vessel, List<List<Integer>> cargoVesselRestrictions) {
		for (int c : sequence) {
			if (cargoVesselRestrictions.get(c).contains(vessel)) {
				return false;
			}
		}
		return true;
	}

	private int calculateLatenessOnSequence(List<Integer> sequence, int availability,
			Interval[] loads, Interval[] discharges,
			int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes) {
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

	private double calculateProfitOnSequence(List<Integer> sequence, double capacity, double[] cargoPNL) {
		// return sequence.stream().mapToDouble(s -> cargoPNL[s] * capacity).sum();

		double sum = 0.0f;
		for (int i = 0; i < sequence.size(); i++) {
			sum += cargoPNL[sequence.get(i)] * capacity;
		}
		return sum;
	}

	public Double evaluate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Interval[] loads, Interval[] discharges) {
		double totalCost = 0;
		double totalPNL = 0;
		totalLateness = 0;
		int used = 0;

		for (int availability = 0; availability < sequences.size(); availability++) {
			List<Integer> sequence = sequences.get(availability);
			used += sequence.size();
			// calculate restrictions
			// boolean checkRestrictions = checkRestrictions(sequences.get(availability),
			// availability, cargoVesselRestrictions);
			// if (!checkRestrictions) {
			// return null;
			// }
			// calculate costs
			double cost = calculateCostOnSequence(sequence, availability, cargoToCargoCostsOnAvailability);
			totalCost += cost;
			// calculate lateness
			int lateness = calculateLatenessOnSequence(sequence, availability, loads, discharges, cargoToCargoMinTravelTimes, cargoMinTravelTimes);
			totalLateness += lateness;
			// calculate pnl
			double profit = calculateProfitOnSequence(sequence, vesselCapacities[availability], cargoPNL);
			totalPNL += profit;
		}

		return totalPNL - totalCost - LATENESS_FINE * totalLateness - UNFULFILLED_FINE * (cargoCount - used);
	}

}
