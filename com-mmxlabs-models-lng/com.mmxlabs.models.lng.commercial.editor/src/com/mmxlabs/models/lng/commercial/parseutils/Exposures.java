/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.rcp.common.ServiceHelper;

/**
 * Utility class to calculate schedule exposure to market indices. Provides static methods
 * 
 * @author Simon McGregor
 */
public class Exposures {

	public enum Mode {
		VALUE, VOLUME
	}

	public enum ValueMode {
		VOLUME_MMBTU, VOLUME_NATIVE, NATIVE_VALUE
	}

	public static @NonNull LookupData createLookupData(final PricingModel pricingModel) {
		final LookupData lookupData = new LookupData();
		lookupData.pricingModel = pricingModel;

		pricingModel.getCommodityIndices().forEach(idx -> lookupData.commodityMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCurrencyIndices().forEach(idx -> lookupData.currencyMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getConversionFactors().forEach(f -> lookupData.conversionMap.put(PriceIndexUtils.createConversionFactorName(f).toLowerCase(), f));

		return lookupData;

	}

	public static void calculateExposures(final LNGScenarioModel scenarioModel, final Schedule schedule, final EditingDomain domain) {

		if (schedule == null) {
			return;
		}
		final CompoundCommand cmd = new CompoundCommand("Calculate Exposures");

		@NonNull
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
		final LookupData lookupData = createLookupData(pricingModel);

		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				cmd.append(SetCommand.create(domain, slotAllocation, SchedulePackage.Literals.SLOT_ALLOCATION__EXPOSURES, SetCommand.UNSET_VALUE));

				final int volume = slotAllocation.getEnergyTransferred();
				final Slot slot = slotAllocation.getSlot();

				if (slot == null) {
					continue;
				}

				final YearMonth pricingDate = PricingMonthUtils.getPricingDate(slotAllocation);
				if (pricingDate == null) {
					continue;
				}

				final MarkedUpNode node = getExposureCoefficient(slot, slotAllocation, lookupData);
				if (node == null) {
					continue;
				}

				final Collection<ExposureDetail> exposureDetail = createExposureDetail(node, pricingDate, volume, slot instanceof LoadSlot, lookupData);
				if (exposureDetail != null && !exposureDetail.isEmpty()) {

					// Should be added to a command!
					cmd.append(AddCommand.create(domain, slotAllocation, SchedulePackage.Literals.SLOT_ALLOCATION__EXPOSURES, exposureDetail));
				}
			}
		}

		if (!scenarioModel.getScheduleModel().isDirty())

		{
			cmd.append(SetCommand.create(domain, scenarioModel.getScheduleModel(), SchedulePackage.Literals.SCHEDULE_MODEL__DIRTY, Boolean.FALSE));
		}
		domain.getCommandStack().execute(cmd);

	}

	/**
	 * For unit test use
	 * 
	 */

