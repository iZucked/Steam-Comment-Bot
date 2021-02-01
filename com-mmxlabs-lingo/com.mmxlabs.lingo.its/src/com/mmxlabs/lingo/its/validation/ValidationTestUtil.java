/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.validation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.its.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ValidationTestUtil {

	/**
	 * Run the validation on the scenarios.
	 * 
	 * @param scenarioDataProvider
	 * @param optimising
	 *            True to run the optimisation constraints, false to run evaluation constraints.
	 * @param relaxedValidation
	 *            True pretend this is a period optimisation scenario and reduce the number of constraints.
	 * @return
	 */
	public static IStatus validate(final IScenarioDataProvider scenarioDataProvider, final boolean optimising, final boolean relaxedValidation) {
		return validate(scenarioDataProvider, optimising, relaxedValidation, Collections.emptySet());
	}

	public static IStatus validate(final IScenarioDataProvider scenarioDataProvider, final boolean optimising, final boolean relaxedValidation, final Set<String> onlyConstraints) {

		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter((constraint, target) -> {

			// If the set is populated, then we only want to target specific constraint ids.
			if (!onlyConstraints.isEmpty() && !onlyConstraints.contains(constraint.getId())) {
				return false;
			}

			for (final Category cat : constraint.getCategories()) {
				if (cat.getId().endsWith(".base")) {
					return true;
				} else if (optimising && cat.getId().endsWith(".optimisation")) {
					return true;
				} else if (!optimising && cat.getId().endsWith(".evaluation")) {
					return true;
				}
			}

			return false;
		});

		final MMXRootObject root = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, relaxedValidation);
			return helper.runValidation(validator, extraContext, root);
		});

		return status;
	}

	/**
	 * Recursively scan the IStatus for any DetailConstraintStatusDecorator instances with the given key.
	 * 
	 * @param status
	 * @param constraintKey
	 * @return
	 */
	public static List<DetailConstraintStatusDecorator> findStatus(@NonNull final IStatus status, final Object constraintKey) {

		final List<DetailConstraintStatusDecorator> children = new LinkedList<>();

		if (status.isMultiStatus()) {
			findStatus(status, children, constraintKey);
		}

		return children;
	}

	/**
	 * Check status message applies to this editor.
	 * 
	 * @param status
	 * @return
	 */
	private static void findStatus(final IStatus status, final List<DetailConstraintStatusDecorator> l, final Object constraintKey) {
		if (status.isMultiStatus()) {
			final IStatus[] children = status.getChildren();
			for (final IStatus element : children) {
				findStatus(element, l, constraintKey);
			}
		}

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator element = (DetailConstraintStatusDecorator) status;
			if (element.getConstraintKey() == constraintKey) {
				l.add(element);
			}
		}
	}

	/**
	 * Clear out other validation errors, e.g. validation constraints may fail and get disabled, so do not abort this test. Note! This may mean a bug in the other validation constraints which is being
	 * ignored.
	 * 
	 * @param status
	 * @return
	 */
	public static IStatus retainDetailConstraintStatus(@Nullable final IStatus status) {
		if (status == null) {
			return null;
		}
		final List<IDetailConstraintStatus> childMessages = new LinkedList<>();
		extractChildren(status, childMessages);

		int code = IStatus.OK;
		for (final IStatus s : childMessages) {
			if (s.getSeverity() > code) {
				code = s.getSeverity();
			}
		}
		return new MultiStatus(Activator.PLUGIN_ID, code, childMessages.toArray(new IStatus[0]), "MultiError", null);
	}

	/**
	 * Recurse through the IStatus object adding any IDetailConstraintStatus to the list
	 * 
	 * @param status
	 * @param childMessages
	 */
	private static void extractChildren(final IStatus status, final List<IDetailConstraintStatus> childMessages) {

		if (status.isMultiStatus()) {
			for (final IStatus s : status.getChildren()) {
				extractChildren(s, childMessages);
			}
		}
		if (status instanceof IDetailConstraintStatus) {
			childMessages.add((IDetailConstraintStatus) status);
		}
	}

	/**
	 * Dump all nested messages to the console.
	 * 
	 * @param status
	 */
	public static void dumpStatusMessages(@Nullable final IStatus status) {
		if (status == null) {
			return;
		}
		if (status.isMultiStatus()) {
			for (final IStatus s : status.getChildren()) {
				dumpStatusMessages(s);
			}
		}
		if (!status.isOK()) {
			System.out.println(status.getMessage());
		}
	}

}
