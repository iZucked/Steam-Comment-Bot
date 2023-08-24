/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.paper;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.paper.model.DatahubPaperDeal;
import com.mmxlabs.lngdataserver.integration.paper.model.PaperVersion;
import com.mmxlabs.models.lng.cargo.CargoModel;

public class PaperToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaperToScenarioCopier.class);

	private PaperToScenarioCopier() {

	}

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final CargoModel cargoModel, final PaperVersion version) {
		final CompoundCommand cmd = new CompoundCommand("Update paper deals");
		List<DatahubPaperDeal> papers = version.getPaperDeals();
		
		return cmd;
	}
}
