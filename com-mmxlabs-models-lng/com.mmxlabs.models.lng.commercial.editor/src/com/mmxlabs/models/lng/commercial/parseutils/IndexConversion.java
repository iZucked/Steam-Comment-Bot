package com.mmxlabs.models.lng.commercial.parseutils;

import java.time.YearMonth;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesOperatorExpression;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

/**
 * Utility class holding methods used to convert a breakeven price from e.g. $9.8 to 115%HH + 6.8 
 * @author achurchill
 *
 */
public class IndexConversion {
	public enum Form {
		M_X_PLUS_C, M_X, X_PLUS_C
	}

	public enum BreakEvenType {
		COEFFICIENT, INTERCEPT
	}

	@Nullable
	public static Form getForm(MarkedUpNode parent) {
		if (!(parent instanceof OperatorNode)) {
			return null;
		} else {
			OperatorNode operator = (OperatorNode) parent;
			@NonNull
			String type = operator.getOperator();
			if (type.equals("+")) {
				Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren(operator);
				if (operatorChildren != null) {
					MarkedUpNode constant = operatorChildren.getFirst();
					MarkedUpNode other = operatorChildren.getSecond();
					if (constant != null && (containsCommodity(other) || containsShift(other)) && (!containsConstant(other) && !containsBreakeven(other))) {
						return Form.X_PLUS_C;
					}
					if (other != null && other instanceof OperatorNode) {
						Pair<MarkedUpNode, MarkedUpNode> otherOperatorChildren = getOperatorChildren((OperatorNode) other);
						if (otherOperatorChildren != null) {
							MarkedUpNode nextConstant = operatorChildren.getFirst();
							MarkedUpNode nextOther = otherOperatorChildren.getSecond();
							if (nextConstant != null && nextOther != null && (containsCommodity(nextOther) || containsShift(nextOther))) {
								return Form.M_X_PLUS_C;
							}
						}
					}
				}
			} else if (type.equals("*") || type.equals("%")) {
				Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren(operator);
				if (operatorChildren != null) {
					MarkedUpNode constant = operatorChildren.getFirst();
					MarkedUpNode other = operatorChildren.getSecond();
					if (constant != null && (containsCommodity(other) || containsShift(other)) && (!containsConstant(other) && !containsBreakeven(other))) {
						return Form.M_X;
					}
				}
			}
		}
		return null;
	}

