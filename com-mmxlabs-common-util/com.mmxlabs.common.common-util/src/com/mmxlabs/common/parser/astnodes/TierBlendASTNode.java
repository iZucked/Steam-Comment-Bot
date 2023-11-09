/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.DefaultLocation;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.TierBlendFunctionConstructor;
import com.mmxlabs.common.parser.series.TierBlendFunctionConstructor.ThresholdExprSeries;

@NonNullByDefault
public class TierBlendASTNode implements ASTNode {
	// Internal data record. Expression that applies to the target up to and including threshold value. The final band should use Double.MAX_VALUE as the threshold.
	@NonNullByDefault({ DefaultLocation.FIELD, DefaultLocation.RETURN_TYPE }) // Override the class level annotation to avoid compile error
	public record ThresholdSeries(@NonNull ASTNode series, @NonNull Number threshold) {

	}

	private ASTNode target;
	private final List<ThresholdSeries> thresholds;
	private final List<ASTNode> children;

	public TierBlendASTNode(final ASTNode target, final List<ThresholdSeries> thresholds) {

		this.target = target;
		this.thresholds = thresholds;
		children = new ArrayList<>(1 + thresholds.size());
		children.add(target);
		for (final var t : thresholds) {
			children.add(t.series());
		}
	}

	@Override
	public Iterable<ASTNode> getChildren() {
		return children;
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (original == target) {
			target = replacement;
		}
		for (int i = 0; i < thresholds.size(); ++i) {
			final var t = thresholds.get(i);
			if (t.series() == original) {
				thresholds.set(i, new ThresholdSeries(replacement, t.threshold()));
			}
		}
	}

	public ASTNode getTarget() {
		return target;
	}

	@Override
	public String asString() {
		final String args = thresholds.stream().map(t -> {
			if (t.threshold().doubleValue() == Double.MAX_VALUE) {
				return String.format("%s", t.series().asString());
			} else {
				return String.format("%s, %s", t.series().asString(), t.threshold());
			}
		}).collect(Collectors.joining(", "));
		return String.format("TIERBLEND(%s, %s)", target.asString(), args);
	}

	@Override
	public IExpression<ISeries> asExpression(final SeriesParser seriesParser) {

		final List<ThresholdExprSeries> list = thresholds.stream().map(t -> new TierBlendFunctionConstructor.ThresholdExprSeries(t.series().asExpression(seriesParser), t.threshold())).toList();
		return new TierBlendFunctionConstructor(target.asExpression(seriesParser), list);
	}

	public List<ThresholdSeries> getThresholds() {
		return thresholds;

	}
}
