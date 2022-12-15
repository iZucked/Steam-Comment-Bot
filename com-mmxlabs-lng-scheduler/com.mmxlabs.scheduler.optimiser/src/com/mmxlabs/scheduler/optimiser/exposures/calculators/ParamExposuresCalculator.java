/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.ParamASTNode;
import com.mmxlabs.scheduler.optimiser.exposures.Constant;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class ParamExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final ParamASTNode paramNode, final InputRecord inputRecord) {
		
		if ("cv".equalsIgnoreCase(paramNode.getName())) {
			return new Pair<>((long)inputRecord.cargoCV(), new Constant(inputRecord.cargoCV(), ""));
		}
		
		final long constant = 0L;
		return new Pair<>(constant, new Constant(constant, ""));
	}
}
