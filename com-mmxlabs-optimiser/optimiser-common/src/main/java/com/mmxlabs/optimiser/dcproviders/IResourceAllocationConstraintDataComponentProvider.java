package com.mmxlabs.optimiser.dcproviders;

import java.util.Collection;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} interface extension to provide
 * {@link IResource} allocation constraints for sequence elements.
 * 
 * @author Simon Goodall
 * 
 */
public interface IResourceAllocationConstraintDataComponentProvider extends
		IDataComponentProvider {

	/**
	 * Returns the {@link Collection} of {@link IResource} objects that the
	 * given sequence element can be assigned to. If null is returned, then
	 * there is no restriction to the {@link IResource}s it can be assigned to.
	 * 
	 * @param element
	 * @return
	 */
	// TODO: Should this be templated?
	public abstract Collection<IResource> getAllowedResources(
			final Object element);

	public abstract void setAllowedResources(final Object element,
			final Collection<IResource> resources);

}