/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

public interface ISeries {
	public int[] getChangePoints();

	public Number evaluate(int point);
}
