package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEvent;
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public interface ICharterLengthElementProviderEditor extends ICharterLengthElementProvider {
	void setCharterLengthLocationElement(ICharterLengthEvent event, IPort port, ISequenceElement element);
	void setOriginalCharterLengthPortSlot(ICharterLengthEventPortSlot original, ICharterLengthEventPortSlot newPortSlot);
}
