/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.time.LocalDateTime;
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

	public enum InputMode {
		FUNCTION, NOTATION
	}

	private ASTNode toShift;
	private final int shiftBy;

	private final InputMode inputMode;

	public ShiftFunctionASTNode(final ASTNode toShift, final Integer shiftBy, final InputMode inputMode) {
		this.toShift = toShift;
		this.inputMode = inputMode;
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
	public void replace(final ASTNode original, final ASTNode replacement) {
		if (toShift != original) {
			throw new IllegalArgumentException();
		}
		toShift = replacement;
	}

	@Override
	public @NonNull String asString() {
		return switch (inputMode) {
		case FUNCTION -> String.format("shift(%s, %d)", toShift.asString(), getShiftBy());
		case NOTATION -> String.format("%s[m%d]", toShift.asString(), -getShiftBy());
		};
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull final SeriesParser seriesParser) {
		return new ShiftFunctionConstructor(seriesParser.getSeriesParserData(), toShift.asExpression(seriesParser), shiftBy);
	}

	public LocalDateTime mapTime(final LocalDateTime currentTime) {
		return mapTime(currentTime, getShiftBy());
	}

	public static LocalDateTime mapTime(final LocalDateTime currentTime, final int shifyBy) {
		return currentTime.minusMonths(shifyBy);
	}
}
