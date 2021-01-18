/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.RawTreeParser;
import com.mmxlabs.common.parser.nodes.CommodityNode;
import com.mmxlabs.common.parser.nodes.ConstantNode;
import com.mmxlabs.common.parser.nodes.ConversionNode;
import com.mmxlabs.common.parser.nodes.CurrencyNode;
import com.mmxlabs.common.parser.nodes.DatedAverageNode;
import com.mmxlabs.common.parser.nodes.MarkedUpNode;
import com.mmxlabs.common.parser.nodes.MaxFunctionNode;
import com.mmxlabs.common.parser.nodes.MinFunctionNode;
import com.mmxlabs.common.parser.nodes.Node;
import com.mmxlabs.common.parser.nodes.OperatorNode;
import com.mmxlabs.common.parser.nodes.SCurveNode;
import com.mmxlabs.common.parser.nodes.ShiftNode;
import com.mmxlabs.common.parser.nodes.SplitNode;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.parseutils.LookupData;
import com.mmxlabs.models.lng.pricing.parseutils.Nodes;

public class IndexToDate {

	/**
	 * For unit test use
	 * 
	 */

	public static @Nullable IIndexToDateNode calculateIndicesToDate(final @NonNull String priceExpression, final LocalDate date, final @NonNull LookupData lookupData) {

		// Parse the expression
		final MarkedUpNode markedUpNode = lookupData.expressionToNode.computeIfAbsent(priceExpression, expr -> {
			final IExpression<Node> parse = new RawTreeParser().parse(expr);
			final Node p = parse.evaluate();
			final Node node = Nodes.expandNode(p, lookupData);
			return Nodes.markupNodes(node, lookupData);
		});

		return getIndexToDateNode(markedUpNode, date, lookupData);
	}

	static interface IIndexToDateNode {

	}

	static class EmptyNode implements IIndexToDateNode {

	}

	static class IndexToDateRecords implements IIndexToDateNode {

		List<IndexDateRecord> records = new LinkedList<>();

		public IndexToDateRecords(final IndexDateRecord record) {
			records.add(record);
		}

		public IndexToDateRecords() {
		}

	}

	static class IndexDateRecord {
		AbstractYearMonthCurve index;
		LocalDate date;

		IndexDateRecord(final AbstractYearMonthCurve index, final LocalDate date) {
			this.index = index;
			this.date = date;
		}

	}

