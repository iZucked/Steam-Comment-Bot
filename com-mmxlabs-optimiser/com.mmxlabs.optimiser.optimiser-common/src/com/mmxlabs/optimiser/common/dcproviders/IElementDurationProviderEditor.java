/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Editor version of the {@link IElementDurationProvider} interface.
 * 
 * @author Simon Goodall
 * 
 */
public interface IElementDurationProviderEditor extends IElementDurationProvider {

	void setElementDuration(ISequenceElement element, IResource resource, int duration);

	void setDefaultValue(int defaultValue);

	void setElementDuration(ISequenceElement element, int durationHours);
}
