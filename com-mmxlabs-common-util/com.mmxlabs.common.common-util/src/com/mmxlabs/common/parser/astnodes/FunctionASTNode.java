/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.FunctionConstructor;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.functions.Max;
import com.mmxlabs.common.parser.series.functions.Min;

public final class FunctionASTNode implements ASTNode {

	private final List<ASTNode> arguments;
	private final FunctionType functionType;

	public FunctionType getFunctionType() {
		return functionType;
	}

	public FunctionASTNode(final List<ASTNode> arguments, final FunctionType functionType) {
		this.arguments = arguments;
		this.functionType = functionType;
	}

	public FunctionASTNode(final ASTNode e1, final ASTNode e2, final FunctionType functionType) {
		this(CollectionsUtil.makeArrayList(e1, e2), functionType);
	}

	public List<ASTNode> getArguments() {
		return arguments;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.unmodifiableList(getArguments());
	}

	@Override
	public void replace(final ASTNode original, final ASTNode replacement) {

		if (arguments.contains(original)) {
			final int i = arguments.indexOf(original);
			arguments.set(i, replacement);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public @NonNull String asString() {
		return String.format("%s(%s)", functionType.asString(), arguments.stream().map(ASTNode::asString).collect(Collectors.joining(",")));
	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {
		Class<? extends ISeries> clazz = switch (functionType) {
		case CAP, MAX -> Max.class;
		case FLOOR, MIN -> Min.class;
		default -> throw new IllegalStateException("Unknown function type found in the AST");
		};
		return new FunctionConstructor(clazz, arguments.stream().map(n -> n.asExpression(seriesParser)).toList());
	}
}
