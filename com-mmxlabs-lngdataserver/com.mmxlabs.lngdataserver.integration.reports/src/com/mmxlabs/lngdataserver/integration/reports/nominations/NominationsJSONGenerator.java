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
				/**
				 * Code which will be used once/if we extend the model to include 
				 * numerous slot date narrowing
				 * vessel nomination is definitely required by M
				 * volume nomination is definitely required by L and RWE
				 * port nomination is definitely required by M
				// here we will second window narrowing nomination
				if (!s.isWindowNominationIsDone()
				&& s.getSlotOrDelegateWindowNominationDate() != null) {
					final RowData r = new RowData(scenario, s, pinned, NominationType.SLOT_DATE_1);
					result.add(r);
				}
				// here we add third window narrowing nomination
				if (!s.isWindowNominationIsDone()
				&& s.getSlotOrDelegateWindowNominationDate() != null) {
					final RowData r = new RowData(scenario, s, pinned, NominationType.SLOT_DATE_2);
					result.add(r);
				}
				// here we add vessel nomination
				if (!s.isWindowNominationIsDone()
				&& s.getSlotOrDelegateWindowNominationDate() != null) {
					if (s.getCargo() != null && s instanceof LoadSlot) {
						final RowData r = new RowData(scenario, s, pinned, NominationType.VESSEL);
						result.add(r);
					}
				}
				// here we add volume nomination
				if (!s.isWindowNominationIsDone()
				&& s.getSlotOrDelegateWindowNominationDate() != null) {
					final RowData r = new RowData(scenario, s, pinned, NominationType.VOLUME);
					result.add(r);
				}
				// here we add volume nomination
				if (!s.isWindowNominationIsDone()
				&& s.getSlotOrDelegateWindowNominationDate() != null) {
					final RowData r = new RowData(scenario, s, pinned, NominationType.PORT);
					result.add(r);
				}
				 */
			}
		}
		final NominationsExportModel exportModel = new NominationsExportModel();
		exportModel.setNominations(result);
		return result;
	}

}
