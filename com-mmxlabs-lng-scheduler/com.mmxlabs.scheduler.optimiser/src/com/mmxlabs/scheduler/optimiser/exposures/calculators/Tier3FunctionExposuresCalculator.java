package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.Tier3FunctionASTNode;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class Tier3FunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final Tier3FunctionASTNode tierNode, final InputRecord inputRecord) {
		final Pair<Long, IExposureNode> baseNodeData = ExposuresASTToCalculator.getExposureNode(tierNode.getTarget(), inputRecord);

		final double baseValue = baseNodeData.getFirst() / (double) Calculator.HighScaleFactor;
		final double lowCheck = tierNode.getLow().doubleValue();
		final double midCheck = tierNode.getMid().doubleValue();
		var selected = tierNode.select(baseValue, lowCheck, midCheck);
		return switch (selected) {
		case LOW -> ExposuresASTToCalculator.getExposureNode(tierNode.getLowValue(), inputRecord);
		case MID -> ExposuresASTToCalculator.getExposureNode(tierNode.getMidValue(), inputRecord);
		case HIGH -> ExposuresASTToCalculator.getExposureNode(tierNode.getHighValue(), inputRecord);
		};
	}

}
