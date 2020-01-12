/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.inventory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
import com.mmxlabs.models.lng.cargo.importer.InventoryExtraImporter;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

/**
 * @author achurchill
 * 
 */
public class InventoryImportAction extends ImportAction {
	private static final Logger log = LoggerFactory.getLogger(SimpleImportAction.class);
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
			final List<Inventory> models = importer.doImportModel(reader, context);
			final CompoundCommand cmd = new CompoundCommand("Inport inventories");
			cmd.append(
					RemoveCommand.create(importHooksProvider.getEditingDomain(), cargoModel, CargoPackage.Literals.CARGO_MODEL__INVENTORY_MODELS, new LinkedList<>(cargoModel.getInventoryModels())));
			cmd.append(AddCommand.create(importHooksProvider.getEditingDomain(), cargoModel, CargoPackage.Literals.CARGO_MODEL__INVENTORY_MODELS, models));
			importHooksProvider.getEditingDomain().getCommandStack().execute(cmd);
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
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
