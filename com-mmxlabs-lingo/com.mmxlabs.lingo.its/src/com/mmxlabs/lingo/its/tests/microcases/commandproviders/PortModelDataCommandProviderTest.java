/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.commandproviders;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

@ExtendWith(value = ShiroRunner.class)
public class PortModelDataCommandProviderTest extends AbstractMicroTestCase {
	enum ExpectedChange {
		NONE, DISTANCE, LOCATION, GROUPS
	}

	class VersionData {
		String distance;
		String location;
		String portGroup;

		public VersionData(final PortModel m) {
			distance = m.getDistanceVersionRecord().getVersion();
			location = m.getPortVersionRecord().getVersion();
			portGroup = m.getPortGroupVersionRecord().getVersion();

			Assertions.assertNotNull(distance);
			Assertions.assertNotNull(location);
			Assertions.assertNotNull(portGroup);
		}
	}

	protected void checkAndUpdate(final PortModel portModel, final VersionData currentVersion, final ExpectedChange... changeTypes) {
		// None is just to make the API call easier.
		final Set<ExpectedChange> changeTypesSet = EnumSet.of(ExpectedChange.NONE, changeTypes);
		if (changeTypesSet.contains(ExpectedChange.DISTANCE)) {
			Assertions.assertNotEquals(currentVersion.distance, portModel.getDistanceVersionRecord().getVersion());
			currentVersion.distance = portModel.getDistanceVersionRecord().getVersion();
		}
		if (changeTypesSet.contains(ExpectedChange.LOCATION)) {
			Assertions.assertNotEquals(currentVersion.location, portModel.getPortVersionRecord().getVersion());
			currentVersion.location = portModel.getPortVersionRecord().getVersion();
		}
		if (changeTypesSet.contains(ExpectedChange.GROUPS)) {
			Assertions.assertNotEquals(currentVersion.portGroup, portModel.getPortGroupVersionRecord().getVersion());
			currentVersion.portGroup = portModel.getPortGroupVersionRecord().getVersion();
		}

		// Lazy duplicated check here
		Assertions.assertEquals(currentVersion.distance, portModel.getDistanceVersionRecord().getVersion());
		Assertions.assertEquals(currentVersion.location, portModel.getPortVersionRecord().getVersion());
		Assertions.assertEquals(currentVersion.portGroup, portModel.getPortGroupVersionRecord().getVersion());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeLocationData() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final VersionData currentVersion = new VersionData(portModel);

		final Port port = PortFactory.eINSTANCE.createPort();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, port)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE, ExpectedChange.LOCATION);

		final Location location = PortFactory.eINSTANCE.createLocation();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__LOCATION, location)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, PortPackage.Literals.LOCATION__COUNTRY, "country")));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name")));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, location, MMXCorePackage.Literals.OTHER_NAMES_OBJECT__OTHER_NAMES, Collections.singletonList("name"))));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, PortPackage.Literals.LOCATION__LAT, 1.0)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, PortPackage.Literals.LOCATION__LON, 1.0)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, PortPackage.Literals.LOCATION__TIME_ZONE, "timezone")));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, location)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, port)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE, ExpectedChange.LOCATION);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeRouteData() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final VersionData currentVersion = new VersionData(portModel);

		final Route route = PortFactory.eINSTANCE.createRoute();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__ROUTES, route)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__DISTANCE, 5.0)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__ROUTE_OPTION, RouteOption.SUEZ)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		final EntryPoint north = PortFactory.eINSTANCE.createEntryPoint();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__NORTH_ENTRANCE, north)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		final EntryPoint south = PortFactory.eINSTANCE.createEntryPoint();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__SOUTH_ENTRANCE, south)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		final Port northPort = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, north, PortPackage.Literals.ENTRY_POINT__PORT, northPort)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		final RouteLine line = PortFactory.eINSTANCE.createRouteLine();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, route, PortPackage.Literals.ROUTE__LINES, line)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		final Port fromPort = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__FROM, fromPort)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		final Port toPort = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__FROM, toPort)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__DISTANCE, 5.0)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__PROVIDER, "abc")));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__ERROR_CODE, "value")));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, line)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, south)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, north)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, route)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangePortData() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final VersionData currentVersion = new VersionData(portModel);

		final Port port = PortFactory.eINSTANCE.createPort();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, port)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION, ExpectedChange.DISTANCE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__ALLOW_COOLDOWN, Boolean.TRUE)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__BERTHS, 2)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, port, PortPackage.Literals.PORT__CAPABILITIES, PortCapability.LOAD)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__CV_VALUE, 12.0)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__DEFAULT_START_TIME, 2)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE, 2)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE_UNITS, TimePeriod.MONTHS)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__DISCHARGE_DURATION, 2)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__LOAD_DURATION, 2)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__MAX_CV_VALUE, 4.0)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__MIN_CV_VALUE, 4.0)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__SHORT_NAME, "abc")));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.LOCATION);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeGroups() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final VersionData currentVersion = new VersionData(portModel);

		final PortGroup portGroup = PortFactory.eINSTANCE.createPortGroup();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORT_GROUPS, portGroup)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.GROUPS);

		Port p = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, p)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE, ExpectedChange.LOCATION);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portGroup, PortPackage.Literals.PORT_GROUP__CONTENTS, p)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.GROUPS);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, portGroup, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "group")));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.GROUPS);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, p)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.DISTANCE, ExpectedChange.LOCATION, ExpectedChange.GROUPS);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, portGroup)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.GROUPS);

		final PortCountryGroup portCountryGroup = PortFactory.eINSTANCE.createPortCountryGroup();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS, portCountryGroup)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, portCountryGroup)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeOtherData_ContingencyMatrix() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final VersionData currentVersion = new VersionData(portModel);

		final ContingencyMatrix matrix = PortFactory.eINSTANCE.createContingencyMatrix();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__CONTINGENCY_MATRIX, matrix)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, matrix, PortPackage.Literals.CONTINGENCY_MATRIX__DEFAULT_DURATION, 5)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);

		final ContingencyMatrixEntry entry = PortFactory.eINSTANCE.createContingencyMatrixEntry();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, matrix, PortPackage.Literals.CONTINGENCY_MATRIX__ENTRIES, entry)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, entry, PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY__DURATION, 5)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);

		final Port p1 = PortFactory.eINSTANCE.createPort();
		final Port p2 = PortFactory.eINSTANCE.createPort();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, entry, PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY__FROM_PORT, p1)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, entry, PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY__TO_PORT, p2)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, entry)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, matrix)));
		checkAndUpdate(portModel, currentVersion, ExpectedChange.NONE);
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