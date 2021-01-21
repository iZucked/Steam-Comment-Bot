package com.mmxlabs.models.lng.scenario.importWizards.inventory.mull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
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

	@NonNull private final CommercialModel commercialModel;
	@NonNull private final MullSubprofile subprofile;

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

		CSVReader reader = null;
		try {
			reader = new FileCSVReader(new File(path), importHooksProvider.getCsvSeparator());
			final MullExtraImporter mullExtraImporter = new MullExtraImporter();
			final List<Pair<String, MullEntityRow>> importedMullEntityRows = mullExtraImporter.doImportMullEntityRows(reader, context);
			
			final Map<String, BaseLegalEntity> existingEntities = new HashMap<>();
			commercialModel.getEntities().forEach(entity -> existingEntities.put(entity.getName().toLowerCase(), entity));
			final List<MullEntityRow> mullEntityRows = importedMullEntityRows.stream() //
					.map(p -> {
						final MullEntityRow mullEntityRow = p.getSecond();
						if (p.getFirst() != null) {
							mullEntityRow.setEntity(existingEntities.get(p.getFirst().toLowerCase()));
						}
						return mullEntityRow;
					}) //
					.collect(Collectors.toList());
			final CompoundCommand cmd = new CompoundCommand("Import MULL entitlements");
			final List<MullEntityRow> oldMullEntityRows = subprofile.getEntityTable();
			if (!oldMullEntityRows.isEmpty()) {
				cmd.append(RemoveCommand.create(importHooksProvider.getEditingDomain(), subprofile, ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE, new LinkedList<>(oldMullEntityRows)));
			}
			if (!mullEntityRows.isEmpty()) {
				cmd.append(AddCommand.create(importHooksProvider.getEditingDomain(), subprofile, ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE, mullEntityRows));
			}
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
