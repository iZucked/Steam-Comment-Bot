package com.mmxlabs.optimiser.dcproviders;

import com.mmxlabs.optimiser.IResource;

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
}
