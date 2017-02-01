package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;

@PerChainUnitScope
public class GuidedMoveHelperImpl implements IGuidedMoveHelper {

	@Inject
	private ISequencesManipulator sequencesManipulator;

	@Inject
	private @NonNull List<IConstraintChecker> constraintCheckers;

	@Override
	public boolean doesMovePassConstraints(final ISequences rawSequences) {

		// Do normal manipulation
		final ISequences movedFullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

		// Run through the constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(movedFullSequences, null)) {
				return false;
			}
		}

		return true;
	}
}
