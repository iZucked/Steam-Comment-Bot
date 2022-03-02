/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class NominationsJSONGenerator {

	private NominationsJSONGenerator() {
		//Disallow construction of this helper class.
	}
	
	public static List<Nominations> createNominationsData(final IScenarioDataProvider scenarioDataProvider) {

		final List<Nominations> result = new ArrayList<>();
		if (scenarioDataProvider != null) {
			final LNGScenarioModel scenarioModel = ScenarioModelUtil.getScenarioModel(scenarioDataProvider);
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
			final List<AbstractNomination> nominationsDerivedFromSpecs = NominationsModelUtils.getGeneratedNominationsForAllDates(scenarioDataProvider);
			for (final AbstractNomination n : nominationsDerivedFromSpecs) {
				if (!n.isDone()) {
					final Nominations r = new Nominations(scenarioModel, n);
					result.add(r);
				}
			}

			final NominationsExportModel exportModel = new NominationsExportModel();
			exportModel.setNominations(result);
		}
		return result;
	}

}
