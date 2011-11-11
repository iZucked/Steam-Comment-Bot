/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import org.junit.Ignore;
import org.junit.Test;

import scenario.Scenario;
import scenario.port.Port;
import scenario.port.PortFactory;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;

/**
 * @author Adam Semenenko
 *
 */
public class MultipleCargoFuelConsumptionTests {

	private static final int dischargePrice = 10;
	
	@Ignore("Incomplete")
	@Test
	public void twoCargoTest() { 
		
		CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);
		
		final Port portA = PortFactory.eINSTANCE.createPort();
		portA.setName("A");
		final Port portB= PortFactory.eINSTANCE.createPort();
		portA.setName("B");
		
		csc.addVesselSimple("class", 1, 10, 10, 10000, 10, 10, 0, 0);
		
		csc.addCargo(portA, portB, dischargePrice, 10, 10);
		
		final Scenario scenario = csc.buildScenario();
	}
}
