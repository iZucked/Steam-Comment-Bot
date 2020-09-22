/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.CargoSizeDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

public class CargoSizeGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot<U>, U extends Contract> boolean canGenerate(final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile) {
		return subProfile.getDistributionModel() instanceof CargoSizeDistributionModel;
	}

	@Override
	public <T extends Slot<U>, U extends Contract> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile,
			final ISlotTemplateFactory factory) {

		final CargoSizeDistributionModel model = (CargoSizeDistributionModel) subProfile.getDistributionModel();
		final List<T> slots = new LinkedList<>();

		BiFunction<LocalDate, Integer, LocalDate> nextDateGenerator;
		if (profile.getVolumeUnit() != model.getModelOrContractVolumeUnit()) {
			// They must match!
			throw new UserFeedbackException("Profile volume unit and slot or contract unit must match.");
		}
		final int numberOfCargoes = (int) Math.round(profile.getTotalVolume() / model.getModelOrContractVolumePerCargo());
		
		

		final Contract contract = profile.getContract();
		if (contract == null) {
			throw new UserFeedbackException("No contract set.");
		}
		int idx = 0;

		final Pair<YearMonth, YearMonth> adpPeriod = ADPModelUtil.getContractProfilePeriod(adpModel, contract);
		if (adpPeriod == null) {
			throw new UserFeedbackException("ADP period is not set.");
		}
		final YearMonth start = adpPeriod.getFirst();
		final YearMonth end = adpPeriod.getSecond();

		LocalDate startDate = start.atDay(1);
		final LocalDate endDate = end.atDay(1);
		
		int months = Months.between(startDate, endDate);
		if (numberOfCargoes % months == 0) {
			int div = numberOfCargoes/months;
			nextDateGenerator = (date, i) -> i % div == 0 ? date.plusMonths(1) : date;
		} else {
			int dateSpacing = Math.max(1, 365 / numberOfCargoes);
			nextDateGenerator = (date, i) -> date.plusDays(dateSpacing);
		}
		
		LocalDate date = startDate;
		int i = 0;
		while (date.isBefore(endDate)) {
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {
				final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, idx++);
				int minQuantity = DistributionModelGeneratorUtil.getMinContractQuantityInUnits(contract, model.getModelOrContractVolumeUnit());
				ADPModelUtil.setSlotVolumeFrom(minQuantity, model.getModelOrContractVolumePerCargo(), model.getModelOrContractVolumeUnit(), slot, model.isExact());
				slots.add(slot);
			}
			i++;
			final LocalDate nextDate = nextDateGenerator.apply(date, i);
			date = nextDate;
		}
		return slots;
	}

}
