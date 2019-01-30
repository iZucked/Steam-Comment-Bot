/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.commandproviders;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

@RunWith(value = ShiroRunner.class)
public class FleetModelDataCommandProviderTest extends AbstractMicroTestCase {
	enum ExpectedChange {
		NONE, FLEET, BUNKERS, GROUPS
	}

	class VersionData {
		String fleet;
		String bunkers;
		String groups;

		public VersionData(final FleetModel m) {
			fleet = m.getFleetVersionRecord().getVersion();
			bunkers = m.getBunkerFuelsVersionRecord().getVersion();
			groups = m.getVesselGroupVersionRecord().getVersion();

			Assert.assertNotNull(fleet);
			Assert.assertNotNull(bunkers);
			Assert.assertNotNull(groups);
		}
	}

	protected void checkAndUpdate(final FleetModel fleetModel, final VersionData currentVersion, final ExpectedChange... changeTypes) {
		// None is just to make the API call easier.
		final Set<ExpectedChange> changeTypesSet = EnumSet.of(ExpectedChange.NONE, changeTypes);
		if (changeTypesSet.contains(ExpectedChange.FLEET)) {
			Assert.assertNotEquals(currentVersion.fleet, fleetModel.getFleetVersionRecord().getVersion());
			currentVersion.fleet = fleetModel.getFleetVersionRecord().getVersion();
		}
		if (changeTypesSet.contains(ExpectedChange.BUNKERS)) {
			Assert.assertNotEquals(currentVersion.bunkers, fleetModel.getBunkerFuelsVersionRecord().getVersion());
			currentVersion.bunkers = fleetModel.getBunkerFuelsVersionRecord().getVersion();
		}
		if (changeTypesSet.contains(ExpectedChange.GROUPS)) {
			Assert.assertNotEquals(currentVersion.groups, fleetModel.getVesselGroupVersionRecord());
			currentVersion.groups = fleetModel.getVesselGroupVersionRecord().getVersion();
		}

		// Lazy duplicated check here
		Assert.assertEquals(currentVersion.fleet, fleetModel.getFleetVersionRecord().getVersion());
		Assert.assertEquals(currentVersion.bunkers, fleetModel.getBunkerFuelsVersionRecord().getVersion());
		Assert.assertEquals(currentVersion.groups, fleetModel.getVesselGroupVersionRecord().getVersion());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeVesselData() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioDataProvider);
		final VersionData currentVersion = new VersionData(fleetModel);

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, vessel)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "vessel")));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__CAPACITY, 100)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__COOLING_VOLUME, 100)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__FILL_CAPACITY, 1.0)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY, Boolean.TRUE)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE, Boolean.TRUE)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__IMO, "123")));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__MAX_SPEED, 5.0)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__MIN_SPEED, 5.0)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__MIN_BASE_FUEL_CONSUMPTION, 5.0)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__MMX_ID, "abc")));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__NOTES, "abc")));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__PILOT_LIGHT_RATE, 1.0)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__SAFETY_HEEL, 1)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__SCNT, 1)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__SHORT_NAME, "abc")));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__TYPE, "abc")));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__WARMING_TIME, 1)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		{
			final BaseFuel fuel = FleetFactory.eINSTANCE.createBaseFuel();
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__BASE_FUELS, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.BUNKERS);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__BASE_FUEL, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET, ExpectedChange.BUNKERS);

		}
		{
			final BaseFuel fuel = FleetFactory.eINSTANCE.createBaseFuel();
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__BASE_FUELS, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.BUNKERS);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__IDLE_BASE_FUEL, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET, ExpectedChange.BUNKERS);

		}
		{
			final BaseFuel fuel = FleetFactory.eINSTANCE.createBaseFuel();
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__BASE_FUELS, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.BUNKERS);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__PILOT_LIGHT_BASE_FUEL, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET, ExpectedChange.BUNKERS);

		}
		{
			final BaseFuel fuel = FleetFactory.eINSTANCE.createBaseFuel();
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__BASE_FUELS, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.BUNKERS);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__IN_PORT_BASE_FUEL, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, fuel)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET, ExpectedChange.BUNKERS);
		}

		{
			final VesselClassRouteParameters param = FleetFactory.eINSTANCE.createVesselClassRouteParameters();
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS, param)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, param, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, param, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_NBO_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, param, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME, 1)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, param, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, param, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_NBO_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, param, FleetPackage.Literals.VESSEL_CLASS_ROUTE_PARAMETERS__ROUTE_OPTION, RouteOption.SUEZ)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, param)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE, Boolean.TRUE)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		}

		{
			final VesselStateAttributes attributes = FleetFactory.eINSTANCE.createVesselStateAttributes();
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__LADEN_ATTRIBUTES, attributes)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper
					.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, Boolean.TRUE)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			final FuelConsumption fc = FleetFactory.eINSTANCE.createFuelConsumption();
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION, fc)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, fc, FleetPackage.Literals.FUEL_CONSUMPTION__CONSUMPTION, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, fc, FleetPackage.Literals.FUEL_CONSUMPTION__SPEED, 1.0)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, fc)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__LADEN_ATTRIBUTES, SetCommand.UNSET_VALUE)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__BALLAST_ATTRIBUTES, attributes)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
			RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, attributes)));
			checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		}

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES, RouteOption.SUEZ)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(RemoveCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES, RouteOption.SUEZ)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE, Boolean.TRUE)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final Port p1 = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, p1)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.NONE);
		final Port p2 = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, p2)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, p1)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, p2)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(RemoveCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, p1)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, p2)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE, Boolean.TRUE)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		final Vessel ref = FleetFactory.eINSTANCE.createVessel();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, ref)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vessel, FleetPackage.Literals.VESSEL__REFERENCE, ref)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, ref)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, vessel)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeVesselGroups() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioDataProvider);
		final VersionData currentVersion = new VersionData(fleetModel);

		final VesselGroup vesselGroup = FleetFactory.eINSTANCE.createVesselGroup();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSEL_GROUPS, vesselGroup)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.GROUPS);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, vesselGroup, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "group")));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.GROUPS);

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, vessel)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, vesselGroup, FleetPackage.Literals.VESSEL_GROUP__VESSELS, vessel)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.GROUPS);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, vessel)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.GROUPS, ExpectedChange.FLEET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, vesselGroup)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.GROUPS);

	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeBunkerFuels() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioDataProvider);
		final VersionData currentVersion = new VersionData(fleetModel);
		Assert.assertNotNull(currentVersion);

		final BaseFuel fuel = FleetFactory.eINSTANCE.createBaseFuel();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, fleetModel, FleetPackage.Literals.FLEET_MODEL__BASE_FUELS, fuel)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.BUNKERS);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, fuel, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "fuel")));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.BUNKERS);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, fuel, FleetPackage.Literals.BASE_FUEL__EQUIVALENCE_FACTOR, 5.0)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.BUNKERS);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, fuel)));
		checkAndUpdate(fleetModel, currentVersion, ExpectedChange.BUNKERS);

	}

	private EditingDomain createEditingDomain(final LNGScenarioModel scenarioModel) {

		// Create the editing domain with a special command stack.
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource r = new ResourceImpl();
		r.getContents().add(scenarioModel);
		resourceSet.getResources().add(r);

		return ScenarioStorageUtil.initEditingDomain(null, resourceSet, scenarioModel).getFirst();
	}

}