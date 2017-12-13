package com.mmxlabs.models.lng.transformer.longterm.metaheuristic;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class TabuLightWeightSequenceOptimiserMoves {


	private Random random;
	private int cargo = 0;
	private int cargoIndex = 0;									// keep index of last accessed element
	private Integer vessel = null;

	private Map<Integer, Integer> pairings;						// cargo-vessel map
	private List<Integer> usedCargoes = new LinkedList<>();		// used+unused add up to all available cargos
	private List<Integer> unusedCargoes;

	public TabuLightWeightSequenceOptimiserMoves(Random random, int cargoCount) {
		this.random = random;
		this.pairings = IntStream
				.range(0, cargoCount).boxed()
				.collect(Collectors.toMap(i -> i, i -> -1));	// initialise to unassigned
		this.unusedCargoes = IntStream
				.range(0, cargoCount).boxed()
				.collect(Collectors.toList());
	}

	public TabuLightWeightSequenceOptimiserMoves(Random random, int cargo, int cargoIndex, Integer vessel,
									 Map<Integer, Integer> pairings, List<Integer> usedCargoes, List<Integer> unusedCargoes, boolean swap, int cargoFirstVessel, int cargoSecondVessel, int firstVesselIdx, int secondVesselIdx) {
		this.random = random;
		this.cargo = cargo;
		this.cargoIndex = cargoIndex;
		this.vessel = vessel;
		this.pairings = pairings;
		this.usedCargoes = usedCargoes;
		this.unusedCargoes = unusedCargoes;
		this.swap = swap;
		this.firstVesselIdx = firstVesselIdx;
		this.secondVesselIdx = secondVesselIdx;
		this.cargoFirstVessel = cargoFirstVessel;
		this.cargoSecondVessel = cargoSecondVessel;
	}

	public int getCargo() {
		return cargo;
	}

	public Random getRandom() {
		return random;
	}

	public int getCargoIndex() {
		return cargoIndex;
	}

	public Integer getVessel() {
		return vessel;
	}

	public Map<Integer, Integer> getPairings() {
		return pairings;
	}

	public List<Integer> getUsedCargoes() {
		return usedCargoes;
	}

	public List<Integer> getUnusedCargoes() {
		return unusedCargoes;
	}

	/**
	 * Create a defensive copy of the object
	 */
	public static TabuLightWeightSequenceOptimiserMoves newInstance(TabuLightWeightSequenceOptimiserMoves moves) {
		return new TabuLightWeightSequenceOptimiserMoves(moves.getRandom(), moves.getCargo(), moves.getCargoIndex(),
				moves.getVessel(), new HashMap<>(moves.getPairings()), new LinkedList<>(moves.getUsedCargoes()),
				new LinkedList<>(moves.getUnusedCargoes()),
				moves.swap,
				moves.cargoFirstVessel,
				moves.cargoSecondVessel,
				moves.firstVesselIdx,
				moves.secondVesselIdx);
	}

	/**
	 * Only use after calling move() to update the change in cargo allocations
	 */
	public void registerDiff() {

		if (swap) {
			pairings.put(cargoFirstVessel, secondVesselIdx);
			pairings.put(cargoSecondVessel, firstVesselIdx);
			swap = false;
		} else {
			if (vessel != null) {
				// remove cargo
				if (vessel == -1) {
					usedCargoes.remove(cargoIndex);
					unusedCargoes.add(cargo);
				}
				// insert cargo
				else if (pairings.get(cargo).equals(-1)) {
					unusedCargoes.remove(cargoIndex);
					usedCargoes.add(cargo);
				}
				pairings.put(cargo, vessel);
				vessel = null;
			}
		}
		// swap
	}

	public void rejectDiff() {
		vessel = null;
	}

	/**
	 * Update used and unused with new changes
	 */
	public void addTabus(HashSet<Integer> features) {
		features.forEach(i -> {
			if (pairings.get(i).equals(-1)) {
				unusedCargoes.add(i);
			}
			else {
				usedCargoes.add(i);
			}
		});
	}

	public void removeTabus(HashSet<Integer> features) {
		unusedCargoes = unusedCargoes.stream()
				.filter(i -> !features.contains(i))
				.collect(Collectors.toList());
		usedCargoes = usedCargoes.stream()
				.filter(i -> !features.contains(i))
				.collect(Collectors.toList());
	}

	public List<List<Integer>> move(List<List<Integer>> sequences) {
		List<List<Integer>> newSequences = copySequences(sequences);
		double d = random.nextDouble();
		boolean noUselessMove = true;

		if (noUselessMove) {
			if (unusedCargoes.size() > 0) {
				if (d < 1.0 / 2) {
					return insertUnusedCargo(newSequences);
				//} else if (d < 2.0 / 3) {
				//	return swapCargo(newSequences);
				} else {
					return moveCargo(newSequences);
				}
			} else {
				if (d < 1.0 / 2) {
					return removeUsedCargo(newSequences);
				} else {
					return moveCargo(newSequences);
				}
				//else {
				//	return newSequences;
					//return swapCargo(newSequences);
				//}
			}
		} else {
            if (d < 1.0 / 3) {
                return insertUnusedCargo(newSequences);
            } else if (d < 2.0 / 3) {
                return removeUsedCargo(newSequences);
            } else {
                return moveCargo(newSequences);
            }
		}
	}

	private List<List<Integer>> insertUnusedCargo(List<List<Integer>> sequences) {
		if (unusedCargoes.size() > 0) {
			cargoIndex = random.nextInt(unusedCargoes.size());
			cargo = unusedCargoes.get(cargoIndex);
			insertCargo(sequences);
		}
		return sequences;
	}

	private List<List<Integer>> removeUsedCargo(List<List<Integer>> sequences) {
		if (usedCargoes.size() > 0) {
			cargoIndex = random.nextInt(usedCargoes.size());
			cargo = usedCargoes.get(cargoIndex);
			// linear search
			int index = sequences.get(pairings.get(cargo)).indexOf(cargo);
			sequences.get(pairings.get(cargo)).remove(index);
			vessel = -1;
		}
		return sequences;
	}

	private List<List<Integer>> moveCargo(List<List<Integer>> sequences) {
		if (usedCargoes.size() > 0) {
			removeUsedCargo(sequences);
			insertCargo(sequences);
		}
		return sequences;
	}

	boolean swap = false;
	int cargoFirstVessel = -1;
	int cargoSecondVessel = -1;

	int firstVesselIdx = -1 ;
	int secondVesselIdx = -1;
	private List<List<Integer>> swapCargo(List<List<Integer>> sequences) {

		firstVesselIdx = random.nextInt(sequences.size());
		secondVesselIdx = random.nextInt(sequences.size());

		final int firstVesselSize = sequences.get(firstVesselIdx).size();
		final int secondVesselSize = sequences.get(secondVesselIdx).size();

		if (firstVesselIdx == secondVesselIdx || firstVesselSize == 0 || secondVesselSize == 0) {
			return sequences;
		} else {
		    swap = true;
			final int cargoIdxFirstVessel = random.nextInt(firstVesselSize);
			final int cargoIdxSecondVessel = random.nextInt(secondVesselSize);

			cargoFirstVessel = sequences.get(firstVesselIdx).get(cargoIdxFirstVessel);
			cargoSecondVessel = sequences.get(secondVesselIdx).get(cargoIdxSecondVessel);

			sequences.get(firstVesselIdx).set(cargoIdxFirstVessel, cargoSecondVessel);
			sequences.get(secondVesselIdx).set(cargoIdxSecondVessel, cargoFirstVessel);
		}
		return sequences;
	}

	private List<List<Integer>> mergeRoute(List<List<Integer>> sequences) {
		// Select two sequence
		// WIP
		// reservoir sampling here
		final int firstVesselIdx = random.nextInt(pairings.size());
		final int secondVesselIdx = random.nextInt(pairings.size());

		if (firstVesselIdx == secondVesselIdx) {
			// Do something smart
		}

		final int endFirstVessel = random.nextInt(sequences.get(firstVesselIdx).size());
		final int startFirstVessel = random.nextInt(endFirstVessel);

		final int endSecondVessel = random.nextInt(sequences.get(secondVesselIdx).size());
		final int startSecondVessel = random.nextInt(endSecondVessel);

		boolean containsTabu = false;

		for(int i = startFirstVessel; i < sequences.get(firstVesselIdx).size(); i++) {
			if (!usedCargoes.contains(sequences.get(firstVesselIdx).get(i))) {
				containsTabu = true;
				break;
			}
		}

		for(int i = startSecondVessel; i < sequences.get(secondVesselIdx).size(); i++) {
			if (!usedCargoes.contains(sequences.get(secondVesselIdx).get(i))) {
				containsTabu = true;
				break;
			}
		}

		if (!containsTabu) {
			//moveCargo(sequences, srcVessel, destVessel, idx);
		}

		if (usedCargoes.size() > 0) {
			removeUsedCargo(sequences);
			insertCargo(sequences);
		}
		return sequences;
	}

	private void insertCargo(List<List<Integer>> sequences) {
		vessel = random.nextInt(sequences.size());
		List<Integer> sequence = sequences.get(vessel);
		try {
			sequence.add(random.nextInt(sequence.size() + 1), cargo);
		} catch(Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	private List<List<Integer>> copySequences(List<List<Integer>> sequences) {
		List<List<Integer>> l = new LinkedList<>();
		for (List<Integer> old : sequences) {
			List<Integer> l2 = new LinkedList<>();
			l2.addAll(old);
			l.add(l2);
		}
		return l;
	}
}
