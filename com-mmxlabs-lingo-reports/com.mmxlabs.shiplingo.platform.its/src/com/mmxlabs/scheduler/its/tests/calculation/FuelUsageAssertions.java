/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.calculation;

import org.junit.Assert;

import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Journey;

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

		for (final FuelQuantity fq : j.getFuelUsage()) {
			if (fq.getFuelType() == FuelType.NBO) {
				Assert.assertTrue("NBO not used", fq.getQuantity() == 0);
			} else if (fq.getFuelType() == FuelType.FBO) {
				Assert.assertTrue("FBO not used", fq.getQuantity() == 0);
			} else if (fq.getFuelType() == FuelType.BASE_FUEL) {
				Assert.assertTrue("Base fuel used", fq.getQuantity() > 0);
			}
		}
	}

	/**
	 * Assert that during a given journey base fuel is not used, but NBO and FBO are
	 * 
	 * @param j
	 */
	public static void assertBaseFuelNotUsed(final Journey j) {

		for (final FuelQuantity fq : j.getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL) {
				Assert.assertTrue("Base fuel not used", fq.getQuantity() == 0);
			} else if (fq.getFuelType() == FuelType.NBO) {
				Assert.assertTrue("NBO used", fq.getQuantity() > 0);
			} else if (fq.getFuelType() == FuelType.FBO) {
				Assert.assertTrue("FBO used", fq.getQuantity() > 0);
			}
		}
	}

	/**
	 * Assert that during a given journey FBO is not used, but NBO and base fuel are.
	 * 
	 * @param j
	 */
	public static void assertFBONotUsed(final Journey j) {

		for (final FuelQuantity fq : j.getFuelUsage()) {
			if (fq.getFuelType() == FuelType.FBO) {
				Assert.assertTrue("FBO not used", fq.getQuantity() == 0);
			} else if (fq.getFuelType() == FuelType.BASE_FUEL) {
				Assert.assertTrue("BF used", fq.getQuantity() > 0);
			} else if (fq.getFuelType() == FuelType.NBO) {
				Assert.assertTrue("NBO used", fq.getQuantity() > 0);
			}
		}
	}
}
