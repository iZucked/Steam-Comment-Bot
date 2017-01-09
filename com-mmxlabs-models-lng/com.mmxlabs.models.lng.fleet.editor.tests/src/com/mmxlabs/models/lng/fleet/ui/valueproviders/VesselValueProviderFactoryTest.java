/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.valueproviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

public class VesselValueProviderFactoryTest {

	private static final class MinimalScenario {
		List<Vessel> vessels;
		FleetModel fleetModel;
		LNGScenarioModel scenarioModel;
		IReferenceValueProvider ref;
	}
	
	private FleetModel buildFleet(List<Vessel> vessels) {
		FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();
		fleetModel.getVessels().clear();
		for (Vessel vessel : vessels) {
			fleetModel.getVessels().add(vessel);
		}
		return fleetModel;
	}
	
	private LNGScenarioModel buildScenario(FleetModel fleetModel) {
		LNGScenarioModel lngScenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		LNGReferenceModel lngReferenceModel = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		lngScenarioModel.setReferenceModel(lngReferenceModel);
		lngReferenceModel.setFleetModel(fleetModel);
		return lngScenarioModel;
	}

	private List<Vessel> buildManyVessels(String[] vesselNames) {
		List<Vessel> vessels = new ArrayList<Vessel>();
		for (String name : vesselNames) {
			vessels.add(buildVessel(name));
		}
		return vessels;
	}
	private Vessel buildVessel(String name) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setVesselClass(null);
		vessel.setName(name);
		return vessel;
	}

	private MinimalScenario createDefaultMinimalScenario() {
		MinimalScenario minimalScenario = new MinimalScenario();
		String[] vesselNames = new String[] {
				"Vessel 1",
				"Vessel 2",
				"Vessel 3"
		};
		minimalScenario.vessels = buildManyVessels(vesselNames);
		minimalScenario.fleetModel = buildFleet(minimalScenario.vessels);
		minimalScenario.scenarioModel = buildScenario(minimalScenario.fleetModel);
		
		VesselValueProviderFactory vesselValueProviderFactory = new VesselValueProviderFactory();
		minimalScenario.ref = vesselValueProviderFactory.createReferenceValueProvider(null, FleetPackage.Literals.FLEET_MODEL__VESSELS, minimalScenario.scenarioModel);
		
		return minimalScenario;
	}
	
	private void changeVesselName(LNGScenarioModel model, int index, String name) {
		List<Vessel> vessels = model.getReferenceModel().getFleetModel().getVessels();
		vessels.get(index).setName(name);
	}
	
	private void removeVessel(LNGScenarioModel model, int index) {
		List<Vessel> vessels = model.getReferenceModel().getFleetModel().getVessels();
		vessels.remove(index);
	}
	
	private void addVessel(LNGScenarioModel model, String vesselName) {
		List<Vessel> vessels = model.getReferenceModel().getFleetModel().getVessels();
		vessels.add(buildVessel(vesselName));
	}
	
	@Test
	public void testNoModificationToModel() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test all vessels in value provider
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
	}

	@Test
	public void testChangeVesselName() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test changed name is picked up
		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed");
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
	}
	
	@Test
	public void testRemoveVessel() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test removed vessel is picked up
		removeVessel(minimalScenario.scenarioModel, 0);
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
	}
	
	@Test
	public void testAddVessel() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test new vessel is picked up
		addVessel(minimalScenario.scenarioModel, "Vessel 4");
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
	}
	
	@Test
	public void testSeveralModifications() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test all vessels in value provider
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
		
		// test changed name is picked up
		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed");
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
		
		// test removed vessel is picked up
		removeVessel(minimalScenario.scenarioModel, 0);
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
		
		// test new vessel is picked up
		addVessel(minimalScenario.scenarioModel, "Vessel 4");
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
		
		// test changed name is picked up
		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed Again");
		testNamesInValueProvider(minimalScenario.fleetModel, minimalScenario.ref);
	}
	
	private void testNamesInValueProvider(FleetModel fleetModel, IReferenceValueProvider valueProvider) {
		List<Pair<String, EObject>>  t = valueProvider.getAllowedValues(fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS);
		for (Pair<String, EObject> p : t) {
			Assert.assertTrue(getNamesFromModel(fleetModel.getVessels()).contains(p.getFirst()));
		}
	}
	
	private Set<String> getNamesFromModel(EList<Vessel> objects) {
		Set<String> s = new HashSet<String>();
		for (NamedObject object : objects) {
			s.add(object.getName());
		}
		return s;
	}

}
