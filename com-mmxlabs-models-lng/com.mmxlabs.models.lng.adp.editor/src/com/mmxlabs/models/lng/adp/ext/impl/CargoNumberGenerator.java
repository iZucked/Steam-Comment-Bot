/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

public class CargoNumberGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot<U>, U extends Contract> boolean canGenerate(final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile) {
		return subProfile.getDistributionModel() instanceof CargoNumberDistributionModel;
	}

	@Override
	public <T extends Slot<U>, U extends Contract> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile,
			final ISlotTemplateFactory factory) {

		final CargoNumberDistributionModel model = (CargoNumberDistributionModel) subProfile.getDistributionModel();
		final List<T> slots = new LinkedList<>();

		Contract contract = profile.getContract();
		if (contract == null) {
			throw new UserFeedbackException("No contract specified.");
		}
		
		final Pair<YearMonth, YearMonth> adpPeriod = ADPModelUtil.getContractProfilePeriod(adpModel, contract);
		if (adpPeriod == null) {
			throw new UserFeedbackException("No ADP period set.");
		}
		final YearMonth start = adpPeriod.getFirst();
		final YearMonth end = adpPeriod.getSecond();
		
		Function<LocalDate, LocalDate> nextDateGenerator;
		final int numberOfCargoes = model.getNumberOfCargoes();

		if (numberOfCargoes == 12) {
			nextDateGenerator = date -> date.plusMonths(1);
		} else {
			int days = Days.between(start, start.plusYears(1));
			nextDateGenerator = date -> {
				int spacing = (int) Math.round((double) days / (double) numberOfCargoes);
				return date.plusDays(Math.max(1, spacing));
			};
		}

		int idx = 0;
		LocalDate date = start.atDay(1);
		final LocalDate endDate = end.atDay(1);

		while (date.isBefore(endDate)) {
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {
				final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, nextDateGenerator, idx++);
				ADPModelUtil.setSlotVolumeFrom(contract.getMinQuantity(), model.getModelOrContractVolumePerCargo(), model.getModelOrContractVolumeUnit(), slot, false);
				slots.add(slot);
			}

			final LocalDate nextDate = nextDateGenerator.apply(date);
			date = nextDate;
		}
		return slots;
	}

}
