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

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.nodes.AbstractMarkedUpNode;
import com.mmxlabs.common.parser.nodes.BreakevenNode;
import com.mmxlabs.common.parser.nodes.CommodityNode;
import com.mmxlabs.common.parser.nodes.ConstantNode;
import com.mmxlabs.common.parser.nodes.ConversionNode;
import com.mmxlabs.common.parser.nodes.CurrencyNode;
import com.mmxlabs.common.parser.nodes.MarkedUpNode;
import com.mmxlabs.common.parser.nodes.OperatorNode;
import com.mmxlabs.common.parser.nodes.ShiftNode;

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
	public static Form getForm(final MarkedUpNode parent) {
		if (!(parent instanceof final OperatorNode operator)) {
			return null;
		} else {
			@NonNull
			final String type = operator.getOperator();
			if (type.equals("+")) {
				final Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren(operator);
				if (operatorChildren != null) {
					final MarkedUpNode constant = operatorChildren.getFirst();
					final MarkedUpNode other = operatorChildren.getSecond();
					if (constant != null && (containsCommodity(other) || containsShift(other)) && (!containsConstant(other) && !containsBreakeven(other))) {
						return Form.X_PLUS_C;
					}
					if (other instanceof final OperatorNode otherOperator) {
						final Pair<MarkedUpNode, MarkedUpNode> otherOperatorChildren = getOperatorChildren(otherOperator);
						if (otherOperatorChildren != null) {
							final MarkedUpNode nextConstant = operatorChildren.getFirst();
							final MarkedUpNode nextOther = otherOperatorChildren.getSecond();
							if (nextConstant != null && nextOther != null && (containsCommodity(nextOther) || containsShift(nextOther))) {
								return Form.M_X_PLUS_C;
							}
						}
					}
				}
			} else if (type.equals("*") || type.equals("%")) {
				final Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren(operator);
				if (operatorChildren != null) {
					final MarkedUpNode constant = operatorChildren.getFirst();
					final MarkedUpNode other = operatorChildren.getSecond();
					if (constant != null && (containsCommodity(other) || containsShift(other)) && (!containsConstant(other) && !containsBreakeven(other))) {
						return Form.M_X;
					}
				}
			}
		}
		return null;
	}

	@Nullable
	public static MarkedUpNode rearrangeGraph(final double price, final MarkedUpNode parent, Form form) {
		if (form == Form.X_PLUS_C) {
			// Convert to a different form
			processCommodityNode(parent);
			form = getForm(parent);
		}
		final ConstantNode breakEvenPriceNode = new ConstantNode(price);
		final BreakEvenType breakEvenType = getBreakEvenType(parent);
		if (form == Form.M_X && breakEvenType == BreakEvenType.COEFFICIENT) {
			if (parent instanceof final OperatorNode operatorNode) {
				final String operator = operatorNode.getOperator();
				if (operator.equals("*") || operator.equals("%")) {
					final Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren(operatorNode);
					final MarkedUpNode constant = operatorChildren.getFirst();
					replaceBreakEvenWithConstant(parent, constant);
					final OperatorNode divider = new OperatorNode("/");
					divider.addChildNode(breakEvenPriceNode);
					divider.addChildNode(parent);
					return divider;
				}
			}
		}
		if (form == Form.M_X_PLUS_C) {

			if (breakEvenType == BreakEvenType.INTERCEPT) {
				if (parent instanceof final OperatorNode operatorNode) {
					final String operator = operatorNode.getOperator();
					if (operator.equals("+")) {
						final Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren(operatorNode);
						final MarkedUpNode indexOperator = operatorChildren.getSecond();
						if (indexOperator instanceof OperatorNode) {
							final MarkedUpNode indexOperator_i = operatorChildren.getSecond();

							if (indexOperator_i != null && containsCommodity(indexOperator_i)) {
								final OperatorNode divider = new OperatorNode("-");
								divider.addChildNode(breakEvenPriceNode);
								divider.addChildNode(indexOperator_i);
								return divider;
							}
						}
					}
				}
			}
			if (breakEvenType == BreakEvenType.COEFFICIENT) {
				if (parent instanceof final OperatorNode operatorNode) {
					final String operator = operatorNode.getOperator();
					if (operator.equals("+")) {
						final Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren(operatorNode);
						final MarkedUpNode constant = operatorChildren.getFirst();
						final MarkedUpNode indexOperator = operatorChildren.getSecond();

						// price nodes
						final MarkedUpNode minus = new OperatorNode("-");
						minus.addChildNode(breakEvenPriceNode);
						minus.addChildNode(constant);
						if (indexOperator instanceof final OperatorNode indexOperatorNode) {
							final Pair<MarkedUpNode, MarkedUpNode> indexOperatorChildren = getOperatorChildren(indexOperatorNode);
							final MarkedUpNode indexOperator_c = indexOperatorChildren.getFirst();
							final MarkedUpNode indexOperator_i = indexOperatorChildren.getSecond();

							if (indexOperator_i != null && containsCommodity(indexOperator_i)) {
								// create new operator node
								replaceBreakEvenWithConstant(indexOperator, indexOperator_c);
								final OperatorNode divider = new OperatorNode("/");
								divider.addChildNode(minus);
								divider.addChildNode(indexOperator);
								return divider;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private static void processCommodityNode(final MarkedUpNode node) {
		for (int i = 0; i < node.getChildren().size(); i++) {
			final MarkedUpNode child = node.getChildren().get(i);
			if (child instanceof CommodityNode) {
				final MarkedUpNode mult = new OperatorNode("*");
				mult.addChildNode(new ConstantNode(1));
				mult.addChildNode(child);
				node.getChildren().set(i, mult);
			}
			processCommodityNode(child);
		}
	}

	private static void replaceBreakEvenWithConstant(final MarkedUpNode indexOperator, final MarkedUpNode indexOperator_c) {
		if (indexOperator_c instanceof BreakevenNode) {
			final int indexOf = indexOperator.getChildren().indexOf(indexOperator_c);
			indexOperator.getChildren().remove(indexOf);
			indexOperator.getChildren().add(indexOf, new ConstantNode(1.0));
		}
	}

	public static BreakEvenType getBreakEvenType(final MarkedUpNode node) {
		final MarkedUpNode findFirstParent = findFirstParent(node, BreakevenNode.class);
		if (findFirstParent instanceof final OperatorNode operatorNode) {
			@NonNull
			final String type = operatorNode.getOperator();
			if (type.equals("+")) {
				return BreakEvenType.INTERCEPT;
			} else if (type.equals("*") || type.equals("%")) {
				return BreakEvenType.COEFFICIENT;
			}
		}
		return null;
	}

	public static <T extends AbstractMarkedUpNode> MarkedUpNode findFirstParent(final MarkedUpNode current, final Class<T> clazz) {
		for (@NonNull
		final MarkedUpNode markedUpNode : current.getChildren()) {
			if (clazz.isAssignableFrom(markedUpNode.getClass())) {
				return current;
			} else {
				final MarkedUpNode parent = findFirstParent(markedUpNode, clazz);
				if (parent != null) {
					return parent;
				}
			}
		}
		return null;
	}

	public static @Nullable Pair<MarkedUpNode, MarkedUpNode> getOperatorChildren(final OperatorNode node) {
		final List<MarkedUpNode> children = node.getChildren();
		if (children.size() == 2) {
			final Optional<MarkedUpNode> optionalConstant = children.stream().filter(c -> (c instanceof ConstantNode || c instanceof BreakevenNode)).findFirst();
			final MarkedUpNode constant = optionalConstant.isPresent() ? optionalConstant.get() : null;
			final Optional<MarkedUpNode> optionalOther = children.stream().filter(c -> c != constant).findFirst();
			final MarkedUpNode other = optionalOther.isPresent() ? optionalOther.get() : null;
			return new Pair<>(constant, other);
		}
		return null;
	}

	public static boolean containsCommodity(final MarkedUpNode node) {
		return containsNodeOfType(node, CommodityNode.class);
	}

	public static boolean containsShift(final MarkedUpNode node) {
		return containsNodeOfType(node, ShiftNode.class);
	}

	public static boolean containsConstant(final MarkedUpNode node) {
		return containsNodeOfType(node, ConstantNode.class);
	}

	public static boolean containsBreakeven(final MarkedUpNode node) {
		return containsNodeOfType(node, BreakevenNode.class);
	}

	private static <T extends AbstractMarkedUpNode> boolean containsNodeOfType(final MarkedUpNode node, final Class<T> clazz) {
		final LinkedList<T> nodes = new LinkedList<>();
		getNodesOfType(node, clazz, nodes);
		return !nodes.isEmpty();
	}

	public static <T extends AbstractMarkedUpNode> void getNodesOfType(final MarkedUpNode node, final Class<T> clazz, final Collection<T> nodes) {
		if (clazz.isAssignableFrom(node.getClass())) {
			nodes.add((T) node);
		}
		for (@NonNull
		final MarkedUpNode markedUpNode : node.getChildren()) {
			getNodesOfType(markedUpNode, clazz, nodes);
		}
	}

	public static String getExpression(final MarkedUpNode node) {
		String s = "";
		if (node instanceof final ConstantNode constantNode) {
			s = "" + constantNode.getConstant();
		} else if (node instanceof final OperatorNode operatorNode) {
			final StringBuilder builder = new StringBuilder();

			final @NonNull String operator = operatorNode.getOperator();
			for (int i = 0; i < node.getChildren().size(); i++) {
				final @NonNull MarkedUpNode child = node.getChildren().get(i);
				String extra = "";
				if (i < node.getChildren().size() - 1) {
					extra = "" + operator;
				} else {
					extra = "";
				}
				if ("%".equals(operator) && i == 0) {
					String expression = getExpression(child);
					if (expression.startsWith("(") && expression.endsWith(")")) {
						expression = expression.substring(1, expression.length() - 1);
					}
					builder.append(expression + extra);
				} else {
					builder.append(getExpression(child) + extra);
				}
			}
			s = builder.toString();
		} else if (node instanceof final ShiftNode shiftNode) {
			s = String.format("shift(%s, %s)", getExpression(shiftNode.getChild()), shiftNode.getMonths());
		} else if (node instanceof final CommodityNode commodityNode) {
			s = commodityNode.getName();
		} else if (node instanceof final CurrencyNode currencyNode) {
			s = currencyNode.getName();
		} else if (node instanceof final ConversionNode conversionNode) {
			s = conversionNode.getName();
		} else if (node instanceof BreakevenNode) {
			s = "?";
		}
		return String.format("(%s)", s);
	}
}
