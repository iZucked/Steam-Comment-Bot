/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public interface IShortCargoReturnElementProviderEditor extends IShortCargoReturnElementProvider {

	void setReturnElement(IResource resource, ISequenceElement loadElement, IPort port, ISequenceElement returnElement);
}
