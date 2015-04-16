/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.editor.valueproviders.SpotMarketsCharterValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

public class VesselAssignmentTypeValueProviderFactoryTest {

	private static final class MinimalScenario {
		CargoModel cargoModel;
		SpotMarketsModel spotMarketsModel;
		LNGScenarioModel scenarioModel;
		IReferenceValueProvider referenceValueProvider;
		Cargo cargo;
	}

	private CargoModel buildCargoModel(List<VesselAvailability> vesselAvailabilities) {
		CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		EList<VesselAvailability> cargoModelVesselAvailabilities = cargoModel.getVesselAvailabilities();
		for (VesselAvailability vesselAvailability : vesselAvailabilities) {
			cargoModelVesselAvailabilities.add(vesselAvailability);
		}
		return cargoModel;
	}

	private LNGScenarioModel buildScenario(CargoModel cargoModel, SpotMarketsModel spotMarketsModel) {
		LNGScenarioModel lngScenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		LNGPortfolioModel lngPortfolioModel = LNGScenarioFactory.eINSTANCE.createLNGPortfolioModel();
		lngScenarioModel.setPortfolioModel(lngPortfolioModel);
		lngPortfolioModel.setCargoModel(cargoModel);
		lngScenarioModel.setSpotMarketsModel(spotMarketsModel);
		return lngScenarioModel;
	}

	private VesselAvailability buildVesselAvailability(String name, VesselClass vesselClass) {
		VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		if (name != null) {
			Vessel vessel = buildVessel(name, vesselClass);
			vesselAvailability.setVessel(vessel);
		}
		return vesselAvailability;
	}

	private List<VesselAvailability> buildVesselAvailabilities(String[] names) {
		List<VesselAvailability> vesselAvailabilities = new ArrayList<>();
		VesselClass vesselClass = buildVesselClass("class1");
		for (String name : names) {
			vesselAvailabilities.add(buildVesselAvailability(name, vesselClass));
		}
		return vesselAvailabilities;
	}

	private VesselClass buildVesselClass(String name) {
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		vesselClass.setName(name);
		return vesselClass;
	}

	private Vessel buildVessel(String name, VesselClass vesselClass) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setVesselClass(vesselClass);
		vessel.setName(name);
		return vessel;
	}

	private MinimalScenario createDefaultMinimalScenario() {
		MinimalScenario minimalScenario = new MinimalScenario();
		String[] vesselNames = new String[] { "Vessel 1", "Vessel 2", "Vessel 3" };
		List<VesselAvailability> vesselAvailabilities = buildVesselAvailabilities(vesselNames);
		minimalScenario.cargoModel = buildCargoModel(vesselAvailabilities);

		minimalScenario.spotMarketsModel = SpotMarketsFactory.eINSTANCE.createSpotMarketsModel();
		minimalScenario.scenarioModel = buildScenario(minimalScenario.cargoModel, minimalScenario.spotMarketsModel);

		CargoValueProviderFactory cargoValueProviderFactory = new CargoValueProviderFactory();
		SpotMarketsCharterValueProviderFactory spotMarketsVPFactory = new SpotMarketsCharterValueProviderFactory();

		VesselAssignmentTypeValueProviderFactory vesselAssignmentTypeValueProviderFactory = new VesselAssignmentTypeValueProviderFactory(cargoValueProviderFactory, spotMarketsVPFactory);
		minimalScenario.referenceValueProvider = vesselAssignmentTypeValueProviderFactory.createReferenceValueProvider(CargoPackage.Literals.CARGO_MODEL,
				CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, minimalScenario.scenarioModel);

		minimalScenario.cargo = CargoFactory.eINSTANCE.createCargo();

		return minimalScenario;
	}

	private void changeVesselName(LNGScenarioModel model, int index, String name) {
		List<VesselAvailability> vessels = model.getPortfolioModel().getCargoModel().getVesselAvailabilities();
		vessels.get(index).getVessel().setName(name);
	}

	private void removeVessel(LNGScenarioModel model, int index) {
		List<VesselAvailability> vessels = model.getPortfolioModel().getCargoModel().getVesselAvailabilities();
		vessels.remove(index);
	}

	private void addVesselAvailability(LNGScenarioModel model, String vesselName, VesselClass vesselClass) {
		List<VesselAvailability> vessels = model.getPortfolioModel().getCargoModel().getVesselAvailabilities();
		vessels.add(buildVesselAvailability(vesselName, vesselClass));
	}

	@Test
	public void testNoModificationToModel() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test all vessels in value provider
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	@Test
	public void testRemoveVesselAvailability() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test removed vessel is picked up
		removeVessel(minimalScenario.scenarioModel, 0);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	@Test
	public void testAddVesselAvailability() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test removed vessel is picked up
		addVesselAvailability(minimalScenario.scenarioModel, "Vessel 4", buildVesselClass("class 2"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	@Test
	public void testChangeVesselName() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test removed vessel is picked up
		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed");
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	@Test
	@Ignore("Vessel name changes do not pass up chain. Enable once BugzId: 1444 is fixed.")
	public void changeAll() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();

		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed 0");
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// test all vessels in value provider
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed 1");
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		removeVessel(minimalScenario.scenarioModel, 0);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed 2");
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		addVesselAvailability(minimalScenario.scenarioModel, "Vessel 4", buildVesselClass("class 2"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed 3");
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

	}

	@Test
	public void changeAllExceptName() {
		MinimalScenario minimalScenario = createDefaultMinimalScenario();

		// test all vessels in value provider
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// remove a vessel availability
		removeVessel(minimalScenario.scenarioModel, 0);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// add a vessel availability
		addVesselAvailability(minimalScenario.scenarioModel, "Vessel 4", buildVesselClass("class 2"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// add a null vessel availability
		addVesselAvailability(minimalScenario.scenarioModel, null, null);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// add a vessel availability
		addVesselAvailability(minimalScenario.scenarioModel, "Vessel 5", buildVesselClass("class 2"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// add a vessel availability
		addVesselAvailability(minimalScenario.scenarioModel, "Vessel 6", buildVesselClass("class 2"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// remove a vessel availability
		removeVessel(minimalScenario.scenarioModel, 2);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	private void testNamesInValueProvider(CargoModel cargoModel, Cargo cargo, IReferenceValueProvider valueProvider) {
		List<Pair<String, EObject>> t = valueProvider.getAllowedValues(cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
		for (Pair<String, EObject> p : t) {
			Assert.assertTrue(getNamesFromModel(cargoModel.getVesselAvailabilities()).contains(p.getFirst()));
		}
	}

	private Set<String> getNamesFromModel(EList<VesselAvailability> objects) {
		Set<String> s = new HashSet<String>();
		s.add("<Unassigned>");
		for (VesselAvailability object : objects) {
			Vessel vessel = object.getVessel();
			if (vessel != null) {
				s.add(vessel.getName());
			}
		}
		return s;
	}

}
