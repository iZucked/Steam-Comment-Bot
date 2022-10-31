/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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

import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.CommoditySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.ConstantASTNode;
import com.mmxlabs.common.parser.astnodes.ConversionASTNode;
import com.mmxlabs.common.parser.astnodes.CurrencySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.DatedAvgFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.FunctionASTNode;
import com.mmxlabs.common.parser.astnodes.FunctionType;
import com.mmxlabs.common.parser.astnodes.Operator;
import com.mmxlabs.common.parser.astnodes.OperatorASTNode;
import com.mmxlabs.common.parser.astnodes.SCurveFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.SplitMonthFunctionASTNode;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.parseutils.LookupData;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class IndexToDate {

	/**
	 * For unit test use
	 * 
	 */

	public static @Nullable IIndexToDateNode calculateIndicesToDate(final @NonNull String priceExpression, final LocalDate date, final @NonNull LookupData lookupData) {

		// Parse the expression
		final ASTNode markedUpNode = lookupData.expressionToNode.computeIfAbsent(priceExpression, expr -> {
			final SeriesParser commodityIndices = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
			return commodityIndices.parse(expr);
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

	private static @NonNull IIndexToDateNode getIndexToDateNode(final @NonNull ASTNode node, final LocalDate date, final LookupData lookupData) {
		if (node instanceof ShiftFunctionASTNode shiftNode) {
			return getIndexToDateNode(shiftNode.getToShift(), date.minusMonths(shiftNode.getShiftBy()), lookupData);
		} else if (node instanceof DatedAvgFunctionASTNode averageNode) {
			LocalDate startDate = date.minusMonths(averageNode.getMonths());
			if (averageNode.getReset() != 1) {
				startDate = startDate.minusMonths((date.getMonthValue() - 1) % averageNode.getReset());
			}
			startDate = startDate.minusMonths(averageNode.getLag());
			final IndexToDateRecords records = new IndexToDateRecords();
			for (int i = 0; i < averageNode.getMonths(); ++i) {
				final IIndexToDateNode p = getIndexToDateNode(averageNode.getSeries(), startDate.plusMonths(i), lookupData);
				final IndexToDateRecords result = (IndexToDateRecords) p;
				records.records.addAll(result.records);
			}

			return records;
		} else if (node instanceof SCurveFunctionASTNode scurveNode) {
			return getIndexToDateNode(scurveNode.getBase(), date, lookupData);
		} else if (node instanceof ConstantASTNode constantNode) {
			return new EmptyNode();
		} else if (node instanceof SplitMonthFunctionASTNode splitNode) {

			if (date.getDayOfMonth() < splitNode.getSplitPoint()) {
				final IIndexToDateNode pc0 = getIndexToDateNode(splitNode.getSeries1(), date, lookupData);
				return pc0;
			} else {
				final IIndexToDateNode pc1 = getIndexToDateNode(splitNode.getSeries2(), date, lookupData);
				return pc1;
			}
		}
		// Arithmetic operator token
		else if (node instanceof OperatorASTNode operatorNode) {

			final var operator = operatorNode.getOperator();
			final IIndexToDateNode c0 = getIndexToDateNode(operatorNode.getLHS(), date, lookupData);
			final IIndexToDateNode c1 = getIndexToDateNode(operatorNode.getRHS(), date, lookupData);
			if (operator == Operator.PLUS) {
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
			} else if (operator == Operator.MINUS) {
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
			} else if (operator == Operator.TIMES) {
				if (c0 instanceof EmptyNode && c1 instanceof EmptyNode) {
					return new EmptyNode();
				} else if (c0 instanceof IndexToDateRecords && c1 instanceof EmptyNode) {
					return c0;
				} else if (c0 instanceof EmptyNode && c1 instanceof IndexToDateRecords) {
					return c1;
				} else {
					return merge((IndexToDateRecords) c0, (IndexToDateRecords) c1, (c_0, c_1) -> new IndexDateRecord(c_0.index, c_0.date));
				}
			} else if (operator == Operator.DIVIDE) {
				if (c0 instanceof EmptyNode && c1 instanceof EmptyNode) {
					return new EmptyNode();
				} else if (c0 instanceof IndexToDateRecords && c1 instanceof EmptyNode) {
					return c0;
				} else if (c0 instanceof EmptyNode && c1 instanceof IndexToDateRecords) {
					return c1;
				} else {
					return merge((IndexToDateRecords) c0, (IndexToDateRecords) c1, (c_0, c_1) -> new IndexDateRecord(c_0.index, c_0.date));
				}
			} else if (operator == Operator.PERCENT) {

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
		} else if (node instanceof CommoditySeriesASTNode commodityNode) {
			return new IndexToDateRecords(new IndexDateRecord(lookupData.commodityMap.get(commodityNode.getName().toLowerCase()), date));
		} else if (node instanceof CurrencySeriesASTNode currencyNode) {
			return new IndexToDateRecords(new IndexDateRecord(lookupData.currencyMap.get(currencyNode.getName().toLowerCase()), date));
		} else if (node instanceof ConversionASTNode) {
			return new EmptyNode();
		} else if (node instanceof FunctionASTNode functionNode) {
//		} else if (node instanceof MaxFunctionNode) {
			if (functionNode.getFunctionType() == FunctionType.MAX) {
				IndexToDateRecords best = null;
				for (ASTNode child : functionNode.getArguments()) {
					final IIndexToDateNode pc = getIndexToDateNode(child, date, lookupData);
					if (pc instanceof EmptyNode) {
						continue;
					}
					if (pc instanceof IndexToDateRecords r) {
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
			} else if (functionNode.getFunctionType() == FunctionType.MIN) {
				IndexToDateRecords best = null;
				for (ASTNode child : functionNode.getArguments()) {
					final IIndexToDateNode pc = getIndexToDateNode(child, date, lookupData);
					if (pc instanceof EmptyNode) {
						continue;
					}
					if (pc instanceof IndexToDateRecords r) {
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
