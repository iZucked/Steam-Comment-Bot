/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

public class InstrumentingMoveGenerator<T> implements IMoveGenerator<T> {
	private static final int HIT_COUNT = 500;

	private final boolean collectStats;

	private final IMoveGenerator<T> delegate;

	int nulls = 0;
	int hits = 0;
	int total = 0;
	int totalAccepted = 0;
	
	long lastPrintTime;
	Class<? extends IMove> lastMoveClass;
	Map<Class<? extends IMove>, Stats> stats = new HashMap<Class<? extends IMove>, Stats>();

	private long lastBatchTime;

	class Stats {
		private Class<? extends IMove> moveClass;

		int moveCount;
		int acceptCount;
		int goodMoves, badMoves;
		int zMoves;

		double meanBadDelta;
		double meanGoodDelta;

		private int generatedCount = 0;

		public Stats(Class<? extends IMove> moveClass, long delta,
				boolean accepted) {
			this.moveClass = moveClass;
			addSample(delta, accepted);
		}

		public Stats(Class<? extends IMove> lastMoveClass) {
			this.moveClass = lastMoveClass;

		}

		public void addSample(long delta, boolean accepted) {
			moveCount++;
			if (accepted) {
				acceptCount++;
			}
			if (delta < 0) {
				goodMoves++;
				meanGoodDelta += Math.abs(delta);
			} else if (delta > 0) {
				badMoves++;
				meanBadDelta += Math.abs(delta);
			} else {
				zMoves++;
			}
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			return String
					.format("%s : %d generated, %d accepted, %d bad accepted, %.2f%% feasible, %.2f%% accepted. %.2f%% improving, %.2f%% detrimental",
							moveClass.getSimpleName(), generatedCount,
							acceptCount, acceptCount - (goodMoves + zMoves),
							moveCount / (0.01 * generatedCount), acceptCount
									/ (0.01 * generatedCount), goodMoves
									/ (0.01 * generatedCount), badMoves
									/ (0.01 * generatedCount));
			// return "Move " + moveClass.getSimpleName() +": "+ generatedCount
			// + " generated, " + moveCount + " tested, " + acceptCount +
			// " accepted. Mean good delta "
			// + meanGoodDelta / goodMoves + ", bad delta = " + meanBadDelta /
			// badMoves;
		}

		public void moveGenerated() {
			this.generatedCount++;
		}
	}

	public InstrumentingMoveGenerator(IMoveGenerator<T> delegate,
			boolean collectStats) {
		super();
		this.collectStats = collectStats;
		this.delegate = delegate;
	}

	@Override
	public IMove<T> generateMove() {
		final IMove<T> move = delegate.generateMove();
		total++;
		if (move != null) {
			if (collectStats) {
				lastMoveClass = move.getClass();
				if (!stats.containsKey(lastMoveClass)) {
					stats.put(lastMoveClass, new Stats(lastMoveClass));
				}
				stats.get(lastMoveClass).moveGenerated();
			}
			if (hits == 0) {
				lastBatchTime = System.currentTimeMillis();
			}
			hits++;
			if (hits >= HIT_COUNT) {
				hits = 0;
				lastBatchTime = System.currentTimeMillis() - lastBatchTime;
				System.err.println(this);
			}
		} else {
			nulls++;
		}
		return move;
	}

	@Override
	public ISequences<T> getSequences() {
		return delegate.getSequences();
	}

	@Override
	public void setSequences(ISequences<T> sequences) {
		delegate.setSequences(sequences);
	}

	@SuppressWarnings("unchecked")
	public void notifyOfThresholderDecision(long delta, boolean answer) {
		if (answer)
			totalAccepted++;
		if (collectStats) {
			if (stats.containsKey(lastMoveClass)) {
				stats.get(lastMoveClass).addSample(delta, answer);
			} else {
				stats.put(lastMoveClass,
						new Stats(lastMoveClass, delta, answer));
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Stats stat : stats.values()) {
			sb.append(stat.toString() + "\n");
		}
		sb.append("Called " + total + " times, " + nulls + " nulls, " + totalAccepted + " accepted\n");

		sb.append("Timing: " + 1000 * HIT_COUNT / (double) lastBatchTime
				+ " moves/second");

		return sb.toString();
	}
}
