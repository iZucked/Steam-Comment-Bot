/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.jdt.annotation.NonNull;

public class LightWeightSchedulerMoves {
	Random random = new Random(0);
	
	public List<List<Integer>> move(List<List<Integer>> sequences, List<Integer> cargoes) {
		return move(sequences, cargoes, this.random);
	}
	
	public static List<List<Integer>> move(List<List<Integer>> sequences, List<Integer> cargoes, Random random) {
		List<List<Integer>> newSequences = copySequences(sequences);
		double d = random.nextDouble();
		if (d < 0.33) {
			return insertUnusedCargo(newSequences, cargoes, random);
		} else if (d < 0.66) {
			return removeUsedCargo(newSequences, cargoes, random);
		} else {
			return moveCargo(newSequences, cargoes, random);
		}
	}
	
	public static List<List<Integer>> copySequences(List<List<Integer>> sequences) {
		List<List<Integer>> l = new ArrayList<>();
		for (List<Integer> old : sequences) {
			l.add(new ArrayList<>(old));
		}
		return l;
	}
	
	public static List<List<Integer>> insertUnusedCargo(List<List<Integer>> sequences, List<Integer> cargoes, Random random) {
		List<Integer> unusedCargoes = new LinkedList<>(cargoes);
		for (List<Integer> sequence : sequences) {
			for (Integer cargo : sequence) {
				unusedCargoes.remove(cargo);
			}
		}
		if (unusedCargoes.size() == 0) {
			return sequences;
		}
		int newCargo = unusedCargoes.get(random.nextInt(unusedCargoes.size()));
		insertCargo(sequences, random, newCargo);
		return sequences;
	}

	public static List<List<Integer>> removeUsedCargo(List<List<Integer>> sequences, List<Integer> cargoes, Random random) {
		List<Integer> usedSequences = getUsedSequences(sequences);
		if (usedSequences.size() > 0) {
			int sequenceIdx = usedSequences.get(random.nextInt(usedSequences.size()));
			List<Integer> sequence = sequences.get(sequenceIdx);
			sequence.remove(random.nextInt(sequence.size()));
		}
		return sequences;
	}

	public static List<List<Integer>> moveCargo(List<List<Integer>> sequences, List<Integer> cargoes, Random random) {
		List<Integer> usedSequences = getUsedSequences(sequences);
		if (usedSequences.size() > 0) {
			int sequenceIdx = usedSequences.get(random.nextInt(usedSequences.size()));
			List<Integer> sequenceToMove = sequences.get(sequenceIdx);
			@NonNull
			Integer removedCargo = sequenceToMove.remove(random.nextInt(sequenceToMove.size()));
			insertCargo(sequences, random, removedCargo);
		}
		return sequences;
	}

	private static void insertCargo(List<List<Integer>> sequences, Random random, int newCargo) {
		List<Integer> sequence = sequences.get(random.nextInt(sequences.size()));
		sequence.add(random.nextInt(sequence.size() + 1), newCargo);
	}

	private static List<Integer> getUsedSequences(List<List<Integer>> sequences) {
		List<Integer> usedSequences = IntStream.range(0, sequences.size())
				.filter(i-> sequences.get(i).size() > 0)
				.mapToObj(i->i)
				.collect(Collectors.toCollection(ArrayList::new));
		return usedSequences;
	}

	
}
