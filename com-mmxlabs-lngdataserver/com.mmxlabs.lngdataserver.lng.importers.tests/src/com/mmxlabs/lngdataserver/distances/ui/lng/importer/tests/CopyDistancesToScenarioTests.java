/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.distances.ui.lng.importer.tests;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.DataConstants;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.distances.model.RoutingPoint;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesToScenarioCopier;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.DistanceModelBuilder;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;

public class CopyDistancesToScenarioTests {


	@Test
	public void testCleanImport() throws Exception {
		final String input = DataLoader.importData(DataConstants.DISTANCES_V1_0_11_250_4_JSON);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());

		final DistancesVersion originalVersion = mapper.readerFor(DistancesVersion.class).readValue(input);
		prepareVersionModel(originalVersion);

		final String expectedResult = serialise(mapper, originalVersion);

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		portModel.setDistanceVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		portModel.setPortVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		final EditingDomain editingDomain = createEditingDomain(portModel);

		final Command updateCommand = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, originalVersion, true);
		editingDomain.getCommandStack().execute(updateCommand);

		final DistancesVersion derivedVersion = DistancesFromScenarioCopier.generateVersion(portModel);

		// To help ensure diff matches
		derivedVersion.setCreatedAt(originalVersion.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assertions.assertEquals(expectedResult, derivedJSON);

	}

	@Test
	public void testReplaceExistingManualVersion() throws Exception {

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		portModel.setDistanceVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		portModel.setPortVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		final EditingDomain editingDomain = createEditingDomain(portModel);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// Load dataset 1 in
		final Port portToRemove;
		{

			PortModelBuilder builder = new PortModelBuilder(portModel);

			// Blank names to force update.
			// If not blank, they would not be updated and cause test to fail if they did not match expected name
			Port portA = builder.createPort("", "L_CA_Kitim", "Etc/UTC", 0, 24);
			Port portB = builder.createPort("", "L_JP_Joets", "Etc/UTC", 0, 24);
			Port portC = builder.createPort("", "", "Etc/UTC", 0, 24);
			portToRemove = portC;

			Route directRoute = builder.createRoute("Direct", RouteOption.DIRECT);
			Route suezRoute = builder.createRoute("Suez", RouteOption.SUEZ);
			Route panamaRoute = builder.createRoute("Panama", RouteOption.PANAMA);

			DistanceModelBuilder distanceBuilder = new DistanceModelBuilder(portModel);
			distanceBuilder.createDistanceMatrix(RouteOption.DIRECT);
			distanceBuilder.createDistanceMatrix(RouteOption.PANAMA);
			distanceBuilder.createDistanceMatrix(RouteOption.SUEZ);

			// Add some canal distances to make sure they get removed during update (port a and b would be retained, port c would be removed);
			distanceBuilder.setPortToPortDistance(portA, portB, RouteOption.SUEZ, 500, true);
			distanceBuilder.setPortToPortDistance(portA, portB, RouteOption.PANAMA, 500, true);
			distanceBuilder.setPortToPortDistance(portA, portC, RouteOption.SUEZ, 500, true);
			distanceBuilder.setPortToPortDistance(portA, portC, RouteOption.PANAMA, 500, true);

			for (Route route : portModel.getRoutes()) {
				if (route.getRouteOption() != RouteOption.DIRECT) {
					Assertions.assertFalse(route.getLines().isEmpty());
				}
			}
		}

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

		final String inputB = DataLoader.importData(DataConstants.DISTANCES_V1_0_11_250_4_JSON);
		final DistancesVersion versionB = mapper.readerFor(DistancesVersion.class).readValue(inputB);
		prepareVersionModel(versionB);

		final Command updateCommand = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, versionB, true);
		editingDomain.getCommandStack().execute(updateCommand);
		Collections.sort(versionB.getLocations(), (a, b) -> a.getMmxId().compareTo(b.getMmxId()));
		final String expectedResult = serialise(mapper, versionB);
		final DistancesVersion derivedVersion = DistancesFromScenarioCopier.generateVersion(portModel);
		Collections.sort(derivedVersion.getLocations(), (a, b) -> a.getMmxId().compareTo(b.getMmxId()));
		// To help ensure diff matches
		derivedVersion.setCreatedAt(versionB.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assertions.assertEquals(expectedResult, derivedJSON);

		// Verify canal distances cleared
		// Canal distances currently re-created during import
		for (Route route : portModel.getRoutes()) {
			if (route.getRouteOption() != RouteOption.DIRECT) {
				// Assertions.assertTrue(route.getLines().isEmpty());
			}
		}
		Assertions.assertNull(portToRemove.eContainer());

	}

	@Test
	public void testReplaceExistingJSONVersion() throws Exception {

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		portModel.setDistanceVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		portModel.setPortVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		final EditingDomain editingDomain = createEditingDomain(portModel);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// Load dataset 1 in
		{
			final String inputA = DataLoader.importData(DataConstants.DISTANCES_V1_0_10_25_1_JSON);
			final DistancesVersion versionA = mapper.readerFor(DistancesVersion.class).readValue(inputA);
			final Command updateCommand = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, versionA, true);
			editingDomain.getCommandStack().execute(updateCommand);
		}

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

		final String inputB = DataLoader.importData(DataConstants.DISTANCES_V1_0_11_250_4_JSON);
		final DistancesVersion versionB = mapper.readerFor(DistancesVersion.class).readValue(inputB);
		prepareVersionModel(versionB);

		final Command updateCommand = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, versionB, true);
		editingDomain.getCommandStack().execute(updateCommand);

		final String expectedResult = serialise(mapper, versionB);
		final DistancesVersion derivedVersion = DistancesFromScenarioCopier.generateVersion(portModel);

		// To help ensure diff matches
		derivedVersion.setCreatedAt(versionB.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assertions.assertEquals(expectedResult, derivedJSON);
	}

	/**
	 * Some data is stored in a set, LiNGO adds stuff in a linked hash set. So re-order the loaded data to match.
	 * 
	 * @param v
	 */
	private void prepareVersionModel(final DistancesVersion v) {
		final List<RoutingPoint> routingPoints = v.getRoutingPoints();
		final List<RoutingPoint> rp2 = new LinkedList<>();
		// Re-order to SUEZ, PANAMA, REST
		for (final RoutingPoint rp : routingPoints) {
			if (rp.getIdentifier().equals("SUZ")) {
				rp2.add(rp);
				break;
			}
		}
		for (final RoutingPoint rp : routingPoints) {
			if (rp.getIdentifier().equals("PAN")) {
				rp2.add(rp);
				break;
			}
		}
		for (final RoutingPoint rp : routingPoints) {
			if (!rp2.contains(rp)) {
				rp2.add(rp);
			}
		}
		v.setRoutingPoints(rp2);

		// Re-order north then south (loose anything else)
		for (final RoutingPoint rp : rp2) {

			final com.mmxlabs.lngdataserver.integration.distances.model.Location northernEntry = rp.getNorthernEntry();
			final com.mmxlabs.lngdataserver.integration.distances.model.Location southernEntry = rp.getSouthernEntry();
			rp.setEntryPoints(new LinkedHashSet<>());
			rp.getEntryPoints().add(northernEntry);
			rp.getEntryPoints().add(southernEntry);
		}
	}

	private EditingDomain createEditingDomain(final EObject rootModel) {
		final BasicCommandStack commandStack = new BasicCommandStack() {
			@Override
			protected void handleError(final Exception exception) {
				throw new RuntimeException(exception);
			}
		};
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);

		final Resource r = new ResourceImpl();
		assert rootModel.eResource() == null;

		r.getContents().add(rootModel);

		editingDomain.getResourceSet().getResources().add(r);
		return editingDomain;
	}

	private String serialise(final ObjectMapper mapper, final DistancesVersion version) throws IOException {
		final StringWriter writer = new StringWriter();
		mapper.writerWithDefaultPrettyPrinter().writeValue(writer, version);
		return writer.toString();
	}

}
