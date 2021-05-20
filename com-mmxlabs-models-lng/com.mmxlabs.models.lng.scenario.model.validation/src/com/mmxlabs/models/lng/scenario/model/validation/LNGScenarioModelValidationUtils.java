package com.mmxlabs.models.lng.scenario.model.validation;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;

public class LNGScenarioModelValidationUtils {
	public static LocalDate getEarliestSlotDate(final @NonNull LNGScenarioModel lngScenarioModel) {
		LocalDate result = LocalDate.MAX;

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);

		LocalDate erl = result;

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			if (ls.getSchedulingTimeWindow().getStart() != null && ls.getSchedulingTimeWindow().getStart().toLocalDate().isBefore(erl)) {
				erl = ls.getSchedulingTimeWindow().getStart().toLocalDate();
			}
		}
		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			if (ds.getSchedulingTimeWindow().getStart() != null && ds.getSchedulingTimeWindow().getStart().toLocalDate().isBefore(erl)) {
				erl = ds.getSchedulingTimeWindow().getStart().toLocalDate();
			}
		}
		if (result.isAfter(erl)) {
			result = erl;
		}

		return result;
	}
}
