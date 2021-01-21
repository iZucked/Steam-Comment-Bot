/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.distances.ui.lng.importer.tests;

import java.io.IOException;
import java.io.StringWriter;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.PortsToScenarioCopier;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;

public class CopyPortsToScenarioTests {

	@Test
	public void testCleanImport() throws Exception {
		final String input = DataLoader.importData("ports-testdata2.json");

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final PortsVersion originalVersion = mapper.readerFor(PortsVersion.class).readValue(input);
		prepareVersionModel(originalVersion);

		final String expectedResult = serialise(mapper, originalVersion);

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		portModel.setDistanceVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		portModel.setPortVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		final EditingDomain editingDomain = createEditingDomain(portModel);
		{
			final String distInput = DataLoader.importData("v1.0.11.250_7.json");
			final DistancesVersion versionA = mapper.readerFor(DistancesVersion.class).readValue(distInput);
			final Command updateCommand = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, versionA, true);
			editingDomain.getCommandStack().execute(updateCommand);
		}
		final Command updateCommand = PortsToScenarioCopier.getUpdateCommand(editingDomain, portModel, originalVersion);
		editingDomain.getCommandStack().execute(updateCommand);

		final PortsVersion derivedVersion = PortFromScenarioCopier.generateVersion(portModel);
		prepareVersionModel(derivedVersion);
		// To help ensure diff matches
		derivedVersion.setCreatedAt(originalVersion.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assertions.assertEquals(expectedResult, derivedJSON);

	}

	@Test
	public void testUpdateImport() throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		portModel.setDistanceVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		portModel.setPortVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		final EditingDomain editingDomain = createEditingDomain(portModel);

		// Load in version 1 data
		{
			{
				final String distInput = DataLoader.importData("v1.0.11.250_7.json");
				final DistancesVersion versionA = mapper.readerFor(DistancesVersion.class).readValue(distInput);
				final Command updateCommand = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, versionA, true);
				editingDomain.getCommandStack().execute(updateCommand);
			}
			{
				final String input = DataLoader.importData("ports-testdata1.json");
				final PortsVersion originalVersion = mapper.readerFor(PortsVersion.class).readValue(input);
				prepareVersionModel(originalVersion);
				final Command updateCommand = PortsToScenarioCopier.getUpdateCommand(editingDomain, portModel, originalVersion);
				editingDomain.getCommandStack().execute(updateCommand);
			}
		}

		// Now load in version 2
		final String input = DataLoader.importData("ports-testdata2.json");
		final PortsVersion originalVersion = mapper.readerFor(PortsVersion.class).readValue(input);
		prepareVersionModel(originalVersion);
		final String expectedResult = serialise(mapper, originalVersion);

		final Command updateCommand = PortsToScenarioCopier.getUpdateCommand(editingDomain, portModel, originalVersion);
		editingDomain.getCommandStack().execute(updateCommand);

		final PortsVersion derivedVersion = PortFromScenarioCopier.generateVersion(portModel);
		prepareVersionModel(derivedVersion);
		// To help ensure diff matches
		derivedVersion.setCreatedAt(originalVersion.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assertions.assertEquals(expectedResult, derivedJSON);

	}

	/**
	 * Some data is stored in a set, LiNGO adds stuff in a linked hash set. So re-order the loaded data to match.
	 * 
	 * @param v
	 */
	private void prepareVersionModel(final PortsVersion v) {
		List<com.mmxlabs.lngdataserver.integration.ports.model.Port> l = new LinkedList<>(v.getPorts());
		l.sort((a, b) -> a.getLocationMmxId().compareTo(b.getLocationMmxId()));
		v.setPorts(l);
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

	private String serialise(final ObjectMapper mapper, final PortsVersion version) throws IOException, JsonGenerationException, JsonMappingException {
		final StringWriter writer = new StringWriter();
		mapper.writerWithDefaultPrettyPrinter().writeValue(writer, version);
		final String result = writer.toString();
		return result;
	}

}
