/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.BasicCommodityCurveData;
import com.mmxlabs.common.curves.BasicUnitConversionData;
import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.RawTreeParser;

public class Nodes {
	/**
	 * Expands the node tree. Returns a new node if the parentNode has change and needs to be replaced in the upper chain. Returns null if the node does not need replacing (note: the children may
	 * still have changed).
	 * 
	 * @param exposedIndexToken
	 * @param parentNode
	 * @param pricingModel
	 * @param date
	 * @return
	 */
	public static @NonNull Node expandNode(@NonNull final Node parentNode, final ExposuresLookupData lookupData) {

		if (lookupData.expressionCache.containsKey(parentNode.token)) {
			return lookupData.expressionCache.get(parentNode.token);
		}

		if (parentNode.children.length == 0) {
			// Leaf node, this should be an index or a value
			if (lookupData.commodityMap.containsKey(parentNode.token.toLowerCase())) {
				final BasicCommodityCurveData curve = lookupData.commodityMap.get(parentNode.token.toLowerCase());

				// Matched derived index...
				if (curve.isSetExpression()) {
					// Parse the expression
					final IExpression<Node> parse = new RawTreeParser().parse(curve.getExpression());
					final Node p = parse.evaluate();
					// Expand the parsed tree again if needed,
					@Nullable
					final Node expandNode = expandNode(p, lookupData);
					// return the new sub-parse tree for the expression
					if (expandNode != null) {
						lookupData.expressionCache.put(curve.getExpression(), expandNode);
						return expandNode;
					}
					return p;
				} else {
					return parentNode;
				}
			}
			return parentNode;
		} else {
			// We have children, token *should* be an operator, expand out the child nodes
			for (int i = 0; i < parentNode.children.length; ++i) {
				final Node replacement = expandNode(parentNode.children[i], lookupData);
				if (replacement != null) {
					parentNode.children[i] = replacement;
				}
			}
			return parentNode;
		}
	}

