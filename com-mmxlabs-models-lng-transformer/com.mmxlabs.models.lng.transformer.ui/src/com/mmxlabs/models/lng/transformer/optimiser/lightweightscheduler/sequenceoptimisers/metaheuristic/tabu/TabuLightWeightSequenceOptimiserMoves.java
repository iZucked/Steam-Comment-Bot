/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu.CargoMap.CargoCoordinates;

public class TabuLightWeightSequenceOptimiserMoves {

	public TabuLightWeightSequenceOptimiserMoves() {
	}

	public static TabuSolution move(List<List<Integer>> inputSequences, List<Integer> unusedCargoes, List<Integer> usedCargoes, CargoMap mapping, Random random) {
		List<List<Integer>> sequences = cloneSolution(inputSequences);

		boolean multipleVessels = sequences.size() > 1;
		if (!unusedCargoes.isEmpty()) {
			if (usedCargoes.size() > 1) {

				int choice = random.nextInt(multipleVessels ? 5 : 4);
				if (choice == 0) {
					return insertUnusedCargo(sequences, unusedCargoes, random);
				} else if (choice == 1) {
					return moveCargo(sequences, usedCargoes, mapping, random);
				} else if (choice == 2) {
					return removeUsedCargo(sequences, usedCargoes, mapping, random);
				} else if (choice == 3) {
					return replaceUsedWithUnusedCargo(sequences, usedCargoes, unusedCargoes, mapping, random);
				} else if (choice == 4) {
					return swapCargo(sequences, usedCargoes, mapping, random);
				}
			} else {
				return insertUnusedCargo(sequences, unusedCargoes, random);
			}
		} else if (!usedCargoes.isEmpty()) {
			int choice = random.nextInt((multipleVessels && usedCargoes.size() > 1) ? 3 : 2);

			if (choice == 0) {
				return removeUsedCargo(sequences, usedCargoes, mapping, random);
			} else if (choice == 1) {
				return moveCargo(sequences, usedCargoes, mapping, random);
			} else if (choice == 2 && usedCargoes.size() > 1) {
				return swapCargo(sequences, usedCargoes, mapping, random);
			}
		}

		return new TabuSolution(sequences, new ArrayList<>());
	}

	private static TabuSolution insertUnusedCargo(List<List<Integer>> sequences, List<Integer> unusedCargoes, Random random) {
		final int vessel = random.nextInt(sequences.size());
		List<Integer> sequence = sequences.get(vessel);

		// Select an unused cargo at random
		final int cargoIndex = random.nextInt(unusedCargoes.size());
		final int cargo = unusedCargoes.get(cargoIndex);

		try {
			sequence.add(random.nextInt(sequence.size() + 1), cargo);
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}

		return new TabuSolution(sequences, Arrays.asList(cargo));
	}

	private static TabuSolution removeUsedCargo(List<List<Integer>> sequences, List<Integer> usedCargoes, CargoMap mapping, Random random) {
		final int choiceIndex = random.nextInt(usedCargoes.size());
		final int cargo = usedCargoes.get(choiceIndex);
		CargoCoordinates coords = mapping.getCoordinates(cargo);
		sequences.get(coords.vessel).remove(coords.position);

		return new TabuSolution(sequences, Arrays.asList(cargo));
	}

	private static TabuSolution moveCargo(List<List<Integer>> sequences, List<Integer> usedCargoes, CargoMap mapping, Random random) {

		if (sequences.size() < 2) {
			// Null move
			return new TabuSolution(sequences, new ArrayList<>());
		}

		// Select and remove cargo
		final int choiceIndex = random.nextInt(usedCargoes.size());
		final int cargo = usedCargoes.get(choiceIndex);

		CargoCoordinates coords = mapping.getCoordinates(cargo);

		sequences.get(coords.vessel).remove(coords.position);

		// Insert cargo
		int newVesselIndex = random.nextInt(sequences.size());
		int newCargoIndex = 0;

		while (newVesselIndex == coords.vessel) {
			newVesselIndex = random.nextInt(sequences.size());
		}

		if (sequences.get(newVesselIndex).size() != 0) {
			newCargoIndex = random.nextInt(sequences.get(newVesselIndex).size());
		}

		sequences.get(newVesselIndex).add(newCargoIndex, cargo);

		return new TabuSolution(sequences, Arrays.asList(cargo));
	}

