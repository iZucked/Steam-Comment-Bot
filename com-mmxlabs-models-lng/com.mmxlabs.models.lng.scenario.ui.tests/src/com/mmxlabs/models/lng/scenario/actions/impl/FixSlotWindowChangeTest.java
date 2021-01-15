/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.provider.CargoItemProviderAdapterFactory;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

public class FixSlotWindowChangeTest {

	@Test
	public void test() {

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new CargoItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		Port p = PortFactory.eINSTANCE.createPort();
		Location l = PortFactory.eINSTANCE.createLocation();
		l.setTimeZone("GMT");
		p.setLocation(l);
		slot.setPort(p);
		LocalDate originalWindowStart = LocalDate.of(2013, 8, 29);
		slot.setWindowStart(originalWindowStart);

		Assertions.assertFalse(slot.isSetWindowSize());
		Assertions.assertFalse(slot.isSetWindowStartTime());

		ZonedDateTime newWindowStart = ZonedDateTime.of(2013, 8, 29, 15, 0, 0, 0, ZoneId.of("GMT+5"));
		final FixSlotWindowChange change = new FixSlotWindowChange(slot, newWindowStart, domain);
		Assertions.assertSame(slot, change.getChangedObject());

		// Check state has not changed
		Assertions.assertFalse(slot.isSetWindowSize());
		Assertions.assertFalse(slot.isSetWindowStartTime());
		Assertions.assertEquals(originalWindowStart, slot.getWindowStart());

		final Command command = change.getCommand();
		Assertions.assertNotNull(command);
		Assertions.assertTrue(command.canExecute());

		command.execute();

		Assertions.assertTrue(slot.isSetWindowSize());
		Assertions.assertEquals(0, slot.getWindowSize());

		LocalDate changedWindowStart = slot.getWindowStart();

		Assertions.assertEquals(2013, changedWindowStart.getYear());
		Assertions.assertEquals(8, changedWindowStart.getMonthValue());
		Assertions.assertEquals(29, changedWindowStart.getDayOfMonth());
		// GMT +5 offset
		Assertions.assertTrue(slot.isSetWindowStartTime());
		Assertions.assertEquals(10, slot.getWindowStartTime());

	}

}
