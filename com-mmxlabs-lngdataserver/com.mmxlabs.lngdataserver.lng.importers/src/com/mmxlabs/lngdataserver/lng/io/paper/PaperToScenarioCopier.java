/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.paper;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.paper.model.PaperVersion;
import com.mmxlabs.models.lng.pricing.PricingModel;

public class PaperToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaperToScenarioCopier.class);

	private PaperToScenarioCopier() {

	}

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final PricingModel pricingModel, final PaperVersion version) {
		// TODO
		return null;
	}
}
