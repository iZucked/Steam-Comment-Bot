/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.IExpression;

@NonNullByDefault
public interface IExpressionContainer extends ISeriesContainer {

	void setExpression(IExpression<ISeries> expression);
}
