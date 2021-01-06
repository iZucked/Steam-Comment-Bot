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
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.types.VolumeUnits;

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
		slot.setWindowSize(subProfile.getWindowSize());
		slot.setWindowSizeUnits(subProfile.getWindowSizeUnits());
		return slot;
	}

	public static int getMinContractQuantityInUnits(Contract contract, LNGVolumeUnit desiredVolumeUnit) {
		int minQuantity = contract.getMinQuantity();
		VolumeUnits contractVolUnit = contract.getVolumeLimitsUnit();
		double cv = 22.3; //If sale contract, no CV, so have to use 22.3 as estimated value to be safe.
		
		//If purchase contract, must have CV.
		if (contract instanceof PurchaseContract) {
			if (((PurchaseContract) contract).isSetCargoCV()) {
				cv = ((PurchaseContract) contract).getCargoCV();
			}
			else {
				cv = (Double)contract.getUnsetValue(CommercialPackage.Literals.PURCHASE_CONTRACT__CARGO_CV);
			}
		}

		switch (desiredVolumeUnit) {
		case M3:
			switch (contractVolUnit) {
			case MMBTU:
				minQuantity = (int)Math.round(ADPModelUtil.convertMMBTUToM3(minQuantity, cv));
				break;
			}
			break;
		case MMBTU:
			switch (contractVolUnit) {
			case M3:
				minQuantity = (int)Math.round(ADPModelUtil.convertM3ToMMBTU(minQuantity, cv));
				break;
			}
			break;
		case MT:
			//How to convert?
			switch (contractVolUnit) {
			case M3:
				minQuantity = (int)Math.round(ADPModelUtil.convertM3toMT(minQuantity));
				break;
			case MMBTU:
				minQuantity = (int)Math.round(ADPModelUtil.convertM3toMT(ADPModelUtil.convertMMBTUToM3(minQuantity, cv)));
				break;
			}
			break;
		}
		
		return minQuantity;
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
