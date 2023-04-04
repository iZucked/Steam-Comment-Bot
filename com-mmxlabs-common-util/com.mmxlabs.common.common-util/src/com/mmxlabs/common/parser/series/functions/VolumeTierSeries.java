/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.nio.channels.IllegalSelectorException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.astnodes.ComparisonOperators;
import com.mmxlabs.common.parser.astnodes.VolumeTierASTNode;
import com.mmxlabs.common.parser.astnodes.VolumeTierASTNode.ExprSelector;
import com.mmxlabs.common.parser.series.ISeries;

public class VolumeTierSeries implements ISeries {

	public static final String PARAM_VOLUME_M3 = "VOLUME_M3";
	public static final String PARAM_VOLUME_MMBTU = "VOLUME_MMBTU";

	private final ISeries tier1Series;
	private final ISeries tier2Series;
	private final double threshold;
	private final boolean isM3Volume;

	private final int[] changePoints;

	private Set<String> parameters;
	private @NonNull ComparisonOperators op;

	public VolumeTierSeries(final boolean isM3Volume, @NonNull final ISeries tier1Series, final @NonNull ComparisonOperators op, final double threshold, @NonNull final ISeries tier2Series) {
		this.isM3Volume = isM3Volume;
		this.tier1Series = tier1Series;
		this.op = op;
		this.threshold = threshold;
		this.tier2Series = tier2Series;

		// Combine the two change points arrays
		this.changePoints = IntStream.concat(IntStream.of(tier1Series.getChangePoints()), IntStream.of(tier2Series.getChangePoints())) //
				.sorted() // Time sorted!
				.distinct() // Unique values
				.toArray();

		parameters = new HashSet<>();

		if (isM3Volume) {
			parameters.add(PARAM_VOLUME_M3);
		} else {
			parameters.add(PARAM_VOLUME_MMBTU);
		}

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

		final String volume;
		if (isM3Volume) {
			volume = params.get(PARAM_VOLUME_M3);
		} else {
			volume = params.get(PARAM_VOLUME_MMBTU);
		}
		if (volume == null) {
			return tier1Series.evaluate(timePoint, params);
		}

		final double inputVolume = Double.parseDouble(volume);

		final ExprSelector selected = VolumeTierASTNode.select(inputVolume, op, threshold);
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
