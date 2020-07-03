/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV129 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 128;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 129;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final EObjectWrapper modelRoot = modelRecord.getModelRoot();

		final EObjectWrapper portModel = modelRoot.getRef("referenceModel").getRef("portModel");

		final List<EObjectWrapper> routes = portModel.getRefAsList("routes");
		// Clear virtual port
		if (routes != null) {
			routes.forEach(route -> route.unsetFeature("virtualPort"));
		}

		// Clear upstream id data model
		final List<EObjectWrapper> ports = portModel.getRefAsList("ports");
		if (ports != null) {
			final List<EObjectWrapper> virtualPorts = new LinkedList<>();
			for (final EObjectWrapper port : ports) {
				final EObjectWrapper location = port.getRef("location");
				if (location != null) {
					final String mmxid = location.getAttrib("mmxId");
					if ("L_V_Panam".equals(mmxid) || "L_V_SuezC".equals(mmxid)) {
						// Virtual port - remove it.
						virtualPorts.add(port);
					} else {
						// Clear upstream id data model
						location.unsetFeature("otherIdentifiers");
					}
				}
			}
		}

	}
}