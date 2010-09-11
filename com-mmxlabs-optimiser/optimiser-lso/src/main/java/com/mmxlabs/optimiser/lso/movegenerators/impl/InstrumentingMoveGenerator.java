package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

public class InstrumentingMoveGenerator<T> implements IMoveGenerator<T> {
	private final IMoveGenerator<T> delegate;

	int hits = 0;
	
	Class<? extends IMove> lastMoveClass;
	Map<Class<? extends IMove>, Stats> stats = new HashMap<Class<? extends IMove>, Stats>();

	class Stats {
		private Class<? extends IMove> moveClass;

		int moveCount;
		int acceptCount;
		int goodMoves, badMoves;
		double meanBadDelta;
		double meanGoodDelta;

		private int generatedCount = 0;
		
		public Stats(Class<? extends IMove> moveClass, long delta, boolean accepted) {
			this.moveClass = moveClass;
			addSample(delta, accepted);
		}
		
		public void addSample(long delta, boolean accepted) {
			moveCount++;
			if (accepted) {
				acceptCount++;
			}
			if (delta < 0) {
				goodMoves++;
				meanGoodDelta += Math.abs(delta);
			} else {
				badMoves++;
				meanBadDelta += Math.abs(delta);
			}
		}
		
		public String toString() {
			return "Move " + moveClass.getSimpleName() + generatedCount + "generated, " + moveCount + " tested, " + acceptCount + " accepted. Mean good delta "
			 + meanGoodDelta / goodMoves + ", bad delta = " + meanBadDelta / badMoves;
		}

		public void moveGenerated() {
			this.generatedCount ++;
		}
	}
	
	public InstrumentingMoveGenerator(IMoveGenerator<T> delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public IMove<T> generateMove() {
		final IMove<T> move = delegate.generateMove();
		if (move != null) {
			lastMoveClass = move.getClass();
			stats.get(lastMoveClass).moveGenerated();
			hits++;
			if (hits >= 10000) {
				hits = 0;
				System.err.println(this);
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

	@SuppressWarnings("unchecked")
	public void notifyOfThresholderDecision(long delta, boolean answer) {
		if (stats.containsKey(lastMoveClass)) {
			stats.get(lastMoveClass).addSample(delta, answer);
		} else {
			stats.put(lastMoveClass, new Stats(lastMoveClass, delta, answer));
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
	
		for (Stats stat : stats.values()) {
			sb.append(stat.toString() + "\n");
		}
		
		return sb.toString();
	}
}
