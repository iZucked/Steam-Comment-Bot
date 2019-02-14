/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.commandproviders;

import java.util.Collections;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.OtherIdentifiers;
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

@RunWith(value = ShiroRunner.class)
public class PortDataCommandProviderTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeOtherPortData() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		String currentVersion = portModel.getPortDataVersion();
		Assert.assertNotNull(currentVersion);

		Port port = PortFactory.eINSTANCE.createPort();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, port)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		Location location = PortFactory.eINSTANCE.createLocation();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__LOCATION, location)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, PortPackage.Literals.LOCATION__COUNTRY, "country")));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name")));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, location, MMXCorePackage.Literals.OTHER_NAMES_OBJECT__OTHER_NAMES, Collections.singletonList("name"))));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, PortPackage.Literals.LOCATION__LAT, 1.0)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, PortPackage.Literals.LOCATION__LON, 1.0)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, location, PortPackage.Literals.LOCATION__TIME_ZONE, "timezone")));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		OtherIdentifiers otherID = PortFactory.eINSTANCE.createOtherIdentifiers();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, location, PortPackage.Literals.LOCATION__OTHER_IDENTIFIERS, otherID)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, otherID, PortPackage.Literals.OTHER_IDENTIFIERS__IDENTIFIER, "value")));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, otherID, PortPackage.Literals.OTHER_IDENTIFIERS__PROVIDER, "value")));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, otherID)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, location)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, port)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeRouteData() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		String currentVersion = portModel.getPortDataVersion();
		Assert.assertNotNull(currentVersion);

		Route route = PortFactory.eINSTANCE.createRoute();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__ROUTES, route)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__DISTANCE, 5.0)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__ROUTE_OPTION, RouteOption.SUEZ)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		Port virtualPort = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__VIRTUAL_PORT, virtualPort)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		EntryPoint north = PortFactory.eINSTANCE.createEntryPoint();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__NORTH_ENTRANCE, north)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		EntryPoint south = PortFactory.eINSTANCE.createEntryPoint();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, route, PortPackage.Literals.ROUTE__SOUTH_ENTRANCE, south)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		Port northPort = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, north, PortPackage.Literals.ENTRY_POINT__PORT, northPort)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RouteLine line = PortFactory.eINSTANCE.createRouteLine();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, route, PortPackage.Literals.ROUTE__LINES, line)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		Port fromPort = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__FROM, fromPort)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		Port toPort = PortFactory.eINSTANCE.createPort();
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__FROM, toPort)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__DISTANCE, 5.0)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__PROVIDER, "abc")));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, line, PortPackage.Literals.ROUTE_LINE__ERROR_CODE, "value")));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, line)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, south)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, north)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, route)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangePortData() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		String currentVersion = portModel.getPortDataVersion();
		Assert.assertNotNull(currentVersion);

		Port port = PortFactory.eINSTANCE.createPort();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, port)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__ALLOW_COOLDOWN, Boolean.TRUE)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__BERTHS, 2)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, port, PortPackage.Literals.PORT__CAPABILITIES, PortCapability.LOAD)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__CV_VALUE, 12.0)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__DEFAULT_START_TIME, 2)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE, 2)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE_UNITS, TimePeriod.MONTHS)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__DISCHARGE_DURATION, 2)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__LOAD_DURATION, 2)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__MAX_CV_VALUE, 4.0)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__MIN_CV_VALUE, 4.0)));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, port, PortPackage.Literals.PORT__SHORT_NAME, "abc")));
		Assert.assertNotEquals(currentVersion, portModel.getPortDataVersion());
		currentVersion = portModel.getPortDataVersion();

	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeOtherData_Groups() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		String currentVersion = portModel.getPortDataVersion();
		Assert.assertNotNull(currentVersion);

		PortGroup portGroup = PortFactory.eINSTANCE.createPortGroup();
		PortCountryGroup portCountryGroup = PortFactory.eINSTANCE.createPortCountryGroup();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORT_GROUPS, portGroup)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, portGroup)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS, portCountryGroup)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, portCountryGroup)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeOtherData_ContingencyMatrix() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		String currentVersion = portModel.getPortDataVersion();
		Assert.assertNotNull(currentVersion);

		ContingencyMatrix matrix = PortFactory.eINSTANCE.createContingencyMatrix();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, portModel, PortPackage.Literals.PORT_MODEL__CONTINGENCY_MATRIX, matrix)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, matrix, PortPackage.Literals.CONTINGENCY_MATRIX__DEFAULT_DURATION, 5)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		ContingencyMatrixEntry entry = PortFactory.eINSTANCE.createContingencyMatrixEntry();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, matrix, PortPackage.Literals.CONTINGENCY_MATRIX__ENTRIES, entry)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, entry, PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY__DURATION, 5)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		Port p1 = PortFactory.eINSTANCE.createPort();
		Port p2 = PortFactory.eINSTANCE.createPort();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, entry, PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY__FROM_PORT, p1)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, entry, PortPackage.Literals.CONTINGENCY_MATRIX_ENTRY__TO_PORT, p2)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, entry)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, matrix)));
		Assert.assertEquals(currentVersion, portModel.getPortDataVersion());
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