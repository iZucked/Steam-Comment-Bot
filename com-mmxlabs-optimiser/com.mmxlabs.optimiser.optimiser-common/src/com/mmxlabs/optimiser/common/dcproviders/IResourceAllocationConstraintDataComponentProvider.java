/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} interface extension to provide {@link IResource} allocation constraints for sequence elements.
 * 
 * @author Simon Goodall
 * 
 */
public interface IResourceAllocationConstraintDataComponentProvider extends IDataComponentProvider {

	/**
	 * Returns the {@link Collection} of {@link IResource} objects that the given sequence element can be assigned to. If null is returned, then there is no restriction to the {@link IResource}s it
	 * can be assigned to.
	 * 
	 * @param element
	 * @return
	 */
	@Nullable
	Collection<@NonNull IResource> getAllowedResources(@NonNull final ISequenceElement element);

}