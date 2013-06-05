/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.adapterfactories;

import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationHelper;

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

	private static IStatus validate(final IResource resource, final MMXRootObject rootObject) {
		final IValidator<EObject> validator = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);

		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);
		validator.setOption(IBatchValidator.OPTION_TRACK_RESOURCES, true);

		final ValidationHelper helper = new ValidationHelper();

		return helper.runValidation(validator, new DefaultExtraValidationContext(rootObject, false), Collections.singleton(rootObject));
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IStatus.class };
	}
}
