package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * The {@link IAlternativeElementProvider} is a {@link IDataComponentProvider} interface defining the possibility of a mutually exclusive alternative {@link ISequenceElement}. An
 * {@link ISequenceElement} may have at most one alternative element that can be used instead of it.
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public interface IAlternativeElementProvider extends IDataComponentProvider {

	boolean hasAlternativeElement(ISequenceElement element);

	ISequenceElement getAlternativeElement(ISequenceElement element);
}
