/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.common.dcproviders;

import com.mmxlabs.optimiser.core.IResource;

/**
 * Editor version of the {@link IElementDurationProvider} interface.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IElementDurationProviderEditor<T> extends
		IElementDurationProvider<T> {

	void setElementDuration(T element, IResource resource, int duration);

	void setDefaultValue(int defaultValue);

	void setElementDuration(T element, int durationHours);
}
