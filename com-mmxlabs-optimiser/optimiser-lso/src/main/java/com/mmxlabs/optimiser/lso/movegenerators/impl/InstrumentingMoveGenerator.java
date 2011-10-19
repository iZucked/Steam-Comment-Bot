/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

public class InstrumentingMoveGenerator<T> implements IMoveGenerator<T> {
	private static final Logger log = LoggerFactory.getLogger(InstrumentingMoveGenerator.class);
	
	private static final int HIT_COUNT = 10000;

	private final boolean collectStats;

	private final IMoveGenerator<T> delegate;

	int nulls = 0;
	int hits = 0;
	int total = 0;
	int totalAccepted = 0;
	
	long lastPrintTime;
	Class<? extends IMove> lastMoveClass;
	Map<Class<? extends IMove>, Stats> stats = new HashMap<Class<? extends IMove>, Stats>();

	private final class AllMoves implements IMove {
		@Override
		public Collection getAffectedResources() {
			return null;
		}

		@Override
		public void apply(IModifiableSequences sequences) {

		}

		@Override
		public boolean validate(ISequences sequences) {
			return false;
		}
	}

	private long lastBatchTime;

	private final BufferedWriter output;
	
	class Stats {
		private final Class<? extends IMove> moveClass;

		int moveCount;
		int acceptCount;
		int goodMoves, badMoves;
		int zMoves;

		int goodAccepted, badAccepted;

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
				if (accepted)
					goodAccepted++;
				meanGoodDelta += Math.abs(delta);
			} else if (delta > 0) {
				badMoves++;
				if (accepted)
					badAccepted++;
				meanBadDelta += Math.abs(delta);
			} else {
				zMoves++;
			}
		}

		@Override
		public String toString() {
			final double den = 0.01 * generatedCount;
			final double den2 = 0.01 * moveCount;
			return String.format("%s: : %d generated, %.2f%% evaluated, %.2f%% accepted. Of feasible, %.2f%% good, %.2f%% bad. %.2f%% of acc are bad", moveClass.getSimpleName(), generatedCount,
					moveCount / den,
					acceptCount / den, goodMoves / den2, badMoves / den2, badAccepted / (0.01 * acceptCount));
		}

		public void moveGenerated() {
			this.generatedCount++;
		}
	}

	public InstrumentingMoveGenerator(IMoveGenerator<T> delegate,
			boolean collectStats, boolean logToFile) {
		super();
		this.collectStats = collectStats;
		this.delegate = delegate;
		if (logToFile) {
			BufferedWriter output2 = null;
			try {
				final String s =System.currentTimeMillis() + "-moves.log";
				log.debug("logging moves to " + s);
				output2 = new BufferedWriter(new FileWriter(s));
			} catch (IOException e) {
				output2 = null;
			}
			output = output2;
		} else {
			output = null;
		}

		reset();
	}

	public void reset() {
		stats.clear();
		stats.put(AllMoves.class, new Stats(AllMoves.class));

		totalAccepted = 0;
		total = 0;
		nulls = 0;
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
				stats.get(AllMoves.class).moveGenerated();
			}
			if (hits == 0) {
				lastBatchTime = System.currentTimeMillis();
			}
			hits++;
			if (hits >= HIT_COUNT) {
				hits = 0;
				lastBatchTime = System.currentTimeMillis() - lastBatchTime;
				log.debug(this.toString());
				reset();
			}
		} else {
			nulls++;
		}
		if (output != null) {
			try {
				output.write("MOVE: " +  (move == null ? "null" : move.toString()) + "\n");
			} catch (IOException e) {
			}
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

			stats.get(AllMoves.class).addSample(delta, answer);
		}
		
		if (output != null) {
			try {
				output.write("RESULT: " + delta + ", " + answer + "\n");
			} catch (IOException e) {
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Move generation stats:\n");

		sb.append("Called " + total + " times, " + nulls + " nulls, " + totalAccepted + " accepted\n");

		sb.append("Timing: " + 1000 * HIT_COUNT / (double) lastBatchTime + " moves/second\n");

		for (Stats stat : stats.values()) {
			sb.append("\t" + stat.toString() + "\n");
		}

		return sb.toString();
	}
}
