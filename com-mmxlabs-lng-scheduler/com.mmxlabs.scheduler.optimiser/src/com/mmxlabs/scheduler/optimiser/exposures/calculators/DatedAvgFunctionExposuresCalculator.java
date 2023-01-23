/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.DatedAvgFunctionASTNode;
import com.mmxlabs.scheduler.optimiser.exposures.Constant;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecord;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecords;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class DatedAvgFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final DatedAvgFunctionASTNode averageNode, final InputRecord inputRecord) {

		final LocalDate date = inputRecord.date();
		final LocalDate startDate = averageNode.mapTimeToStartDate(date.atStartOfDay()).toLocalDate();

		final int months = averageNode.getMonths();
		final ExposureRecords records = new ExposureRecords();
		long price = 0;
		for (int i = 0; i < averageNode.getMonths(); ++i) {
			final Pair<Long, IExposureNode> p = ExposuresASTToCalculator.getExposureNode(averageNode.getSeries(), inputRecord.withDate(startDate.plusMonths(i)));
			if (p.getSecond() instanceof ExposureRecords result) {
				price = Math.addExact(price, p.getFirst());
				result = ExposuresCalculatorUtils.modify(result, c -> new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), c.nativeVolume() / months, c.nativeValue() / months,
						c.mmbtuVolume() / months, c.date(), c.volumeUnit()));
				records.records.addAll(result.records);
			} else if (p.getSecond() instanceof Constant) {
				return new Pair<>(p.getFirst(), p.getSecond());
			}
		}

		return new Pair<>(price / months, records);
	}

}
