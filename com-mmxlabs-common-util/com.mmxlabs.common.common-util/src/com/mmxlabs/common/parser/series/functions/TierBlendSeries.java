/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.astnodes.TierBlendASTNode;
import com.mmxlabs.common.parser.astnodes.TierBlendASTNode.ExprSelector;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public class TierBlendSeries implements ISeries {

	private final ISeries targetSeries;
	private final ISeries tier1Series;
	private final ISeries tier2Series;
	private final double threshold;

	private final int[] changePoints;

	private Set<String> parameters;

	public TierBlendSeries(final @NonNull ISeries targetSeries, @NonNull final ISeries tier1Series, final double threshold, @NonNull final ISeries tier2Series) {
		this.targetSeries = targetSeries;
		this.tier1Series = tier1Series;
		this.threshold = threshold;
		this.tier2Series = tier2Series;

		this.changePoints = SeriesUtil.mergeChangePoints(targetSeries, tier1Series, tier2Series);

		parameters = new HashSet<>();

		parameters.addAll(targetSeries.getParameters());
		parameters.addAll(tier1Series.getParameters());
		parameters.addAll(tier2Series.getParameters());

	}

	@Override
	public boolean isParameterised() {
		return true;
	}

	@Override
	public Set<String> getParameters() {
		return parameters;
	}

	@Override
	public int[] getChangePoints() {
		return changePoints;
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {

		final Number volume = targetSeries.evaluate(timePoint, params);
		if (volume == null) {
			return tier1Series.evaluate(timePoint, params);
		}
		double inputVolume = volume.doubleValue();

		final ExprSelector selected = TierBlendASTNode.select(inputVolume, threshold);
		switch (selected) {
		case LOW:
			return tier1Series.evaluate(timePoint, params);
		case BLEND:
			// Get the tier prices
			final Number a = tier1Series.evaluate(timePoint, params);
			final Number b = tier2Series.evaluate(timePoint, params);

			// Compute the combined value across tiers
			double v = (a.doubleValue() * threshold) //
					+ (b.doubleValue() * (inputVolume - threshold));
			// Divide by total volume to get price again
			v = v / inputVolume;
			return v;
		default:
			throw new IllegalStateException();
		}
	}
}
