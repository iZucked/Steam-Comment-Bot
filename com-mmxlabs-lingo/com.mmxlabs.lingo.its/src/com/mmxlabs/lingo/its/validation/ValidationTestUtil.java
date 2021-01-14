package com.mmxlabs.lingo.its.validation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;

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
	 * @param optimising           True to run the optimisation constraints, false
	 *                             to run evaluation constraints.
	 * @param relaxedValidation    True pretend this is a period optimisation
	 *                             scenario and reduce the number of constraints.
	 * @return
	 */
	public static IStatus validate(final IScenarioDataProvider scenarioDataProvider, final boolean optimising, final boolean relaxedValidation) {

		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter((constraint, target) -> {
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
	 * Recursively scan the IStatus for any DetailConstraintStatusDecorator instances
	 * with the given key.
	 * 
	 * @param status
	 * @param constraintKey
	 * @return
	 */
	public static List<DetailConstraintStatusDecorator> findStatus(@NonNull IStatus status, Object constraintKey) {

		List<DetailConstraintStatusDecorator> children = new LinkedList<>();

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
	private static void findStatus(final IStatus status, List<DetailConstraintStatusDecorator> l, Object constraintKey) {
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
}