	@Nullable
	public static MarkedUpNode rearrangeGraph(double price, MarkedUpNode parent, Form form) {
		ConstantNode breakEvenPriceNode = new ConstantNode(price);
		BreakEvenType breakEvenType = getBreakEvenType(parent);
		if (form == Form.M_X && breakEvenType == BreakEvenType.COEFFICIENT) {
			if (parent instanceof OperatorNode) {
				String operator = ((OperatorNode) parent).getOperator();
				if (operator.equals("*") || operator.equals("%")) {
					Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren((OperatorNode) parent);
					MarkedUpNode constant = operatorChildren.getFirst();
					replaceBreakEvenWithConstant(parent, constant);
					OperatorNode divider = new OperatorNode("/");
					divider.addChildNode(breakEvenPriceNode);
					divider.addChildNode(parent);
					return divider;
				}
			}
		}
		if (form == Form.M_X_PLUS_C) {

			if (breakEvenType == BreakEvenType.INTERCEPT) {
				if (parent instanceof OperatorNode) {
					String operator = ((OperatorNode) parent).getOperator();
					if (operator.equals("+")) {
						Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren((OperatorNode) parent);
						MarkedUpNode indexOperator = operatorChildren.getSecond();
						if (indexOperator instanceof OperatorNode) {
							MarkedUpNode indexOperator_i = operatorChildren.getSecond();

							if (indexOperator_i != null && containsCommodity(indexOperator_i)) {
								OperatorNode divider = new OperatorNode("-");
								divider.addChildNode(breakEvenPriceNode);
								divider.addChildNode(indexOperator_i);
								return divider;
							}
						}
					}
				}
			}
			if (breakEvenType == BreakEvenType.COEFFICIENT) {
				if (parent instanceof OperatorNode) {
					String operator = ((OperatorNode) parent).getOperator();
					if (operator.equals("+")) {
						Pair<MarkedUpNode, MarkedUpNode> operatorChildren = getOperatorChildren((OperatorNode) parent);
						MarkedUpNode constant = operatorChildren.getFirst();
						MarkedUpNode indexOperator = operatorChildren.getSecond();

						// price nodes
						MarkedUpNode minus = new OperatorNode("-");
						minus.addChildNode(breakEvenPriceNode);
						minus.addChildNode(constant);
						if (indexOperator instanceof OperatorNode) {
							Pair<MarkedUpNode, MarkedUpNode> indexOperatorChildren = getOperatorChildren((OperatorNode) indexOperator);
							MarkedUpNode indexOperator_c = indexOperatorChildren.getFirst();
							MarkedUpNode indexOperator_i = indexOperatorChildren.getSecond();

							if (indexOperator_i != null && containsCommodity(indexOperator_i)) {
								// create new operator node
								replaceBreakEvenWithConstant(indexOperator, indexOperator_c);
								OperatorNode divider = new OperatorNode("/");
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

	private static void replaceBreakEvenWithConstant(MarkedUpNode indexOperator, MarkedUpNode indexOperator_c) {
		if (indexOperator_c instanceof BreakevenNode) {
			int indexOf = indexOperator.getChildren().indexOf(indexOperator_c);
			indexOperator.getChildren().remove(indexOf);
			indexOperator.getChildren().add(indexOf, new ConstantNode(1.0));
		}
	}

	public static BreakEvenType getBreakEvenType(MarkedUpNode node) {
		MarkedUpNode findFirstParent = findFirstParent(node, BreakevenNode.class);
		if (findFirstParent instanceof OperatorNode) {
			@NonNull
			String type = ((OperatorNode) findFirstParent).getOperator();
			if (type.equals("+")) {
				return BreakEvenType.INTERCEPT;
			} else if (type.equals("*") || type.equals("%")) {
				return BreakEvenType.COEFFICIENT;
			}
		}
		return null;
	}

	public static <T extends AbstractMarkedUpNode> MarkedUpNode findFirstParent(MarkedUpNode current, Class<T> clazz) {
		for (@NonNull
		MarkedUpNode markedUpNode : current.getChildren()) {
			if (clazz.isAssignableFrom(markedUpNode.getClass())) {
				return current;
			} else {
				MarkedUpNode parent = findFirstParent(markedUpNode, clazz);
				if (parent != null) {
					return parent;
				}
			}
		}
		return null;
	}

	public static Pair<MarkedUpNode, MarkedUpNode> getOperatorChildren(OperatorNode node) {
		List<@NonNull MarkedUpNode> children = node.getChildren();
		if (children.size() == 2) {
			Optional<MarkedUpNode> optionalConstant = children.stream().filter(c -> (c instanceof ConstantNode || c instanceof BreakevenNode)).findFirst();
			MarkedUpNode constant = optionalConstant.isPresent() ? optionalConstant.get() : null;
			Optional<MarkedUpNode> optionalOther = children.stream().filter(c -> c != constant).findFirst();
			MarkedUpNode other = optionalOther.isPresent() ? optionalOther.get() : null;
			return new Pair<>(constant, other);
		}
		return null;
	}

	public static boolean containsCommodity(MarkedUpNode node) {
		return containsNodeOfType(node, CommodityNode.class);
	}

	public static boolean containsShift(MarkedUpNode node) {
		return containsNodeOfType(node, ShiftNode.class);
	}

	public static boolean containsConstant(MarkedUpNode node) {
		return containsNodeOfType(node, ConstantNode.class);
	}

	public static boolean containsBreakeven(MarkedUpNode node) {
		return containsNodeOfType(node, BreakevenNode.class);
	}

	private static <T extends AbstractMarkedUpNode> boolean containsNodeOfType(MarkedUpNode node, Class<T> clazz) {
		LinkedList<T> nodes = new LinkedList<T>();
		getNodesOfType(node, clazz, nodes);
		return nodes.size() > 0;
	}

	public static <T extends AbstractMarkedUpNode> void getNodesOfType(MarkedUpNode node, Class<T> clazz, Collection<T> nodes) {
		if (clazz.isAssignableFrom(node.getClass())) {
			nodes.add((T) node);
		}
		for (@NonNull
		MarkedUpNode markedUpNode : node.getChildren()) {
			getNodesOfType(markedUpNode, clazz, nodes);
		}
	}
	
	public static String getExpression(MarkedUpNode node) {
		String s = "";
		if (node instanceof ConstantNode) {
			s = ""+((ConstantNode) node).getConstant();
		} else if (node instanceof OperatorNode) {
			for (int i = 0; i < node.getChildren().size(); i++) {
				@NonNull
				MarkedUpNode child = node.getChildren().get(i);
				String extra = "";
				if (i < node.getChildren().size() - 1) {
					extra = "" + ((OperatorNode) node).getOperator();
				} else {
					extra = "";
				}
				s += (getExpression(child) + extra);
			}
		} else if (node instanceof ShiftNode) {
			s = String.format("shift(%s, %s)", getExpression(((ShiftNode) node).getChild()), ((ShiftNode) node).getMonths());
		} else if (node instanceof CommodityNode) {
			s = ((CommodityNode) node).getIndex().getName();
		} else if (node instanceof CurrencyNode) {
			s = ((CurrencyNode) node).getIndex().getName();
		} else if (node instanceof ConversionNode) {
			s = ((ConversionNode) node).getName();
		}
		return String.format("(%s)", s);
	}
	
	public static double getRearrangedPrice(@NonNull PricingModel pricingModel, @NonNull String expression, double breakevenPrice, YearMonth date) {
		double price = 0.0;
		MarkedUpNode markedUpNode = getMarkedUpNode(pricingModel, expression);
		Form form = getForm(markedUpNode);
		if (form == null) {
			return price;
		}
		@Nullable
		MarkedUpNode rearrangeGraph = rearrangeGraph(breakevenPrice, markedUpNode, form);
		if (rearrangeGraph == null) {
			return price;
		}
		String rearrangedExpression = getExpression(rearrangeGraph);
		if (rearrangedExpression == null) {
			return price;
		}
		price = parseExpression(pricingModel, rearrangedExpression, date);
		return price;
	}
	
	private static double parseExpression(@NonNull PricingModel pricingModel, @NonNull String expression, YearMonth date) {
		@NonNull
		final LookupData lookupData = Exposures.createLookupData(pricingModel);
		
		final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
		final IExpression<ISeries> series = p.parse(expression);
		double unitPrice = 0.0;
		if (series instanceof SeriesOperatorExpression) {
			@NonNull
			ISeries opSeries = ((SeriesOperatorExpression) series).evaluate();
			final Number evaluate = opSeries.evaluate(Hours.between(PriceIndexUtils.dateZero, date));
			unitPrice = evaluate.doubleValue();
		}
		return unitPrice;
	}
	
	public static MarkedUpNode getMarkedUpNode(@NonNull PricingModel pricingModel, @NonNull String expression) {
		@NonNull
		final LookupData lookupData = Exposures.createLookupData(pricingModel);
		// Parse the expression
		final IExpression<Node> parse = new RawTreeParser().parse(expression);
		final Node p = parse.evaluate();
		final Node node = Exposures.expandNode(p, lookupData);
		final MarkedUpNode markedUpNode = Exposures.markupNodes(node, lookupData);
		return markedUpNode;
	}
	
	public static boolean isExpressionValidForIndexConversion(@NonNull PricingModel pricingModel, @NonNull String expression) {
		try {
			MarkedUpNode markedUpNode = getMarkedUpNode(pricingModel, expression);
			return getForm(markedUpNode) != null;
		} catch (Exception e) {
			return false;
		}
	}
}
