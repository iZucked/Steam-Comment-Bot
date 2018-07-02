/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation;

import org.junit.jupiter.api.Assertions;

import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Journey;

/**
 * Methods for asserting which fuel types are used on a journey.
 * 
 * @author Adam
 * 
 */
public class FuelUsageAssertions {

	/**
	 * Assert that during a given journey NBO and FBO are not used and base fuel is.
	 * 
	 * @param j
	 */
	public static void assertLNGNotUsed(final Journey j) {

		for (final FuelQuantity fq : j.getFuels()) {
			if (fq.getFuel() == Fuel.NBO) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() == 0, "NBO not used");
				}
			} else if (fq.getFuel() == Fuel.FBO) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() == 0, "FBO not used");
				}
			} else if (fq.getFuel() == Fuel.BASE_FUEL) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() > 0, "Base fuel used");
				}
			}
		}
	}

	/**
	 * Assert that during a given journey base fuel is not used, but NBO and FBO are
	 * 
	 * @param j
	 */
	public static void assertBaseFuelNotUsed(final Journey j) {

		for (final FuelQuantity fq : j.getFuels()) {
			if (fq.getFuel() == Fuel.BASE_FUEL) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() == 0, "Base fuel not used");
				}
			} else if (fq.getFuel() == Fuel.NBO) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() > 0, "NBO used");
				}
			} else if (fq.getFuel() == Fuel.FBO) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() > 0, "FBO used");
				}
			}
		}
	}

	/**
	 * Assert that during a given journey FBO is not used, but NBO and base fuel are.
	 * 
	 * @param j
	 */
	public static void assertFBONotUsed(final Journey j) {

		for (final FuelQuantity fq : j.getFuels()) {
			if (fq.getFuel() == Fuel.FBO) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() == 0, "FBO not used");
				}
			} else if (fq.getFuel() == Fuel.BASE_FUEL) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() > 0, "BF used");
				}
			} else if (fq.getFuel() == Fuel.NBO) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() > 0, "NBO used");
				}
			}
		}
	}

	public static void assertFuelNotUsed(final Iterable<FuelQuantity> fuels, Fuel fuelType) {

		for (final FuelQuantity fq : fuels) {
			if (fq.getFuel() == fuelType) {
				for (final FuelAmount amount : fq.getAmounts()) {
					Assertions.assertTrue(amount.getQuantity() == 0, fuelType + " not used");
				}
			}
		}
	}

	public static void assertFuelUsed(final Iterable<FuelQuantity> fuels, Fuel fuelType, FuelUnit fuelUnit) {

		for (final FuelQuantity fq : fuels) {
			if (fq.getFuel() == fuelType) {
				for (final FuelAmount amount : fq.getAmounts()) {
					if (amount.getUnit() == fuelUnit) {
						Assertions.assertTrue(amount.getQuantity() > 0, fuelType + " used");
					}
				}
			}
		}
	}

}
