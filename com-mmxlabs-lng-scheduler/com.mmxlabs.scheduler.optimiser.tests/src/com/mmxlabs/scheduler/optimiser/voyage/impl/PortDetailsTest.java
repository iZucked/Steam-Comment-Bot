/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class PortDetailsTest {

	@Test
	public void testGetSetFuelConsumption() {
		final FuelComponent c = FuelComponent.Base;
		final IBaseFuel bf = Mockito.mock(IBaseFuel.class);
		FuelKey fk = new FuelKey(c, FuelUnit.MT, bf);
		final long value = 100L;
		final PortDetails details = new PortDetails(new PortOptions(Mockito.mock(IPortSlot.class)));
		Assertions.assertEquals(0, details.getFuelConsumption(fk));
		details.setFuelConsumption(fk, value);
		Assertions.assertEquals(value, details.getFuelConsumption(fk));
	}

	@Test
	public void testEquals() {

		final IPortSlot slot1 = Mockito.mock(IPortSlot.class, "s1");
		final IPortSlot slot2 = Mockito.mock(IPortSlot.class, "s2");

		final FuelComponent fuel1 = FuelComponent.Base;
		final FuelComponent fuel2 = FuelComponent.NBO;

		IBaseFuel bf = Mockito.mock(IBaseFuel.class);

		final CapacityViolationType cvt1 = CapacityViolationType.FORCED_COOLDOWN;
		final CapacityViolationType cvt2 = CapacityViolationType.VESSEL_CAPACITY;

		final PortDetails details1 = make(1, slot1, fuel1, bf, 5);
		final PortDetails details2 = make(1, slot1, fuel1, bf, 5);

		final PortDetails details3 = make(21, slot1, fuel1, bf, 5);
		final PortDetails details4 = make(1, slot2, fuel1, bf, 5);
		final PortDetails details5 = make(1, slot1, fuel2, IBaseFuel.LNG, 5);
		final PortDetails details6 = make(1, slot1, fuel1, bf, 25);

		Assertions.assertTrue(details1.equals(details1));
		Assertions.assertTrue(details1.equals(details2));
		Assertions.assertTrue(details2.equals(details1));

		Assertions.assertFalse(details1.equals(details3));
		Assertions.assertFalse(details1.equals(details4));
		Assertions.assertFalse(details1.equals(details5));
		Assertions.assertFalse(details1.equals(details6));

		Assertions.assertFalse(details3.equals(details1));
		Assertions.assertFalse(details4.equals(details1));
		Assertions.assertFalse(details5.equals(details1));
		Assertions.assertFalse(details6.equals(details1));

		Assertions.assertFalse(details1.equals(new Object()));
	}

	PortDetails make(final int duration, final @NonNull IPortSlot portSlot, FuelComponent fuel, final IBaseFuel bf, final long consumption) {

		final PortDetails d = new PortDetails(new PortOptions(portSlot));
		// final IVesselClass vesselClass = d.getOptions().getVessel().getVesselClass();

		d.getOptions().setVisitDuration(duration);

		d.setFuelConsumption(new FuelKey(fuel, fuel.getDefaultFuelUnit(), bf), consumption);
		return d;
	}

}
