/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.modules.LWSTabuOptimiserModule;

public class TabuLightWeightSequenceOptimiser implements ILightWeightSequenceOptimiser {
	@Inject
	@Named(LWSTabuOptimiserModule.GLOBAL_ITERATIONS)
	private int iterations;
	@Inject
	@Named(LWSTabuOptimiserModule.TABU_LIST_SIZE)
	private int maxAge;
	@Inject
	@Named(LWSTabuOptimiserModule.NEIGHBOURHOOD_ITERATIONS)
	private int search;
	@Inject
	@Named(LWSTabuOptimiserModule.SEED)
	private int seed;

	@Override
	public List<List<Integer>> optimise(ILightWeightOptimisationData lightWeightOptimisationData, List<ILightWeightConstraintChecker> constraintCheckers,
			List<ILightWeightFitnessFunction> fitnessFunctions, IProgressMonitor monitor) {
		int cargoCount = lightWeightOptimisationData.getCargoes().size();
		int vesselCount = lightWeightOptimisationData.getVessels().size();

		TabuList tabuList = new TabuList();
		
		List<List<Integer>> schedule = lightWeightOptimisationData.getVessels()
				.stream().map(v -> new LinkedList<Integer>()).collect(Collectors.toList());
		TabuSolution currentSolution = new TabuSolution(schedule, null);
		TabuSolution bestSolution = new TabuSolution(new ArrayList<>(schedule), null);
		long currentFitness = evaluate(currentSolution.schedule, cargoCount, lightWeightOptimisationData,
				constraintCheckers, fitnessFunctions, false);
		
		currentSolution.fitness = currentFitness;
		bestSolution.fitness = currentFitness;

		Set<Integer> allCargoes = IntStream.range(0, cargoCount).boxed()
				.collect(Collectors.toSet());

		Random random = new Random(this.seed);

		long[] neighbourhoodFitnesses = new long[search];
		CargoMap mapping = new CargoMap(currentSolution.schedule);

		int bestIteration = 0;

		System.out.println("Tabu search starting...\n");
		monitor.beginTask("Search", iterations);
		try {
			for (int iteration = 0; iteration < iterations; iteration++) {
				// init data
				List<Integer> unusedCargoes = updateUnusedList(currentSolution.schedule, allCargoes);
				List<Integer> usedCargoes = updateUsedList(currentSolution.schedule);
				mapping.setMapping(currentSolution.schedule);

				// Remove tabu element from the list of "movable" elements
				Set<Integer> currentTabuSolutions = tabuList.getTabuSet();
				unusedCargoes.removeAll(currentTabuSolutions);
				usedCargoes.removeAll(currentTabuSolutions);

				// Print progess
				if (iteration % (iterations / 10) == 0) {
					System.out.println(iteration * 100 / iterations + "% ");
				}

				// un-tabu elements
				if (tabuList.sizeOfList() > maxAge) {
					tabuList.removeOldestEntry();
				}

				// Create new candidate schedules
				// TODO: not threadsafe. Cannot use a parallel stream due to the random object being reused.
				List<TabuSolution> tabuSolutions = getNewCandidateSolutions(currentSolution.schedule, unusedCargoes, usedCargoes, random, mapping);

				// Evaluate solutions
				evaluateTabuSolutions(lightWeightOptimisationData, constraintCheckers, fitnessFunctions, cargoCount, tabuSolutions);

				// Set the new base solution for the next iteration
				int currentIndex = getBestUniqueValidIndex(currentSolution, tabuSolutions);
				// set the new schedule and add the tabuList to the master set
				if (currentIndex != -1) {
					// Add the tabuList to the master set
					tabuList.addToTabu(tabuSolutions.get(currentIndex).tabu);
					currentFitness = tabuSolutions.get(currentIndex).fitness;
					currentSolution = tabuSolutions.get(currentIndex);
				}

				// Record the overall best solution (a Tabu search will necessarily lose it) 
				if (currentFitness > bestSolution.fitness) {
					bestSolution = currentSolution.copy();
					bestIteration = iteration;
					System.out.println("best found:" + iteration);
				}

				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				monitor.worked(1);
			}

			System.out.println("\n\nFitness best : " + bestSolution.fitness);
			System.out.println("Iteration best: %d it\n" + bestIteration);

			annotateSolution(bestSolution.schedule, cargoCount, vesselCount, lightWeightOptimisationData, constraintCheckers, fitnessFunctions, allCargoes);

			return bestSolution.schedule;
		} finally {
			monitor.done();
		}
	}

	private void evaluateTabuSolutions(ILightWeightOptimisationData lightWeightOptimisationData, List<ILightWeightConstraintChecker> constraintCheckers,
			List<ILightWeightFitnessFunction> fitnessFunctions, int cargoCount, List<TabuSolution> tabuSolutions) {
		tabuSolutions.parallelStream().forEach(s -> {
			s.fitness = evaluate(s.schedule, cargoCount, lightWeightOptimisationData,
				constraintCheckers, fitnessFunctions, false);
			}
		);
	}

