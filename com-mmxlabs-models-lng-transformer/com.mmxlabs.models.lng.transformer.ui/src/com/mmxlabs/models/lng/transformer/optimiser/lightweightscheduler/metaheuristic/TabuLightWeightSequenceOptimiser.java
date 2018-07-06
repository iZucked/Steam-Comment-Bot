/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.metaheuristic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import com.minimaxlabs.rnd.representation.ShipmentData;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightCargoDetails;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class TabuLightWeightSequenceOptimiser implements ILightWeightSequenceOptimiser {
	public static final double LATENESS_FINE = 1_000_000;
	public static final double UNFULFILLED_FINE = 10_000_000;

	public static final double MINUTES = 1.0 / 60;
	public static final double HOURS = 1.0 / 3600;

	private int iterations;
	private int maxAge;
	private int search;
	private int seed;
	private Random r;

	public static class Interval {
		private final int start;
		private final int end;

		public Interval(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
			this.start = (int) (startDateTime.toEpochSecond() * HOURS);
			this.end = (int) (endDateTime.toEpochSecond() * HOURS);
		}

		public Interval(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public int getEnd() {
			return end;
		}

		public int getStart() {
			return start;
		}
	}

	public TabuLightWeightSequenceOptimiser() {
		this(10_000, 1000, 15, 0);
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

	@Override
	public List<List<Integer>> optimise(ILightWeightOptimisationData lightWeightOptimisationData, List<ILightWeightConstraintChecker> constraintCheckers,
			List<ILightWeightFitnessFunction> fitnessFunctions, IProgressMonitor monitor) {

		TabuLightWeightSequenceOptimiserDataTransformer dataTransformer = new TabuLightWeightSequenceOptimiserDataTransformer(lightWeightOptimisationData);

		// createExternalData(lightWeightOptimisationData, dataTransformer);

		return optimise(0, false, //
				lightWeightOptimisationData.getCargoes().size(), //
				lightWeightOptimisationData.getVessels().size(), //
				lightWeightOptimisationData, dataTransformer, //
				constraintCheckers, fitnessFunctions, monitor);
	}

	public List<List<Integer>> optimise(int id, boolean loggingFlag, //
			int cargoCount, int vesselCount, //
			ILightWeightOptimisationData lightWeightOptimisationData, //
			TabuLightWeightSequenceOptimiserDataTransformer dataTransformer, //
			List<ILightWeightConstraintChecker> constraintCheckers, //
			List<ILightWeightFitnessFunction> fitnessFunctions, IProgressMonitor monitor) {

		double[] vesselCapacities = lightWeightOptimisationData.getVesselCapacities();
		List<HashSet<Integer>> tabu = new LinkedList<>();

		List<List<Integer>> schedule = Arrays.stream(vesselCapacities, 0, vesselCount).mapToObj(v -> (List<Integer>) new LinkedList<Integer>()).collect(Collectors.toList());

		List<List<Integer>> bestSchedule = Arrays.stream(vesselCapacities, 0, vesselCount).mapToObj(v -> (List<Integer>) new LinkedList<Integer>()).collect(Collectors.toList());

		List<Integer> cargoes = IntStream.range(0, cargoCount).boxed().collect(Collectors.toList());
		List<Integer> unusedCargoes = new ArrayList<>(cargoes);
		List<Integer> usedCargoes = new ArrayList<>();

		List<Integer> tabuFlat;

		Random random = new Random(this.seed);

		double[] fitness = new double[search];
		Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> mapping = getMapping(schedule);
		List<TabuLightWeightSequenceOptimiserMoves.TabuSolution> tabuSolutions = new ArrayList<>(search);

		int bestIteration = 0;

		Double bestFitness = -Double.MAX_VALUE;

		for (int j = 0; j < search; j++) {
			tabuSolutions.add(new TabuLightWeightSequenceOptimiserMoves.TabuSolution(null, null));
		}

		System.out.println("Tabu search starting...\n");
		monitor.beginTask("Search", iterations);
		try {
			for (int iteration = 0; iteration < iterations; iteration++) {
				int currentIndex = -1;

				// Remove tabu element from the list of "movable" element
				HashSet<Integer> tabuSet = new HashSet<>();
				tabuFlat = tabu.stream().flatMap(HashSet::stream).collect(Collectors.toList());

				unusedCargoes.removeAll(tabuFlat);
				usedCargoes.removeAll(tabuFlat);

				// Print progess
				if (iteration % (iterations / 10) == 0) {
					System.out.println(iteration * 100 / iterations + "% ");
				}

				// add back tabu elements
				if (tabu.size() > maxAge) {
					tabu.remove(0);
				}

				// Forced first move
				TabuLightWeightSequenceOptimiserMoves.TabuSolution currentSolution = TabuLightWeightSequenceOptimiserMoves.move(copySolution(schedule), unusedCargoes, usedCargoes, mapping, random);
				List<List<Integer>> currentSchedules = currentSolution.schedule;
				List<Integer> currentTabu = currentSolution.tabu;

				double currentFitness = evaluate(currentSchedules, cargoCount, lightWeightOptimisationData, dataTransformer, constraintCheckers, fitnessFunctions);

				// Search the neighbourhood
				final List<List<Integer>> lambdaSchedule = schedule;
				final List<Integer> lambdaUnusedCargoes = unusedCargoes;
				final List<Integer> lambdaUsedCargoes = usedCargoes;
				final Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> lambdaMapping = mapping;

				tabuSolutions = tabuSolutions.stream().map(t -> {
					return TabuLightWeightSequenceOptimiserMoves.move(copySolution(lambdaSchedule), lambdaUnusedCargoes, lambdaUsedCargoes, lambdaMapping, random);
				}).collect(Collectors.toList());

				// Evaluate solutions
				fitness = tabuSolutions.parallelStream().mapToDouble(s -> evaluate(s.schedule, cargoCount, lightWeightOptimisationData, dataTransformer, constraintCheckers, fitnessFunctions))
						.toArray();

				// Set the new base solution for the next iteration
				for (int i = 0; i < search; i++) {
					if (fitness[i] > currentFitness) {
						currentFitness = fitness[i];
						currentIndex = i;
					}
				}

				if (currentIndex == -1) {
					schedule = copySolution(currentSchedules);
					tabuSet.addAll(currentTabu);
				} else {
					schedule = copySolution(tabuSolutions.get(currentIndex).schedule);
					tabuSet.addAll(tabuSolutions.get(currentIndex).tabu);
				}

				mapping = getMapping(schedule);
				unusedCargoes = updateUnusedList(schedule, cargoes);
				usedCargoes = updateUsedList(schedule);

				// Add the tabuList to the master set
				if (tabuSet.size() > 0) {
					tabu.add(tabuSet);
				}

				// Elitism: keep the overall best solution
				if (currentFitness > bestFitness) {
					bestSchedule = copySolution(schedule);
					bestFitness = currentFitness;
					bestIteration = iteration;
					System.out.println("best found:" + iteration);
				}

				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				monitor.worked(1);
			}

			// Print final result
			if (unusedCargoes.size() > 0) {
				System.out.println(" Uncomplete solution :" + unusedCargoes.size());
			}

			Double bestScoreBest = evaluate(bestSchedule, cargoCount, lightWeightOptimisationData, dataTransformer, constraintCheckers, fitnessFunctions);

			System.out.printf("\n\nFitness best : %f\n", bestScoreBest);
			System.out.printf("Iteration best: %d it\n", bestIteration);

			finalPrint(bestSchedule, cargoCount, vesselCount, lightWeightOptimisationData, dataTransformer, constraintCheckers, fitnessFunctions);

			printSolution(bestSchedule);
			return bestSchedule;
		} finally {
			monitor.done();
		}
	}

	Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> getMapping(List<List<Integer>> solution) {
		Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> mapping = new HashMap<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>>();

		for (List<Integer> vessel : solution) {
			for (Integer cargo : vessel) {
				mapping.put(cargo, new AbstractMap.SimpleImmutableEntry<Integer, Integer>(solution.indexOf(vessel), vessel.indexOf(cargo)));
			}
		}
		return mapping;
	}

	List<Integer> updateUsedList(List<List<Integer>> solution) {
		Set used = new HashSet();

		for (int i = 0; i < solution.size(); i++) {
			for (int j = 0; j < solution.get(i).size(); j++) {
				used.add(solution.get(i).get(j));
			}
		}

		return new ArrayList<Integer>(used);
	}

	List<Integer> updateUnusedList(List<List<Integer>> solution, List<Integer> cargoes) {
		List cargoesSet = new ArrayList(cargoes);
		cargoesSet.removeAll(updateUsedList(solution));

		return cargoesSet;
	}

	List<List<Integer>> copySolution(List<List<Integer>> solution) {
		List<List<Integer>> clone = new ArrayList<>(solution.size());

		for (int i = 0; i < solution.size(); i++) {
			clone.add(new ArrayList<>(solution.get(i)));
		}

		return clone;
	}

	public Double evaluate(List<List<Integer>> sequences, int cargoCount, //
			ILightWeightOptimisationData lightWeightOptimisationData, //
			TabuLightWeightSequenceOptimiserDataTransformer dataTransformer, //
			List<ILightWeightConstraintChecker> constraintCheckers, //
			List<ILightWeightFitnessFunction> fitnessFunctions) {

		Interval[] loads = dataTransformer.getLoads();
		Interval[] discharges = dataTransformer.getDischarges();

		int[][] cargoMinTravelTimes = lightWeightOptimisationData.getCargoMinTravelTimes();
		int[][][] cargoToCargoMinTravelTimes = lightWeightOptimisationData.getCargoToCargoMinTravelTimes();
		double[] volumes = dataTransformer.getCargoesVolumesProcessed();
		LightWeightCargoDetails[] cargoDetails = lightWeightOptimisationData.getCargoDetails();

		List<List<Integer>> cargoVesselRestrictions = dataTransformer.getCargoVesselRestrictionAsList();
		double[] cargoPNL = dataTransformer.getCargoPNLasDouble();
		double[] vesselCapacities = dataTransformer.getCapacity();
		double[][][] cargoToCargoCostsOnAvailability = dataTransformer.getCargoToCargoCostsProcessed();
		double[][] cargoDailyCharterCostPerAvailabilityProcessed = dataTransformer.getCargoDailyCharterCostPerAvailabilityProcessed();

		for (ILightWeightConstraintChecker constraintChecker : constraintCheckers) {
			for (int availability = 0; availability < sequences.size(); availability++) {
				List<Integer> sequence = sequences.get(availability);
				// calculate restrictions
				boolean checkRestrictions = constraintChecker.checkSequence(sequence, availability);
				if (!checkRestrictions) {
					return -Double.MAX_VALUE;
				}
			}
		}

		Double fitness = 0.;
		for (ILightWeightFitnessFunction fitnessFunction : fitnessFunctions) {
			Double evaluate = fitnessFunction.evaluate(sequences, cargoCount, //
					cargoPNL, vesselCapacities, //
					cargoToCargoCostsOnAvailability, cargoVesselRestrictions, //
					cargoToCargoMinTravelTimes, cargoMinTravelTimes, //
					loads, discharges, //
					volumes, cargoDetails, cargoDailyCharterCostPerAvailabilityProcessed);

			fitness = fitness + evaluate;
		}

		return fitness;
	}

	public Double finalPrint(List<List<Integer>> sequences, int cargoCount, int vesselCount, ILightWeightOptimisationData lightWeightOptimisationData,
			TabuLightWeightSequenceOptimiserDataTransformer dataTransformer, List<ILightWeightConstraintChecker> constraintCheckers, List<ILightWeightFitnessFunction> fitnessFunctions) {

		Interval[] loads = dataTransformer.getLoads();
		Interval[] discharges = dataTransformer.getDischarges();

		int[][] cargoMinTravelTimes = lightWeightOptimisationData.getCargoMinTravelTimes();
		int[][][] cargoToCargoMinTravelTimes = lightWeightOptimisationData.getCargoToCargoMinTravelTimes();
		double[] volumes = dataTransformer.getCargoesVolumesProcessed();
		LightWeightCargoDetails[] cargoDetails = lightWeightOptimisationData.getCargoDetails();

		List<List<Integer>> cargoVesselRestrictions = dataTransformer.getCargoVesselRestrictionAsList();
		double[] cargoPNL = dataTransformer.getCargoPNLasDouble();
		double[] vesselCapacities = dataTransformer.getCapacity();
		double[][][] cargoToCargoCostsOnAvailability = dataTransformer.getCargoToCargoCostsProcessed();
		double[][] cargoDailyCharterCostPerAvailabilityProcessed = dataTransformer.getCargoDailyCharterCostPerAvailabilityProcessed();
		for (ILightWeightConstraintChecker constraintChecker : constraintCheckers) {
			for (int availability = 0; availability < sequences.size(); availability++) {
				List<Integer> sequence = sequences.get(availability);
				// calculate restrictions
				boolean checkRestrictions = constraintChecker.checkSequence(sequence, availability);
				if (!checkRestrictions) {
					return -Double.MAX_VALUE;
				}
			}
		}
		Double fitness = 0.;
		for (ILightWeightFitnessFunction fitnessFunction : fitnessFunctions) {
			Double evaluate = fitnessFunction.evaluate(sequences, cargoCount, //
					cargoPNL, vesselCapacities, //
					cargoToCargoCostsOnAvailability, cargoVesselRestrictions, //
					cargoToCargoMinTravelTimes, cargoMinTravelTimes, loads, discharges, //
					volumes, cargoDetails, cargoDailyCharterCostPerAvailabilityProcessed);

			fitness = fitness + evaluate;
		}

		double totalCost = 0;
		double totalPNL = 0;
		long totalLateness = 0;
		int used = 0;

		for (int availability = 0; availability < sequences.size(); availability++) {
			List<Integer> sequence = sequences.get(availability);
			used += sequence.size();
			// calculate costs
			// calculate lateness
			int lateness = calculateLatenessOnSequence(sequence, availability, loads, discharges, cargoToCargoMinTravelTimes, cargoMinTravelTimes);
			totalLateness += lateness;
			// calculate pnl
		}
		System.out.println("lateness:" + totalLateness);

		return fitness;
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

	private void printSolution(List<List<Integer>> solution) {

		System.out.println("\nOrdering");
		for (List vessel : solution) {
			System.out.print("Ship " + solution.indexOf(vessel) + ": ");
			if (vessel.isEmpty()) {
				System.out.print("empty");
			} else {
				for (Object i : vessel) {
					System.out.print(i + " ");
				}
			}
			System.out.println();
		}
	}

	private void createExternalData(ILightWeightOptimisationData lightWeightOptimisationData, TabuLightWeightSequenceOptimiserDataTransformer dataTransformer) {
		Interval[] loads = dataTransformer.getLoads();
		Interval[] discharges = dataTransformer.getDischarges();

		List<List<IPortSlot>> cargoes = lightWeightOptimisationData.getCargoes();
		List<IVesselAvailability> vessels = lightWeightOptimisationData.getVessels();
		int[][] cargoMinTravelTimes = lightWeightOptimisationData.getCargoMinTravelTimes();
		int[][][] cargoToCargoMinTravelTimes = lightWeightOptimisationData.getCargoToCargoMinTravelTimes();

		List<List<Integer>> cargoVesselRestrictionAsList = dataTransformer.getCargoVesselRestrictionAsList();
		double[] cargoPNL = dataTransformer.getCargoPNLasDouble();
		double[] vesselCapacities = dataTransformer.getCapacity();
		double[][][] cargoToCargoCostsOnAvailability = dataTransformer.getCargoToCargoCostsProcessed();

		com.minimaxlabs.rnd.representation.Interval[] arrivals = Arrays.stream(loads).map(a -> new com.minimaxlabs.rnd.representation.Interval((int) a.start, (int) a.end))
				.toArray(size -> new com.minimaxlabs.rnd.representation.Interval[size]);
		com.minimaxlabs.rnd.representation.Interval[] departures = Arrays.stream(discharges).map(a -> new com.minimaxlabs.rnd.representation.Interval((int) a.start, (int) a.end))
				.toArray(size -> new com.minimaxlabs.rnd.representation.Interval[size]);

		// transform restrictions
		boolean[][] cargoVesselRestrictionsMatrix = new boolean[cargoes.size()][vessels.size()];
		for (int cargoId = 0; cargoId < cargoes.size(); cargoId++) {
			// default to true
			for (int vesselId = 0; vesselId < vessels.size(); vesselId++) {
				cargoVesselRestrictionsMatrix[cargoId][vesselId] = true;
			}
			// set restrictions false
			for (int vesselId : cargoVesselRestrictionAsList.get(cargoId)) {
				cargoVesselRestrictionsMatrix[cargoId][vesselId] = false;
			}
		}

		ShipmentData sd = new ShipmentData(cargoes.size(), vessels.size(), //
				cargoPNL, vesselCapacities, cargoToCargoMinTravelTimes, //
				cargoVesselRestrictionsMatrix, cargoToCargoCostsOnAvailability, cargoMinTravelTimes, //
				arrivals, departures);

		try {
			FileOutputStream fileOut = new FileOutputStream("/tmp/shippedData.sd");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(sd);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in /tmp/shippedData.sd");
		} catch (IOException iff) {
			iff.printStackTrace();
		}
	}
}
