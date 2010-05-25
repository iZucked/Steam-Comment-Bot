package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import com.mmxlabs.optimiser.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;


//TODO: AmbiguousAPI. What about vargoes which visit the same port muliple times (e..g FOB?DES)
public interface ISequenceElementProvider extends IDataComponentProvider {

	// TODO: Required -- on opt.data object?
	List<ISequenceElement> getSequenceElements();

	ISequenceElement getSequenceElement(ICargo cargo, IPort port);
}
