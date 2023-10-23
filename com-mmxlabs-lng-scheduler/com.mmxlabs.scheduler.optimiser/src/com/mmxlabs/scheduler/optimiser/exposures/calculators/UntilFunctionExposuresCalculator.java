/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.UntilASTNode;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class UntilFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final UntilASTNode untilNode, final InputRecord inputRecord) {

		if (inputRecord.date().isBefore(untilNode.getDate())) {
			return ExposuresASTToCalculator.getExposureNode(untilNode.getLHS(), inputRecord);
		} else {
			return ExposuresASTToCalculator.getExposureNode(untilNode.getRHS(), inputRecord);
		}
	}

}
