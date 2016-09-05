package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

public class DistributionModelGeneratorUtil {

	public static <T> T generateProfile(final Function<ISlotTemplateFactory, T> func) {
		final Bundle bundle = FrameworkUtil.getBundle(DistributionModelGeneratorUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<ISlotTemplateFactory> serviceReference = bundleContext.getServiceReference(ISlotTemplateFactory.class);

		try {
			final ISlotTemplateFactory factory = bundleContext.getService(serviceReference);
			return func.apply(factory);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static String generateName(final ContractProfile<?> profile, final Contract contract, final YearMonth start, final int i) {

		final String contractShortName = getContractShortName(profile, contract);
		return String.format("%2d-%s-%02d", start.getYear() - 2000, contractShortName, i);
	}

	public static String getContractShortName(final ContractProfile<?> profile, final Contract contract) {

		String s = profile.getContractCode();
		if (s != null && !s.isEmpty()) {
			return s;
		}

		String contractShortName = contract.getName();
		if (contractShortName.length() > 3) {
			contractShortName = contractShortName.substring(0, 3);
		}
		contractShortName = contractShortName.toUpperCase();
		return contractShortName;
	}

	public static <T extends Slot> T generateSlot(ISlotTemplateFactory factory, ContractProfile<T> profile, SubContractProfile<T> subProfile, YearMonth start, LocalDate date,
			Function<LocalDate, LocalDate> nextDateGenerator, int idx) {
		final String name = factory.generateName(subProfile.getSlotTemplateId(), profile, subProfile, start, idx);
		final T slot = factory.createSlot(subProfile.getSlotTemplateId(), profile, subProfile);
		slot.setName(name);
		slot.setWindowStart(date);

		final LocalDate nextDate = nextDateGenerator.apply(date);
		final int windowSize = Hours.between(date, nextDate);
		slot.setWindowSize(windowSize);

		return slot;
	}

	public static boolean checkContractDate(@NonNull Contract contract, @NonNull LocalDate date) {
		if (contract.getStartDate() != null) {
			if (date.isBefore(contract.getStartDate().atDay(1))) {
				return false;
			}
		}
		if (contract.getEndDate() != null) {
			if (!date.isBefore(contract.getEndDate().atDay(1))) {
				return false;
			}
		}
		return true;
	}
}
