/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import java.time.LocalDateTime;
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
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.provider.FleetItemProviderAdapterFactory;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

public class FixVesselEventWindowChangeTest {

	@Test
	public void test() {

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new FleetItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

		Port p = PortFactory.eINSTANCE.createPort();
		Location l = PortFactory.eINSTANCE.createLocation();
		l.setTimeZone("UTC");
		p.setLocation(l);
		final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
		event.setPort(p);

		LocalDateTime originalStartAfter = LocalDateTime.of(2013, 8, 29, 0, 0, 0);

		event.setStartAfter(originalStartAfter);
		final LocalDateTime originalStartBy = originalStartAfter.plusDays(5);

		event.setStartBy(originalStartBy);

		ZonedDateTime newWindowStart = ZonedDateTime.of(2013, 8, 29, 15, 0, 0, 0, ZoneId.of("UTC"));

		final FixVesselEventWindowChange change = new FixVesselEventWindowChange(event, newWindowStart, domain);
		Assertions.assertSame(event, change.getChangedObject());

		// Check state has not changed
		Assertions.assertEquals(originalStartAfter, event.getStartAfter());
		Assertions.assertEquals(originalStartBy, event.getStartBy());

		final Command command = change.getCommand();
		Assertions.assertNotNull(command);
		Assertions.assertTrue(command.canExecute());

		command.execute();
		{
			final LocalDateTime changeEventStart = event.getStartAfter();

			Assertions.assertEquals(2013, changeEventStart.getYear());
			Assertions.assertEquals(8, changeEventStart.getMonthValue());
			Assertions.assertEquals(29, changeEventStart.getDayOfMonth());
			Assertions.assertEquals(15, changeEventStart.getHour());
		}
		{
			final LocalDateTime changeEventStart = event.getStartBy();

			Assertions.assertEquals(2013, changeEventStart.getYear());
			Assertions.assertEquals(8, changeEventStart.getMonthValue());
			Assertions.assertEquals(29, changeEventStart.getDayOfMonth());
			Assertions.assertEquals(15, changeEventStart.getHour());
		}

	}

}