	private int getBestUniqueValidIndex(TabuSolution currentSolution, List<TabuSolution> tabuSolutions) {
		int currentIndex = -1;
		long bestNeighbourhoodFitness = Long.MIN_VALUE;
		for (int i = 0; i < tabuSolutions.size(); i++) {
			TabuSolution solution = tabuSolutions.get(i);
			if (solution.fitness > bestNeighbourhoodFitness
					&& !currentSolution.equals(solution)) {
				bestNeighbourhoodFitness = solution.fitness;
				currentIndex = i;
			}
		}
		return currentIndex;
	}

	/**
	 * Applies pertubations to an initial solution
	 * TODO: not threadsafe. Cannot use a parallel stream due to the random object being reused.
	 * @param schedule
	 * @param unusedCargoes
	 * @param usedCargoes
	 * @param random
	 * @param mapping
	 * @return
	 */
	private List<TabuSolution> getNewCandidateSolutions(final List<List<Integer>> schedule, final List<Integer> unusedCargoes,
			final List<Integer> usedCargoes, Random random, CargoMap mapping) {
		List<TabuSolution> tabuSolutions = IntStream.range(0, search).mapToObj(t -> {
			return TabuLightWeightSequenceOptimiserMoves.move(schedule, unusedCargoes, usedCargoes, mapping, random);
		}).collect(Collectors.toCollection(ArrayList::new));
		return tabuSolutions;
	}

	private long evaluate(List<List<Integer>> sequences, int cargoCount, //
			ILightWeightOptimisationData lightWeightOptimisationData, //
			List<ILightWeightConstraintChecker> constraintCheckers, //
			List<ILightWeightFitnessFunction> fitnessFunctions,
			boolean annotate) {
				
		for (ILightWeightConstraintChecker constraintChecker : constraintCheckers) {
			for (int availability = 0; availability < sequences.size(); availability++) {
				List<Integer> sequence = sequences.get(availability);
				// calculate restrictions
				boolean checkRestrictions = constraintChecker.checkSequence(sequence, availability);
				if (!checkRestrictions) {
					return -Long.MAX_VALUE;
				}
			}
		}
	
		long fitness = 0L;
		for (ILightWeightFitnessFunction fitnessFunction : fitnessFunctions) {
			final long evaluate;
			
			if (!annotate) {
				evaluate = fitnessFunction.evaluate(sequences);
			} else {
				evaluate = fitnessFunction.annotate(sequences);
			}
			fitness = fitness + evaluate;
		}
	
		return fitness;
	}

	private long annotateSolution(List<List<Integer>> sequences, int cargoCount, int vesselCount, ILightWeightOptimisationData lightWeightOptimisationData,
			List<ILightWeightConstraintChecker> constraintCheckers, List<ILightWeightFitnessFunction> fitnessFunctions, Set<Integer> allCargoes) {
		printSolution(sequences);
		System.out.println(String.format("Left over cargoes: %s",
				updateUnusedList(sequences, allCargoes).size()));
		return evaluate(sequences, cargoCount, lightWeightOptimisationData, constraintCheckers, fitnessFunctions, true);
	}

	private List<Integer> updateUsedList(List<List<Integer>> solution) {
		Set<Integer> used = new HashSet<>();

		for (int i = 0; i < solution.size(); i++) {
			for (int j = 0; j < solution.get(i).size(); j++) {
				used.add(solution.get(i).get(j));
			}
		}

		return new ArrayList<Integer>(used);
	}

	private List<Integer> updateUnusedList(List<List<Integer>> solution, Set<Integer> cargoes) {
		List<Integer> cargoesSet = new ArrayList<Integer>(cargoes);
		cargoesSet.removeAll(updateUsedList(solution));

		return cargoesSet;
	}

	private List<List<Integer>> copySchedule(List<List<Integer>> solution) {
		List<List<Integer>> clone = new ArrayList<>(solution.size());

		for (int i = 0; i < solution.size(); i++) {
			clone.add(new ArrayList<>(solution.get(i)));
		}

		return clone;
	}

	private void printSolution(List<List<Integer>> solution) {
		System.out.println("\nOrdering");
		for (List<Integer> vessel : solution) {
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
	
	private void printSolutionToFile(List<List<Integer>> solution) {
		DateTime date = DateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/sequenceTABU-%s-%s-%s.txt", date.getHourOfDay(), date.getMinuteOfHour(), date.getSecondOfMinute()), "UTF-8");
	
			writer.println("\nOrdering");
			for (List<Integer> vessel : solution) {
				writer.print("Ship " + solution.indexOf(vessel) + ": ");
				if (vessel.isEmpty()) {
					writer.print("empty");
				} else {
					for (Object i : vessel) {
						writer.print(i + " ");
					}
				}
				writer.println();
			}
			writer.close();
		} catch (Exception e) {
			
		}
	}

}
