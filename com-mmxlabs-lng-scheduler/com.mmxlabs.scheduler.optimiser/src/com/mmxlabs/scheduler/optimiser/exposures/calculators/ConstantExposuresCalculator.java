/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.ConstantASTNode;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.exposures.Constant;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class ConstantExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final ConstantASTNode constantNode, final InputRecord inputRecord) {
		final long constant = OptimiserUnitConvertor.convertToInternalPrice(constantNode.getConstant().doubleValue());
		return new Pair<>(constant, new Constant(constant, ""));
	}

}
