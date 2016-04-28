/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 * Utility class to calculate schedule exposure to market indices. Provides static methods
 * 
 * @author Simon McGregor
 */
public class Exposures {

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

	private static double getExposureCoefficient(final Node node, final CommodityIndex index) {
		final String indexToken = index.getName();
		final String token = node.token;

		// addition: add coefficients of summands
		if (token.equals("+")) {
			return getExposureCoefficient(node.children[0], index) + getExposureCoefficient(node.children[1], index);
		}
		// multiplication: check for index token and return the other value if appropriate
		else if (token.equals("*") || token.equals("%")) {
			if (node.children[0].children.length > 0 || node.children[1].children.length > 0) {
				throw new RuntimeException("Expression too complex");
			}

			for (int i = 0; i < 2; i++) {
				if (node.children[i].token.equals(indexToken)) {
					// divide by 100 for % symbol
					double multiplier = token.equals("%") ? 0.01 : 1;
					return multiplier * Double.parseDouble(node.children[1 - i].token);
				}
			}

		}
		// subtraction: subtract coefficients
		else if (node.token.equals("-")) {
			return getExposureCoefficient(node.children[0], index) - getExposureCoefficient(node.children[1], index);
		}
		// division: check for index token and return reciprocal of other parameter
		else if (node.token.equals("/")) {
			if (!node.children[0].token.equals(indexToken) || node.children[1].children.length > 0) {
				throw new RuntimeException("Expression too complex");
			}
			return 1 / Double.parseDouble(node.children[1].token);
		}
		// index token alone has coefficient of 1
		else if (node.token.equals(indexToken)) {
			return 1;
		}

		return 0;

	}

	/**
	 */
	public static double getExposureCoefficient(final String priceExpression, final CommodityIndex index) {
		final RawTreeParser parser = new RawTreeParser();
		try {
			final IExpression<Node> parsed = parser.parse(priceExpression);
			return getExposureCoefficient(parsed.evaluate(), index);
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * Determines the amount of exposure to a particular index which is created by a specific contract.
	 * 
	 * @param contract
	 * @param index
	 * @return
	 */
	public static double getExposureCoefficient(final Slot slot, final CommodityIndex index) {
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
				return getExposureCoefficient(priceExpression, index);
			}
		}

		return 0;
	}

	/**
	 * Calculates the exposure of a given schedule to a given index. Depends on the getExposureCoefficient method to correctly determine the exposure per cubic metre associated with a load or
	 * discharge slot.
	 * 
	 * @param schedule
	 * @param index
	 * @return
	 */
	public static Map<YearMonth, Double> getExposuresByMonth(final Schedule schedule, final CommodityIndex index) {
		final CumulativeMap<YearMonth> result = new CumulativeMap<YearMonth>();

		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				final int volume = slotAllocation.getEnergyTransferred();
				final Slot slot = slotAllocation.getSlot();
				final double exposureCoefficient = getExposureCoefficient(slot, index);
				double exposure = exposureCoefficient * volume;

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
				final ZonedDateTime date = slotAllocation.getSlotVisit().getStart();
				result.plusEquals(YearMonth.of(date.getYear(), date.getMonthValue()), exposure);
			}
		}

		return result;

	}
}
