/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.ParamASTNode;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.exposures.Constant;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class ParamExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final ParamASTNode paramNode, final InputRecord inputRecord) {

		// Here we convert everything into internal price scaling as regardless of the parameter, it is treated as a price.
		// We convert from internal to external and then back to internal at the correct scale.
		// Note, we multiple directly rather than use Calculator in some cases to avoid the intermediate cast to int.

		if ("CV".equalsIgnoreCase(paramNode.getName())) {
			final long constant = OptimiserUnitConvertor.convertToInternalPrice(OptimiserUnitConvertor.convertToExternalConversionFactor(inputRecord.cargoCV()));
			return new Pair<>(constant, new Constant(constant, ""));
		} else if ("VOL_MMBTU".equalsIgnoreCase(paramNode.getName())) {
			double extVolumeMMBTU = OptimiserUnitConvertor.convertToExternalFloatVolume(inputRecord.volumeInMMBTU());
			final long constant = Math.round(extVolumeMMBTU * (double) Calculator.HighScaleFactor);
			return new Pair<>(constant, new Constant(constant, ""));
		} else if ("VOL_M3".equalsIgnoreCase(paramNode.getName())) {
			final long volumeM3 = Calculator.convertMMBTuToM3(inputRecord.volumeInMMBTU(), inputRecord.cargoCV());
			double extVolumeM3 = OptimiserUnitConvertor.convertToExternalFloatVolume(volumeM3);
			final long constant = Math.round(extVolumeM3 * (double) Calculator.HighScaleFactor);
			return new Pair<>(constant, new Constant(constant, ""));
		}
		final long constant = 0L;
		return new Pair<>(constant, new Constant(constant, ""));
	}
}
