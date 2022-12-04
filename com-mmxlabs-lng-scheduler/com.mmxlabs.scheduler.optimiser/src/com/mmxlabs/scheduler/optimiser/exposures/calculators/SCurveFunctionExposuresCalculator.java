/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.SCurveFunctionASTNode;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class SCurveFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final SCurveFunctionASTNode scurveNode, final InputRecord inputRecord) {
		final Pair<Long, IExposureNode> baseNodeData = ExposuresASTToCalculator.getExposureNode(scurveNode.getBase(), inputRecord);

		final double baseValue = baseNodeData.getFirst() / (double) Calculator.HighScaleFactor;
		final double lowCheck = scurveNode.getFirstThreshold();
		final double midCheck = scurveNode.getSecondThreshold();
		var selected = scurveNode.select(baseValue, lowCheck, midCheck);
		return switch (selected) {
		case LOW -> ExposuresASTToCalculator.getExposureNode(scurveNode.getLowerSeries(), inputRecord);
		case MID -> ExposuresASTToCalculator.getExposureNode(scurveNode.getMiddleSeries(), inputRecord);
		case HIGH -> ExposuresASTToCalculator.getExposureNode(scurveNode.getHigherSeries(), inputRecord);
		};
	}

}
