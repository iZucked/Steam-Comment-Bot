/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class NominationsJSONGenerator {

	public static List<Nominations> createNominationsData(final LNGScenarioModel scenarioModel) {

		final List<Nominations> result = new ArrayList<>();
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		final NominationsModel nominationsModel = scenarioModel.getNominationsModel();

		assert cargoModel != null;
		for (final AbstractNomination sn : nominationsModel.getNominations()) {
			if (!sn.isDone()) {
				final Nominations r = new Nominations(scenarioModel, sn);
				result.add(r);
			}
		}

		// Do nominations derived / lazily generated from nominations specs.
		final List<AbstractNomination> nominationsDerivedFromSpecs = NominationsModelUtils.generateNominationsForAllDates(scenarioModel);
		for (final AbstractNomination n : nominationsDerivedFromSpecs) {
			if (!n.isDone()) {
				final Nominations r = new Nominations(scenarioModel, n);
				result.add(r);
			}
		}

		final NominationsExportModel exportModel = new NominationsExportModel();
		exportModel.setNominations(result);
		return result;
	}

}
