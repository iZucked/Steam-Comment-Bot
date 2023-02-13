/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

public class CargoIntervalGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot<U>, U extends Contract> boolean canGenerate(final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile) {
		return subProfile.getDistributionModel() instanceof CargoIntervalDistributionModel;
	}

	@Override
	public <T extends Slot<U>, U extends Contract> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T, U> profile, final SubContractProfile<T, U> subProfile,
			final ISlotTemplateFactory factory) {

		final CargoIntervalDistributionModel model = (CargoIntervalDistributionModel) subProfile.getDistributionModel();
		final List<T> slots = new LinkedList<>();

		if (model.getSpacing() == 0) {
			return slots;
		}

		Function<LocalDate, LocalDate> nextDateGenerator;
		switch (model.getIntervalType()) {
		case MONTHLY:
			nextDateGenerator = date -> date.plusMonths(1 * model.getSpacing());
			break;
		case QUARTERLY:
			nextDateGenerator = date -> date.plusMonths(3 * model.getSpacing());
			break;
		case WEEKLY:
			nextDateGenerator = date -> date.plusWeeks(1 * model.getSpacing());
			break;
		case BIMONTHLY:
			nextDateGenerator = date -> date.plusMonths(2 * model.getSpacing());
			break;
		case YEARLY:
			nextDateGenerator = date -> date.plusMonths(12 * model.getSpacing());
			break;
		default:
			throw new IllegalArgumentException();

		}

		Contract contract = profile.getContract();

		if (contract == null) {
			throw new IllegalStateException();
		}
		
		final Pair<YearMonth, YearMonth> adpPeriod = ADPModelUtil.getContractProfilePeriod(adpModel, contract);
		if (adpPeriod == null) {
			throw new IllegalStateException();
		}
		final YearMonth start = adpPeriod.getFirst();
		final YearMonth end = adpPeriod.getSecond();

		LocalDate date = start.atDay(1);
		final LocalDate endDate = end.atDay(1);

		int idx = 0;
		while (date.isBefore(endDate)) {
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {

				final int numberOfCargoes = model.getQuantity();

				for (int i = 0; i < numberOfCargoes; ++i) {
					final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, idx++);
					int minQuantity = DistributionModelGeneratorUtil.getMinContractQuantityInUnits(contract, model.getModelOrContractVolumeUnit());
					ADPModelUtil.setSlotVolumeFrom(minQuantity, model.getModelOrContractVolumePerCargo(), model.getModelOrContractVolumeUnit(), slot, false);
					if (model.getIntervalType() != IntervalType.MONTHLY) {
						if (model.getIntervalType() == IntervalType.QUARTERLY) {
						} else if (model.getIntervalType() == IntervalType.WEEKLY) {
							slot.setWindowStart(date);
						} else if (model.getIntervalType() == IntervalType.YEARLY) {
							slot.setWindowStart(date);
						} else if (model.getIntervalType() == IntervalType.BIMONTHLY) {
							slot.setWindowStart(date);
						}
					}
					slots.add(slot);

				}
			}
			final LocalDate nextDate = nextDateGenerator.apply(date);
			date = nextDate;
		}
		return slots;
	}
}
