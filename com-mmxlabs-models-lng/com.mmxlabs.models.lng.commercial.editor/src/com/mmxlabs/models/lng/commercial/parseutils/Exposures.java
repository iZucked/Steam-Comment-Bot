/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.pricing.parseutils.CommodityNode;
import com.mmxlabs.models.lng.pricing.parseutils.ConstantNode;
import com.mmxlabs.models.lng.pricing.parseutils.ConversionNode;
import com.mmxlabs.models.lng.pricing.parseutils.CurrencyNode;
import com.mmxlabs.models.lng.pricing.parseutils.DatedAverageNode;
import com.mmxlabs.models.lng.pricing.parseutils.LookupData;
import com.mmxlabs.models.lng.pricing.parseutils.MarkedUpNode;
import com.mmxlabs.models.lng.pricing.parseutils.MaxFunctionNode;
import com.mmxlabs.models.lng.pricing.parseutils.MinFunctionNode;
import com.mmxlabs.models.lng.pricing.parseutils.Nodes;
import com.mmxlabs.models.lng.pricing.parseutils.OperatorNode;
import com.mmxlabs.models.lng.pricing.parseutils.SCurveNode;
import com.mmxlabs.models.lng.pricing.parseutils.ShiftNode;
import com.mmxlabs.models.lng.pricing.parseutils.SplitNode;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

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
	
	public enum AggregationMode {
		BY_MONTH_NO_TOTAL, BY_MONTH, BY_DEALSET
	}
	
	public static void calculateExposures(final @NonNull IScenarioDataProvider scenarioDataProvider, final @Nullable Schedule schedule) {
		ServiceHelper.withOptionalServiceConsumer(IExposuresCustomiser.class, p -> {
			if (p == null) {
				calculateExposures(scenarioDataProvider, schedule, new DefaultExposuresCustomiser());
			} else {
				calculateExposures(scenarioDataProvider, schedule, p);
			}
		});
	}

	public static void calculateExposures(final @NonNull IScenarioDataProvider scenarioDataProvider, final @Nullable Schedule schedule,//
			final IExposuresCustomiser exposuresCustomiser) {

		if (schedule == null) {
			return;
		}
		
		final ModelMarketCurveProvider mmCurveProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
		// TODO : call of getLinkedCurves

		@NonNull
		final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		
		@NonNull
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
		final LookupData lookupData = LookupData.createLookupData(pricingModel);

		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				slotAllocation.getExposures().clear();

				final int volume = slotAllocation.getEnergyTransferred();
				final Slot slot = slotAllocation.getSlot();

				if (slot == null) {
					continue;
				}
				
				String priceExpression = "";
				if (slot.eIsSet(CargoPackage.Literals.SLOT__PRICE_EXPRESSION)) {
					priceExpression = slot.getPriceExpression();
				} else if (slot.eIsSet(CargoPackage.Literals.SPOT_SLOT__MARKET)){
					SpotSlot ss = (SpotSlot) slot;
					ss.getMarket();
				}else {
					priceExpression = exposuresCustomiser.provideExposedPriceExpression(slot, slotAllocation);
				}
				Collection<AbstractYearMonthCurve> curves = mmCurveProvider.getLinkedCurves(priceExpression);

				final boolean isPurchase = slot instanceof LoadSlot;
				{
					final ExposureDetail physical = ScheduleFactory.eINSTANCE.createExposureDetail();

					physical.setDealType(DealType.PHYSICAL);

					SlotAllocation sa = exposuresCustomiser.getExposed(slotAllocation);
					
					physical.setVolumeInMMBTU((isPurchase ? 1.0 : -1.0) * sa.getEnergyTransferred());
					physical.setVolumeInNativeUnits((isPurchase ? 1.0 : -1.0) * sa.getEnergyTransferred());
					physical.setNativeValue((isPurchase ? -1.0 : 1.0) * sa.getVolumeValue());
					physical.setVolumeUnit("mmBtu");
					physical.setIndexName("Physical");
					physical.setDate(YearMonth.from(sa.getSlotVisit().getStart().toLocalDate()));

					slotAllocation.getExposures().add(physical);
				}

				final LocalDate pricingFullDate = PricingMonthUtils.getFullPricingDate(slotAllocation);
				if (pricingFullDate != null) {
					final YearMonth pricingDate = YearMonth.of(pricingFullDate.getYear(), pricingFullDate.getMonth());
					final MarkedUpNode node = getExposureCoefficient(slot, slotAllocation, lookupData);
					if (node != null) {
						final Collection<ExposureDetail> exposureDetails = createExposureDetail(node, pricingDate, volume, isPurchase, lookupData, pricingFullDate.getDayOfMonth());
						if (exposureDetails != null && !exposureDetails.isEmpty()) {
							slotAllocation.getExposures().addAll(exposureDetails);
						}
					}
				}

				for (final ExposureDetail d : slotAllocation.getExposures()) {
					// This is only for unit test reload validation as -0.0 != 0.0 and we cannot
					// persist/reload -0.0
					if (d.getNativeValue() == -0.0) {
						d.setNativeValue(0.0);
					}
					if (d.getUnitPrice() == -0.0) {
						d.setUnitPrice(0.0);
					}
					if (d.getVolumeInMMBTU() == -0.0) {
						d.setVolumeInMMBTU(0.0);
					}
					if (d.getVolumeInNativeUnits() == -0.0) {
						d.setVolumeInNativeUnits(0.0);
					}
				}
			}
		}
	}

	/**
	 * For unit test use
	 * 
	 */

	public static @Nullable Collection<ExposureDetail> calculateExposure(final @NonNull String priceExpression, final YearMonth date, final double volumeInMMBTu, final boolean isPurchase,
			final @NonNull LookupData lookupData, final int dayOfMonth) {

		// Parse the expression
		final IExpression<Node> parse = new RawTreeParser().parse(priceExpression);
		final Node p = parse.evaluate();
		final Node node = Nodes.expandNode(p, lookupData);
		final MarkedUpNode markedUpNode = Nodes.markupNodes(node, lookupData);
		return createExposureDetail(markedUpNode, date, volumeInMMBTu, isPurchase, lookupData, dayOfMonth);
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
			} else if (parameters instanceof DateShiftExpressionPriceParameters) {
				final DateShiftExpressionPriceParameters pec = (DateShiftExpressionPriceParameters) parameters;
				// Note: date shift should be taken care of from PricingMonthUtils call
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
				ServiceHelper.withAllServices(IExposuresCustomiser.class, null, provider -> {
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

				if (lookupData.expressionToNode.containsKey(priceExpression)) {
					return lookupData.expressionToNode.get(priceExpression);
				}
				// Parse the expression
				final IExpression<Node> parse = new RawTreeParser().parse(priceExpression);
				final Node p = parse.evaluate();
				final Node node = Nodes.expandNode(p, lookupData);
				final MarkedUpNode markedUpNode = Nodes.markupNodes(node, lookupData);
				lookupData.expressionToNode.put(priceExpression, markedUpNode);
				return markedUpNode;
			}
		}

		return null;
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
	public static Map<YearMonth, Double> getExposuresByMonth(final @NonNull Schedule schedule, final @NonNull CommodityCurve index, @NonNull final PricingModel pricingModel, final ValueMode mode,
			final Collection<Object> filterOn) {
		final Map<YearMonth, Double> result = new HashMap<>();

		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				if (!filterOn.isEmpty()) {
					final boolean include = filterOn.contains(slotAllocation) || filterOn.contains(slotAllocation.getSlot()) || filterOn.contains(cargoAllocation)
					// || filterOn.contains(cargoAllocation.getInputCargo())
					;
					if (!include) {
						continue;
					}
				}

				for (final ExposureDetail detail : slotAllocation.getExposures()) {
					if (Objects.equals(detail.getIndexName(), index.getName())) {
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
		for (final PaperDealAllocation paperDealAllocation : schedule.getPaperDealAllocations()) {
			for (final PaperDealAllocationEntry paperDealAllocationEntry : paperDealAllocation.getEntries()) {
				if (!filterOn.isEmpty()) {
					final boolean include = false;
					if (!include) {
						continue;
					}
				}

				for (final ExposureDetail detail : paperDealAllocationEntry.getExposures()) {
					if (Objects.equals(detail.getIndexName(), index.getName())) {
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
			final LookupData lookupData, final int dayOfMonth) {
		final List<ExposureDetail> m = new LinkedList<>();

		final InputRecord inputRecord = new InputRecord();
		inputRecord.volumeInMMBTU = volumeInMMBtu;
		final IExposureNode enode = getExposureNode(inputRecord, node, pricingDate, lookupData, dayOfMonth).getSecond();
		if (enode instanceof ExposureRecords) {
			final ExposureRecords exposureRecords = (ExposureRecords) enode;
			for (final ExposureRecord record : exposureRecords.records) {
				final ExposureDetail exposureDetail = ScheduleFactory.eINSTANCE.createExposureDetail();
				exposureDetail.setDealType(DealType.FINANCIAL);

				exposureDetail.setIndexName(record.index.getName());
				exposureDetail.setCurrencyUnit(record.index.getCurrencyUnit());
				exposureDetail.setVolumeUnit(record.index.getVolumeUnit());

				exposureDetail.setDate(record.date);
				exposureDetail.setUnitPrice(record.unitPrice);

				exposureDetail.setVolumeInMMBTU(isPurchase ? -record.mmbtuVolume : record.mmbtuVolume);

				// Is the record unit in mmBtu? Then either it always was mmBtu OR we have
				// converted the native units to mmBtu
				if (record.volumeUnit == null || record.volumeUnit.isEmpty() || "mmbtu".equalsIgnoreCase(record.volumeUnit)) {
					exposureDetail.setVolumeInNativeUnits(isPurchase ? -record.nativeVolume : record.nativeVolume);
					exposureDetail.setNativeValue(isPurchase ? -record.nativeValue : record.nativeValue);
				} else {
					// Not mmBtu? then the mmBtu field is still really native units
					exposureDetail.setVolumeInNativeUnits(isPurchase ? -record.mmbtuVolume : record.mmbtuVolume);
					// Perform units conversion - compute mmBtu equivalent of exposed native volume
					double mmbtuVolume = record.mmbtuVolume;
					for (final UnitConversion factor : lookupData.pricingModel.getConversionFactors()) {
						if (factor.getTo().equalsIgnoreCase("mmbtu")) {
							if (factor.getFrom().equalsIgnoreCase(record.volumeUnit)) {
								mmbtuVolume /= factor.getFactor();
								break;
							}
						}
					}
					exposureDetail.setVolumeInMMBTU(isPurchase ? -mmbtuVolume : mmbtuVolume);

					final double nativeValue = exposureDetail.getVolumeInNativeUnits() * exposureDetail.getUnitPrice();
					exposureDetail.setNativeValue(nativeValue);

				}

				m.add(exposureDetail);
			}
		}
		return m;
	}

	static interface IExposureNode {

	}

	static class Constant implements IExposureNode {

		private final String newVolumeUnit;

		public Constant(final double constant, final String newVolumeUnit) {
			this.constant = constant;
			this.newVolumeUnit = newVolumeUnit;
		}

		public String getNewVolumeUnit() {
			return newVolumeUnit;
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
		CommodityCurve index;
		double unitPrice;
		double nativeVolume;
		double nativeValue;
		double mmbtuVolume;
		YearMonth date;
		String volumeUnit;

		ExposureRecord(final CommodityCurve index, final double unitPrice, final double nativeVolume, final double nativeValue, final double mmbtuVolume, final YearMonth date,
				final String volumeUnit) {
			this.index = index;
			this.unitPrice = unitPrice;
			this.nativeVolume = nativeVolume;
			this.nativeValue = nativeValue;
			this.mmbtuVolume = mmbtuVolume;
			this.date = date;
			this.volumeUnit = volumeUnit;

		}

	}

	static class InputRecord {
		double volumeInMMBTU;
	}

	private static @NonNull Pair<Double, IExposureNode> getExposureNode(final InputRecord inputRecord, final @NonNull MarkedUpNode node, final YearMonth date, final LookupData lookupData,
			final int dayOfMonth) {
		if (node instanceof ShiftNode) {
			final ShiftNode shiftNode = (ShiftNode) node;
			return getExposureNode(inputRecord, shiftNode.getChild(), date.minusMonths(shiftNode.getMonths()), lookupData, dayOfMonth);
		} else if (node instanceof DatedAverageNode) {
			final DatedAverageNode averageNode = (DatedAverageNode) node;

			YearMonth startDate = date.minusMonths(averageNode.getMonths());
			if (averageNode.getReset() != 1) {
				startDate = startDate.minusMonths((date.getMonthValue() - 1) % averageNode.getReset());
			}
			startDate = startDate.minusMonths(averageNode.getLag());
			final double months = averageNode.getMonths();
			final ExposureRecords records = new ExposureRecords();
			double price = 0.0;
			for (int i = 0; i < averageNode.getMonths(); ++i) {
				final Pair<Double, IExposureNode> p = getExposureNode(inputRecord, averageNode.getChild(), startDate.plusMonths(i), lookupData, dayOfMonth);
				ExposureRecords result = (ExposureRecords) p.getSecond();
				price += p.getFirst();
				result = modify(result, c -> new ExposureRecord(c.index, c.unitPrice, c.nativeVolume / months, c.nativeValue / months, c.mmbtuVolume / months, c.date, c.volumeUnit));
				records.records.addAll(result.records);
			}

			return new Pair<>(price / months, records);
		} else if (node instanceof SCurveNode) {
			final SCurveNode scurveNode = (SCurveNode) node;

			final Pair<Double, IExposureNode> baseNodeData = getExposureNode(inputRecord, scurveNode.getBase(), date, lookupData, dayOfMonth);

			double timesValue;
			double plusValue;
			if (baseNodeData.getFirst() < scurveNode.getLowerThan()) {
				timesValue = scurveNode.getA1();
				plusValue = scurveNode.getB1();
			} else if (baseNodeData.getFirst() > scurveNode.getHigherThan()) {
				timesValue = scurveNode.getA3();
				plusValue = scurveNode.getB3();
			} else {
				timesValue = scurveNode.getA2();
				plusValue = scurveNode.getB2();
			}

			OperatorNode times = new OperatorNode("*");
			times.addChildNode(new ConstantNode(timesValue));
			times.addChildNode(scurveNode.getBase());

			OperatorNode plus = new OperatorNode("+");
			plus.addChildNode(times);
			plus.addChildNode(new ConstantNode(scurveNode.getB1()));

			return getExposureNode(inputRecord, plus, date, lookupData, dayOfMonth);
		} else if (node instanceof ConstantNode) {
			final ConstantNode constantNode = (ConstantNode) node;
			return new Pair<>(constantNode.getConstant(), new Constant(constantNode.getConstant(), ""));
		} else if (node instanceof SplitNode) {
			final SplitNode splitNode = (SplitNode) node;
			if (node.getChildren().size() != 3) {
				throw new IllegalStateException();
			}

			final Pair<Double, IExposureNode> pc0 = getExposureNode(inputRecord, node.getChildren().get(0), date, lookupData, dayOfMonth);
			final Pair<Double, IExposureNode> pc1 = getExposureNode(inputRecord, node.getChildren().get(1), date, lookupData, dayOfMonth);

			if (dayOfMonth < splitNode.getSplitPointInDays()) {
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
			final Pair<Double, IExposureNode> pc0 = getExposureNode(inputRecord, node.getChildren().get(0), date, lookupData, dayOfMonth);
			final Pair<Double, IExposureNode> pc1 = getExposureNode(inputRecord, node.getChildren().get(1), date, lookupData, dayOfMonth);
			final IExposureNode c0 = pc0.getSecond();
			final IExposureNode c1 = pc1.getSecond();
			if (operator.equals("+")) {
				// addition: add coefficients of summands
				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Pair<>(pc0.getFirst() + pc1.getFirst(), new Constant(((Constant) c0).getConstant() + ((Constant) c1).getConstant(), ""));
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					return new Pair<>(pc0.getFirst() + pc1.getFirst(), c0);
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					return new Pair<>(pc0.getFirst() + pc1.getFirst(), c1);
				} else {
					return new Pair<>(pc0.getFirst() + pc1.getFirst(), merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new ExposureRecord(c_0.index, c_0.unitPrice,
							c_0.nativeVolume + c_1.nativeVolume, c_0.nativeValue + c_1.nativeValue, c_0.mmbtuVolume + c_1.mmbtuVolume, c_0.date, c_0.volumeUnit)));
				}
			} else if (operator.equals("-")) {
				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Pair<>(pc0.getFirst() - pc1.getFirst(), new Constant(((Constant) c0).getConstant() - ((Constant) c1).getConstant(), ""));
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					return new Pair<>(pc0.getFirst() - pc1.getFirst(), c0);
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					return new Pair<>(pc0.getFirst() - pc1.getFirst(),
							modify((ExposureRecords) c1, c -> new ExposureRecord(c.index, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date, c.volumeUnit)));
				} else {
					final ExposureRecords newC1 = modify((ExposureRecords) c1, c -> new ExposureRecord(c.index, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date, c.volumeUnit));
					return new Pair<>(pc0.getFirst() - pc1.getFirst(), merge((ExposureRecords) c0, newC1, (c_0, c_1) -> new ExposureRecord(c_0.index, c_0.unitPrice,
							c_0.nativeVolume + c_1.nativeVolume, c_0.nativeValue + c_1.nativeValue, c_0.mmbtuVolume + c_1.mmbtuVolume, c_0.date, c_0.volumeUnit)));
				}
			} else if (operator.equals("*")) {
				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Pair<>(pc0.getFirst() * pc1.getFirst(), new Constant(((Constant) c0).getConstant() * ((Constant) c1).getConstant(), ""));
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					final Constant const_c1 = (Constant) c1;
					final double constant = const_c1.getConstant();
					return new Pair<>(pc0.getFirst() * pc1.getFirst(), modify((ExposureRecords) c0,
							c -> new ExposureRecord(c.index, c.unitPrice, c.nativeVolume * constant, c.nativeValue * constant, c.mmbtuVolume * constant, c.date, const_c1.getNewVolumeUnit())));
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					final Constant const_c0 = (Constant) c0;
					final double constant = ((Constant) c0).getConstant();
					return new Pair<>(pc0.getFirst() * pc1.getFirst(), modify((ExposureRecords) c1,
							c -> new ExposureRecord(c.index, c.unitPrice, constant * c.nativeVolume, constant * c.nativeValue, constant * c.mmbtuVolume, c.date, const_c0.getNewVolumeUnit())));
				} else {
					return new Pair<>(pc0.getFirst() * pc1.getFirst(), merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new ExposureRecord(c_0.index, c_0.unitPrice,
							c_0.nativeVolume * c_1.nativeVolume, c_0.nativeValue * c_1.nativeValue, c_0.mmbtuVolume * c_1.mmbtuVolume, c_0.date, c_0.volumeUnit)));
				}
			} else if (operator.equals("/")) {
				final double value = pc1.getFirst() == 0.0 ? 0.0 : pc0.getFirst() / pc1.getFirst();
				if (c0 instanceof Constant && c1 instanceof Constant) {
					final Constant const_c0 = (Constant) c0;
					final Constant const_c1 = (Constant) c1;

					final double newConstant = const_c1.getConstant() == 0.0 ? 0.0 : const_c0.getConstant() / const_c1.getConstant();

					return new Pair<>(value, new Constant(newConstant, ""));
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					final Constant const_c1 = (Constant) c1;

					final double constant = const_c1.getConstant();
					if (constant == 0.0) {
						return new Pair<>(value, modify((ExposureRecords) c0, c -> new ExposureRecord(c.index, c.unitPrice, 0.0, 0.0, 0.0, c.date, const_c1.getNewVolumeUnit())));
					} else {
						return new Pair<>(value, modify((ExposureRecords) c0,
								c -> new ExposureRecord(c.index, c.unitPrice, c.nativeVolume / constant, c.nativeValue / constant, c.mmbtuVolume / constant, c.date, const_c1.getNewVolumeUnit())));
					}
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					final Constant const_c0 = (Constant) c0;

					final double constant = const_c0.getConstant();
					if (constant == 0.0) {
						return new Pair<>(value, modify((ExposureRecords) c0, c -> new ExposureRecord(c.index, c.unitPrice, 0.0, 0.0, 0.0, c.date, const_c0.getNewVolumeUnit())));
					} else {
						return new Pair<>(value, modify((ExposureRecords) c1, c -> {
							final double nativeVolume = c.nativeVolume == 0.0 ? 0.0 : constant / c.nativeVolume;
							final double nativeValue = c.nativeValue == 0.0 ? 0.0 : constant / c.nativeValue;
							final double mmbtuVolume = c.mmbtuVolume == 0.0 ? 0.0 : constant / c.mmbtuVolume;
							return new ExposureRecord(c.index, c.unitPrice, nativeVolume, nativeValue, mmbtuVolume, c.date, c.volumeUnit);
						}));
					}
				} else {
					return new Pair<>(value, merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> {
						final double nativeVolume = c_1.nativeVolume == 0.0 ? 0.0 : c_0.nativeVolume / c_1.nativeVolume;
						final double nativeValue = c_1.nativeValue == 0.0 ? 0.0 : c_0.nativeValue / c_1.nativeValue;
						final double mmbtuVolume = c_1.mmbtuVolume == 0.0 ? 0.0 : c_0.mmbtuVolume / c_1.mmbtuVolume;
						return new ExposureRecord(c_0.index, c_0.unitPrice, nativeVolume, nativeValue, mmbtuVolume, c_0.date, c_0.volumeUnit);
					}));
				}
			} else if (operator.equals("%")) {

				if (c0 instanceof Constant && c1 instanceof Constant) {
					return new Pair<>(0.01 * pc0.getFirst() * pc1.getFirst(), new Constant(0.01 * ((Constant) c0).getConstant() * ((Constant) c1).getConstant(), ""));
				} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
					assert false;
					throw new UnsupportedOperationException();
					// return modify((ExposureRecords) c0, c -> new ExposureRecord(c.index,
					// c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date));
				} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
					final double constant = 0.01 * ((Constant) c0).getConstant();
					return new Pair<>(0.01 * pc0.getFirst() * pc1.getFirst(), modify((ExposureRecords) c1,
							c -> new ExposureRecord(c.index, c.unitPrice, constant * c.nativeVolume, constant * c.nativeValue, constant * c.mmbtuVolume, c.date, c.volumeUnit)));
				} else {
					// return merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new
					// ExposureRecord(c_0.index, c_0.unitPrice, c_0.nativeVolume - c_1.nativeVolume,
					// c_0.nativeValue - c_1.nativeValue, c_0.mmbtuVolume - c_1.mmbtuVolume,
					// c_0.date));
					throw new UnsupportedOperationException();
				}
			} else {
				throw new IllegalStateException("Invalid operator");
			}
		} else if (node instanceof CommodityNode) {
			final CommodityNode commodityNode = (CommodityNode) node;

			// Should really look up actual value from curve...
			final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
			final ISeries series = p.getSeries(commodityNode.getCurve().getName());
			final Number evaluate = series.evaluate(Hours.between(PriceIndexUtils.dateZero, date));

			final double unitPrice = evaluate.doubleValue();
			double nativeVolume = inputRecord.volumeInMMBTU;

			// Perform units conversion.
			final String u = commodityNode.getCurve().getVolumeUnit();
			for (final UnitConversion factor : lookupData.pricingModel.getConversionFactors()) {
				if (factor.getTo().equalsIgnoreCase("mmbtu")) {
					if (factor.getFrom().equalsIgnoreCase(u)) {
						nativeVolume *= factor.getFactor();
						break;
					}
				}
			}

			final ExposureRecord record = new ExposureRecord(commodityNode.getCurve(), unitPrice, nativeVolume, nativeVolume * unitPrice, inputRecord.volumeInMMBTU, date,
					commodityNode.getCurve().getVolumeUnit());
			return new Pair<>(unitPrice, new ExposureRecords(record));
		} else if (node instanceof CurrencyNode) {

			final CurrencyNode currencyNode = (CurrencyNode) node;

			// Should really look up actual value from curve...
			final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.CURRENCY);
			final ISeries series = p.getSeries(currencyNode.getCurve().getName());
			final Number evaluate = series.evaluate(Hours.between(PriceIndexUtils.dateZero, date));

			final double unitPrice = evaluate.doubleValue();

			return new Pair<>(unitPrice, new Constant(1.0, ""));
		} else if (node instanceof ConversionNode) {
			final ConversionNode conversionNode = (ConversionNode) node;
			return new Pair<>(conversionNode.getFactor().getFactor(), new Constant(1.0, conversionNode.getToUnits()));
		} else if (node instanceof MaxFunctionNode) {
			if (node.getChildren().size() == 0) {
				throw new IllegalStateException();
			}
			Pair<Double, IExposureNode> best = getExposureNode(inputRecord, node.getChildren().get(0), date, lookupData, dayOfMonth);
			for (int i = 1; i < node.getChildren().size(); ++i) {
				final Pair<Double, IExposureNode> pc = getExposureNode(inputRecord, node.getChildren().get(i), date, lookupData, dayOfMonth);
				if (pc.getFirst() > best.getFirst()) {
					best = pc;
				}
			}
			return best;
		} else if (node instanceof MinFunctionNode) {
			if (node.getChildren().size() == 0) {
				throw new IllegalStateException();
			}
			Pair<Double, IExposureNode> best = getExposureNode(inputRecord, node.getChildren().get(0), date, lookupData, dayOfMonth);
			for (int i = 1; i < node.getChildren().size(); ++i) {
				final Pair<Double, IExposureNode> pc = getExposureNode(inputRecord, node.getChildren().get(i), date, lookupData, dayOfMonth);
				if (pc.getFirst() < best.getFirst()) {
					best = pc;
				}
			}
			return best;
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
						if (Objects.equals(c_c0.volumeUnit, c_c1.volumeUnit)) {
							n.records.add(mapper.apply(c_c0, c_c1));
							c1Itr.remove();
							c0Itr.remove();
							continue LOOP_C0;
						}
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
