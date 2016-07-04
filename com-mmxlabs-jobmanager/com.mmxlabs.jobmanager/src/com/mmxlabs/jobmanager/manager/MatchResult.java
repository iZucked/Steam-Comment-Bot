/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.manager;

public final class MatchResult implements Comparable<MatchResult> {

	private final int score;
	private final IJobManager manager;

	public MatchResult(final IJobManager manager, final int score) {
		this.manager = manager;
		this.score = score;
	}

	@Override
	public final int compareTo(final MatchResult o) {
		return score - o.score;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MatchResult) {
			final MatchResult mr = (MatchResult) obj;
			if (score != mr.score) {
				return false;
			}
			return manager.equals(mr.manager);
		}
		return false;
	}

	@Override
	public int hashCode() {
		// TODO: Implement properly
		return super.hashCode();
	}

	public final int getScore() {
		return score;
	}

	public final IJobManager getManager() {
		return manager;
	}
}
