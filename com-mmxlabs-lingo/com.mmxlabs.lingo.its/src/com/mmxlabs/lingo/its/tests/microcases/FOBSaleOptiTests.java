/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.Vessel;

public class FOBSaleOptiTests extends AbstractMicroTestCase {

	@Test
	@Category(MicroTest.class)
	public void divertibleFOBSale_SimpleSwap() throws Exception {

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.withOptional(true)//
				.build() //

				.makeFOBSale("D1", true, LocalDate.of(2018, 2, 16), portFinder.findPort("Idku LNG"), null, entity, "5", nominatedVessel) //
				.with(s -> s.setShippingDaysRestriction(32))//
				.build() //

				.build();

		LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		LoadSlot load2 = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "4", 22.6) //
				.withOptional(true)//
				.build();

		optimiseWithLSOTest(runner -> {

			// Assert cargo wiring has changed.
			Assert.assertNotNull(load2.getCargo());
			Assert.assertTrue(load2.getCargo().getSlots().contains(discharge1));

		});
	}

	@Test
	@Category(MicroTest.class)
	public void divertibleFOBSale_SimpleSwap_NoDays() throws Exception {

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.withOptional(true)//
				.build() //

				.makeFOBSale("D1", true, LocalDate.of(2018, 2, 16), portFinder.findPort("Idku LNG"), null, entity, "5", nominatedVessel) //
				.with(s -> s.setShippingDaysRestriction(1))//
				.build() //

				.build();

		LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		LoadSlot load2 = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "4", 22.6) //
				.withOptional(true)//
				.build();

		optimiseWithLSOTest(runner -> {

			// Assert cargo wiring has not changed.
			Assert.assertNull(load2.getCargo());
			Assert.assertNotNull(load1.getCargo());
			Assert.assertTrue(load1.getCargo().getSlots().contains(discharge1));

		});
	}

	@Test
	@Category(MicroTest.class)
	public void divertibleFOBSale_SimpleSwap_OnlyLadenDays() throws Exception {

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.withOptional(true)//
				.build() //

				.makeFOBSale("D1", true, LocalDate.of(2018, 2, 16), portFinder.findPort("Idku LNG"), null, entity, "5", nominatedVessel) //
				.with(s -> s.setShippingDaysRestriction(16))//
				.build() //

				.build();

		LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		LoadSlot load2 = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2018, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "4", 22.6) //
				.withOptional(true)//
				.build();

		optimiseWithLSOTest(runner -> {

			// Assert cargo wiring has not changed.
			Assert.assertNull(load2.getCargo());
			Assert.assertNotNull(load1.getCargo());
			Assert.assertTrue(load1.getCargo().getSlots().contains(discharge1));

		});
	}
}
