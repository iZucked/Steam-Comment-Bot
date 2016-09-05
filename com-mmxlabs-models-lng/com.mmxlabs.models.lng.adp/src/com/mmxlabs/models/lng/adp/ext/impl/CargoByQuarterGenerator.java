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
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

@NonNullByDefault
public class CargoByQuarterGenerator implements IProfileGenerator {

	@Override
	public <T extends Slot> boolean canGenerate(final ContractProfile<T> profile, final SubContractProfile<T> subProfile) {
		return subProfile.getDistributionModel() instanceof CargoByQuarterDistributionModel;
	}

	@Override
	public <T extends Slot> List<T> generateSlots(final ADPModel adpModel, final ContractProfile<T> profile, final SubContractProfile<T> subProfile, final ISlotTemplateFactory factory) {

		final CargoByQuarterDistributionModel model = (CargoByQuarterDistributionModel) subProfile.getDistributionModel();
		final YearMonth start = adpModel.getYearStart();
		final List<T> slots = new LinkedList<>();

		final Function<LocalDate, LocalDate> nextDateGenerator = date -> date.plusMonths(3);

		LocalDate date = LocalDate.of(start.getYear(), start.getMonthValue(), 1);

		final Contract contract = profile.getContract();

		int idx = 0;
		for (int q = 0; q < 4; ++q) {
			final int numberOfCargoes = getNumberOfCargoes(model, q);
			if (DistributionModelGeneratorUtil.checkContractDate(contract, date)) {
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
