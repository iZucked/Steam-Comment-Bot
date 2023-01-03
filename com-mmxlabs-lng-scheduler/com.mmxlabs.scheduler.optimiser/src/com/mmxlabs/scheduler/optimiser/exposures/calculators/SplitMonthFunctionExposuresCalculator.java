/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.SplitMonthFunctionASTNode;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class SplitMonthFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final SplitMonthFunctionASTNode splitNode, final InputRecord inputRecord) {

		final int dayOfMonth = inputRecord.date().getDayOfMonth();
		if (dayOfMonth < splitNode.getSplitPoint()) {
			final Pair<Long, IExposureNode> pc0 = ExposuresASTToCalculator.getExposureNode(splitNode.getSeries1(), inputRecord);
			return pc0;
		} else {
			final Pair<Long, IExposureNode> pc1 = ExposuresASTToCalculator.getExposureNode(splitNode.getSeries2(), inputRecord);
			return pc1;
		}
	}

}
