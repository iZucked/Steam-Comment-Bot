/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
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

	private CargoModel buildCargoModel(final List<VesselCharter> vesselAvailabilities) {
		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		final EList<VesselCharter> cargoModelVesselAvailabilities = cargoModel.getVesselCharters();
		for (final VesselCharter vesselCharter : vesselAvailabilities) {
			cargoModelVesselAvailabilities.add(vesselCharter);
		}
		return cargoModel;
	}

	private LNGScenarioModel buildScenario(final CargoModel cargoModel, final SpotMarketsModel spotMarketsModel) {
		final LNGScenarioModel lngScenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		final LNGReferenceModel referenceModel = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		lngScenarioModel.setReferenceModel(referenceModel);
		lngScenarioModel.setCargoModel(cargoModel);
		referenceModel.setSpotMarketsModel(spotMarketsModel);
		referenceModel.setFleetModel(FleetFactory.eINSTANCE.createFleetModel());

		return lngScenarioModel;
	}

	private VesselCharter buildVesselCharter(final Vessel vessel) {
		final VesselCharter vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setVessel(vessel);
		return vesselCharter;
	}

	private List<VesselCharter> buildVesselAvailabilities(final String[] names) {
		final List<VesselCharter> vesselAvailabilities = new ArrayList<>();
		for (final String name : names) {
			vesselAvailabilities.add(buildVesselCharter(buildVessel(name)));
		}
		return vesselAvailabilities;
	}

	private Vessel buildVessel(final String name) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setName(name);
		return vessel;
	}

	private MinimalScenario createDefaultMinimalScenario() {
		final MinimalScenario minimalScenario = new MinimalScenario();
		final String[] vesselNames = new String[] { "Vessel 1", "Vessel 2", "Vessel 3" };
		final List<VesselCharter> vesselAvailabilities = buildVesselAvailabilities(vesselNames);
		minimalScenario.cargoModel = buildCargoModel(vesselAvailabilities);

		minimalScenario.spotMarketsModel = SpotMarketsFactory.eINSTANCE.createSpotMarketsModel();
		minimalScenario.scenarioModel = buildScenario(minimalScenario.cargoModel, minimalScenario.spotMarketsModel);

		final CargoValueProviderFactory cargoValueProviderFactory = new CargoValueProviderFactory();
		final SpotMarketsCharterValueProviderFactory spotMarketsVPFactory = new SpotMarketsCharterValueProviderFactory();

		final VesselAssignmentTypeValueProviderFactory vesselAssignmentTypeValueProviderFactory = new VesselAssignmentTypeValueProviderFactory(cargoValueProviderFactory, spotMarketsVPFactory);
		minimalScenario.referenceValueProvider = vesselAssignmentTypeValueProviderFactory.createReferenceValueProvider(CargoPackage.Literals.CARGO_MODEL,
				CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, minimalScenario.scenarioModel);

		minimalScenario.cargo = CargoFactory.eINSTANCE.createCargo();

		return minimalScenario;
	}

	private void changeVesselName(final LNGScenarioModel model, final int index, final String name) {
		final List<VesselCharter> vessels = model.getCargoModel().getVesselCharters();
		vessels.get(index).getVessel().setName(name);
	}

	private void removeVessel(final LNGScenarioModel model, final int index) {
		final List<VesselCharter> vessels = model.getCargoModel().getVesselCharters();
		vessels.remove(index);
	}

	private void addVesselCharter(final LNGScenarioModel model, final Vessel vessel) {
		final List<VesselCharter> vesselAvailabilities = model.getCargoModel().getVesselCharters();
		vesselAvailabilities.add(buildVesselCharter(vessel));
	}

	@Test
	public void testNoModificationToModel() {
		final MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test all vessels in value provider
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	@Test
	public void testRemoveVesselCharter() {
		final MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test removed vessel is picked up
		removeVessel(minimalScenario.scenarioModel, 0);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	@Test
	public void testAddVesselCharter() {
		final MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test removed vessel is picked up
		addVesselCharter(minimalScenario.scenarioModel, buildVessel("Vessel 4"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	@Test
	public void testChangeVesselName() {
		final MinimalScenario minimalScenario = createDefaultMinimalScenario();
		// test removed vessel is picked up
		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed");
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	@Test
	@Disabled("Vessel name changes do not pass up chain. Enable once BugzId: 1444 is fixed.")
	public void changeAll() {
		final MinimalScenario minimalScenario = createDefaultMinimalScenario();

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

		addVesselCharter(minimalScenario.scenarioModel, buildVessel("Vessel 4"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		changeVesselName(minimalScenario.scenarioModel, 0, "Vessel 1 Changed 3");
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

	}

	@Test
	public void changeAllExceptName() {
		final MinimalScenario minimalScenario = createDefaultMinimalScenario();

		// test all vessels in value provider
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// remove a vessel availability
		removeVessel(minimalScenario.scenarioModel, 0);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// add a vessel availability
		addVesselCharter(minimalScenario.scenarioModel, buildVessel("Vessel 4"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// add a null vessel availability
		addVesselCharter(minimalScenario.scenarioModel, null);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// add a vessel availability
		addVesselCharter(minimalScenario.scenarioModel, buildVessel("Vessel 5"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// add a vessel availability
		addVesselCharter(minimalScenario.scenarioModel, buildVessel("Vessel 6"));
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);

		// remove a vessel availability
		removeVessel(minimalScenario.scenarioModel, 2);
		testNamesInValueProvider(minimalScenario.cargoModel, minimalScenario.cargo, minimalScenario.referenceValueProvider);
	}

	private void testNamesInValueProvider(final CargoModel cargoModel, final Cargo cargo, final IReferenceValueProvider valueProvider) {
		final List<Pair<String, EObject>> t = valueProvider.getAllowedValues(cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
		for (final Pair<String, EObject> p : t) {
			Assertions.assertTrue(getNamesFromModel(cargoModel.getVesselCharters()).contains(p.getFirst()));
		}
	}

	private Set<String> getNamesFromModel(final EList<VesselCharter> objects) {
		final Set<String> s = new HashSet<String>();
		s.add("<Unassigned>");
		for (final VesselCharter object : objects) {
			final Vessel vessel = object.getVessel();
			if (vessel != null) {
				s.add(vessel.getName());
			}
		}
		return s;
	}

}
