/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class ShiftFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull ShiftFunctionASTNode shiftNode, InputRecord inputRecord) {
		final LocalDate date = inputRecord.date();
		final LocalDate pricingDate = shiftNode.mapTime(date.atStartOfDay()).toLocalDate();
		return ExposuresASTToCalculator.getExposureNode(shiftNode.getToShift(), inputRecord.withDate(pricingDate));
	}

}
