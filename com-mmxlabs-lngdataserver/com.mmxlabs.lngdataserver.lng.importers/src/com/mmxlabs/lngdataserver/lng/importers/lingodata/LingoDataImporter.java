/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsVersion;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupsVersion;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.model.DataManifest;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.model.Entry;
import com.mmxlabs.lngdataserver.lng.io.bunkerfuels.BunkerFuelsToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.PortsToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.portgroups.PortGroupsToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.vesselgroups.VesselGroupsToScenarioImporter;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class LingoDataImporter {

	public void importIntoScenario(final String filename, final ScenarioInstance scenarioInstance) throws IOException {
		final String baseURI = "archive:" + URI.createFileURI(filename).toString() + "!";

		final ObjectMapper mapper = new ObjectMapper();
		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

		DataManifest manifest;
		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/manifest.json"))) {
			manifest = mapper.readValue(inputStream, DataManifest.class);
		}

		Entry distanceEntry = null;
		Entry portEntry = null;
		Entry portGroupsEntry = null;
		Entry pricingEntry = null;
		Entry vesselsEntry = null;
		Entry vesselGroupsEntry = null;
		Entry bunkerFuelsEntry = null;
		for (final Entry entry : manifest.getEntries()) {
			switch (entry.getType()) {
			case "distances":
				distanceEntry = entry;
				break;
			case "ports":
				portEntry = entry;
				break;
			case "port-groups":
				portGroupsEntry = entry;
				break;
			case "bunker-fuels":
				bunkerFuelsEntry = entry;
				break;
			case "vessels":
				vesselsEntry = entry;
				break;
			case "vessel-groups":
				vesselGroupsEntry = entry;
				break;
			default:
				break;

			}
		}

		String msg = String.format("Import distance data into %s?", scenarioInstance.getName());

		Boolean ok = RunnerHelper.syncExecFunc(display -> MessageDialog.openConfirm(display.getActiveShell(), "Data import", msg));
		if (!ok) {
			return;
		}

		final Entry pDistanceEntry = distanceEntry;
		final Entry pPortEntry = portEntry;
		final Entry pPortGroupsEntry = portGroupsEntry;
		final Entry pVesselsEntry = vesselsEntry;
		final Entry pVesselGroupsEntry = vesselGroupsEntry;
		final Entry pBunkerFuelsEntry = bunkerFuelsEntry;
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		try (ModelReference modelReference = modelRecord.aquireReference(LingoDataImporter.class.getSimpleName())) {
			modelReference.executeWithLock(true, () -> {
				try {
					importDistances(pDistanceEntry, baseURI, uc, modelReference, false);
					importPorts(pPortEntry, baseURI, uc, modelReference);
					importPortGroups(pPortGroupsEntry, baseURI, uc, modelReference);
					importBunkerFuels(pBunkerFuelsEntry, baseURI, uc, modelReference);
					importVessels(pVesselsEntry, baseURI, uc, modelReference);
					importVesselGroups(pVesselGroupsEntry, baseURI, uc, modelReference);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	private void importDistances(final Entry entry, final String baseURI, final URIConverter uc, final ModelReference modelReference, boolean updatePortNames) throws IOException {
		if (entry == null) {
			return;
		}
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());
		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/" + entry.getPath()))) {
			final DistancesVersion version = mapper.readValue(inputStream, DistancesVersion.class);

			final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
			final EditingDomain editingDomain = modelReference.getEditingDomain();
			final Command command = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, version, updatePortNames);

			if (!command.canExecute()) {
				throw new RuntimeException("Unable to execute command");
			}
			RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
		}
	}

	private void importPorts(final Entry entry, final String baseURI, final URIConverter uc, final ModelReference modelReference) throws IOException {
		if (entry == null) {
			return;
		}
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());

		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/" + entry.getPath()))) {
			final PortsVersion version = mapper.readValue(inputStream, PortsVersion.class);

			final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
			final EditingDomain editingDomain = modelReference.getEditingDomain();
			final Command command = PortsToScenarioCopier.getUpdateCommand(editingDomain, portModel, version);

			if (!command.canExecute()) {
				throw new RuntimeException("Unable to execute command");
			}
			RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
		}
	}

	private void importVesselGroups(final Entry entry, final String baseURI, final URIConverter uc, final ModelReference modelReference) throws IOException {
		if (entry == null) {
			return;
		}
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());

		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/" + entry.getPath()))) {
			final VesselGroupsVersion version = mapper.readValue(inputStream, VesselGroupsVersion.class);

			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel((LNGScenarioModel) modelReference.getInstance());
			final EditingDomain editingDomain = modelReference.getEditingDomain();
			final Command command = VesselGroupsToScenarioImporter.getUpdateCommand(editingDomain, fleetModel, version);

			if (!command.canExecute()) {
				throw new RuntimeException("Unable to execute command");
			}
			RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
		}
	}

	private void importVessels(final Entry entry, final String baseURI, final URIConverter uc, final ModelReference modelReference) throws IOException {
		if (entry == null) {
			return;
		}
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());

		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/" + entry.getPath()))) {
			final VesselsVersion version = mapper.readValue(inputStream, VesselsVersion.class);

			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel((LNGScenarioModel) modelReference.getInstance());
			final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
			final EditingDomain editingDomain = modelReference.getEditingDomain();
			final Command command = VesselsToScenarioCopier.getUpdateCommand(editingDomain, fleetModel, portModel, version);

			if (!command.canExecute()) {
				throw new RuntimeException("Unable to execute command");
			}
			RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
		}
	}

	private void importBunkerFuels(final Entry entry, final String baseURI, final URIConverter uc, final ModelReference modelReference) throws IOException {
		if (entry == null) {
			return;
		}
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());

		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/" + entry.getPath()))) {
			final BunkerFuelsVersion version = mapper.readValue(inputStream, BunkerFuelsVersion.class);

			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel((LNGScenarioModel) modelReference.getInstance());
			final EditingDomain editingDomain = modelReference.getEditingDomain();
			final Command command = BunkerFuelsToScenarioImporter.getUpdateCommand(editingDomain, fleetModel, version);

			if (!command.canExecute()) {
				throw new RuntimeException("Unable to execute command");
			}
			RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
		}
	}

	private void importPortGroups(final Entry entry, final String baseURI, final URIConverter uc, final ModelReference modelReference) throws IOException {
		if (entry == null) {
			return;
		}
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());

		try (InputStream inputStream = uc.createInputStream(URI.createURI(baseURI + "/" + entry.getPath()))) {
			final PortGroupsVersion version = mapper.readValue(inputStream, PortGroupsVersion.class);

			final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
			final EditingDomain editingDomain = modelReference.getEditingDomain();
			final Command command = PortGroupsToScenarioImporter.getUpdateCommand(editingDomain, portModel, version);

			if (!command.canExecute()) {
				throw new RuntimeException("Unable to execute command");
			}
			RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));
		}
	}
}
