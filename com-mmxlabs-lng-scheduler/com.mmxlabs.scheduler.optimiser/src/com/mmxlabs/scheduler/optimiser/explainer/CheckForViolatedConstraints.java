/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.explainer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintInfoGetter;
import com.mmxlabs.scheduler.optimiser.constraints.impl.MinMaxSlotGroupConstraintChecker;

public class CheckForViolatedConstraints {

	@Inject
	private List<IConstraintChecker> constraintCheckers;

	public @NonNull ISequences run(@NonNull final ISequences rawSequences) {
		final List<@NonNull String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: run", this.getClass().getName()));
		} else {
			messages = null;
		}
		final List<IConstraintChecker> failedConstraints = new ArrayList<>();
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (checker instanceof MinMaxSlotGroupConstraintChecker && !checker.checkConstraints(rawSequences, null, messages)) {
				failedConstraints.add(checker);
			}
		}
		// TODO: we might want to log the failed messages?

		if (!failedConstraints.isEmpty()) {
			final List<Object> failedConstraintInfos = getConstraintInfo(rawSequences, failedConstraints);
			final String errorMessage = "Some ADP constraints cannot be satisfied without manual pairing or an ADP clean slate optimisation:\r\n\r\n";
			throw new UserFeedbackException(errorMessage, failedConstraintInfos);
		}
		return rawSequences;
	}

	private List<Object> getConstraintInfo(@NonNull final ISequences rawSequences, final List<IConstraintChecker> failedConstraints) {
		final List<Object> cis = new ArrayList<>();
		for (final IConstraintChecker fc : failedConstraints) {
			if (fc instanceof final IConstraintInfoGetter constraintInfoGetter) {
				cis.addAll(constraintInfoGetter.getFailedConstraintInfos(rawSequences, null));
			}
		}
		return cis;
	}
}
