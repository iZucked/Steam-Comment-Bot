/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesUtil;

public class TierBlendSeries implements ISeries {

	public record ThresholdISeries(ISeries series, double threshold) {
	}

	private final ISeries targetSeries;
	private final List<ThresholdISeries> thresholds;

	private final int[] changePoints;
	private final Set<String> parameters;

	public TierBlendSeries(final @NonNull ISeries targetSeries, final List<ThresholdISeries> thresholds) {
		this.targetSeries = targetSeries;
		this.thresholds = thresholds;

		final List<ISeries> l = new LinkedList<>();
		l.add(targetSeries);
		thresholds.forEach(t -> l.add(t.series()));
		this.changePoints = SeriesUtil.mergeChangePoints(l);

		parameters = new HashSet<>();

		parameters.addAll(targetSeries.getParameters());
		thresholds.forEach(t -> parameters.addAll(t.series().getParameters()));

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
			return thresholds.get(0).series().evaluate(timePoint, params);
		}
		final double inputVolume = volume.doubleValue();

		double sumValue = 0.0;
		int count = 0;
		double usedVolume = 0.0;
		for (final var t : thresholds) {
			// Increment count for average
			++count;
			// Evaluate the price
			final Number a = t.series().evaluate(timePoint, params);
			if (count == 1 && inputVolume <= t.threshold()) {
				// Break out early as we are covered by the first band
				return a;
			}

			double contrib;
			boolean breakLoop = false;
			// Determine how much volume is needed for this band
			if (t.threshold == Double.MAX_VALUE) {
				contrib = inputVolume - usedVolume;
			} else {
				// Check for partial band use
				contrib = Math.min(inputVolume, t.threshold()) - usedVolume;
				breakLoop = inputVolume <= t.threshold();
				// Reset to the current threshold level
				usedVolume = t.threshold();
			}
			// Compute the value contribution
			final double v = (a.doubleValue() * contrib);
			sumValue += v;

			if (breakLoop) {
				break;
			}
		}

		return sumValue / inputVolume;
	}
}
