/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;

@NonNullByDefault
public interface ISlotTemplateFactory {

	<T extends Slot> T createSlot(String templateID, ContractProfile<T> profile, SubContractProfile<T> subProfile);

	<T extends Slot> String generateName(String slotTemplateId, ContractProfile<T> profile, SubContractProfile<T> subProfile, YearMonth month, int cargoNumber);

}