	public static @Nullable Collection<ExposureDetail> calculateExposure(final @NonNull String priceExpression, final YearMonth date, final double volumeInMMBTu, final boolean isPurchase,
			final @NonNull LookupData lookupData) {

		// Parse the expression
		final IExpression<Node> parse = new RawTreeParser().parse(priceExpression);
		final Node p = parse.evaluate();
		final Node node = expandNode(p, lookupData);
		final MarkedUpNode markedUpNode = markupNodes(node, lookupData);
		if (markedUpNode == null) {
			return null;
		}
		return createExposureDetail(markedUpNode, date, volumeInMMBTu, isPurchase, lookupData);
	}

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
	private static @NonNull Node expandNode(@NonNull final Node parentNode, final LookupData lookupData) {

		if (lookupData.expressionCache.containsKey(parentNode.token)) {
			return lookupData.expressionCache.get(parentNode.token);
		}

		if (parentNode.children.length == 0) {
			// Leaf node, this should be an index or a value
			if (lookupData.commodityMap.containsKey(parentNode.token.toLowerCase())) {
				final CommodityIndex idx = lookupData.commodityMap.get(parentNode.token.toLowerCase());

				// Matched derived index...
				if (idx.getData() instanceof DerivedIndex<?>) {
					final DerivedIndex<?> derivedIndex = (DerivedIndex<?>) idx.getData();
					// Parse the expression
					final IExpression<Node> parse = new RawTreeParser().parse(derivedIndex.getExpression());
					final Node p = parse.evaluate();
					// Expand the parsed tree again if needed,
					@Nullable
					final Node expandNode = expandNode(p, lookupData);
					// return the new sub-parse tree for the expression
					if (expandNode != null) {
						lookupData.expressionCache.put(derivedIndex.getExpression(), expandNode);
						return expandNode;
					}
					return p;
				} else {
					return parentNode;
				}
			}
			return parentNode;

			// return null;
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
		// return null;
	}

	/**
	 * Determines the amount of exposure to a particular index which is created by a specific contract.
	 * 
	 * @param contract
	 * @param index
	 * @return
	 */
	public static MarkedUpNode getExposureCoefficient(final @NonNull Slot slot, final @NonNull SlotAllocation slotAllocation, final @NonNull LookupData lookupData) {
		String priceExpression = null;
		if (slot instanceof SpotSlot) {
			final SpotSlot spotSlot = (SpotSlot) slot;
			final SpotMarket market = spotSlot.getMarket();
			final LNGPriceCalculatorParameters parameters = market.getPriceInfo();
			// do a case switch on price parameters
			if (parameters instanceof ExpressionPriceParameters) {
				final ExpressionPriceParameters pec = (ExpressionPriceParameters) parameters;
				priceExpression = pec.getPriceExpression();
			}
		} else if (slot.isSetPriceExpression()) {
			priceExpression = slot.getPriceExpression();
		} else {
			LNGPriceCalculatorParameters parameters = null;
			final Contract contract = slot.getContract();
			if (contract != null) {
				parameters = contract.getPriceInfo();
			}
			// do a case switch on price parameters
			if (parameters instanceof ExpressionPriceParameters) {
				final ExpressionPriceParameters pec = (ExpressionPriceParameters) parameters;
				priceExpression = pec.getPriceExpression();
			} else {
				// Delegate to services registry to see if we have a provider to help us.
				final String[] result = new String[1];
				ServiceHelper.withAllServices(IExposuredExpressionProvider.class, provider -> {
					final String exp = provider.provideExposedPriceExpression(slot, slotAllocation);
					if (exp != null) {
						result[0] = exp;
						return false;
					}
					return true;
				});
				priceExpression = result[0];
			}
		}

		if (priceExpression != null && !priceExpression.isEmpty()) {
			if (!priceExpression.equals("?")) {

				if (lookupData.expressionCache2.containsKey(priceExpression)) {
					return lookupData.expressionCache2.get(priceExpression);
				}
				// Parse the expression
				final IExpression<Node> parse = new RawTreeParser().parse(priceExpression);
				final Node p = parse.evaluate();
				final Node node = expandNode(p, lookupData);
				final MarkedUpNode markedUpNode = markupNodes(node, lookupData);
				lookupData.expressionCache2.put(priceExpression, markedUpNode);
				return markedUpNode;
				// return getExposureCoefficient(markedUpNode, index, date);
			}
		}

		return null;
	}

	private static MarkedUpNode markupNodes(@NonNull final Node parentNode, final LookupData lookupData) {
		MarkedUpNode n;

		if (parentNode.token.equalsIgnoreCase("SHIFT")) {
			final MarkedUpNode child = markupNodes(parentNode.children[0], lookupData);
			final MarkedUpNode shiftValue = markupNodes(parentNode.children[1], lookupData);
			final double shift;
			if (shiftValue instanceof ConstantNode) {
				ConstantNode constantNode = (ConstantNode) shiftValue;
				shift = constantNode.getConstant();
			} else if (shiftValue instanceof OperatorNode) {
				// FIXME: Only allow a specific operation here -- effectively the expression -x, generated as 0-x.
				OperatorNode operatorNode = (OperatorNode) shiftValue;
				if (operatorNode.getOperator().equals("-") && operatorNode.getChildren().size() == 2 && operatorNode.getChildren().get(0) instanceof ConstantNode
						&& operatorNode.getChildren().get(1) instanceof ConstantNode) {
					shift = ((ConstantNode) operatorNode.getChildren().get(0)).getConstant() - ((ConstantNode) operatorNode.getChildren().get(1)).getConstant();
				} else {
					throw new IllegalStateException();
				}
			} else {
				throw new IllegalStateException();
			}
			// = Integer.parseInt(parentNode.children[1].token);
			// for (final Node child : parentNode.children) {
			// }
			// n = new CommodityNode(lookupData.commodityMap.get(parentNode.token.toLowerCase()));
			n = new ShiftNode(child, (int) Math.round(shift));
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
			n = new CommodityNode(lookupData.commodityMap.get(parentNode.token.toLowerCase()));

		} else if (lookupData.currencyMap.containsKey(parentNode.token.toLowerCase())) {
			n = new CurrencyNode(lookupData.currencyMap.get(parentNode.token.toLowerCase()));

		} else if (lookupData.conversionMap.containsKey(parentNode.token.toLowerCase())) {
			n = new ConversionNode(parentNode.token, lookupData.conversionMap.get(parentNode.token.toLowerCase()));
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

	/**
	 * Calculates the exposure of a given schedule to a given index. Depends on the getExposureCoefficient method to correctly determine the exposure per cubic metre associated with a load or
	 * discharge slot.
	 * 
	 * @param schedule
	 * @param index
	 * @param filterOn
	 * @return
	 */
	public static Map<YearMonth, Double> getExposuresByMonth(final @NonNull Schedule schedule, final @NonNull CommodityIndex index, @NonNull final PricingModel pricingModel, final ValueMode mode,
			final Collection<Object> filterOn) {
		final Map<YearMonth, Double> result = new HashMap<>();

		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				if (!filterOn.isEmpty()) {
					final boolean include = filterOn.contains(slotAllocation) || filterOn.contains(slotAllocation.getSlot()) || filterOn.contains(cargoAllocation)
							|| filterOn.contains(cargoAllocation.getInputCargo());
					if (!include) {
						continue;
					}
				}

				for (final ExposureDetail detail : slotAllocation.getExposures()) {
					if (detail.getIndex() == index) {
						switch (mode) {
						case VOLUME_MMBTU:
							result.merge(detail.getDate(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							break;
						case VOLUME_NATIVE:
							result.merge(detail.getDate(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							break;
						case NATIVE_VALUE:
							result.merge(detail.getDate(), detail.getNativeValue(), (a, b) -> (a + b));
							break;
						default:
							throw new IllegalArgumentException();
						}
					}
				}
			}
		}

		return result;

	}

	private static Collection<ExposureDetail> createExposureDetail(final @NonNull MarkedUpNode node, final YearMonth pricingDate, final double volumeInMMBtu, final boolean isPurchase,
			final LookupData lookupData) {
		final List<ExposureDetail> m = new LinkedList<>();

		final InputRecord inputRecord = new InputRecord();
		inputRecord.volumeInMMBTU = volumeInMMBtu;
		final IExposureNode enode = getExposureNode(inputRecord, node, pricingDate, lookupData);
		if (enode instanceof ExposureRecords) {
			final ExposureRecords exposureRecords = (ExposureRecords) enode;
			for (final ExposureRecord record : exposureRecords.records) {
				final ExposureDetail exposureDetail = ScheduleFactory.eINSTANCE.createExposureDetail();

				exposureDetail.setIndex(record.index);
				exposureDetail.setCurrencyUnit(record.index.getCurrencyUnit());
				exposureDetail.setVolumeUnit(record.index.getVolumeUnit());

				exposureDetail.setDate(record.date);
				exposureDetail.setUnitPrice(record.unitPrice);

				exposureDetail.setVolumeInMMBTU(isPurchase ? record.mmbtuVolume : -record.mmbtuVolume);
				exposureDetail.setVolumeInNativeUnits(isPurchase ? record.nativeVolume : -record.nativeVolume);
				exposureDetail.setNativeValue(isPurchase ? record.nativeValue : -record.nativeValue);

				m.add(exposureDetail);
			}
		}
		return m;
	}

	static interface IExposureNode {

	}

	static class Constant implements IExposureNode {

		public Constant(final double constant) {
			this.constant = constant;
		}

		double constant;

		public double getConstant() {
			return constant;
		}
	}

	static class ExposureRecords implements IExposureNode {

		List<ExposureRecord> records = new LinkedList<>();

		public ExposureRecords(final ExposureRecord record) {
			records.add(record);
		}

		public ExposureRecords() {
		}

	}

	static class ExposureRecord {
		CommodityIndex index;
		double unitPrice;
		double nativeVolume;
		double nativeValue;
		double mmbtuVolume;
		YearMonth date;

		ExposureRecord(final CommodityIndex index, final double unitPrice, final double nativeVolume, final double nativeValue, final double mmbtuVolume, final YearMonth date) {
			this.index = index;
			this.unitPrice = unitPrice;
			this.nativeVolume = nativeVolume;
			this.nativeValue = nativeValue;
			this.mmbtuVolume = mmbtuVolume;
			this.date = date;

		}

	}

	static class InputRecord {
		double volumeInMMBTU;
	}

	// private void evaluate(MarkedUpNode node, String indexName, YearMonth date) {
	private static @NonNull IExposureNode getExposureNode(final InputRecord inputRecord, final @NonNull MarkedUpNode node, final YearMonth date, final LookupData lookupData) {
		if (node instanceof ShiftNode) {
			final ShiftNode shiftNode = (ShiftNode) node;
			return getExposureNode(inputRecord, shiftNode.getChild(), date.minusMonths(shiftNode.getMonths()), lookupData);
		} else if (node instanceof ConstantNode) {
			final ConstantNode constantNode = (ConstantNode) node;
			return new Constant(constantNode.getConstant());
		}

		// Arithmetic operator token
		else if (node instanceof OperatorNode) {
			final OperatorNode operatorNode = (OperatorNode) node;
			if (node.getChildren().size() != 2) {
				throw new IllegalStateException();
			}

			final String operator = operatorNode.getOperator();
			final IExposureNode c0 = getExposureNode(inputRecord, node.getChildren().get(0), date, lookupData);
			final IExposureNode c1 = getExposureNode(inputRecord, node.getChildren().get(1), date, lookupData);
			if (operator.equals("+")) {
				// addition: add coefficients of summands
				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Constant(((Constant) c0).getConstant() + ((Constant) c1).getConstant());
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					return c0;
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					return c1;
				} else {
					return merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new ExposureRecord(c_0.index, c_0.unitPrice, c_0.nativeVolume + c_1.nativeVolume,
							c_0.nativeValue + c_1.nativeValue, c_0.mmbtuVolume + c_1.mmbtuVolume, c_0.date));
				}
			} else if (operator.equals("-")) {
				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Constant(((Constant) c0).getConstant() - ((Constant) c1).getConstant());
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					return c0;// modify((ExposureRecords) c0, c -> new ExposureRecord(c.index, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date));
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					return modify((ExposureRecords) c1, c -> new ExposureRecord(c.index, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date));
				} else {
					final ExposureRecords newC1 = modify((ExposureRecords) c1, c -> new ExposureRecord(c.index, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date));
					return merge((ExposureRecords) c0, newC1, (c_0, c_1) -> new ExposureRecord(c_0.index, c_0.unitPrice, c_0.nativeVolume + c_1.nativeVolume, c_0.nativeValue + c_1.nativeValue,
							c_0.mmbtuVolume + c_1.mmbtuVolume, c_0.date));
				}
			} else if (operator.equals("*")) {
				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Constant(((Constant) c0).getConstant() * ((Constant) c1).getConstant());
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					final double constant = ((Constant) c1).getConstant();
					return modify((ExposureRecords) c0, c -> new ExposureRecord(c.index, c.unitPrice, c.nativeVolume * constant, c.nativeValue * constant, c.mmbtuVolume * constant, c.date));
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					final double constant = ((Constant) c0).getConstant();
					return modify((ExposureRecords) c1, c -> new ExposureRecord(c.index, c.unitPrice, constant * c.nativeVolume, constant * c.nativeValue, constant * c.mmbtuVolume, c.date));
				} else {
					return merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new ExposureRecord(c_0.index, c_0.unitPrice, c_0.nativeVolume * c_1.nativeVolume,
							c_0.nativeValue * c_1.nativeValue, c_0.mmbtuVolume * c_1.mmbtuVolume, c_0.date));
				}
			} else if (operator.equals("/")) {
				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Constant(((Constant) c0).getConstant() / ((Constant) c1).getConstant());
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					final double constant = ((Constant) c1).getConstant();
					return modify((ExposureRecords) c0, c -> new ExposureRecord(c.index, c.unitPrice, c.nativeVolume / constant, c.nativeValue / constant, c.mmbtuVolume / constant, c.date));
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					final double constant = ((Constant) c0).getConstant();
					return modify((ExposureRecords) c1, c -> new ExposureRecord(c.index, c.unitPrice, constant / c.nativeVolume, constant / c.nativeValue, constant / c.mmbtuVolume, c.date));
				} else {
					return merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new ExposureRecord(c_0.index, c_0.unitPrice, c_0.nativeVolume / c_1.nativeVolume,
							c_0.nativeValue / c_1.nativeValue, c_0.mmbtuVolume / c_1.mmbtuVolume, c_0.date));
				}
			} else if (operator.equals("%")) {

				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Constant(0.01 * ((Constant) c0).getConstant() * ((Constant) c1).getConstant());
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					assert false;
					throw new UnsupportedOperationException();
					// return modify((ExposureRecords) c0, c -> new ExposureRecord(c.index, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date));
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					final double constant = 0.01 * ((Constant) c0).getConstant();
					return modify((ExposureRecords) c1, c -> new ExposureRecord(c.index, c.unitPrice, constant * c.nativeVolume, constant * c.nativeValue, constant * c.mmbtuVolume, c.date));
				} else {
					// return merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new ExposureRecord(c_0.index, c_0.unitPrice, c_0.nativeVolume - c_1.nativeVolume,
					// c_0.nativeValue - c_1.nativeValue, c_0.mmbtuVolume - c_1.mmbtuVolume, c_0.date));
					throw new UnsupportedOperationException();
				}
			} else {
				throw new IllegalStateException("Invalid operator");
			}
		} else if (node instanceof CommodityNode) {
			final CommodityNode commodityNode = (CommodityNode) node;

			// Should really look up actual value from curve...
			final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
			final ISeries series = p.getSeries(commodityNode.getIndex().getName());
			final Number evaluate = series.evaluate(Hours.between(PriceIndexUtils.dateZero, date));

			final double unitPrice = evaluate.doubleValue();
			double nativeVolume = inputRecord.volumeInMMBTU;

			// Perform units conversion.
			final String u = commodityNode.getIndex().getVolumeUnit();
			for (final UnitConversion factor : lookupData.pricingModel.getConversionFactors()) {
				if (factor.getTo().equalsIgnoreCase("mmbtu")) {
					if (factor.getFrom().equalsIgnoreCase(u)) {
						nativeVolume /= factor.getFactor();
						break;
					}
				}
			}

			final ExposureRecord record = new ExposureRecord(commodityNode.getIndex(), unitPrice, nativeVolume, nativeVolume * unitPrice, inputRecord.volumeInMMBTU, date);
			return new ExposureRecords(record);
		} else if (node instanceof CurrencyNode) {
			return new Constant(1.0);
		} else if (node instanceof ConversionNode) {
			return new Constant(1.0);
		}

		throw new IllegalStateException("Unexpected node type");

	}

	private static ExposureRecords merge(final ExposureRecords c0, final ExposureRecords c1, final BiFunction<ExposureRecord, ExposureRecord, ExposureRecord> mapper) {
		final ExposureRecords n = new ExposureRecords();
		final Iterator<ExposureRecord> c0Itr = c0.records.iterator();
		LOOP_C0: while (c0Itr.hasNext()) {
			final ExposureRecord c_c0 = c0Itr.next();
			final Iterator<ExposureRecord> c1Itr = c1.records.iterator();
			while (c1Itr.hasNext()) {
				final ExposureRecord c_c1 = c1Itr.next();
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

	private static ExposureRecords modify(final ExposureRecords c0, final Function<ExposureRecord, ExposureRecord> mapper) {
		final ExposureRecords n = new ExposureRecords();
		for (final ExposureRecord c : c0.records) {
			n.records.add(mapper.apply(c));
		}
		return n;
	}
}
