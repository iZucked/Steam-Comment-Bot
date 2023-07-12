/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.SwitchASTNode;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class SwitchFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final SwitchASTNode switchNode, final InputRecord inputRecord) {

		if (inputRecord.date().isBefore(switchNode.getDate())) {
			return ExposuresASTToCalculator.getExposureNode(switchNode.getLHS(), inputRecord);
		} else {
			return ExposuresASTToCalculator.getExposureNode(switchNode.getRHS(), inputRecord);
		}
	}

}