	private static @NonNull IIndexToDateNode getIndexToDateNode(final @NonNull MarkedUpNode node, final LocalDate date, final LookupData lookupData) {
		if (node instanceof ShiftNode) {
			final ShiftNode shiftNode = (ShiftNode) node;
			return getIndexToDateNode(shiftNode.getChild(), date.minusMonths(shiftNode.getMonths()), lookupData);
		} else if (node instanceof DatedAverageNode) {
			final DatedAverageNode averageNode = (DatedAverageNode) node;

			LocalDate startDate = date.minusMonths(averageNode.getMonths());
			if (averageNode.getReset() != 1) {
				startDate = startDate.minusMonths((date.getMonthValue() - 1) % averageNode.getReset());
			}
			startDate = startDate.minusMonths(averageNode.getLag());
			final IndexToDateRecords records = new IndexToDateRecords();
			for (int i = 0; i < averageNode.getMonths(); ++i) {
				final IIndexToDateNode p = getIndexToDateNode(averageNode.getChild(), startDate.plusMonths(i), lookupData);
				final IndexToDateRecords result = (IndexToDateRecords) p;
				records.records.addAll(result.records);
			}

			return records;
		} else if (node instanceof SCurveNode) {
			final SCurveNode scurveNode = (SCurveNode) node;
			return getIndexToDateNode(scurveNode.getBase(), date, lookupData);
		} else if (node instanceof ConstantNode) {
			final ConstantNode constantNode = (ConstantNode) node;
			return new EmptyNode();
		} else if (node instanceof SplitNode) {
			final SplitNode splitNode = (SplitNode) node;
			if (node.getChildren().size() != 3) {
				throw new IllegalStateException();
			}

			final IIndexToDateNode pc0 = getIndexToDateNode(node.getChildren().get(0), date, lookupData);
			final IIndexToDateNode pc1 = getIndexToDateNode(node.getChildren().get(1), date, lookupData);

			if (date.getDayOfMonth() < splitNode.getSplitPointInDays()) {
				return pc0;
			} else {
				return pc1;
			}
		}
		// Arithmetic operator token
		else if (node instanceof OperatorNode) {
			final OperatorNode operatorNode = (OperatorNode) node;
			if (node.getChildren().size() != 2) {
				throw new IllegalStateException();
			}

			final String operator = operatorNode.getOperator();
			final IIndexToDateNode c0 = getIndexToDateNode(node.getChildren().get(0), date, lookupData);
			final IIndexToDateNode c1 = getIndexToDateNode(node.getChildren().get(1), date, lookupData);
			if (operator.equals("+")) {
				// addition: add coefficients of summands
				if (c0 instanceof EmptyNode && c1 instanceof EmptyNode) {
					return new EmptyNode();
				} else if (c0 instanceof IndexToDateRecords && c1 instanceof EmptyNode) {
					return c0;
				} else if (c0 instanceof EmptyNode && c1 instanceof IndexToDateRecords) {
					return c1;
				} else {
					return merge((IndexToDateRecords) c0, (IndexToDateRecords) c1, (c_0, c_1) -> new IndexDateRecord(c_0.index, c_0.date));
				}
			} else if (operator.equals("-")) {
				if (c0 instanceof EmptyNode && c1 instanceof EmptyNode) {
					return new EmptyNode();
				} else if (c0 instanceof IndexToDateRecords && c1 instanceof EmptyNode) {
					return c0;
				} else if (c0 instanceof EmptyNode && c1 instanceof IndexToDateRecords) {
					return c1;
				} else {
					final IndexToDateRecords newC1 = modify((IndexToDateRecords) c1, c -> new IndexDateRecord(c.index, c.date));
					return merge((IndexToDateRecords) c0, newC1, (c_0, c_1) -> new IndexDateRecord(c_0.index, c_0.date));
				}
			} else if (operator.equals("*")) {
				if (c0 instanceof EmptyNode && c1 instanceof EmptyNode) {
					return new EmptyNode();
				} else if (c0 instanceof IndexToDateRecords && c1 instanceof EmptyNode) {
					return c0;
				} else if (c0 instanceof EmptyNode && c1 instanceof IndexToDateRecords) {
					return c1;
				} else {
					return merge((IndexToDateRecords) c0, (IndexToDateRecords) c1, (c_0, c_1) -> new IndexDateRecord(c_0.index, c_0.date));
				}
			} else if (operator.equals("/")) {
				if (c0 instanceof EmptyNode && c1 instanceof EmptyNode) {
					return new EmptyNode();
				} else if (c0 instanceof IndexToDateRecords && c1 instanceof EmptyNode) {
					return c0;
				} else if (c0 instanceof EmptyNode && c1 instanceof IndexToDateRecords) {
					return c1;
				} else {
					return merge((IndexToDateRecords) c0, (IndexToDateRecords) c1, (c_0, c_1) -> new IndexDateRecord(c_0.index, c_0.date));
				}
			} else if (operator.equals("%")) {

				if (c0 instanceof EmptyNode && c1 instanceof EmptyNode) {
					return new EmptyNode();
				} else if (c0 instanceof IndexToDateRecords && c1 instanceof EmptyNode) {
					assert false;
					throw new UnsupportedOperationException();
				} else if (c0 instanceof EmptyNode && c1 instanceof IndexToDateRecords) {
					return c1;
				} else {
					throw new UnsupportedOperationException();
				}
			} else {
				throw new IllegalStateException("Invalid operator");
			}
		} else if (node instanceof CommodityNode) {
			final CommodityNode commodityNode = (CommodityNode) node;
			return new IndexToDateRecords(new IndexDateRecord(lookupData.commodityMap.get(commodityNode.getName().toLowerCase()), date));
		} else if (node instanceof CurrencyNode) {
			final CurrencyNode currencyNode = (CurrencyNode) node;
			return new IndexToDateRecords(new IndexDateRecord(lookupData.currencyMap.get(currencyNode.getName().toLowerCase()), date));
		} else if (node instanceof ConversionNode) {
			final ConversionNode conversionNode = (ConversionNode) node;
			return new EmptyNode();
		} else if (node instanceof MaxFunctionNode) {
			if (node.getChildren().isEmpty()) {
				throw new IllegalStateException();
			}

			IndexToDateRecords best = null;
			for (int i = 0; i < node.getChildren().size(); ++i) {
				final IIndexToDateNode pc = getIndexToDateNode(node.getChildren().get(i), date, lookupData);
				if (pc instanceof ConstantNode) {
					continue;
				}
				if (pc instanceof IndexToDateRecords) {
					final IndexToDateRecords r = (IndexToDateRecords) pc;
					if (best == null) {
						best = r;
					} else {
						best = merge(best, r, (c0, c1) -> new IndexDateRecord(c0.index, c0.date));
					}
				}
			}
			if (best == null) {
				return new EmptyNode();
			}
			return best;
		} else if (node instanceof MinFunctionNode) {
			if (node.getChildren().size() == 0) {
				throw new IllegalStateException();
			}
			IndexToDateRecords best = null;
			for (int i = 0; i < node.getChildren().size(); ++i) {
				final IIndexToDateNode pc = getIndexToDateNode(node.getChildren().get(i), date, lookupData);
				if (pc instanceof EmptyNode) {
					continue;
				}
				if (pc instanceof IndexToDateRecords) {
					final IndexToDateRecords r = (IndexToDateRecords) pc;
					if (best == null) {
						best = r;
					} else {
						best = merge(best, r, (c_0, c_1) -> new IndexDateRecord(c_0.index, c_0.date));
					}
				}
			}
			if (best == null) {
				return new EmptyNode();
			}
			return best;
		}

		throw new IllegalStateException("Unexpected node type");

	}

	private static IndexToDateRecords merge(final IndexToDateRecords c0, final IndexToDateRecords c1, final BiFunction<IndexDateRecord, IndexDateRecord, IndexDateRecord> mapper) {
		final IndexToDateRecords n = new IndexToDateRecords();
		final Iterator<IndexDateRecord> c0Itr = c0.records.iterator();
		LOOP_C0: while (c0Itr.hasNext()) {
			final IndexDateRecord c_c0 = c0Itr.next();
			final Iterator<IndexDateRecord> c1Itr = c1.records.iterator();
			while (c1Itr.hasNext()) {
				final IndexDateRecord c_c1 = c1Itr.next();
				if (c_c0.index == c_c1.index) {
					if (c_c0.date.equals(c_c1.date)) {
						n.records.add(mapper.apply(c_c0, c_c1));
						c1Itr.remove();
						c0Itr.remove();
						continue LOOP_C0;
					}
				}
			}
			n.records.add(c_c0);
			c0Itr.remove();
		}
		// Add remaining.
		n.records.addAll(c1.records);

		return n;
	}

	private static IndexToDateRecords modify(final IndexToDateRecords c0, final Function<IndexDateRecord, IndexDateRecord> mapper) {
		final IndexToDateRecords n = new IndexToDateRecords();
		for (final IndexDateRecord c : c0.records) {
			n.records.add(mapper.apply(c));
		}
		return n;
	}
}
