/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.ConstantASTNode;
import com.mmxlabs.common.parser.astnodes.DatedAvgFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.FunctionASTNode;
import com.mmxlabs.common.parser.astnodes.MonthFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.NamedSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.OperatorASTNode;
import com.mmxlabs.common.parser.astnodes.ParamASTNode;
import com.mmxlabs.common.parser.astnodes.SCurveFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.SplitMonthFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.Tier2FunctionASTNode;
import com.mmxlabs.common.parser.astnodes.Tier3FunctionASTNode;
import com.mmxlabs.common.parser.astnodes.VolumeTierASTNode;
import com.mmxlabs.scheduler.optimiser.exposures.Constant;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class ExposuresASTToCalculator {

	public static @NonNull Pair<Long, IExposureNode> getExposureNode(final @NonNull ASTNode node, final InputRecord inputRecord) {
		if (node instanceof final ShiftFunctionASTNode shiftNode) {
			return ShiftFunctionExposuresCalculator.getExposureNode(shiftNode, inputRecord);
		} else if (node instanceof final MonthFunctionASTNode monthNode) {
			return MonthFunctionExposuresCalculator.getExposureNode(monthNode, inputRecord);
		} else if (node instanceof final DatedAvgFunctionASTNode averageNode) {
			return DatedAvgFunctionExposuresCalculator.getExposureNode(averageNode, inputRecord);
		} else if (node instanceof final VolumeTierASTNode volumeTierNode) {
			return VolumeTierFunctionExposuresCalculator.getExposureNode(volumeTierNode, inputRecord);
		} else if (node instanceof final SCurveFunctionASTNode scurveNode) {
			return SCurveFunctionExposuresCalculator.getExposureNode(scurveNode, inputRecord);
		} else if (node instanceof final Tier2FunctionASTNode tierNode) {
			return Tier2FunctionExposuresCalculator.getExposureNode(tierNode, inputRecord);
		} else if (node instanceof final Tier3FunctionASTNode tierNode) {
			return Tier3FunctionExposuresCalculator.getExposureNode(tierNode, inputRecord);
		} else if (node instanceof final ConstantASTNode constantNode) {
			return ConstantExposuresCalculator.getExposureNode(constantNode, inputRecord);
		} else if (node instanceof final SplitMonthFunctionASTNode splitNode) {
			return SplitMonthFunctionExposuresCalculator.getExposureNode(splitNode, inputRecord);
		} else if (node instanceof final OperatorASTNode operatorNode) {
			return OperatorExposuresCalculator.getExposureNode(operatorNode, inputRecord);
		} else if (node instanceof final NamedSeriesASTNode namedSeriesNode) {
			return NamedSeriedExposuresCalculator.getExposureNode(namedSeriesNode, inputRecord);
		} else if (node instanceof final FunctionASTNode functionNode) {
			return FunctionExposuresCalculator.getExposureNode(functionNode, inputRecord);
		} else if (node instanceof final ParamASTNode paramNode) {
			return ParamExposuresCalculator.getExposureNode(paramNode, inputRecord);
		}
		throw new IllegalStateException("Unexpected node type");
	}

}
