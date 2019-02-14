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

import com.google.common.util.concurrent.Monitor;
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
import com.mmxlabs.models.lng.types.TimePeriod;

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

		final YearMonth start = adpModel.getYearStart();

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
		default:
			throw new IllegalArgumentException();

		}

		Contract contract = profile.getContract();

		if (contract == null) {
			throw new IllegalStateException();
		}
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

		int idx = 0;
		while (date.isBefore(endDate)) {
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {

				final int numberOfCargoes = model.getQuantity();

				for (int i = 0; i < numberOfCargoes; ++i) {
					final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, nextDateGenerator, idx++);
					ADPModelUtil.setSlotVolumeFrom(model.getModelOrContractVolumePerCargo(), model.getModelOrContractVolumeUnit(), slot);
					if (model.getIntervalType() != IntervalType.MONTHLY) {
						if (model.getIntervalType() == IntervalType.QUARTERLY) {
							slot.setWindowSize(3);
							slot.setWindowSizeUnits(TimePeriod.MONTHS);
						} else if (model.getIntervalType() == IntervalType.WEEKLY) {
							slot.setWindowStart(date);
							slot.setWindowSize(7);
							slot.setWindowSizeUnits(TimePeriod.DAYS);
						} else if (model.getIntervalType() == IntervalType.YEARLY) {
							slot.setWindowStart(date);
							slot.setWindowSize(12);
							slot.setWindowSizeUnits(TimePeriod.MONTHS);
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
