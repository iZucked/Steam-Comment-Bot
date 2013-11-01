package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface INominatedVesselProviderEditor extends INominatedVesselProvider {

	void setNominatedVessel(@NonNull ISequenceElement element, @NonNull IVessel vessel);
}
