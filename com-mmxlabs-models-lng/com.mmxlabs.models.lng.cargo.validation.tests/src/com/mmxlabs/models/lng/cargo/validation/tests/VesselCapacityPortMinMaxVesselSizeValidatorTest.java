/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.VesselCapacityPortMinMaxVesselSizeValidator;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

public class VesselCapacityPortMinMaxVesselSizeValidatorTest {

	private static final int vesselCapacity = 15000;
	
	public static Collection<?> getTestParameters() {
		return Arrays.asList(new Object[][] {
			{ 0, 0, 0, 0, true },
			{ 10000, 0, 0, 0, true },
			{ 0, 20000, 0, 0, true },
			{ 10000, 20000, 0, 0, true },
			{ 20000, 0, 0, 0, false },
			{ 0, 10000, 0, 0, false },
			{ 20000, 0, 0, 10000, false }			
		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testVesselAvailability(int port1MinVesselSize, int port1MaxVesselSize, int port2MinVesselSize, int port2MaxVesselSize, boolean expectedConstraintCheck) {
		final Port port1 = createPort(port1MinVesselSize, port1MaxVesselSize);
		final Port port2 = createPort(port2MinVesselSize, port2MaxVesselSize);
		final Vessel vessel = createVessel(vesselCapacity);
		VesselAvailability vat = createVesselAvailability(vessel);
		final Cargo cargo = createCargo(port1, port2, vat);
		checkValidator(cargo, expectedConstraintCheck);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testCharterInMarket(int port1MinVesselSize, int port1MaxVesselSize, int port2MinVesselSize, int port2MaxVesselSize, boolean expectedConstraintCheck) {
		final Port port1 = createPort(port1MinVesselSize, port1MaxVesselSize);
		final Port port2 = createPort(port2MinVesselSize, port2MaxVesselSize);
		final Vessel vessel = createVessel(vesselCapacity);
		CharterInMarket vat = createCharterInMarket(vessel);
		final Cargo cargo = createCargo(port1, port2, vat);
		checkValidator(cargo, expectedConstraintCheck);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testCharterInMarketOverride(int port1MinVesselSize, int port1MaxVesselSize, int port2MinVesselSize, int port2MaxVesselSize, boolean expectedConstraintCheck) {
		final Port port1 = createPort(port1MinVesselSize, port1MaxVesselSize);
		final Port port2 = createPort(port2MinVesselSize, port2MaxVesselSize);
		final Vessel vessel = createVessel(vesselCapacity);
		CharterInMarketOverride vat = createCharterInMarketOverride(vessel);
		final Cargo cargo = createCargo(port1, port2, vat);
		checkValidator(cargo, expectedConstraintCheck);
	}

	private CharterInMarketOverride createCharterInMarketOverride(final Vessel vessel) {
		CharterInMarketOverride vat = CargoFactory.eINSTANCE.createCharterInMarketOverride();
		CharterInMarket vat1 = createCharterInMarket(vessel); 
		vat.setCharterInMarket(vat1);
		return vat;
	}

	private CharterInMarket createCharterInMarket(final Vessel vessel) {
		CharterInMarket vat = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		vat.setVessel(vessel);
		return vat;
	}
	
	private VesselAvailability createVesselAvailability(final Vessel vessel) {
		VesselAvailability vat = CargoFactory.eINSTANCE.createVesselAvailability();
		vat.setVessel(vessel);
		return vat;
	}

	private Vessel createVessel(int capacity) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setCapacity(capacity);
		return vessel;
	}
	
	private Port createPort(int minVesselSize, int maxVesselSize) {
		final Port port = PortFactory.eINSTANCE.createPort();
		if (minVesselSize != 0) port.setMinVesselSize(minVesselSize);
		if (maxVesselSize != 0)	port.setMaxVesselSize(maxVesselSize);
		return port;
	}
		
	private Cargo createCargo(final Port srcPort, final Port destPort, final VesselAssignmentType vat) {
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		loadSlot.setPort(srcPort);
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
		dischargeSlot.setPort(destPort);
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);
		cargo.setVesselAssignmentType(vat);
		return cargo;
	}
	
	private void checkValidator(final EObject target, boolean expectSuccess) {
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final VesselCapacityPortMinMaxVesselSizeValidator constraint = new VesselCapacityPortMinMaxVesselSizeValidator();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assertions.assertTrue(status.isOK(), "Success expected");
		} else {
			Assertions.assertFalse(status.isOK(), "Failure expected");
		}
	}
}
