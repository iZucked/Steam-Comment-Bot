/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.paper;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.paper.model.DatahubPaperDeal;
import com.mmxlabs.lngdataserver.integration.paper.model.PaperVersion;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;

public class PaperToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaperToScenarioCopier.class);

	private PaperToScenarioCopier() {

	}

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final CargoModel cargoModel, final PaperVersion version) {
		final CompoundCommand cmd = new CompoundCommand("Update paper deals");
		
		// Existing papers
		List<PaperDeal> oldPapers = cargoModel.getPaperDeals();
		if (!oldPapers.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, oldPapers));
		}

		// New version
		List<DatahubPaperDeal> newPapers = version.getPaperDeals();		
		for (DatahubPaperDeal paper : newPapers) {
			// cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.PAPER_DEAL, existingFuel));
		}
		
		VersionRecord record = cargoModel.getPaperDealsVersionRecord();
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));
		return cmd;
	}
}
