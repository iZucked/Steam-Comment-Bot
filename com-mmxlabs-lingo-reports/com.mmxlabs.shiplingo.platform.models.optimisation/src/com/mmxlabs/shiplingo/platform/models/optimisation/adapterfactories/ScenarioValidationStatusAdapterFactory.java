/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.adapterfactories;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * {@link IAdapterFactory} to adapt a Resource to an IStatus indicating the validation status of the resource's scenario.
 * 
 * @author Tom Hinton
 * 
 */
public class ScenarioValidationStatusAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {

		MMXRootObject scenario = null;
		IResource resource = null;
		if (adaptableObject instanceof IResource) {

			resource = (IResource) adaptableObject;

			scenario = (MMXRootObject) resource.getAdapter(MMXRootObject.class);

		} else if (adaptableObject instanceof MMXRootObject) {
			scenario = (MMXRootObject) adaptableObject;
		}

		if (scenario == null) {
			return null;
		}

		return validate(resource, scenario);
	}

	private static IStatus validate(final IResource resource, final MMXRootObject scenario) {
		final IValidator<EObject> validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);

		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		final IStatus status = validator.validate(scenario);
		return status;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IStatus.class };
	}
}
