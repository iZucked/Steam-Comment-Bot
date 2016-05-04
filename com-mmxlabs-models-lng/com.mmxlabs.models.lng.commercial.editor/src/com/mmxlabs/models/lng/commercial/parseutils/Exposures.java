/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 * Utility class to calculate schedule exposure to market indices. Provides static methods
 * 
 * @author Simon McGregor
 */
public class Exposures {

	private static final Set<String> operators = CollectionsUtil.makeHashSet("+", "-", "/", "*", "%");

	/**
	 * Class to store cumulative numeric data by key.
	 */
	public static class CumulativeMap<T> extends HashMap<T, Double> {
		private static final long serialVersionUID = 1L;

		/**
		 * Adds a cumulative numeric value to the map. If the key is contained in the map, the new value is added to the existing value. Otherwise, the new value is entered into the map. In other
		 * words, the map conceptually defaults new keys to zero before adding the specified value.
		 * 
		 * @param key
		 * @param value
		 */
		public void plusEquals(final T key, final Double value) {
			if (containsKey(key)) {
				put(key, get(key) + value);
			} else {
				put(key, value);
			}
		}
	}

	public static CoEff getExposureCoefficient(final @NonNull String priceExpression, final @NonNull CommodityIndex index, final @NonNull PricingModel pricingModel, @NonNull final YearMonth date) {

		// If derived, then skip
		if (index.getData() instanceof DerivedIndex<?>) {
			return new CoEff(0, false);
		}

		// Null check
		final String exposedIndexToken = index.getName();
		if (exposedIndexToken == null) {
			return new CoEff(0, false);
		}

		// Here we perform multiple steps. First we recursively expand out the price expression, replacing terms referencing derived curves with the derived curve data or expression. We also replace
		// any index reference which is not the target index with the *value* for the given date. Finally we evaluate the expression to determine the multipliers to apply to the index.

		final RawTreeParser parser = new RawTreeParser();
		try {
			// Parse basic expression
			final IExpression<Node> parsed = parser.parse(priceExpression);

			// Expand out the tree, if a Node is another expression, recursively expand it
			Node n = expandNode(exposedIndexToken, parsed.evaluate(), pricingModel, date);
			if (n == null) {
				n = parsed.evaluate();
			}
			// Does out expanded parse tree contain the index we are looking for?
			if (!checkIndexNode(exposedIndexToken, n)) {
				return new CoEff(0, false);
			}

			// Get the final index co-efficient (apply the arithmetic)
			return getExposureCoefficient(n, exposedIndexToken);
		} catch (final Exception e) {
			return new CoEff(0, false);
		}
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
	private static @Nullable Node expandNode(final @NonNull String exposedIndexToken, @NonNull final Node parentNode, final PricingModel pricingModel, final YearMonth date) {

		// Match!, nothing to expand
		if (exposedIndexToken.equals(parentNode.token)) {
			return null;
		}
		if (parentNode.children.length == 0) {
			// Leaf node, this should be an index or a value

			// Look to see if this is a derived index
			for (final CommodityIndex idx : pricingModel.getCommodityIndices()) {
				// Null checks
				if (idx.getName() == null) {
					continue;
				}

				if (parentNode.token.equals(idx.getName())) {
					// Matched derived index...
					if (idx.getData() instanceof DerivedIndex<?>) {
						final DerivedIndex<?> derivedIndex = (DerivedIndex<?>) idx.getData();
						// Parse the expression
						final IExpression<Node> parse = new RawTreeParser().parse(derivedIndex.getExpression());
						final Node p = parse.evaluate();
						// Expand the parsed tree again if needed,
						@Nullable
						final Node expandNode = expandNode(exposedIndexToken, p, pricingModel, date);

						// return the new sub-parse tree for the expression
						if (expandNode != null) {
							return expandNode;
						}
						return p;
					}
				}
			}
			// Not a derived index, so evaluate the expression to get the current value for the pricing event date
			try {
				final SeriesParser p = PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.COMMODITY);
				final ISeries series = p.getSeries(parentNode.token);
				// "Magic" date constant used in PriceIndexUtils for date zero
				final Number evaluate = series.evaluate(Months.between(YearMonth.of(2000, 1), date));
				return new Node(Double.toString(evaluate.doubleValue()), new Node[0]);
			} catch (final Exception e) {
				// Unknown error
				return null;
			}
		} else {
			// We have children, token *should* be an operator, expand out the child nodes
			for (int i = 0; i < parentNode.children.length; ++i) {
				final Node replacement = expandNode(exposedIndexToken, parentNode.children[i], pricingModel, date);
				if (replacement != null) {
					parentNode.children[i] = replacement;
				}
			}
		}
		return null;
	}

