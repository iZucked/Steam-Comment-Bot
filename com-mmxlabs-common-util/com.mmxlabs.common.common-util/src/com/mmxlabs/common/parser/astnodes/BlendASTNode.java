/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.Lists;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.BlendFunctionConstructor;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;

@NonNullByDefault
public class BlendASTNode implements ASTNode {

	private ASTNode target;
	private ASTNode tier1Series;
	private ASTNode tier2Series;
	private final double threshold;

	public BlendASTNode(final ASTNode target, final ASTNode tier1Series, final Number threshold, final ASTNode tier2Series) {

		this.target = target;
		this.tier1Series = tier1Series;
		this.tier2Series = tier2Series;
		this.threshold = threshold.doubleValue();
	}

	@Override
	public Iterable<ASTNode> getChildren() {
		return Lists.newArrayList(target, tier1Series, tier2Series);
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (original == target) {
			target = replacement;
		}
		if (original == tier1Series) {
			tier1Series = replacement;
		}
		if (original == tier2Series) {
			tier2Series = replacement;
		}
	}

	public ASTNode getTarget() {
		return target;
	}

	public Number getThreshold() {
		return threshold;
	}

	public ASTNode getLowTier() {
		return tier1Series;
	}

	public ASTNode getHighTier() {
		return tier2Series;
	}

	@Override
	public String asString() {
		return String.format("BLEND(%s, %s, %s, %s)", target.asString(), tier1Series.asString(), threshold, tier2Series.asString());
	}

	@Override
	public IExpression<ISeries> asExpression(final SeriesParser seriesParser) {
		return new BlendFunctionConstructor(target.asExpression(seriesParser), tier1Series.asExpression(seriesParser), threshold, tier2Series.asExpression(seriesParser));
	}

	public enum ExprSelector {
		LOW, BLEND
	}

	public static ExprSelector select(final double value, final double threshold) {
		if (value <= threshold) {
			return ExprSelector.LOW;
		} else {
			return ExprSelector.BLEND;
		}
	}
}
