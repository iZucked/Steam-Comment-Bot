/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import java.util.List;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

public interface IProfileGenerator {

	<T extends Slot<U>, U extends Contract> boolean canGenerate(ContractProfile<T, U> profile, SubContractProfile<T, U> subProfile);

	<T extends Slot<U>, U extends Contract> List<T> generateSlots(ADPModel model, ContractProfile<T, U> profile, SubContractProfile<T, U> subProfile, ISlotTemplateFactory factory);
}