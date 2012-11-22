package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * A {@link IDataComponentProvider} which provides a list of {@link ISequenceElement}s which are not permitted to follow or precede a given {@link ISequenceElement}
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public interface IRestrictedElementsProvider extends IDataComponentProvider {

	Collection<ISequenceElement> getRestrictedFollowerElements(ISequenceElement element);

	Collection<ISequenceElement> getRestrictedPrecedingElements(ISequenceElement element);
}