	/**
	 * Examine the {@link Node} tree to see if the target index is present
	 * 
	 * @param index
	 * @param n
	 * @return
	 */
	private static boolean checkIndexNode(final @NonNull String exposedIndexToken, @NonNull final Node n) {
		// Found index match, pass up the tree
		if (exposedIndexToken.equals(n.token)) {
			return true;
		}
		// No children, termination condition
		if (n.children.length == 0) {
			return false;
		}

		// Check all the children for the index name
		for (int i = 0; i < n.children.length; ++i) {
			if (checkIndexNode(exposedIndexToken, n.children[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines the amount of exposure to a particular index which is created by a specific contract.
	 * 
	 * @param contract
	 * @param index
	 * @return
	 */
	public static CoEff getExposureCoefficient(final @NonNull Slot slot, final @NonNull CommodityIndex index, final @NonNull PricingModel pricingModel, @NonNull final YearMonth date) {
		String priceExpression = null;
		if (slot.isSetPriceExpression()) {
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
			}
		}

		if (priceExpression != null && !priceExpression.isEmpty()) {
			if (!priceExpression.equals("?")) {
				return getExposureCoefficient(priceExpression, index, pricingModel, date);
			}
		}

		return new CoEff(1, false);
	}

	/**
	 * Calculates the exposure of a given schedule to a given index. Depends on the getExposureCoefficient method to correctly determine the exposure per cubic metre associated with a load or
	 * discharge slot.
	 * 
	 * @param schedule
	 * @param index
	 * @return
	 */
	public static Map<YearMonth, Double> getExposuresByMonth(final @NonNull Schedule schedule, final @NonNull CommodityIndex index, @NonNull final PricingModel pricingModel) {
		final Map<YearMonth, Double> result = new HashMap<>();

		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				final int volume = slotAllocation.getEnergyTransferred();
				final Slot slot = slotAllocation.getSlot();
				if (slot == null) {
					continue;
				}

				final YearMonth pricingDate = getPricingDate(slotAllocation);
				if (pricingDate == null) {
					continue;
				}
				// final ZonedDateTime date = slotAllocation.getSlotVisit().getStart();
				// YearMonth.of(date.getYear(), date.getMonthValue())
				//
				final CoEff exposureCoefficient = getExposureCoefficient(slot, index, pricingModel, pricingDate);
				if (!exposureCoefficient.exposed) {
					continue;
				}
				if (exposureCoefficient.coeff == 0.0) {
					continue;
				}

				double exposure = exposureCoefficient.coeff * volume;

				if (slot instanceof SpotSlot) {
					// FIXME - Should spot volumes be included?
					// continue;
				}

				if (slot instanceof LoadSlot) {
					// +ve exposure
				} else if (slot instanceof DischargeSlot) {
					// -ve exposure
					exposure = -exposure;
				} else {
					// Unknown slot type!
					throw new IllegalStateException("Unsupported slot type");
				}

				result.merge(pricingDate, exposure, (a, b) -> (a + b));
			}
		}

		return result;

	}

	public static class CoEff {
		private final double coeff;
		private final boolean exposed;

		public CoEff(final double coeff, final boolean exposed) {
			this.coeff = coeff;
			this.exposed = exposed;
		}

		public double getCoeff() {
			return coeff;
		}

		public boolean isExposed() {
			return exposed;
		}

	}

	private static @NonNull CoEff getExposureCoefficient(final @NonNull Node node, final @NonNull String exposedIndexToken) {

		final String token = node.token;

		// Is this our search term?
		if (node.token.equals(exposedIndexToken)) {
			return new CoEff(1, true);
		}

		// Arithmetic operator token
		if (operators.contains(token)) {
			if (node.children.length != 2) {
				// Invalid state
				return new CoEff(0, false);
			}
			final CoEff c0 = getExposureCoefficient(node.children[0], exposedIndexToken);
			final CoEff c1 = getExposureCoefficient(node.children[1], exposedIndexToken);
			if (token.equals("+")) {
				// addition: add coefficients of summands
				if (c0.exposed == c1.exposed) {
					return new CoEff(c0.coeff + c1.coeff, c0.exposed);
				} else if (c0.exposed) {
					return c0;
				} else {
					assert c1.exposed;
					return c1;
				}
			} else if (node.token.equals("*")) {
				return new CoEff(c0.coeff * c1.coeff, c0.exposed | c1.exposed);
			} else if (node.token.equals("/")) {
				return new CoEff(c0.coeff / c1.coeff, c0.exposed | c1.exposed);
			} else if (node.token.equals("%")) {
				return new CoEff(0.01 * c0.coeff * c1.coeff, c0.exposed | c1.exposed);
			} else if (node.token.equals("-")) {
				// subtraction: subtract coefficients
				if (c0.exposed == c1.exposed) {
					return new CoEff(c0.coeff - c1.coeff, c0.exposed);
				} else if (c0.exposed) {
					return c0;
				} else {
					assert c1.exposed;
					return new CoEff(-c1.coeff, c1.exposed);
				}
			} else {
				throw new IllegalStateException("Invalid operator");
			}
		}

		// Try and parse token as a number
		try {
			final double parsed = Double.parseDouble(token);
			return new CoEff(parsed, false);
		} catch (final NumberFormatException e) {
			// Ignore
		}

		// Some kind of unknown thing. (probably an illegal state)
		return new CoEff(0, false);
	}

	private static final Function<Optional<SlotAllocation>, Optional<YearMonth>> getCompletionOf = slotAllocation -> {
		if (slotAllocation.isPresent()) {
			final ZonedDateTime end = slotAllocation.get().getSlotVisit().getEnd();
			final ZonedDateTime withZoneSameLocal = end.withZoneSameLocal(ZoneId.of("UTC"));
			return Optional.of(YearMonth.of(withZoneSameLocal.getYear(), withZoneSameLocal.getMonthValue()));
		} else {
			return Optional.empty();
		}
	};

	private static final Function<Optional<SlotAllocation>, Optional<YearMonth>> getStartOf = slotAllocation -> {
		if (slotAllocation.isPresent()) {
			final ZonedDateTime end = slotAllocation.get().getSlotVisit().getStart();
			final ZonedDateTime withZoneSameLocal = end.withZoneSameLocal(ZoneId.of("UTC"));
			return Optional.of(YearMonth.of(withZoneSameLocal.getYear(), withZoneSameLocal.getMonthValue()));
		} else {
			return Optional.empty();
		}
	};

	private static final BiFunction<Optional<SlotAllocation>, Class<? extends Slot>, Optional<SlotAllocation>> getAllocationOf = (slotAllocation, cls) -> {
		if (slotAllocation.isPresent()) {
			final Slot slot = slotAllocation.get().getSlot();
			if (cls.isInstance(slot)) {
				return slotAllocation;
			}
			final CargoAllocation cargoAllocation = slotAllocation.get().getCargoAllocation();
			if (cargoAllocation != null) {
				for (final SlotAllocation slotAllocation2 : cargoAllocation.getSlotAllocations()) {
					if (cls.isInstance(slotAllocation2.getSlot())) {
						return Optional.of(slotAllocation2);
					}
				}
			}
		}
		return Optional.empty();
	};

	private static final Function<SlotAllocation, Optional<SlotAllocation>> getLoadAllocationOf = (slotAllocation) -> getAllocationOf.apply(Optional.of(slotAllocation), LoadSlot.class);
	private static final Function<SlotAllocation, Optional<SlotAllocation>> getDischargeAllocationOf = (slotAllocation) -> getAllocationOf.apply(Optional.of(slotAllocation), DischargeSlot.class);

	/**
	 * Returns the pricing {@link YearMonth} for this {@link SlotAllocation}
	 * 
	 * @param slotAllocation
	 * @return
	 */
	private static @Nullable YearMonth getPricingDate(@NonNull final SlotAllocation slotAllocation) {
		final Slot slot = slotAllocation.getSlot();

		final Optional<YearMonth> pricingDate;
		if (slot.isSetPricingDate()) {
			final LocalDate slotPricingDate = slot.getPricingDate();
			pricingDate = Optional.of(YearMonth.of(slotPricingDate.getYear(), slotPricingDate.getMonthValue()));
		} else {
			switch (slot.getSlotOrDelegatedPricingEvent()) {
			case END_DISCHARGE:
				pricingDate = getCompletionOf.apply(getDischargeAllocationOf.apply(slotAllocation));
				break;
			case END_LOAD:
				pricingDate = getCompletionOf.apply(getLoadAllocationOf.apply(slotAllocation));
				break;
			case START_DISCHARGE:
				pricingDate = getStartOf.apply(getDischargeAllocationOf.apply(slotAllocation));
				break;
			case START_LOAD:
				pricingDate = getStartOf.apply(getLoadAllocationOf.apply(slotAllocation));
				break;
			default:
				pricingDate = null;
			}
		}
		return pricingDate.get();
	}

}