	public static @NonNull MarkedUpNode markupNodes(@NonNull final Node parentNode, final ExposuresLookupData lookupData) {
		final @NonNull MarkedUpNode n;

		if (parentNode.token.equalsIgnoreCase("CAP")) {
			n = new MinFunctionNode();
		} else if (parentNode.token.equalsIgnoreCase("FLOOR")) {
			n = new MaxFunctionNode();
		} else if (parentNode.token.equalsIgnoreCase("MAX")) {
			n = new MaxFunctionNode();
		} else if (parentNode.token.equalsIgnoreCase("MIN")) {
			n = new MinFunctionNode();
		} else if (parentNode.token.equalsIgnoreCase("SPLITMONTH")) {
			final MarkedUpNode splitPoint = markupNodes(parentNode.children[2], lookupData);
			double splitPointValue = -1;
			splitPointValue = extractDoubleNode(splitPoint);
			n = new SplitNode((int) splitPointValue);
		} else if (parentNode.token.equalsIgnoreCase("SHIFT")) {
			final MarkedUpNode child = markupNodes(parentNode.children[0], lookupData);
			final MarkedUpNode shiftValue = markupNodes(parentNode.children[1], lookupData);
			final double shift;
			if (shiftValue instanceof ConstantNode) {
				final ConstantNode constantNode = (ConstantNode) shiftValue;
				shift = constantNode.getConstant();
			} else if (shiftValue instanceof OperatorNode) {
				// FIXME: Only allow a specific operation here -- effectively the expression -x,
				// generated as 0-x.
				final OperatorNode operatorNode = (OperatorNode) shiftValue;
				if (operatorNode.getOperator().equals("-") && operatorNode.getChildren().size() == 2 && operatorNode.getChildren().get(0) instanceof ConstantNode
						&& operatorNode.getChildren().get(1) instanceof ConstantNode) {
					shift = ((ConstantNode) operatorNode.getChildren().get(0)).getConstant() - ((ConstantNode) operatorNode.getChildren().get(1)).getConstant();
				} else {
					throw new IllegalStateException();
				}
			} else {
				throw new IllegalStateException();
			}
			n = new ShiftNode(child, (int) Math.round(shift));
			return n;
		} else if (parentNode.token.equalsIgnoreCase("DATEDAVG")) {
			final MarkedUpNode child = markupNodes(parentNode.children[0], lookupData);
			final MarkedUpNode monthsValue = markupNodes(parentNode.children[1], lookupData);
			final MarkedUpNode lagValue = markupNodes(parentNode.children[2], lookupData);
			final MarkedUpNode resetValue = markupNodes(parentNode.children[3], lookupData);
			final double months;
			final double lag;
			final double reset;
			months = extractDoubleNode(monthsValue);
			lag = extractDoubleNode(lagValue);
			reset = extractDoubleNode(resetValue);
			n = new DatedAverageNode(child, (int) Math.round(months), (int) Math.round(lag), (int) Math.round(reset));
			return n;
		} else if (parentNode.token.equalsIgnoreCase("S")) {
			final MarkedUpNode base = markupNodes(parentNode.children[0], lookupData);

			final double lowerThan = extractDoubleNode(markupNodes(parentNode.children[1], lookupData));
			final double higherThan = extractDoubleNode(markupNodes(parentNode.children[2], lookupData));
			final double a1 = extractDoubleNode(markupNodes(parentNode.children[3], lookupData));
			final double b1 = extractDoubleNode(markupNodes(parentNode.children[4], lookupData));
			final double a2 = extractDoubleNode(markupNodes(parentNode.children[5], lookupData));
			final double b2 = extractDoubleNode(markupNodes(parentNode.children[6], lookupData));
			final double a3 = extractDoubleNode(markupNodes(parentNode.children[7], lookupData));
			final double b3 = extractDoubleNode(markupNodes(parentNode.children[8], lookupData));

			n = new SCurveNode(base, lowerThan, higherThan, a1, b1, a2, b2, a3, b3);
			return n;
		} else if (parentNode.token.equals("-") && parentNode.children.length == 1) {
			// Prefix operator! - Convert to 0-expr
			n = new OperatorNode(parentNode.token);
			n.addChildNode(new ConstantNode(0.0));
			n.addChildNode(markupNodes(parentNode.children[0], lookupData));
			return n;
		} else if (parentNode.token.equals("*") || parentNode.token.equals("/") || parentNode.token.equals("+") || parentNode.token.equals("-") || parentNode.token.equals("%")) {
			n = new OperatorNode(parentNode.token);
		} else if (lookupData.commodityMap.containsKey(parentNode.token.toLowerCase())) {
			final BasicCommodityCurveData curve = lookupData.commodityMap.get(parentNode.token.toLowerCase());
			n = new CommodityNode(curve);
		} else if (lookupData.currencyList.contains(parentNode.token.toLowerCase())) {
			
			n = new CurrencyNode(parentNode.token.toLowerCase());

		} else if (lookupData.conversionMap.containsKey(parentNode.token.toLowerCase())) {
			final String from = lookupData.conversionMap.get(parentNode.token.toLowerCase()).getFrom();
			final String to = lookupData.conversionMap.get(parentNode.token.toLowerCase()).getTo();
			final double factor = lookupData.conversionMap.get(parentNode.token.toLowerCase()).getFactor();
			final BasicUnitConversionData uce = new BasicUnitConversionData(from, to, factor);
			
			n = new ConversionNode(parentNode.token, uce, false);
		} else if (lookupData.reverseConversionMap.containsKey(parentNode.token.toLowerCase())) {
			final String from = lookupData.conversionMap.get(parentNode.token.toLowerCase()).getFrom();
			final String to = lookupData.conversionMap.get(parentNode.token.toLowerCase()).getTo();
			final double factor = lookupData.conversionMap.get(parentNode.token.toLowerCase()).getFactor();
			final BasicUnitConversionData uce = new BasicUnitConversionData(from, to, factor);
			
			n = new ConversionNode(parentNode.token, uce, true);
		} else if (parentNode.token.equals("?")) {
			n = new BreakevenNode();
		} else {
			// This should be a constant
			try {
				n = new ConstantNode(Double.parseDouble(parentNode.token));
			} catch (final Exception e) {
				throw new RuntimeException("Unexpected token: " + parentNode.token);
			}
		}

		for (final Node child : parentNode.children) {
			n.addChildNode(markupNodes(child, lookupData));
		}
		return n;
	}

	private static double extractDoubleNode(final MarkedUpNode lowerThanValue) {
		if (lowerThanValue instanceof ConstantNode) {
			final ConstantNode constantNode = (ConstantNode) lowerThanValue;
			return constantNode.getConstant();
		}
		throw new IllegalStateException();
	}

}
