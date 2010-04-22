package com.mmxlabs.scheduler.optmiser.providers;

import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;

public interface ISequenceElementProviderEditor extends
		ISequenceElementProvider {

	void setSequenceElement(ICargo cargo, IPort port, ISequenceElement element);

}
