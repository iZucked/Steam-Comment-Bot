/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import org.junit.Assert;
import org.junit.Test;

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

		Assert.assertFalse(slot.isSetWindowSize());
		Assert.assertFalse(slot.isSetWindowStartTime());

		ZonedDateTime newWindowStart = ZonedDateTime.of(2013, 8, 29, 15, 0, 0, 0, ZoneId.of("GMT+5"));
		final FixSlotWindowChange change = new FixSlotWindowChange(slot, newWindowStart, domain);
		Assert.assertSame(slot, change.getChangedObject());

		// Check state has not changed
		Assert.assertFalse(slot.isSetWindowSize());
		Assert.assertFalse(slot.isSetWindowStartTime());
		Assert.assertEquals(originalWindowStart, slot.getWindowStart());

		final Command command = change.getCommand();
		Assert.assertNotNull(command);
		Assert.assertTrue(command.canExecute());

		command.execute();

		Assert.assertTrue(slot.isSetWindowSize());
		Assert.assertEquals(0, slot.getWindowSize());

		LocalDate changedWindowStart = slot.getWindowStart();

		Assert.assertEquals(2013, changedWindowStart.getYear());
		Assert.assertEquals(8, changedWindowStart.getMonthValue());
		Assert.assertEquals(29, changedWindowStart.getDayOfMonth());
		// GMT +5 offset
		Assert.assertTrue(slot.isSetWindowStartTime());
		Assert.assertEquals(10, slot.getWindowStartTime());

	}

}
