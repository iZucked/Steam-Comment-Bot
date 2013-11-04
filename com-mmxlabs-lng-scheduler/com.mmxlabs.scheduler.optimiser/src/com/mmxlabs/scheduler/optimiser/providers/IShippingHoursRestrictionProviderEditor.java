package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;

public interface IShippingHoursRestrictionProviderEditor extends IShippingHoursRestrictionProvider {

	void setShippingHoursRestriction(@NonNull ISequenceElement element, int hours);

}
