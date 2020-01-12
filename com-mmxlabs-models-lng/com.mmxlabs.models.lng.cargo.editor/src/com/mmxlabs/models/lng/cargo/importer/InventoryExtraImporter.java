/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class InventoryExtraImporter implements IExtraModelImporter {
	public static final @NonNull String INVENTORY_KEY = "INVENTORY";
	public static final @NonNull String INVENTORY_NAME = "Inventories";

	private static final String KEY_INVENTORY_NAME = "facility";
	private static final String KEY_INVENTORY_PORT = "port";

	private static final String KEY_TYPE = "type";
	private static final String KEY_FLOW_TYPE_FEED = "feed";
	private static final String KEY_FLOW_TYPE_OFFTAKE = "offtake";
	private static final String KEY_CAPACITY = "capacity";

	final static @NonNull Map<String, String> inputs = new LinkedHashMap<>();

	static {
		inputs.put(INVENTORY_KEY, INVENTORY_NAME);
	}

	@Override
	public @NonNull Map<String, String> getRequiredInputs() {
		if (LicenseFeatures.isPermitted("features:inventory-model")) {
			return inputs;
		}
		return Collections.emptyMap();
	}

	@Override
	public void importModel(@NonNull final MMXRootObject rootObject, @NonNull final Map<String, CSVReader> inputs, @NonNull final IMMXImportContext context) {

		final CSVReader reader = inputs.get(INVENTORY_KEY);

		if (reader != null) {

			try {
				context.pushReader(reader);

				List<Inventory> inventories = doImportModel(reader, context);

				if (inventories == null) {
					return;
				}

				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
					final CargoModel cargoModel = lngScenarioModel.getCargoModel();

					if (cargoModel != null) {
						cargoModel.getInventoryModels().addAll(inventories);
					}
				}

			} catch (final IOException e) {
			} finally {
				try {
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
				context.popReader();
			}
		}
	}

	public List<Inventory> doImportModel(@NonNull CSVReader reader, @NonNull final IMMXImportContext context) throws IOException {
		if (!LicenseFeatures.isPermitted("features:inventory-model")) {
			return null;
		}

		LinkedList<Inventory> results = new LinkedList<>();

		final Map<String, Inventory> inventories = new HashMap<>();

		final Map<String, List<InventoryEventRow>> feeds = new HashMap<>();
		final Map<String, List<InventoryEventRow>> offtakes = new HashMap<>();
		final Map<String, List<InventoryCapacityRow>> capacities = new HashMap<>();

		final DefaultClassImporter importer = new DefaultClassImporter();

		Map<String, String> row;
		while ((row = reader.readRow(true)) != null) {
			if (row.containsKey(KEY_INVENTORY_PORT) && !row.get(KEY_INVENTORY_PORT).isEmpty()) {
				final Inventory inventory = CargoFactory.eINSTANCE.createInventory();
				inventory.setName(row.get(KEY_INVENTORY_NAME));
				inventory.setPort((Port) context.getNamedObject(row.get(KEY_INVENTORY_PORT), PortPackage.Literals.PORT));
				inventories.put(inventory.getName(), inventory);
			} else {
				final ImportResults importObject = importer.importObject(null, EcorePackage.eINSTANCE.getEObject(), row, context);
				final String inventory = row.get(KEY_INVENTORY_NAME);
				final String type = row.get(KEY_TYPE);
				if (KEY_FLOW_TYPE_FEED.equals(type)) {
					feeds.computeIfAbsent(inventory, k -> new LinkedList<>()).add((InventoryEventRow) importObject.importedObject);
				} else if (KEY_FLOW_TYPE_OFFTAKE.equals(type)) {
					offtakes.computeIfAbsent(inventory, k -> new LinkedList<>()).add((InventoryEventRow) importObject.importedObject);
				} else if (KEY_CAPACITY.equals(type)) {
					capacities.computeIfAbsent(inventory, k -> new LinkedList<>()).add((InventoryCapacityRow) importObject.importedObject);
				}
			}
		}
		for (final Inventory inventory : inventories.values()) {

			inventory.getFeeds().addAll(feeds.getOrDefault(inventory.getName(), Collections.emptyList()));
			inventory.getOfftakes().addAll(offtakes.getOrDefault(inventory.getName(), Collections.emptyList()));
			inventory.getCapacities().addAll(capacities.getOrDefault(inventory.getName(), Collections.emptyList()));

			results.add(inventory);
		}

		return results;

	}

	@Override
	public void exportModel(@NonNull final Map<String, Collection<Map<String, String>>> output, @NonNull final IMMXExportContext context) {

		if (!LicenseFeatures.isPermitted("features:inventory-model")) {
			return;
		}

		final List<Map<String, String>> rows = new LinkedList<>();

		final IScenarioDataProvider scenarioDataProvider = context.getScenarioDataProvider();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		for (final Inventory inventory : cargoModel.getInventoryModels()) {
			{
				final Map<String, String> row = new HashMap<>();
				row.put(KEY_INVENTORY_NAME, inventory.getName());
				final Port port = inventory.getPort();
				if (port != null) {
					row.put(KEY_INVENTORY_PORT, port.getName());
				}
				rows.add(row);
			}
			final DefaultClassImporter exporter = new DefaultClassImporter();
			{
				final Collection<Map<String, String>> exportObjects = exporter.exportObjects(inventory.getFeeds(), context);
				for (final Map<String, String> m : exportObjects) {
					m.put(KEY_INVENTORY_NAME, inventory.getName());
					m.put(KEY_TYPE, KEY_FLOW_TYPE_FEED);
					rows.add(m);
				}
			}
			{
				final Collection<Map<String, String>> exportObjects = exporter.exportObjects(inventory.getOfftakes(), context);
				for (final Map<String, String> m : exportObjects) {
					m.put(KEY_INVENTORY_NAME, inventory.getName());
					m.put(KEY_TYPE, KEY_FLOW_TYPE_OFFTAKE);
					rows.add(m);
				}
			}
			{
				final Collection<Map<String, String>> exportObjects = exporter.exportObjects(inventory.getCapacities(), context);
				for (final Map<String, String> m : exportObjects) {
					m.put(KEY_INVENTORY_NAME, inventory.getName());
					m.put(KEY_TYPE, KEY_CAPACITY);
					rows.add(m);
				}
			}
		}

		output.put(INVENTORY_KEY, rows);
	}
}