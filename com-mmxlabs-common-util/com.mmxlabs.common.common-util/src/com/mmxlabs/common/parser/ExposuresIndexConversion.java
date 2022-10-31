/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.BreakEvenASTNode;
import com.mmxlabs.common.parser.astnodes.CommoditySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.ConstantASTNode;
import com.mmxlabs.common.parser.astnodes.Operator;
import com.mmxlabs.common.parser.astnodes.OperatorASTNode;
import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;

/**
 * Utility class holding methods used to convert a breakeven price from e.g.
 * $9.8 to 115%HH + 6.8
 * 
 * @author achurchill, refactor by FM
 *
 */
public class ExposuresIndexConversion {
	public enum Form {
		M_X_PLUS_C, M_X, X_PLUS_C
	}

	public enum BreakEvenType {
		COEFFICIENT, INTERCEPT
	}

	@Nullable
	public static Form getForm(final ASTNode parent) {
		if (!(parent instanceof final OperatorASTNode operator)) {
			return null;
		} else {
			final var type = operator.getOperator();
			if (type == Operator.PLUS) {
				final Pair<ASTNode, ASTNode> operatorChildren = getOperatorChildren(operator);
				if (operatorChildren != null) {
					final ASTNode constant = operatorChildren.getFirst();
					final ASTNode other = operatorChildren.getSecond();
					if (constant != null && (containsCommodity(other) || containsShift(other)) && (!containsConstant(other) && !containsBreakeven(other))) {
						return Form.X_PLUS_C;
					}
					if (other instanceof final OperatorASTNode otherOperator) {
						final Pair<ASTNode, ASTNode> otherOperatorChildren = getOperatorChildren(otherOperator);
						if (otherOperatorChildren != null) {
							final ASTNode nextConstant = operatorChildren.getFirst();
							final ASTNode nextOther = otherOperatorChildren.getSecond();
							if (!(nextOther instanceof OperatorASTNode) && nextConstant != null && nextOther != null && (containsCommodity(nextOther) || containsShift(nextOther))) {
								return Form.M_X_PLUS_C;
							}
						}
					}
				}
			} else if (type == Operator.TIMES || type == Operator.PERCENT) {
				final Pair<ASTNode, ASTNode> operatorChildren = getOperatorChildren(operator);
				if (operatorChildren != null) {
					final ASTNode constant = operatorChildren.getFirst();
					final ASTNode other = operatorChildren.getSecond();
					if (constant != null && (containsCommodity(other) || containsShift(other)) && (!containsConstant(other) && !containsBreakeven(other))) {
						return Form.M_X;
					}
				}
			}
		}
		return null;
	}

