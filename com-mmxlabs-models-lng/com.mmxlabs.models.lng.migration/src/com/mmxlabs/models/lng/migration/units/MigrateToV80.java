/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV80 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 79;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 80;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		EObjectWrapper analysisModel = model.getRef("analyticsModel");

		List<EObjectWrapper> optimisations = analysisModel.getRefAsList("optimisations");
		List<EObjectWrapper> insertionOptions = analysisModel.getRefAsList("insertionOptions");

		if (insertionOptions != null) {
			List<EObjectWrapper> newOptimisations = new LinkedList<>();
			if (optimisations != null) {
				newOptimisations.addAll(optimisations);
			}
			for (EObjectWrapper option : insertionOptions) {
				option.setRef("options", option.getRefAsList("insertionOptions"));
				option.unsetFeature("insertionOptions");

				final List<String> names = new LinkedList<>();
				List<EObjectWrapper> targets1 = option.getRefAsList("slotsInserted");
				List<EObjectWrapper> targets2 = option.getRefAsList("eventsInserted");

				if (targets1 != null) {
					targets1.forEach(o -> names.add(o.getAttrib("name")));
				}
				if (targets2 != null) {
					targets2.forEach(o -> names.add(o.getAttrib("name")));
				}

				option.setAttrib("name", "Inserting: " + Joiner.on(", ").join(names));

				newOptimisations.add(option);
			}
			analysisModel.setRef("optimisations", newOptimisations);
		}
		analysisModel.unsetFeature("insertionOptions");
	}
}
