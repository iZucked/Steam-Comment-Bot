/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.MonthFunctionASTNode;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class MonthFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final MonthFunctionASTNode monthNode, final InputRecord inputRecord) {
		final LocalDate date = inputRecord.date();
		final LocalDate pricingDate = monthNode.mapTime(date.atStartOfDay()).toLocalDate();
		return ExposuresASTToCalculator.getExposureNode(monthNode.getSeries(), inputRecord.withDate(pricingDate));
	}

}