	@Nullable
	public static ASTNode rearrangeGraph(final double price, final ASTNode parent, Form form) {
		if (form == Form.X_PLUS_C) {
			// Convert to a different form
			processCommodityNode(parent);
			form = getForm(parent);
		}
		final ConstantASTNode breakEvenPriceNode = new ConstantASTNode(price);
		final BreakEvenType breakEvenType = getBreakEvenType(parent);
		if (form == Form.M_X && breakEvenType == BreakEvenType.COEFFICIENT) {
			if (parent instanceof final OperatorASTNode operatorNode) {
				final var operator = operatorNode.getOperator();
				if (operator == Operator.TIMES || operator == Operator.PERCENT) {
					final Pair<ASTNode, ASTNode> operatorChildren = getOperatorChildren(operatorNode);
					final ASTNode constant = operatorChildren.getFirst();
					replaceBreakEvenWithConstant(parent, constant);
					final OperatorASTNode divider = new OperatorASTNode(breakEvenPriceNode, parent, Operator.DIVIDE);
					return divider;
				}
			}
		}
		if (form == Form.M_X_PLUS_C) {

			if (breakEvenType == BreakEvenType.INTERCEPT) {
				if (parent instanceof final OperatorASTNode operatorNode) {
					final var operator = operatorNode.getOperator();
					if (operator == Operator.PLUS) {
						final Pair<ASTNode, ASTNode> operatorChildren = getOperatorChildren(operatorNode);
						final ASTNode indexOperator = operatorChildren.getSecond();
						if (indexOperator instanceof OperatorASTNode) {
							final ASTNode indexOperator_i = operatorChildren.getSecond();

							if (indexOperator_i != null && containsCommodity(indexOperator_i)) {
								final OperatorASTNode divider = new OperatorASTNode(breakEvenPriceNode, indexOperator_i, Operator.MINUS);
								return divider;
							}
						}
					}
				}
			}
			if (breakEvenType == BreakEvenType.COEFFICIENT) {
				if (parent instanceof final OperatorASTNode operatorNode) {
					final var operator = operatorNode.getOperator();
					if (operator == Operator.PLUS) {
						final Pair<ASTNode, ASTNode> operatorChildren = getOperatorChildren(operatorNode);
						final ASTNode constant = operatorChildren.getFirst();
						final ASTNode indexOperator = operatorChildren.getSecond();

						// price nodes
						final OperatorASTNode minus = new OperatorASTNode(breakEvenPriceNode, constant, Operator.MINUS);
						if (indexOperator instanceof final OperatorASTNode indexOperatorNode) {
							final Pair<ASTNode, ASTNode> indexOperatorChildren = getOperatorChildren(indexOperatorNode);
							final ASTNode indexOperator_c = indexOperatorChildren.getFirst();
							final ASTNode indexOperator_i = indexOperatorChildren.getSecond();

							if (indexOperator_i != null && containsCommodity(indexOperator_i)) {
								// create new operator node
								replaceBreakEvenWithConstant(indexOperator, indexOperator_c);
								final OperatorASTNode divider = new OperatorASTNode(minus, indexOperator, Operator.DIVIDE);
								return divider;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private static void processCommodityNode(final ASTNode node) {
		for (final ASTNode child : node.getChildren()) {
			if (child instanceof CommoditySeriesASTNode) {
				final ASTNode mult = new OperatorASTNode(new ConstantASTNode(1), child, Operator.TIMES);
				node.replace(child, mult);
			}
			processCommodityNode(child);
		}
	}

	private static void replaceBreakEvenWithConstant(final ASTNode indexOperator, final ASTNode indexOperator_c) {
		if (indexOperator_c instanceof BreakEvenASTNode) {
			indexOperator.replace(indexOperator_c, new ConstantASTNode(1.0));
		}
	}

	public static BreakEvenType getBreakEvenType(final ASTNode node) {
		final ASTNode findFirstParent = findFirstParent(node, BreakEvenASTNode.class);
		if (findFirstParent instanceof final OperatorASTNode operatorNode) {
			final var type = operatorNode.getOperator();
			if (type == Operator.PLUS) {
				return BreakEvenType.INTERCEPT;
			} else if (type == Operator.TIMES || type == Operator.PERCENT) {
				return BreakEvenType.COEFFICIENT;
			}
		}
		return null;
	}

	public static <T extends ASTNode> ASTNode findFirstParent(final ASTNode current, final Class<T> clazz) {
		for (@NonNull
		final ASTNode markedUpNode : current.getChildren()) {
			if (clazz.isAssignableFrom(markedUpNode.getClass())) {
				return current;
			} else {
				final ASTNode parent = findFirstParent(markedUpNode, clazz);
				if (parent != null) {
					return parent;
				}
			}
		}
		return null;
	}

	public static @Nullable Pair<ASTNode, ASTNode> getOperatorChildren(final OperatorASTNode node) {
		final List<ASTNode> children = Lists.newArrayList(node.getLHS(), node.getRHS());
		final Optional<ASTNode> optionalConstant = children.stream().filter(c -> (c instanceof ConstantASTNode || c instanceof BreakEvenASTNode)).findFirst();
		final ASTNode constant = optionalConstant.isPresent() ? optionalConstant.get() : null;
		final Optional<ASTNode> optionalOther = children.stream().filter(c -> c != constant).findFirst();
		final ASTNode other = optionalOther.isPresent() ? optionalOther.get() : null;
		return new Pair<>(constant, other);
	}

	public static boolean containsCommodity(final ASTNode node) {
		return containsNodeOfType(node, CommoditySeriesASTNode.class);
	}

	public static boolean containsShift(final ASTNode node) {
		return containsNodeOfType(node, ShiftFunctionASTNode.class);
	}

	public static boolean containsConstant(final ASTNode node) {
		return containsNodeOfType(node, ConstantASTNode.class);
	}

	public static boolean containsBreakeven(final ASTNode node) {
		return containsNodeOfType(node, BreakEvenASTNode.class);
	}

	private static <T extends ASTNode> boolean containsNodeOfType(final ASTNode node, final Class<T> clazz) {
		final LinkedList<T> nodes = new LinkedList<>();
		getNodesOfType(node, clazz, nodes);
		return !nodes.isEmpty();
	}

	public static <T extends ASTNode> void getNodesOfType(final ASTNode node, final Class<T> clazz, final Collection<T> nodes) {
		if (clazz.isAssignableFrom(node.getClass())) {
			nodes.add((T) node);
		}
		for (@NonNull
		final ASTNode markedUpNode : node.getChildren()) {
			getNodesOfType(markedUpNode, clazz, nodes);
		}
	}

	public static String getExpression(final ASTNode node) {
		return node.asString();
	}
}
