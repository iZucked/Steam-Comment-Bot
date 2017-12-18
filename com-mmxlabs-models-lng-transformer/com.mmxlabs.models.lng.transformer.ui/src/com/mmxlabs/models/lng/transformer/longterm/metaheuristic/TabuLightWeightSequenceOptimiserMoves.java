package com.mmxlabs.models.lng.transformer.longterm.metaheuristic;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TabuLightWeightSequenceOptimiserMoves {

	public static class TabuSolution {
		public List<List<Integer>> schedule;
		public List<Integer> tabu;

		public TabuSolution(List<List<Integer>> schedule, List<Integer> tabu) {
			this.schedule = schedule;
			this.tabu = tabu;
		}

	}

	public TabuLightWeightSequenceOptimiserMoves() {
	}


	static public TabuSolution move(List<List<Integer>> sequences, List<Integer> unusedCargoes, List<Integer> usedCargoes, Random random) {
		double d = random.nextDouble();

		if (unusedCargoes.size() > 0) {
			if (usedCargoes.size() > 1) {
				int choice = random.nextInt(4);
				if (choice == 0) {
					return insertUnusedCargo(sequences, unusedCargoes, random);
				} else if (choice == 1) {
					return moveCargo(sequences, random);
				} else if (choice == 2) {
					return swapCargo(sequences, random);
				} else if (choice == 3) {
					return removeUsedCargo(sequences, random);
				}
			} else {
				return insertUnusedCargo(sequences, unusedCargoes, random);
			}
		} else {
			int choice = random.nextInt(3);

			if (choice == 0) {
				return removeUsedCargo(sequences, random);
			} else if (choice == 1) {
				return moveCargo(sequences, random);
			} else if (choice == 2) {
				return swapCargo(sequences, random);
			}
		}

		return new TabuSolution(sequences, new ArrayList<>());
	}


	static private TabuSolution insertUnusedCargo(List<List<Integer>> sequences, List<Integer> unusedCargoes, Random random) {
		final int vessel = random.nextInt(sequences.size());
		List<Integer> sequence = sequences.get(vessel);

		// Select an unused cargo at random
		final int cargoIndex = random.nextInt(unusedCargoes.size());
		final int cargo = unusedCargoes.get(cargoIndex);

		try {
			sequence.add(random.nextInt(sequence.size() + 1), cargo);
		} catch(Exception e) {
			throw new UnsupportedOperationException();
		}

		return new TabuSolution(sequences, Arrays.asList(cargo));
	}


	static private TabuSolution removeUsedCargo(List<List<Integer>> sequences, Random random) {
		int vesselIndex = random.nextInt(sequences.size());

		while (sequences.get(vesselIndex).size() == 0) {
			vesselIndex = random.nextInt(sequences.size());
		}

		final int cargoIndex = random.nextInt(sequences.get(vesselIndex).size());

		final int cargo = sequences.get(vesselIndex).get(cargoIndex);
		sequences.get(vesselIndex).remove(cargoIndex);

		return new TabuSolution(sequences, Arrays.asList(cargo));
	}


	static private TabuSolution moveCargo(List<List<Integer>> sequences, Random random) {
		// Select and remove cargo
		int vesselIndex = random.nextInt(sequences.size());

		while (sequences.get(vesselIndex).size() == 0) {
			vesselIndex = random.nextInt(sequences.size());
		}

		final int cargoIndex = random.nextInt(sequences.get(vesselIndex).size());

		final int cargo = sequences.get(vesselIndex).get(cargoIndex);
		sequences.get(vesselIndex).remove(cargoIndex);

		//Insert cargo
		int newVesselIndex = random.nextInt(sequences.size());

		while (newVesselIndex == vesselIndex) {
			newVesselIndex = random.nextInt(sequences.size());
		}

		int newCargoIndex = 0;

		if (sequences.get(newVesselIndex).size() != 0) {
			newCargoIndex = random.nextInt(sequences.get(newVesselIndex).size());
		}

		sequences.get(newVesselIndex).add(newCargoIndex, cargo);

		return new TabuSolution(sequences, Arrays.asList(cargo));
	}


	static private TabuSolution swapCargo(List<List<Integer>> sequences, Random random) {
		final List<Integer> tabu = new ArrayList<>();

		int firstVesselIdx = random.nextInt(sequences.size());
		while (sequences.get(firstVesselIdx).size() == 0) {
			firstVesselIdx = random.nextInt(sequences.size());
		}

		int secondVesselIdx = random.nextInt(sequences.size());

		while (sequences.get(secondVesselIdx).size() == 0 || secondVesselIdx == firstVesselIdx) {
			secondVesselIdx = random.nextInt(sequences.size());
		}

		final int firstVesselSize = sequences.get(firstVesselIdx).size();
		final int secondVesselSize = sequences.get(secondVesselIdx).size();

		if (firstVesselIdx == secondVesselIdx || firstVesselSize == 0 || secondVesselSize == 0) {
			return new TabuSolution(sequences, new ArrayList<Integer>());
		} else {
			final int cargoIdxFirstVessel = random.nextInt(firstVesselSize);
			final int cargoIdxSecondVessel = random.nextInt(secondVesselSize);

			final int cargoFirstVessel = sequences.get(firstVesselIdx).get(cargoIdxFirstVessel);
			final int cargoSecondVessel = sequences.get(secondVesselIdx).get(cargoIdxSecondVessel);

			tabu.add(cargoFirstVessel);
			tabu.add(cargoSecondVessel);
			sequences.get(firstVesselIdx).set(cargoIdxFirstVessel, cargoSecondVessel);
			sequences.get(secondVesselIdx).set(cargoIdxSecondVessel, cargoFirstVessel);
		}
		return new TabuSolution(sequences, tabu);
	}


	static private TabuSolution mergeRoute(List<List<Integer>> sequences, List<Integer> tabuList, Random random) {
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

		for(int i = startFirstVessel; i < sequences.get(firstVesselIdx).size(); i++) {
			if (tabuList.contains(sequences.get(firstVesselIdx).get(i))) {
				containsTabu = true;
				break;
			}
		}

		for(int i = startSecondVessel; i < sequences.get(secondVesselIdx).size(); i++) {
			if (tabuList.contains(sequences.get(secondVesselIdx).get(i))) {
				containsTabu = true;
				break;
			}
		}

		if (!containsTabu) {
			//moveCargo(sequences, srcVessel, destVessel, idx);
		}

		return new TabuSolution(sequences, tabu);
	}

}
