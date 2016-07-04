/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Editor version of the {@link IElementDurationProvider} interface.
 * 
 * @author Simon Goodall
 * 
 */
public interface IElementDurationProviderEditor extends IElementDurationProvider {

	void setElementDuration(@NonNull ISequenceElement element, @NonNull IResource resource, int duration);

	void setDefaultValue(int defaultValue);

	void setElementDuration(@NonNull ISequenceElement element, int durationHours);
}
