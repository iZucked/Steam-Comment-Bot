package com.mmxlabs.scheduler.optimiser.voyage;

import java.util.List;

import com.mmxlabs.optimiser.IAnnotatedSequence;
import com.mmxlabs.optimiser.IResource;

/**
 * A {@link IVoyagePlanAnnotator} uses {@link IVoyagePlan}s to annotate
 * {@link IAnnotatedSequence} objects.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IVoyagePlanAnnotator<T> {

	/**
	 * Annotate the {@link IAnnotatedSequence} from the list of
	 * {@link IVoyagePlan} objects.
	 * 
	 * @param resource
	 * @param plans
	 * @param annotatedSequence
	 */
	void annonateFromVoyagePlan(IResource resource, List<IVoyagePlan> plans,
			IAnnotatedSequence<T> annotatedSequence);

}
