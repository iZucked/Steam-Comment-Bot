/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.types.TimePeriod;

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

	public static String generateName(final ContractProfile<?, ?> profile, final Contract contract, final YearMonth start, final int i) {

		final String contractShortName = getContractShortName(profile, contract);
		return String.format("%2d-%s-%02d", start.getYear() - 2000, contractShortName, i);
	}

	public static String getContractShortName(final ContractProfile<?, ?> profile, final Contract contract) {

		String s = profile.getContractCode();
		if (s != null && !s.isEmpty()) {
			return s;
		}

		String contractShortName = contract.getName();
		contractShortName = contractShortName.toUpperCase();
		return contractShortName;
	}

	public static <T extends Slot<U>, U extends Contract> T generateSlot(ISlotTemplateFactory factory, ContractProfile<T, U> profile, 
			SubContractProfile<T, U> subProfile, YearMonth start, LocalDate date, int idx) {
		final String name = factory.generateName(subProfile.getSlotTemplateId(), profile, subProfile, start, idx);
		final T slot = factory.createSlot(subProfile.getSlotTemplateId(), profile, subProfile);
		if (slot instanceof LoadSlot) {
			LoadSlot loadSlot = (LoadSlot) slot;
			loadSlot.setDESPurchase(subProfile.getContractType() == ContractType.DES);
		} else if (slot instanceof DischargeSlot) {
			DischargeSlot dischargeSlot = (DischargeSlot) slot;
			dischargeSlot.setFOBSale(subProfile.getContractType() == ContractType.FOB);
		}
		slot.setName(name);
		// Whole month window based on given date
		slot.setWindowStart(date.withDayOfMonth(1));
		slot.setWindowStartTime(0);
		slot.setWindowSize(1);
		slot.setWindowSizeUnits(TimePeriod.MONTHS);
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
