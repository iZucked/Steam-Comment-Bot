/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.integration.distances.model.RoutingPoint;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.lng.exporters.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distances.PortAndDistancesToScenarioCopier;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.DistanceModelBuilder;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;

public class CopyDistancesToScenarioTests {

	@Test
	public void testCleanImport() throws Exception {
		final String input = DataLoader.importData("v1.0.11.250_4.json");

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();

		final DistancesVersion originalVersion = mapper.readerFor(DistancesVersion.class).readValue(input);
		prepareVersionModel(originalVersion);

		final String expectedResult = serialise(mapper, originalVersion);

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		final EditingDomain editingDomain = createEditingDomain(portModel);

		final Command updateCommand = PortAndDistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, originalVersion, true);
		editingDomain.getCommandStack().execute(updateCommand);

		final DistancesVersion derivedVersion = DistancesFromScenarioCopier.generateVersion(portModel);

		// To help ensure diff matches
		derivedVersion.setCreatedAt(originalVersion.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assert.assertEquals(expectedResult, derivedJSON);

	}

	@Test
	public void testReplaceExistingManualVersion() throws Exception {

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		final EditingDomain editingDomain = createEditingDomain(portModel);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
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
					Assert.assertFalse(route.getLines().isEmpty());
				}
			}
		}

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

		final String inputB = DataLoader.importData("v1.0.11.250_4.json");
		final DistancesVersion versionB = mapper.readerFor(DistancesVersion.class).readValue(inputB);
		prepareVersionModel(versionB);

		final Command updateCommand = PortAndDistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, versionB, true);
		editingDomain.getCommandStack().execute(updateCommand);
		Collections.sort(versionB.getLocations(), (a, b) -> a.getMmxId().compareTo(b.getMmxId()));
		final String expectedResult = serialise(mapper, versionB);
		final DistancesVersion derivedVersion = DistancesFromScenarioCopier.generateVersion(portModel);
		Collections.sort(derivedVersion.getLocations(), (a, b) -> a.getMmxId().compareTo(b.getMmxId()));
		// To help ensure diff matches
		derivedVersion.setCreatedAt(versionB.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assert.assertEquals(expectedResult, derivedJSON);

		// Verify canal distances cleared
		// Canal distances currently re-created during import
		for (Route route : portModel.getRoutes()) {
			if (route.getRouteOption() != RouteOption.DIRECT) {
				// Assert.assertTrue(route.getLines().isEmpty());
			}
		}
		Assert.assertNull(portToRemove.eContainer());
	}

	@Test
	public void testReplaceExistingJSONVersion() throws Exception {

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		final EditingDomain editingDomain = createEditingDomain(portModel);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// Load dataset 1 in
		{
			final String inputA = DataLoader.importData("v1.0.10.25_1.json");
			final DistancesVersion versionA = mapper.readerFor(DistancesVersion.class).readValue(inputA);
			final Command updateCommand = PortAndDistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, versionA, true);
			editingDomain.getCommandStack().execute(updateCommand);
		}

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

		final String inputB = DataLoader.importData("v1.0.11.250_4.json");
		final DistancesVersion versionB = mapper.readerFor(DistancesVersion.class).readValue(inputB);
		prepareVersionModel(versionB);

		final Command updateCommand = PortAndDistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, versionB, true);
		editingDomain.getCommandStack().execute(updateCommand);

		final String expectedResult = serialise(mapper, versionB);
		final DistancesVersion derivedVersion = DistancesFromScenarioCopier.generateVersion(portModel);

		// To help ensure diff matches
		derivedVersion.setCreatedAt(versionB.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assert.assertEquals(expectedResult, derivedJSON);
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
	//
	// @Test
	// public void testCopyDistances() {
	//
	// final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
	// final PortModelBuilder portBuilder = new PortModelBuilder(portModel);
	//
	// final Port a = portBuilder.createPort("portA", "a", "UTC", 0, 24);
	// final Port b = portBuilder.createPort("portB", "b", "UTC", 0, 24);
	// final Port c = portBuilder.createPort("portC", "c", "UTC", 0, 24);
	//
	// portBuilder.createRoute("Direct", RouteOption.DIRECT);
	// portBuilder.createRoute("Suez", RouteOption.SUEZ);
	// portBuilder.createRoute("Panama", RouteOption.PANAMA);
	//
	// final EditingDomain ed = createLocalEditingDomain(portModel);
	//
	// final @NonNull ILocationProvider lp = new DefaultPortProvider("1", portModel.getPorts());
	//
	// final @NonNull IDistanceProvider dp = Mockito.mock(IDistanceProvider.class);
	//
	// final Set<@NonNull String> knownLocations = CollectionsUtil.makeHashSet(a.getLocation().getMmxId(), b.getLocation().getMmxId(), c.getLocation().getMmxId());
	// when(dp.getKnownPorts()).thenReturn(knownLocations);
	//
	// when(dp.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	// when(dp.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(1.0);
	// when(dp.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(2.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(3.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(4.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(5.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(6.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	//
	// when(dp.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	// when(dp.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(7.0);
	// when(dp.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(8.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(9.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(10.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(11.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(12.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	//
	// final PortAndDistancesToScenarioCopier copier = new PortAndDistancesToScenarioCopier();
	// final Command updateDistancesCommand = copier.getUpdateCommand(ed, lp, dp, portModel);
	//
	// Assert.assertNotNull(updateDistancesCommand);
	// Assert.assertTrue(updateDistancesCommand.canExecute());
	// ed.getCommandStack().execute(updateDistancesCommand);
	//
	// Assert.assertEquals(6, portModel.getRoutes().get(0).getLines().size()); // i.e. no RouteLine for identity routes
	// Assert.assertEquals(6, portModel.getRoutes().get(1).getLines().size());
	//
	// final Optional<RouteLine> potential = portModel.getRoutes().get(1).getLines().stream().filter(e -> {
	// return Objects.equals(e.getFrom().getLocation().getMmxId(), a.getLocation().getMmxId()) && Objects.equals(e.getTo().getLocation().getMmxId(), b.getLocation().getMmxId());
	// }).findFirst();
	// Assert.assertTrue(potential.isPresent());
	// Assert.assertEquals(7, potential.get().getDistance());
	// }
	//
	// @Test
	// public void lostRouteLinesAfterUpdateTest() {
	//
	// final List<Port> ports = new ArrayList<>();
	// final Port a = Mockito.mock(PortImpl.class);
	// final Location l_a = Mockito.mock(Location.class);
	//
	// Mockito.when(a.getLocation()).thenReturn(l_a);
	// when(a.getName()).thenReturn("a");
	// when(l_a.getMmxId()).thenReturn("a");
	// ports.add(a);
	//
	// final Port b = Mockito.mock(PortImpl.class);
	// final Location l_b = Mockito.mock(Location.class);
	// Mockito.when(b.getLocation()).thenReturn(l_b);
	// when(b.getName()).thenReturn("b");
	// when(l_b.getMmxId()).thenReturn("b");
	// ports.add(b);
	//
	// final Port c = Mockito.mock(PortImpl.class);
	// final Location l_c = Mockito.mock(Location.class);
	// Mockito.when(c.getLocation()).thenReturn(l_c);
	// when(c.getName()).thenReturn("c");
	// when(l_c.getMmxId()).thenReturn("c");
	// ports.add(c);
	//
	// final List<Route> routes = new ArrayList<>();
	// final Route rd = PortFactory.eINSTANCE.createRoute();
	// rd.setRouteOption(RouteOption.DIRECT);
	// routes.add(rd);
	//
	// final Route rp = PortFactory.eINSTANCE.createRoute();
	// rp.setRouteOption(RouteOption.PANAMA);
	// routes.add(rp);
	//
	// final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
	// final EditingDomain ed = createLocalEditingDomain(portModel);
	//
	// portModel.getPorts().addAll(ports);
	// portModel.getRoutes().addAll(routes);
	//
	// final IDistanceProvider dp = Mockito.mock(IDistanceProvider.class);
	// final Set<@NonNull String> knownLocations = CollectionsUtil.makeHashSet(a.getLocation().getMmxId(), b.getLocation().getMmxId(), c.getLocation().getMmxId());
	// when(dp.getKnownPorts()).thenReturn(knownLocations);
	//
	// when(dp.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	// when(dp.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(1.0);
	// when(dp.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(2.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(3.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(4.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(5.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(6.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	//
	// when(dp.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	// when(dp.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(7.0);
	// when(dp.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(8.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(9.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	// when(dp.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(10.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(11.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(12.0);
	// when(dp.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	//
	// final PortAndDistancesToScenarioCopier copier = new PortAndDistancesToScenarioCopier();
	// final Pair<Command, Map<RouteOption, List<RouteLine>>> updateDistancesCommand = null;// copier.getUpdateDistancesCommand(ed, dp, portModel);
	// updateDistancesCommand.getFirst().execute();
	//
	// final IDistanceProvider dp2 = Mockito.mock(IDistanceProvider.class);
	// when(dp2.getKnownPorts()).thenReturn(knownLocations);
	//
	// when(dp2.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	// when(dp2.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(13.0);
	// when(dp2.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(14.0);
	// when(dp2.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(15.0);
	// when(dp2.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	// when(dp2.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(16.0);
	// when(dp2.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(Double.MAX_VALUE);
	// when(dp2.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(18.0);
	// when(dp2.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(0.0);
	//
	// when(dp2.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	// when(dp2.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(19.0);
	// when(dp2.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(20.0);
	// when(dp2.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(21.0);
	// when(dp2.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	// when(dp2.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(22.0);
	// when(dp2.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(23.0);
	// when(dp2.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(Double.MAX_VALUE);
	// when(dp2.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0.0);
	//
	// final Pair<Command, Map<RouteOption, List<RouteLine>>> updateDistancesIncomplete = null;// copier.getUpdateDistancesCommand(ed, dp2, portModel);
	// Assert.assertNotNull(updateDistancesIncomplete.getFirst());
	// final boolean canExecute = updateDistancesIncomplete.getFirst().canExecute();
	// Assert.assertTrue(canExecute);
	// Assert.assertEquals(1, updateDistancesIncomplete.getSecond().get(RouteOption.DIRECT).size());
	// Assert.assertEquals(5, updateDistancesIncomplete.getSecond().get(RouteOption.DIRECT).get(0).getDistance());
	//
	// Assert.assertEquals(12, updateDistancesIncomplete.getSecond().get(RouteOption.PANAMA).get(0).getDistance());
	// updateDistancesIncomplete.getFirst().execute();
	// }

	// @NonNull
	// public static EditingDomain createLocalEditingDomain(final EObject rootObject) {
	// final BasicCommandStack commandStack = new BasicCommandStack();
	// final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	// adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	// adapterFactory.addAdapterFactory(new PortItemProviderAdapterFactory());
	// final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, commandStack);
	// final ResourceImpl r = new ResourceImpl();
	//
	// ed.getResourceSet().getResources().add(r);
	// r.getContents().add(rootObject);
	// return ed;
	// }

	private String serialise(final ObjectMapper mapper, final DistancesVersion version) throws IOException, JsonGenerationException, JsonMappingException {
		final StringWriter writer = new StringWriter();
		mapper.writerWithDefaultPrettyPrinter().writeValue(writer, version);
		final String result = writer.toString();
		return result;
	}

}
