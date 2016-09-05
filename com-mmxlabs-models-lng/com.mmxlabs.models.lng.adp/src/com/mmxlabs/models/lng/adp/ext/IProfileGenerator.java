package com.mmxlabs.models.lng.adp.ext;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;

@NonNullByDefault
public interface IProfileGenerator {

	<T extends Slot> boolean canGenerate(ContractProfile<T> profile, SubContractProfile<T> subProfile);

	<T extends Slot> List<T> generateSlots(ADPModel model, ContractProfile<T> profile, SubContractProfile<T> subProfile, ISlotTemplateFactory factory);
}
