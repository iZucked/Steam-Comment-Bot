/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.metaheuristic;


import java.util.*;

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


	static public TabuSolution move(List<List<Integer>> sequences, List<Integer> unusedCargoes, List<Integer> usedCargoes, Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> mapping, Random random) {
		double d = random.nextDouble();

		if (unusedCargoes.size() > 0) {
			if (usedCargoes.size() > 1) {
				int choice = random.nextInt(5);
				if (choice == 0) {
					return insertUnusedCargo(sequences, unusedCargoes, random);
				} else if (choice == 1) {
					return moveCargo(sequences, usedCargoes, mapping, random);
				} else if (choice == 2) {
					return swapCargo(sequences, usedCargoes, mapping, random);
				} else if (choice == 3) {
					return removeUsedCargo(sequences, usedCargoes, mapping, random);
				} else if (choice == 4) {
					return replaceWithUnusedCargo(sequences, usedCargoes, unusedCargoes, mapping, random);
				}
			} else {
				return insertUnusedCargo(sequences, unusedCargoes, random);
			}
		} else if (usedCargoes.size() > 0){
			int choice = random.nextInt(3);

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


	static private TabuSolution removeUsedCargo(List<List<Integer>> sequences, List<Integer> usedCargoes, Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> mapping, Random random) {
		if (usedCargoes.size() < 1) {
			int z = 0;
		}
		final int choiceIndex = random.nextInt(usedCargoes.size());
		final int cargo = usedCargoes.get(choiceIndex);

		sequences.get(mapping.get(cargo).getKey()).remove((int) mapping.get(cargo).getValue());

		return new TabuSolution(sequences, Arrays.asList(cargo));
	}


	static private TabuSolution moveCargo(List<List<Integer>> sequences,  List<Integer> usedCargoes, Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> mapping, Random random) {
		// Select and remove cargo
		final int choiceIndex = random.nextInt(usedCargoes.size());
		final int cargo = usedCargoes.get(choiceIndex);

		sequences.get(mapping.get(cargo).getKey()).remove((int) mapping.get(cargo).getValue());

		//Insert cargo
		int newVesselIndex = random.nextInt(sequences.size());
		int newCargoIndex = 0;

		while (newVesselIndex == mapping.get(cargo).getKey()) {
			newVesselIndex = random.nextInt(sequences.size());
		}

		if (sequences.get(newVesselIndex).size() != 0) {
			newCargoIndex = random.nextInt(sequences.get(newVesselIndex).size());
		}

		sequences.get(newVesselIndex).add(newCargoIndex, cargo);

		return new TabuSolution(sequences, Arrays.asList(cargo));
	}


	static private TabuSolution swapCargo(List<List<Integer>> sequences,   List<Integer> usedCargoes, Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> mapping, Random random) {
		final List<Integer> tabu = new ArrayList<>();

		final int choiceAIndex = random.nextInt(usedCargoes.size());
		final Integer cargoA = usedCargoes.get(choiceAIndex);

		int choiceBIndex = random.nextInt(usedCargoes.size());

		while(choiceAIndex == choiceBIndex) {
			choiceBIndex = random.nextInt(usedCargoes.size());
		}

		final Integer cargoB = usedCargoes.get(choiceBIndex);

		sequences.get(mapping.get(cargoA).getKey()).set(mapping.get(cargoA).getValue(), cargoB);
		sequences.get(mapping.get(cargoB).getKey()).set(mapping.get(cargoB).getValue(), cargoA);

		tabu.add(cargoA);
		tabu.add(cargoB);

		return new TabuSolution(sequences, tabu);
	}

	static private TabuSolution replaceWithUnusedCargo(List<List<Integer>> sequences, List<Integer> usedCargoes, List<Integer> unusedCargoes, Map<Integer, AbstractMap.SimpleImmutableEntry<Integer, Integer>> mapping, Random random) {
		final List<Integer> tabu = new ArrayList<>();

		final int choiceUsedIndex = random.nextInt(usedCargoes.size());
		final Integer usedCargo = usedCargoes.get(choiceUsedIndex);

		int choiceUnusedIndex = random.nextInt(unusedCargoes.size());

		final Integer unusedCargo = unusedCargoes.get(choiceUnusedIndex);

		sequences.get(mapping.get(usedCargo).getKey()).set(mapping.get(usedCargo).getValue(), unusedCargo);

		tabu.add(unusedCargo);
		tabu.add(usedCargo);

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