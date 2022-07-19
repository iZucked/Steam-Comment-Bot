/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.inventory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.importer.InventoryExtraImporter;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

/**
 * @author achurchill
 * 
 */
public class InventoryImportAction extends ImportAction {
	private static final Logger LOG = LoggerFactory.getLogger(InventoryImportAction.class);

	private final CargoModel cargoModel;

	public InventoryImportAction(final ImportHooksProvider iph, final CargoModel cargoModel) {
		super(iph);
		this.cargoModel = cargoModel;
	}

	@Override
	protected void doImportStages(final DefaultImportContext context) {

		final String path = importHooksProvider.getImportFilePath();

		if (path == null) {
			return;
		}

		CSVReader reader = null;
		try {
			reader = new FileCSVReader(new File(path), importHooksProvider.getCsvSeparator());
			final InventoryExtraImporter importer = new InventoryExtraImporter();
			final Map<String, Map<String, List<InventoryEventRow>>> eventRows = importer.doImportInventoryEvents(reader, context);

//			final List<Inventory> models = importer.doImportModel(reader, context);
			final CompoundCommand cmd = new CompoundCommand("Import inventories");
			final Map<String, Inventory> existingInventories = new HashMap<>();
			for (final Inventory inventory : cargoModel.getInventoryModels()) {
				existingInventories.put(inventory.getName().toLowerCase(), inventory);
			}

			for (Entry<String, Map<String, List<InventoryEventRow>>> entry : eventRows.entrySet()) {
				Inventory inventory = existingInventories.get(entry.getKey().toLowerCase());
				if (inventory != null) {
					for (Entry<String, List<InventoryEventRow>> entry2 : entry.getValue().entrySet()) {
						final String type = entry2.getKey();
						final Object feature;
						final List<InventoryEventRow> oldEventRows;
						if (type.equals("feed")) {
							feature = CargoPackage.Literals.INVENTORY__FEEDS;
							oldEventRows = inventory.getFeeds();
						} else {
							feature = CargoPackage.Literals.INVENTORY__OFFTAKES;
							oldEventRows = inventory.getOfftakes();
						}
						if (!oldEventRows.isEmpty()) {
							cmd.append(RemoveCommand.create(importHooksProvider.getEditingDomain(), inventory, feature, new LinkedList<>(oldEventRows)));
						}
						if (!entry2.getValue().isEmpty()) {
							cmd.append(AddCommand.create(importHooksProvider.getEditingDomain(), inventory, feature, entry2.getValue()));
						}
					}
				}
			}
//			
//			cmd.append(
//					RemoveCommand.create(importHooksProvider.getEditingDomain(), cargoModel, CargoPackage.Literals.CARGO_MODEL__INVENTORY_MODELS, new LinkedList<>(cargoModel.getInventoryModels())));
//			cmd.append(AddCommand.create(importHooksProvider.getEditingDomain(), cargoModel, CargoPackage.Literals.CARGO_MODEL__INVENTORY_MODELS, models));
			importHooksProvider.getEditingDomain().getCommandStack().execute(cmd);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException e) {
			}
		}
	}
}
