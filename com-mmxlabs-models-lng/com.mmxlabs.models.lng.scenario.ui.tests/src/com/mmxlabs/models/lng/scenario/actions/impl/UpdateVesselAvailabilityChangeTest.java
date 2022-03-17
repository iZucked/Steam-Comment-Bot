/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.provider.FleetItemProviderAdapterFactory;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

public class UpdateVesselAvailabilityChangeTest {

	private EditingDomain createEditingDomain() {
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new FleetItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		return new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
	}

	@Test
	public void appendUpdatePortCommandTest_NewPort() {
		EditingDomain domain = createEditingDomain();

		VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();

		Port port1 = PortFactory.eINSTANCE.createPort();
		Port port2 = PortFactory.eINSTANCE.createPort();

		vesselAvailability.setStartAt(port1);

		final UpdateVesselAvailabilityChange change = new UpdateVesselAvailabilityChange(vesselAvailability, domain) {
		};

		change.appendUpdatePortCommand(vesselAvailability, domain, port2);

		final Command command = change.getCommand();
		Assertions.assertNotNull(command);
		Assertions.assertTrue(command.canExecute());

		command.execute();

		Port startAt = vesselAvailability.getStartAt();
		Assertions.assertNotNull(startAt);
		Assertions.assertSame(port2, startAt);
	}

	@Test
	public void appendUpdatePortCommandTest_Empty() {
		EditingDomain domain = createEditingDomain();

		VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();

		Port port1 = PortFactory.eINSTANCE.createPort();

		final UpdateVesselAvailabilityChange change = new UpdateVesselAvailabilityChange(vesselAvailability, domain) {
		};

		change.appendUpdatePortCommand(vesselAvailability, domain, port1);

		final Command command = change.getCommand();
		Assertions.assertNotNull(command);
		Assertions.assertTrue(command.canExecute());

		command.execute();

		Port startAt = vesselAvailability.getStartAt();
		Assertions.assertNotNull(startAt);
		Assertions.assertSame(port1, startAt);
	}
}
