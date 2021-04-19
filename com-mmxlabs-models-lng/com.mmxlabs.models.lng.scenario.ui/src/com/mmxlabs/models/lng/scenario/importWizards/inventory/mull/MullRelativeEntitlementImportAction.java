/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.inventory.mull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.adp.importer.MullExtraImporter;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

public class MullRelativeEntitlementImportAction extends ImportAction {
	private static final Logger log = LoggerFactory.getLogger(SimpleImportAction.class);

	@NonNull
	private final CommercialModel commercialModel;
	@NonNull
	private final MullSubprofile subprofile;

	public MullRelativeEntitlementImportAction(final ImportHooksProvider iph, @NonNull final CommercialModel commercialModel, @NonNull final MullSubprofile subprofile) {
		super(iph);
		this.commercialModel = commercialModel;
		this.subprofile = subprofile;
	}

	@Override
	protected void doImportStages(DefaultImportContext context) {
		final String path = importHooksProvider.getImportFilePath();

		if (path == null) {
			return;
		}

		try (final CSVReader reader = new FileCSVReader(new File(path), importHooksProvider.getCsvSeparator())) {
			final MullExtraImporter mullExtraImporter = new MullExtraImporter();
			final List<Pair<String, MullEntityRow>> importedMullEntityRows = mullExtraImporter.doImportMullEntityRows(reader, context);

			final Map<String, BaseLegalEntity> existingEntities = new HashMap<>();
			commercialModel.getEntities().forEach(entity -> existingEntities.put(entity.getName().toLowerCase(), entity));

			final CompoundCommand cmd = new CompoundCommand("Import MULL entitlements");
			final List<MullEntityRow> oldMullEntityRows = subprofile.getEntityTable();
			final Map<BaseLegalEntity, MullEntityRow> existingMullEntityRows = new HashMap<>();
			oldMullEntityRows.forEach(row -> existingMullEntityRows.put(row.getEntity(), row));
			final Set<MullEntityRow> rowsToRetain = new HashSet<>();
			final Set<BaseLegalEntity> seenEntities = new HashSet<>();

			final List<MullEntityRow> newMullEntityRows = new LinkedList<>();
			for (final Pair<String, MullEntityRow> importedRow : importedMullEntityRows) {
				final String expectedEntity = importedRow.getFirst();
				if (expectedEntity == null) {
					context.addProblem(context.createProblem("Empty entity field.", false, false, false));
				} else {
					final BaseLegalEntity foundEntity = existingEntities.get(expectedEntity.toLowerCase());
					if (foundEntity != null) {
						if (seenEntities.contains(foundEntity)) {
							context.addProblem(context.createProblem(String.format("Duplicate entry for %s.", expectedEntity), false, false, false));
						} else {
							seenEntities.add(foundEntity);
						}
						final MullEntityRow importedMullEntityRow = importedRow.getSecond();
						final MullEntityRow existingMullEntityRow = existingMullEntityRows.get(foundEntity);
						if (existingMullEntityRow != null) {
							cmd.append(SetCommand.create(importHooksProvider.getEditingDomain(), existingMullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__INITIAL_ALLOCATION,
									importedMullEntityRow.getInitialAllocation()));
							cmd.append(SetCommand.create(importHooksProvider.getEditingDomain(), existingMullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT,
									importedMullEntityRow.getRelativeEntitlement()));
							rowsToRetain.add(existingMullEntityRow);
						} else {
							importedMullEntityRow.setEntity(foundEntity);
							newMullEntityRows.add(importedMullEntityRow);
						}
					} else {
						context.addProblem(context.createProblem(String.format("%s is not present in scenario.", expectedEntity), false, false, false));
					}
				}
			}

			final List<MullEntityRow> rowsToRemove = oldMullEntityRows.stream().filter(row -> !rowsToRetain.contains(row)).collect(Collectors.toList());
			if (!rowsToRemove.isEmpty()) {
				cmd.append(RemoveCommand.create(importHooksProvider.getEditingDomain(), subprofile, ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE, rowsToRemove));
			}
			if (!newMullEntityRows.isEmpty()) {
				cmd.append(AddCommand.create(importHooksProvider.getEditingDomain(), subprofile, ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE, newMullEntityRows));
			}
			if (context.getProblems().isEmpty()) {
				importHooksProvider.getEditingDomain().getCommandStack().execute(cmd);
			}
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
