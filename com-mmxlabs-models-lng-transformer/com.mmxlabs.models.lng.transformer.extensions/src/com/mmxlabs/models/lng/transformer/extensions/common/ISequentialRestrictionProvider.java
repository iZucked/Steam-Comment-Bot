/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.common;

import java.util.Collection;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Base class for DCP objects which provide information about which {@link ISequenceElement} objects
 * are prohibited from preceding or following other ones.
 * 
 * @author Simon McGregor
 *
 */
public interface ISequentialRestrictionProvider  extends IDataComponentProvider {
	Collection<ISequenceElement> getRestrictedFollowerElements(ISequenceElement element);

	Collection<ISequenceElement> getRestrictedPrecedingElements(ISequenceElement element);
}
