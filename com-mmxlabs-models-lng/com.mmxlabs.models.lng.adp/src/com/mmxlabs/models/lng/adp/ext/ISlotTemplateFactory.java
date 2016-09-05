package com.mmxlabs.models.lng.adp.ext;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;

public interface ISlotTemplateFactory {

	<T extends Slot> T createSlot(String templateID, ContractProfile<T> profile, SubContractProfile<T> subProfile);

	<T extends Slot> String generateName(String slotTemplateId, @NonNull ContractProfile<T> profile, @NonNull SubContractProfile<T> subProfile, YearMonth start, int i);

}
