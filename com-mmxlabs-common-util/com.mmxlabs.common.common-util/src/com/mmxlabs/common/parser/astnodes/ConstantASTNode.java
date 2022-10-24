package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ConstantSeriesExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;

@NonNullByDefault
public final class ConstantASTNode implements ASTNode {

	private final Number constant;

	public Number getConstant() {
		return constant;
	}

	public ConstantASTNode(final Number constant) {
		this.constant = constant;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {
		throw new IllegalStateException();
	}

	@Override
	public String asString() {
		return "" + constant;
	}

	@Override
	public IExpression<ISeries> asExpression(final SeriesParser seriesParser) {
		return new ConstantSeriesExpression(getConstant());
	}
}
