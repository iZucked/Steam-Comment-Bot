package com.mmxlabs.models.lng.transformer.util;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequences;

// Temporary interface....
// Either requests a pre-computed sequences object, or reports the results.
public interface IRunnerHook {

	public static final String PHASE_INITIAL = "initial";
	public static final String PHASE_LSO = "lso";
	public static final String PHASE_HILL = "hill";
	public static final String PHASE_ACTION_SETS = "actionset";

	@Nullable
	ISequences getSequences(String phase);

	void reportSequences(String phase, @NonNull ISequences rawSequences);
}
