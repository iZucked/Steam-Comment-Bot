/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.metaheuristic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.minimaxlabs.rnd.representation.ShipmentData;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightSequenceOptimiser;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
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
		this(10_000, 1000, 6, 0);
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
	public List<List<Integer>> optimise(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, long[] cargoPNL, Long[][][] cargoToCargoCostsOnAvailability,
			List<Set<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, List<ILightWeightConstraintChecker> constraintCheckers, List<ILightWeightFitnessFunction> fitnessFunctions) {
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
		for (int i = 0; i < cargoToCargoCostsOnAvailability.length; i++) {
			for (int j = 0; j < cargoToCargoCostsOnAvailability[i].length; j++) {
				for (int k = 0; k < cargoToCargoCostsOnAvailability[i][j].length; k++) {
					cargoToCargoCostsProcessed[i][j][k] = (double) cargoToCargoCostsOnAvailability[i][j][k] / 1_000_000.0;
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
		
		createExternalData(cargoes, vessels, cargoToCargoMinTravelTimes, cargoVesselRestrictionAsList, cargoMinTravelTimes, capacity, cargoPNLasDouble, cargoToCargoCostsProcessed, loads, discharges);
		
		return optimise(0, false,
				cargoes.size(), vessels.size(),
				cargoPNLasDouble, capacity,
				cargoToCargoCostsProcessed, cargoVesselRestrictionAsList,
				cargoToCargoMinTravelTimes, cargoMinTravelTimes,
				loads, discharges,
				constraintCheckers, fitnessFunctions);
	}

	private void createExternalData(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, int[][][] cargoToCargoMinTravelTimes, List<List<Integer>> cargoVesselRestrictionAsList, int[][] cargoMinTravelTimes, double[] capacity,
			double[] cargoPNLasDouble, double[][][] cargoToCargoCostsProcessed, Interval[] loads, Interval[] discharges) {
		com.minimaxlabs.rnd.representation.Interval[] arrivals = Arrays.stream(loads).map(a -> new com.minimaxlabs.rnd.representation.Interval((int) a.start, (int) a.end)).toArray(size -> new com.minimaxlabs.rnd.representation.Interval[size]);
		com.minimaxlabs.rnd.representation.Interval[] departures = Arrays.stream(discharges).map(a -> new com.minimaxlabs.rnd.representation.Interval((int) a.start, (int) a.end)).toArray(size -> new com.minimaxlabs.rnd.representation.Interval[size]);
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
		ShipmentData sd = new ShipmentData(cargoes.size(), vessels.size(), cargoPNLasDouble, capacity, cargoToCargoMinTravelTimes, cargoVesselRestrictionsMatrix, cargoToCargoCostsProcessed, cargoMinTravelTimes, arrivals, departures);

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

	public List<List<Integer>> optimise(int id, boolean loggingFlag, int cargoCount, int vesselCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Interval[] loads, Interval[] discharges, List<ILightWeightConstraintChecker> constraintCheckers, List<ILightWeightFitnessFunction> fitnessFunctions) {
        List<HashSet<Integer>> tabu = new LinkedList<>();

        List<List<Integer>> schedule = Arrays.stream(vesselCapacities, 0, vesselCount)
                .mapToObj(v -> (List<Integer>) new LinkedList<Integer>())
                .collect(Collectors.toList());

        List<List<Integer>> bestSchedule = null;

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

            double currentFitness = evaluate(currentSchedules, cargoCount, cargoPNL, vesselCapacities, cargoToCargoCostsOnAvailability,
                    cargoVesselRestrictions, cargoToCargoMinTravelTimes, cargoMinTravelTimes, loads, discharges, constraintCheckers, fitnessFunctions);


            // Search the neighbourhood
            final List<List<Integer>> lambdaSchedule = schedule;
            final List<Integer> lambdaUnusedCargoes = unusedCargoes;
            final List<Integer> lambdaUsedCargoes = usedCargoes;
            final Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> lambdaMapping = mapping;

            tabuSolutions = tabuSolutions.stream().map(t -> {
                    return TabuLightWeightSequenceOptimiserMoves.move(copySolution(lambdaSchedule), lambdaUnusedCargoes, lambdaUsedCargoes, lambdaMapping, random);
            }).collect(Collectors.toList());

            // Evaluate solutions
            fitness = tabuSolutions.parallelStream().mapToDouble(s ->
                    evaluate(s.schedule, cargoCount, cargoPNL, vesselCapacities, cargoToCargoCostsOnAvailability, cargoVesselRestrictions,
                            cargoToCargoMinTravelTimes, cargoMinTravelTimes, loads, discharges, constraintCheckers, fitnessFunctions)).toArray();

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
            }
        }

        // Print final result
        if (unusedCargoes.size() > 0) {
            System.out.println(" Uncomplete solution :"+ unusedCargoes.size());
        }

        Double bestScoreBest = evaluate(bestSchedule, cargoCount, cargoPNL, vesselCapacities, cargoToCargoCostsOnAvailability,
                cargoVesselRestrictions, cargoToCargoMinTravelTimes, cargoMinTravelTimes, loads, discharges, constraintCheckers, fitnessFunctions);

        System.out.printf("\n\nFitness best : %f\n", bestScoreBest);
        System.out.printf("Iteration best: %d it\n", bestIteration);

        printSolution(bestSchedule);
		return bestSchedule;
	}

    Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> getMapping (List<List<Integer>> solution) {
        Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> mapping = new HashMap<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>>();

        for (List<Integer> vessel: solution) {
            for (Integer cargo: vessel) {
                mapping.put(cargo, new AbstractMap.SimpleImmutableEntry<Integer, Integer>(solution.indexOf(vessel), vessel.indexOf(cargo)));
            }
        }
        return mapping;
    }

    void printSolution(List<List<Integer>> solution) {

        System.out.println("\nOrdering");
        for (List vessel : solution) {
            System.out.print("Ship " + solution.indexOf(vessel) + ": ");
            if (vessel.isEmpty()) {
                System.out.print("empty");
            }
            else {
                for (Object i : vessel) {
                    System.out.print( i + " ");
                }
            }
            System.out.println();
        }
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

	public Double evaluate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
	List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Interval[] loads, Interval[] discharges, List<ILightWeightConstraintChecker> constraintCheckers, List<ILightWeightFitnessFunction> fitnessFunctions) {
		
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
			Double evaluate = fitnessFunction.evaluate(sequences, cargoCount, cargoPNL, vesselCapacities, cargoToCargoCostsOnAvailability, cargoVesselRestrictions, cargoToCargoMinTravelTimes, cargoMinTravelTimes, loads, discharges);
			fitness = fitness + evaluate;
		}
		
		return fitness;
	}

}
