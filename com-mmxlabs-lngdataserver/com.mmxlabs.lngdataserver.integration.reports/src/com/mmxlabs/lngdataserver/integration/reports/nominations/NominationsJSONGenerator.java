/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.lngdataserver.integration.reports.nominations.Nominations.NominationType;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class NominationsJSONGenerator {
	public static List<Nominations> createNominationsData(final LNGScenarioModel scenarioModel) {
		
		final List<Nominations> result = new ArrayList<Nominations>();
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		assert cargoModel != null;
		for (final Cargo c : cargoModel.getCargoes()) {
			for (final Slot s : c.getSlots()) {
				//Only picking up the slots which are not done, and have a window nomination date
				if (!s.isWindowNominationIsDone() && s.getSlotOrDelegateWindowNominationDate() != null) {
					final Nominations r = new Nominations(s, NominationType.SLOT_DATE_0);
					result.add(r);
				}
				// here we add vessel nomination
				if (!s.isVesselNominationDone()
				&& s.getSlotOrDelegateVesselNominationDate() != null) {
					final Nominations r = new Nominations(s, NominationType.VESSEL);
					result.add(r);
				}
				// here we add volume nomination
				if (!s.isVolumeNominationDone()
				&& s.getSlotOrDelegateVolumeNominationDate() != null) {
					final Nominations r = new Nominations(s, NominationType.VOLUME);
					result.add(r);
				}
				// here we add port nomination
				if (!s.isPortNominationDone()
				&& s.getSlotOrDelegatePortNominationDate() != null) {
					final Nominations r = new Nominations(s, NominationType.PORT);
					result.add(r);
				}
				// 
				if (!s.isPortLoadNominationDone()
				&& s.getSlotOrDelegatePortLoadNominationDate() != null) {
					final Nominations r = new Nominations(s, NominationType.PORT_2);
					result.add(r);
				}
			}
		}
		final NominationsExportModel exportModel = new NominationsExportModel();
		exportModel.setNominations(result);
		return result;
	}

}
