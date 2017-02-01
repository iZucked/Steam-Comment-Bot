/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

public class InstrumentingMoveGenerator implements IMoveGenerator {
	private static final Logger log = LoggerFactory.getLogger(InstrumentingMoveGenerator.class);

	private static final int HIT_COUNT = 10000;

	private final boolean collectStats;

	private final IMoveGenerator delegate;

	int nulls = 0;
	int hits = 0;
	int total = 0;
	int totalAccepted = 0;

	long lastPrintTime;
	Class<? extends IMove> lastMoveClass;
	Map<Class<? extends IMove>, Stats> stats = new HashMap<Class<? extends IMove>, Stats>();

	private final class AllMoves implements IMove {
		@SuppressWarnings("null")
		@Override
		@NonNull
		public Collection<@NonNull IResource> getAffectedResources() {
			// TODO: SG - 2014-12-11 Changed from null to empty list for null analysis stuff. Is this correct? I am not sure how this move interacts with the rest of the application.
			return Collections.emptySet();
		}

		@Override
		public void apply(@NonNull final IModifiableSequences sequences) {

		}

		@Override
		public boolean validate(@NonNull final ISequences sequences) {
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

		public Stats(final Class<? extends IMove> moveClass, final long delta, final boolean accepted) {
			this.moveClass = moveClass;
			addSample(delta, accepted);
		}

		public Stats(final Class<? extends IMove> lastMoveClass) {
			this.moveClass = lastMoveClass;

		}

		public void addSample(final long delta, final boolean accepted) {
			moveCount++;
			if (accepted) {
				acceptCount++;
			}
			if (delta < 0) {
				goodMoves++;
				if (accepted) {
					goodAccepted++;
				}
				meanGoodDelta += Math.abs(delta);
			} else if (delta > 0) {
				badMoves++;
				if (accepted) {
					badAccepted++;
				}
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
					moveCount / den, acceptCount / den, goodMoves / den2, badMoves / den2, badAccepted / (0.01 * acceptCount));
		}

		public void moveGenerated() {
			this.generatedCount++;
		}
	}

	public InstrumentingMoveGenerator(final IMoveGenerator delegate, final boolean collectStats, final boolean logToFile) {
		super();
		this.collectStats = collectStats;
		this.delegate = delegate;
		if (logToFile) {
			BufferedWriter output2 = null;
			try {
				final String s = System.currentTimeMillis() + "-moves.log";
				log.debug("logging moves to " + s);
				output2 = new BufferedWriter(new FileWriter(s));
			} catch (final IOException e) {
				// output2 = null;
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
	public IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random) {
		final IMove move = delegate.generateMove(rawSequences, stateManager, random);
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
				output.write("MOVE: " + (move == null ? "null" : move.toString()) + "\n");
			} catch (final IOException e) {
			}
		}
		return move;
	}

	public void notifyOfThresholderDecision(final long delta, final boolean answer) {
		if (answer) {
			totalAccepted++;
		}
		if (collectStats) {
			if (stats.containsKey(lastMoveClass)) {
				stats.get(lastMoveClass).addSample(delta, answer);
			} else {
				stats.put(lastMoveClass, new Stats(lastMoveClass, delta, answer));
			}

			stats.get(AllMoves.class).addSample(delta, answer);
		}

		if (output != null) {
			try {
				output.write("RESULT: " + delta + ", " + answer + "\n");
			} catch (final IOException e) {
			}
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("Move generation stats:\n");

		sb.append("Called " + total + " times, " + nulls + " nulls, " + totalAccepted + " accepted\n");

		sb.append("Timing: " + ((1000 * HIT_COUNT) / (double) lastBatchTime) + " moves/second\n");

		for (final Stats stat : stats.values()) {
			sb.append("\t" + stat.toString() + "\n");
		}

		return sb.toString();
	}
}
