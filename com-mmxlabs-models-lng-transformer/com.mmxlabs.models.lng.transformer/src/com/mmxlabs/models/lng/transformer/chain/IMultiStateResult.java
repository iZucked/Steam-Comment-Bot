package com.mmxlabs.models.lng.transformer.chain;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Results storing raw {@link ISequences} to {@link IAnnotatedSolution} pairings
 * 
 * @author Simon Goodall
 *
 */
public interface IMultiStateResult {

	@NonNull
	Pair<ISequences, IAnnotatedSolution> getBestSolution();

	@NonNull
	List<Pair<ISequences, IAnnotatedSolution>> getSolutions();
}