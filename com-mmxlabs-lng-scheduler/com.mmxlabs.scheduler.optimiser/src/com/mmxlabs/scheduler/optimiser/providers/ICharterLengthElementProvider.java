package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEvent;
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public interface ICharterLengthElementProvider extends IDataComponentProvider {
	@Nullable ISequenceElement getCharterLengthLocationElement(ICharterLengthEvent event, IPort port);
	ICharterLengthEventPortSlot getOriginalCharterLengthSlot(ICharterLengthEventPortSlot portSlot);
}
