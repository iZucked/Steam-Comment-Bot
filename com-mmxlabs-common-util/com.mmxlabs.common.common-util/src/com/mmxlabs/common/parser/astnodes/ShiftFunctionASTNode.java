package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.ShiftFunctionConstructor;

/**
 * @author Mihnea-Teodor
 *
 */
public final class ShiftFunctionASTNode implements ASTNode {

	private ASTNode toShift;
	private final int shiftBy;

	public ShiftFunctionASTNode(ASTNode toShift, Integer shiftBy) {
		this.toShift = toShift;
		this.shiftBy = shiftBy.intValue();
	}

	public ASTNode getToShift() {
		return toShift;
	}

	public int getShiftBy() {
		return shiftBy;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.singletonList(toShift);
	}

	@Override
	public void replace(ASTNode original, ASTNode replacement) {
		if (toShift != original) {
			throw new IllegalArgumentException();
		}
		toShift = replacement;
	}

	@Override
	public @NonNull String asString() {
		return String.format("shift(%s, %d)", toShift.asString(), getShiftBy());
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {
		return new ShiftFunctionConstructor(seriesParser.getSeriesParserData(), toShift.asExpression(seriesParser), shiftBy);
	}
}
