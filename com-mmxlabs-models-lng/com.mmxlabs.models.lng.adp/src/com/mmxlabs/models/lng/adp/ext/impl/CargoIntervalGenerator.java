package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel;
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

@NonNullByDefault
public class CargoIntervalGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot> boolean canGenerate(final ContractProfile<T> profile, final SubContractProfile<T> subProfile) {
		return subProfile.getDistributionModel() instanceof CargoIntervalDistributionModel;
	}

	@Override
	public <T extends Slot> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T> profile, final SubContractProfile<T> subProfile, final ISlotTemplateFactory factory) {

		final CargoIntervalDistributionModel model = (CargoIntervalDistributionModel) subProfile.getDistributionModel();
		final YearMonth start = adpModel.getYearStart();
		final List<T> slots = new LinkedList<>();

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

		LocalDate date = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
		int idx = 0;
		Contract contract = profile.getContract();
		while (date.isBefore(start.plusYears(1).atDay(1))) {
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {

				final int numberOfCargoes = model.getQuantity();

				for (int i = 0; i < numberOfCargoes; ++i) {

					final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, nextDateGenerator, idx++);
					slots.add(slot);

				}
			}
			final LocalDate nextDate = nextDateGenerator.apply(date);
			date = nextDate;
		}
		return slots;
	}
}
