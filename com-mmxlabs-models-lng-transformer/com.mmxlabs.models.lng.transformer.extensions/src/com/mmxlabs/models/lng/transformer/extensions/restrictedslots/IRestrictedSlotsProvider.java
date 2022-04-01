/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedslots;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * A {@link IDataComponentProvider} which provides a list of {@link ISequenceElement}s which are not permitted to follow or precede a given {@link ISequenceElement}
 * 
 * @author Simon Goodall
 */
@NonNullByDefault
public interface IRestrictedSlotsProvider extends IDataComponentProvider {

	Collection<ISequenceElement> getRestrictedFollowerElements(ISequenceElement element);

	Collection<ISequenceElement> getRestrictedPrecedingElements(ISequenceElement element);
}
