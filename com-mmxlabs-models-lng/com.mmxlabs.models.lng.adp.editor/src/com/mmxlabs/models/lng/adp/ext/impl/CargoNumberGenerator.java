/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.common.time.Months;
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
		
		if (start == null || end == null) {
			throw new UserFeedbackException("Please specify both ADP start and end period.");
		}
		if (!end.isAfter(start)) {
			throw new UserFeedbackException("Please specify both ADP end period that is after start period.");
		}
		final int numberOfCargoes = model.getNumberOfCargoes();
		if (numberOfCargoes == 0) {
			throw new UserFeedbackException("Please specify number of cargoes to generate (greater than 0).");
		}
		
		double daysFromStart = 0.0;
		LocalDate startDate = start.atDay(1);
		int days = Days.between(start, end);
		double spacing = ((double) days / (double) numberOfCargoes);
		int generatedSlots = 0;
		
		int idx = 0;
		int i = 0;
		LocalDate date = startDate;
		final LocalDate endDate = end.atDay(1);

		int months = Months.between(startDate, endDate);
		boolean integerMultiples = numberOfCargoes % months == 0;
		int div = numberOfCargoes/months;

		while (date.isBefore(endDate) && generatedSlots < numberOfCargoes) {
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {
				final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, idx++);
				int minQuantity = DistributionModelGeneratorUtil.getMinContractQuantityInUnits(contract, model.getModelOrContractVolumeUnit());
				ADPModelUtil.setSlotVolumeFrom(minQuantity, model.getModelOrContractVolumePerCargo(), model.getModelOrContractVolumeUnit(), slot, false);
				slots.add(slot);
				generatedSlots++;
			}
			i++;
			if (integerMultiples) {
				if (i % div == 0) {
					date = date.plusMonths(1);
				}
			}
			else {
				daysFromStart += spacing;
				date = startDate.plusDays((long)Math.floor(daysFromStart));
			}
		}
		return slots;
	}
}
