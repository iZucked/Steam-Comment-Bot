/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.impl.AbstractSlotTemplateFactory;
import com.mmxlabs.models.lng.adp.ext.impl.DistributionModelGeneratorUtil;
import com.mmxlabs.models.lng.cargo.Slot;

@NonNullByDefault
public class DefaultSlotTemplateFactory extends AbstractSlotTemplateFactory {

	@Override
	public <T extends Slot> T createSlot(final String templateID, final ContractProfile<T> profile, final SubContractProfile<T> subProfile) {

		return super.createSlot(templateID, profile, subProfile);
	}

	@Override
	public <T extends Slot> String generateName(final String templateID, final ContractProfile<T> profile, final SubContractProfile<T> subProfile, final YearMonth yearStart, final int cargoNumber) {

		final String contractShortName = DistributionModelGeneratorUtil.getContractShortName(profile, profile.getContract());
		if (profile.getSubProfiles().size() == 1) {
			return String.format("%2d-%s-%02d", yearStart.getYear() - 2000, contractShortName, 1 + cargoNumber);
		} else {
			final String code = subProfile.getName();
			return String.format("%2d-%s-%s-%02d", yearStart.getYear() - 2000, contractShortName, code, 1 + cargoNumber);
		}
	}
}
