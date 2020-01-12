/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import java.time.YearMonth;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

public interface ISlotTemplateFactory {

	<T extends Slot<U>, U extends Contract> T createSlot(String templateID, ContractProfile<T, U> profile, SubContractProfile<T, U> subProfile);

	<T extends Slot<U>, U extends Contract> String generateName(String slotTemplateId, ContractProfile<T, U> profile, SubContractProfile<T, U> subProfile, YearMonth month, int cargoNumber);

}
