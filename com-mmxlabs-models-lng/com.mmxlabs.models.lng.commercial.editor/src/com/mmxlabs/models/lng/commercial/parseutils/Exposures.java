/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

				for (final CommodityIndex idx : pricingModel.getCommodityIndices()) {
					ExposureDetail exposureDetail = createExposureDetail(node, pricingDate, idx, volume, slot instanceof LoadSlot, lookupData);
					if (exposureDetail != null) {

						// Should be added to a command!
						cmd.append(AddCommand.create(domain, slotAllocation, SchedulePackage.Literals.SLOT_ALLOCATION__EXPOSURES, exposureDetail));
					}
				}
			}
		}

		if (!scenarioModel.getScheduleModel().isDirty())

		{
			cmd.append(SetCommand.create(domain, scenarioModel.getScheduleModel(), SchedulePackage.Literals.SCHEDULE_MODEL__DIRTY, Boolean.FALSE));
		}
		domain.getCommandStack().execute(cmd);

	}

	// private static final Set<String> operators = CollectionsUtil.makeHashSet("+", "-", "/", "*", "%");

	/**
	 * For unit test use
	 * 
	 * @param priceExpression
	 * @param lookupData
	 * @return
	 */
	public static @Nullable ExposureDetail calculateExposure(final @NonNull String priceExpression, CommodityIndex index, YearMonth date, double volumeInMMBTu, boolean isPurchase,
			final @NonNull LookupData lookupData) {

		// Parse the expression
		final IExpression<Node> parse = new RawTreeParser().parse(priceExpression);
		final Node p = parse.evaluate();
		final Node node = expandNode(p, lookupData);
		final MarkedUpNode markedUpNode = markupNodes(node, lookupData);

		return createExposureDetail(markedUpNode, date, index, volumeInMMBTu, isPurchase, lookupData);
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
			if (lookupData.commodityMap.containsKey(parentNode.token)) {
				final CommodityIndex idx = lookupData.commodityMap.get(parentNode.token);

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
				String[] result = new String[1];
				ServiceHelper.withAllServices(IExposuredExpressionProvider.class, provider -> {
					String exp = provider.provideExposedPriceExpression(slot, slotAllocation);
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

		if (parentNode.token.equals("*") || parentNode.token.equals("/") || parentNode.token.equals("+") || parentNode.token.equals("-") || parentNode.token.equals("%")) {
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

	// private void evaluate(MarkedUpNode node, String indexName, YearMonth date) {
	private static @NonNull CoEff getExposureCoefficient(final @NonNull MarkedUpNode node, final YearMonth date, final CommodityIndex index, final Mode mode, final LookupData lookupData) {
		if (node instanceof ConstantNode) {
			final ConstantNode constantNode = (ConstantNode) node;
			// if (mode == Mode.VALUE) {
			// return new CoEff(1, false);
			// } else {
			return new CoEff(constantNode.getConstant(), false);
			// }
		}

		// Arithmetic operator token
		else if (node instanceof OperatorNode) {
			final OperatorNode operatorNode = (OperatorNode) node;
			if (node.getChildren().size() != 2) {
				throw new IllegalStateException();
				// Invalid state
				// return new CoEff(0, false);
			}

			final String operator = operatorNode.getOperator();
			final CoEff c0 = getExposureCoefficient(node.getChildren().get(0), date, index, mode, lookupData);
			final CoEff c1 = getExposureCoefficient(node.getChildren().get(1), date, index, mode, lookupData);
			if (operator.equals("+")) {
				if (mode == Mode.VALUE) {
					if (c0.isExposed() && c1.isExposed()) {
						return c0;// new CoEff(c0.getCoeff() * c1.getCoeff(), true);
					} else if (c0.isExposed()) {
						return c0;
					} else if (c1.isExposed()) {
						return c1;
					} else {
						return new CoEff(1.0, false);
					}
				}
				// addition: add coefficients of summands
				if (c0.isExposed() == c1.isExposed()) {
					return new CoEff(c0.getCoeff() + c1.getCoeff(), c0.isExposed());
				} else if (c0.isExposed()) {
					return c0;
				} else {
					assert c1.isExposed();
					return c1;
				}
			} else if (operator.equals("*")) {
				if (mode == Mode.VALUE) {
					if (c0.isExposed() && c1.isExposed()) {
						return c0;// new CoEff(c0.getCoeff() * c1.getCoeff(), true);
					} else if (c0.isExposed()) {
						return c0;
					} else if (c1.isExposed()) {
						return c1;
					} else {
						return new CoEff(1.0, false);
					}
				}
				return new CoEff(c0.getCoeff() * c1.getCoeff(), c0.isExposed() | c1.isExposed());
			} else if (operator.equals("/")) {
				if (mode == Mode.VALUE) {
					if (c0.isExposed() && c1.isExposed()) {
						return c0;// new CoEff(c0.getCoeff() / c1.getCoeff(), true);
					} else if (c0.isExposed()) {
						return c0;
					} else if (c1.isExposed()) {
						return c1;
					} else {
						return new CoEff(1.0, false);
					}
				}
				return new CoEff(c0.getCoeff() / c1.getCoeff(), c0.isExposed() | c1.isExposed());
			} else if (operator.equals("%")) {
				if (mode == Mode.VALUE) {
					if (c1.isExposed()) {
						return c1;
					} else {
						return new CoEff(1.0, false);
					}
				}
				// // In value mode this will be 1.0 rather than real value
				// return new CoEff(c0.getCoeff() * c1.getCoeff(), c0.isExposed() | c1.isExposed());
				// } else {
				return new CoEff(0.01 * c0.getCoeff() * c1.getCoeff(), c0.isExposed() | c1.isExposed());
				// }
			} else if (operator.equals("-")) {
				// subtraction: subtract coefficients
				if (c0.isExposed() == c1.isExposed()) {
					return new CoEff(c0.getCoeff() - c1.getCoeff(), c0.isExposed());
				} else if (c0.isExposed()) {
					return c0;
				} else {
					assert c1.isExposed();
					return new CoEff(-c1.getCoeff(), c1.isExposed());
				}
			} else {
				throw new IllegalStateException("Invalid operator");
			}
		}

		else if (node instanceof CommodityNode) {
			final CommodityNode commodityNode = (CommodityNode) node;

			// IF MODE == VOLUME
			if (mode == Mode.VOLUME) {
				if (commodityNode.getIndex() == index) {
					return new CoEff(1, true);
				} else {
					return new CoEff(1, false);
				}
			}
			if (mode == Mode.VALUE) {

				if (commodityNode.getIndex() == index) {
					final SeriesParser p = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
					final ISeries series = p.getSeries(commodityNode.getIndex().getName());
					// "Magic" date constant used in PriceIndexUtils for date zero
					final Number evaluate = series.evaluate(Hours.between(YearMonth.of(2000, 1), date));

					return new CoEff(evaluate.doubleValue(), true);
				} else {
					return new CoEff(1, false);
				}
			}

		} else if (node instanceof CurrencyNode) {
			final CurrencyNode currencyNode = (CurrencyNode) node;

			// IF MODE == VOLUME
			if (mode == Mode.VOLUME) {
				return new CoEff(1, false);
			} else if (mode == Mode.VALUE) {
				return new CoEff(1, false);
			}
		} else if (node instanceof ConversionNode) {
			final ConversionNode conversionNode = (ConversionNode) node;

			// IF MODE == VOLUME
			if (mode == Mode.VOLUME) {
				return new CoEff(1, false);
			} else if (mode == Mode.VALUE) {
				return new CoEff(1, false);
			}
		}

		throw new IllegalStateException("Unexpected node type");

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

	private static @Nullable ExposureDetail createExposureDetail(final @NonNull MarkedUpNode node, final YearMonth pricingDate, final CommodityIndex idx, double volumeInMMBtu, boolean isPurchase,
			final LookupData lookupData) {
		final ExposureDetail exposureDetail = ScheduleFactory.eINSTANCE.createExposureDetail();
		{
			final CoEff exposureCoefficient = getExposureCoefficient(node, pricingDate, idx, Mode.VOLUME, lookupData);
			if (!exposureCoefficient.isExposed()) {
				return null;
			}
			if (exposureCoefficient.getCoeff() == 0.0) {
				return null;
			}

			double exposure = exposureCoefficient.getCoeff() * volumeInMMBtu;

			if (!isPurchase) {
				// -ve exposure
				exposure = -exposure;
			}

			exposureDetail.setIndex(idx);

			exposureDetail.setDate(pricingDate);
			exposureDetail.setVolumeInMMBTU(exposure);
			{
				final String u = idx.getVolumeUnit();
				double nativeVolume = exposure;
				for (UnitConversion factor : lookupData.pricingModel.getConversionFactors()) {
					if (factor.getTo().equalsIgnoreCase("mmbtu")) {
						if (factor.getFrom().equalsIgnoreCase(u)) {
							nativeVolume *= factor.getFactor();
							break;
						}
					}
				}

				exposureDetail.setVolumeInNativeUnits(nativeVolume);
			}
			exposureDetail.setCurrencyUnit(idx.getCurrencyUnit());
			exposureDetail.setVolumeUnit(idx.getVolumeUnit());
		}
		{
			final CoEff exposureCoefficient = getExposureCoefficient(node, pricingDate, idx, Mode.VALUE, lookupData);
			if (exposureCoefficient.isExposed() && exposureCoefficient.getCoeff() != 0.0) {

				// Use abs as any negation link to the co-eff should already be applied to the volume
				double exposure = Math.abs(exposureCoefficient.getCoeff()) * exposureDetail.getVolumeInNativeUnits();
				exposureDetail.setNativeValue(exposure);
				exposureDetail.setUnitPrice(exposureCoefficient.getCoeff());
			}
		}
		return exposureDetail;
	}
}
