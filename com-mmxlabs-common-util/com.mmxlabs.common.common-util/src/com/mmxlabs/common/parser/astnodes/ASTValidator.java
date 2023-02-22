/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.common.Pair;

public final class ASTValidator {

	private static final int MONTH_LOWER_BOUND = 1;
	private static final int MONTH_UPPER_BOUND = 31;

	private ASTValidator() {

	}

	public static List<Pair<ASTNode, String>> validate(final ASTNode node) {
		if (node instanceof BreakEvenASTNode) {
			return Collections.emptyList();
		} else if (node instanceof ConstantASTNode) {
			return Collections.emptyList();
		} else if (node instanceof final DatedAvgFunctionASTNode datedAvgFunctionASTNode) {
			final List<Pair<ASTNode, String>> childValidationList = validate(datedAvgFunctionASTNode.getSeries());
			if (!(datedAvgFunctionASTNode.getSeries() instanceof NamedSeriesASTNode)) {
				final String error = String.format("The first argument of DATEDAVG with a window of %s months, with a delay of %s and a period of %s should be an index, not a price expression",
						datedAvgFunctionASTNode.getMonths(), datedAvgFunctionASTNode.getLag(), datedAvgFunctionASTNode.getReset());
				childValidationList.add(new Pair<>(node, error));
			}
			return childValidationList;
		} else if (node instanceof final FunctionASTNode functionASTNode) {
			final List<Pair<ASTNode, String>> childValidationLists = new LinkedList<>();
			functionASTNode.getArguments().forEach(arg -> childValidationLists.addAll(validate(arg)));
			return childValidationLists;
		} else if (node instanceof final MinusASTNode minusASTNode) {
			return validate(minusASTNode.getExpression());
		} else if (node instanceof final NamedSeriesASTNode) {
			// TODO check for circular dependencies and also they should be in the lookup
			// Data?
		} else if (node instanceof final OperatorASTNode operatorASTNode) {
			final List<Pair<ASTNode, String>> childListLHS = validate(operatorASTNode.getLHS());
			final List<Pair<ASTNode, String>> childListRHS = validate(operatorASTNode.getRHS());
			childListLHS.addAll(childListRHS);
			return childListLHS;
		} else if (node instanceof final SCurveFunctionASTNode sCurveFunctionASTNode) {
			final List<Pair<ASTNode, String>> baseList = validate(sCurveFunctionASTNode.getBase());
			final double firstThreshold = sCurveFunctionASTNode.getFirstThreshold();
			final double secondThreshold = sCurveFunctionASTNode.getSecondThreshold();
			if (firstThreshold >= secondThreshold) {
				baseList.add(new Pair<>(node, "The first threshold should be strictly lower than the second one"));
			}
			return baseList;
		} else if (node instanceof final Tier3FunctionASTNode sCurveFunctionASTNode) {
			final List<Pair<ASTNode, String>> baseList = validate(sCurveFunctionASTNode.getTarget());
			final double firstThreshold = sCurveFunctionASTNode.getLow().doubleValue();
			final double secondThreshold = sCurveFunctionASTNode.getMid().doubleValue();
			if (firstThreshold >= secondThreshold) {
				baseList.add(new Pair<>(node, "The first threshold should be strictly lower than the second one"));
			}
			return baseList;
		} else if (node instanceof final ShiftFunctionASTNode shiftFunctionASTNode) {
			return validate(shiftFunctionASTNode.getToShift());
		} else if (node instanceof final SplitMonthFunctionASTNode splitMonthFunctionASTNode) {
			final List<Pair<ASTNode, String>> series1List = validate(splitMonthFunctionASTNode.getSeries1());
			final List<Pair<ASTNode, String>> series2List = validate(splitMonthFunctionASTNode.getSeries2());
			series1List.addAll(series2List);
			final int splitPoint = splitMonthFunctionASTNode.getSplitPoint();
			if (splitPoint < MONTH_LOWER_BOUND || splitPoint > MONTH_UPPER_BOUND) {
				final String splitPointError = String.format("The day %s is not a valid day for splitting the month", splitPoint);
				series1List.add(new Pair<>(node, splitPointError));
			}
			return series1List;
		}

		final List<Pair<ASTNode, String>> listOfUnknownNode = new LinkedList<>();
		final String unknownNodeError = "Reached an unknown node in the price expression";
		listOfUnknownNode.add(new Pair<>(node, unknownNodeError));
		return listOfUnknownNode;

	}
}
