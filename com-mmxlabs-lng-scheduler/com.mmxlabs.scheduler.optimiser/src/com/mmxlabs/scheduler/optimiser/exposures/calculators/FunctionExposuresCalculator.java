/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.FunctionASTNode;
import com.mmxlabs.common.parser.astnodes.FunctionType;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecords;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class FunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final FunctionASTNode functionNode, final InputRecord inputRecord) {

		if (functionNode.getChildren().isEmpty()) {
			return new Pair<>(0L, new ExposureRecords());
		}

		if (functionNode.getFunctionType() == FunctionType.MAX || functionNode.getFunctionType() == FunctionType.CAP) {
			Pair<Long, IExposureNode> best = ExposuresASTToCalculator.getExposureNode(functionNode.getArguments().get(0), inputRecord);
			for (int i = 1; i < functionNode.getArguments().size(); ++i) {
				final Pair<Long, IExposureNode> pc = ExposuresASTToCalculator.getExposureNode(functionNode.getArguments().get(i), inputRecord);
				if (pc.getFirst() > best.getFirst()) {
					best = pc;
				}
			}
			return best;
		} else if (functionNode.getFunctionType() == FunctionType.MIN || functionNode.getFunctionType() == FunctionType.FLOOR) {
			Pair<Long, IExposureNode> best = ExposuresASTToCalculator.getExposureNode(functionNode.getArguments().get(0), inputRecord);
			for (int i = 1; i < functionNode.getArguments().size(); ++i) {
				final Pair<Long, IExposureNode> pc = ExposuresASTToCalculator.getExposureNode(functionNode.getArguments().get(i), inputRecord);
				if (pc.getFirst() < best.getFirst()) {
					best = pc;
				}
			}
			return best;
		} else {
			throw new IllegalStateException();
		}
	}

}
