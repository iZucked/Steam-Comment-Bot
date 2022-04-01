/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

public class CargoByQuarterGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot<U>, U extends Contract> boolean canGenerate(final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile) {
		return subProfile.getDistributionModel() instanceof CargoByQuarterDistributionModel;
	}

	@Override
	public <T extends Slot<U>, U extends Contract> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile,
			final ISlotTemplateFactory factory) {

		final CargoByQuarterDistributionModel model = (CargoByQuarterDistributionModel) subProfile.getDistributionModel();
		final List<T> slots = new LinkedList<>();

		final Function<LocalDate, LocalDate> nextDateGenerator = date -> date.plusMonths(3);

		final Contract contract = profile.getContract();
		if (contract == null) {
			throw new IllegalStateException();
		}
		
		final Pair<YearMonth, YearMonth> adpPeriod = ADPModelUtil.getContractProfilePeriod(adpModel, contract);
		if (adpPeriod == null) {
			throw new IllegalStateException();
		}
		
		final YearMonth start = adpPeriod.getFirst();
		final YearMonth end = adpPeriod.getSecond();

		LocalDate date = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
		final LocalDate endDate = end.atDay(1);

		int idx = 0;
		while (date.isBefore(endDate)) {
			final int q;
			switch (date.getMonth()) {
			case JANUARY:
			case FEBRUARY:
			case MARCH:
				q = 0;
				break;
			case APRIL:
			case MAY:
			case JUNE:
				q = 1;
				break;
			case JULY:
			case AUGUST:
			case SEPTEMBER:
				q = 2;
				break;
			case OCTOBER:
			case NOVEMBER:
			case DECEMBER:
				q = 3;
				break;
			default:
				throw new IllegalStateException();
			}
			final int numberOfCargoes = getNumberOfCargoes(model, q);
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {
				for (int i = 0; i < numberOfCargoes; ++i) {
					final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, idx++);
					int minQuantity = DistributionModelGeneratorUtil.getMinContractQuantityInUnits(contract, model.getModelOrContractVolumeUnit());
					ADPModelUtil.setSlotVolumeFrom(minQuantity, model.getModelOrContractVolumePerCargo(), model.getModelOrContractVolumeUnit(), slot, false);
					slots.add(slot);
				}
			}

			final LocalDate nextDate = nextDateGenerator.apply(date);
			date = nextDate;
		}
		return slots;
	}

	private int getNumberOfCargoes(final CargoByQuarterDistributionModel model, final int q) {
		switch (q) {
		case 0:
			return model.getQ1();
		case 1:
			return model.getQ2();
		case 2:
			return model.getQ3();
		case 3:
			return model.getQ4();
		}
		throw new IllegalArgumentException();
	}

}
