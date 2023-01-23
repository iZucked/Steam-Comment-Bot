/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.BasicCommodityCurveData;
import com.mmxlabs.common.curves.BasicUnitConversionData;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.NamedSeriesASTNode;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.exposures.Constant;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecord;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecords;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class NamedSeriedExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull NamedSeriesASTNode namedSeriesNode, InputRecord inputRecord) {
		final LocalDate date = inputRecord.date();

		final String name = namedSeriesNode.getName().toLowerCase();
		if (inputRecord.lookupData().commodityMap.containsKey(name)) {
			final BasicCommodityCurveData curve = inputRecord.lookupData().commodityMap.get(name);
			if (curve.isSetExpression()) {
				final ASTNode node2 = inputRecord.getExposureCoefficient(curve.getExpression());
				if (node2 == null) {
					throw new NullPointerException();
				}
				return ExposuresASTToCalculator.getExposureNode(node2, inputRecord);
			}

			// Should really look up actual value from curve...
			// Lazy commodity curves should be initialised by now.
			final ISeries series = inputRecord.commodityIndices().getSeries(name).get();
			// The series is in EXTERNAL format
			final Number evaluate = series.evaluate(Hours.between(inputRecord.externalDateProvider().getEarliestTime().toLocalDate(), date), Collections.emptyMap());

			final long unitPrice = OptimiserUnitConvertor.convertToInternalPrice(evaluate.doubleValue());
			long nativeVolume = inputRecord.volumeInMMBTU() * 10;
			long volumeInMMBTU = inputRecord.volumeInMMBTU() * 10;

			// Perform units conversion.
			for (final BasicUnitConversionData factor : inputRecord.lookupData().conversionMap.values()) {
				if (factor.getTo().equalsIgnoreCase(ExposuresCalculatorUtils.MMBTU)) {
					if (factor.getFrom().equalsIgnoreCase(curve.getVolumeUnit())) {
						if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES_IGNORE_ENERGY_CONVERSION)) {
							// If we are in this situation, that means, that volume is *ACTUALLY* in native
							// units
							volumeInMMBTU /= factor.getFactor();
						} else {
							// Long * a double appears to get to Long.MAX_VALUE if we overflow
							nativeVolume *= factor.getFactor();
							if (nativeVolume == Long.MAX_VALUE) {
								throw new ArithmeticException("long overflow");
							}
						}
						break;
					}
				}
			}

			// Client S has a curve (units just $) with values of ~256 causing an overflow.
			// The formulas end up being something like XXX + (0.5 * (256 / 255)) so in
			// practise end up being a small adder on the rest of the price expression.
			// We don't normally want to do this, but an exception is made here to use
			// BigInteger arithmetic to handle the situation
			long costFromVolume;
			try {
				costFromVolume = Calculator.costFromVolume(nativeVolume, unitPrice);
			} catch (final ArithmeticException e) {
				costFromVolume = BigInteger.valueOf(nativeVolume) //
						.multiply(BigInteger.valueOf(unitPrice)) //
						.divide(BigInteger.valueOf(Calculator.HighScaleFactor)) //
						.longValueExact();

			}

			final ExposureRecord record = new ExposureRecord(curve.getName(), curve.getCurrencyUnit(), unitPrice, nativeVolume, costFromVolume, volumeInMMBTU, date, curve.getVolumeUnit());
			return new Pair<>(unitPrice, new ExposureRecords(record));
		} else if (inputRecord.lookupData().currencyList.stream().anyMatch(x -> x.equals(name))) {
			// Should really look up actual value from curve...
			// Currency curves should not be lazy
			final ISeries series = inputRecord.currencyIndices().getSeries(name).get();
			final Number evaluate = series.evaluate(Hours.between(inputRecord.externalDateProvider().getEarliestTime().toLocalDate(), date), Collections.emptyMap());
			final long unitPrice = OptimiserUnitConvertor.convertToInternalPrice(evaluate.doubleValue());
			return new Pair<>(unitPrice, new Constant(1_000_000, ""));
		} else if (inputRecord.lookupData().conversionMap.containsKey(name)) {
			final BasicUnitConversionData factor = inputRecord.lookupData().conversionMap.get(name);
			return new Pair<>((long) (factor.getFactor() * Calculator.HighScaleFactor), new Constant(1_000_000, factor.getTo()));
		} else if (inputRecord.lookupData().reverseConversionMap.containsKey(name)) {
			final BasicUnitConversionData factor = inputRecord.lookupData().reverseConversionMap.get(name);
			return new Pair<>((long) (factor.getFactor() * Calculator.HighScaleFactor), new Constant(1_000_000, factor.getTo()));
		} else {
			throw new IllegalStateException();
		}
	}

}