	private static TabuSolution swapCargo(List<List<Integer>> sequences, List<Integer> usedCargoes, CargoMap mapping, Random random) {
		final List<Integer> tabu = new ArrayList<>();

		final int choiceAIndex = random.nextInt(usedCargoes.size());
		final Integer cargoA = usedCargoes.get(choiceAIndex);

		int choiceBIndex = random.nextInt(usedCargoes.size());

		while (choiceAIndex == choiceBIndex) {
			choiceBIndex = random.nextInt(usedCargoes.size());
		}

		final Integer cargoB = usedCargoes.get(choiceBIndex);

		CargoCoordinates coordsA = mapping.getCoordinates(cargoA);
		CargoCoordinates coordsB = mapping.getCoordinates(cargoB);

		sequences.get(coordsA.vessel).set(coordsA.position, cargoB);
		sequences.get(coordsB.vessel).set(coordsB.position, cargoA);

		tabu.add(cargoA);
		tabu.add(cargoB);

		return new TabuSolution(sequences, tabu);
	}

	private static TabuSolution replaceUsedWithUnusedCargo(List<List<Integer>> sequences, List<Integer> usedCargoes, List<Integer> unusedCargoes, CargoMap mapping, Random random) {
		final List<Integer> tabu = new ArrayList<>();

		final int choiceUsedIndex = random.nextInt(usedCargoes.size());
		final Integer usedCargo = usedCargoes.get(choiceUsedIndex);

		CargoCoordinates coords = mapping.getCoordinates(usedCargo);
		int choiceUnusedIndex = random.nextInt(unusedCargoes.size());

		final Integer unusedCargo = unusedCargoes.get(choiceUnusedIndex);

		sequences.get(coords.vessel).set(coords.position, unusedCargo);

		tabu.add(unusedCargo);
		tabu.add(usedCargo);

		return new TabuSolution(sequences, tabu);
	}

	private static TabuSolution mergeRoute(List<List<Integer>> sequences, List<Integer> tabuList, Random random) {
		final List<Integer> tabu = new ArrayList<>();
		// Select two sequence
		// WIP
		// reservoir sampling here

		final int firstVesselIdx = random.nextInt(sequences.size());
		final int secondVesselIdx = random.nextInt(sequences.size());

		if (firstVesselIdx == secondVesselIdx) {
			// Do something smart
		}

		final int endFirstVessel = random.nextInt(sequences.get(firstVesselIdx).size());
		final int startFirstVessel = random.nextInt(endFirstVessel);

		final int endSecondVessel = random.nextInt(sequences.get(secondVesselIdx).size());
		final int startSecondVessel = random.nextInt(endSecondVessel);

		boolean containsTabu = false;

		for (int i = startFirstVessel; i < sequences.get(firstVesselIdx).size(); i++) {
			if (tabuList.contains(sequences.get(firstVesselIdx).get(i))) {
				containsTabu = true;
				break;
			}
		}

		for (int i = startSecondVessel; i < sequences.get(secondVesselIdx).size(); i++) {
			if (tabuList.contains(sequences.get(secondVesselIdx).get(i))) {
				containsTabu = true;
				break;
			}
		}

		if (!containsTabu) {
			// moveCargo(sequences, srcVessel, destVessel, idx);
		}

		return new TabuSolution(sequences, tabu);
	}

	private static List<List<Integer>> cloneSolution(List<List<Integer>> solution) {
		List<List<Integer>> clone = new ArrayList<>(solution.size());

		for (int i = 0; i < solution.size(); i++) {
			clone.add(new ArrayList<>(solution.get(i)));
		}

		return clone;
	}

}