package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

public interface ISequenceElementProviderEditor extends
		ISequenceElementProvider {

	void setSequenceElement(ICargo cargo, IPort port, ISequenceElement element);

}
