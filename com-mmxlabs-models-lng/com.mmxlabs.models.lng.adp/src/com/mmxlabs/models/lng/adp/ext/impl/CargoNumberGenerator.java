package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

@NonNullByDefault
public class CargoNumberGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot> boolean canGenerate(final ContractProfile<T> profile, final SubContractProfile<T> subProfile) {
		return subProfile.getDistributionModel() instanceof CargoNumberDistributionModel;
	}

	@Override
	public <T extends Slot> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T> profile, final SubContractProfile<T> subProfile, final ISlotTemplateFactory factory) {

		final CargoNumberDistributionModel model = (CargoNumberDistributionModel) subProfile.getDistributionModel();
		final YearMonth start = adpModel.getYearStart();
		final List<T> slots = new LinkedList<>();

		Function<LocalDate, LocalDate> nextDateGenerator;
		final int numberOfCargoes = model.getNumberOfCargoes();

		if (numberOfCargoes == 12) {
			nextDateGenerator = date -> date.plusMonths(1);
		} else {
			nextDateGenerator = date -> date.plusDays(Math.max(1, (int) 365 / numberOfCargoes));
		}
		Contract contract = profile.getContract();
		int idx = 0;
		LocalDate date = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
		for (int i = 0; i < numberOfCargoes; ++i) {
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {
				final T slot = DistributionModelGeneratorUtil.generateSlot(factory, profile, subProfile, start, date, nextDateGenerator, idx++);
				slots.add(slot);
			}

			final LocalDate nextDate = nextDateGenerator.apply(date);
			date = nextDate;
		}
		return slots;
	}

}
