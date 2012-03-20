package com.mmxlabs.common.parser.series;

public interface ISeries {
	public int[] getChangePoints();
	public Number evaluate(int point);
}
