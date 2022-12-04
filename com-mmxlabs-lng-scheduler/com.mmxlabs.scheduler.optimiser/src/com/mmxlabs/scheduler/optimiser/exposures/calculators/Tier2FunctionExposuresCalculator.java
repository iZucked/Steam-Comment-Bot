/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.SCurveFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.Tier2FunctionASTNode;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class Tier2FunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final Tier2FunctionASTNode tierNode, final InputRecord inputRecord) {
		final Pair<Long, IExposureNode> baseNodeData = ExposuresASTToCalculator.getExposureNode(tierNode.getTarget(), inputRecord);

		final double baseValue = baseNodeData.getFirst() / (double) Calculator.HighScaleFactor;
		final double lowCheck = tierNode.getLow().doubleValue();
		var selected = tierNode.select(baseValue, lowCheck);
		return switch (selected) {
		case LOW -> ExposuresASTToCalculator.getExposureNode(tierNode.getLowValue(), inputRecord);
		case HIGH -> ExposuresASTToCalculator.getExposureNode(tierNode.getHighValue(), inputRecord);
		};
	}

}
