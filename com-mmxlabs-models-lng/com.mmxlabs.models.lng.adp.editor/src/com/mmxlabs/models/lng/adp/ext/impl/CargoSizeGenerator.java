/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.CargoSizeDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

@NonNullByDefault
public class CargoSizeGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot> boolean canGenerate(final ContractProfile<T> profile, final SubContractProfile<T> subProfile) {
		return subProfile.getDistributionModel() instanceof CargoSizeDistributionModel;
	}

	@Override
	public <T extends Slot> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T> profile, final SubContractProfile<T> subProfile, final ISlotTemplateFactory factory) {

		final CargoSizeDistributionModel model = (CargoSizeDistributionModel) subProfile.getDistributionModel();
		final YearMonth start = adpModel.getYearStart();
		final List<T> slots = new LinkedList<>();

		Function<LocalDate, LocalDate> nextDateGenerator;
		if (profile.getVolumeUnit() != model.getVolumeUnit()) {
			// They must match!
			throw new IllegalArgumentException();
		}
		final int numberOfCargoes = (int) Math.round(profile.getTotalVolume() / model.getVolumePerCargo());

		if (numberOfCargoes == 12) {
			nextDateGenerator = date -> date.plusMonths(1);
		} else {
			nextDateGenerator = date -> date.plusDays(Math.max(1, (int) 365 / numberOfCargoes));
		}

		final Contract contract = profile.getContract();
		if (contract == null) {
			throw new IllegalStateException();
		}
		int idx = 0;

		YearMonth startMonth = adpModel.getYearStart();
		if (contract.isSetStartDate()) {
			final YearMonth contractStart = contract.getStartDate();
			if (contractStart.isAfter(startMonth)) {
				startMonth = contractStart;
			}
		}

		YearMonth endMonth = adpModel.getYearEnd();
		if (contract.isSetEndDate()) {
			final YearMonth contractEnd = contract.getEndDate();
			if (contractEnd.isBefore(endMonth)) {
				endMonth = contractEnd;
			}
		}

		LocalDate date = startMonth.atDay(1);
		final LocalDate endDate = endMonth.atDay(1);

		while (date.isBefore(endDate)) {
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {
				final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, nextDateGenerator, idx++);
				ADPModelUtil.setSlotVolumeFrom(model.getVolumePerCargo(), model.getVolumeUnit(), slot);
				slots.add(slot);
			}

			final LocalDate nextDate = nextDateGenerator.apply(date);
			date = nextDate;
		}
		return slots;
	}

}
