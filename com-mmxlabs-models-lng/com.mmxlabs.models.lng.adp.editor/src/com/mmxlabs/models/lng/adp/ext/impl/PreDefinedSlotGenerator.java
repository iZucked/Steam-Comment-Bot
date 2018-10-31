/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.PreDefinedDate;
import com.mmxlabs.models.lng.adp.PreDefinedDistributionModel;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.TimePeriod;

@NonNullByDefault
public class PreDefinedSlotGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot> boolean canGenerate(final ContractProfile<T> profile, final SubContractProfile<T> subProfile) {
		return subProfile.getDistributionModel() instanceof PreDefinedDistributionModel;
	}

	@Override
	public <T extends Slot> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T> profile, final SubContractProfile<T> subProfile, final ISlotTemplateFactory factory) {

		final PreDefinedDistributionModel model = (PreDefinedDistributionModel) subProfile.getDistributionModel();
		final YearMonth start = adpModel.getYearStart();
		final List<T> slots = new LinkedList<>();

		int idx = 0;
		for (PreDefinedDate preDefinedDate : model.getDates()) {
			final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, preDefinedDate.getDate(), (d) -> d.plusDays(1), idx++);
			slot.setWindowStart(preDefinedDate.getDate());
			slot.setWindowSize(model.getWindowSize());
			slot.setWindowSizeUnits(model.getWindowSizeUnits());
			ADPModelUtil.setSlotVolumeFrom(model.getModelOrContractVolumePerCargo(), model.getModelOrContractVolumeUnit(), slot);
			slots.add(slot);
		}
		return slots;
	}
}
